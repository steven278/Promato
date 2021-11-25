package id.ac.umn.promato;

import java.util.Date;

public class Todo {
    private String task;
    private String date;
    public String taskID;

    public Todo() {

    }

    public Todo(String task, String date, String taskID) {
        this.task = task;
        this.date = date;
        this.taskID = taskID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }
}
