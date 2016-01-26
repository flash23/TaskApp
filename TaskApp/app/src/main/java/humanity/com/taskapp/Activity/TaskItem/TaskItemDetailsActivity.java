package humanity.com.taskapp.Activity.TaskItem;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
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
    LinearLayout taskResolveBar;

    Button taskResolveBt;
    Button taskCntResolveBt;

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

    public void onEventMainThread(TaskItemModel item)
    {
        this.task = item;
        EventBus.getDefault().removeStickyEvent(item);

        populateUI();
    }

    private void setupUI()
    {
        taskTitle = (TextView) findViewById(R.id.task_title);
        taskPrio = (TextView) findViewById(R.id.task_priority);
        taskDueDate = (TextView) findViewById(R.id.task_due_date);
        taskDaysLeft = (TextView) findViewById(R.id.task_days_left);
        taskDesc = (TextView) findViewById(R.id.task_desc);
        taskStatus = (TextView) findViewById(R.id.task_status);

        taskResolveBar = (LinearLayout) findViewById(R.id.task_resolve_bar);
        taskResolveBt = (Button) findViewById(R.id.task_resolve_button);
        taskCntResolveBt = (Button) findViewById(R.id.task_cntresolve_button);

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
            taskDaysLeft.setText(task.TargetDate);
            taskDesc.setText(task.Description);

            taskResolveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //handle buy
                }
            });
            taskCntResolveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //handle buy
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
