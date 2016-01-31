package humanity.com.taskapp.IOService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.Activity.Main.Fragment.TaskFrakment.ControlSpinnersEvent;
import humanity.com.taskapp.IOService.DOJOBS.GenericDoJob;
import humanity.com.taskapp.IOService.DOJOBS.OpenTaskStatusDoJob;
import humanity.com.taskapp.IOService.DOJOBS.SaveTaskStatusDoJob;
import humanity.com.taskapp.IOService.DOJOBS.TaskApiDoJob;
import humanity.com.taskapp.IOService.DoneJobs.ServiceStarted;
import humanity.com.taskapp.IOService.JOBS.OpenTaskStatusJob;
import humanity.com.taskapp.IOService.JOBS.SaveTaskStatusJob;
import humanity.com.taskapp.IOService.JOBS.TaskApiJob;

/**
 * Created by mirkomesner on 01/25/16.
 */
//We are using Service for network communication because we want persistence.
//When user moves our app in the background Service will stay alive and it will finish all given tasks
//If we have had implemented network communication in Activities user would be able to cancel upload(of lets say big image)
// by just moving the app in background.
//For communication between Service and Activities we use EventBus library. No need to worry about broadcasters handlers etc..
//If we start to expend this app with location services, notification handling etc... all of that should go in this Service
public class InOutService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);

        synchronized (monitorWrite) {
            jobs = new ArrayList<GenericDoJob>();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        if(intent.getBooleanExtra("isFirstLunch",false))
            EventBus.getDefault().postSticky(new ServiceStarted());

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    List<GenericDoJob> jobs;
    boolean isRunning = false;

    public class MonitorObject {
    }

    MonitorObject monitorRead = new MonitorObject();
    MonitorObject monitorWrite = new MonitorObject();

    //Small implementation of producers consumers problem...Managing of network requests like jobs on the line.
    //The be correct lunching of requests is done synchronously but they are all run in there own threads
    //The thread in which the service shall run
    private class serviceThread implements Runnable {

        public void run() {
            try {
                while (true) {
                    synchronized (monitorRead) {
                        if (jobs.size() == 0) {
                            isRunning = false;
                            break;
                        }

                        GenericDoJob job = jobs.get(0);
                        job.doJob();
                    }

                    synchronized (monitorWrite) {
                        jobs.remove(0);
                    }

                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void runJobs() {
        if (!isRunning)
        {
            isRunning = true;
            Thread serviceThread = new Thread(new serviceThread());
            serviceThread.start();
        }
    }

    ///////////////////////////////////////

    // This method will be called when a SomeOtherEvent is posted
    //Here we inform object who is listening for this EventBus event... In this case adapter requested items for Ascii warehouse
    public void onEvent(TaskApiJob event){
        synchronized (monitorWrite) {
            TaskApiJob taskJob =  event;
            jobs.add(new TaskApiDoJob(taskJob));

            EventBus.getDefault().postSticky(new ControlSpinnersEvent(true));
        }
        runJobs();
    }

    public void onEvent(SaveTaskStatusJob event){
        synchronized (monitorWrite) {
            SaveTaskStatusJob taskJob =  event;
            jobs.add(new SaveTaskStatusDoJob(getApplicationContext(), taskJob));
        }
        runJobs();
    }

    public void onEvent(OpenTaskStatusJob event){
        synchronized (monitorWrite) {
            OpenTaskStatusJob taskJob =  event;
            jobs.add(new OpenTaskStatusDoJob(getApplicationContext(), taskJob));
        }
        runJobs();
    }
}
