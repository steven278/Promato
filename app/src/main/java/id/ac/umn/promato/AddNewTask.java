package id.ac.umn.promato;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

public class AddNewTask extends AppCompatActivity {
    private Button btn_Submit;

    private DatePickerDialog.OnDateSetListener datePickerDialog;
    EditText et_task;
    TextView btn_date;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        //retrieve intent value
        String userEmail = getIntent().getStringExtra("useremail");

        btn_Submit = findViewById(R.id.btn_submitNewTask);
        et_task = findViewById(R.id.et_taskName);
        btn_date = findViewById(R.id.et_dateInput);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String taskID = createTransactionID();


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddNewTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month += 1;
                        String date = day+"/"+month+"/"+year;
                        btn_date.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/");//panggil root node database
                reference = rootNode.getReference("todolist");

                //Get all values from edittext
                String task = et_task.getText().toString();
                String date = btn_date.getText().toString();
            //

            //
                if(!task.equals("") && !date.equals("Pick a Date")){
                    Todo todo = new Todo(task, date, taskID, R.drawable.mini_circle_blue);
                    //insert to database
                    reference.child("todo").child(userEmail).child(taskID).setValue(todo);
                    Intent intent = new Intent(AddNewTask.this, Pomodoro.class);
                    intent.putExtra("useremail", userEmail);
                    startActivity(intent);
                }else{
                    Toast.makeText(AddNewTask.this, "Semua field harus diisi terlebih dahulu. Salam dari binjai 👊🌴", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public String createTransactionID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }


}