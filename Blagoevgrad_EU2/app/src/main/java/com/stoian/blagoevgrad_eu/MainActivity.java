package com.stoian.blagoevgrad_eu;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private Handler mHandler = new Handler();
    private PowerSendMessage myReceiver;
    private WebView myWebView;
    private static int countPowerOff = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://www.blagoevgrad.eu/");
        myWebView.setWebViewClient(new MyAppWebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        myReceiver = new PowerSendMessage();
        registerReceiver(myReceiver, filter);
    }

    public void notifyNewNews(){
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.emedia_blagoevgrad_eu)
                        .setContentTitle("Blagoevgrad EU")
                        .setContentText("View new NEWS in Blagoevgrad EU");

        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        mBuilder.setContentIntent(resPendingIntent);

        int mNotificationID = 001;

        NotificationManager mNotifyMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMan.notify(mNotificationID, mBuilder.build());

    }


    @Override
    public void onBackPressed() {
        if(myWebView.canGoBack()){
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }


    private class PowerSendMessage extends BroadcastReceiver {


        public PowerSendMessage ()
        {

        }
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
            {
                Log.e("In on receive", "In Method:  ACTION_SCREEN_OFF");
                countPowerOff++;
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
            {
                String str = new String(String.valueOf(countPowerOff));
                Log.e("In on receive", str);
                if (countPowerOff > 20)
                {
                    countPowerOff=0;
                    Toast.makeText(context, "MAIN ACTIVITY IS BEING CALLED ", Toast.LENGTH_LONG).show();
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(2000);
                    notifyNewNews();
                }
            }
            else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT))
            {
                if (myReceiver != null)
                {
                    unregisterReceiver(myReceiver);
                    myReceiver = null;
                }
                Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");
                if (countPowerOff > 20)
                {

                    countPowerOff=0;
                    String str = new String(String.valueOf(countPowerOff));
                    Toast.makeText(context, "MAIN ACTIVITY IS BEING CALLED ", Toast.LENGTH_LONG).show();
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(2000);
                    notifyNewNews();
                }

            }
        }


    }

    @Override
    protected void onDestroy() {
        if (myReceiver != null)
        {
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        super.onDestroy();
    }
}

