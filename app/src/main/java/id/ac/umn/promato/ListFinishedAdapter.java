package id.ac.umn.promato;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListFinishedAdapter extends RecyclerView.Adapter<ListFinishedAdapter.ListViewHolder> {
    private ArrayList<Todo> listTodo;
    public ListFinishedAdapter(ArrayList<Todo> list){
        this.listTodo = list;
    }
    @NonNull
    @Override
    public ListFinishedAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_finished, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFinishedAdapter.ListViewHolder holder, int position) {
        Todo todo = listTodo.get(position);
        holder.tvTodo.setText(todo.getTask());
        holder.tvDate.setText(todo.getDate());
        holder.btn_addProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskID = todo.getTaskID();
                holder.reference.child("finished").child(holder.userEmail).child(taskID).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTodo.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView tvTodo, tvDate;
        Button btn_addProgress;
        DatabaseReference reference;
        FirebaseDatabase rootNode;
        String userEmail;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            Intent email = ((Activity) itemView.getContext()).getIntent();
            userEmail= email.getStringExtra("useremail");
            tvTodo = itemView.findViewById(R.id.tv_finished);
            tvDate = itemView.findViewById(R.id.tv_dateFinished);
            btn_addProgress = itemView.findViewById(R.id.btn_remove);
            rootNode = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/");//panggil root node database
            reference = rootNode.getReference("todolist");
        }
    }
}
