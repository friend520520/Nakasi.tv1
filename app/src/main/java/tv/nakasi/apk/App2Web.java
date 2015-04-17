package tv.nakasi.apk;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.Toast;
import org.json.*;

public class App2Web extends WebView{

	private App2WebCallBack _AppCallBack;
	
	public App2Web(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		// enabled javascript.
		getSettings().setJavaScriptEnabled(true);
		
		//  injection javascript callback object for apk call javascript.
		//loadUrl("javascript:var _App2Web=function(){var c={};this.Extend=function(d){c=$.extend({},c,d)};this.Execute=function(d,h){try{var e,f,g={},a={},b='';f=c[d];if('function'==typeof f){try{g=$.parseJSON(h)}catch(k){a.Status=-2,a.Msg=k.message,b=JSON.stringify(a),window.App2WebCall.Return(b)}e=f(g);'undefined'==typeof e?a.Status=2:(a.Status=1,a.Result=e);try{b=JSON.stringify(a),window.App2WebCall.Return(b)}catch(m){}}else a.Status=-1,a.Msg='Not a javasciprt function.',b=JSON.stringify(a),window.App2WebCall.Return(b)}catch(l){try{a.Status=-2,a.Msg=l.message,b=JSON.stringify(a),window.App2WebCall.Return(b)}catch(n){}}}},App2Web=new _App2Web;");
		loadUrl("javascript:var _App2Web=function(){var g={},h={};this.ExtendInterface=function(d){h=$.extend({},h,d)};this.Extend=function(d){g=$.extend({},g,d)};this.ExecuteInterface=function(d,g){try{var e,f={},b={},a='';e=h[d];if('function'==typeof e){try{f=$.parseJSON(g)}catch(c){b.Status=-2,b.Msg=c.message,a=JSON.stringify(b),window.App2WebCall.Return(a)}e(f,function(c){'undefined'==typeof c?b.Status=2:(b.Status=1,b.Result=c);try{a=JSON.stringify(b),window.App2WebCall.Return(a)}catch(d){}})}else b.Status=-1,b.Msg='Not a javasciprt function.',a=JSON.stringify(b),window.App2WebCall.Return(a)}catch(k){try{b.Status=-2,b.Msg=k.message,a=JSON.stringify(b),window.App2WebCall.Return(a)}catch(m){}}};this.Execute=function(d,h){try{var e,f,b={},a={},c='';f=g[d];if('function'==typeof f){try{b=$.parseJSON(h)}catch(k){a.Status=-2,a.Msg=k.message,c=JSON.stringify(a),window.App2WebCall.Return(c)}e=f(b);'undefined'==typeof e?a.Status=2:(a.Status=1,a.Result=e);try{c=JSON.stringify(a),window.App2WebCall.Return(c)}catch(m){}}else a.Status=-1,a.Msg='Not a javasciprt function.',c=JSON.stringify(a),window.App2WebCall.Return(c)}catch(l){try{a.Status=-2,a.Msg=l.message,c=JSON.stringify(a),window.App2WebCall.Return(c)}catch(n){}}}},App2Web=new _App2Web;");
		
		// register javascript interface
		addJavascriptInterface(new Object(){
			public void Return(String dataString){
		
				// generate default json object
				JSONObject dataObject = new JSONObject();
				try 
				{
					dataObject.put("Status", -3);
					dataObject.put("Msg", "Json convert error");
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try 
				{
					dataObject = new JSONObject(dataString);
					//_AppCallBackCosure.Return(dataString);
					_AppCallBack.Return( dataObject );
				} 
				catch (JSONException exception) 
				{
					_AppCallBack.Return( dataObject );
				}
				
			}
			
			public void test(String dataString){
				loadUrl("javascript:alert('hahaha')");
			}
			
		}, "App2WebCall");
	}
	
	public void App2Web( String functionName, JSONObject paramObject, App2WebCallBack callBack)
	{
		_AppCallBack = callBack;
		loadUrl( "javascript:App2Web.Execute( '"+ functionName + "', '"+ paramObject.toString() + "' );" );
	}
	
	public void App2WebInterface( String functionName, JSONObject paramObject, App2WebCallBack callBack)
	{
		_AppCallBack = callBack;
		//loadUrl("javascript:$('#TextOut').val('App2WebInterface!')");
		loadUrl( "javascript:App2Web.ExecuteInterface( '"+ functionName + "', '"+ paramObject.toString() + "' );" );
	}
	
	public void Web2App( Object obj)
	{
		addJavascriptInterface(obj, "Web2App");
	}

}












