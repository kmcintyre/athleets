package com.athleets;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

public class ConnectIntentService extends IntentService {

	public static final String NOTIFICATION = "notification";
	
	public static final String NOTIFICATION_CONNECT = "connect";	
	public static final String NOTIFICATION_DISCONNECT = "disconnect";
	public static final String NOTIFICATION_RECONNECT = "reconnect";
	
	public static final String MESSAGE = "message";	
	public static final String MESSAGE_JSON = "message_json";
	public static final String MESSAGE_TWEET = "message_tweet";	
	
	public static final String ACTION_CONNECT = "connect";
	public static final String ACTION_DISCONNECT = "disconnect";
	
	public static final WebSocket mConnection = new WebSocketConnection();
	
	public ConnectIntentService() {
		super("ConnectIntentService");
		Log.i("ConnectIntentService", "constructor");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {				
		Log.i("ConnectIntentService", "isconnected:" + mConnection.isConnected() + " action:" + intent.getAction() + " type:" + (intent.getType() == null ? "null" : intent.getType()) + " onHandleIntent:" + intent.getDataString() + " flags:" + intent.getFlags());		
		if ( intent.getAction().equals(ACTION_CONNECT) ) {
			if ( !mConnection.isConnected() ) {
				Log.i("ConnectIntentService", "connect");
				connect();
			} else {
				Log.i("ConnectIntentService", "re-connect");
	            Intent connected = new Intent(NOTIFICATION);
	            intent.putExtra(NOTIFICATION, NOTIFICATION_RECONNECT);
	            sendBroadcast(connected);				
			}
		} else if ( intent.getAction().equals(ACTION_DISCONNECT) && mConnection.isConnected() ) {
			Log.i("ConnectIntentService", "disconnect");
			mConnection.disconnect();
		} else if ( intent.getAction().equals(MESSAGE) ) {
			Log.i("ConnectIntentService", "message");			
		} else  {
			Log.i("ConnectIntentService", "unknown intent");			
		}
	}
	
	private void connect() {
	   
      final String wsuri = "ws://" + 
    		  PreferenceManager.getDefaultSharedPreferences(this).getString("settings_host", "dev.e2brute.com") + 
    		  ":" + 
    		  PreferenceManager.getDefaultSharedPreferences(this).getString("settings_port", "8080");
      
      Log.i("ConnectIntentService", "connect to:" + wsuri);
      
      try {    	  
    	  mConnection.connect(wsuri, new WebSocketConnectionHandler() {
            
        	@Override
            public void onOpen() {
               Log.i("ConnectIntentService", "onOpen()");
               Intent intent = new Intent(NOTIFICATION);
               intent.putExtra(NOTIFICATION, NOTIFICATION_CONNECT);
               sendBroadcast(intent);            
            }

            @Override
            public void onTextMessage(String message) {
               Log.i("ConnectIntentService", "onTextMessage:" + message);
               Intent intent = new Intent(MESSAGE);
               intent.putExtra(MESSAGE_JSON, message);
               sendBroadcast(intent);
            }
            
            @Override
            public void onBinaryMessage(byte[] payload) {
               Log.i("ConnectIntentService", "onBinaryMessage:" + payload);
               Intent intent = new Intent(MESSAGE);
               intent.putExtra(MESSAGE_TWEET, payload);
               sendBroadcast(intent);
            }            

            @Override
            public void onClose(int code, String reason) {               
               Log.i("ConnectIntentService", "Connection lost - " + code + " reason:" + reason);
               Intent intent = new Intent(NOTIFICATION);
               intent.putExtra(NOTIFICATION, NOTIFICATION_DISCONNECT);
               sendBroadcast(intent);
            }                        
         });
      } catch (WebSocketException e) {
         Log.e("ConnectIntentService", "websocketexception:" + e.toString());
      }
	}
	
}