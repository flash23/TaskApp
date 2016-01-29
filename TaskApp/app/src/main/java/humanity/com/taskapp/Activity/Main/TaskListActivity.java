package humanity.com.taskapp.Activity.Main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.IOService.API.CancelableCallback;
import humanity.com.taskapp.IOService.DoneJobs.PopulateTaskPagerWithFragmentsJob;
import humanity.com.taskapp.IOService.DoneJobs.TaskApiDoneJob;
import humanity.com.taskapp.IOService.JOBS.TaskApiJob;
import humanity.com.taskapp.R;

//Main acitivity of the app
public class TaskListActivity extends AppCompatActivity {

    TaskPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;

    private boolean loading = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        EventBus.getDefault().register(this);

        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ///TODO

        fetchTasks(true);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().unregister(mCustomPagerAdapter);
        super.onStop();
    }

    private void setupUI()
    {
        mCustomPagerAdapter = new TaskPagerAdapter(getSupportFragmentManager(), this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.tasks);
    }

    private void populateUI(boolean reload)
    {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//           // recyclerViewAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void fetchTasks(boolean reload)
    {
        CancelableCallback.cancelAll();

        EventBus.getDefault().post(new TaskApiJob(reload));
    }


    public void onEventBackgroundThread(TaskApiDoneJob event)
    {
        if(event.succes && event.groupedTasks!=null) {

            EventBus.getDefault().postSticky(new PopulateTaskPagerWithFragmentsJob(event));
        }
        EventBus.getDefault().removeStickyEvent(event);

    }

}
