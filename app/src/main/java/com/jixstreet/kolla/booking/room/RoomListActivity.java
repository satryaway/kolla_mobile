package com.jixstreet.kolla.booking.room;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.content_room_list)
public class RoomListActivity extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.toolbar_title_tv)
    protected TextView toolbarTitleTv;

    @ViewById(R.id.room_rv)
    protected RecyclerView roomRv;

    private RoomListAdapter roomListAdapter;

    @AfterViews
    void onViewsCreated() {
        modifyStatusBar();
        initAdapter();
        setValue();
    }

    private void modifyStatusBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initAdapter() {
        roomListAdapter = new RoomListAdapter(this);
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(this, true);
        roomRv.setNestedScrollingEnabled(false);
        roomRv.setClipToPadding(true);
        roomRv.setLayoutManager(layoutManager);
        roomRv.setItemAnimator(new DefaultItemAnimator());
        roomRv.setAdapter(roomListAdapter);
    }

    private void setValue() {
        //dummy
        roomListAdapter.setRooms(Seeder.getRoomList());
        toolbarTitleTv.setText(getString(R.string.locations_available, Seeder.getRoomList().size()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }
}
