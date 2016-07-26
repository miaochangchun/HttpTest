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

public class Template_Test {

	private static String string = "http://test.api.hcicloud.com:8880/ocr/auto_recognise";
	private static String filepath;
	private static int filelen;
	
	private static String app_key = "c85d54f1";
	private static String dev_key = "712ddd892cf9163e6383aa169e0454e3";
	private static String cap_key = "vpr.cloud.verify";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient client = new HttpClient();
		PostMethod myPost = new PostMethod(string  );
		
		try{
			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", "utf-8");
			myPost.setRequestHeader("x-app-key", app_key);	
			myPost.setRequestHeader("x-sdk-version", "3.6");	

			String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			myPost.setRequestHeader("x-request-date", date);	
			//���ô�
			myPost.setRequestHeader("x-task-config", "capkey=" + cap_key + ",domain=idcard,templateIndex=0,templatePageIndex=0");
			String str = date + dev_key;
			System.out.println(str);
			myPost.setRequestHeader("x-session-key", MD5.getMD5(str.getBytes()));
			myPost.setRequestHeader("x-udid", "101:123456789");
			
			filepath = new String(System.getProperty("user.dir") + "\\wav\\IDCard.jpg");
			System.out.println(filepath);
			File fileSrc = new File(filepath);
			if (!fileSrc.exists()){
				System.out.println("图片不存在");
				return;
			}
			
			byte [] content = null;
			
			FileInputStream rd = new FileInputStream(fileSrc);
			filelen = rd.available();
			content = new byte[filelen];
				
			rd.read(content);
			//application/octet-stream�����Ʒ�ʽ
			RequestEntity entity = new StringRequestEntity(new String(content, "iso-8859-1"), "application/octet-stream", "iso-8859-1");
			myPost.setRequestEntity(entity);
			int statusCode = client.executeMethod(myPost);
			String.format("%d", statusCode);
			System.out.println(statusCode);
			
			
			if (statusCode == HttpStatus.SC_OK) {
				InputStream txtis = myPost.getResponseBodyAsStream(); 
				BufferedReader br = new BufferedReader(new InputStreamReader(txtis, "utf-8"));
				
				String tempbf;
				StringBuffer html = new StringBuffer(256);
				while((tempbf = br.readLine()) != null){
					html.append(tempbf);
				}
				
				System.out.println(html.toString());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
