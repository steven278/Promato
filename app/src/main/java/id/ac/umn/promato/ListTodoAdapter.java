package id.ac.umn.promato;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListTodoAdapter extends RecyclerView.Adapter<ListTodoAdapter.ListViewHolder> {
    private ArrayList<Todo> listTodo;
    public ListTodoAdapter(ArrayList<Todo> list){
        this.listTodo = list;
    }
    @NonNull
    @Override
    public ListTodoAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_todo, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTodoAdapter.ListViewHolder holder, int position) {
        Todo todo = listTodo.get(position);
        holder.tvTodo.setText(todo.getTask());
        holder.tvDate.setText(todo.getDate());
    }

    @Override
    public int getItemCount() {
        return listTodo.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTodo, tvDate;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTodo = itemView.findViewById(R.id.tv_todo);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
