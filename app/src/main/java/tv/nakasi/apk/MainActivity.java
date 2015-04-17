package tv.nakasi.apk;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import tv.nakasi.apk.ShakeListener.OnShakeListener;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.speech.RecognizerIntent;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.SmartScreen.tv.R;
import com.ypcloud.WebService;
import com.ypcloud.app2web.App2WebCallBack;
import com.ypcloud.app2web.App2WebView;


public class MainActivity  extends Activity  implements CreateNdefMessageCallback {
	
	// Class configuration.
	final static private boolean 	_DEFINE_APP_SHARKE_IS_ENABLE	= false;
	final static private boolean	_DEFINE_DEBUG_MSG_IS_ENABLE		= false;
	final static private boolean	_DEFINE_INFO_MSG_IS_ENABLE		= true;
	final static private boolean	_DEFINE_ERROR_MSG_IS_ENABLE		= true;
	
	final static private String		_DEFINE_WEBAPP_URL				= "http://SmartScreen.tv/";
	//final static private String		_DEFINE_WEBAPP_URL				= "http://la32.ypcall.com/smartscreen/";
	final static private String 	_DEFINE_WEBSERVICE_API_URL		= "http://pqd.cc/info/";
	
	private boolean 				_nfcAdapterCreated;
	private String 					_keyboardStringRecorder;		// recording all keyboard input.
	private int 					_keyboardInputCounter; 			// counter for filter nfc number.
	
	public	static	MainActivity 	mainActivity;					// apk activity instance.
	public 	String 					androidID ;
	
	
	RelativeLayout L1;
	private ProgressBar probar; //loading bar
	private SensorManager mSensorManager; 
    private Sensor mAccelerometer;
    private ShakeListener mShakeListener;
	private boolean re_entry = false; 
    private Thread thread;   //watch dog
    private boolean NFC_mNfcAdapter_boolean = false;//�O�_��NFC�\�� 

	protected static final int RESULT_SPEECH = 1;
	protected static boolean loadBoot; // If Boot = true
    protected static String DDN;// DDN number
    protected boolean RecordSoundBooline=false;//�ݿ�\��ϧ_�}��
    protected	 SimpleDateFormat formatter;//�{�b�ɶ��T��
    protected	 String NowTime;                       //�{�b�ɶ��T��

    public static String ActiveValue;// �����̹�����,�ثe�令��̡A�S���ϥΡC
    //MediaPlayer media_music; // music
    public screen_size screensize; //screen_size.java
    public static MainActivity mainactivity; //MainActivity ����ŧi


    public NFC nfc = new NFC();
    public NfcAdapter mNfcAdapter;

    public bluetooth bluetooth = new bluetooth();
    private BluetoothAdapter myBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    public CardEven cardeven = new CardEven(); //CardEven.java  �M��B�z�S�w�\��d
    public Record_Sound recorder1;  //��
    public ConnectivityManager conns;// test�s�u
    public App2WebView wv; // �s�u��WebView
    public DecideConnect decideConnect; // decide connect
    public ImageView im;// loading_background_white
	public String JudgePortraitLandscape = ""; // Judge portrait landscape
	public VideoView vv; // Boot video
	public String CardNumber;

	PendingIntent mNfcPendingIntent;
	IntentFilter[] mWriteTagFilters;
	IntentFilter[] mNdefExchangeFilters;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
		// getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		setContentView(R.layout.activity_main);
		mainactivity = this;
		
		androidID				=	Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
	  	_nfcAdapterCreated		= 	false;
		mainActivity 			= 	this;
		_keyboardStringRecorder	=	"";
		_keyboardInputCounter	=	0;

		
		// startService(new Intent(this, MyService.class));
		wv = (App2WebView) findViewById(R.id.webView1);
		wv.setVisibility(WebView.INVISIBLE);
		vv = (VideoView) findViewById(R.id.videoView1);
		// media_music = MediaPlayer.create(this, R.raw.jujuemusic);
		wv.setWebChromeClient(new WebChromeClient());
		probar = (ProgressBar) findViewById(R.id.progressBar1);
		im = (ImageView) findViewById(R.id.imageView1);
		L1 = (RelativeLayout) findViewById(R.id.LinearLayout01);
		wv.getSettings()
		.setUserAgentString(
		"Mozilla/5.0 (X11;Linux86_64) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.34 Safari/534.24");
		wv.getSettings().setLoadWithOverviewMode(true);// loads the WebView													                                                	// completely zoomed out
		wv.getSettings().setJavaScriptEnabled(true);
		//wv.getSettings().getBuiltInZoomControls();
		//wv.getSettings().setPluginsEnabled(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setPluginState(PluginState.ON);
		wv.getSettings().setSupportZoom(true);	
		
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wv.addJavascriptInterface(new js2app(), "Web2App");
		
