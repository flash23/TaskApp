package humanity.com.taskapp.Activity.Main.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import humanity.com.taskapp.R;

/**
 * Created by mirkomesner on 01/26/16.
 */
//cell of RecycleView
public class TaskItemView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView taskTitle;
    public TextView taskPriorty;
    public TextView taskDueDate;
    public TextView taskDaysLeft;

    public ViewHolderClicks mListener;

    public TaskItemView(View itemView, ViewHolderClicks listener) {
        super(itemView);

        taskTitle = (TextView) itemView.findViewById(R.id.task_title);
        taskPriorty = (TextView) itemView.findViewById(R.id.task_priority);
        taskDueDate = (TextView) itemView.findViewById(R.id.task_due_date);
        taskDaysLeft = (TextView) itemView.findViewById(R.id.task_days_left);

        itemView.setOnClickListener(this);

        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view, getLayoutPosition());
    }

    public static interface ViewHolderClicks {
        public void onClick(View caller, int position);

    }
}
