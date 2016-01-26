package humanity.com.taskapp.Activity.TaskItem;

import humanity.com.taskapp.IOService.JOBS.GenericJob;
import humanity.com.taskapp.IOService.MODEL.TaskItemModel;

/**
 * Created by mirkomesner on 01/25/16.
 */
//We are not sending intents around....using fancy and simple EventBus
public class OpenTaskDetailsJob extends GenericJob{
    public TaskItemModel item;

    public OpenTaskDetailsJob(TaskItemModel item)
    {
        this.item = item;
    }
}
