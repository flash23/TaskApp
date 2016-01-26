package humanity.com.taskapp.IOService.JOBS;

/**
 * Created by mirkomesner on 01/25/16.
 */

//we are teling to service that we want api task request
public class TaskApiJob extends GenericJob{

    public TaskApiJob(Boolean reload)
    {
        if(reload!=null)
            this.reload = reload;
    }

    public boolean reload = true;
}