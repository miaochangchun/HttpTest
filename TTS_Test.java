package com.mcc.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class TTS_Test {

	private static String URL = "http://test.api.hcicloud.com:8880/tts/synthtext";
	private static String responseString;
//	private static String app_key = "c85d54f1";
//	private static String dev_key = "712ddd892cf9163e6383aa169e0454e3";

	private static String app_key = "f85d54b3";
	private static String dev_key = "3fb4443773085b6fbbe8265f1c805c4b";
	private static String cap_key = "tts.cloud.wangjing";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient client = new HttpClient();
		PostMethod myPost = new PostMethod(URL);
		
		try {
			
			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", "utf-8");
			myPost.setRequestHeader("x-app-key", app_key);
			myPost.setRequestHeader("x-sdk-version", "3.6");

			String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			myPost.setRequestHeader("x-request-date", date);
			myPost.setRequestHeader("x-task-config", "capkey=" + cap_key + ",audioformat=auto,pitch=5,volume=5,speed=5");
			String str = date + dev_key;
			System.out.println(str);
			myPost.setRequestHeader("x-session-key", MD5.getMD5(str.getBytes()));
			myPost.setRequestHeader("x-udid", "101:123456789");
			
			RequestEntity entity = new StringRequestEntity("一二三四五", "text/html", "utf-8");
			myPost.setRequestEntity(entity);
			int statusCode = client.executeMethod(myPost);
			String output = String.format("%d", statusCode);
			System.out.println(statusCode);
			
			if (statusCode == HttpStatus.SC_OK) {
				BufferedInputStream bis = new BufferedInputStream(
						
				myPost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				//responseString = new String(strByte, 0, strByte.length, "utf-8");
				responseString = new String(strByte, 0, strByte.length, "iso-8859-1");
				
				//System.out.println(responseString);
				//output = String.format("%s", responseString);
				output = String.format("%d", responseString.indexOf("</ResponseInfo>"));
				System.out.println(responseString.substring(0, responseString.indexOf("</ResponseInfo>") + 15));
				responseString = responseString.substring(responseString.indexOf("</ResponseInfo>") + 15);
				
				strByte = responseString.getBytes("iso-8859-1");
				String path = System.getProperty("user.dir") + "\\wav\\";
				System.out.println(path);
				//output = String.format("%s", path);
				File file = new File(path, "nihao.pcm");
				if (!file.exists())
					file.createNewFile();
				FileOutputStream outStream = new FileOutputStream(file);
				outStream.write(strByte, 0, strByte.length);
				outStream.close();
				bos.close();
				bis.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
