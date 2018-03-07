/**
 * 
 */
package com.newexcavator.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.newexcavator.wechat.AccessTokenTool;
import com.newexcavator.wechat.model.message.response.ResponseStatus;
import com.newexcavator.wechat.model.message.response.UploadResJson;
import com.newexcavator.wechat.model.message.server.AttentionMessage;
import com.newexcavator.wechat.model.message.server.DeployMessage;
import com.newexcavator.wechat.model.message.server.NewsMessage;

/**
 * @author Administrator
 * 
 */
public class DataUtil {

	/**
	 * 每位允许的字符
	 */
	// private static final String POSSIBLE_CHARS =
	// "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String POSSIBLE_CHARS = "0123456789";

	/**
	 * 生产一个指定长度的随机字符串
	 * 
	 * @param length
	 *            字符串长度
	 * @return
	 */
	public static String generateRandomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS
					.length())));
		}
		return sb.toString();
	}

	public static String formatData(int number, int width) {
		if (number <= 0)
			return null;
		String sNum = number + "";
		if (sNum.length() < width) {
			int w = width - sNum.length();
			for (int i = 0; i < w; i++) {
				sNum = "0" + sNum;
			}
		}
		return sNum;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			String[] parsedName = new FileUtils().getFullFileNameAndExtension(sPath);
			String thumbPath = parsedName[0] + parsedName[1] + "_S" + parsedName[2];
			deleteFile(thumbPath);
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static String encodeStr(String str) {
		if (str == null)
			return null;
		try {
			return new String(str.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encodeStrUTF8(String str) {
		if (StringUtils.isBlank(str))
			return null;
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static String copyToDir(String filename, boolean createThumb) throws IOException {
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");
		String sep = System.getProperty("file.separator");
		String imgFileDir = SettingUtils.getCommonSetting("upload2.path");
		
		File srcFile = new File(fileDir + sep + filename);
		
		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(array[random.nextInt(array.length)]);
			sb.append(array[random.nextInt(array.length)]);
			
			subDir.append(sb.toString());
		}
		
		File dstDir = new File(imgFileDir + sep + subDir);
		if (!dstDir.exists()) {
			dstDir.mkdirs();
		}
		
		org.apache.commons.io.FileUtils.moveFileToDirectory(srcFile, dstDir, true);
		String[] parsedName = new FileUtils().getFullFileNameAndExtension(fileDir + sep + filename);
		String thumbPath = parsedName[0] + parsedName[1] + "_S" + parsedName[2];
		deleteFile(thumbPath);
		
		if (createThumb) {
			String heightS = SettingUtils.getCommonSetting("thumbnailator.height");
			String widthS = SettingUtils.getCommonSetting("thumbnailator.width");

			Integer height = !StringUtils.isBlank(heightS) ? Integer.valueOf(heightS) : 0;
			Integer width = !StringUtils.isBlank(widthS) ? Integer.valueOf(widthS) : 0;

			ImageResizer.resizeImage(imgFileDir + sep + subDir + sep + filename, width, height, "_S");
		}
		
		return subDir + sep + filename;
	}

	public static final char[] array = { 'q', 'w', 'e', 'r', 't', 'y', 'u',
			'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z',
			'x', 'c', 'v', 'b', 'n', 'm', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
			'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V',
			'B', 'N', 'M' };

	public static String _10_to_62(long number) {
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {
			stack.add(array[new Long((rest - (rest / 62) * 62)).intValue()]);
			rest = rest / 62;
		}
		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		return result.toString();
	}

	public static long _62_to_10(String sixty_str) {
		int multiple = 1;
		long result = 0;
		Character c;
		for (int i = 0; i < sixty_str.length(); i++) {
			c = sixty_str.charAt(sixty_str.length() - i - 1);
			result += _62_value(c) * multiple;
			multiple = multiple * 62;
		}
		return result;
	}

	private static int _62_value(Character c) {
		for (int i = 0; i < array.length; i++) {
			if (c == array[i]) {
				return i;
			}
		}
		return -1;
	}

	public static void deleteByUpload2Img(String fileName) {
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload2.path");// 存放文件文件夹名称

		String filePath = fileDir + sep + fileName;
		deleteFile(filePath);

		if (!StringUtils.isBlank(fileName)) {
			String[] parsedName = new FileUtils()
					.getFullFileNameAndExtension(filePath);
			String thumbPath = parsedName[0] + parsedName[1] + "_S"
					+ parsedName[2];
			deleteFile(thumbPath);
		}
	}

	public static List<String> upload2Img(HttpServletRequest req, String name,
			boolean createThumb) throws Exception {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload2.path");// 存放文件文件夹名称
		
		StringBuffer subDir = new StringBuffer();
		for (int i = 0; i < 2; i++) {
			if (i != 0) {
				subDir.append(sep);
			}
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append(array[random.nextInt(array.length)]);
			sb.append(array[random.nextInt(array.length)]);
			
			subDir.append(sb.toString());
		}
		
		File dirPath = new File(fileDir + sep + subDir.toString());
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		List<MultipartFile> mfs = multiRequest.getFiles(name);
		List<String> files = new ArrayList<String>();
		for (MultipartFile mft : mfs) {
			CommonsMultipartFile mf = (CommonsMultipartFile) mft;
			byte[] bytes = mf.getBytes();
			StringBuffer newFileName = new StringBuffer();
			if (bytes.length != 0) {
				String fileTrueName = mf.getOriginalFilename();
				String ext = fileTrueName.substring(fileTrueName
						.lastIndexOf("."));
				if (!".jpg/.jpeg/.gif/.bmp/.png".contains(ext.toLowerCase())) {
					throw new Exception("格式错误！");
				}
				newFileName.append(System.currentTimeMillis());
				newFileName.append(ext);
				String fileName = fileDir  + sep + subDir.toString() + sep + newFileName.toString();

				File uploadedFile = new File(fileName);
				try {
					FileCopyUtils.copy(bytes, uploadedFile);
					if (createThumb) {
						String heightS = SettingUtils.getCommonSetting("thumbnailator.height");
						String widthS = SettingUtils.getCommonSetting("thumbnailator.width");

						Integer height = !StringUtils.isBlank(heightS) ? Integer.valueOf(heightS) : 0;
						Integer width = !StringUtils.isBlank(widthS) ? Integer.valueOf(widthS) : 0;

						ImageResizer.resizeImage(fileName, width, height, "_S");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				files.add(subDir.toString() + sep + newFileName.toString());
			}

		}

		return files;
	}

	public static List<String> upload2Img(HttpServletRequest req, String name)
			throws Exception {
		return upload2Img(req, name, false);
	}

	public static String uploadFile(File f, String url, int count) {
		if (StringUtils.isBlank(url)) {
			url = "https://api.weixin.qq.com/cgi-bin/media/upload?type=thumb&access_token=" + AccessTokenTool.getAccessToken(false);
		}
		if (!f.exists()) {
			return null;
		}
		
		String media_id = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			MultipartEntityBuilder meb = MultipartEntityBuilder.create();
			meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			meb.addPart("file", new FileBody(f));
			httpPost.setEntity(meb.build());

			HttpResponse response = httpclient.execute(httpPost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	            	System.out.println("Response message======>>>" + result);
	            	UploadResJson urj = JsonUtil.fromJson(result, UploadResJson.class);
	            	if (urj.getMedia_id() != null) {
	            		media_id = urj.getMedia_id();
	            	} else if (urj.getThumb_media_id() != null) {
	            		media_id = urj.getThumb_media_id();
	            	} else if (urj.getErrcode() != null) {
	            		Integer errcode = urj.getErrcode();
	            		if (errcode.intValue() == 42001) {
	            			return uploadFile(f, "https://api.weixin.qq.com/cgi-bin/media/upload?type=thumb&access_token=" + AccessTokenTool.getAccessToken(true), ++count);
	            		} else if (errcode.intValue() == 40001 && count <= 3) {
	            			return uploadFile(f, "https://api.weixin.qq.com/cgi-bin/media/upload?type=thumb&access_token=" + AccessTokenTool.getAccessToken(false), ++count);
	            		}
	            	}
	            }
			} else {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
            	System.out.println("Response message======>>>" + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpPost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return media_id;
	}
	
	public static String uploadFile(File f, String url, int count, String type) {
		if (StringUtils.isBlank(url)) {
			url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?type=" + type + "&access_token=" + AccessTokenTool.getAccessToken(false);
		}
		if (!f.exists()) {
			return null;
		}
		
		String media_id = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			MultipartEntityBuilder meb = MultipartEntityBuilder.create();
			meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			meb.addPart("file", new FileBody(f));
			httpPost.setEntity(meb.build());

			HttpResponse response = httpclient.execute(httpPost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	            	System.out.println("Response message======>>>" + result);
	            	UploadResJson urj = JsonUtil.fromJson(result, UploadResJson.class);
	            	if (urj.getMedia_id() != null) {
	            		media_id = urj.getMedia_id();
	            	} else if (urj.getThumb_media_id() != null) {
	            		media_id = urj.getThumb_media_id();
	            	} else if (urj.getErrcode() != null) {
	            		Integer errcode = urj.getErrcode();
	            		if (errcode.intValue() == 42001) {
	            			return uploadFile(f, "http://file.api.weixin.qq.com/cgi-bin/media/upload?type=" + type + "&access_token=" + AccessTokenTool.getAccessToken(true), ++count);
	            		} else if (errcode.intValue() == 40001 && count <= 3) {
	            			return uploadFile(f, "http://file.api.weixin.qq.com/cgi-bin/media/upload?type=" + type + "&access_token=" + AccessTokenTool.getAccessToken(false), ++count);
	            		}
	            	}
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpPost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return media_id;
	}
	
	public static String uploadNews(NewsMessage nm, String url, int count) {
		if (StringUtils.isBlank(url)) {
			url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + AccessTokenTool.getAccessToken(false);
		}
		
		String media_id = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			StringEntity stringEntity = new StringEntity(JsonUtil.toJson(nm), "utf-8");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

			HttpResponse response = httpclient.execute(httpPost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	            	System.out.println("Response message======>>>" + result);
	            	ResponseStatus status = null;
	        		try {
	        			status = JsonUtil.fromJson(result, ResponseStatus.class);
	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	            		
	            	if (status != null && status.getMedia_id() != null) {
	            		media_id = status.getMedia_id();
	            	} else if (status != null && status.getErrcode() != null) {
	            		Integer errcode = status.getErrcode();
	            		System.out.println("errcode===========>" + errcode);
	            		if (errcode.intValue() == 42001) {
	            			return uploadNews(nm, "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + AccessTokenTool.getAccessToken(true), ++count);
	            		} else if (errcode.intValue() == 40001 && count <= 3) {
	            			return uploadNews(nm, "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + AccessTokenTool.getAccessToken(false), ++count);
	            		}
	            	}
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpPost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return media_id;
	}
	
	public static List<String> getAttentionList(String url, int count) {
		if (StringUtils.isBlank(url)) {
			url = "https://api.weixin.qq.com/cgi-bin/user/get?next_openid=&access_token=" + AccessTokenTool.getAccessToken(false);
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	            	System.out.println("Response message======>>>" + result);
	            	AttentionMessage am = JsonUtil.fromJson(result, AttentionMessage.class);
	            	if (am.getErrcode() != null && am.getErrcode().intValue() == 42001) {
	            		return getAttentionList("https://api.weixin.qq.com/cgi-bin/user/get?next_openid=&access_token=" + AccessTokenTool.getAccessToken(true), ++count);
	            	} else if (am.getErrcode() != null && am.getErrcode().intValue() == 40001 && count <= 3) {
	            		return getAttentionList("https://api.weixin.qq.com/cgi-bin/user/get?next_openid=&access_token=" + AccessTokenTool.getAccessToken(false), ++count);
	            	} else {
	            		return am.getData().get("openid");
	            	}
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpGet.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static Integer sendNews(DeployMessage dm, String url, int count) {
		if (StringUtils.isBlank(url)) {
			url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + AccessTokenTool.getAccessToken(false);
			//url = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=" + AccessTokenTool.getAccessToken(false);
		}
		Integer errcode=-1;
		//String media_id = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			StringEntity stringEntity = new StringEntity(JsonUtil.toJson(dm), "utf-8");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

			HttpResponse response = httpclient.execute(httpPost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	            	System.out.println("Response message======>>>" + result);
	            	ResponseStatus status = null;
	        		try {
	        			status = JsonUtil.fromJson(result, ResponseStatus.class);
	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	            	if (status != null && status.getErrcode() != null) {
	            		errcode = status.getErrcode();
	            		System.out.println("errcode===========>" + errcode);
	            		if (errcode.intValue() == 42001) {
	            			return sendNews(dm, "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + AccessTokenTool.getAccessToken(true), ++count);
	            		} else if (errcode.intValue() == 40001 && count <= 3) {
	            			return sendNews(dm, "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + AccessTokenTool.getAccessToken(false), ++count);
	            		}
	            	}
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpPost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return errcode;
	}

	public static Boolean sendMessage(String msg, String url, int count) {
		if (StringUtils.isBlank(url)) {
			url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + AccessTokenTool.getAccessToken(false);
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			StringEntity stringEntity = new StringEntity(msg,"UTF-8");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

			HttpResponse response = httpclient.execute(httpPost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				//显示内容  
	            if (entity != null) {
	            	String result = EntityUtils.toString(entity);
	            	System.out.println("Response message======>>>" + result);
	            	ResponseStatus status = null;
	        		try {
	        			status = JsonUtil.fromJson(result, ResponseStatus.class);
	        		} catch (Exception e) {
	        			e.printStackTrace();
	        		}
	            	if (status != null && status.getErrcode() != null) {
	            		Integer errcode = status.getErrcode();
	            		System.out.println("errcode===========>" + errcode);
	            		if (errcode.intValue() == 42001) {
	            			return sendMessage(msg, "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + AccessTokenTool.getAccessToken(true), ++count);
	            		} else if (errcode.intValue() == 40001 && count <= 3) {
	            			return sendMessage(msg, "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + AccessTokenTool.getAccessToken(false), ++count);
	            		}
	            	} else {
	            		return true;
	            	}
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			httpPost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public static void uploadFile(HttpServletRequest req, String name) {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

		File dirPath = new File(fileDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		
		MultipartFile mf = multiRequest.getFile("file");
		
		try {
			byte [] src = mf.getBytes();
			if (src != null && src.length != 0) {
				File dst = new File(fileDir + sep + name);
				copy(src, dst);
				String heightS = SettingUtils.getCommonSetting("thumbnailator.height");
				String widthS = SettingUtils.getCommonSetting("thumbnailator.width");

				Integer height = !StringUtils.isBlank(heightS) ? Integer.valueOf(heightS) : 0;
				Integer width = !StringUtils.isBlank(widthS) ? Integer.valueOf(widthS) : 0;

				ImageResizer.resizeImage(fileDir + sep + name, width, height, "_S");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String uploadFile2(HttpServletRequest req) {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
		String sep = System.getProperty("file.separator");
		String fileDir = SettingUtils.getCommonSetting("upload.file.temp.path");// 存放文件文件夹名称

		File dirPath = new File(fileDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		
		MultipartFile mf = multiRequest.getFile("file");
		String name =  mf.getOriginalFilename(); 
		System.out.println(name);
		try {
			byte [] src = mf.getBytes();
			if (src != null && src.length != 0) {
				File dst = new File(fileDir + sep + name);
				copy(src, dst);
				String heightS = SettingUtils.getCommonSetting("thumbnailator.height");
				String widthS = SettingUtils.getCommonSetting("thumbnailator.width");

				Integer height = !StringUtils.isBlank(heightS) ? Integer.valueOf(heightS) : 0;
				Integer width = !StringUtils.isBlank(widthS) ? Integer.valueOf(widthS) : 0;

				ImageResizer.resizeImage(fileDir + sep + name, width, height, "_S");
			}
			return name;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static void copy(byte [] src, File dst) {
		OutputStream out = null;
		try {
			if (dst.exists()) {
				out = new BufferedOutputStream(new FileOutputStream(dst, true)); // plupload 配置了chunk的时候新上传的文件appand到文件末尾
			} else {
				out = new BufferedOutputStream(new FileOutputStream(dst));
			}

			out.write(src);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		// DataUtil.deleteDirectory("D:\\erweima\\lyqhn_1_3\\code");
		
		String mess = DataUtil.uploadFile(new File("D:/t/t.jpg"), "http://file.api.weixin.qq.com/cgi-bin/media/upload?type=image&access_token=9Yvl6pGK8w32n-nDSK4dMJgGEY_fbUB-so_1mGfKVK0ayb5aq-TkC9d1H8qIrnym", 1);
		System.out.println(mess);
	}
}
