/**
 * 
 */
package com.newexcavator.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;








import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.DateUtils;










import com.newexcavator.domain.Customer;
import com.newexcavator.domain.OrderList;
import com.newexcavator.domain.SysUsers;
import com.newexcavator.service.UserService;
import com.newexcavator.util.DateFormatter;
import com.newexcavator.util.JsonUtil;
import com.newexcavator.util.PageSupport;
import com.newexcavator.wechat.model.message.response.UserInfo;

/**
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/admin")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/list_user", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String list_user(HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer delete,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) String searchIDcard,
			@RequestParam(required = false) String searchPhone) {
		PageSupport pageSupport = PageSupport.initPageSupport(request);
		if (delete != null && delete.intValue() > 0) {
			model.addAttribute("msg", "删除成功！");
		}
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
		return "pages/user/list_user";
	}
	
	@RequestMapping(value = "/user_share_list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String user_share_list(HttpServletRequest request, Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) String searchIDcard,
			@RequestParam(required = false) String searchPhone) {
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
		param.put("invited_id", id);
		List<SysUsers> users = userService.querySysUsers(param, pageSupport);
		SysUsers u = userService.querySysUserByid(id);

		model.addAttribute("user", u);
		model.addAttribute("users", users);
		model.addAttribute("searchName", searchName);
		//model.addAttribute("searchIDcard", searchIDcard);
		model.addAttribute("searchPhone", searchPhone);
		return "pages/user/user_share_list";
	}

	@RequestMapping(value = "/export_user", method = RequestMethod.POST)
	public @ResponseBody
	String export_user(Model model, HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String searchName,
			@RequestParam(required = false) String searchIDcard,
			@RequestParam(required = false) String searchPhone) {
		Map<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isBlank(searchName))
			param.put("name", searchName.trim());
		/*if (!StringUtils.isBlank(searchIDcard))
			param.put("IDCard", searchIDcard.trim());*/
		if (!StringUtils.isBlank(searchPhone))
			param.put("cell_phone", searchPhone.trim());
		List<SysUsers> users = userService.querySysUsers(param, null);

		try {
			String en ="用户信息.xls";
			String excelName = new String(en.getBytes("UTF-8"), "ISO-8859-1");
			response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");
			   if (request.getHeader("user-agent").toLowerCase().indexOf("msie") > 0)//IE浏览器
			   {
			       response.setHeader("content-disposition", "filename=" + java.net.URLEncoder.encode(en, "UTF-8"));
			   } else {
				   response.setHeader("Content-Disposition", "attachment; filename="
						   + excelName);
			   }
			OutputStream os = null;
			// HSSFWorkbook work = null;
			HSSFWorkbook work = new HSSFWorkbook();
			HSSFSheet sheet1 = work.createSheet("sheet1");

			// 合并单元格
			sheet1.addMergedRegion(new CellRangeAddress(0, 2, 0, 2));

			sheet1.setColumnWidth(0, 20*256);
			sheet1.setColumnWidth(1, 30*256);
			sheet1.setColumnWidth(2, 30*256);
			
			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 表头格式
			CellStyle titleStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();

			/*
			 * //内容加上边框 dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			 * //dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			 * dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			 * //dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			 * dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			 * //dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			 * dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			 * //dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			 */

			// 居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setWrapText(true);

			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);

			// 标题字体
			Font font = work.createFont();
			// 表头字体
			Font titlefont = work.createFont();
			// 内容字体
			Font datafont = work.createFont();

			font.setFontHeightInPoints((short) 16);
			font.setFontName("宋体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			titlefont.setFontHeightInPoints((short) 11);
			titlefont.setFontName("宋体");
			titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 11);
			datafont.setFontName("宋体");
			// datafont.setColor(HSSFColor.RED.index);

			// 把字体加入到格式中
			cellStyle.setFont(font);
			titleStyle.setFont(titlefont);
			dataStyle.setFont(datafont);

			// 设置标题
			Row row = sheet1.createRow(0);
			Cell cell = row.createCell(0);
			// 设置内容
			cell.setCellValue("用户信息");
			// 设置单元格格式
			cell.setCellStyle(cellStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// 设置表头
			row = sheet1.createRow(3);
			cell = row.createCell(0);
			// 设置内容
			cell.setCellValue("姓名");
			// 设置单元格格式
			cell.setCellStyle(titleStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// 二层
			cell = row.createCell(1);
			// 设置内容
			cell.setCellValue("电话");
			// 设置单元格格式
			cell.setCellStyle(titleStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			// 三层
			cell = row.createCell(2);
			// 设置内容
			cell.setCellValue("创建时间");
			// 设置单元格格式
			cell.setCellStyle(titleStyle);
			// 设置单元格内容类型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);

			

			Integer x = 4;
			/*
			 * for(CommunityAddressLevel co:cal){ if(co.getType()==5){ level=5;
			 * break; } if(co.getType()==4){ level=4; } }
			 */
			for (SysUsers u : users) {
				row = sheet1.createRow(x++);
				
				cell = row.createCell(0);
				// 设置内容
				cell.setCellValue(u.getName());
				// 设置单元格格式
				cell.setCellStyle(dataStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(1);
				// 设置内容
				cell.setCellValue(u.getCell_phone());
				// 设置单元格格式
				cell.setCellStyle(dataStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell = row.createCell(2);
				// 设置内容
				cell.setCellValue(DateUtil.formatDate(u.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
				// 设置单元格格式
				cell.setCellStyle(dataStyle);
				// 设置单元格内容类型
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			}


			os = response.getOutputStream();

			work.write(os);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		

		return null;
	}

/*	@RequestMapping(value = "/import_user", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String import_user(Model model, HttpServletRequest request) {
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			MultipartFile mf = multiRequest.getFile("file");
			if (mf != null) {
				try {
					InputStream in = mf.getInputStream();
					if (in.available() > 0) {
						Workbook wb = Workbook.getWorkbook(in);
						if (wb != null) {
							// 获取所有的工作表
							Sheet[] sheets = wb.getSheets();
							if (sheets != null && sheets.length != 0) {
								for (int i = 0; i < sheets.length; i++) {
									// 获取每一个工作表,此后的操作都在工作表上面进行
									Sheet sheet = wb.getSheet(i);

									// 获取行的长度
									int rows_len = sheet.getRows();
									for (int j = 3; j < rows_len; j++) {
										// 获取所有的列
										Cell[] cells = sheet.getRow(j);
										if (cells != null && cells.length != 0) {
											Customer customer = new Customer();
											customer.setTotal_number(Integer
													.valueOf(cells[0]
															.getContents()));
											customer.setSn(Integer
													.valueOf(cells[1]
															.getContents()));

											Calendar calendar = DateUtils
													.create(cells[2]
															.getContents(),
															cells[3].getContents(),
															cells[4].getContents());
											customer.setSale_date(calendar
													.getTime());

											calendar = DateUtils.create(
													cells[5].getContents(),
													cells[6].getContents(),
													cells[7].getContents());
											customer.setVei_date(calendar
													.getTime());

											customer.setName(cells[8]
													.getContents());
											customer.setType(cells[9]
													.getContents());
											customer.setType_attribute(cells[10]
													.getContents());
											customer.setMachine_number(cells[11]
													.getContents());
											customer.setEngine_number(cells[12]
													.getContents());
											customer.setPhone_number(cells[13]
													.getContents());
											customer.setSale_area(cells[14]
													.getContents());
											customer.setSale_rep(cells[15]
													.getContents());
											customer.setCustomer_area(cells[16]
													.getContents());
											customer.setIDCard(cells[17]
													.getContents());
											customer.setCustomer_type(cells[18]
													.getContents());
											customer.setComments(cells[19]
													.getContents());
											customer.setPrice(Float
													.valueOf(cells[20]
															.getContents()));
											customer.setFreight(Float
													.valueOf(cells[21]
															.getContents()));
											customer.setPayment_method(cells[22]
													.getContents());
											customer.setContract_deposit(Float
													.valueOf(cells[23]
															.getContents()));
											customer.setVender_deposit(Float
													.valueOf(cells[24]
															.getContents()));
											customer.setNeed_deposit(Float
													.valueOf(cells[25]
															.getContents()));
											customer.setActual_deposit(Float
													.valueOf(cells[26]
															.getContents()));
											customer.setDebt(Float
													.valueOf(cells[27]
															.getContents()));

											userService.saveCustomer(customer);
										}
									}
								}

								model.addAttribute("msg", "数据导入成功！");
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (BiffException e) {
					e.printStackTrace();
				}
			}
		}
		return "pages/user/import_user";
	}*/

	@RequestMapping(value = "/update_user_password", method = RequestMethod.GET)
	public String update_user_password(Model model,
			@RequestParam Integer id,
			@RequestParam(required = false) Integer save) {
			SysUsers user = userService.querySysUserByid(id);
			model.addAttribute("user", user);

			if (save != null)
				model.addAttribute("msg", "操作成功！");
			return "pages/user/edit_user";
	}
	
	@RequestMapping(value = "/update_user_password_save", method = RequestMethod.POST)
	public String update_user_password_save(Model model,
			@RequestParam Integer id,
			@RequestParam String password) {
		PasswordEncoder pe = new BCryptPasswordEncoder();
		userService.updatePassword(pe.encode(password),id);
		
		return "redirect:update_user_password?save=1&id="+id;
	}
	
	
	@RequestMapping(value = "/del_user", method = RequestMethod.GET)
	public String del_user(Model model, @RequestParam Integer[] id) {
		List<Integer> ids = Arrays.asList(id);
		userService.delCustomer(ids);
		return "redirect:/admin/list_user?delete=1";
	}
	
	@RequestMapping(value = "/add_user", method = RequestMethod.GET)
	public String add_user(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) Integer save) {
		if (id != null && id.intValue() > 0) {
			SysUsers user = userService.querySysUserByid(id);
			model.addAttribute("user", user);

			if (save != null)
				model.addAttribute("msg", "操作成功！");
			return "pages/user/edit_user";
		}

		return "pages/user/add_user";
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public String saveUser(Model model,
			@RequestParam(required = false) Integer id,
			@RequestParam String name, @RequestParam String cell_phone,
			@RequestParam String IDCard) {

		Customer user;
		if (id != null && id.intValue() > 0) {
			user = userService.queryCustomerById(id);
		} else {
			user = new Customer();
		}

		user.setName(name);
		user.setPhone_number(cell_phone);
		user.setIDCard(IDCard);
		userService.saveCustomer(user);

		return "redirect:/admin/add_user?save=1&id=" + user.getId();
	}

	@RequestMapping(value = "/list_order", method = { RequestMethod.GET, RequestMethod.POST })
	public String list_order(HttpServletRequest request, Model model, @RequestParam(required = false) Integer delete,
			@RequestParam(required = false) String type) {
		PageSupport pageSupport = PageSupport.initPageSupport(request);
		if (delete != null && delete.intValue() > 0) {
			model.addAttribute("msg", "删除成功！");
		}

		Map<String, Object> param = new HashMap<String, Object>();
		if (StringUtils.equals("subscribe", type)) {//预约
			param.put("type_id", 0);//非零部件
		} else {
			param.put("type_id", 3);//零部件
		}
		List<OrderList> orderList = userService.queryOrderList(param, pageSupport);

		
		model.addAttribute("orderList", orderList);
		model.addAttribute("type", type);
		return "pages/user/list_order";
	}
	
	@RequestMapping(value = "/del_order", method = RequestMethod.GET)
	public String del_order(Model model, @RequestParam Integer[] id) {
		List<Integer> ids = Arrays.asList(id);
		userService.delOrderList(ids);
		return "redirect:/admin/list_order?delete=1";
	}
	
	@RequestMapping(value = "/order/update_check", method = RequestMethod.GET)
	public String orderUpdateCheck(Model model, @RequestParam Integer id, @RequestParam String type) {
		userService.updateCheck(id);
		return "redirect:/admin/list_order?type=" + type;
	}
	
	@RequestMapping(value = "/checkUserName", method = RequestMethod.POST)
	public @ResponseBody String checkUserName(Model model, @RequestParam String name, @RequestParam(required=false) Integer id) {
		Integer count = userService.queryCustomerByName(name, id);
		return count > 0 ? JsonUtil.toJson("exists") : JsonUtil.toJson("ok");
	}
	
}
