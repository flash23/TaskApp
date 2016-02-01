package humanity.com.taskapp.Activity.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.Activity.Main.Fragment.EmptyFragment.EmptyFragment;
import humanity.com.taskapp.Activity.Main.Fragment.TaskFrakment.ControlSpinnersEvent;
import humanity.com.taskapp.Activity.Main.Fragment.TaskFrakment.TasksFragment;
import humanity.com.taskapp.IOService.DoneJobs.PopulateTaskFragmentsJob;
import humanity.com.taskapp.IOService.DoneJobs.PopulateTaskPagerWithFragmentsJob;
import humanity.com.taskapp.IOService.MODEL.TaskItemModel;

/**
 * Created by mirkomesner on 1/26/16.
 */
class TaskPagerAdapter extends FragmentPagerAdapter {

    TaskListActivity mContext;
    public Map<Date, List<TaskItemModel>> groupedTasks = null;

    TimeFrame timeFrame;

    private class TimeFrame {
        Date startDate;
        Date endDate;

        public TimeFrame(Date startDate, Date endDate)
        {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public long getDaysBetween()
        {
            long diff = -1;
            try {
                diff = Math.round((endDate.getTime() - startDate.getTime()) / (double) 86400000);
            } catch (Exception e) {
            }
            return diff==0?1:(diff+1);
        }

        public long getDaysToday()
        {
            long diff = -1;
            try {
                diff = Math.round((new Date().getTime() - startDate.getTime()) / (double) 86400000);
            } catch (Exception e) {
            }
            return diff;
        }
    }

    public TaskPagerAdapter(FragmentManager fm, TaskListActivity context) {
        super(fm);
        mContext = context;

        timeFrame = new TimeFrame(new Date(), new Date());

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().registerSticky(this);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        if(groupedTasks != null) {

            Calendar c = Calendar.getInstance();
            c.setTime(timeFrame.startDate);
            c.add(Calendar.DATE, position);

            if(groupedTasks.containsKey(c.getTime()))
                fragment = new TasksFragment(c.getTime());
            else
                fragment = new EmptyFragment();
        }
        else
        {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            fragment = new TasksFragment(cal.getTime());
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return (int)timeFrame.getDaysBetween();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Date toShowDate = null;
        if(groupedTasks!=null) {
            Calendar c = Calendar.getInstance();
            c.setTime(timeFrame.startDate);
            c.add(Calendar.DATE, position);

            toShowDate = c.getTime();
        }
        else
            toShowDate =  new Date();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(toShowDate);
    }

    public void scrollToday()
    {
        mContext.mViewPager.setCurrentItem((int)timeFrame.getDaysToday()-1, false);
    }

    public void onEventBackgroundThread(PopulateTaskPagerWithFragmentsJob event)
    {
        if(event.succes) {
            Set<Date> kset = event.groupedTasks.keySet();
            Date []keysDate = new Date[kset.size()];
            kset.toArray(keysDate);

            Arrays.sort(keysDate);

            if(keysDate.length>1)
                timeFrame = new TimeFrame(keysDate[0], keysDate[keysDate.length - 1]);
            else
                timeFrame = new TimeFrame(new Date(), new Date());

            long dif = timeFrame.getDaysBetween();
            final PopulateTaskPagerWithFragmentsJob fevent = event;
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    groupedTasks = fevent.groupedTasks;
                    notifyDataSetChanged();
                    if(mContext.mViewPager.getCurrentItem()<1)
                        scrollToday();
                }
            });

            EventBus.getDefault().postSticky(new PopulateTaskFragmentsJob(event));
            EventBus.getDefault().removeStickyEvent(event);

            EventBus.getDefault().removeStickyEvent(ControlSpinnersEvent.class);
            EventBus.getDefault().postSticky(new ControlSpinnersEvent(false));
        }
    }

}
