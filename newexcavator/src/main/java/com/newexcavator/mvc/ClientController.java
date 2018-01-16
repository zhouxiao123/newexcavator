/**
 * 
 */
package com.newexcavator.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newexcavator.domain.Customer;
import com.newexcavator.domain.DeployInfor;
import com.newexcavator.domain.MachineInfor;
import com.newexcavator.domain.OrderList;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.domain.Wechat_tb;
import com.newexcavator.service.DeployInforService;
import com.newexcavator.service.MachineService;
import com.newexcavator.service.UserService;
import com.newexcavator.service.Wechat_tbService;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.JsonUtil;
import com.newexcavator.util.WechatUtils;
import com.newexcavator.wechat.model.message.response.WebOpenidModel;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/mobile")
public class ClientController {
	@Autowired
	private DeployInforService deployInforService;
	
	@Autowired
	private MachineService machineService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Wechat_tbService wechat_tbService;
	
	@RequestMapping(value = "/show_deploy_infor", method = RequestMethod.GET)
	public String del_news(Model model, @RequestParam Integer id) {
		DeployInfor di = deployInforService.queryDeployInforById(id);
		model.addAttribute("di", di);
		return "pages/mobile/deploy_infor";
	}
	
	@RequestMapping(value = "/open_call", method = RequestMethod.GET)
	public String open_call(Model model) {
		return "pages/mobile/open_call";
	}
	
	@RequestMapping(value = "/listUserRecord", method = RequestMethod.GET)
	public String listUserRecord(Model model, @RequestParam String openid,
			@RequestParam String phone) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("phone_number", phone);
		List<Customer> customers = userService.queryCustomer(param, null);
		
		model.addAttribute("customers", customers);
		return "pages/mobile/list_user_order";
	}
	
	@RequestMapping(value = "/bind_user", method = RequestMethod.GET)
	public String bind_user(Model model, HttpServletRequest request, @RequestParam(required=false) String code) {
		if (!StringUtils.isBlank(code)) {
			System.out.println("CODE=====>>>" + code);
			WebOpenidModel wom = WechatUtils.getOpenidInfor(code);
			if (!StringUtils.isBlank(wom.getOpenid())) {
				System.out.println("openid=====>>>" + wom.getOpenid());
				SysUsers su = userService.querySysUserByOpenid(wom.getOpenid());
				if (su != null) {//已绑定
					return "redirect:/mobile/listUserRecord?openid=" + wom.getOpenid() + "&phone=" + su.getCell_phone();
				}
				model.addAttribute("openid", wom.getOpenid());
			}
		}
		
		return "pages/mobile/bind_user";
	}
	
	@RequestMapping(value = "/saveBindInfo", method = RequestMethod.POST)
	public String saveBindInfo(Model model,HttpServletRequest request, @RequestParam String openid, @RequestParam String phone, @RequestParam String name) {
		System.out.println("openid====>" + openid);
		System.out.println("phone====>" + phone);
		System.out.println("name====>" + name);
		if (!StringUtils.isBlank(openid)) {
			SysUsers sysUsers = new SysUsers();
			sysUsers.setOpenid(openid);
			sysUsers.setCell_phone(phone);
			sysUsers.setName(name);
			userService.saveUser(sysUsers);
		}
		
		return "redirect:/mobile/listUserRecord?openid=" + openid + "&phone=" + phone;
	}
	
	@RequestMapping(value = "/saveOrderList", method = RequestMethod.POST)
	public @ResponseBody String saveOrderList(Model model, @RequestParam Integer quantity, @RequestParam String phone,
			@RequestParam String name, @RequestParam String address, @RequestParam Integer mechineid) {
		OrderList ol = new OrderList();
		ol.setAddress(address);
		ol.setName(name);
		ol.setPhone(phone);
		ol.setQuantity(quantity);
		ol.setMachine_id(mechineid);
		
		userService.saveOrderList(ol);
		//-------------------------------------------------------------------
		//openid
		Wechat_tb wechat_tb=wechat_tbService.queryWechatNo();
		String openId=wechat_tb.getWechatNo();
		//"o0N5iuMRRGqiqAJ_yTU_-XLb6jbI"
		//------
	
		String Type=null;
		MachineInfor machineInfor = machineService.queryMachineInforById(ol.getMachine_id());
		switch (machineInfor.getType_id()) {
		case 1:
			Type = "全新挖掘机";
			break;
		case 2:
			Type = "二手机械";
			break;
		case 3:
			Type = "零部件";
			break;
		case 4:
			Type = "机械租赁";
			break;
		}
		switch (machineInfor.getCat_id()) {
		case 1:
			Type = Type + "-挖掘机";
			break;
		case 2:
			Type = Type + "-装载机";
			break;
		case 3:
			Type = Type + "-汽车";
			break;
		case 4:
			Type = Type + "-零配件";
			break;
		case 5:
			Type = Type + "-其他";
			break;
		case 6:
			Type = Type + "-破碎头";
			break;
		}
		
		String strText = String.format("【预约】\n产品类型:%s\n产品名称:%s-%s\n预约人:%s\n联系电话:%s ", Type, machineInfor.getName(), machineInfor.getVersion(), ol.getName(), ol.getPhone());
		
		Map<String, Object> msg = new HashMap<String, Object>();

		//TODO just for test
		if(name!=null&&name.contains("@test")){
			msg.put("touser", "o0N5iuICzLFFyICh2LI8EqfKtWjk");
		}else{
			msg.put("touser", openId);
		}
		
		
		msg.put("msgtype", "text");
		Map<String, Object> txt = new HashMap<String, Object>();
		txt.put("content", strText);
		msg.put("text", txt);
		
		String msgStr = JsonUtil.toJson(msg);
		System.out.println("msgStr====>" + msgStr);
		
		DataUtil.sendMessage(msgStr, null, 1);
		
		
		
		//-------------------------------------------------------------------
		return JsonUtil.toJson("ok");
	}
	
	/**
	 * 验证是否登录过，登录成功session中是存在openid的
	 * 
	 * @param request
	 * @return
	 */
	private String validateSession(HttpServletRequest request) {
		Object openid = request.getSession().getAttribute("openid");
		return openid != null ? (String)openid : "o0N5iuMRRGqiqAJ_yTU_-XLb6jbI";
	}
}

