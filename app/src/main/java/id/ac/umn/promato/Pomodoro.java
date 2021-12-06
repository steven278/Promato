package id.ac.umn.promato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Pomodoro extends AppCompatActivity implements AlarmFragment.OnTime {
    FirebaseAuth mAuth;

    MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public void GetPomNotification() {
        if(player == null)
        {
            player = MediaPlayer.create(this, R.raw.alarm);
        }

        player.start();

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(10000);

        NotificationCompat.Builder notif = new NotificationCompat.Builder(Pomodoro.this,"abc");
        notif.setContentTitle("Finished");
        notif.setContentText("Selamat anda berhasil menyelesaikan cycle");
        notif.setSmallIcon(R.drawable.ic_alarm_icon);
        notif.setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(Pomodoro.this);
        manager.notify(1, notif.build());
    }
}