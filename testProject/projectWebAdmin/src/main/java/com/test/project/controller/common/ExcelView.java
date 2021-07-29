package com.test.project.controller.common;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.redjframework.bean.PropertyUtil;

@Component
public class ExcelView extends AbstractXlsView {

	public static String EXCEL_DATA = "EXCEL_DATA";

	public static String EXCEL_HEADER = "EXCEL_HEADER";

	public static String EXCEL_KEYS = "EXCEL_KEYS";

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String[] headers = (String[]) model.get(EXCEL_HEADER);
		List<Object> datas = (List<Object>) model.get(EXCEL_DATA);
		String keys = (String) model.get(EXCEL_KEYS);

		response.setHeader("Content-Type","application/vnd.ms-excel;");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = sdf.format(new Date());
    	String excelFileName = java.net.URLEncoder.encode(fileName+".xls", "UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + excelFileName + ";");

		HSSFWorkbook wb = new HSSFWorkbook();

		try {
			HSSFCellStyle styleCenter = wb.createCellStyle();
			styleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleCenter.setBottomBorderColor(HSSFColor.BLACK.index);
			styleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleCenter.setLeftBorderColor(HSSFColor.BLACK.index);
			styleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleCenter.setRightBorderColor(HSSFColor.BLACK.index);
			styleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
			styleCenter.setTopBorderColor(HSSFColor.BLACK.index);
			styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
			styleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

			HSSFSheet sheet = wb.createSheet();

			int rownum = 0;

			for(String header: headers){
				HSSFRow row = sheet.createRow(rownum++);
				int cellnum = 0;
				String[] hdrs = header.split("[\\|,]");
				for(String h: hdrs){
					HSSFCell cell = row.createCell(cellnum++);
					cell.setCellStyle(styleCenter);
					cell.setCellValue(h);
				}
			}


			for(Object o: datas){
				HSSFRow row = sheet.createRow(rownum++);
				int cellnum = 0;
				for(String key: keys.split("[\\|,]")){
					HSSFCell cell = row.createCell(cellnum++);
					cell.setCellStyle(styleCenter);
					Object value = propertyValue(o, key);

					if(value == null)
						cell.setCellValue("");
					else
						cell.setCellValue(value.toString());
				}
			}

			wb.write(response.getOutputStream());
		} catch (IOException e) {
		}

	}

	private Object propertyValue(Object o, String key) throws Exception {
		if(o == null
				|| key == null)
			return null;

		key = key.trim();

		if(o instanceof Map)
			return ((Map)o).get(key);

		Class<?> cls = o.getClass();
		Method method = PropertyUtil.getPropertyReadMethod(cls, key);
		return method.invoke(o);
	}

	private static String num2Cell(int i, int j) {
		return CellReference.convertNumToColString(j-1) + i;
	}
}
