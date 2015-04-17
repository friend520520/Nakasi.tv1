package tv.nakasi.apk;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class CardEven {
	private String jujue ="3305108402" , ceppei ="3305580594",nakasi="2830079081" ;	
	public void Card(String str){
		/*if( str.compareT
		 * o( jujue ) ==0  ){
			//		Intent jujueIntent = new Intent();
			//		jujueIntent.setClass(MainActivity.this, Main_jujue_Activity.class);
			//	startActivity(jujueIntent);
				//	Toast.makeText(this, "jujue", Toast.LENGTH_SHORT).show();
					  MainActivity.mainactivity.probar.setVisibility(View.VISIBLE); 
					  MainActivity.mainactivity.im.setVisibility(View.VISIBLE); 
	       
					  MainActivity.mainactivity.wv.loadUrl("http://apk.jujue.tv");
					  MainActivity.mainactivity.onPageFinished();  
				}else if( str.compareTo( ceppei ) ==0){
				//	Intent ceppeiIntent = new Intent();
			//		ceppeiIntent.setClass(MainActivity.this, Main_ceppei.class);		
			//	startActivity(ceppeiIntent);
				//	Toast.makeText(this, "ceppei", Toast.LNGTH_SHORT).show();
					MainActivity.mainactivity.probar.setVisibility(View.VISIBLE); 
					MainActivity.mainactivity.im.setVisibility(View.VISIBLE); 

					MainActivity.mainactivity.wv .loadUrl("http://apk.ceppei.com");
					MainActivity.mainactivity.onPageFinished();  
				}else if(str.compareTo(nakasi)==0){
					
					MainActivity.mainactivity.probar.setVisibility(View.VISIBLE); 
					MainActivity.mainactivity.im.setVisibility(View.VISIBLE); 
			
				      // wv.loadUrl("http://apk.nakasi.tv/index.html?mode=app&udid="+Android_ID); 	
					MainActivity.mainactivity.connectSuccess();
				      // onPageFinished();  
				}else{			*/	
			MainActivity.mainactivity.wv.loadUrl("javascript:MsgBox.ShowPrompt('"+str+"')");
		    //new RequestTask().execute("http://pqd.cc/pn/?q="+str +"&t="+"&d="+MainActivity.mainactivity.DDN);
			//intent youtubeapi = new intent();
		    //MainActivity.mainactivity.youtube();
					
			// }		
	}
	
}
