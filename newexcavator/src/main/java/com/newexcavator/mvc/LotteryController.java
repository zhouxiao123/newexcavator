package com.newexcavator.mvc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newexcavator.domain.Lottery;
import com.newexcavator.service.LotteryService;

@Controller
public class LotteryController {
	@Autowired
	private LotteryService lotteryService;
	
	@RequestMapping(value="/mobile/lottery")
	public String lottery(Model model){
	
		return "pages/mobile/bind_user_o";
	}
	
	@RequestMapping(value="/mobile/saveLotteryInfo")
	public String Lottery(Model model,@RequestParam String name,@RequestParam String phone){
	
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("name", name.trim());
		param.put("phone", phone.trim());
	 	Lottery l = lotteryService.queryLottery(param);
	 	if(l!=null){
	 		model.addAttribute("saved", 2);
	 	} else{
	 		Lottery lo = new Lottery();
	 		lo.setUsername(name);
	 		lo.setPhone(phone);
	 		lotteryService.saveLottery(lo);
			model.addAttribute("saved", 1);
	 	}
		return "pages/mobile/bind_user_o";
	}
	
	@RequestMapping(value="/mobile/lottery_start")
	public String choujiang(Model model){
		List<Lottery> los = lotteryService.queryLotteryList(0);
		model.addAttribute("lotteryList", los);
		return "pages/mobile/lottery";
	}
	
	@RequestMapping(value="/mobile/lottery_set")
	public String choujiangset(Model model,@RequestParam(required = false) Integer delete){
		if(delete!=null && delete==1){
			model.addAttribute("msg", "清除成功!");
		}
		return "pages/mobile/lottery_set";
	}
	
	@RequestMapping(value="/mobile/lottery_clear")
	public String choujiangclear(Model model){
		//List<Lottery> los = lotteryService.queryLotteryList();
		//model.addAttribute("lotteryList", los);
		lotteryService.clearLotteryList();
		
		return "redirect:lottery_set?delete=1";
	}
	
	@RequestMapping(value="/mobile/lottery_exp")
	public void choujiangexp(HttpServletRequest request,HttpServletResponse response,Model model){
		List<Lottery> los = lotteryService.queryLotteryList(null);
		try {
			String en = "摇奖报名信息.xls";
			String excelName = new String(en.getBytes("UTF-8"), "ISO-8859-1");
			response.reset();
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
			HSSFSheet sheet = work.createSheet("sheet1");
			HSSFCell cell;

			// 标题格式
			CellStyle cellStyle = work.createCellStyle();
			// 内容格式
			CellStyle dataStyle = work.createCellStyle();
			
			CellStyle conStyle = work.createCellStyle();

			// 内容加上边框
			//dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
			// dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			//dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
			// dataStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
			//dataStyle.setBorderRight(CellStyle.BORDER_THIN);
			// dataStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
			//dataStyle.setBorderTop(CellStyle.BORDER_THIN);
			// dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			// 标题居中
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			// 内容居中
			dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			dataStyle.setWrapText(true);
			
			conStyle.setAlignment(CellStyle.ALIGN_CENTER);
			conStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			conStyle.setWrapText(true);

			// 标题字体
			Font font = work.createFont();
			// 内容字体
			Font datafont = work.createFont();
			
			Font confont = work.createFont();

			font.setFontHeightInPoints((short) 20);
			font.setFontName("方正仿宋简体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);

			datafont.setFontHeightInPoints((short) 10);
			datafont.setFontName("方正仿宋简体");
			datafont.setColor(HSSFColor.RED.index);
			
			confont.setFontHeightInPoints((short) 10);
			confont.setFontName("方正仿宋简体");
			//datafont.setBoldweight(Font.BOLDWEIGHT_BOLD);

			// 把字体加入到格式中
			cellStyle.setFont(font);
			dataStyle.setFont(datafont);
			conStyle.setFont(confont);
			// 设置列宽度
			sheet.setColumnWidth(0, 20 * 256);
			sheet.setColumnWidth(1, 20 * 256);
			sheet.setColumnWidth(2, 20 * 256);
			/*sheet.setColumnWidth(3, 10 * 256);
			sheet.setColumnWidth(4, 10 * 256);
			sheet.setColumnWidth(5, 10 * 256);
			sheet.setColumnWidth(6, 10 * 256);
			sheet.setColumnWidth(7, 10 * 256);
			sheet.setColumnWidth(8, 12 * 256);
			sheet.setColumnWidth(9, 10 * 256);
			sheet.setColumnWidth(10, 10 * 256);
			sheet.setColumnWidth(11, 17 * 256);
			sheet.setColumnWidth(12, 10 * 256);
			sheet.setColumnWidth(13, 10 * 256);*/

			int row_index = 0;
				// 合并单元格
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 2));
				
				// 设置标题格
				HSSFRow row = sheet.createRow(row_index);
				row_index++;
				row.setHeightInPoints(30);
				cell = row.createCell(0);
				cell.setCellValue("中奖信息导出表");
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				row_index += 2;
				row = sheet.createRow(row_index);
				row_index++;
				row.setHeightInPoints(24);
				cell = row.createCell(0);
				cell.setCellValue("报名者");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(1);
				cell.setCellValue("手机号码");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);

				cell = row.createCell(2);
				cell.setCellValue("中奖状态");
				cell.setCellStyle(dataStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				
				
				for(Lottery l:los){
					row = sheet.createRow(row_index++);
					cell = row.createCell(0);
					cell.setCellValue(l.getUsername());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(conStyle);
					
					cell = row.createCell(1);
					cell.setCellValue(l.getPhone());
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(conStyle);
					
					cell = row.createCell(2);
					cell.setCellValue(l.getStatus()==1?"中奖":"未中奖");
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(conStyle);
					
					
				}
				os = response.getOutputStream();
				work.write(os);
				os.flush();
				os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//model.addAttribute("lotteryList", los);
		//return "pages/mobile/lottery";
	}
	
	@RequestMapping(value="/mobile/lottery_save")
	public @ResponseBody String chou(Model model,@RequestParam String phone){
		String[] p = phone.split(":");
		lotteryService.updateLottery(p[1]);
		//model.addAttribute("lotteryList", los);
		return "ok";
	}
}
