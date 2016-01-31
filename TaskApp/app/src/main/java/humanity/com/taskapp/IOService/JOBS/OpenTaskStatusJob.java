package humanity.com.taskapp.IOService.JOBS;

/**
 * Created by mirkomesner on 01/25/16.
 */

//we are teling to service that we want api task request
public class OpenTaskStatusJob extends GenericJob{
    public String taskID = "0";

    public OpenTaskStatusJob(String taskID)
    {
        if(taskID!=null) {
            this.taskID = taskID;
        }
    }

}