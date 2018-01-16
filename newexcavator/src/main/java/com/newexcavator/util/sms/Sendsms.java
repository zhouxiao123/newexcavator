package com.newexcavator.util.sms;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Sendsms {
	private static String Url = "http://115.29.170.211:8085/sms.aspx?&sendTime=&extno=&";
	
	private static String msg = "msg";
	// cf_loushi 密码：123456

	public static String getMsg(){
		return msg;
	}
	
	public static boolean send(String validateCode, String phone) {

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);

		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");

		String content = new String("尊敬的用户：您的验证码是" + validateCode + "，请及时输入！【贝格畅斯】");

		NameValuePair[] data = {// 提交短信
		new NameValuePair("action", "send"),
		new NameValuePair("userid", "2540"),
		new NameValuePair("account", "BGCS2"), 
		new NameValuePair("password", "BGCS123"),
		new NameValuePair("mobile", phone), 
		new NameValuePair("content", content)
		};

		method.setRequestBody(data);
		
		String code = null;
		try {
			client.executeMethod(method);

			String SubmitResult = method.getResponseBodyAsString();
			
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			code = root.elementText("returnstatus");
			msg = root.elementText("message");
			String point = root.elementText("remainpoint");
			String smsid = root.elementText("taskID");

			System.out.println("短信回执消息===> code: " + code + "\tmsg:" + msg + "\tsmsid:" + smsid + "\tpoint:" + point);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
		}
		
		return StringUtils.equals(code, "Success");
	}
}