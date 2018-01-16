/**
 * 
 */
package com.newexcavator.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.newexcavator.util.SettingUtils;

/**
 * @author randy
 * 
 */
public class ImageServlet extends HttpServlet {
	protected Logger log = Logger.getLogger(getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		System.out.println("IMG:" + path);
		String [] paths = path.split("/");
		String mediaDir="";
		if(paths[1].equals("temp")){
			mediaDir = SettingUtils.getCommonSetting("upload.file.temp.path");
			path = paths[2];
		} else {
			mediaDir = SettingUtils.getCommonSetting("upload2.path");
		}
		
		File mediaFile = new File(mediaDir + File.separator + path);

		InputStream mediaIn = null;
		try {
			mediaIn = new FileInputStream(mediaFile);
		} catch (FileNotFoundException e) {
			log.debug("Media not found:" + mediaIn);
		}
		if (mediaIn != null) {
			BufferedInputStream in = new BufferedInputStream(mediaIn);
			OutputStream mediaOS;
			try {
				mediaOS = resp.getOutputStream();
				int r1 = in.read();
				while (r1 != -1) {
					mediaOS.write(r1);
					r1 = in.read();
				}
				mediaOS.flush();
				mediaOS.close();
			} catch (IOException e) {
				throw new RuntimeException("Error generating media", e);
			} finally {
				try {
					mediaIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
