package humanity.com.taskapp.IOService.API;

import humanity.com.taskapp.IOService.MODEL.TaskApiResponseModel;
import retrofit.http.GET;

/**
 * Created by mirkomesner on 01/25/16.
 */
//siple code using interface :)
public interface TaskApi {
    @GET("/tasks")
    public void getTaskItems(
            CancelableCallback<TaskApiResponseModel> response);
}