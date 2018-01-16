package com.newexcavator.mvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.newexcavator.domain.Advertisement;
import com.newexcavator.domain.Brand;
import com.newexcavator.domain.City;
import com.newexcavator.domain.ExcavatorType;
import com.newexcavator.domain.Machine;
import com.newexcavator.domain.Person;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.service.AdvertisementService;
import com.newexcavator.service.DeployInforService;
import com.newexcavator.service.MachineService;
import com.newexcavator.service.UserService;
import com.newexcavator.util.PageSupport;
import com.newexcavator.util.SettingUtils;
import com.newexcavator.wechat.Sign;

@Controller
public class PersonController {


	@Autowired
	private MachineService machineService;

	@Autowired
	private DeployInforService deployInforService;
	
	@Autowired
	private AdvertisementService advertisementService;
	
	@Autowired
	private UserService userService;
	
	
	private SysUsers validateSession(HttpServletRequest request) {
		Object su = request.getSession().getAttribute("sysUsers");
		return (SysUsers)su;
	}
	
	@RequestMapping(value = "/mobile/per_add", method = RequestMethod.GET)
	public String mobilebuy_add(Model model, HttpServletRequest request,@RequestParam (required = false) String openid) {
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
		
		return "pages/person/add";

	}
	
	@RequestMapping(value = "/mobile/per_save", method = RequestMethod.POST)
	public String save(Model model,HttpServletRequest request,
			@RequestParam Integer excavator_type,
			@RequestParam Integer province,
			@RequestParam Integer city,
			@RequestParam Float salary,
			@RequestParam String job,
			@RequestParam String description,
			@RequestParam String link_name,
			@RequestParam String phone) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		
		Person p = new Person();
		
		p.setUser_id(sus.getId());
		//m.setUser_id(1);
		
		p.setDescription(description);
		p.setLink_name(link_name);
		p.setM_type(excavator_type);
		p.setPhone(phone);
		p.setPlace_c(city);
		p.setPlace_p(province);
		p.setJob(job);
		p.setSalary(salary);
		