		conns = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		

		
		screensize = new screen_size(this);
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		 
		// Register sharke function.
		if( _DEFINE_APP_SHARKE_IS_ENABLE )
		{
			mShakeListener=new ShakeListener(); 
			mShakeListener.setOnShakeListene(new OnShakeListener() {		
				@Override
				public void onShake(int count) {
					// TODO Auto-generated method stub
					if(count==1){				
						try {
							onNfcLoad("0000000000");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
		
		
	    // oncreate NFCreader
		if (loadBoot == true) { // First Boot Method , Close in video
			// video();
			vv.setVisibility(View.GONE);
			getActiveNetworkInfo();
		} else {
			vv.setVisibility(View.GONE);
			getActiveNetworkInfo();
		}

        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
		
		NFCreader();

        // oncreate NFCreader
        BTCreader();

	}
	
	public class js2app {
		
            public void video_play() {
                    wv.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
            }


            public void Speech()
            {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                    try
                    {
                            startActivityForResult(intent, RESULT_SPEECH);
                    }
                    catch (ActivityNotFoundException a)
                    {
                            infoMessage("Not support voice.");
                    }
            }

            public void getUDID()
            {
                    wv.loadUrl("javascript:setUDID('"+ androidID +"')");
            }
            /*public void PutNFC( String NFC )
            {
                    wv.loadUrl("javascript:PutNFC('"+ NFC +"')");
            }*/

	}
	
	/*
    @Override
    protected void onPause() {
        super.onPause();
        wv.onPause();
    }
 
    @Override
    protected void onResume() {
        wv.onResume();
        super.onResume();
    }
	*/

    private void NFCreader()
    {
        //NFC defaultAdapter
        // TODO Auto-generated method stub
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter ==null) {
            return;
        }
        else{
            NFC_mNfcAdapter_boolean =true;
        }

        mNfcAdapter.setNdefPushMessageCallback(this,this);

    }

    private void BTCreader()
    {

        // take an instance of BluetoothAdapter - Bluetooth radio
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetooth.on( myBluetoothAdapter );
        registerReceiver( bReceiver , new IntentFilter(BluetoothDevice.ACTION_FOUND) );

    }


    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // add the name and the MAC address of the object to the arrayAdapter
                    infoMessage( device.getName() + "\n" + device.getAddress() );

                    wv.loadUrl("javascript:PutBT('"+ device.getName() + "," + device.getAddress() +"')");

            }

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                NetworkInfo WFIdevice = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                infoMessage( "wifi1 => " + WFIdevice.toString() );
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                NetworkInfo WFIdevice = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                infoMessage( "wifi2 => " + WFIdevice.toString() );
                    //boolean connected = WFIdevice.isConnected();
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections
                NetworkInfo WFIdevice = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                infoMessage( "wifi3 => " + WFIdevice.toString() );
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
                NetworkInfo WFIdevice = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                infoMessage( "wifi4 => " + WFIdevice.toString() );
            }
        }
    };


