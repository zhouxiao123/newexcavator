/**
 * 
 */
package com.newexcavator.wechat.model.menu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.newexcavator.util.JsonUtil;
import com.newexcavator.util.SettingUtils;
import com.newexcavator.wechat.AccessTokenTool;

/**
 * @author Administrator
 * 
 */
public class Menu {
	/**
	 * 创建菜单接口
	 */
	public static final String MENU_POST_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
	
	public static final String MENU_GET_FIND_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
	
	public static final String MENU_GET_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";
	
	private Button[] button;

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) throws MenuException {
		if (button != null && button.length > 3) {
			throw new MenuException("一级菜单超过3个。。。");
		} 
		this.button = button;
	}

	public static String postCreateMenuUrl(Boolean reloadAccessToken) {
		return MENU_POST_CREATE_URL + AccessTokenTool.getAccessToken(reloadAccessToken);
	}
	
	public static String getFindMenuUrl(Boolean reloadAccessToken) {
		return MENU_GET_FIND_URL + AccessTokenTool.getAccessToken(reloadAccessToken);
	}
	
	public static String getDelteteMenuUrl(Boolean reloadAccessToken) {
		return MENU_GET_DELETE_URL + AccessTokenTool.getAccessToken(reloadAccessToken);
	}
	
	/**
	 * 组装菜单数据
	 * 
	 * @return
	 * @throws MenuException 
	 */
	private static Menu getMenu() throws MenuException {
		String baseUrl = SettingUtils.getCommonSetting("base.url");
		CommonView btn14 = new CommonView();
		
		btn14.setName("我要抽奖");
		btn14.setType("view");
		btn14.setUrl(baseUrl + "/mobile/lottery");
		
		CommonView btn11 = new CommonView();
		btn11.setName("全新挖掘机");
		btn11.setType("view");
		btn11.setUrl(baseUrl + "/mobile/list_mechine?type=new");

		CommonView btn12 = new CommonView();
		btn12.setName("二手机械");
		btn12.setType("view");
		btn12.setUrl(baseUrl + "/mobile/list_mechine?type=old");
		
		
		CommonView btn13 = new CommonView();
		btn13.setName("租赁信息");
		btn13.setType("view");
		btn13.setUrl(baseUrl + "/mobile/list_mechine?type=lease");

		// ====================================
		CommonView btn21 = new CommonView();
		btn21.setName("配件订购");
		btn21.setType("view");
		btn21.setUrl(baseUrl + "/mobile/list_mechine?type=part");

		CommonView btn24 = new CommonView();
		btn24.setName("订单支付演示");
		btn24.setType("view");
		btn24.setUrl("http://wd.koudai.com/item.html?itemID=81327145");
		
		
		CommonButton btn22 = new CommonButton();
		btn22.setName("报修");
//		btn22.setType("view");
//		btn22.setUrl(baseUrl + "/mobile/open_call");
		btn22.setType("click");
		btn22.setKey("22");

		CommonButton btn23 = new CommonButton();
		btn23.setName("维修咨询");
//		btn23.setType("view");
//		btn23.setUrl(baseUrl + "/mobile/open_call");
		btn23.setType("click");
		btn23.setKey("23");
		// ///////////////////////////////////////////////////////
		CommonView btn31 = new CommonView();
		btn31.setName("微官网");
		btn31.setType("view");
		btn31.setUrl(baseUrl + "/mobile");

		CommonButton btn32 = new CommonButton();
		btn32.setName("最新活动");
		btn32.setType("click");
		btn32.setKey("32");

		CommonButton btn33 = new CommonButton();
		btn33.setName("查看二手机械");
		//btn33.setType("view");
		//btn33.setUrl(baseUrl + "/mobile/index");
		btn33.setType("click");
		btn33.setKey("10");
		
		CommonButton btn34 = new CommonButton();
		btn34.setName("个人中心");
		//btn34.setType("view");
		//btn34.setUrl(baseUrl + "/mobile/login");
		btn34.setType("click");
		btn34.setKey("11");
		
		CommonView btn35 = new CommonView();
		btn35.setName("分享测试");
		btn35.setType("view");
		btn35.setUrl(baseUrl + "/mobile/test_share");
		
		
		CommonButton btn36 = new CommonButton();
		btn36.setName("我要发布");
		//btn36.setType("view");
		/*String backUrl = baseUrl + "/mobile/add";
		String url = "";
		try {
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + AccessTokenTool.AppId + "&redirect_uri=" + 
					URLEncoder.encode(backUrl, "UTF-8")
					+ "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		//btn36.setUrl(baseUrl + "/mobile/add");
		btn36.setType("click");
		btn36.setKey("12");
		
		// ////////////////////////////////////////////////////////////
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("产品中心");
		mainBtn1.setSub_button(new Button[] {btn31, btn12, btn13, btn11/*, btn14*/});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("配件与维修");
		mainBtn2.setSub_button(new Button[] {btn22, btn21, /*btn24,*/ btn23});

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("挖友圈");
		mainBtn3.setSub_button(new Button[] {  btn32, btn36,btn34,btn33,btn35});

		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
		return menu;
	}
	
	public static String vurl = "";
	
	/**
	 * 登录流程
	 * 
	 * @return
	 */
	public static String redirectToLogin() {
		return redirectToLogin("redirect:");
	}
	
	/**
	 * 登录流程
	 * 
	 * @return
	 */
	public static synchronized String redirectToLogin(String redirect) {
		if (StringUtils.isBlank(vurl)) {
			String vbackUrl = SettingUtils.getCommonSetting("base.url") + "/mobile/login";
			try {
				vurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + AccessTokenTool.AppId + "&redirect_uri=" + 
						URLEncoder.encode(vbackUrl, "UTF-8")
						+ "&response_type=code&scope=snsapi_userinfo&state=2#wechat_redirect";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return redirect + vurl;
	}
	
	public static void createMenu() throws UnsupportedCharsetException, MenuException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=cxZhDGTbZ9JJyDUI8Nmv_SNfBrh58z5zaPNqSxINEXr3iVGGtP5-AaT337RNCZp51xJd4FABjog0WcKviah5GtbElKO33Wqul8gGElUbfxI");
			StringEntity stringEntity = new StringEntity(JsonUtil.toJson(getMenu()), "utf-8");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
			
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				
				String responseContent = EntityUtils.toString(entity, "utf-8");
				
				System.out.println("===================>>" + responseContent);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String [] args) {
		try {
			createMenu();
//			System.out.println(JsonUtil.toJson(getMenu()));
		} catch (MenuException e) {
			e.printStackTrace();//ooWLRNfhwNR7z6wJ5kfS4Go81ZBS7JHa-AvF8xTm4JD431_-n9G-VK9-2wDGRMy594uGwL6h8-dpcOlu4HEzHQ
		}
	}
}
