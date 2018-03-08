/**
 * 
 */
package com.newexcavator.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.newexcavator.domain.Advertisement;
import com.newexcavator.domain.Brand;
import com.newexcavator.domain.City;
import com.newexcavator.domain.Commodity;
import com.newexcavator.domain.ExcavatorType;
import com.newexcavator.domain.Machine;
import com.newexcavator.domain.MachinePic;
import com.newexcavator.domain.Order;
import com.newexcavator.domain.Point;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.domain.Version;
import com.newexcavator.service.AdvertisementService;
import com.newexcavator.service.DeployInforService;
import com.newexcavator.service.MachineService;
import com.newexcavator.service.UserService;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.DateFormatter;
import com.newexcavator.util.JsonUtil;
import com.newexcavator.util.PageSupport;
import com.newexcavator.util.QRCodeUtil;
import com.newexcavator.util.SettingUtils;
import com.newexcavator.util.sms.Sendsms;
import com.newexcavator.wechat.Sign;

/**
 * @author Administrator
 * 
 */
@Controller
public class MobileController {

	@Autowired
	private MachineService machineService;

	@Autowired
	private DeployInforService deployInforService;
	
	@Autowired
	private AdvertisementService advertisementService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/mobile", method = RequestMethod.GET)
	public String mobile(Model model,HttpServletRequest request) {
		List<Advertisement> advertisement=advertisementService.queryAdvertisementList();
		model.addAttribute("advertisement", advertisement);	
		PageSupport ps =PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", 1);
		param.put("type", 1);
		ps.setPageSize(12);
		List<Machine> ms1 = machineService.queryMachine(param,ps);
		param.remove("type");
		param.put("type",2);
		List<Machine> ms2 = machineService.queryMachine(param,ps);
		/*List<Machine> ms1 = new ArrayList<Machine>();
		List<Machine> ms2 = new ArrayList<Machine>();
		if(!CollectionUtils.isEmpty(ms)){
			for(Machine m:ms){
				if(m.getM_type()==1){
					ms1.add(m);
				}else{
					ms2.add(m);
				}
			}
		}*/
		model.addAttribute("ms1", ms1);
		model.addAttribute("ms2", ms2);
		return "pages/excavator/index2";
	}
	
	
	/**
	 * 验证是否登录过，登录成功session中是存在openid的
	 * 
	 * @param request
	 * @return
	 */
	private SysUsers validateSession(HttpServletRequest request) {
		Object su = request.getSession().getAttribute("sysUsers");
		return (SysUsers)su;
	}
	
