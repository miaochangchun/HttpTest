package com.mcc.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class VPR_Test {
	private static String filepath;
	private static int filelen;
	private static String xml;
	
	private static String app_key = "c85d54f1";
	private static String dev_key = "712ddd892cf9163e6383aa169e0454e3";
	private static String cap_key = "vpr.cloud.verify";
	private static String registerURL = "http://test.api.hcicloud.com:8880/vpr/register";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//创建httpclient工具对象   
		String output = "output";
		HttpClient client = new HttpClient();
		PostMethod myPost = new PostMethod(registerURL); 
		try{
			//设置请求头部类型   
			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", "utf-8");
//			myPost.setRequestHeader("x-app-key", "7c5d543d");
			myPost.setRequestHeader("x-app-key", app_key);
			myPost.setRequestHeader("x-sdk-version", "3.8");

			String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			myPost.setRequestHeader("x-request-date", date);
			myPost.setRequestHeader("x-task-config", "capkey=" + cap_key + ",audioformat=pcm16k16bit");
			String str = date + dev_key;
//			String str = date + "developer_key";
			myPost.setRequestHeader("x-session-key", MD5.getMD5(str.getBytes()));
			myPost.setRequestHeader("x-udid", "101:123456789");
			myPost.setRequestHeader("x-tid", "1234567");
			
			filepath = new String(System.getProperty("user.dir") + "\\wav\\nihao.pcm");
			System.out.println(filepath);
			File fileSrc = new File(filepath);
			if (!fileSrc.exists()) {
			}
			
			byte [] content = null;
			
			FileInputStream rd = new FileInputStream(fileSrc);
			filelen = rd.available();
			content = new byte[filelen];
			rd.read(content);
			
			byte [] contentlen = new byte[4];
            contentlen[0] = (byte) (filelen & 0xff);// 最低位 
            contentlen[1] = (byte) ((filelen >> 8) & 0xff);// 次低位 
            contentlen[2] = (byte) ((filelen >> 16) & 0xff);// 次高位 
            contentlen[3] = (byte) (filelen >>> 24);// 最高位,无符号右移。
            
            byte[] byte_data = new byte[contentlen.length+content.length];   
            System.arraycopy(contentlen, 0, byte_data, 0, contentlen.length);   
            System.arraycopy(content, 0, byte_data, contentlen.length, content.length);  
            
            RequestEntity entity = new StringRequestEntity(new String(byte_data, "iso-8859-1"), "application/octet-stream", "iso-8859-1");
			myPost.setRequestEntity(entity);
   
			int statusCode = client.executeMethod(myPost);
			
			output = String.format("%d", statusCode);
			System.out.println(output);
			if (statusCode == HttpStatus.SC_OK) {
				InputStream txtis = myPost.getResponseBodyAsStream(); 
				BufferedReader br = new BufferedReader(new InputStreamReader(txtis));
				
				String tempbf;
				StringBuffer html = new StringBuffer(256);
				while((tempbf = br.readLine()) != null){
					html.append(tempbf);
				}
				
				xml = html.toString();
				System.out.println(xml);
				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
