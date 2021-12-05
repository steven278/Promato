    package id.ac.umn.promato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

    public class UserProfileActivity extends AppCompatActivity {
    TextView tvUserName;
    TextView tvUserEmail;
    ImageView userImageView;
    Button btnSignOut,btnMainMenu;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvUserName = findViewById(R.id.userName);
        tvUserEmail = findViewById(R.id.userEmail);
        userImageView = findViewById(R.id.userImageView);
        btnSignOut = findViewById(R.id.btnLogout);
        btnMainMenu = findViewById(R.id.gotoMainMenu);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userName = preferences.getString("username", "");
        String userEmail = preferences.getString("useremail", "");
        String hashedEmail = md5(userEmail);
        String userPhotoUrl = preferences.getString("userPhoto", "");

        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
        Glide.with(this).load(userPhotoUrl).into(userImageView);
        btnSignOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(UserProfileActivity.this, LandingPageActivity.class));
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainMenu(hashedEmail);
            }
        });
    }
    public void gotoMainMenu(String hashedEmail){
        Intent intent = new Intent(this, Pomodoro.class);
        intent.putExtra("useremail", hashedEmail);
        finish();
        startActivity(intent);
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