    package id.ac.umn.promato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

    public class UserProfileActivity extends AppCompatActivity {
    TextView tvUserName;
    TextView tvUserEmail;
    ImageView userImageView;
    Button btnSignOut;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvUserName = findViewById(R.id.userName);
        tvUserEmail = findViewById(R.id.userEmail);
        userImageView = findViewById(R.id.userImageView);
        btnSignOut = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userName = preferences.getString("username", "");
        String userEmail = preferences.getString("useremail", "");
        String userPhotoUrl = preferences.getString("userPhoto", "");

        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
        Glide.with(this).load(userPhotoUrl).into(userImageView);
        btnSignOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(UserProfileActivity.this, LandingPageActivity.class));
        });
    }
}