package com.mcc.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class HWR_Test {

	private static String string = "http://test.api.hcicloud.com:8880/hwr/Recognise";

	// ���ڲ��Ե�����
		private static short[] g_sShortData = { 103, 283, 105, 283, 107, 283, 113, 283,
				120, 283, 129, 283, 138, 283, 146, 283, 156, 283, 162, 283, 165,
				283, 166, 283, -1, 0, 282, 245, 277, 247, 270, 251, 266, 255, 263,
				257, 259, 261, 254, 266, 250, 273, 246, 281, 243, 286, 240, 292,
				240, 294, 239, 296, 238, 297, 238, 298, -1, 0, 262, 271, 264, 272,
				266, 272, 268, 272, 270, 273, 272, 274, 275, 274, 278, 276, 280,
				278, 283, 279, 286, 281, 289, 282, 289, 283, 291, 284, 292, 285,
				292, 286, -1, 0, 268, 281, 268, 282, 268, 284, 270, 287, 270, 290,
				270, 294, 270, 297, 270, 299, 270, 301, 270, 303, 270, 304, 270,
				306, 270, 308, 269, 309, 269, 310, 269, 311, 269, 312, 269, 314,
				269, 316, 269, 318, 269, 319, 269, 321, 269, 322, 269, 323, 269,
				324, 268, 324, -1, 0, 382, 255, 382, 256, 382, 260, 382, 263, 381,
				267, 378, 274, 375, 278, 373, 282, 372, 287, 371, 291, 369, 294,
				368, 297, 367, 300, 367, 301, 366, 302, 365, 304, 364, 305, 364,
				306, 363, 308, 362, 308, 362, 309, 361, 310, 361, 311, 360, 311,
				-1, 0, 376, 289, 377, 290, 378, 290, 380, 291, 381, 292, 382, 293,
				384, 294, 385, 294, 387, 297, 388, 298, 390, 299, 393, 300, 394,
				301, 396, 302, 398, 303, 400, 305, 401, 306, 403, 307, 404, 309,
				405, 309, 407, 311, 408, 312, 409, 314, 410, 314, 411, 314, -1, 0,
				-1, -1 };
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient client = new HttpClient();
		PostMethod myPost = new PostMethod(string );
		try{
			//��������ͷ������   
			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", "utf-8");
			myPost.setRequestHeader("x-app-key", "c85d54f1");
			myPost.setRequestHeader("x-sdk-version", "3.1");

			String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			myPost.setRequestHeader("x-request-date", date);
			String x_task_config = "capkey=hwr.cloud.letter";
			myPost.setRequestHeader("x-task-config", x_task_config);
//			String b = new String(g_sShortData);
			String str = "712ddd892cf9163e6383aa169e0454e3" + date +  x_task_config ;
//			System.out.println(str);
			myPost.setRequestHeader("x-auth", MD5.getMD5((str + g_sShortData.toString()).getBytes()));
//			myPost.setRequestHeader("x-udid", "101:123456789");
			System.out.println(g_sShortData.length);
			byte[] b = new byte[g_sShortData.length];
			for(int i=0; i<g_sShortData.length; i++){
				b[i] = (byte) g_sShortData[i];
			}
			
			RequestEntity entity = new StringRequestEntity(new String(b, "iso-8859-1"), "application/octet-stream", "iso-8859-1");
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
