package tv.nakasi.apk;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class bluetooth extends Activity {

   private static final int REQUEST_ENABLE_BT = 1;
   private Button onBtn;
   private Button offBtn;
   private Button listBtn;
   private Button findBtn;
   private TextView text;
   //private BluetoothAdapter myBluetoothAdapter;
   private Set<BluetoothDevice> pairedDevices;

   private ListView myListView;
   private ArrayAdapter<String> BTArrayAdapter;

    public void create( BluetoothAdapter myBluetoothAdapter ){

          // take an instance of BluetoothAdapter - Bluetooth radio
          myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
          if(myBluetoothAdapter == null) {

                  text.setText("Status: not supported");

                  Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
                         Toast.LENGTH_LONG).show();
          } else {
                  // create the arrayAdapter that contains the BTDevices, and set it to the ListView

          }
   }

   public void on( BluetoothAdapter myBluetoothAdapter ){

       if (!myBluetoothAdapter.isEnabled()) {
             Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
             startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);

             Toast.makeText(getApplicationContext(),"Bluetooth turned on" ,
                     Toast.LENGTH_LONG).show();
          }
          else{
                /*
                 Toast.makeText(getApplicationContext(),"Bluetooth is already on",
                         Toast.LENGTH_LONG).show();*/
                find(myBluetoothAdapter);
          }
   }

   protected void onActivityResult(int requestCode, int resultCode, Intent data, BluetoothAdapter myBluetoothAdapter ) {
           // TODO Auto-generated method stub
           if(requestCode == REQUEST_ENABLE_BT){
               if(myBluetoothAdapter.isEnabled()) {
                   text.setText("Status: Enabled");
               } else {
                   text.setText("Status: Disabled");
               }
           }
   }
   
   public void list( BluetoothAdapter myBluetoothAdapter ){

          //String rtn = "" ;

          // get paired devices
          pairedDevices = myBluetoothAdapter.getBondedDevices();


          // put it's one to the adapter
          for(BluetoothDevice device : pairedDevices)
          {
              //BTArrayAdapter.add(device.getName()+ "\n" + device.getAddress());
          }

           //Toast.makeText(getApplicationContext(), rtn , Toast.LENGTH_SHORT).show();

           /*
           Toast.makeText(getApplicationContext(),"Show Paired Devices",
                   Toast.LENGTH_SHORT).show();*/
      
   }


   //public void find(View view)
   public void find( BluetoothAdapter myBluetoothAdapter )
   {
	   if (myBluetoothAdapter.isDiscovering())
       {
		   // the button is pressed when it discovers, so cancel the discovery
		   myBluetoothAdapter.cancelDiscovery();
	   }
	   else
       {
			//BTArrayAdapter.clear();
			myBluetoothAdapter.startDiscovery();
			//registerReceiver(  this.bReceiver , new IntentFilter( BluetoothDevice.ACTION_FOUND ) );
		}    
   }


    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name and the MAC address of the object to the arrayAdapter
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

   public void off( BluetoothAdapter myBluetoothAdapter )
   {
	  myBluetoothAdapter.disable();
	  text.setText( "Status: Disconnected" );
	  
      Toast.makeText( getApplicationContext() , "Bluetooth turned off" , Toast.LENGTH_LONG ).show();
   }


   protected void onDestroy() {
	   // TODO Auto-generated method stub
	   super.onDestroy();
	   unregisterReceiver(bReceiver);
   }
		
}
