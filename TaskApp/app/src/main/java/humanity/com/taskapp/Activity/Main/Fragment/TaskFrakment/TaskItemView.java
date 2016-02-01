package humanity.com.taskapp.Activity.Main.Fragment.TaskFrakment;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.Activity.TaskItemDetails.TaskStatusEnum;
import humanity.com.taskapp.IOService.DoneJobs.OpenTaskStatusDoneJob;
import humanity.com.taskapp.IOService.JOBS.SaveTaskStatusJob;
import humanity.com.taskapp.R;

/**
 * Created by mirkomesner on 01/26/16.
 */
//cell of RecycleView
public class TaskItemView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView taskTitle;
    public TextView taskPriorty;
    public RelativeLayout taskPriortyRL;
    public TextView taskDueDate;
    public TextView taskDaysLeft;

    public RelativeLayout resolveBarRL;
    public Button resolveBT;
    public Button cantresolveBT;

    public String taskID = "0";
    public String taskStatus = TaskStatusEnum.UNRESOLVED;
    public Activity context = null;

    public ViewHolderClicks mListener;

    public TaskItemView(View itemView, ViewHolderClicks listener) {
        super(itemView);

        taskTitle = (TextView) itemView.findViewById(R.id.task_title);
        taskPriorty = (TextView) itemView.findViewById(R.id.task_priority);
        taskPriortyRL = (RelativeLayout) itemView.findViewById(R.id.task_priority_rl);
        taskDueDate = (TextView) itemView.findViewById(R.id.task_due_date);
        taskDaysLeft = (TextView) itemView.findViewById(R.id.task_days_left);

        resolveBarRL = (RelativeLayout) itemView.findViewById(R.id.resolve_bar);
        resolveBT = (Button) itemView.findViewById(R.id.resolved_bt);
        resolveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new SaveTaskStatusJob(taskID, TaskStatusEnum.RESOLVED));
                setTaskStatusUI(TaskStatusEnum.RESOLVED);///we dont wait for network ;)
            }
        });
        cantresolveBT = (Button) itemView.findViewById(R.id.unresolved_bt);
        cantresolveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new SaveTaskStatusJob(taskID, TaskStatusEnum.CANTRESOLVE));
                setTaskStatusUI(TaskStatusEnum.CANTRESOLVE);
            }
        });

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        mListener = listener;

        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view, getLayoutPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        //mListener.OnLongClickListener(view, getLayoutPosition());
        if(taskStatus.equals(TaskStatusEnum.UNRESOLVED))
            resolveBarRL.setVisibility(resolveBarRL.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
        return true;
    }

    public static interface ViewHolderClicks {
        public void onClick(View caller, int position);
        //public void OnLongClickListener(View caller, int position);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if(!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().registerSticky(this);
        } finally {
            super.finalize();
        }
    }

    public void onEventBackgroundThread(final OpenTaskStatusDoneJob event)
    {
        if(event.taskID.equals(taskID))
        {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTaskStatusUI(event.taskStatus);
                }
            });
            EventBus.getDefault().removeStickyEvent(event);
        }

    }

    void setTaskStatusUI(String taskStatus)
    {
        this.taskStatus = taskStatus;
        taskTitle.setTextColor(context.getResources().getColor(R.color.taskRed));
        taskPriortyRL.setVisibility(View.VISIBLE);
        taskDueDate.setTextColor(context.getResources().getColor(R.color.taskRed));
        taskDaysLeft.setTextColor(context.getResources().getColor(R.color.taskRed));

        ((CardView)itemView).setCardBackgroundColor(Color.WHITE);

        if(taskStatus.equals(TaskStatusEnum.UNRESOLVED)) {

        }
        else{
            taskPriortyRL.setVisibility(View.INVISIBLE);

            if(taskStatus.equals(TaskStatusEnum.RESOLVED))
            {
                taskTitle.setTextColor(context.getResources().getColor(R.color.taskGreen));

                taskDueDate.setTextColor(context.getResources().getColor(R.color.taskGreen));
                taskDaysLeft.setTextColor(context.getResources().getColor(R.color.taskGreen));
                ((CardView)itemView).setCardBackgroundColor(context.getResources().getColor(R.color.taskBlue));
            }
            else
            {
                ((CardView)itemView).setCardBackgroundColor(context.getResources().getColor(R.color.taskPink));
            }
        }
    }



}
