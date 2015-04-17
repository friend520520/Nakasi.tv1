package tv.nakasi.apk;
import android.net.NetworkInfo;
import android.util.Log;
public class DecideConnect extends Thread{
public DecideConnect(final MainActivity mainActivity) {
		// TODO Auto-generated constructor stub
		new Thread(){
			   public void run() {
				     
				   NetworkInfo Info = mainActivity.conns.getActiveNetworkInfo();   
		
			         int j=1;
			         for(int i=0;i<j;i++){
			       		if(Info == null ||  !Info.isConnected()){	    			
			        		try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        		 Info = mainActivity.conns.getActiveNetworkInfo();   
			       			j++;
			       		}
			       		else{
			       			if(!Info.isAvailable()){   			 
			       				try {
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}     			
			       			 Info = mainActivity.conns.getActiveNetworkInfo();
			       				 j++;		
			       			}else
			       			{		    		
			       			i++;   		
			       			mainActivity.connectSuccess();
			     			}
			       		}
			    }
			   }
		}.start();
	}

}
