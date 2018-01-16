/**
 * 
 */
package com.newexcavator.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import com.newexcavator.domain.Category;
import com.newexcavator.domain.MachineInfor;
import com.newexcavator.domain.MachineThumb;
import com.newexcavator.service.MachineService;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.JsonUtil;
import com.newexcavator.util.PageSupport;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/admin")
public class MechineController {
	
	@Autowired
	private MachineService machineService;
	
	@RequestMapping(value = "/list_mechine", method = RequestMethod.GET)
	public String list_mechine(HttpServletRequest request, Model model, @RequestParam String type, @RequestParam(required=false) Integer delete) {
		int moduleId = 1;
		if (StringUtils.equals(type, "old")) {
			moduleId = 2;
		} else if (StringUtils.equals(type, "part")) {
			moduleId = 3;
		} else if (StringUtils.equals(type, "lease")) {
			moduleId = 4;
		}
		
		PageSupport pageSupport = PageSupport.initPageSupport(request);
		
		List<MachineInfor> mechines = machineService.queryMachineInforByTypeId(moduleId, null, pageSupport);
		
		model.addAttribute("mechines", mechines);
		model.addAttribute("type", type);
		
		if (delete != null && delete.intValue() > 0) {
			model.addAttribute("msg", "删除成功！");
		}
		return "pages/mechine/list_mechine";
	}
	
	@RequestMapping(value = "/add_mechine", method = RequestMethod.GET)
	public String add_mechine(Model model, @RequestParam String type, @RequestParam(required=false) Integer id,
			@RequestParam(required=false) Integer save) {
		int moduleId = 1;
		if (StringUtils.equals(type, "old")) {
			moduleId = 2;
		} else if (StringUtils.equals(type, "part")) {
			moduleId = 3;
		} else if (StringUtils.equals(type, "lease")) {
			moduleId = 4;
		}
		
		List<Category> categories = machineService.queryCategoryByModuleId(moduleId);
		model.addAttribute("categories", categories);
		model.addAttribute("type", type);
		
		if (id != null && id.intValue() > 0) {
			MachineInfor mi = machineService.queryMachineInforById(id);
			model.addAttribute("mi", mi);
			if (save != null)
				model.addAttribute("msg", "操作成功！");
			return "pages/mechine/edit_mechine";
		}
		
		return "pages/mechine/add_mechine";
	}
	
	@RequestMapping(value = "/del_mechine", method = RequestMethod.GET)
	public String del_mechine(Model model, @RequestParam Integer [] id, @RequestParam String type) {
		
		List<Integer> ids = Arrays.asList(id);
		machineService.delMachineInfor(ids);
		
		return "redirect:/admin/list_mechine?delete=1&type=" + type;
	}
	
	@RequestMapping(value = "/saveMechine", method = RequestMethod.POST)
	public String saveMechine(HttpServletRequest request, Model model, @RequestParam(required=false) Integer id,
			@RequestParam String version, @RequestParam Integer category,
			@RequestParam String srctype, @RequestParam String name,
			@RequestParam String phone, @RequestParam String content, @RequestParam Float jine,
			@RequestParam String url) {
		int moduleId = 1;
		if (StringUtils.equals(srctype, "old")) {
			moduleId = 2;
		} else if (StringUtils.equals(srctype, "part")) {
			moduleId = 3;
		} else if (StringUtils.equals(srctype, "lease")) {
			moduleId = 4;
		}
		
		MachineInfor mi;
		if (id != null && id.intValue() > 0) {
			mi = machineService.queryMachineInforById(id);
		} else {
			mi = new MachineInfor();
		}
		
		mi.setName(name);
		mi.setCat_id(category);
		Document doc = Jsoup.parse(content);
		Elements es = doc.select("iframe");
		for (Element e : es) {
			//e.attr("width", "90%");
			e.removeAttr("width");
			e.removeAttr("height");
			e.removeAttr("style");
		}
		
		Elements imgES = doc.select("img");
		for (Element e : imgES) {
			//e.attr("width", "90%");
			e.removeAttr("width");
			e.removeAttr("height");
			e.removeAttr("style");
		}
		
		Elements tableES = doc.select("table");
		for (Element e : tableES) {
			//e.attr("width", "90%");
			e.removeAttr("width");
			e.removeAttr("height");
			e.removeAttr("style");
		}
		
		mi.setDescription(doc.body().html());
		mi.setPhone(phone);
		mi.setType_id(moduleId);
		mi.setVersion(version);
		mi.setJine(jine);
		mi.setUrl(url);
		try {
			List<String> fileNames = DataUtil.upload2Img(request, "file", true);
			if (!CollectionUtils.isEmpty(fileNames)) {
				mi.setThumb_url(fileNames.get(0));
				
				List<MachineThumb> mts = new ArrayList<MachineThumb>();
				for (String fileName : fileNames) {
					MachineThumb mt = new MachineThumb();
					mt.setThumb_url(fileName);
					
					mts.add(mt);
				}
				
				mi.setThumbList(mts);
			} else {
				mi.setThumbList(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		machineService.saveMachineInfor(mi);
		return "redirect:/admin/add_mechine?save=1&type=" + srctype + "&id=" + mi.getId();
	}
	
	@RequestMapping(value = "/del_thumb", method = RequestMethod.GET)
	public @ResponseBody String del_thumb(Model model, @RequestParam Integer id) {
		
		machineService.delThumbById(id);
		
		return JsonUtil.toJson("ok");
	}
}