    @Override
	public void onNewIntent(Intent intent) {
		
		//Ū��NFC�ƥ�A�d���Ǩ�NFC.java�̰��s�Xfnction
		// onResume gets called after this to handle the intent
		Tag myTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		byte[] id = myTag.getId();
		
		if (nfc.getDec(id) == 0) {
			// do nothing.
		} 
		else {
			try {
				MainActivity.mainactivity.onNfcLoad(String.valueOf(nfc.getDec(id)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Toast.makeText(this, "NFC:" + nfc.getDec(id), Toast.LENGTH_SHORT).show();
			setIntent(intent);
		}
	}

	// Record keyboard input and filting NFC number.
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
				
			
		Pattern pattern;			// pattern using to check input.
		Matcher matcher;			// matcher using to check input.
		String 	keyCodeString;		// keycode from event.
			
		try {
				
			// extend keyboard device input.
			if( event.getDeviceId() > 0 ){
					
				// is key down action.
				if(event.getAction() == 0){
							
					// check nfc input ended.
					if( event.getKeyCode() != event.KEYCODE_ENTER ){
							
						// nfc input.
						keyCodeString = String.valueOf(event.getNumber());
							
						if(keyCodeString != null){
								
							pattern = Pattern.compile("[0-9]*");	
							matcher = pattern.matcher(keyCodeString);
								
								
							if( matcher.matches() ){
								
								if(_keyboardInputCounter < 10){
									
									_keyboardStringRecorder += keyCodeString;
									_keyboardInputCounter ++;
										
								}
									
							}
								
						}
							
					}
					else {
							
						// nfc input ended.
						if(_keyboardInputCounter == 10){
								
							// got complete nfc number.
							onNfcLoad(_keyboardStringRecorder);

						}	
							
						_keyboardInputCounter 	= 0;
						_keyboardStringRecorder = "";
							
					}	
						
				}
					
			}
			else{
					
				// reset counter and recorder.
				_keyboardInputCounter 	= 0;
				_keyboardStringRecorder = "";
					
			}
		} 
		catch (Exception e) {
				
			// reset counter, recorder and disable flag.
			_keyboardInputCounter 	= 0;
			_keyboardStringRecorder = "";
				
		}
			
		return super.dispatchKeyEvent(event);		
	
	} // dispatchKeyEvent ended.
	

	// nfc card load event handler: Get QQNInfo by NFC number.
	// filter and handle:
	//		@voice,
	//		@home,
	//		snap.
	// forward object to webapp function 'receiveObjectFromNFC'.
	public void  onNfcLoad(String cardNumberString) throws JSONException{
		
	   	String 			qqnString;									// qqn in result.qqn
	    String			d1String;									// d1 from result.
	    String 			d3String;									// d3 from result.
	    	
	    JSONObject  	resultObject;								// result object.
	    JSONObject		qqninfoObject;								// qqninfo in result.qqninfo.
	    JSONObject		paramObject;								// paramter of web service.
	    	
	    WebService 		webService;									// web service.
	    	
	    infoMessage("NFC:" + cardNumberString);
	    wv.loadUrl("javascript:PutNFC('"+ cardNumberString +"')");

        bluetooth.find( myBluetoothAdapter );
        bluetooth.list( myBluetoothAdapter );

        //ScreenShot scrShot = new ScreenShot();
        //scrShot.screenshot(MainActivity.mainactivity.L1);
	    //wv.loadUrl("javascript:alert('NFC number: " + cardNumberString + "')");

        /*
	    try{
	    	
	    	// call api parameters.
	    	paramObject = new JSONObject();
	    	paramObject.put("q", cardNumberString);
	    	paramObject.put("u", androidID);
	    	
    		
	    	// get resurce by web service api.
	    	webService = new WebService( _DEFINE_WEBSERVICE_API_URL );
	    	resultObject = webService.get(paramObject);
	    		
	    	debugMessage( "resultObject:" + resultObject.toString());
	    		
	    		
	    	if(resultObject.length() > 0){
	    			
		    	// conver result to object.
		        qqninfoObject = resultObject.getJSONObject("QQNInfo");	
		        qqnString	= resultObject.getString("QQN");
	
		        	    
		        d1String = qqninfoObject.getString("d1");
		        d3String =  qqninfoObject.getString("d3");
		        	    
		        	    
		        d1String = d1String.replaceAll(" ", "");
		        d1String = d1String.toLowerCase();
		        d3String = d3String.replaceAll(" ", "");
		        d3String = d3String.toLowerCase();
	           	
	        	    
	           if(d3String.compareTo("@voice") == 0){
	            	
	        	   	speech();
	        	   	debugMessage("@voice command");
	        	    
	           }
	           else if(d3String.compareTo("@snap") == 0){
	        	    	
	    	       	ScreenShot scrShot = new ScreenShot();
	    	       	scrShot.screenshot(MainActivity.mainactivity.L1);
	    	       		
	    	   }
	           else	if(d3String.compareTo("@rec") == 0){
	    	       		
	    	       	MainActivity.mainactivity.RecordSoundBooline = true;
	    	       	MainActivity.mainactivity.RecordVoice(true);
	    	       		
	    	   }
	    	   else if(d3String.compareTo("@url") == 0){
	    	       	 	 
	    	       	if(d1String.compareTo("") != 0){
	    	           	
	    	       		MainActivity.mainactivity.wv.loadUrl("http://" + d1String);
	    	         
	    	       	}
	    	       	
	    	   }
	    	   else if(d3String.compareTo("@home") == 0){
	    	       		
	    	       	MainActivity.mainactivity.wv.loadUrl(_DEFINE_WEBAPP_URL);  

	    	   }
	    	   else {
	        	    	
	        	    try{
	            			
	            		wv.App2Web("receiveObjectFromNFC", qqninfoObject, new App2WebCallBack() {
	            				
	            			@Override
	            			public void Return(JSONObject a2wResultObject) {
	            					
	            				debugMessage( a2wResultObject.toString() );
	            					
	            			}
	            				
	            		});
	            			
	            	}
	            	catch (Exception e) {
	            			
	           			errMessage(e.getMessage());
	            		infoMessage("�{���L�k�^���A�Э��աC");
	            	
	            	}
	        	    	
    			}
	    			
	   		}
	   		else
            {
					
	   			// web service response error or null.
	   			errMessage("web service invoke error: " + _DEFINE_WEBSERVICE_API_URL);
	    		infoMessage("�{���L�k�^���A�Э��աC");
			}
	 
	   	}    
		catch(Exception e){
				
				errMessage(e.getMessage());
				infoMessage("try catch error");
		
		}*/
	    
	} // onNFCLoad ended.
	

	
	 // Connect detection function
	public void getActiveNetworkInfo() {
		
		NetworkInfo Info = conns.getActiveNetworkInfo();
		decideConnect = new DecideConnect(this);
		
	}// getActiveNetworkInfo ended.

	public void connectSuccess() {
		
		// network connection success.
		if(re_entry == false){
			
			if (JudgePortraitLandscape.compareTo("portrait") == 0) {
				
				//wv.loadUrl( _DEFINE_WEBAPP_URL + "?mode=app&udid=" + androidID + "&height=" + screensize.height + "&width=" + screensize.width + "&R=S");
				wv.loadUrl( _DEFINE_WEBAPP_URL + "?mode=app&d=" + androidID );
				
				//wv.loadUrl("javascript:alert('UDID: " + androidID + "')");
				
			} 
			else {
				
				//wv.loadUrl( _DEFINE_WEBAPP_URL + "?mode=app&udid=" + androidID + "&height=" + screensize.height + "&width=" + screensize.width + "&R=H");
				wv.loadUrl( _DEFINE_WEBAPP_URL + "?mode=app&d=" + androidID );
				
				//wv.loadUrl("javascript:alert('UDID: " + androidID + "')");
			}
			
			onPageFinished();
		}
		
	}// connectSuccess ended.

	public void onPageFinished() {
		
		wv.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onPageFinished(WebView view, String url) {
				
				super.onPageFinished(wv, url);
				
				decideConnect.interrupt();
				probar.setVisibility(View.GONE);
				im.setVisibility(View.GONE);
				wv.setVisibility(WebView.VISIBLE);
				re_entry =true;
				//wv.loadUrl("javascript:$('#you').attr('src','//www.youtube.com/embed/yBdy98j7F9o?autoplay=1')");
				//wv.loadUrl("javascript:$('body').append( '<iframe width='420' height='315' src='//www.youtube.com/embed/yBdy98j7F9o?autoplay=1' frameborder='0' allowfullscreen></iframe>' )");
				//wv.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
				//wv.loadUrl("javascript: alert('android call play')");
			}
		
		});
		
	}// onPageFinished ended.
	

