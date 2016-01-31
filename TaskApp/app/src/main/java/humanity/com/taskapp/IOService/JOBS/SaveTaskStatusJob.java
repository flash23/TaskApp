package humanity.com.taskapp.IOService.JOBS;

import humanity.com.taskapp.Activity.TaskItemDetails.TaskStatusEnum;

/**
 * Created by mirkomesner on 01/25/16.
 */

//we are teling to service that we want api task request
public class SaveTaskStatusJob extends GenericJob{
    public String taskID = "0";

    public String taskStatusToSave = TaskStatusEnum.UNRESOLVED;

    public SaveTaskStatusJob(String taskID, String taskStatusToSave)
    {
        if(taskStatusToSave!=null && taskID!=null) {
            this.taskID = taskID;
            this.taskStatusToSave = taskStatusToSave;
        }
    }

}