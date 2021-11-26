package id.ac.umn.promato;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodolistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodolistFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodolistFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodolistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodolistFragment newInstance(String param1, String param2) {
        TodolistFragment fragment = new TodolistFragment();
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
    private RecyclerView rvTodo;
    private ArrayList<Todo> list = new ArrayList<>();
    private Button btn_addTask;
    ListTodoAdapter listTodoAdapter;
    DatabaseReference database;

    private RecyclerView rvProgress;
    private ArrayList<Todo> listProgress = new ArrayList<>();
    ListInProgressAdapter listInProgressAdapter;
    DatabaseReference databaseProgress;

    private RecyclerView rvFinished;
    private ArrayList<Todo> listFinished = new ArrayList<>();
    ListFinishedAdapter listFinishedAdapter;
    DatabaseReference databaseFinished;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_todolist, container, false);
        //retrieve intent value
        String userEmail = getActivity().getIntent().getStringExtra("useremail");

        //todo
        btn_addTask = v.findViewById(R.id.btn_addTask);
        rvTodo = v.findViewById(R.id.rv_todo);
        database = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("todolist").child("todo").child(userEmail);
        rvTodo.setHasFixedSize(true);
        showRecyclerList();
        btn_addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddNewTask.class);
                intent.putExtra("useremail", userEmail);
                startActivity(intent);
            }
        });

        //in progress
        rvProgress = v.findViewById(R.id.rv_inProgress);
        databaseProgress = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("todolist").child("inProgress").child(userEmail);
        rvProgress.setHasFixedSize(true);
        showProgressRecyclerList();

        //finished
        rvFinished = v.findViewById(R.id.rv_finished);
        databaseFinished = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("todolist").child("finished").child(userEmail);
        rvFinished.setHasFixedSize(true);
        showFinishedRecyclerList();

        return v;
    }
    private void showRecyclerList(){
        rvTodo.setLayoutManager(new LinearLayoutManager(getActivity()));
        listTodoAdapter = new ListTodoAdapter(list);
        rvTodo.setAdapter(listTodoAdapter);
        rvTodo.setNestedScrollingEnabled(false);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Todo todo = dataSnapshot.getValue(Todo.class);
                    list.add(todo);
                }
                listTodoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showProgressRecyclerList(){
        rvProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
        listInProgressAdapter = new ListInProgressAdapter(listProgress);
        rvProgress.setAdapter(listInProgressAdapter);
        rvProgress.setNestedScrollingEnabled(false);
        databaseProgress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProgress.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Todo todoProgress = dataSnapshot.getValue(Todo.class);
                    listProgress.add(todoProgress);
                }
                listInProgressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showFinishedRecyclerList(){
        rvFinished.setLayoutManager(new LinearLayoutManager(getActivity()));
        listFinishedAdapter = new ListFinishedAdapter(listFinished);
        rvFinished.setAdapter(listFinishedAdapter);
        rvFinished.setNestedScrollingEnabled(false);
        databaseFinished.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFinished.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Todo todoFinished = dataSnapshot.getValue(Todo.class);
                    listFinished.add(todoFinished);
                }
                listFinishedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}