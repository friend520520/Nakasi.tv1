package tv.nakasi.apk;

import com.SmartScreen.tv.R;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.Toast;

public class screen_size {
	int width, height;
	MainActivity main = new MainActivity();
	public screen_size(MainActivity mainActivity) {
		// TODO Auto-generated constructor stub
	
		DisplayMetrics displaymetrics = new DisplayMetrics();
		mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);		
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;
		mainActivity.wv.getSettings().setJavaScriptEnabled(true);
		if(height>width){
			mainActivity.im.setImageResource(R.drawable.nakasitv1);
			mainActivity.JudgePortraitLandscape = "portrait";
		}else{
			mainActivity.im.setImageResource(R.drawable.nakasitv2);
			//mainActivity.JudgePortraitLandscape = "portrait"; 
		}
	}

}
