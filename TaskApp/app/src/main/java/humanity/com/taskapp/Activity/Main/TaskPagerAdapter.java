package humanity.com.taskapp.Activity.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import humanity.com.taskapp.Activity.Main.Fragment.TasksFragment;

/**
 * Created by mirkomesner on 1/26/16.
 */
class TaskPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public TaskPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        // Create fragment object
        Fragment fragment = new TasksFragment();

        Bundle args = new Bundle();
        args.putInt("page_position", position + 1);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + (position + 1);
    }
}
