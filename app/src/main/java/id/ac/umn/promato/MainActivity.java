package id.ac.umn.promato;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Chronometer pomodoroTime;
    private Button startPause;

    private boolean isRunning, paused=true;
    private  long currentTVal;

    private TextView status;

    private boolean work = true, longRest = false;
    private int restCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pomodoroTime = findViewById(R.id.podTime);
        startPause = findViewById(R.id.startPause);

        pomodoroTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if(work && (SystemClock.elapsedRealtime() - pomodoroTime.getBase()) > 1500000)
                {
                    work = false;

                    ResetStart();

                    restCount++;
                    if(restCount %4 == 0)
                    {
                        longRest = true;
                        status.setText("Long Rest");

                    }else status.setText("Rest");
                }else if (!work && !longRest && (SystemClock.elapsedRealtime() - pomodoroTime.getBase()) > 300000)
                {
                    work = true;
                    status.setText("Working");

                    ResetStart();
                }else if(!work && longRest && (SystemClock.elapsedRealtime() - pomodoroTime.getBase()) > 900000)
                {
                    work = true;
                    status.setText("Working");

                    ResetStart();

                    longRest = false;
                }
            }
        });
        status = findViewById(R.id.status);
    }

    private void ResetStart()
    {
        pomodoroTime.setBase(SystemClock.elapsedRealtime());
        currentTVal = 0;
    }

    public void StartPodomoro(View v)
    {
        if(!isRunning && paused)
        {
            status.setText("Working");
            pomodoroTime.setBase(SystemClock.elapsedRealtime() - currentTVal);
            pomodoroTime.start();
            isRunning = true;
            startPause.setText("Pause");
            paused = false;
        }
        else
        {
            PausePodomoro();
        }
    }

    private void PausePodomoro()
    {
        if(!isRunning) return;
        pomodoroTime.stop();
        currentTVal = SystemClock.elapsedRealtime() - pomodoroTime.getBase();
        isRunning = false;
        startPause.setText("Start");
        paused = true;
    }

    public void StopPodomoro(View v)
    {
        status.setText("Status");
        pomodoroTime.setBase(SystemClock.elapsedRealtime());
        currentTVal = 0;
        PausePodomoro();
    }
}