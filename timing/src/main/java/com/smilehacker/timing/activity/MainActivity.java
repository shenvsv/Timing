package com.smilehacker.timing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.smilehacker.timing.R;
import com.smilehacker.timing.service.AppListenerService;
import com.smilehacker.timing.util.AppListener;
import com.smilehacker.timing.util.DLog;


public class MainActivity extends Activity {

    private Button mBtnShow;

    private AppListener mAppListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppListener = new AppListener(this);

        findView();
        initView();
    }

    private void findView() {
        mBtnShow = (Button) findViewById(R.id.btn_show);
    }


    private void initView() {
        mBtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AppListenerService.class);
                intent.putExtra(AppListenerService.KEY_COMMAND, AppListenerService.COMMAND_START_TRICK);
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(this, AppListenerService.class);
        stopService(intent);
    }

    private void getForeground() {
        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                DLog.i("foreground = " + mAppListener.getForegroundPackage());
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
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
}
