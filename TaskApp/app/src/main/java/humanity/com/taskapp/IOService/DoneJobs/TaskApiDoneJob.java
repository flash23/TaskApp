package humanity.com.taskapp.IOService.DoneJobs;

import java.util.List;

import humanity.com.taskapp.IOService.MODEL.TaskItemModel;

/**
 * Created by mirkomesner on 01/25/16.
 */
//we are
public class TaskApiDoneJob extends GenericDoneJob {

    public boolean succes = false;
    public List<TaskItemModel> taskItemModelList = null;

    public boolean reloaded = true;

    public TaskApiDoneJob(boolean succes, List<TaskItemModel> taskItemModelList, boolean reloaded)
    {
        this.succes = succes;
        this.reloaded = reloaded;
        if(taskItemModelList != null)
            this.taskItemModelList = taskItemModelList;
    }
}
