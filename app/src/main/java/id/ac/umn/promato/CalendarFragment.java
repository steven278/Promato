package id.ac.umn.promato;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView rvCalendarTodo, rvCalendarProgress, rvCalendarFinish;
    private ArrayList<Todo> listTodo = new ArrayList<>();
    private ArrayList<Todo> listProgress = new ArrayList<>();
    private ArrayList<Todo> listFinish = new ArrayList<>();
    private CalendarListAdapter calendarListTodoAdapter, calendarListProgressAdapter, calendarListFinishAdapter;
    private DatabaseReference databaseTodo, databaseProgress, databaseFinish;
    private Query dbqueryTodo, dbqueryProgress, dbqueryFinish;

    private ImageButton btnSetting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        btnSetting = v.findViewById(R.id.setting_btn_todolist);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                getActivity().finish();
                startActivity(intent);
            }
        });

        CalendarView calendarView = v.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + " " + new DateFormatSymbols().getMonths()[month] + " " + year + "";
                String clickedDate = dayOfMonth+"/"+(month+1)+"/"+year;
                Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
                String userEmail = getActivity().getIntent().getStringExtra("useremail");
                rvCalendarTodo = v.findViewById(R.id.rv_calendar_todo);
                databaseTodo = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("todolist").child("todo").child(userEmail);
                dbqueryTodo = databaseTodo.orderByChild("date").equalTo(clickedDate);
                rvCalendarTodo.setHasFixedSize(true);

                rvCalendarProgress = v.findViewById(R.id.rv_calendar_progress);
                databaseProgress = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("todolist").child("inProgress").child(userEmail);
                dbqueryProgress = databaseProgress.orderByChild("date").equalTo(clickedDate);
                rvCalendarProgress.setHasFixedSize(true);

                rvCalendarFinish = v.findViewById(R.id.rv_calendar_finish);
                databaseFinish = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("todolist").child("finished").child(userEmail);
                dbqueryFinish = databaseFinish.orderByChild("date").equalTo(clickedDate);
                rvCalendarFinish.setHasFixedSize(true);
                showRecyclerList();
            }
        });

        return v;
    }
    public void showRecyclerList(){
        rvCalendarTodo.setLayoutManager(new LinearLayoutManager(getActivity()));
        calendarListTodoAdapter = new CalendarListAdapter(listTodo);
        rvCalendarTodo.setAdapter(calendarListTodoAdapter);
        dbqueryTodo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTodo.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Todo todoProgress = dataSnapshot.getValue(Todo.class);
                    listTodo.add(todoProgress);
                }
                calendarListTodoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rvCalendarProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
        calendarListProgressAdapter = new CalendarListAdapter(listProgress);
        rvCalendarProgress.setAdapter(calendarListProgressAdapter);
        dbqueryProgress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProgress.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Todo todoProgress = dataSnapshot.getValue(Todo.class);
                    listProgress.add(todoProgress);
                }
                calendarListProgressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rvCalendarFinish.setLayoutManager(new LinearLayoutManager(getActivity()));
        calendarListFinishAdapter = new CalendarListAdapter(listFinish);
        rvCalendarFinish.setAdapter(calendarListFinishAdapter);
        dbqueryFinish.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFinish.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Todo todoProgress = dataSnapshot.getValue(Todo.class);
                    listFinish.add(todoProgress);
                }
                calendarListFinishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
