/**
 * 
 */
package com.newexcavator.mvc;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.newexcavator.util.DataUtil;
import com.newexcavator.util.SettingUtils;

/**
 * @author Administrator
 *
 */
@Controller
public class CkeditorAction {
	
	@RequestMapping(value = "/actions/ckeditorUpload", method = RequestMethod.POST)
	public void listTdc(HttpServletRequest request, Model model, @RequestParam String CKEditorFuncNum, PrintWriter out) {
		try {
			String fileName = DataUtil.upload2Img(request, "upload").get(0);
			String baseUrl = SettingUtils.getCommonSetting("base.url");
			out.println("<script type=\"text/javascript\">");  
			out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",'"  + baseUrl + "/img/" + fileName + "','')");   
			out.println("</script>");
		} catch (Exception e) {
			e.printStackTrace();
			out.println("<script type=\"text/javascript\">");    
		    out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",''," + "'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");   
		    out.println("</script>");  
		}
		
	}
}
