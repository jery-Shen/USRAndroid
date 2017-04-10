package usr.work.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {
	public final static String URL_PRE = "http://test.lightxx.cn:8080/USR/";
	
	public static String getStrFromUrl(String url){
		
		
		HttpURLConnection conn = null;
		StringBuffer buffer = new StringBuffer();
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);
    		conn.setUseCaches(false);
    		InputStream is = conn.getInputStream();
    		BufferedReader bf=new BufferedReader(new InputStreamReader(is,"UTF-8"));  
    	     //最好在将字节流转换为字符流的时候 进行转码  
    	     String line="";  
    	     while((line=bf.readLine())!=null){  
    	         buffer.append(line);  
    	     } 
    	     is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	public static String postUrl(String url,String params){
		
		
		HttpURLConnection conn = null;
		StringBuffer buffer = new StringBuffer();
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(6000);
			conn.setDoOutput(true);   //需要输出
			conn.setDoInput(true); 
    		conn.setUseCaches(false);
    		PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
            printWriter.write(params);//post的参数 xx=xx&yy=yy
            printWriter.flush();
    		InputStream is = conn.getInputStream();
    		BufferedReader bf=new BufferedReader(new InputStreamReader(is,"UTF-8"));  
    	     //最好在将字节流转换为字符流的时候 进行转码  
    	     String line="";  
    	     while((line=bf.readLine())!=null){  
    	         buffer.append(line);  
    	     } 
    	     is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
