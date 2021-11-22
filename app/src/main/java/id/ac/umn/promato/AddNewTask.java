package id.ac.umn.promato;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddNewTask extends AppCompatActivity {
    private Button btn_Submit;
    EditText et_task, et_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn_Submit = findViewById(R.id.btn_addTask);
        et_task = findViewById(R.id.et_taskName);
        et_date = findViewById(R.id.et_dateInput);

        setContentView(R.layout.activity_add_new_task);
    }
}