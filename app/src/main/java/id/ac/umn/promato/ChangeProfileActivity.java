package id.ac.umn.promato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangeProfileActivity extends AppCompatActivity {
    ImageView userImageView;
    Button btnCamera, btnGallery;
    FirebaseAuth mAuth;
    String hashedEmail;
    int SELECT_PHOTO = 1;
    Uri uri;
    StorageReference storageReference;
    Bitmap bitmapTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        userImageView = findViewById(R.id.userImageView);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userEmail = preferences.getString("useremail", "");
        hashedEmail = md5(userEmail);
        StorageReference profileReference = FirebaseStorage.getInstance().getReference().child(hashedEmail+".jpg");

        try {
            final File localFile = File.createTempFile(hashedEmail, "jpg");
            profileReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmapTemp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    userImageView.setImageBitmap(bitmapTemp);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(this).load(bitmapTemp).placeholder(R.drawable.ic_placeholder).into(userImageView);

        btnCamera.setOnClickListener(view -> {
            if(checkAndRequestPermissions()) {
                takePhotoWithCamera();
            }
        });

        btnGallery.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_PHOTO);
        });
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child(hashedEmail+".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ChangeProfileActivity.this, "Changes Saved.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeProfileActivity.this, "Save Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImageToFirebaseCam(byte[] bb) {
        StorageReference fileRef = storageReference.child(hashedEmail+".jpg");
        fileRef.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ChangeProfileActivity.this, "Changes Saved.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeProfileActivity.this, "Save Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void takePhotoWithCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, 2);
        }
    }

    private boolean checkAndRequestPermissions() {
        if(Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(ChangeProfileActivity.this, Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(ChangeProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePhotoWithCamera();
        } else {
            Toast.makeText(ChangeProfileActivity.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                userImageView.setImageBitmap(bitmap);
                Glide.with(this).load(bitmap).placeholder(R.drawable.ic_placeholder).into(userImageView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadImageToFirebase(uri);
        } else if(requestCode == 2 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmapImage = (Bitmap) bundle.get("data");
            userImageView.setImageBitmap(bitmapImage);
            Glide.with(this).load(bitmapImage).placeholder(R.drawable.ic_placeholder).into(userImageView);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            byte bb[] = bytes.toByteArray();

            uploadImageToFirebaseCam(bb);
        }
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}