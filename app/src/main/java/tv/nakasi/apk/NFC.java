package tv.nakasi.apk;

import java.nio.charset.Charset;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.widget.Toast;

public class NFC 
{
	public long getDec(byte[] bytes) {
		
			long result = 0;
		    long factor = 1;
		    
		    for (int i = 0; i < bytes.length; ++i) {
		    	long value = bytes[i] & 0xffl;
		        result += value * factor;
		        factor *= 256l;
		    }
		    
		    return result;
		        
	 }

	
	
	 public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
		 
			 byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		     NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
		     
		     return mimeRecord;
	 }

	 
	 
	 public void processIntent(Intent intent) {
		 
			 // TODO Auto-generated method stub
			 Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		        
			 // only one message sent during the beam
		     NdefMessage msg = (NdefMessage) rawMsgs[0];
		     
		    
		     //MainActivity.mainactivity.value(); // test
		     //record 0 contains the MIME type, record 1 is the AAR, if present
		     //Toast.makeText(this," "+msg.getRecords()[0].getPayload() , Toast.LENGTH_LONG).show();
		     //textView.setText(new String(msg.getRecords()[0].getPayload()));	 
	 }	
	
	 
}