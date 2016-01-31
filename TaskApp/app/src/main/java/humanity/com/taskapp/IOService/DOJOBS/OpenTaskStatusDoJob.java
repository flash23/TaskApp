package humanity.com.taskapp.IOService.DOJOBS;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.Activity.TaskItemDetails.TaskStatusEnum;
import humanity.com.taskapp.IOService.DoneJobs.OpenTaskStatusDoneJob;
import humanity.com.taskapp.IOService.JOBS.OpenTaskStatusJob;

/**
 * Created by mirkomesner on 01/31/16.
 */
public class OpenTaskStatusDoJob implements GenericDoJob {

    OpenTaskStatusJob openTaskJob;
    Context context;

    public static String TAG = "OPEN_TASK_STATUS";

    public OpenTaskStatusDoJob(Context context, OpenTaskStatusJob openTaskJob)
    {
        this.context = context;
        this.openTaskJob = openTaskJob;
    }

    @Override
    public void doJob() {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String taskStatus = sharedpreferences.getString("TASKSTATUS"+openTaskJob.taskID, TaskStatusEnum.UNRESOLVED);

        EventBus.getDefault().postSticky(new OpenTaskStatusDoneJob(openTaskJob.taskID, taskStatus));
    }
}
