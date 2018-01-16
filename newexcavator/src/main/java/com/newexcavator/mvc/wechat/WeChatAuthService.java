/**
 * 
 */
package com.newexcavator.mvc.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newexcavator.domain.DeployInfor;
import com.newexcavator.domain.Message;
import com.newexcavator.service.DeployInforService;
import com.newexcavator.service.MessageService;
import com.newexcavator.util.SettingUtils;
import com.newexcavator.wechat.model.message.MessageUtils;
import com.newexcavator.wechat.model.message.response.Article;
import com.newexcavator.wechat.model.message.response.NewsMessage;
import com.newexcavator.wechat.model.message.response.TextMessage;

/**
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/wechat")
public class WeChatAuthService {
	private static Logger log = Logger.getLogger(WeChatAuthService.class);
	// 自定义的TOken
	public static final String Token = "wajuejiwechattoken";

	@Autowired
	private DeployInforService deployInforService;
	
	@Autowired
	private MessageService messageService;

	/**
	 * 
	 * @param response
	 * @param signature 微信加密签名
	 * @param timestamp 时间戳
	 * @param nonce 随机数
	 * @param echostr  随机字符串
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public void weixinService(HttpServletResponse response,
			@RequestParam String signature, @RequestParam String timestamp,
			@RequestParam String nonce, @RequestParam String echostr) {
		log.debug("WECHAT=>signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " echostr:" + echostr);
		try {
			if (StringUtils.isNotEmpty(echostr)) {
				echostr = checkAuthentication(signature, timestamp, nonce, echostr);
				// 验证通过返回随即字串
				response.getWriter().write(echostr);
				response.getWriter().flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用来接收微信服务器的消息
	 * 
	 * @param request
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public @ResponseBody
	String weixinServicePost(HttpServletRequest request) {
		String respMessage = null;

		try {
			Map<String, String> requestMap = MessageUtils.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			System.out.println("fromUserName:" + fromUserName);
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			System.out.println("toUserName:" + toUserName);
			// 消息类型
			String msgType = requestMap.get("MsgType");
			System.out.println("msgType:" + msgType);
			// 消息ID
			String MsgId = requestMap.get("MsgId");
			System.out.println("MsgId:" + MsgId);
			// 事件类型
			String event = requestMap.get("Event");
			System.out.println("event:" + event);
			// 事件KEY值，与自定义菜单接口中KEY值对应
			String eventKey = requestMap.get("EventKey");
			System.out.println("eventKey:" + eventKey);
			// 消息内容
			String Content = requestMap.get("Content");
			System.out.println("Content:" + Content);
			//图片链接
			String PicUrl = requestMap.get("PicUrl");
			System.out.println("PicUrl:" + PicUrl);
			//媒体ID
			String MediaId = requestMap.get("MediaId");
			System.out.println("MediaId:" + MediaId);
			
			//获取openid
			/*request.getSession().setAttribute("openid", fromUserName);
			
			Object openid = request.getSession().getAttribute("openid");
			System.out.println((String)openid+"-=-=-=-=-=-=-=-'");*/
			
			if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_EVENT)) {//处理事件消息
				respMessage = this.dealWithEventMessage(event, fromUserName, toUserName, eventKey);
			} else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_TEXT)) {//处理文字消息
				Message message = new Message();
				/*if (Content.equals("wifi")){
					TextMessage textMessage = new TextMessage();
					textMessage.setToUserName(fromUserName);
					textMessage.setFromUserName(toUserName);
					textMessage.setCreateTime(new Date().getTime());
					textMessage.setMsgType(MessageUtils.RESP_MESSAGE_TYPE_TEXT);
					textMessage.setFuncFlag(0);
					// 由于href属性值必须用双引号引起，这与字符串本身的双引号冲突，所以要转义
					textMessage.setContent("欢迎,您可以点击<a href=\"http://115.29.167.166/weixin.htm?usrkey=651562998734&msg=weixinfun=wifi\">获取上网密码</a>然后返回认证页面输入上网密码");
					respMessage=MessageUtils.textMessageToXml(textMessage);
				}*/
				message.setContent(Content);
				message.setMsg_id(MsgId);
				message.setMsg_type(msgType);
				message.setOpen_id(fromUserName);
				
				messageService.saveMessage(message);
			} else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_IMAGE)) {//处理图片消息
				Message message = new Message();
				message.setMsg_id(MsgId);
				message.setMsg_type(msgType);
				message.setOpen_id(fromUserName);
				message.setMedia_id(MediaId);
				message.setPic_url(PicUrl);
				
				messageService.saveMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("respMessage:" + respMessage);
		return respMessage;
	}

	private String dealWithEventMessage(String event, String fromUserName, String toUserName,
			String eventKey) {
		String respMessage = "";
		if (event.equals(MessageUtils.EVENT_TYPE_CLICK)) {//click事件
			//====================图文消息
			NewsMessage newsMessage = new NewsMessage();
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtils.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);
			
			//====================文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtils.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			// 由于href属性值必须用双引号引起，这与字符串本身的双引号冲突，所以要转义
			//textMessage.setContent("您好！欢迎关注大至机械。");
			// 将文本消息对象转换成xml字符串
			respMessage = MessageUtils.textMessageToXml(textMessage);
			
			List<Article> articleList = new ArrayList<Article>();
			int key = Integer.valueOf(eventKey);
			String baseUrl = SettingUtils.getCommonSetting("base.url");
			switch (key) {
			case 10:// 
				textMessage.setContent("<a href='"+baseUrl + "/mobile/index?openid="+fromUserName+"'>点击查看二手机械</a>");
				respMessage = MessageUtils.textMessageToXml(textMessage);
				break;
			case 11:// 
				textMessage.setContent("<a href='"+baseUrl + "/mobile/login?openid="+fromUserName+"'>点击进入个人中心</a>");
				respMessage = MessageUtils.textMessageToXml(textMessage);
				break;
			case 12:// 
				textMessage.setContent("<a href='"+baseUrl + "/mobile/add?openid="+fromUserName+"'>点击发布二手机械</a>");
				respMessage = MessageUtils.textMessageToXml(textMessage);
				break;
			case 22:// 报修
				textMessage.setContent("雷沃挖掘机售后服务热线，028-66426666，大至机械设备有限公司将竭诚为您服务。");
				respMessage = MessageUtils.textMessageToXml(textMessage);
				break;

			case 23:// 维修咨询
				textMessage.setContent("尊敬的客户您好，需要维修咨询请您输入：区域+问题+您的姓名+联系方式，我们将有专门的工程师给您联系！也可以直接拨打雷沃服务热线028-66426666咨询。");
				respMessage = MessageUtils.textMessageToXml(textMessage);
				break;
			case 32:// 最新活动
				DeployInfor di = deployInforService.queryLastestDeployInforByTypeId(DeployInfor.TYPE_ACTIVITY);
				if (di != null) {
					Article article = new Article();
					article.setTitle(di.getTitle());
					article.setDescription(di.getDescription());
					//String baseUrl = SettingUtils.getCommonSetting("base.url");
					article.setPicUrl(baseUrl + "/img/" + di.getThumb_url());
					article.setUrl(baseUrl + "/mobile/show_deploy_infor?id=" + di.getId());
					articleList.add(article);
					// 设置图文消息个数
					newsMessage.setArticleCount(articleList.size());
					// 设置图文消息包含的图文集合
					newsMessage.setArticles(articleList);
					// 将图文消息对象转换成xml字符串
					respMessage = MessageUtils.newsMessageToXml(newsMessage);
				} else {
					textMessage.setContent("暂无活动！");
					// 将文本消息对象转换成xml字符串
					respMessage = MessageUtils.textMessageToXml(textMessage);
				}
				break;
			case 33:// 最新闻
				DeployInfor din = deployInforService.queryLastestDeployInforByTypeId(DeployInfor.TYPE_NEWS);
				if (din != null) {
					Article article = new Article();
					article.setTitle(din.getTitle());
					article.setDescription(din.getDescription());
					//String baseUrl = SettingUtils.getCommonSetting("base.url");
					article.setPicUrl(baseUrl + "/img/" + din.getThumb_url());
					article.setUrl(baseUrl + "/mobile/show_deploy_infor?id="
							+ din.getId());
					articleList.add(article);
					// 设置图文消息个数
					newsMessage.setArticleCount(articleList.size());
					// 设置图文消息包含的图文集合
					newsMessage.setArticles(articleList);
					// 将图文消息对象转换成xml字符串
					respMessage = MessageUtils.newsMessageToXml(newsMessage);
				}
				break;
			}
		} else if (event.equals(MessageUtils.EVENT_TYPE_SUBSCRIBE)) {//subscribe事件
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtils.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			textMessage.setContent("欢迎关注 二手挖掘机交易,我们将为您提供最优质的挖掘机交易信息！客服微信号：wjjjy666");
			// 将文本消息对象转换成xml字符串
			respMessage = MessageUtils.textMessageToXml(textMessage);
		}
		return respMessage;
	}

	/**
	 * Function:微信验证方法
	 * 
	 * @param signature 微信加密签名
	 * @param timestamp  时间戳
	 * @param nonce 随机数
	 * @param echostr 随机字符串
	 * @return
	 */
	private String checkAuthentication(String signature, String timestamp,
			String nonce, String echostr) {
		String result = "";
		// 将获取到的参数放入数组
		String[] ArrTmp = { Token, timestamp, nonce };
		// 按微信提供的方法，对数据内容进行排序
		Arrays.sort(ArrTmp);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ArrTmp.length; i++) {
			sb.append(ArrTmp[i]);
		}
		// 对排序后的字符串进行SHA-1加密
		String pwd = Encrypt(sb.toString());
		if (pwd.equals(signature)) {
			try {
				System.out.println("微信平台签名消息验证成功！");
				result = echostr;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("微信平台签名消息验证失败！");
		}
		return result;
	}

	/**
	 * 用SHA-1算法加密字符串并返回16进制串
	 * 
	 * @param strSrc
	 * @return
	 */
	private String Encrypt(String strSrc) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("错误");
			return null;
		}
		return strDes;
	}

	private String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
