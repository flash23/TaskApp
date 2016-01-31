package humanity.com.taskapp.Activity.Main.Fragment.EmptyFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;
import humanity.com.taskapp.Activity.Main.Fragment.TaskFrakment.ControlSpinnersEvent;
import humanity.com.taskapp.Activity.Main.Fragment.TaskFrakment.TasksRecyclerViewAdapter;
import humanity.com.taskapp.IOService.JOBS.TaskApiJob;
import humanity.com.taskapp.R;

/**
 * Created by mirkomesner on 1/31/16.
 */
public class EmptyFragment extends Fragment {

    public SwipeRefreshLayout dummyRefresher;///we dont load date in empty fragment
    public RecyclerView recyclerView;
    private TasksRecyclerViewAdapter recyclerViewAdapter;

    public EmptyFragment() {
        super();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_empty, container, false);

        dummyRefresher = (SwipeRefreshLayout)root.findViewById(R.id.tasksrefresher);
        dummyRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new TaskApiJob(true));
            }
        });

        recyclerView=(RecyclerView) root.findViewById(R.id.taskrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewAdapter=new TasksRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);

        setupUI(root);

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().registerSticky(this);

        return root;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && getView()!=null) {
            if(!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().registerSticky(this);
        }
        else
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    private void setupUI(View root)
    {

    }

    public void onEventBackgroundThread(ControlSpinnersEvent event)
    {
        final ControlSpinnersEvent fevent = event;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dummyRefresher.setRefreshing(fevent.animate);
            }
        });

    }
}
