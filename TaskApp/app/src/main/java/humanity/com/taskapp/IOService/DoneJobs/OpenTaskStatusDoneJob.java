package humanity.com.taskapp.IOService.DoneJobs;

/**
 * Created by mirkomesner on 01/25/16.
 */
//we are
public class OpenTaskStatusDoneJob extends GenericDoneJob {

    public String taskID;
    public String taskStatus;

    public OpenTaskStatusDoneJob(String taskID, String taskStatus)
    {
        this.taskID = taskID;
        this.taskStatus = taskStatus;
    }
}
