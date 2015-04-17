package tv.nakasi.apk;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;


public class Record_Sound {
	 final MediaRecorder recorder = new MediaRecorder();
	File audiofile = null;
	private String path;
	public Record_Sound(String path) {
		// TODO Auto-generated constructor stub
		 this.path = Environment.getExternalStorageDirectory().getAbsolutePath() + path;	 
	}

	  public void start() throws IOException {
		    String state = android.os.Environment.getExternalStorageState();
		    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
		        throw new IOException("SD Card is not mounted.  It is " + state + ".");
		    }
		    /**
			   * Starts a new recording.
			   */
		    // make sure the directory we plan to store the recording in exists
		    File directory = new File(path).getParentFile();
		    if (!directory.exists() && !directory.mkdirs()) {
		      throw new IOException("Path to file could not be created.");
		    }
		    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//Sets the audio source to be used for recording.
		    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//setting 3gp as output format
		    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//setting encoding format
		    recorder.setOutputFile(path);//path of the audio recording to be stored
		    recorder.prepare();//Prepares the recorder to begin
		    recorder.start();//begins capturing data
		  }
	  /**
	   * Stops a recording that has been previously started.
	   */
	  public void stop() throws IOException {
	    recorder.stop();//stops recording
	    recorder.release();//releases resources
	  }


}
