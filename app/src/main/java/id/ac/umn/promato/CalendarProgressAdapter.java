package id.ac.umn.promato;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CalendarProgressAdapter extends RecyclerView.Adapter<CalendarProgressAdapter.ListViewHolder> {
    private ArrayList<Todo> listTodo;
    public CalendarProgressAdapter(ArrayList<Todo> list){
        this.listTodo = list;
    }
    @NonNull
    @Override
    public CalendarProgressAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_progress_calendar, viewGroup, false);
        return new CalendarProgressAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarProgressAdapter.ListViewHolder holder, int position) {
        Todo todo = listTodo.get(position);
        holder.tvTodo.setText(todo.getTask());
        Glide.with(holder.itemView.getContext())
                .load(todo.getStatusIcon())
                .apply(new RequestOptions().override(16,16))
                .into(holder.imgStatusIcon);
    }

    @Override
    public int getItemCount() {
        return listTodo.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTodo;
        DatabaseReference reference;
        FirebaseDatabase rootNode;
        String userEmail;
        ImageView imgStatusIcon;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            Intent email = ((Activity) itemView.getContext()).getIntent();
            userEmail= email.getStringExtra("useremail");
            tvTodo = itemView.findViewById(R.id.tv_calendarTodoListProgress);
            imgStatusIcon = itemView.findViewById(R.id.img_StatusIconProgress);
            rootNode = FirebaseDatabase.getInstance("https://promato-87428-default-rtdb.asia-southeast1.firebasedatabase.app/");//panggil root node database
            reference = rootNode.getReference("todolist");
        }
    }
}
