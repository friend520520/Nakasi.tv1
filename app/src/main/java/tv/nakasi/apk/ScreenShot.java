package tv.nakasi.apk;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class ScreenShot {
        protected	 SimpleDateFormat formatter;
        protected	 String NowTime;
	    public void screenshot(RelativeLayout L1){
             formatter = new SimpleDateFormat("yyyyMMddHHmmSSS");
             View v1 = L1.getRootView();
             v1.setDrawingCacheEnabled(true);
             Bitmap bm = v1.getDrawingCache();
             BitmapDrawable bitmapDrawable = new BitmapDrawable(bm);
             NowTime = formatter.format(new Date());

             savefolder(bm);
        }

        protected void savefolder(Bitmap bit){
              File saved_image_folder = new File(
                      Environment.getExternalStorageDirectory()
                                + "/Jujue");
              if(!saved_image_folder.exists()){
              if(saved_image_folder.mkdir())
              {
                    savefile(bit);
              }
              }else{
                    savefile(bit);
              }
        }

        public void  savefile(Bitmap bit){
              File saved_image_file = new File(
                      Environment.getExternalStorageDirectory()+"/Jujue/Capture/"+NowTime+".jpg");
              if(saved_image_file.exists()){
                  saved_image_file.delete();
              }
              try {
                  FileOutputStream out = new FileOutputStream(saved_image_file);
                  bit.compress(Bitmap.CompressFormat.PNG, 100, out);
                  out.flush();
                  out.close();
              } catch (Exception e) {
                  e.printStackTrace();
              }
        }
}
