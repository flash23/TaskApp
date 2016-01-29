package humanity.com.taskapp.IOService.DoneJobs;

import java.util.Date;
import java.util.List;
import java.util.Map;

import humanity.com.taskapp.IOService.MODEL.TaskItemModel;

/**
 * Created by mirkomesner on 01/25/16.
 */
//we are
public class TaskApiDoneJob extends GenericDoneJob {

    public boolean succes = false;
    public Map<Date, List<TaskItemModel>> groupedTasks = null;

    public boolean reloaded = true;

    public TaskApiDoneJob(boolean succes, Map<Date, List<TaskItemModel>> groupedTasks, boolean reloaded)
    {
        this.succes = succes;
        this.reloaded = reloaded;
        if(groupedTasks != null)
            this.groupedTasks = groupedTasks;
    }
}
