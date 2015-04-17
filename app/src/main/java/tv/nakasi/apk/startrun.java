package tv.nakasi.apk;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class startrun extends BroadcastReceiver {
    final String action_boot="android.intent.action.BOOT_COMPLETED"; 
    MainActivity main = new MainActivity();
    @Override
   public void onReceive(Context context, Intent intent) {  	
         if (intent.getAction().equals(action_boot)){ 
            	Intent ootStartIntent=new Intent(context,MainActivity.class); 
            	ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            	main.loadBoot = true;
            
            	        	
            	  context.startActivity(ootStartIntent); 
        }
    }
  
}


