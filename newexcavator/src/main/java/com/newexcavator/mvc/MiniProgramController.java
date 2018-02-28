package com.newexcavator.mvc;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.newexcavator.domain.Advertisement;
import com.newexcavator.domain.Brand;
import com.newexcavator.domain.City;
import com.newexcavator.domain.ExcavatorType;
import com.newexcavator.domain.Machine;
import com.newexcavator.domain.MachinePic;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.service.AdvertisementService;
import com.newexcavator.service.DeployInforService;
import com.newexcavator.service.MachineService;
import com.newexcavator.service.UserService;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.DateFormatter;
import com.newexcavator.util.PageSupport;
import com.newexcavator.util.SettingUtils;
import com.newexcavator.wechat.Sign;

@Controller
@RequestMapping(value = "/wx")
public class MiniProgramController {

	@Autowired
	private MachineService machineService;
	
	@Autowired
	private AdvertisementService advertisementService;
	
	@Autowired
	private UserService userService;
	
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
			@RequestParam(required=false) String exb_name) {
		PageSupport ps =PageSupport.initPageSupport(request);
		Map<String,Object> pa = new  HashMap<String,Object>();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", 1);
		if(!StringUtil.isBlank(search_name)){
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
}
