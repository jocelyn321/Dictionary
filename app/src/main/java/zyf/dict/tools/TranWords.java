package zyf.dict.tools;

import java.io.InputStream;
import org.json.JSONObject;
import zyf.dict.service.DataAccess;
public class TranWords {
	//有道在线翻译的url
	private final String url = "http://fanyi.youdao.com/openapi.do?keyfrom=android-dict&key=1038310316&type=data&doctype=json&version=1.1&q=";
	public TranWords(String words)
	{
		//调用查询函数
		Search(words);
	}
	public String strTran;
	private static String replaceJson(String value)
	{
		value=value.replace("[\"", "");
		value=value.replace("\"]", "");
		return value;
	}
	public void Search(String searchWord){
		//获取InputStream对象
		InputStream is = DataAccess.getStreamByUrl(url+searchWord);
	    byte[] buffer = new byte[4096];
		try {
			
			Thread.sleep(200);
			//buffer = new byte[is.available()];
			is.read(buffer);
			String json  = new String(buffer,"utf-8");
			//获取Json对象
			JSONObject obj = new JSONObject(json);
			strTran = replaceJson(obj.getString("translation"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