	public Handler myhandHandler = new Handler() {
		
		public void handleMessage(Message msg) {
		
			super.handleMessage(msg);
			
			DDN = msg.getData().getString("ddn");
			// mediaplay = msg.getData().getString("mediavalue");
			ActiveValue = msg.getData().getString("ActiveValue");
			
		}
		
	};// myhandHandler ended.

	@Override
	public void onResume() {

		super.onResume();

		if(NFC_mNfcAdapter_boolean == true){
			
			enableNdefExchangeMode();
			
		}
		 
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			
			nfc.processIntent( getIntent() );
			
		}
		
		mSensorManager.registerListener(mShakeListener, mAccelerometer,    SensorManager.SENSOR_DELAY_UI);
		
	}// onResume ended.

	@Override
	public void onPause() {
		
		// Add the following line to unregister the Sensor Manager onPause
		mSensorManager.unregisterListener(mShakeListener);
	    super.onPause();

	}// onPause ended.

	//beam function  
	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0)
	{
		
		Time time = new Time();
		time.setToNow();
		String text = ("Beam me up!\n\n" + "Beam Time: " + time.format("%H:%M:%S"));
		NdefMessage msg = new NdefMessage(new NdefRecord[] { nfc.createMimeRecord("application/tv.nakasi.apk", text.getBytes()) });
		
		return msg;
		
	}// createNdefMessage ended.
	
	//�y�����Ѧ����T�B
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{

		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
			
			case RESULT_SPEECH: {
			
				if (resultCode == RESULT_OK && null != data) {
					
					ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					wv.loadUrl( "javascript:VoiceSearch('"+ text.get(0)+"')");
					
					//wv.loadUrl( "javascript:alert('"+ text.get(0)+"')");
					infoMessage(text.get(0));
				}
				
				break;

			}
			
		}
		
	}// onActivityResult ended.

	public void speech( ) //�}�һy������
	{

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");	
		try {
			
			startActivityForResult(intent, RESULT_SPEECH);
		
		}
		catch (ActivityNotFoundException a) {
		
			infoMessage("Not support voice.");
	
		}
		
	}// speech ended.

	// to solve prevoius peage rerun problem.
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		
        if(event.getAction() == KeyEvent.ACTION_DOWN){
        	
            switch(keyCode)
            {
            
	            case KeyEvent.KEYCODE_BACK:   
	            	
	                if(!wv.canGoBack()){
	                
	                	try{
	                		
	                		Intent i = new Intent(Intent.ACTION_MAIN);
	                    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    	i.addCategory(Intent.CATEGORY_HOME);
	                    	startActivity(i);
	                    	
	                	}
	                	catch(Exception e){
	                		// do nothing.
	                    }
	                	
	                }
	                
	             return true;
            }
            
        }
        
        return super.onKeyDown(keyCode, event);
    
	} // onKeyDown ended.

	// voice record
	public void RecordVoice(Boolean Start_Stop){ 
		
		formatter = new SimpleDateFormat("yyyyMMddHHmmSSS"); 
		NowTime = formatter.format(new Date()); 
		
		if(Start_Stop == true){
			
		 recorder1 = new Record_Sound("/Jujue/Voice/rc"+NowTime+".mp3");
		 
			try {
				
				infoMessage("��}�l");
				recorder1.start();
		
			} 
			catch (IOException e) {
			
				e.printStackTrace();
			
			}
		
		}
		else if (Start_Stop == false){
			
			try {		
				
				infoMessage("���");
				recorder1.stop();
				
			} 
			catch (Exception e) {
				
				e.printStackTrace();
				
			}
			
		}
	}// RecordVoice ended.

	private void enableNdefExchangeMode() {
		
	    mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
	    
	}// enableNdefExchangeMode ended.

	//display debug message to application screen.
	public void debugMessage(String messageString){
	
		if(_DEFINE_DEBUG_MSG_IS_ENABLE){
				
			Toast.makeText(getApplicationContext(), messageString, Toast.LENGTH_LONG).show();
				
		}
		
	}// debugMessage ended.

	// display info message to application screen.
	public void infoMessage(String messageString){
		
		if(_DEFINE_INFO_MSG_IS_ENABLE){
				
			Toast.makeText(getApplicationContext(), messageString, Toast.LENGTH_LONG).show();
				
		}
		
	}// infoMessage ended.


	
	
	// display error message to application screen.
	public void errMessage(String messageString){
			
		if(_DEFINE_ERROR_MSG_IS_ENABLE){
					
			Toast.makeText(getApplicationContext(), messageString, Toast.LENGTH_LONG).show();
					
		}
			
	}// errMessage ended.
}