		machineService.savePerson(p);
		//machineService.saveBuyMachine(m);
		return "redirect:publicPerList";
	}
	
	@RequestMapping(value = "/mobile/per_list", method = {RequestMethod.GET,RequestMethod.POST})
	public String list(Model model,HttpServletRequest request,
			@RequestParam(required=false) String search_name,
			@RequestParam(required=false) Integer type,
			@RequestParam(required=false) Integer city_id,
			@RequestParam(required=false) String city_name) {
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
				
			}
			if(city_id!=null && city_id.intValue() > 0){
				param.put("city", city_id);
				model.addAttribute("city_id", city_id);
				model.addAttribute("city_name", city_name);
			}
			
			
			List<Person> ms = machineService.queryPerson(param,ps);
			List<City> cs = machineService.queryCityByPid(0);			
			model.addAttribute("cs", cs);
			model.addAttribute("ms", ms);
			return "pages/person/list";
	}
	
	@RequestMapping(value = "/mobile/per_detail", method = RequestMethod.GET)
	public String mobile_detail(Model model,HttpServletRequest request,@RequestParam Integer id,@RequestParam(required=false) String type,@RequestParam(required=false) Integer change) {
		Person m = machineService.queryPersonById(id);
		model.addAttribute("m", m);
		Advertisement adv = advertisementService.queryBottomAdvertisement(4);
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
		return "pages/person/detail";
	}
	
	@RequestMapping(value = "/mobile/change_per_close", method = RequestMethod.GET)
	public String change_buy_close(Model model,RedirectAttributes redirect,HttpServletRequest request,@RequestParam Integer id,@RequestParam Integer close) {

		machineService.updatePersonClose(id, close);
		redirect.addAttribute("change", 1);
		return "redirect:publicPerList";
	}
	
	@RequestMapping(value = "/mobile/publicPerList", method = {RequestMethod.GET,RequestMethod.POST})
	public String publicList(Model model,HttpServletRequest request,@RequestParam(required=false) Integer change) {
		SysUsers sus = validateSession(request);
		if (sus == null) {
			return "redirect:login";
		}
		PageSupport ps =PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("userId", sus.getId());
		//param.put("userId", 1);
		List<Person> ms = machineService.queryPerson(param,ps);
		model.addAttribute("ms", ms);
		if(change!=null && change.intValue() == 1){
			model.addAttribute("change", change);
		}
		return "pages/person/publicList";
	}
	
	/**
	 * 审核发布的出售信息
	 * @return
	 */
	@RequestMapping(value="/admin/machine/per_verify",method = {RequestMethod.GET,RequestMethod.POST})
	public String verify(Model model,HttpServletRequest request,@RequestParam(required=false) Integer delete,@RequestParam(required=false) Integer verify){
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("verify", 0);
		List<Person> ms = machineService.queryPerson(param, ps);
		model.addAttribute("ms", ms);
		if(delete!=null && delete.intValue() ==1 ){
			model.addAttribute("msg", "删除成功!");
		}
		if(verify!=null && verify.intValue() ==1 ){
			model.addAttribute("msg", "审核成功!");
		}
		return "pages/person/ad_verify";
	}
	
	
	/**
	 * 查看所有资料
	 * @return
	 */
	@RequestMapping(value="/admin/machine/per_list",method = {RequestMethod.GET,RequestMethod.POST})
	public String Machinelist(Model model,HttpServletRequest request,@RequestParam(required=false) Integer delete,
			@RequestParam(required=false) String searchName,
			@RequestParam(required=false) Integer type,
			@RequestParam(required=false) Integer city_id,
			@RequestParam(required=false) Integer close,
			@RequestParam(required=false) Integer verify){
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		//param.put("verify", 0);
		if(!StringUtils.isBlank(searchName)){
			param.put("search_name", searchName);
			model.addAttribute("searchName", searchName);
		}
		if(type!=null && type.intValue() > 0){
			param.put("type", type);
			model.addAttribute("type", type);
		
		}
		if(city_id!=null && city_id.intValue() > 0){
			param.put("city", city_id);
			model.addAttribute("city_id", city_id);
		}
		
		if(close!=null && close.intValue() > 0){
			param.put("close", close);
			model.addAttribute("close", close);
		}
		if(verify!=null && verify.intValue() >= 0){
			param.put("verify", verify);
			model.addAttribute("verify", verify);
		}
		List<Person> ms = machineService.queryPerson(param, ps);
		model.addAttribute("ms", ms);
		List<City> cs = machineService.queryCityByPid(0);			
		model.addAttribute("cs", cs);
		if(delete!=null && delete.intValue() ==1 ){
			model.addAttribute("msg", "删除成功!");
		}
		return "pages/person/ad_list";
	}
	
	
	
	@RequestMapping(value = "/admin/del_per_machine", method = RequestMethod.GET)
	public String del_mechine(Model model, @RequestParam Integer [] id, @RequestParam Integer type) {
		
		List<Integer> ids = Arrays.asList(id);
		machineService.delPerson(ids);
		if(type==0){
			return "redirect:/admin/machine/per_verify?delete=1";
		} else if(type==1){
			return "redirect:/admin/machine/per_list?delete=1";
		}
		return "";
	}
	
	@RequestMapping(value = "/admin/machine/per_detail", method = RequestMethod.GET)
	public String mobile_detail(Model model,@RequestParam Integer id,@RequestParam(required=false) Integer type,@RequestParam(required=false) Integer verify) {
		Person m = machineService.queryPersonById(id);
		model.addAttribute("m", m);
		model.addAttribute("type", type);
		if(verify!=null && verify.intValue() > 0){
			model.addAttribute("msg", "审核成功");
		}
		return "pages/person/ad_detail";
	}
	
	
	
	@RequestMapping(value="/admin/machine/per_verify_save",method = RequestMethod.GET)
	public String verify_sae(Model model,HttpServletRequest request,@RequestParam Integer id,@RequestParam Integer verify,@RequestParam(required = false) String type){
		machineService.updatePersonVerify(id,verify);
		if(verify==1){
			Person m =  machineService.queryPersonById(id);
			userService.addPoint(m.getUser_id(), 1);
		}
		if(type!=null && type.equals("list")){
			return "redirect:per_verify?verify=1";
		} else{
			return "redirect:per_detail?verify=1&id="+id;
		}
	}
}
