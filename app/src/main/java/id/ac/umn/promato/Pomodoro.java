package id.ac.umn.promato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Pomodoro extends AppCompatActivity {

//    private Chronometer podomoroTime;
//    private Button startPause;
//
//    private boolean isRunning, paused=true;
//    private  long currentTVal;
//
//    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
//        NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

//
//        podomoroTime = findViewById(R.id.podTime);
//        startPause = findViewById(R.id.startPause);
//
//        podomoroTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//            @Override
//            public void onChronometerTick(Chronometer chronometer) {
//
//                if((SystemClock.elapsedRealtime() - podomoroTime.getBase()) > 1500000)
//                {
//                    status.setText("Rest");
//                }
//            }
//        });
//        status = findViewById(R.id.status);
    }

//    public void StartPodomoro(View v)
//    {
//        if(!isRunning && paused)
//        {
//            status.setText("Working");
//            podomoroTime.setBase(SystemClock.elapsedRealtime() - currentTVal);
//            podomoroTime.start();
//            isRunning = true;
//            startPause.setText("Pause");
//            paused = false;
//        }
//        else
//        {
//            PausePodomoro();
//        }
//    }
//
//    private void PausePodomoro()
//    {
//        if(!isRunning) return;
//        podomoroTime.stop();
//        currentTVal = SystemClock.elapsedRealtime() - podomoroTime.getBase();
//        isRunning = false;
//        startPause.setText("Start");
//        paused = true;
//    }
//
//    public void StopPodomoro(View v)
//    {
//        status.setText("Status");
//        podomoroTime.setBase(SystemClock.elapsedRealtime());
//        currentTVal = 0;
//        PausePodomoro();
//    }
}