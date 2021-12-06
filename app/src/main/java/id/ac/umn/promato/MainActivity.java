package id.ac.umn.promato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;

public class MainActivity extends AppCompatActivity implements AlarmFragment.OnTime {

    MediaPlayer player;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LandingPageActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }


    @Override
    public void GetPomNotification() {

        if(player == null)
        {
            player = MediaPlayer.create(this, R.raw.alarm);
        }

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(10000);

        NotificationCompat.Builder notif = new NotificationCompat.Builder(MainActivity.this,"abc");
        notif.setContentTitle("Finished");
        notif.setContentText("Selamat anda berhasil menyelesaikan cycle");
        notif.setSmallIcon(R.drawable.ic_alarm_icon);
        notif.setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(MainActivity.this);
        manager.notify(1, notif.build());
    }
}