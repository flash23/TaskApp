package humanity.com.taskapp.Activity.Main.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.IOService.DoneJobs.TaskApiDoneJob;
import humanity.com.taskapp.IOService.JOBS.TaskApiJob;
import humanity.com.taskapp.R;

/**
 * Created by mirkomesner on 1/26/16.
 */
public class TasksFragment extends Fragment {

    public SwipeRefreshLayout refresher;
    public RecyclerView recyclerView;
    private ActionBar actionBar;
    private EditText search;
    private CheckBox instockCheckBox;
    private TasksRecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_task_list, container, false);

        setupUI(root);

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().registerSticky(this);///we try do get date if we are visible

        return root;
    }

    private void setupUI(View root)
    {
        refresher = (SwipeRefreshLayout)root.findViewById(R.id.tasksrefresher);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new TaskApiJob(true));
            }
        });

        recyclerView=(RecyclerView) root.findViewById(R.id.taskrecyclerview);
        recyclerViewAdapter=new TasksRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());//

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        //EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //we always get latest data because we are always getting  "sticky persistant event"
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if(!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().registerSticky(this);
        }
        else
            EventBus.getDefault().unregister(this);
    }

    int itemLoading = -1;
    public void onEventBackgroundThread(TaskApiDoneJob event)
    {
        if(event.succes) {
            if(event.reloaded) {
                //context.previousTotal = 0;
                if(recyclerViewAdapter.items.size()>0)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(0);
                        }
                    });
                recyclerViewAdapter.items = event.taskItemModelList;

            } else
                recyclerViewAdapter.items.addAll(event.taskItemModelList);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerViewAdapter.notifyDataSetChanged();

                }
            });
        }


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresher.setRefreshing(false);

            }
        });

        //We kill all the spinners
        EventBus.getDefault().removeStickyEvent(ControlSpinnersEvent.class);

    }

    public void onEventBackgroundThread(ControlSpinnersEvent event)
    {
        final ControlSpinnersEvent fevent = event;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresher.setRefreshing(fevent.animate);

            }
        });

    }
}