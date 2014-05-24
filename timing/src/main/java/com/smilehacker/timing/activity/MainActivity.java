package com.smilehacker.timing.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.smilehacker.timing.R;
import com.smilehacker.timing.fragment.AppRecordFragment;
import com.smilehacker.timing.fragment.CategotyMenuFragment;
import com.smilehacker.timing.model.event.CategoryEvent;
import com.smilehacker.timing.service.AppListenerService;
import com.smilehacker.timing.util.DateHelper;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private EventBus mEventBus;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mEventBus = EventBus.getDefault();
        mEventBus.register(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_container);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new AppRecordFragment())
                .replace(R.id.left_drawer, new CategotyMenuFragment())
                .commit();
        startListenerService();

        initNavigationDrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    private void initNavigationDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void onEvent(CategoryEvent event) {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_time){
            DateHelper.getDate(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
        
    }

    private void startListenerService() {
        Intent intent = new Intent(this, AppListenerService.class);
        intent.putExtra(AppListenerService.KEY_COMMAND, AppListenerService.COMMAND_START_TRICK);
        startService(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
