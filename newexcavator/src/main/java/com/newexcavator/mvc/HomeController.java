package com.newexcavator.mvc;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newexcavator.domain.Advertisement;
import com.newexcavator.domain.Brand;
import com.newexcavator.domain.City;
import com.newexcavator.domain.Commodity;
import com.newexcavator.domain.Machine;
import com.newexcavator.domain.Order;
import com.newexcavator.domain.Point;
import com.newexcavator.domain.SendTime;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.domain.User;
import com.newexcavator.domain.Wechat_tb;
import com.newexcavator.service.AdvertisementService;
import com.newexcavator.service.MachineService;
import com.newexcavator.service.UserService;
import com.newexcavator.service.Wechat_tbService;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.JsonUtil;
import com.newexcavator.util.PageSupport;
import com.newexcavator.util.SettingUtils;
import com.newexcavator.wechat.model.message.server.Article;
import com.newexcavator.wechat.model.message.server.DeployMessage;
import com.newexcavator.wechat.model.message.server.NewsMessage;

@Controller
public class HomeController {
	@Autowired
	private MachineService machineService;

	@Autowired
	private Wechat_tbService wechat_tbService;

	@Autowired
	private UserService userService;

	@Autowired
	private AdvertisementService advertisementService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model,
			@RequestParam(required = false) String error) {

		model.addAttribute("error", error);
		return "pages/login";
	}
	
	@RequestMapping(value = "/mobile/find_pwd", method = RequestMethod.GET)
	public String findPassword(Model model, HttpServletRequest request) {
		return "pages/find_pwd";
	}

	@RequestMapping(value = { "/", "/index" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String home(Model model) {
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		model.addAttribute("currentUser",
				new User(ud.getUsername(), ud.getUsername(), ud.getUsername(),
						""));
		return "pages/main";
	}

	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public String header(Model model,
			@RequestParam(required = false) String error) {
		return "pages/top";
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome(Model model,
			@RequestParam(required = false) String error) {
		return "pages/index";
	}

	@RequestMapping(value = "/left", method = RequestMethod.GET)
	public String menuList(Model model,
			@RequestParam(required = false) String error) {
		return "pages/left";
	}

	@RequestMapping(value = "/mod_pwd", method = RequestMethod.GET)
	public String mod_pwd(Model model) {

		return "pages/mod_pwd";
	}

	@RequestMapping(value = "/admin/wechat_add", method = RequestMethod.GET)
	public String addWechat_tb(Model model,
			@RequestParam(required = false) String type) {
		Wechat_tb wc = wechat_tbService.queryWechatNo();
		model.addAttribute("type", type);
		model.addAttribute("wc", wc);
		return "pages/wechat_add";
	}

	@RequestMapping(value = "/admin/saveWechat_tb", method = { RequestMethod.POST })
	public String saveWechat_tb(HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam String wechatNo,
			@RequestParam(required = false) String type) {
		System.out.println("------->>" + wechatNo + "----" + type);
		Wechat_tb wc;
		if (type.equals("update")) {
			wechat_tbService.updateWechatNo(wechatNo, id); // userService.queryCustomerById(id);
			wc = wechat_tbService.queryWechatNo();
			model.addAttribute("msg", "修改成功！");
			model.addAttribute("wc", wc);
		} else {
			wc = wechat_tbService.queryWechatNo();
			if (wc == null) {
				wc = new Wechat_tb();
				wc.setWechatNo(wechatNo);
				wechat_tbService.saveWechatNo(wc);
				model.addAttribute("msg", "添加成功！");
			} else {
				model.addAttribute("msg", "已经存在微信号！");
			}
		}
		model.addAttribute("type", type);
		return "pages/wechat_add";
	}

	@RequestMapping(value = "/admin/modPassword", method = { RequestMethod.POST })
	public String savePassword(HttpServletRequest request, Model model,
			@RequestParam String historyPwd, @RequestParam String newPwd) {
		SysUsers ud = (SysUsers) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		SysUsers u = userService.querySysUserByUsername(ud.getUsername(), null);
		
		PasswordEncoder pe = new BCryptPasswordEncoder();
		if (pe.matches(historyPwd, u.getPassword())) {
			userService.updatePassword(pe.encode(newPwd),u.getId());
			model.addAttribute("state", "修改成功！");
		} else {
			model.addAttribute("state", "修改失败！原始密码不正确！");
		}
		
		/*if (StringUtils.equals(u.getPassword(), historyPwd)) {
			userService.updatePassword(newPwd, u.getId());
			model.addAttribute("state", "修改成功！");
		} else {
			model.addAttribute("state", "修改失败！原始密码不正确！");
		}*/
		return "pages/mod_pwd";
	}

	@RequestMapping(value = "/admin/index/adv_setting", method = RequestMethod.GET)
	public String indexAdvSetting(Model model,@RequestParam(required = false) Integer error) {
		List<Advertisement> advertisements = advertisementService
				.queryAdvertisementList();

		model.addAttribute("advertisements", advertisements);
		if(error!=null){
			if(error.intValue() == 0){
				model.addAttribute("msg", "保存成功!");
			} else if(error.intValue() == 1){
				model.addAttribute("msg", "保存失败!");
			}
		}
		return "pages/adv/index_setting";
	}

	@RequestMapping(value = "/admin/adv_setting/save", method = RequestMethod.POST)
	public String save(Model model, HttpServletRequest request,
			@RequestParam String[] img_url,
			@RequestParam(required = false) Integer id) {
		int error = 0;
		try {
			// String base_img_url =
			// SettingUtils.getCommonSetting("base.img.url");
			List<String> img_paths = DataUtil.upload2Img(request, "file",true);
			int i = 0;
			for (String img_path : img_paths) {
				Advertisement adv = new Advertisement();
				adv.setImg_path(img_path);
				try {
					adv.setImg_url(img_url[i]);
				} catch (Exception e) {
					System.out.println("未设置广告跳转地址!");
				}
				i++;

				advertisementService.saveAdvertisement(adv);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = 1;
		}
		return "redirect:/admin/index/adv_setting?error=" + error + "&img_url="
				+ img_url;
	}

	@RequestMapping(value = "/admin/adv_setting/del", method = RequestMethod.GET)
	public @ResponseBody
	String delAdvSetting(Model model, @RequestParam Integer id) {
		advertisementService.delAdvertisementByid(id);
		return "ok";
	}
	
	//发布挖掘机底部广告
	@RequestMapping(value = "/admin/index/b_adv_setting", method = RequestMethod.GET)
	public String b_indexAdvSetting(Model model,@RequestParam(required = false) Integer error) {
		Advertisement advertisements = advertisementService.queryBottomAdvertisement(1);

		model.addAttribute("advertisements", advertisements);
		if(error!=null){
			if(error.intValue() == 0){
				model.addAttribute("msg", "保存成功!");
			} else if(error.intValue() == 1){
				model.addAttribute("msg", "保存失败!");
			}
		}
		return "pages/adv/b_index_setting";
	}

	@RequestMapping(value = "/admin/b_adv_setting/save", method = RequestMethod.POST)
	public String b_save(Model model, HttpServletRequest request,
			@RequestParam String img_url,
			@RequestParam String description,
			@RequestParam(required = false) Integer id) {
		int error = 0;
		try {
			// String base_img_url =
			// SettingUtils.getCommonSetting("base.img.url");
			Advertisement adv = new Advertisement();
			List<String> img_paths = DataUtil.upload2Img(request, "file",true);
			if(!CollectionUtils.isEmpty(img_paths)){
				adv.setImg_path(img_paths.get(0));
			}
			adv.setImg_url(img_url);
			adv.setDescription(description);
			adv.setId(id);
			adv.setFlag(1);
			advertisementService.saveBottomAdvertisement(adv);
		} catch (Exception e) {
			e.printStackTrace();
			error = 1;
		}
		return "redirect:/admin/index/b_adv_setting?error=" + error + "&img_url="
				+ img_url;
	}

	/**
	 * 求购挖掘机底部广告
	 * @param model
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/admin/index/buy_b_adv_setting", method = RequestMethod.GET)
	public String buy_b_indexAdvSetting(Model model,@RequestParam(required = false) Integer error) {
		Advertisement advertisements = advertisementService.queryBottomAdvertisement(2);

		model.addAttribute("advertisements", advertisements);
		if(error!=null){
			if(error.intValue() == 0){
				model.addAttribute("msg", "保存成功!");
			} else if(error.intValue() == 1){
				model.addAttribute("msg", "保存失败!");
			}
		}
		return "pages/adv/buy_b_index_setting";
	}

	@RequestMapping(value = "/admin/buy_b_adv_setting/save", method = RequestMethod.POST)
	public String buy_b_save(Model model, HttpServletRequest request,
			@RequestParam String img_url,
			@RequestParam String description,
			@RequestParam(required = false) Integer id) {
		int error = 0;
		try {
			// String base_img_url =
			// SettingUtils.getCommonSetting("base.img.url");
			Advertisement adv = new Advertisement();
			List<String> img_paths = DataUtil.upload2Img(request, "file",true);
			if(!CollectionUtils.isEmpty(img_paths)){
				adv.setImg_path(img_paths.get(0));
			}
			adv.setImg_url(img_url);
			adv.setDescription(description);
			adv.setId(id);
			adv.setFlag(2);
			advertisementService.saveBottomAdvertisement(adv);
		} catch (Exception e) {
			e.printStackTrace();
			error = 1;
		}
		return "redirect:/admin/index/buy_b_adv_setting?error=" + error + "&img_url="
				+ img_url;
	}
	
	/**出租底部广告
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/admin/index/rent_b_adv_setting", method = RequestMethod.GET)
	public String rent_b_indexAdvSetting(Model model,@RequestParam(required = false) Integer error) {
		Advertisement advertisements = advertisementService.queryBottomAdvertisement(3);

		model.addAttribute("advertisements", advertisements);
		if(error!=null){
			if(error.intValue() == 0){
				model.addAttribute("msg", "保存成功!");
			} else if(error.intValue() == 1){
				model.addAttribute("msg", "保存失败!");
			}
		}
		return "pages/adv/rent_b_index_setting";
	}

	@RequestMapping(value = "/admin/rent_b_adv_setting/save", method = RequestMethod.POST)
	public String rent_b_save(Model model, HttpServletRequest request,
			@RequestParam String img_url,
			@RequestParam String description,
			@RequestParam(required = false) Integer id) {
		int error = 0;
		try {
			// String base_img_url =
			// SettingUtils.getCommonSetting("base.img.url");
			Advertisement adv = new Advertisement();
			List<String> img_paths = DataUtil.upload2Img(request, "file",true);
			if(!CollectionUtils.isEmpty(img_paths)){
				adv.setImg_path(img_paths.get(0));
			}
			adv.setImg_url(img_url);
			adv.setDescription(description);
			adv.setId(id);
			adv.setFlag(3);
			advertisementService.saveBottomAdvertisement(adv);
		} catch (Exception e) {
			e.printStackTrace();
			error = 1;
		}
		return "redirect:/admin/index/rent_b_adv_setting?error=" + error + "&img_url="
				+ img_url;
	}
	
	/**
	 * 人才招聘底部广告
	 * @param model
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/admin/index/per_b_adv_setting", method = RequestMethod.GET)
	public String per_b_indexAdvSetting(Model model,@RequestParam(required = false) Integer error) {
		Advertisement advertisements = advertisementService.queryBottomAdvertisement(4);

		model.addAttribute("advertisements", advertisements);
		if(error!=null){
			if(error.intValue() == 0){
				model.addAttribute("msg", "保存成功!");
			} else if(error.intValue() == 1){
				model.addAttribute("msg", "保存失败!");
			}
		}
		return "pages/adv/per_b_index_setting";
	}

	@RequestMapping(value = "/admin/per_b_adv_setting/save", method = RequestMethod.POST)
	public String per_b_save(Model model, HttpServletRequest request,
			@RequestParam String img_url,
			@RequestParam String description,
			@RequestParam(required = false) Integer id) {
		int error = 0;
		try {
			// String base_img_url =
			// SettingUtils.getCommonSetting("base.img.url");
			Advertisement adv = new Advertisement();
			List<String> img_paths = DataUtil.upload2Img(request, "file",true);
			if(!CollectionUtils.isEmpty(img_paths)){
				adv.setImg_path(img_paths.get(0));
			}
			adv.setImg_url(img_url);
			adv.setDescription(description);
			adv.setId(id);
			adv.setFlag(4);
			advertisementService.saveBottomAdvertisement(adv);
		} catch (Exception e) {
			e.printStackTrace();
			error = 1;
		}
		return "redirect:/admin/index/per_b_adv_setting?error=" + error + "&img_url="
				+ img_url;
	}

	
	

	/**
	 * 文件上传，用于处理大文件上传
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @param chunk
	 * @param chunks
	 * @return
	 */
	@RequestMapping(value = "/admin/uploadFile", method = RequestMethod.POST)
	public void uploadApp(Model model, HttpServletRequest request,
			@RequestParam String name, @RequestParam Integer chunk,
			@RequestParam Integer chunks, PrintWriter pw) {
		String sessionId = request.getSession().getId();
		name = sessionId + "_" + name;
		DataUtil.uploadFile(request, name);

		pw.write(name);
		pw.flush();
	}
	/**
	 * 文件删除
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @param chunk
	 * @param chunks
	 * @return
	 */
	@RequestMapping(value = "/admin/uploadFile_del", method = RequestMethod.POST)
	public void uploadApp_del(Model model, HttpServletRequest request,
			@RequestParam String name, PrintWriter pw) {
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
		DataUtil.deleteFile(fileDir+sep+name);
		pw.write(JsonUtil.toJson("ok"));
		pw.flush();
	}
	
	
	
	/**
	 * 已上传文件删除
	 * 
	 * @param model
	 * @param request
	 * @param name
	 * @param chunk
	 * @param chunks
	 * @return
	 */
	@RequestMapping(value = "/admin/mobileFile_del", method = RequestMethod.POST)
	public void mobile_del(Model model, HttpServletRequest request,
			@RequestParam Integer id, PrintWriter pw) {
		machineService.delMachinePic(id);
		pw.write(JsonUtil.toJson("ok"));
		pw.flush();
	}
	
	
	@RequestMapping(value = "/admin/mobileFile_rent_del", method = RequestMethod.POST)
	public void mobile_rent_del(Model model, HttpServletRequest request,
			@RequestParam Integer id, PrintWriter pw) {
		machineService.delRentMachinePic(id);
		pw.write(JsonUtil.toJson("ok"));
		pw.flush();
	}
	
	
	/**
	 * 审核发布的出售信息
	 * @return
	 */
	@RequestMapping(value="/admin/machine/verify",method = {RequestMethod.GET,RequestMethod.POST})
	public String verify(Model model,HttpServletRequest request,@RequestParam(required=false) Integer delete,@RequestParam(required=false) Integer verify){
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("verify", 0);
		List<Machine> ms = machineService.queryMachine(param, ps);
		model.addAttribute("ms", ms);
		if(delete!=null && delete.intValue() ==1 ){
			model.addAttribute("msg", "删除成功!");
		}
		if(verify!=null && verify.intValue() ==1 ){
			model.addAttribute("msg", "审核成功!");
		}
		return "pages/machine/verify";
	}
	
	
	/**
	 * 查看所有资料
	 * @return
	 */
	@RequestMapping(value="/admin/machine/list",method = {RequestMethod.GET,RequestMethod.POST})
	public String Machinelist(Model model,HttpServletRequest request,@RequestParam(required=false) Integer delete,
			@RequestParam(required=false) String searchName,
			@RequestParam(required=false) Integer type,
			@RequestParam(required=false) Integer city_id,
			@RequestParam(required=false) Integer exb_id,
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
			List<Brand> bs = machineService.queryBrandByExcavatorTypeId(type);
			model.addAttribute("bs", bs);
		}
		if(city_id!=null && city_id.intValue() > 0){
			param.put("city", city_id);
			model.addAttribute("city_id", city_id);
		}
		if(exb_id!=null && exb_id.intValue() > 0){
			param.put("brand", exb_id);
			model.addAttribute("exb_id", exb_id);
		}
		if(close!=null && close.intValue() > 0){
			param.put("close", close);
			model.addAttribute("close", close);
		}
		if(verify!=null && verify.intValue() >= 0){
			param.put("verify", verify);
			model.addAttribute("verify", verify);
		}
		List<Machine> ms = machineService.queryMachine(param, ps);
		model.addAttribute("ms", ms);
		List<City> cs = machineService.queryCityByPid(0);			
		model.addAttribute("cs", cs);
		if(delete!=null && delete.intValue() ==1 ){
			model.addAttribute("msg", "删除成功!");
		}
		return "pages/machine/list";
	}
	
	/**
	 * 所有发布的出售信息
	 * @return
	 *//*
	@RequestMapping(value="/admin/machine/list",method = RequestMethod.GET)
	public String machineList(Model model,HttpServletRequest request,@RequestParam(required=false) Integer delete){
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		//param.put("verify", 0);
		List<Machine> ms = machineService.queryMachine(param, ps);
		model.addAttribute("ms", ms);
		if(delete!=null && delete.intValue() ==1 ){
			model.addAttribute("msg", "删除成功!");
		}
		return "pages/machine/list";
	}*/
	
	@RequestMapping(value = "/admin/del_machine", method = RequestMethod.GET)
	public String del_mechine(Model model, @RequestParam Integer [] id, @RequestParam Integer type) {
		
		List<Integer> ids = Arrays.asList(id);
		machineService.delMachineInfor(ids);
		if(type==0){
			return "redirect:/admin/machine/verify?delete=1";
		} else if(type==1){
			return "redirect:/admin/machine/list?delete=1";
		} else if(type==2){
			return "redirect:/admin/machine/send_list?delete=1";
		}
		return "";
	}
	
	@RequestMapping(value = "/admin/machine/detail", method = RequestMethod.GET)
	public String mobile_detail(Model model,@RequestParam Integer id,@RequestParam(required=false) Integer type,@RequestParam(required=false) Integer verify) {
		Machine m = machineService.queryMachineById(id);
		model.addAttribute("m", m);
		model.addAttribute("type", type);
		if(verify!=null && verify.intValue() > 0){
			model.addAttribute("msg", "审核成功");
		}
		return "pages/machine/detail";
	}
	
	@RequestMapping(value = "/admin/machine/edit", method = RequestMethod.GET)
	public String mobile_edit(Model model,@RequestParam Integer id,@RequestParam(required=false) Integer type,@RequestParam(required=false) Integer verify) {
		Machine m = machineService.queryMachineById(id);
		model.addAttribute("m", m);
		model.addAttribute("type", type);
		if(verify!=null && verify.intValue() > 0){
			model.addAttribute("msg", "修改成功");
		}
		return "pages/machine/edit";
	}
	
	@RequestMapping(value="/admin/machine/verify_save",method = RequestMethod.GET)
	public String verify_sae(Model model,HttpServletRequest request,@RequestParam Integer id,@RequestParam Integer verify,@RequestParam(required = false) String type){
		machineService.updateMachineVerify(id,verify);
		if(verify==1){
			Machine m =  machineService.queryMachineById(id);
			userService.addPoint(m.getUser_id(), 1);
		}
		if(type!=null && type.equals("list")){
			return "redirect:verify?verify=1";
		} else{
			return "redirect:detail?verify=1&id="+id;
		}
	}
	
	
	@RequestMapping(value="/admin/commodity/add",method = RequestMethod.GET)
	public String commodity_add(Model model,HttpServletRequest request){
		return "pages/commodity/add";
	}
	
	@RequestMapping(value="/admin/commodity/edit",method = RequestMethod.GET)
	public String commodity_add(Model model,HttpServletRequest request,@RequestParam Integer id,@RequestParam(required = false) Integer save){
		Commodity co = machineService.queryCommodityById(id);
		model.addAttribute("co", co);
		if(save!=null && save.intValue() > 0){
			model.addAttribute("msg", "保存成功");
		}
		return "pages/commodity/edit";
	}
	
	
	@RequestMapping(value="/admin/commodity/save",method = RequestMethod.POST)
	public String commodity_save(Model model,HttpServletRequest request,
			@RequestParam String name,
			@RequestParam String description,
			@RequestParam(required = false) Integer id,
			@RequestParam Integer point){
		Commodity co = new Commodity();
		co.setDescription(description);
		co.setId(id);
		co.setName(name);
		co.setPoint(point);
		try {
			List<String> img_paths = DataUtil.upload2Img(request, "file",true);
			if(!CollectionUtils.isEmpty(img_paths)){
				co.setPath(img_paths.get(0));
			}
			machineService.saveCommodity(co);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return "redirect:edit?save=1&id="+co.getId();
	}
	
	@RequestMapping(value="/admin/commodity/list",method = {RequestMethod.GET,RequestMethod.POST})
	public String commodity_list(Model model,HttpServletRequest request,@RequestParam(required = false) Integer delete){
		Map<String ,Object> param = new HashMap<String,Object>();
		PageSupport ps = PageSupport.initPageSupport(request);
		List<Commodity> cos = machineService.queryCommodity(param,ps);
		model.addAttribute("cos", cos);
		if(delete != null && delete.intValue() > 0){
			model.addAttribute("msg", "删除成功");
		}
		return "pages/commodity/list";
	}
	
	@RequestMapping(value="/admin/commodity/change_list",method = {RequestMethod.GET,RequestMethod.POST})
	public String change_list(Model model,HttpServletRequest request,@RequestParam(required = false) Integer status,@RequestParam(required = false) Integer change){
		Map<String ,Object> param = new HashMap<String,Object>();
		PageSupport ps = PageSupport.initPageSupport(request);
		if(status!=null && status.intValue() >= 0){
			param.put("status", status);
			model.addAttribute("status", status);
		}
		List<Order> os = machineService.queryOrder(param,ps);
		model.addAttribute("os", os);
		if(change!=null && change.intValue()  == 1){
			model.addAttribute("msg", "修改成功");
		}
		return "pages/commodity/change_list";
	}
	
	@RequestMapping(value="/admin/commodity/change_update",method = {RequestMethod.GET,RequestMethod.POST})
	public String change_update(Model model,HttpServletRequest request,@RequestParam(required = false) Integer id){
		machineService.updateOrderStatus(id,1);
		return "redirect:change_list?change=1";
	}
	
	
	@RequestMapping(value="/admin/commodity/add_point_list",method = {RequestMethod.GET,RequestMethod.POST})
	public String add_point_list(Model model,HttpServletRequest request,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) String searchIDcard,
			@RequestParam(required = false) String searchPhone){
		PageSupport pageSupport = PageSupport.initPageSupport(request);
		if (request.getMethod().equals("GET")) {// GET方式提交需要处理参数为中文的字符集问题
			try {
				if (!StringUtils.isBlank(searchName)) {
					searchName = new String(searchName.getBytes("iso8859-1"),
							"UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isBlank(searchName))
			param.put("name", searchName.trim());
		/*if (!StringUtils.isBlank(searchIDcard))
			param.put("IDCard", searchIDcard.trim());*/
		if (!StringUtils.isBlank(searchPhone))
			param.put("cell_phone", searchPhone.trim());
		List<SysUsers> users = userService.querySysUsers(param, pageSupport);


		model.addAttribute("users", users);
		model.addAttribute("searchName", searchName);
		//model.addAttribute("searchIDcard", searchIDcard);
		model.addAttribute("searchPhone", searchPhone);
		return "pages/commodity/add_point_list";
	}
	
	
	@RequestMapping(value = "/admin/commodity/update_user_point", method = RequestMethod.GET)
	public String update_user_password(Model model,
			@RequestParam Integer id,
			@RequestParam(required = false) Integer save) {
			SysUsers user = userService.querySysUserByid(id);
			model.addAttribute("user", user);

			if (save != null)
				model.addAttribute("msg", "操作成功！");
			return "pages/commodity/update_user_point";
	}
	
	@RequestMapping(value = "/admin/commodity/update_user_point_save", method = RequestMethod.POST)
	public String update_user_password_save(Model model,
			@RequestParam Integer id,
			@RequestParam Integer flag,
			@RequestParam Integer point) {
		if(flag.intValue()==2){
			userService.subPoint(id, point);
		} else {
			userService.addPoint(id, point);
		}
		
		return "redirect:update_user_point?save=1&id="+id;
	}
	
	@RequestMapping(value = "/admin/commodity/invited_point_set", method = RequestMethod.GET)
	public String invited_point_set(Model model,@RequestParam(required = false) Integer save) {
		Point p = userService.queryPoint();
		model.addAttribute("point", p);
		if(save!=null && save.intValue()==1){
			model.addAttribute("msg", "修改成功!");
		}
		return "pages/commodity/invited_point_set";
	}
	
	@RequestMapping(value = "/admin/commodity/invited_point_set_save", method = RequestMethod.POST)
	public String invited_point_set_save(Model model,
			
			@RequestParam Integer point) {
		userService.updatePoint(point);
		
		return "redirect:invited_point_set?save=1";
	}
	
	
	@RequestMapping(value = "/admin/del_commodity", method = RequestMethod.GET)
	public String del_commodity(Model model, @RequestParam Integer [] id, @RequestParam Integer type) {
		
		List<Integer> ids = Arrays.asList(id);
		machineService.delCommodity(ids);
		return "redirect:/admin/commodity/list?delete=1";
	}
	
	
	
	@RequestMapping(value="/admin/machine/send_list",method = {RequestMethod.GET,RequestMethod.POST})
	public String send_list(Model model,HttpServletRequest request,@RequestParam(required=false) Integer delete,@RequestParam(required=false) Integer send,@RequestParam(required=false) Integer is_send,
			@RequestParam(required=false) String searchName,
			@RequestParam(required=false) Integer type,
			@RequestParam(required=false) Integer city_id,
			@RequestParam(required=false) Integer exb_id,
			@RequestParam(required=false) Integer close,
			@RequestParam(required=false) Integer verify){
		
		SendTime st = machineService.querySendTime();
		if(st!=null){
			model.addAttribute("remark", "每次发布上限为10条,每天只能发布一条,今日已经发布");
		} else {
			model.addAttribute("remark", "每次发布上限为10条,每天只能发布一条");
		}
		PageSupport ps = PageSupport.initPageSupport(request);
		Map<String,Object> param = new HashMap<String,Object>();
		//param.put("verify", 0);
		/*if(!StringUtils.isBlank(searchName)){
			param.put("search_name", searchName);
			model.addAttribute("searchName", searchName);
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
		}
		if(exb_id!=null && exb_id.intValue() > 0){
			param.put("brand", exb_id);
			model.addAttribute("exb_id", exb_id);
		}
		if(close!=null && close.intValue() > 0){
			param.put("close", close);
			model.addAttribute("close", close);
		}
		if(verify!=null && verify.intValue() > 0){
			param.put("verify", verify);
			model.addAttribute("verify", verify);
		}*/
		param.put("is_send", 0);
		param.put("verify", 1);
		param.put("close", 0);
		List<Machine> ms = machineService.queryMachine(param, ps);
		model.addAttribute("ms", ms);
		/*List<City> cs = machineService.queryCityByPid(0);			
		model.addAttribute("cs", cs);*/
		if(delete!=null && delete.intValue() ==1 ){
			model.addAttribute("msg", "删除成功!");
		}
		if(send!=null && send.intValue() ==1 ){
			model.addAttribute("msg", "推送成功!");
		}
		if(is_send!=null && is_send.intValue() ==1 ){
			model.addAttribute("msg", "当日已经推送过!");
		}
		
		return "pages/machine/send_list";
	}
	
	
	@RequestMapping(value="/admin/machine/send_message",method = RequestMethod.GET)
	public String send_message(Model model,HttpServletRequest request,@RequestParam Integer[] ids){
		SendTime st = machineService.querySendTime();
		if(st!=null){
			return "redirect:/admin/machine/send_list?is_send=1";
		}
		
		List<Machine> ms = machineService.queryMachineByIds(ids);
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload2.path");
		// 上传图文消息到微信服务器
		NewsMessage nm = new NewsMessage();
		List<Article> alist = new ArrayList<Article>();
		nm.setArticles(alist);
		Integer i=0;
		for (Machine m:ms) {
			File f = new File(fileDir + sep + m.getCover_path().split("\\.")[0]
					+ "_S." + m.getCover_path().split("\\.")[1]);
			String media_thumb_id = DataUtil.uploadFile(f, null, 1);
			Article a = new Article();
			a.setAuthor("管理员");
			a.setContent(m.getP_name()+"-"+m.getC_name()+"二手"+m.getType_name()+" "+m.getBrand_name()+"_"+m.getVersion_name()+"售价"+m.getPrice()+"万，获取更多信息请点击阅读原文");
			a.setDigest(m.getP_name()+"-"+m.getC_name()+"二手"+m.getType_name()+" "+m.getBrand_name()+"_"+m.getVersion_name()+"售价"+m.getPrice()+"万，点击查看详情");
			if (!StringUtils.isEmpty(media_thumb_id))
				a.setThumb_media_id(media_thumb_id);
			a.setTitle(m.getP_name()+"-"+m.getC_name()+"二手"+m.getType_name()+" "+m.getBrand_name()+"_"+m.getVersion_name()+"售价"+m.getPrice()+"万");

			String baseUrl = SettingUtils.getCommonSetting("base.url.nohttp");
			a.setContent_source_url(baseUrl + "/mobile/detail?id=" + ids[i]);
			i++;
			alist.add(a);
		}
		String media_id = DataUtil.uploadNews(nm, null, 1);

		//deployInforService.updateMediaIdDeployInfor(di);
			// 获取关注列表
			//List<String> as = DataUtil.getAttentionList(null, 1);
		
			// 开始发布
			DeployMessage dm = new DeployMessage();
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("is_to_all", true);
			//filter.put("group_id", 100);
			dm.setMsgtype("mpnews");
			Map<String, Object> mpnews = new HashMap<String, Object>();
			mpnews.put("media_id", media_id);
			
			//测试代码
			//dm.setTouser("oCwisjrKR1NbpMetmZYpeYm5fGBk");
			//正式发布时送到全体
			mpnews.put("filter", filter);
			dm.setMpnews(mpnews);

			if(DataUtil.sendNews(dm, null, 1)==0){
				machineService.updateMachinesSend(ids);
				machineService.updateSendTime();
			}
			
			
		return "redirect:/admin/machine/send_list?send=1";
	}
	
	
	
	
}