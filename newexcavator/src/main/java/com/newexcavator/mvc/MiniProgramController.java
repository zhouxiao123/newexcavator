package com.newexcavator.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import com.newexcavator.domain.CollectMachine;
import com.newexcavator.domain.ExcavatorType;
import com.newexcavator.domain.Machine;
import com.newexcavator.domain.MachinePic;
import com.newexcavator.domain.Point;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.service.AdvertisementService;
import com.newexcavator.service.CollectMachineService;
import com.newexcavator.service.DeployInforService;
import com.newexcavator.service.MachineService;
import com.newexcavator.service.UserService;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.DateFormatter;
import com.newexcavator.util.JsonUtil;
import com.newexcavator.util.PageSupport;
import com.newexcavator.util.SettingUtils;
import com.newexcavator.util.WechatUtils;
import com.newexcavator.util.sms.Sendsms;
import com.newexcavator.wechat.Sign;
import com.newexcavator.wechat.model.message.response.WebOpenidModel;

@Controller
@RequestMapping(value = "/wx")
public class MiniProgramController {

	@Autowired
	private MachineService machineService;
	
	@Autowired
	private AdvertisementService advertisementService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CollectMachineService collectMachineService;
	
	@RequestMapping(value = "/mobile", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> mobile(Model model,HttpServletRequest request) {
		List<Advertisement> advertisement=advertisementService.queryAdvertisementList();
		Map<String,Object> pa = new  HashMap<String,Object>();
		pa.put("advertisement", advertisement);	
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
		pa.put("ms1", ms1);
		pa.put("ms2", ms2);
		return pa;
	}
	
	@RequestMapping(value = "/mobile/detail", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> mobile_detail(Model model,HttpServletRequest request,@RequestParam Integer id,@RequestParam(required=false) String type,@RequestParam(required=false) Integer change) {
		Machine m = machineService.queryMachineById(id);
		Map<String,Object> pa = new  HashMap<String,Object>();
		pa.put("m", m);
		Advertisement adv = advertisementService.queryBottomAdvertisement(1);
		pa.put("adv", adv);
		if(!StringUtils.isBlank(type)){
			pa.put("type", type);
		}
		if(change!=null && change.intValue() == 1){
			pa.put("change", change);
		}
		
		/*Map<String,String> param = new HashMap<String,String>();
		param = Sign.getSign(SettingUtils.getCommonSetting("base.url")+request.getServletPath()+(StringUtils.isBlank(request.getQueryString())?"":"?"+request.getQueryString()));
		System.out.println(request.getServletPath()+"?"+request.getQueryString()+"--------------");
		model.addAttribute("sign", param);*/
		return pa;
	}
	
	@RequestMapping(value = "/mobile/list", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> list(Model model,HttpServletRequest request,
			@RequestParam(required=false) String search_name,
			@RequestParam(required=false) Integer type,
			@RequestParam(required=false) Integer city_id,
			@RequestParam(required=false) String city_name,
			@RequestParam(required=false) Integer exb_id,
			@RequestParam(required=false) String exb_name) throws UnsupportedEncodingException {
		PageSupport ps =PageSupport.initPageSupport(request);
		Map<String,Object> pa = new  HashMap<String,Object>();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", 1);
		request.setCharacterEncoding("UTF-8");  

		//search_name  = URLDecoder.decode(search_name, "UTF-8"); 
		if(!StringUtil.isBlank(search_name)){
			search_name = new String(search_name.getBytes("ISO8859-1"), "UTF-8"); 
			param.put("search_name", search_name);			
			pa.put("search_name", search_name);
		} 
			if(type!=null && type.intValue() > 0){
				param.put("type", type);
				pa.put("type", type);
				List<Brand> bs = machineService.queryBrandByExcavatorTypeId(type);
				pa.put("bs", bs);
			}
			if(city_id!=null && city_id.intValue() > 0){
				param.put("city", city_id);
				pa.put("city_id", city_id);
				pa.put("city_name", city_name);
			}
			if(exb_id!=null && exb_id.intValue() > 0){
				param.put("brand", exb_id);
				pa.put("exb_id", exb_id);
				pa.put("exb_name", exb_name);
			}
			
			List<Machine> ms = machineService.queryMachine(param,ps);
			List<City> cs = machineService.queryCityByPid(0);			
			pa.put("cs", cs);
			pa.put("ms", ms);
			return pa;
		
		
	}
	
	@RequestMapping(value = "/mobile/add", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> mobile_add(Model model, HttpServletRequest request,@RequestParam (required = false) String openid) {
		/*SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}*/
		//SysUsers sysuser = userService.querySysUserByOpenid(openid);
		//SysUsers sysuser = userService.querySysUserByOpenid("oCwisjrKR1NbpMetmZYpeYm5fGBk");
		/*if (sysuser == null) {
			return "redirect:login";
		}*/
		Map<String,Object> pa = new HashMap<String ,Object>();
		List<ExcavatorType> et = machineService.queryExcavatorType();
		List<City> cs = machineService.queryCityByPid(0);
		pa.put("et", et);
		pa.put("cs", cs);
		//model.addAttribute("su", sus);
		//String token = DataUtil.generateRandomString(8);
		//request.getSession().setAttribute("token", token);
		//model.addAttribute("token", token);
		return pa;

	}
	
		
	@RequestMapping(value = "/mobile/uploadFile", method = RequestMethod.POST)
	public void uploadApp(Model model, HttpServletRequest request,
			 PrintWriter pw) {
		//String sessionId = request.getSession().getId();

		String name = DataUtil.uploadFile2(request);
		System.out.println(name);
		pw.write(name);
		pw.flush();
	}
	
	
	/**
	 * 获取校验码
	 * 
	 * @param request
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/mobile/getvalidate", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getvalidate(HttpServletRequest request, Model model, @RequestParam String username, @RequestParam(required = false) String type, @RequestParam(required=false) Integer id) {
		Map<String,Object> pa = new HashMap<String ,Object>();
		String info = "ok";
			if (StringUtils.equals(type, "mod")) {
				Integer count = userService.queryCountSysUserByUsername(username);
				//System.out.println("|||||||||||||||||||" + count);
				if (count == 0) {
					info = "用户不存在,请先注册!";
					pa.put("info",info);
					return pa;
				}
			} else {
				Integer count = userService.queryCountSysUserByUsername(username);
				if (count != null && count.intValue() > 0) {
					pa.put("info",info);
					return pa;
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
			pa.put("info",info);
			return pa;
	}
	
	@RequestMapping(value = "/mobile/reg_save", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> mobile_reg_save(Model model,RedirectAttributes redirect,HttpServletRequest request,@RequestParam String phone,@RequestParam String name, @RequestParam String password,@RequestParam(required = false)String invited,@RequestParam(required = false) String valicode) throws UnsupportedEncodingException {
		//注册保存
		Map<String,Object> pa = new HashMap<String ,Object>();
		Integer invited_id=0;
		SysUsers sus = userService.querySysUserByUsername(phone, null);
		if(sus == null){
			if(!StringUtils.isBlank(invited)){
				SysUsers s = userService.querySysUserByUsername(invited, null);
				if(s==null){
					pa.put("phone", phone);
					pa.put("name", name);
					pa.put("info", "邀请人不存在!");
					return pa;
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
		pa.put("sessionId", request.getSession().getId());
		pa.put("info", "ok");
		return pa;
		} else {
			pa.put("phone", phone);
			pa.put("name", name);
			pa.put("info", "账号已存在!");
			return pa;
		}
	}
	
    @RequestMapping(value="/login",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> login(@RequestParam("code") String code) {
    	Map<String,Object> pa = new HashMap<String ,Object>();
        WebOpenidModel wom = WechatUtils.getOpenidInfor(code);
        if(StringUtils.isBlank(wom.getOpenid())){
        	pa.put("oid","");
            return pa;
        }
        pa.put("oid",wom.getOpenid());
        return pa;
    }
    
    @RequestMapping(value="/getUserDetail",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> userDetail(@RequestParam String oid) {
    	Map<String,Object> pa = new HashMap<String ,Object>();
        SysUsers u = userService.querySysUserByOpenid(oid);
        if(u == null) {
        	pa.put("info", "fail");
        	return pa;
        }
       pa.put("info", "ok");
       pa.put("user", u);
        return pa;
    }
    
    @RequestMapping(value="/updateUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateUser(@RequestParam String oid,@RequestParam String nickname,@RequestParam String head) {
    	Map<String,Object> pa = new HashMap<String ,Object>();
    	if(!StringUtils.isBlank(oid)&&!StringUtils.isBlank(head)) {
    		SysUsers u = new SysUsers();
        	u.setOpenid(oid);
        	u.setHead(head);
        	u.setNickname(nickname);
        	userService.updateNickNameAndHead(u);
    	}
       pa.put("info", "ok");
        return pa;
    }
    
    @RequestMapping(value = "/mobile/bind_oid", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> BindOid(Model model,RedirectAttributes redirect,HttpServletRequest request,@RequestParam String phone,@RequestParam String oid,@RequestParam String nickname,@RequestParam String head,@RequestParam String name, @RequestParam(required = false)String invited,@RequestParam(required = false) String valicode) throws UnsupportedEncodingException {
		//注册保存
		Map<String,Object> pa = new HashMap<String ,Object>();
		Integer invited_id=0;
		SysUsers sus = userService.querySysUserByOpenid(oid);
		if(sus == null){
			if(!StringUtils.isBlank(invited)){
				SysUsers s = userService.querySysUserByUsername(invited, null);
				if(s==null){
					pa.put("phone", phone);
					pa.put("name", name);
					pa.put("info", "邀请人不存在!");
					return pa;
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
		
		//PasswordEncoder pe = new BCryptPasswordEncoder();
		SysUsers su2 = userService.querySysUserByUsername(phone, null);	
		if(su2 != null && su2.getOpenid()==null) {
			SysUsers su = new SysUsers();
//			su.setInvited_id(invited_id);
			su.setUsername(phone);
			su.setOpenid(oid);
			su.setNickname(nickname);
			su.setHead(head);
			userService.updateOpenidAndNickNameAndHead(su);
		} else {
			SysUsers su = new SysUsers();
			su.setCell_phone(phone);
			su.setUsername(phone);
			su.setName(name);
			su.setInvited_id(invited_id);
			su.setOpenid(oid);
			su.setNickname(nickname);
			su.setHead(head);
			//su.setPassword(pe.encode(password));
			userService.saveUser(su);
			//userService.updatePassword(pe.encode(password), su.getId());
		}
		
		//request.getSession().removeAttribute("VC_" + phone);
		
		//加入session
		//userService.updateLoginInfor(su.getId());
		//su.setLast_login_time(new Date());
		//userService.addPoint(su.getId(), 1);
		//request.getSession().setAttribute("sysUsers", su);
		//pa.put("sessionId", request.getSession().getId());
			pa.put("info", "ok");
			return pa;
		} else {
			pa.put("phone", phone);
			pa.put("name", name);
			pa.put("info", "账号已存在!");
			return pa;
		}
	}
        
	@RequestMapping(value = "/mobile/login_save", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> mobile_login_save(Model model,HttpServletRequest request,@RequestParam String phone, @RequestParam String password) {
		//判断登陆
		Map<String,Object> pa = new HashMap<String ,Object>();
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
			pa.put("sessionId", request.getSession().getId());
			pa.put("info","ok");
			return pa;
		}
		}
		pa.put("info","账号密码错误!");
			return pa;
		
	}
	
	 /**
     * 是否已经收藏
     * @return
     */
    @RequestMapping(value="/is_collect_machine", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map isCollectMachine(@RequestParam Integer mid,@RequestParam String oid){
        Map param = new HashMap();
        SysUsers u = userService.querySysUserByOpenid(oid);
        if(u==null){
            param.put("result","fail");
            return param;
        }
        CollectMachine cm = collectMachineService.findByUseridAndMid(u.getId(), mid);
        if(cm!=null){
            param.put("result","ok");
            return param;
        }
        param.put("result","fail");
        return param;
    }


    /**
     * 收藏专家
     * @return
     */
    @RequestMapping(value="/collect_machine", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map collectMachine(@RequestParam Integer mid,@RequestParam String oid){
        Map param = new HashMap();
        SysUsers u = userService.querySysUserByOpenid(oid);
        if(u==null){
            param.put("result","not");
            return param;
        }
        CollectMachine cm = collectMachineService.findByUseridAndMid(u.getId(), mid);
        if(cm!=null){
            param.put("result","fail");
            return param;
        }
        cm = new CollectMachine();
        cm.setCreatetime(new Date());
        cm.setM_id(mid);
        cm.setUserid(u.getId());
        collectMachineService.saveCollectMachine(cm);
        param.put("result","ok");
        return param;
    }

    /**
     * 取消收藏专家
     * @return
     */
    @RequestMapping(value="/cancel_collect_machine", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map cancelCollectMachine(@RequestParam Integer mid,@RequestParam String oid){
        Map param = new HashMap();
        SysUsers u = userService.querySysUserByOpenid(oid);
        if(u==null){
            param.put("result","not");
            return param;
        }
        CollectMachine cm = collectMachineService.findByUseridAndMid(u.getId(), mid);
        if(cm==null){
            param.put("result","fail");
            return param;
        }

        collectMachineService.deleteCollectMachine(cm);
        param.put("result","ok");
        return param;
    }
    
    /**
     * 收藏列表
     * @return
     */
    @RequestMapping(value="/collect_list", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map collectionLecturerList(HttpServletRequest request,@RequestParam(required = false) String search_name,@RequestParam String oid){
        Map param = new HashMap();
        String f_yxname = "%%";
        if(!StringUtils.isBlank(search_name)){
            f_yxname = "%"+search_name+"%";

        }
        SysUsers u = userService.querySysUserByOpenid(oid);
        if(u==null){
            param.put("result","fail");
            return param;
        }
        PageSupport pageSupport = PageSupport.initPageSupport(request);
        List<CollectMachine> cm = collectMachineService.queryCollectMachineByUserid(u.getId(), pageSupport);
        param.put("result","ok");
        param.put("collectMachine",cm);
        
        return param;
    }
	
	
	@RequestMapping(value = "/mobile/save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> save(Model model,HttpServletRequest request,
			@RequestParam Integer excavator_type,
			@RequestParam Integer big_type,
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
			@RequestParam String oid,
			@RequestParam String[] filename,
			@RequestParam Integer first_num) {
		Map<String,Object> pa = new HashMap<String ,Object>();
		SysUsers sus = userService.querySysUserByOpenid(oid);
		if (sus == null) {
			pa.put("info", "not");
			return pa;
		}
		
		
		Machine m = new Machine();
		
		m.setUser_id(sus.getId());
		//m.setUser_id(1);
		m.setBig_type(big_type);
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
		pa.put("info","ok");
		return pa;
	}
}
