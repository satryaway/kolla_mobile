package com.jixstreet.kolla;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jixstreet.kolla.booking.category.BookingFragment;
import com.jixstreet.kolla.event.EventFragment;
import com.jixstreet.kolla.news.NewsFragment;
import com.jixstreet.kolla.topup.TopUpListActivity;
import com.jixstreet.kolla.topup.TopUpListActivity_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;
import com.jixstreet.kolla.view.BottomNavigationHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
        implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String BOOKING = "booking";
    private static final String EVENT = "event";
    private static final String NEWS = "news";

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    protected NavigationView navigationView;

    @ViewById(R.id.bottom_navigation)
    protected BottomNavigationView bottomNavigationView;

    @ViewById(R.id.toolbar_title_tv)
    protected TextView toolbarTitleTv;

    @ViewById(R.id.appbar)
    protected AppBarLayout appBarLayout;
    private Fragment previousFragment;
    private BookingFragment bookingFragment;
    private NewsFragment newsFragment;
    private EventFragment eventFragment;

    @AfterViews
    void onViewsCreated() {
        modifyActionBar();
        initDrawer();

        newsFragment = NewsFragment.newInstance();
        setContent(newsFragment, NEWS);
    }

    private void modifyActionBar() {
        ViewUtils.setToolbar(this, toolbar);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setContent(Fragment fragment, String tag) {
        if (fragment == null && previousFragment == null) return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment savedFragment = fragmentManager.findFragmentByTag(tag);

        if (savedFragment == null) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_effect, R.anim.fade_out_effect)
                    .add(R.id.content_wrapper, fragment, tag)
                    .commit();

            previousFragment = fragment;
        } else {
            if (!previousFragment.getTag().equals(tag)) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in_effect, R.anim.fade_out_effect)
                        .show(fragment)
                        .hide(previousFragment)
                        .commit();
                previousFragment = fragment;
            }
        }
    }

    private void setToolbarColorTransparent(boolean isTransparent) {
        if (isTransparent) {
            appBarLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        } else {
            appBarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        }
    }

    private void setToolbarTitle(String toolbarTitle) {
        toolbarTitleTv.setText(toolbarTitle);
    }

    boolean doubleBackToExitPressedOnce = false;

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            View view = bottomNavigationView.findViewById(id);
            if (view != null)
                view.performClick();
            else {
                if (id == R.id.action_credit) {
                    ActivityUtils.startActivityAndWait(MainActivity.this, TopUpListActivity_.class,
                            TopUpListActivity.requestCode);
                }

                drawer.closeDrawer(GravityCompat.START);
            }

            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            DialogUtils.makeToast(this, getString(R.string.tap_twice_to_exit));

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        String tag = "";

        String toolbarTitle = "";
        boolean isTransparent = false;

        if (id == R.id.action_profile) {
        } else if (id == R.id.action_settings) {

        } else if (id == R.id.action_help) {

        } else if (id == R.id.action_logout) {

        } else if (id == R.id.action_credit) {
            ActivityUtils.startActivityAndWait(this, TopUpListActivity_.class,
                    TopUpListActivity.requestCode);
        } else if (id == R.id.action_news) {
            if (newsFragment == null)
                newsFragment = NewsFragment.newInstance();
            fragment = newsFragment;
            toolbarTitle = getString(R.string.news);
            tag = NEWS;
        } else if (id == R.id.action_booking) {
            if (bookingFragment == null)
                bookingFragment = BookingFragment.newInstance();

            fragment = bookingFragment;
            toolbarTitle = getString(R.string.your_kolla_credits);
            isTransparent = true;
            tag = BOOKING;
        } else if (id == R.id.action_events) {
            if (eventFragment == null)
                eventFragment = EventFragment.newInstance();

            fragment = eventFragment;
            toolbarTitle = getString(R.string.events);
            tag = EVENT;
        }

        navigationView.setCheckedItem(id);

        if (fragment != null) {
            setContent(fragment, tag);
            setToolbarTitle(toolbarTitle);
            setToolbarColorTransparent(isTransparent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
