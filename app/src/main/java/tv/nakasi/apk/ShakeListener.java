package tv.nakasi.apk;

import tv.nakasi.apk.ShakeListener.OnShakeListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ShakeListener implements SensorEventListener {
	   private final float NOISE = (float) 2.0;
			private float mLastX, mLastY, mLastZ;
		    private OnShakeListener mListener;
		    private int mShakeCount;
			private boolean mInitialized;
			private int ShakeFrequency = 0;
		
			  public interface OnShakeListener {
			        public void onShake(int count);
			    }
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				if (!mInitialized) {
					mLastX = x;
					mLastY = y;
					mLastZ = z;
					mInitialized = true;
				} else {
					float deltaX = Math.abs(mLastX - x);
					float deltaY = Math.abs(mLastY - y);
					float deltaZ = Math.abs(mLastZ - z);
					if (deltaX < NOISE) deltaX = (float)0.0;
					if (deltaY < NOISE) deltaY = (float)0.0;
					if (deltaZ < NOISE) deltaZ = (float)0.0;
					mLastX = x;
					mLastY = y;
					mLastZ = z;
					if(ShakeFrequency==5){
						//Log.e("shake", "OK");
						mShakeCount=1;
						   mListener.onShake(mShakeCount);   
					}
					if (deltaX > deltaY) {
					//Log.e("X>Y","fgd");
					ShakeFrequency++;
					} else if (deltaY > deltaX) {
					//	Log.e("X<Y","fgd");
						ShakeFrequency++;
					} else {
						ShakeFrequency = 0;
						mShakeCount = 0;
					}
				}
			}

			public void setOnShakeListene(OnShakeListener onShakeListener) {
				// TODO Auto-generated method stub
				 this.mListener = onShakeListener;
			}

	
}
