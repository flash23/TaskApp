package humanity.com.taskapp.Activity.Main.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.Activity.Main.TaskItemView;
import humanity.com.taskapp.Activity.TaskItem.OpenTaskDetailsJob;
import humanity.com.taskapp.IOService.MODEL.TaskItemModel;
import humanity.com.taskapp.R;

/**
 * Created by mirkomesner on 01/25/16.
 */
public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TaskItemView> {

    public List<TaskItemModel> items;

    TasksFragment context;

    public TasksRecyclerViewAdapter(TasksFragment context)
    {
        this.context = context;

        items = new ArrayList<TaskItemModel>();
    }

    @Override
    public TaskItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_view, null);

        TaskItemView rcv = new TaskItemView(layoutView,new TaskItemView.ViewHolderClicks() {
            public void onClick(View v, int position) {
                TaskItemModel item = items.get(position);
                EventBus.getDefault().post(new OpenTaskDetailsJob(item));
            };
        });

        return rcv;
    }

    @Override
    public void onBindViewHolder(TaskItemView holder, int position) {
        holder.taskTitle.setText(items.get(position).title);
        holder.taskPriorty.setText(items.get(position).Priority + "");
        holder.taskDueDate.setText(items.get(position).DueDate);
        holder.taskDaysLeft.setText(items.get(position).TargetDate);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
