package humanity.com.taskapp.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import humanity.com.taskapp.Activity.TaskItemDetails.TaskItemDetailsActivity;
import humanity.com.taskapp.Activity.TaskItemDetails.OpenTaskDetailsJob;
import humanity.com.taskapp.Activity.Main.TaskListActivity;
import humanity.com.taskapp.Activity.Splash.CloseSplashEvent;
import humanity.com.taskapp.Activity.Splash.SplashActivity;
import humanity.com.taskapp.IOService.DoneJobs.ServiceErrorStop;
import humanity.com.taskapp.IOService.DoneJobs.ServiceStarted;
import humanity.com.taskapp.IOService.InOutService;
import de.greenrobot.event.EventBus;

/**
 * Created by mirkomesner on 01/25/16.
 */
public class TaskApplication extends Application implements Application.ActivityLifecycleCallbacks {

    //We want to control activities and services life from one place application

    private static Context mContext;
    private static Activity currentAtivity;

    public static Activity getCurrentAtivity(){
        return currentAtivity;
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);

        this.mContext = this;
    }

    //region waiting for events for app activity life cycles
    //wait for service to start... all the network communication and caching is in service
    public void onEvent(ServiceStarted event){

        //LET'S see the splash ;)
        Handler handler = new Handler();
        final Context context = this;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //kill the splash
                EventBus.getDefault().postSticky(new CloseSplashEvent());

                lunchTaskMain();

            }
        }, 1000);///we are to fast for splash ;)
    }

    //Relunch Service... we want it alive
    public void onEvent(ServiceErrorStop event){
        Intent i= new Intent(this, InOutService.class);
        this.startService(i);
    }

    //Relunch Service... we want it alive
    public void onEvent(OpenTaskDetailsJob event){
        lunchTaskDetails(event);
    }
    //endregion

    //region WE control lifecycle of activites

    private int resumed;
    private int stopped;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if(resumed == stopped )//first start 0 == 0 or else we came from background
        {
            EventBus.getDefault().register(this);

            //first start or app was removed from memory... we can build in support for saved states
            if(activity.getClass() == SplashActivity.class) {
                lunchService();
            }
        }
        ++resumed;

        currentAtivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;

        if(resumed == stopped)
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
    //endregion
    private void lunchService()
    {
        Intent i = new Intent(this, InOutService.class);
        i.putExtra("isFirstLunch", true);
        this.startService(i);
    }

    private void lunchTaskMain()
    {
        if(currentAtivity!=null) {
            Intent mainActy = new Intent(currentAtivity, TaskListActivity.class);
            currentAtivity.startActivity(mainActy);
        }
    }

    private void lunchTaskDetails(OpenTaskDetailsJob event)
    {
        if(currentAtivity!=null) {
            Intent deatilsActy = new Intent(currentAtivity, TaskItemDetailsActivity.class);
            currentAtivity.startActivity(deatilsActy);
            //we don't pass data using Intents
            EventBus.getDefault().postSticky(event.item);
        }
    }
}