	@RequestMapping(value = "/mobile/add", method = RequestMethod.GET)
	public String mobile_add(Model model, HttpServletRequest request,@RequestParam (required = false) String openid) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		//SysUsers sysuser = userService.querySysUserByOpenid(openid);
		//SysUsers sysuser = userService.querySysUserByOpenid("oCwisjrKR1NbpMetmZYpeYm5fGBk");
		/*if (sysuser == null) {
			return "redirect:login";
		}*/
		List<ExcavatorType> et = machineService.queryExcavatorType();
		List<City> cs = machineService.queryCityByPid(0);
		model.addAttribute("et", et);
		model.addAttribute("cs", cs);
		model.addAttribute("su", sus);
		String token = DataUtil.generateRandomString(8);
		request.getSession().setAttribute("token", token);
		model.addAttribute("token", token);
		return "pages/excavator/add";

	}
	
	@RequestMapping(value = "/mobile/getBrand", method = RequestMethod.GET)
	public void getBrand(Model model,@RequestParam Integer id,PrintWriter out) {
		List<Brand> bs = machineService.queryBrandByExcavatorTypeId(id);
		out.write(JsonUtil.toJson(bs));
		out.flush();
	}
	
	@RequestMapping(value = "/mobile/getVersion", method = RequestMethod.GET)
	public void getVersion(Model model,@RequestParam Integer tid,@RequestParam Integer bid,PrintWriter out) {
		List<Version> vs = machineService.queryVersionByExcavatorTypeIdAndBrandId(tid,bid);
		out.write(JsonUtil.toJson(vs));
		out.flush();
	}
	
	@RequestMapping(value = "/mobile/getCity", method = RequestMethod.GET)
	public void getCity(Model model,@RequestParam Integer cid,PrintWriter out) {
		List<City> cs = machineService.queryCityByPid(cid);
		out.write(JsonUtil.toJson(cs));
		out.flush();
	}
	
	@RequestMapping(value = "/mobile/detail", method = RequestMethod.GET)
	public String mobile_detail(Model model,HttpServletRequest request,@RequestParam Integer id,@RequestParam(required=false) String type,@RequestParam(required=false) Integer change) {
		Machine m = machineService.queryMachineById(id);
		model.addAttribute("m", m);
		Advertisement adv = advertisementService.queryBottomAdvertisement(1);
		model.addAttribute("adv", adv);
		if(!StringUtils.isBlank(type)){
			model.addAttribute("type", type);
		}
		if(change!=null && change.intValue() == 1){
			model.addAttribute("change", change);
		}
		
		Map<String,String> param = new HashMap<String,String>();
		param = Sign.getSign(SettingUtils.getCommonSetting("base.url")+request.getServletPath()+(StringUtils.isBlank(request.getQueryString())?"":"?"+request.getQueryString()));
		System.out.println(request.getServletPath()+"?"+request.getQueryString()+"--------------");
		model.addAttribute("sign", param);
		return "pages/excavator/detail";
	}
	
	@RequestMapping(value = "/mobile/detail_share", method = RequestMethod.GET)
	public String mobile_detail_share(Model model,HttpServletRequest request,@RequestParam Integer user_id) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		userService.updateShareTime(sus.getId());
		return "pages/excavator/success";
	}
	
	@RequestMapping(value = "/mobile/index", method = {RequestMethod.GET,RequestMethod.POST})
	public String index(Model model,HttpServletRequest request,@RequestParam(required = false) String openid) {
		/*try {
			Map<String, String> requestMap = MessageUtils.parseXml(request);
		// 发送方帐号（open_id）
		String fromUserName = requestMap.get("FromUserName");
		System.out.println("fromUserName:=========" + fromUserName);
		UserInfo ui = WechatUtils.getUserInfoByOpenId(fromUserName, null, 0);
		if(ui!=null){
			SysUsers sysUsers = new SysUsers();
			sysUsers.setOpenid(ui.getOpenid());
			sysUsers.setNickname(ui.getNickname());
			sysUsers.setHead(ui.getHeadimgurl());
			userService.saveUser(sysUsers);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*String openids = validateSession(request);
		if (StringUtils.isBlank(openids)) {
			if (StringUtils.isBlank(openid)) {
				return "redirect:login";
			} else{
				request.getSession().setAttribute("openid", openid);
			}
		} else {
			openid=openids;
		}*/
		/*SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}*/
		PageSupport ps =PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", 1);
		param.put("type", 1);
		ps.setPageSize(12);
		List<Machine> ms1 = machineService.queryMachine(param,ps);
		param.remove("type");
		param.put("type",2);
		List<Machine> ms2 = machineService.queryMachine(param,ps);
		/*List<Machine> ms1 = new ArrayList<Machine>();
		List<Machine> ms2 = new ArrayList<Machine>();
		if(!CollectionUtils.isEmpty(ms)){
			for(Machine m:ms){
				if(m.getM_type()==1){
					ms1.add(m);
				}else{
					ms2.add(m);
				}
			}
		}*/
		model.addAttribute("ms1", ms1);
		model.addAttribute("ms2", ms2);
		return "pages/excavator/index";
	}
	@RequestMapping(value = "/mobile/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String list(Model model,HttpServletRequest request,
			@RequestParam(required=false) String search_name,
			@RequestParam(required=false) Integer type,
			@RequestParam(required=false) Integer city_id,
			@RequestParam(required=false) String city_name,
			@RequestParam(required=false) Integer exb_id,
			@RequestParam(required=false) String exb_name) {
		PageSupport ps =PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", 1);
		if(!StringUtil.isBlank(search_name)){
			param.put("search_name", search_name);			
			model.addAttribute("search_name", search_name);
		} 
			if(type!=null && type.intValue() > 0){
				param.put("type", type);
				model.addAttribute("type", type);
				List<Brand> bs = machineService.queryBrandByExcavatorTypeId(type);
				model.addAttribute("bs", bs);
			}
			if(city_id!=null && city_id.intValue() > 0){
				param.put("city", city_id);
				model.addAttribute("city_id", city_id);
				model.addAttribute("city_name", city_name);
			}
			if(exb_id!=null && exb_id.intValue() > 0){
				param.put("brand", exb_id);
				model.addAttribute("exb_id", exb_id);
				model.addAttribute("exb_name", exb_name);
			}
			
			List<Machine> ms = machineService.queryMachine(param,ps);
			List<City> cs = machineService.queryCityByPid(0);			
			model.addAttribute("cs", cs);
			model.addAttribute("ms", ms);
			return "pages/excavator/list";
		
		
	}
	@RequestMapping(value = "/mobile/login", method = RequestMethod.GET)
	public String mobile_login(Model model,HttpServletRequest request,@RequestParam(required=false) String code,@RequestParam(required=false) String openid,@RequestParam(required=false) Integer state) {
		//if (!StringUtils.isBlank(code)) {
			/*WebOpenidModel wom = WechatUtils.getOpenidInfor(code);
			if (!StringUtils.isBlank(wom.getOpenid())) {*/
			/*String openid = validateSession(request);
			System.out.println("login:----------"+openid);
			if (StringUtils.isBlank(openid)) {
				model.addAttribute("msg", "请先关注我们,微信号wjjjy888");
				return "pages/topic/blank";
			}*/
			
				SysUsers sus = validateSession(request);
				if (sus != null) {
					return "redirect:person";
				}
				/*SysUsers su = userService.querySysUserByOpenid(openid);
				UserInfo ui = WechatUtils.getUserInfoByOpenId(openid, null, 0);
				if (su != null) {
					su.setNickname(ui.getNickname());
					su.setHead(ui.getHeadimgurl());
					userService.updateSysUsers(su);
					//request.getSession().setAttribute("openid", wom.getOpenid());
					return "redirect:person";
				}
				model.addAttribute("openid", openid);
				model.addAttribute("head", ui.getHeadimgurl());
				model.addAttribute("nick_name", ui.getNickname());*/
				/*} else {
				model.addAttribute("msg", "未知错误！");
				return "pages/topic/blank";
			}
		} else if (StringUtils.isBlank(code)) {// code为空，表示用户不同意授权
			model.addAttribute("msg", "您不能进行该操作,没有得到授权！");
			return "pages/topic/blank";
		} */
		if(state!=null && state.intValue() == 1){
			model.addAttribute("state", state);
		}
		return "pages/excavator/login";
	}
	
	@RequestMapping(value = "/mobile/login_save", method = RequestMethod.POST)
	public String mobile_login_save(Model model,HttpServletRequest request,@RequestParam String phone, @RequestParam String password) {
		//判断登陆
		SysUsers u = userService.querySysUserByUsername(phone, null);
		if(u!=null){
		PasswordEncoder pe = new BCryptPasswordEncoder();
		if (pe.matches(password, u.getPassword())) {
			if(u.getLast_login_time()!=null){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date1 = df.format(u.getLast_login_time());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date2 = sdf.format(new Date());
				System.out.println(date1+"+++++++++++"+date2);
				if(!date1.equals(date2)){
					System.out.println(date1+"------"+date2);
					userService.addPoint(u.getId(), 1);
				}
			} else if(u.getLast_login_time()==null) {
				userService.addPoint(u.getId(), 1);
			}
			userService.updateLoginInfor(u.getId());
			u.setLast_login_time(new Date());
			request.getSession().setAttribute("sysUsers", u);
			return "redirect:person";
		}
		}
			return "redirect:login?state=1";
		
	}
	
	@RequestMapping(value = "/mobile/reg", method = RequestMethod.GET)
	public String mobile_reg(Model model,HttpServletRequest request,
			@RequestParam(required = false) String invited,
			@RequestParam(required = false) Integer exist,
			@RequestParam(required = false) Integer noinvited,
			@RequestParam(required = false) Integer vali,
			@RequestParam(required = false) String phone,
			@RequestParam(required = false) String name
			) throws UnsupportedEncodingException {
		//注册
		if(!StringUtils.isBlank(phone)){
			model.addAttribute("phone", phone);
		}
		if(!StringUtils.isBlank(name)){
			model.addAttribute("name", new String(name.getBytes("ISO-8859-1"),"UTF-8"));
		}
		if(!StringUtils.isBlank(invited)){
			model.addAttribute("invited", invited);
		}
		if(vali!=null&&vali.intValue()==1){
			model.addAttribute("msg", "验证码错误");
		}
		if(exist!=null&&exist.intValue()==1){
			model.addAttribute("exist", exist);
		}
		if(noinvited!=null&&noinvited.intValue()==1){
			model.addAttribute("noinvited", noinvited);
		}
		return "pages/excavator/reg";
	}
	
	@RequestMapping(value = "/mobile/reg_save", method = RequestMethod.POST)
	public String mobile_reg_save(Model model,RedirectAttributes redirect,HttpServletRequest request,@RequestParam String phone,@RequestParam String name, @RequestParam String password,@RequestParam(required = false)String invited,@RequestParam(required = false) String valicode) throws UnsupportedEncodingException {
		//注册保存
		Integer invited_id=0;
		SysUsers sus = userService.querySysUserByUsername(phone, null);
		if(sus == null){
			if(!StringUtils.isBlank(invited)){
				SysUsers s = userService.querySysUserByUsername(invited, null);
				if(s==null){
					redirect.addAttribute("phone", phone);
					redirect.addAttribute("name", name);
					return "redirect:reg?noinvited=1";
				}
				invited_id=s.getId();
				Point p = userService.queryPoint();
				userService.addPoint(s.getId(), p.getPoint());
			}
			
		/*Object vco = request.getSession().getAttribute("VC_" + phone);
		if(vco == null || !valicode.toLowerCase().equals(((String)vco).toLowerCase())) {
			redirect.addAttribute("phone", phone);
			redirect.addAttribute("name", name);
			return "redirect:reg?vali=1&invited="+invited;
		}*/
		
		PasswordEncoder pe = new BCryptPasswordEncoder();
		SysUsers su = new SysUsers();
		su.setCell_phone(phone);
		su.setUsername(phone);
		su.setName(name);
		su.setInvited_id(invited_id);
		//su.setPassword(pe.encode(password));
		userService.saveUser(su);
		userService.updatePassword(pe.encode(password), su.getId());
		
		
		//request.getSession().removeAttribute("VC_" + phone);
		
		//加入session
		userService.updateLoginInfor(su.getId());
		su.setLast_login_time(new Date());
		userService.addPoint(su.getId(), 1);
		request.getSession().setAttribute("sysUsers", su);
		return "redirect:person";
		} else {
			redirect.addAttribute("phone", phone);
			redirect.addAttribute("name", name);
			return "redirect:reg?exist=1&invited="+invited;
		}
	}
	
	@RequestMapping(value = "/mobile/person", method = RequestMethod.GET)
	public String person(Model model,HttpServletRequest request,@RequestParam(required = false) Integer update) {
		/*String openid = validateSession(request);
		if (StringUtils.isBlank(openid)) {
			return "redirect:login";
		}
		SysUsers sysuser = userService.querySysUserByOpenid(openid);
		//SysUsers sysuser = userService.querySysUserByOpenid("oCwisjrKR1NbpMetmZYpeYm5fGBk");
		if(sysuser==null){
			return "redirect:login";
		}*/
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		//SysUsers sysuser = new SysUsers();
		SysUsers su = userService.querySysUserByid(sus.getId());
		request.getSession().setAttribute("sysUsers", su);
		model.addAttribute("user", su);
		if(update!=null && update.intValue() == 1){
			model.addAttribute("update", update);
		}
		return "pages/excavator/person";
	}
	
	@RequestMapping(value = "/mobile/person_share", method = RequestMethod.GET)
	public String person_share(Model model,HttpServletRequest request,@RequestParam(required = false) Integer update) {
		
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		//SysUsers sysuser = new SysUsers();
		model.addAttribute("user", sus);
		Map<String,String> param = new HashMap<String,String>();
		param = Sign.getSign(SettingUtils.getCommonSetting("base.url")+request.getServletPath()+(StringUtils.isBlank(request.getQueryString())?"":"?"+request.getQueryString()));
		System.out.println(request.getServletPath()+"?"+request.getQueryString()+"--------------");
		model.addAttribute("sign", param);
		String sep = System.getProperty("file.separator");
		try {
			QRCodeUtil.encode(SettingUtils.getCommonSetting("base.url")+sep+"mobile"+sep+"reg?invited="+sus.getUsername(),SettingUtils.getCommonSetting("upload.qrcode.path")+sep+sus.getId()  , true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("qrcode",SettingUtils.getCommonSetting("base.url")+sep+"img"+sep+"qrcode"+sep+ sus.getId());
		return "pages/excavator/person_share";
	}
	
	
	@RequestMapping(value = "/mobile/my_commodity", method = RequestMethod.GET)
	public String my_commodity(Model model,HttpServletRequest request,@RequestParam(required = false) Integer update) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		Map<String ,Object> param = new HashMap<String,Object>();
		PageSupport ps = PageSupport.initPageSupport(request);
		param.put("user_id", sus.getId());
		List<Order> os = machineService.queryOrder(param,ps);
		model.addAttribute("os", os);
		return "pages/excavator/my_commodity";
	}
	
	@RequestMapping(value = "/mobile/my_commodity_detail", method = RequestMethod.GET)
	public String my_commodity_detail(Model model,HttpServletRequest request,@RequestParam Integer id) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		Map<String ,Object> param = new HashMap<String,Object>();
		PageSupport ps = PageSupport.initPageSupport(request);
		param.put("id", id);
		
		List<Order> os = machineService.queryOrder(param, ps);
		model.addAttribute("o", os.get(0));
		return "pages/excavator/my_commodity_detail";
	}
	
	@RequestMapping(value = "/mobile/publicList", method = {RequestMethod.GET,RequestMethod.POST})
	public String publicList(Model model,HttpServletRequest request,@RequestParam(required=false) Integer change) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		PageSupport ps =PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("userId", sus.getId());
		//param.put("userId", 1);
		List<Machine> ms = machineService.queryMachine(param,ps);
		model.addAttribute("ms", ms);
		if(change!=null && change.intValue() == 1){
			model.addAttribute("change", change);
		}
		return "pages/excavator/publicList";
	}
	
	@RequestMapping(value = "/mobile/change_close", method = RequestMethod.GET)
	public String change_close(Model model,RedirectAttributes redirect,HttpServletRequest request,@RequestParam Integer id,@RequestParam Integer close) {

		machineService.updateMachineClose(id, close);
		redirect.addAttribute("change", 1);
		return "redirect:publicList";
	}
	
	@RequestMapping(value = "/mobile/rank", method = {RequestMethod.GET,RequestMethod.POST})
	public String rank(Model model,RedirectAttributes redirect,HttpServletRequest request) {
		SysUsers ss = validateSession(request);
		if (ss == null) {
			return "redirect:login";
		}
		Map<String,Object> param = new HashMap<String,Object>();
		PageSupport pageSupport = PageSupport.initPageSupport(request);
		pageSupport.setPageSize(100);
		List <SysUsers> sus = userService.querySysUserRank(param, pageSupport);
		Integer flag = 0;
		for(SysUsers s:sus){
			s.setUsername(s.getUsername().replace(s.getUsername().substring(3,7), "****"));
			if(s.getId()==ss.getId()){
				flag = s.getRank();
			}
		}
		
		model.addAttribute("flag", flag);
		model.addAttribute("sus", sus);
		return "pages/excavator/rank";
	}
	
	
	@RequestMapping(value = "/mobile/change_commodity", method = {RequestMethod.GET,RequestMethod.POST})
	public String change_commodity(Model model,RedirectAttributes redirect,HttpServletRequest request) {
		Map<String,Object> param = new HashMap<String,Object>();
		PageSupport pageSupport = PageSupport.initPageSupport(request);
		List <Commodity> cos = machineService.queryCommodity(param, pageSupport);
		model.addAttribute("cos", cos);
		return "pages/excavator/change_commodity";
	}
	
	@RequestMapping(value = "/mobile/commodity_change_save", method = {RequestMethod.GET,RequestMethod.POST})
	public String commodity_change_save(Model model,RedirectAttributes redirect,HttpServletRequest request,@RequestParam String name,@RequestParam String phone,@RequestParam String address,@RequestParam Integer user_id,@RequestParam Integer id) {
		Order o = new Order();
		o.setUser_id(user_id);
		o.setCo_id(id);
		o.setName(name);
		o.setPhone(phone);
		Commodity c = machineService.queryCommodityById(id);
		o.setCo_name(c.getName());
		o.setCo_description(c.getDescription());
		o.setCo_path(c.getPath());
		o.setCo_point(c.getPoint());
		o.setAddress(address);
		SysUsers su = userService.querySysUserByid(user_id);
		if(su.getPoint() < c.getPoint()){
			return "redirect:commodity_detail?error=1&id="+id;
		}
		machineService.saveOrder(o);
		userService.subPoint(user_id, c.getPoint());
		return "redirect:commodity_detail?success=1&id="+id;
	}
	
	@RequestMapping(value = "/mobile/commodity_detail", method = RequestMethod.GET)
	public String commodity_detail(Model model,RedirectAttributes redirect,HttpServletRequest request,@RequestParam Integer id,@RequestParam(required = false) Integer error,@RequestParam(required = false) Integer success) {
		
		
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		//SysUsers sysuser = new SysUsers();
		SysUsers su = userService.querySysUserByid(sus.getId());
		model.addAttribute("su", su);
		Commodity co = machineService.queryCommodityById(id);
		model.addAttribute("co", co);
		if(error!=null && error==1){
			model.addAttribute("error", error);
		}
		if(success!=null && success==1){
			model.addAttribute("success", success);
		}
		return "pages/excavator/commodity_detail";
	}
	
	@RequestMapping(value = "/mobile/save", method = RequestMethod.POST)
	public String save(Model model,HttpServletRequest request,
			@RequestParam Integer excavator_type,
			@RequestParam Integer excavator_brand,
			@RequestParam Integer excavator_version,
			@RequestParam Integer used_time,
			@RequestParam Float price,
			@RequestParam Integer change_price,
			@RequestParam Integer province,
			@RequestParam Integer city,
			@RequestParam Integer imported,
			@RequestParam Integer qualified,
			@RequestParam Integer receipt,
			@RequestParam String production_date,
			@RequestParam String buy_date,
			@RequestParam String code_no,
			@RequestParam Integer modified,
			@RequestParam Integer fixed,
			@RequestParam Integer old_level,
			@RequestParam Integer use,
			@RequestParam Integer current_status,
			@RequestParam String description,
			@RequestParam String link_name,
			@RequestParam String phone,
			@RequestParam String token,
			//@RequestParam String qq,
			@RequestParam String[] filename,
			@RequestParam Integer first_num) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		
		if(!isRepeatSubmit(token,request)){
		request.getSession().removeAttribute("token");
		Machine m = new Machine();
		
		m.setUser_id(sus.getId());
		//m.setUser_id(1);
		
		m.setBrand(excavator_brand);
		m.setBuy_date(DateFormatter.stringToDate(buy_date, "yyyy-MM-dd"));
		m.setCode_no(code_no);
		m.setCurrent_status(current_status);
		m.setDescription(description);
		m.setFixed(fixed);
		m.setImported(imported);
		m.setLink_name(link_name);
		m.setM_type(excavator_type);
		m.setModified(modified);
		m.setOld_level(old_level);
		m.setPhone(phone);
		m.setPlace_c(city);
		m.setPlace_p(province);
		m.setPrice(price);
		m.setChange_price(change_price);
		m.setProduction_date(DateFormatter.stringToDate(production_date, "yyyy-MM-dd"));
		//m.setQq(qq);
		m.setQualified(qualified);
		m.setReceipt(receipt);
		m.setUse(use);
		m.setUsed_time(used_time);
		m.setVersion(excavator_version);
		for(String fn:filename){
			MachinePic mp = new MachinePic();
			String path="";
			try {
				 path = DataUtil.copyToDir(fn, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.setPath(path);
			mp.setCover(0);
			m.addMp(mp);
		}
		m.getMp().get(first_num).setCover(1);
		
		machineService.saveMachine(m);
		}
		return "redirect:publicList";
	}
	

	private boolean isRepeatSubmit(String token,HttpServletRequest request) {
		String client_token = token;
		// 1、如果用户提交的表单数据中没有token，则用户是重复提交了表单
		if (client_token == null) {
			return true;
		}
		// 取出存储在Session中的token
		String server_token = (String) request.getSession().getAttribute(
				"token");
		// 2、如果当前用户的Session中不存在Token(令牌)，则用户是重复提交了表单
		if (server_token == null) {
			return true;
		}
		// 3、存储在Session中的Token(令牌)与表单提交的Token(令牌)不同，则用户是重复提交了表单
		if (!client_token.equals(server_token)) {
			return true;
		}

		return false;
	}
	
	/*@RequestMapping(value = "/mobile/list_mechine", method = RequestMethod.GET)
	public String mechineList(HttpServletRequest request, Model model,
			@RequestParam(required = false) String type,@RequestParam(required = false) Integer cat_id,
			@RequestParam(required = false) String searchName) {
		int moduleId = 1;
		if (StringUtils.equals(type, "old")) {
			moduleId = 2;
		} else if (StringUtils.equals(type, "part")) {
			moduleId = 3;
		} else if (StringUtils.equals(type, "lease")) {
			moduleId = 4;
		}
		
		List<Category> categories = machineService.queryCategoryByModuleId(moduleId);
		
		if (request.getMethod().equals("GET")) {// GET方式提交需要处理参数为中文的字符集问题
			try {
				if (!org.apache.commons.lang.StringUtils.isBlank(searchName)) {
					searchName = new String(searchName.getBytes("iso8859-1"), "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		PageSupport pageSupport = PageSupport.initPageSupport(request);

		if (cat_id == null || cat_id.intValue() <= 0) {
			cat_id = categories.get(0).getId();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cat_id", cat_id);
		if (!StringUtils.isEmptyOrWhitespace(searchName)) {
			String sName = searchName.toString().trim();
			String[] Sarray = sName.split(" ");
			if (Sarray.length < 2) {
				param.put("name", Sarray[0]);
			} else {
				param.put("name", Sarray[0]);
				param.put("version", Sarray[1]);
			}
		}
		List<MachineInfor> mechines = machineService.queryMachineInforByTypeId(moduleId, param, pageSupport);
		//zx_可以进行类型和名称混合查找I
		//---------------------------------------------------------------------------
		if (!StringUtils.isEmptyOrWhitespace(searchName)) {
			if(mechines==null||mechines.size()==0){
			String sName = searchName.toString().trim();
			String[] Sarray = sName.split(" ");
			if (Sarray.length < 2) {
				param.put("version", Sarray[0]);
				param.remove("name");
			} else {
				param.put("name", Sarray[1]);
				param.put("version", Sarray[0]);
			}
			mechines = machineService.queryMachineInforByTypeId(moduleId, param, pageSupport);
			}
		}
		
		//---------------------------------------------------------------------------
		model.addAttribute("mechines", mechines);
		model.addAttribute("type", type);
		model.addAttribute("name", searchName);
		model.addAttribute("categories", categories);
		model.addAttribute("cat_id", cat_id);
		return "pages/mobile/list_mechine";
	}

	@RequestMapping(value = "/mobile/loadNextPage", method = RequestMethod.POST)
	public String loadNextPage(HttpServletRequest request, Model model,
			@RequestParam String type,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) Integer cat_id,
			@RequestParam Integer next) {
		
		int moduleId = 1;
		if (StringUtils.equals(type, "old")) {
			moduleId = 2;
		} else if (StringUtils.equals(type, "part")) {
			moduleId = 3;
		} else if (StringUtils.equals(type, "lease")) {
			moduleId = 4;
		}
		
		List<Category> categories = machineService.queryCategoryByModuleId(moduleId);
		
		PageSupport pageSupport = PageSupport.initPageSupport(request);
		pageSupport.setPageOffset((next-1) * pageSupport.getPageSize());

		if (cat_id == null || cat_id.intValue() <= 0) {
			cat_id = categories.get(0).getId();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cat_id", cat_id);
		if (!StringUtils.isEmptyOrWhitespace(searchName)) {
			String sName = searchName.trim();
			String[] Sarray = sName.split(" ");
			if (Sarray.length < 2) {
				param.put("name", Sarray[0]);
			} else {
				param.put("name", Sarray[0]);
				param.put("version", Sarray[1]);
			}
		}
		List<MachineInfor> mechines = machineService.queryMachineInforByTypeId(moduleId, param, pageSupport);

		//zx_可以进行类型和名称混合查找I
		//---------------------------------------------------------------------------
				if (!StringUtils.isEmptyOrWhitespace(searchName)) {
					if(mechines==null||mechines.size()==0){
					String sName = searchName.toString().trim();
					String[] Sarray = sName.split(" ");
					if (Sarray.length < 2) {
						param.put("version", Sarray[0]);
					} else {
						param.put("name", Sarray[1]);
						param.put("version", Sarray[0]);
					}
					mechines = machineService.queryMachineInforByTypeId(moduleId, param, pageSupport);
					}
				}
				
				//---------------------------------------------------------------------------
		model.addAttribute("mechines", mechines);
		return "pages/mobile/snippet/list_mechine";
	}*/

	/*@RequestMapping(value = "/mobile/detail_mechine", method = RequestMethod.GET)
	public String detail_mechine(Model model, @RequestParam String type,
			@RequestParam Integer id) {
		MachineInfor machineInfor = machineService.queryMachineInforById(id);
		if (!StringUtils.isEmpty(machineInfor.getDescription())) {
			Document doc = Jsoup.parse(machineInfor.getDescription());
			Elements es = doc.select("img");
			Pattern p = Pattern.compile("^http:\\/\\/");
			for (Element e : es) {
				String src = e.attr("src");
				if (p.matcher(src).find()) {
					continue;
				}
				String baseUrl = SettingUtils.getCommonSetting("base.url.noPN");
				e.attr("src", baseUrl + src);
			}
			machineInfor.setDescription(doc.body().html());
		}
		model.addAttribute("mi", machineInfor);
		model.addAttribute("type", type);
		return "pages/mobile/detail_mechine";
	}

	@RequestMapping(value = "/mobile/list_news", method = RequestMethod.GET)
	public String list_news(HttpServletRequest request, Model model,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String searchName) {
		int moduleId = 1;
		if (StringUtils.equals(type, "news")) {
			moduleId = 2;
		}

		PageSupport pageSupport = PageSupport.initPageSupport(request);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("searchName", searchName);
		List<DeployInfor> infors = deployInforService.queryDeployInforByTypeId(
				moduleId, param, pageSupport);

		model.addAttribute("news", infors);
		model.addAttribute("type", type);
		return "pages/mobile/list_news";
	}
	
	@RequestMapping(value = "/mobile/loadNextNews", method = RequestMethod.POST)
	public String loadNextNews(HttpServletRequest request, Model model,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String searchName,
			@RequestParam Integer next) {
		int moduleId = 1;
		if (StringUtils.equals(type, "news")) {
			moduleId = 2;
		}

		PageSupport pageSupport = PageSupport.initPageSupport(request);
		pageSupport.setPageOffset((next-1) * pageSupport.getPageSize());
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("searchName", searchName);
		List<DeployInfor> infors = deployInforService.queryDeployInforByTypeId(moduleId, param, pageSupport);

		model.addAttribute("news", infors);
		return "pages/mobile/snippet/list_news";
	}*/

	@RequestMapping(value = "/mobile/user_detail", method = RequestMethod.GET)
	public String user_detail(Model model,HttpServletRequest request,@RequestParam(required = false) Integer update) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		//SysUsers sysuser = userService.querySysUserByOpenid("oCwisjrKR1NbpMetmZYpeYm5fGBk");
		model.addAttribute("su", sus);
		if(update!=null){
			model.addAttribute("update", update);
		}
		return "pages/excavator/user_detail";
	}
	
	@RequestMapping(value = "/mobile/user_update", method = RequestMethod.POST)
	public String user_update(Model model,HttpServletRequest request,RedirectAttributes redirect,@RequestParam Integer id,@RequestParam String name,@RequestParam String username,@RequestParam String password,@RequestParam String h_password) {
		
		SysUsers sysuser = new SysUsers();
		sysuser.setId(id);
		sysuser.setName(name);
		sysuser.setCell_phone(username);
		userService.updateSysUsersById(sysuser);
		model.addAttribute("update", 1);
		SysUsers u = userService.querySysUserByUsername(username, null);
		request.getSession().setAttribute("sysUsers", u);
		if(!StringUtils.isBlank(password)){
			PasswordEncoder pe = new BCryptPasswordEncoder();
			if (pe.matches(h_password, u.getPassword())) {
				userService.updatePassword(pe.encode(password),u.getId());
			} else {
				//model.addAttribute("update", "0");
				return "redirect:user_detail?update=0";
			}
		}
		
		return "redirect:person?update=1";
		
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String test_share(Model model,HttpServletRequest request) {
		Map<String,String> param = new HashMap<String,String>();
		String baseUrl = SettingUtils.getCommonSetting("base.url");
		param = Sign.getSign("http://a.ohless.net"+request.getServletPath()+(StringUtils.isBlank(request.getQueryString())?"":"?"+request.getQueryString()));
		System.out.println(request.getServletPath()+"?"+request.getQueryString()+"--------------");
		model.addAttribute("sign", param);
		return "pages/share/test_share";
	}
	
	@RequestMapping(value = "/get2", method = RequestMethod.GET)
	public String after_share(Model model) {
		
		return "pages/share/after_share";
	}
	
	/**
	 * 获取校验码
	 * 
	 * @param request
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/mobile/getvalidate", method = RequestMethod.POST)
	public @ResponseBody String getvalidate(HttpServletRequest request,HttpServletResponse response, Model model, @RequestParam String username, @RequestParam(required = false) String type, @RequestParam(required=false) Integer id) {
		String info = "ok";
			if (StringUtils.equals(type, "mod")) {
				Integer count = userService.queryCountSysUserByUsername(username);
				//System.out.println("|||||||||||||||||||" + count);
				if (count == 0) {
					info = "用户不存在,请先注册!";
					return JsonUtil.toJson(info);
				}
			} else {
				Integer count = userService.queryCountSysUserByUsername(username);
				if (count != null && count.intValue() > 0) {
					info = "用户已注册，不可重复注册";
					return JsonUtil.toJson(info);
				}
			}
			String vc = DataUtil.generateRandomString(4);
			/*request.getSession().setAttribute("VC_" + username, vc);
			info = "ok";
			System.out.println("VC--------------------" + vc);*/
			if (Sendsms.send(vc, username)) {
				request.getSession().setAttribute("VC_" + username, vc);
				info = "ok";
			} else {
				info = "短信发送失败！"+Sendsms.getMsg();
			}
			response.setContentType("application/json;charset=utf-8");
		return JsonUtil.toJson(info);
	}
	
	@RequestMapping(value = "/mobile/changePass", method = RequestMethod.POST)
	public String changePassword(Model model, HttpServletRequest request, @RequestParam String username,@RequestParam String password,@RequestParam String valicode) {
		SysUsers user = userService.querySysUserByUsername(username, null);
		model.addAttribute("username", username);
		if(user == null) {
			model.addAttribute("msg", "用户名错误");
			return "pages/find_pwd";
		}
		Object vco = request.getSession().getAttribute("VC_" + username);
		if(vco == null || !valicode.toLowerCase().equals(((String)vco).toLowerCase())) {
			System.out.println("1111111111111111111----------"+valicode.toLowerCase());
			System.out.println("2222222222222222222----------"+((String)vco).toLowerCase());
			model.addAttribute("msg", "验证码错误");
			return "pages/find_pwd";
		} 
		PasswordEncoder pe = new BCryptPasswordEncoder();
		userService.updatePassword(pe.encode(password), user.getId());
		model.addAttribute("msg", "修改成功");
		request.getSession().removeAttribute("VC_" + username);
		return "pages/find_pwd";
	}
}
