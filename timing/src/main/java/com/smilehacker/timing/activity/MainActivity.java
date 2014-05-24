package com.smilehacker.timing.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.smilehacker.timing.R;
import com.smilehacker.timing.fragment.AppRecordFragment;
import com.smilehacker.timing.fragment.CategotyMenuFragment;
import com.smilehacker.timing.service.AppListenerService;
import com.smilehacker.timing.util.AppListener;
import com.smilehacker.timing.util.DLog;


public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new AppRecordFragment())
                .replace(R.id.left_drawer, new CategotyMenuFragment())
                .commit();
        startListenerService();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
        
    }

    private void startListenerService() {
        Intent intent = new Intent(this, AppListenerService.class);
        intent.putExtra(AppListenerService.KEY_COMMAND, AppListenerService.COMMAND_START_TRICK);
        startService(intent);
    }
}
