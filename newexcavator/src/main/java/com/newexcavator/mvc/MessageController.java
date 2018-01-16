/**
 * 
 */
package com.newexcavator.mvc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.newexcavator.domain.Message;
import com.newexcavator.domain.Reply;
import com.newexcavator.service.MessageService;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.JsonUtil;
import com.newexcavator.util.PageSupport;
import com.newexcavator.util.SettingUtils;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/admin")
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value = "/list_message", method = RequestMethod.GET)
	public String list_message(HttpServletRequest request, Model model) {
		
		PageSupport pageSupport = PageSupport.initPageSupport(request);
		
		List<Message> msgList = messageService.queryMessage(pageSupport);
		
		model.addAttribute("msgList", msgList);
		return "pages/message/list";
	}
	
	@RequestMapping(value = "/reply/{msgType}/{msgid}/{openid}/{show}", method = RequestMethod.GET)
	public String reply_message(HttpServletRequest request, Model model,
			@PathVariable String msgType, @PathVariable Integer msgid, @PathVariable String openid,
			@PathVariable Integer show, @RequestParam(required=false) Integer save) {
		
		List<Reply> replies = messageService.queryReplyByMessageId(msgid);
		if (show.intValue() == 1) {
			if (save != null && save.intValue() > 0) {
				model.addAttribute("msg", "回复成功！");
			}
		} else {
			if (save != null && save.intValue() == 0) {
				model.addAttribute("msg", "回复失败！");
			}
		}
		
		model.addAttribute("replies", replies);
		model.addAttribute("msgType", msgType);
		model.addAttribute("msgid", msgid);
		model.addAttribute("openid",openid);
		return "pages/message/reply";
	}
	
	@RequestMapping(value = "/reply/save", method = RequestMethod.POST)
	public String reply(HttpServletRequest request, Model model, @RequestParam String msgType,
			@RequestParam String openid, @RequestParam Integer msgid) {
		Reply rep = new Reply();
		rep.setMessage_id(msgid);
		rep.setMsg_type(msgType);
		
		boolean success = false;
		if (msgType.equals("text")) {
			String content = request.getParameter("content");
			rep.setContent(content);
			
			Map<String, Object> msg = new HashMap<String, Object>();
			msg.put("touser", openid);
			msg.put("msgtype", msgType);
			Map<String, Object> txt = new HashMap<String, Object>();
			txt.put("content", content);
			msg.put("text", txt);
			
			String msgStr = JsonUtil.toJson(msg);
			System.out.println("msgStr====>" + msgStr);
			
			success = DataUtil.sendMessage(msgStr, null, 1);
		} else if (msgType.equals("image")) {
			try {
				List<String> pathList = DataUtil.upload2Img(request, "file");
				if (!CollectionUtils.isEmpty(pathList)) {
					String filePath = pathList.get(0);
					//上传图片到微信服务器
					String sep = System.getProperty("file.separator");
					String fileDir = SettingUtils.getCommonSetting("upload2.path");
					File f = new File(fileDir + sep + filePath);
					String media_thumb_id = DataUtil.uploadFile(f, null, 1, "image");
					
					rep.setPath(filePath);
					rep.setMedia_id(media_thumb_id);
					
					Map<String, Object> msg = new HashMap<String, Object>();
					msg.put("touser", openid);
					msg.put("msgtype", msgType);
					Map<String, Object> img = new HashMap<String, Object>();
					img.put("media_id", media_thumb_id);
					msg.put("image", img);
					
					String msgStr = JsonUtil.toJson(msg);
					System.out.println("msgStr====>" + msgStr);
					
					success = DataUtil.sendMessage(msgStr, null, 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int show = 0;
		
		if (success) {
			show = 1;
			messageService.saveReply(rep);
		}
		
		return "redirect:/admin/reply/" + msgType + "/" + msgid + "/" + openid + "/" + show + "?save=1"; 
	}
}
