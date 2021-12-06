package id.ac.umn.promato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SettingsActivity extends AppCompatActivity {
    TextView tvUserName;
    TextView tvUserEmail;
    ImageView userImageView;
    Button btnSignOut, btnChangeProfile;
    FirebaseAuth mAuth;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String hashedEmail;
    Bitmap bitmapTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tvUserName = findViewById(R.id.userName);
        tvUserEmail = findViewById(R.id.userEmail);
        userImageView = findViewById(R.id.userImageView);
        btnChangeProfile = findViewById(R.id.btnChangeProfile);
        btnSignOut = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userName = preferences.getString("username", "");
        String userEmail = preferences.getString("useremail", "");
        hashedEmail = md5(userEmail);

        hashedEmail = md5(userEmail);
        storageReference = FirebaseStorage.getInstance().getReference().child(hashedEmail+".jpg");

        try {
            final File localFile = File.createTempFile(hashedEmail, "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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

        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);

        btnSignOut.setOnClickListener(view -> {
            mAuth.signOut();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("114566570168-8nrknd6dej1q76dodkjptcrka4sdhs98.apps.googleusercontent.com")
                    .requestEmail()
                    .build();

            GoogleSignInClient googleClient = GoogleSignIn.getClient(this, gso);
            googleClient.signOut();

            startActivity(new Intent(SettingsActivity.this, LandingPageActivity.class));
            finishAffinity();
        });

        btnChangeProfile.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, ChangeProfileActivity.class));
            finish();
        });
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