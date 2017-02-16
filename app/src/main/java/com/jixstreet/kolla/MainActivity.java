package com.jixstreet.kolla;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jixstreet.kolla.news.NewsFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

@EActivity(R.layout.activity_home)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.drawer)
    protected DrawerLayout drawerLayout;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.navigation_view)
    protected NavigationView navigationView;

    @ViewById(R.id.content_wrapper)
    protected FrameLayout contentWrapper;

    @AfterViews
    void onViewsCreated() {
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        initContent();
    }

    public void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.category_1:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initContent() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_wrapper, NewsFragment.newInstance(), "news")
                .commit();
    }

}
