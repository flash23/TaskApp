package humanity.com.taskapp.IOService.DOJOBS;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import humanity.com.taskapp.IOService.JOBS.SaveTaskStatusJob;

/**
 * Created by mirkomesner on 01/31/16.
 */
public class SaveTaskStatusDoJob implements GenericDoJob {

    SaveTaskStatusJob saveTaskJob;
    Context context;

    public static String TAG = "SAVE_TASK_STATUS";

    public SaveTaskStatusDoJob(Context context, SaveTaskStatusJob saveTaskJob)
    {
        this.context = context;
        this.saveTaskJob = saveTaskJob;
    }

    @Override
    public void doJob() {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("TASKSTATUS"+saveTaskJob.taskID, saveTaskJob.taskStatusToSave);
        editor.commit();

        //EventBus.getDefault().postSticky(new SaveTaskStatusDoneJob());/// nobody is listening for this event... we should error handling
    }
}
