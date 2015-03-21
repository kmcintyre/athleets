package com.athleets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;  
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

	
	/*
	 * We're only gettting 'notifications'
	 */
	private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {		
		
		@Override
		public void onReceive(Context context, Intent intent) {			
			String notification = intent.getExtras().getString(ConnectIntentService.NOTIFICATION);
			Log.i(notificationReceiver.getClass().getSimpleName(), notification);			
			Toast.makeText(MainActivity.this, notification, Toast.LENGTH_LONG).show();
			if ( notification == ConnectIntentService.NOTIFICATION_CONNECT ) {

				//context.getApplicationContext()
				//Intent i = new Intent(getParent(), ConnectIntentService.MESSAGE);
				//context.sendBroadcast(intent);
				//Intent connect = new Intent(this, ConnectIntentService.class);
				//connect.setAction();				
			}
		}
		
	};
	
	/*
	 * We're only gettting 'json'
	 */
	private BroadcastReceiver jsonReceiver = new BroadcastReceiver() {		
		
		@Override
		public void onReceive(Context context, Intent intent) {
		}
		
	};	
	
	/*
	 * We're only gettting 'binary'
	 */
	private BroadcastReceiver binaryReceiver = new BroadcastReceiver() {		
		
		@Override
		public void onReceive(Context context, Intent intent) {			
		}
		
	};		
	
	@Override
	protected void onResume() {
		Log.d("MainActivity", "onResume");
		super.onResume();		
		registerReceiver(notificationReceiver, new IntentFilter(ConnectIntentService.NOTIFICATION));
	}

	@Override
	protected void onPause() {
		Log.d("MainActivity", "onPause");		
		super.onPause();
		unregisterReceiver(notificationReceiver);
	}	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        } else {
        	Log.i("MainActivity", "onCreate-savedInstanceState");
        }
        
		//Intent connect = new Intent(this, ConnectIntentService.class);
		//connect.setAction(ConnectIntentService.ACTION_CONNECT);
		//startService( connect );
        
		registerReceiver(notificationReceiver, new IntentFilter(ConnectIntentService.NOTIFICATION));
		
		
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

   
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        	
        	WebView myWebView = (WebView)rootView.findViewById(R.id.webview);
            myWebView.loadUrl("http://www.whatismyip.com");
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);            
            return rootView;
        }
    }

}
