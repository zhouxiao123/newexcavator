/**
 * 
 */
package com.newexcavator.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.newexcavator.wechat.AccessTokenTool;
import com.newexcavator.wechat.model.message.response.ResponseStatus;
import com.newexcavator.wechat.model.message.response.UserInfo;
import com.newexcavator.wechat.model.message.response.WebOpenidModel;

/**
 * @author Administrator
 *
 */
public class WechatUtils {
	
	/**
	 * 用户管理--->网页授权获取用户基本信息
	 * 
	 * 
	 * 验证access_token是否有效
	 * 
	 * @param access_token
	 * @param openid
	 * @return
	 * @throws Exception 
	 */
	public static boolean checkAccessToken(String access_token, String openid) throws Exception {
		String url = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + "&openid=" + openid;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	            	System.out.println("[checkAccessToken]Response message======>>>" + result);
	            	ResponseStatus rs = JsonUtil.fromJson(result, ResponseStatus.class);
	            	return rs.getErrcode() != null && rs.getErrcode().intValue() == 0;
	            }
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			// 释放连接
			httpGet.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/**
	 * 用户管理--->网页授权获取用户基本信息
	 * 
	 * 通过code换取网页授权access_token
	 * 
	 * @param code
	 * @return
	 */
	public static WebOpenidModel getOpenidInfor(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + AccessTokenTool.AppId + "&secret=" + AccessTokenTool.AppSecret + "&code=" + code + "&grant_type=authorization_code";
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	            	System.out.println("[getOpenidInfor]Response message======>>>" + result);
	            	WebOpenidModel am = JsonUtil.fromJson(result, WebOpenidModel.class);
	            	return am;
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpGet.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	/**
	 * 用户管理--->网页授权获取用户基本信息
	 * 
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 * 
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static UserInfo getUserInfo(String access_token, String openid) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity, "utf-8");
	            	System.out.println("[getUserInfo]Response message======>>>" + result);
	            	UserInfo am = JsonUtil.fromJson(result, UserInfo.class);
	            	return am;
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpGet.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	/**
	 * 用户管理--->通过openid获取用户基本信息
	 * 
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 * 
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static UserInfo getUserInfoByOpenId(String openid, String url, int count) {
		if(StringUtils.isBlank(url)){
			url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + AccessTokenTool.getAccessToken(false) + "&openid=" + openid + "&lang=zh_CN";
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
				if (entity != null) {
					String result = EntityUtils.toString(entity, "utf-8");
					System.out.println("[getUserInfo]Response message======>>>" + result);
					ResponseStatus status = null;
	        		try {
	        			status = JsonUtil.fromJson(result, ResponseStatus.class);
	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	            	if (status != null && status.getErrcode() != null) {
	            		Integer errcode = status.getErrcode();
	            		System.out.println("errcode===========>" + errcode);
	            		if (errcode.intValue() == 42001) {
	            			UserInfo am = getUserInfoByOpenId(openid, "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + AccessTokenTool.getAccessToken(true) + "&openid=" + openid + "&lang=zh_CN", ++count);
	            			return am;
	            		} else if (errcode.intValue() == 40001 && count <= 3) {
	            			UserInfo am =  getUserInfoByOpenId(openid, "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + AccessTokenTool.getAccessToken(false) + "&openid=" + openid + "&lang=zh_CN", ++count);
	            			return am;
	            		}
	            	} else {
	            		UserInfo am = JsonUtil.fromJson(result, UserInfo.class);
    					return am;
	            	}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpGet.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
