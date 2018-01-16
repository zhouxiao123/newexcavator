package com.newexcavator.wechat.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestTxtSender {

	public static void main(String[] args) {
	
		sendTextMessage();
	}
	
	//获取access_token
	public static String getAccessToken(){
		
		return "9Yl18f1LkiOaC9_QD1Ac-UpaGjp5ukCT6zuWn2DU2DDNnE7cCt1dMscjN7ZRhfBt";
	}
	
	public static String getContent(){
		
		return "{\"touser\":\"o0N5iuFWGqRTh4iFMOR0CqBVCmX0\", \"msgtype\":\"text\", \"text\": {\"content\":\"tody is 0507\"}}";
	}
	
	
	public static void sendTextMessage(){
		String apiAdressPre="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
		String token=getAccessToken();
		String url=apiAdressPre+token;
		String content=getContent();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost=new HttpPost(url);
		StringEntity entity;
		try {
			entity = new StringEntity(content);
			httpPost.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			String responseContent = EntityUtils.toString(resEntity, "utf-8");
			System.out.println(responseContent);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		
		
		
		
	}
	
	
	
	

}
