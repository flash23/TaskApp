package humanity.com.taskapp.IOService.DOJOBS;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.IOService.API.Api;
import humanity.com.taskapp.IOService.API.CancelableCallback;
import humanity.com.taskapp.IOService.API.TaskApi;
import humanity.com.taskapp.IOService.DoneJobs.TaskApiDoneJob;
import humanity.com.taskapp.IOService.JOBS.TaskApiJob;
import humanity.com.taskapp.IOService.MODEL.TaskApiResponseModel;
import humanity.com.taskapp.IOService.MODEL.TaskItemModel;
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

                ///we are mising Java 8 for simple code solution... Java 8 is supported in versions above Android 4.4 I think...
                //We can add Java 8 for Android versions below 4.4 but out of this test project scope...
                Map<Date, List<TaskItemModel>> groupedTasks = new HashMap<Date, List<TaskItemModel>>();
                for (TaskItemModel task: taskItemModelList.tasks) {

                    String dueDate = task.DueDate;
                    //We need to get time time zone reference... this is aproctimation!!!
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(dueDate);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                    task.DueDateDate = convertedDate;

                    Calendar cal = Calendar.getInstance();
                    cal.clear();
                    cal.setTime(convertedDate);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    Date trimedDate = cal.getTime();

                    task.DueDateDateTrimed = trimedDate;

                    if (groupedTasks.get(trimedDate) == null) {
                        groupedTasks.put(trimedDate, new ArrayList<TaskItemModel>());
                    }
                    groupedTasks.get(trimedDate).add(task);
                }

                Iterator it = groupedTasks.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();

                    List<TaskItemModel> ts = (List<TaskItemModel>)pair.getValue();
                    Collections.sort(ts);
                    pair.setValue(ts);
                }

                EventBus.getDefault().postSticky(new TaskApiDoneJob(true, groupedTasks, taskJob.reload));
            }

            public void onFailure(RetrofitError error) {
                EventBus.getDefault().post(new TaskApiDoneJob(false, null, false));
            }
        });

    }
}
