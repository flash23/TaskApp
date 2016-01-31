package humanity.com.taskapp.Activity.TaskItemDetails;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.IOService.DoneJobs.OpenTaskStatusDoneJob;
import humanity.com.taskapp.IOService.JOBS.OpenTaskStatusJob;
import humanity.com.taskapp.IOService.JOBS.SaveTaskStatusJob;
import humanity.com.taskapp.IOService.MODEL.TaskItemModel;
import humanity.com.taskapp.R;

/**
 * Created by mirkomesner on 01/25/16.
 */
///details of task
public class TaskItemDetailsActivity extends AppCompatActivity {

    private TaskItemModel task;

    TextView taskTitle;
    TextView taskPrio;
    TextView taskDueDate;
    TextView taskDaysLeft;
    TextView taskDesc;
    TextView taskStatus;

    RelativeLayout taskPriorityRL;

    LinearLayout taskResolveBar;
    Button taskResolveBt;
    Button taskCntResolveBt;

    ImageView taskStatusImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_details);

        setupUI();

        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setupUI()
    {
        taskTitle = (TextView) findViewById(R.id.task_title);
        taskPrio = (TextView) findViewById(R.id.task_priority);
        taskDueDate = (TextView) findViewById(R.id.task_due_date);
        taskDaysLeft = (TextView) findViewById(R.id.task_days_left);
        taskDesc = (TextView) findViewById(R.id.task_desc);
        taskStatus = (TextView) findViewById(R.id.task_status);

        taskPriorityRL = (RelativeLayout) findViewById(R.id.task_priority_rl);

        taskResolveBar = (LinearLayout) findViewById(R.id.task_resolve_bar);
        taskResolveBt = (Button) findViewById(R.id.task_resolve_button);
        taskCntResolveBt = (Button) findViewById(R.id.task_cntresolve_button);

        taskStatusImage = (ImageView) findViewById(R.id.task_status_image);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.task_details);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void populateUI()
    {
        if(task!=null) {
            taskTitle.setText(task.title);
            taskPrio.setText(task.Priority + "");
            taskDueDate.setText(task.DueDate);

            int dleft = (int)Math.max(0, Math.round((task.DueDateDate.getTime() - new Date().getTime() ) / (double) 86400000));

            taskDaysLeft.setText(dleft+"");
            taskDesc.setText(task.Description);

            taskResolveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().postSticky(new SaveTaskStatusJob(task.id, TaskStatusEnum.RESOLVED));
                    setTaskStatusUI(TaskStatusEnum.RESOLVED);///we dont wait for network ;)
                }
            });
            taskCntResolveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().postSticky(new SaveTaskStatusJob(task.id, TaskStatusEnum.CANTRESOLVE));
                    setTaskStatusUI(TaskStatusEnum.CANTRESOLVE);
                }
            });

            EventBus.getDefault().post(new OpenTaskStatusJob(task.id));
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void onEventMainThread(TaskItemModel item)
    {
        this.task = item;
        EventBus.getDefault().removeStickyEvent(item);

        populateUI();
    }

    public void onEventBackgroundThread(final OpenTaskStatusDoneJob event)
    {
        if(event.taskID.equals(task.id))
        {
            EventBus.getDefault().removeStickyEvent(event);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTaskStatusUI(event.taskStatus);
                }
            });
        }

    }

    private void setTaskStatusUI(String etaskStatus)
    {
        taskTitle.setTextColor(getResources().getColor(R.color.taskRed));
        taskPriorityRL.setBackgroundResource(R.drawable.circle_background);
        taskDueDate.setTextColor(getResources().getColor(R.color.taskRed));
        taskDaysLeft.setTextColor(getResources().getColor(R.color.taskRed));

        taskStatus.setTextColor(getResources().getColor(R.color.taskRed));

        if(etaskStatus.equals(TaskStatusEnum.UNRESOLVED))
        {
            taskResolveBar.setVisibility(View.VISIBLE);
            taskStatusImage.setVisibility(View.GONE);
            taskStatus.setText(getResources().getString(R.string.unresolved));

            taskStatus.setTextColor(getResources().getColor(R.color.taskOrange));
        }
        else{
            taskResolveBar.setVisibility(View.GONE);
            taskStatusImage.setVisibility(View.VISIBLE);

            if(etaskStatus.equals(TaskStatusEnum.RESOLVED))
            {
                taskStatus.setText(getResources().getString(R.string.resolved));
                taskStatusImage.setImageResource(R.mipmap.resolved_sign);

                taskTitle.setTextColor(getResources().getColor(R.color.taskGreen));
                taskPriorityRL.setBackgroundResource(R.drawable.circle_background_green);
                taskDueDate.setTextColor(getResources().getColor(R.color.taskGreen));
                taskDaysLeft.setTextColor(getResources().getColor(R.color.taskGreen));

                taskStatus.setTextColor(getResources().getColor(R.color.taskGreen));
            }
            else
            {
                taskStatus.setText(getResources().getString(R.string.unresolved));
                taskStatusImage.setImageResource(R.mipmap.unresolved_sign);
            }
        }
    }
}
