package com.jixstreet.kolla;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginResult;
import com.jixstreet.kolla.booking.BookingFragment_;
import com.jixstreet.kolla.intro.IntroActivity_;
import com.jixstreet.kolla.login.LoginJson;
import com.jixstreet.kolla.news.NewsFragment;
import com.jixstreet.kolla.news.NewsFragment_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String BOOKING = "booking";
    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    protected NavigationView navigationView;

    @ViewById(R.id.bottom_navigation)
    protected BottomNavigationView bottomNavigationView;

    private static final String NEWS = "news";
    private Fragment previousFragment;

    @AfterViews
    void onViewsCreated() {
        if (LoginJson.Response.getAccessToken(this) == null) {
            ActivityUtils.startActivity(this, IntroActivity_.class);
            finish();
            return;
        }
        modifyActionBar();
        initDrawer();
        setContent(NewsFragment.newInstance(), NEWS);
    }

    private void modifyActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setContent(Fragment fragment, String tag) {
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
                        .replace(R.id.content_wrapper, fragment, tag)
                        .commit();
                previousFragment = fragment;
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;

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
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.action_news) {
            fragment = new NewsFragment_();
            tag = NEWS;
        } else if (id == R.id.action_booking) {
            fragment = new BookingFragment_();
            tag = BOOKING;
        }

        if (fragment != null)
            setContent(fragment, tag);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
