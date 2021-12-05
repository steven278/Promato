package id.ac.umn.promato;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private Chronometer podomoroTime;
    private Button startPause, stopPodomoro;

    private boolean isRunning, paused=true;
    private  long currentTVal;

    private TextView status;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bawaan dari sana
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void PausePodomoro()
    {
        if(!isRunning) return;
        podomoroTime.stop();
        currentTVal = SystemClock.elapsedRealtime() - podomoroTime.getBase();
        isRunning = false;
        startPause.setText("Start");
        paused = true;
    }

    private ImageButton btnSetting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);

//        btnSetting = v.findViewById(R.id.setting_btn_);
//        btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), SettingsActivity.class);
////                getActivity().finish();
//                startActivity(intent);
//            }
//        });

        podomoroTime = v.findViewById(R.id.podTime);
        startPause = v.findViewById(R.id.startPause);
        stopPodomoro = v.findViewById(R.id.Stop);
        status = v.findViewById(R.id.status);
        startPause = v.findViewById(R.id.startPause);
        podomoroTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                if((SystemClock.elapsedRealtime() - podomoroTime.getBase()) > 1500000)
                {
                    status.setText("Rest");
                }
            }
        });
        startPause.setOnClickListener(this);
        stopPodomoro.setOnClickListener(this);
        return v;
//        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startPause:
                if(!isRunning && paused)
                {
                    status.setText("Working");
                    podomoroTime.setBase(SystemClock.elapsedRealtime() - currentTVal);
                    podomoroTime.start();
                    isRunning = true;
                    startPause.setText("Pause");
                    paused = false;
                }
                else
                {
                    PausePodomoro();
                }
                break;
            case R.id.Stop:
                status.setText("Status");
                podomoroTime.setBase(SystemClock.elapsedRealtime());
                currentTVal = 0;
                PausePodomoro();
                break;
        }
    }
}