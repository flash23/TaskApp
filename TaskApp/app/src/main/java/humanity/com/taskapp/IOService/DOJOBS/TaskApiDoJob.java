package humanity.com.taskapp.IOService.DOJOBS;

import android.app.Activity;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.IOService.API.Api;
import humanity.com.taskapp.IOService.API.CancelableCallback;
import humanity.com.taskapp.IOService.API.TaskApi;
import humanity.com.taskapp.IOService.DoneJobs.TaskApiDoneJob;
import humanity.com.taskapp.IOService.JOBS.TaskApiJob;
import humanity.com.taskapp.IOService.MODEL.TaskApiResponseModel;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mirkomesner on 01/25/16.
 */
public class TaskApiDoJob implements GenericDoJob {
    TaskApiJob taskJob;
    Activity context;

    public static String TAG = "TASKAPI";

    public static int number = 0;

    public TaskApiDoJob(TaskApiJob searchJob)
    {
        this.taskJob = searchJob;
    }

    @Override
    public void doJob() {

        TaskApi search = Api.getInstance().restAdapter.create(TaskApi.class);


        search.getTaskItems( new CancelableCallback<TaskApiResponseModel>(TAG) {
            @Override
            public void onSuccess(TaskApiResponseModel taskItemModelList, Response response) {
                ///TODO: HANDLE status respons...btw Response response already has status code 200 etc...
                //TODO: sort all the tasks!!!!
                EventBus.getDefault().postSticky(new TaskApiDoneJob(true, taskItemModelList.tasks, taskJob.reload));
            }

            public void onFailure(RetrofitError error) {
                EventBus.getDefault().post(new TaskApiDoneJob(false, null, false));
            }
        });

    }
}
