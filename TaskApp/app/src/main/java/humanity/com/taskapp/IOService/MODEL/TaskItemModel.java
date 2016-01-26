package humanity.com.taskapp.IOService.MODEL;

import com.google.gson.annotations.Expose;

/**
 * Created by mirkomesner on 01/25/16.
 */
public class TaskItemModel {

    @Expose
    public String id;
    @Expose
    public String title;
    @Expose
    public String TargetDate;
    @Expose
    public String DueDate;
    @Expose
    public String Description;
    @Expose
    public int Priority;

//    "id": "52dc3c20fa9f41ed8a54e49927e82dac",
//            "title": "Perform food preparation duties such as preparing salads, appetizers, and cold dishes, portioning desserts, and brewing coffee.",
//            "TargetDate": "2015-12-03 11:00:00",
//            "DueDate": "2015-12-06 12:00:00",
//            "Description": "Perform food preparation duties such as preparing salads, appetizers, and cold dishes, portioning desserts, and brewing coffee. Perform food preparation duties such as preparing salads, appetizers, and cold dishes, portioning desserts, and brewing coffee.",
//            "Priority": 2
}
