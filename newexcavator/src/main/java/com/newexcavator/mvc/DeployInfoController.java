/**
 * 
 */
package com.newexcavator.mvc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import com.newexcavator.domain.DeployInfor;
import com.newexcavator.service.DeployInforService;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.PageSupport;
import com.newexcavator.util.SettingUtils;
import com.newexcavator.wechat.model.message.server.Article;
import com.newexcavator.wechat.model.message.server.DeployMessage;
import com.newexcavator.wechat.model.message.server.NewsMessage;

/**
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/admin")
public class DeployInfoController {

	@Autowired
	private DeployInforService deployInforService;

	@RequestMapping(value = "/list_news", method = RequestMethod.GET)
	public String list_news(HttpServletRequest request, Model model,
			@RequestParam String type,
			@RequestParam(required = false) Integer delete) {
		int moduleId = 1;
		if (StringUtils.equals(type, "news")) {
			moduleId = 2;
		}

		PageSupport pageSupport = PageSupport.initPageSupport(request);

		List<DeployInfor> infors = deployInforService.queryDeployInforByTypeId(
				moduleId, null, pageSupport);

		model.addAttribute("news", infors);
		model.addAttribute("type", type);
		if (delete != null && delete.intValue() > 0) {
			model.addAttribute("msg", "删除成功！");
		}
		return "pages/news/list_news";
	}

	@RequestMapping(value = "/del_news", method = RequestMethod.GET)
	public String del_news(Model model, @RequestParam Integer[] id,
			@RequestParam String type) {
		List<Integer> ids = Arrays.asList(id);
		deployInforService.delDeployInfor(ids);

		return "redirect:/admin/list_news?delete=1&type=" + type;
	}

	@RequestMapping(value = "/add_news", method = RequestMethod.GET)
	public String add_news(Model model, @RequestParam String type,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer save) {
		model.addAttribute("type", type);

		if (id != null && id.intValue() > 0) {
			DeployInfor di = deployInforService.queryDeployInforById(id);
			model.addAttribute("di", di);
			if (save != null)
				model.addAttribute("msg", "操作成功！");
			return "pages/news/edit_news";
		}

		return "pages/news/add_news";
	}

	@RequestMapping(value = "/saveNews", method = RequestMethod.POST)
	public String saveNews(HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam String title, @RequestParam String description,
			@RequestParam String type, @RequestParam String content,
			@RequestParam(required = false) Integer issend) {
		int moduleId = 1;
		if (StringUtils.equals(type, "news")) {
			moduleId = 2;
		}

		DeployInfor di;
		if (id != null && id.intValue() > 0) {
			di = deployInforService.queryDeployInforById(id);
		} else {
			di = new DeployInfor();
		}

		di.setTitle(title);
		di.setDescription(description);
		di.setType(moduleId);

		if (issend != null && issend == 1) {
			di.setIssend(issend);
			Date create_time = new Date();
			di.setCreate_time(create_time);
		} else {
			di.setIssend(0);
			Date create_time = new Date(0);
			di.setCreate_time(create_time);
		}

		Document doc = Jsoup.parse(content);
		Elements es = doc.select("iframe");
		for (Element e : es) {
			// e.attr("width", "90%");
			e.removeAttr("width");
			e.removeAttr("height");
			e.removeAttr("style");
		}

		Elements imgES = doc.select("img");
		for (Element e : imgES) {
			// e.attr("width", "90%");
			e.removeAttr("width");
			e.removeAttr("height");
			e.removeAttr("style");
		}

		Elements tableES = doc.select("table");
		for (Element e : tableES) {
			// e.attr("width", "90%");
			e.removeAttr("width");
			e.removeAttr("height");
			e.removeAttr("style");
		}

		di.setContent(doc.body().html());
		try {
			String fileName = DataUtil.upload2Img(request, "file", true).get(0);

			if (!StringUtils.isEmpty(fileName)) {
				// 上传图片到微信服务器
				String sep = System.getProperty("file.separator");
				String fileDir = SettingUtils.getCommonSetting("upload2.path");
				File f = new File(fileDir + sep + fileName.split("\\.")[0]
						+ "_S." + fileName.split("\\.")[1]);
				String media_thumb_id = DataUtil.uploadFile(f, null, 1);
				di.setMedia_thumb_id(media_thumb_id);
			}

			di.setThumb_url(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		deployInforService.saveDeployInfor(di);

		// 上传图文消息到微信服务器
		NewsMessage nm = new NewsMessage();
		List<Article> alist = new ArrayList<Article>();
		nm.setArticles(alist);

		Article a = new Article();
		a.setAuthor("管理员");
		a.setContent(content);
		a.setDigest(description);
		if (!StringUtils.isEmpty(di.getMedia_thumb_id()))
			a.setThumb_media_id(di.getMedia_thumb_id());
		a.setTitle(title);

		String baseUrl = SettingUtils.getCommonSetting("base.url.nohttp");
		a.setContent_source_url(baseUrl + "/mobile/detail_news?type=" + type
				+ "&id=" + di.getId());
		alist.add(a);

		String media_id = DataUtil.uploadNews(nm, null, 1);

		di.setMedia_id(media_id);
		deployInforService.updateMediaIdDeployInfor(di);
		if (issend != null && issend == 1) {
			// 获取关注列表
			List<String> as = DataUtil.getAttentionList(null, 1);

			// 开始发布
			DeployMessage dm = new DeployMessage();
			//dm.setTouser(as);
			dm.setMsgtype("mpnews");
			Map<String, Object> mpnews = new HashMap<String, Object>();
			mpnews.put("media_id", media_id);
			dm.setMpnews(mpnews);

			DataUtil.sendNews(dm, null, 1);
		}

		return "redirect:/admin/add_news?save=1&type=" + type + "&id="
				+ di.getId();
	}
}
