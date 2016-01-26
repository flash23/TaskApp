package humanity.com.taskapp.IOService.MODEL;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mirkomesner on 01/25/16.
 */
public class TaskApiResponseModel {

    @Expose
    public int status;

    @SerializedName("tasks ")
    @Expose
    public List<TaskItemModel> tasks;
}