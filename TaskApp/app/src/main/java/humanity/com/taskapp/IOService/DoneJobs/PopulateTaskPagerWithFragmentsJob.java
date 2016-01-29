package humanity.com.taskapp.IOService.DoneJobs;

import java.util.Date;
import java.util.List;
import java.util.Map;

import humanity.com.taskapp.IOService.MODEL.TaskItemModel;

/**
 * Created by mirkomesner on 01/25/16.
 */
public class PopulateTaskPagerWithFragmentsJob extends GenericDoneJob {

    public boolean succes = false;
    public Map<Date, List<TaskItemModel>> groupedTasks = null;

    public boolean reloaded = true;

    public PopulateTaskPagerWithFragmentsJob(TaskApiDoneJob taskApiSortedGrouped)
    {
        this.succes = taskApiSortedGrouped.succes;
        this.reloaded = taskApiSortedGrouped.reloaded;
        if(taskApiSortedGrouped.groupedTasks != null)
            this.groupedTasks = taskApiSortedGrouped.groupedTasks;
    }
}
