package TestPOI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


public class CreateDocFile {
	
	private static String toDir = "C:\\Users\\Administrator\\Desktop\\Desktop\\ZCWJ\\";
	
	private static String toDocDir = "C:\\Users\\Administrator\\Desktop\\Desktop\\ZCWJ_DOC\\";
	
	private static String fromDir = "C:\\Users\\Administrator\\Desktop\\Desktop\\国发新\\";
	
	public static void main(String[] args) {
		readXml("C:\\Users\\Administrator\\Desktop\\Desktop\\国发.xlsx");
		System.out.println("-------------");
		//readXml("d:/test2.xls");
 	}
	public static void readXml(String fileName){
		boolean isE2007 = false;	//判断是否是excel2007格式
		if(fileName.endsWith("xlsx"))
			isE2007 = true;
		try {
			InputStream input = new FileInputStream(fileName);	//建立输入流
			Workbook wb  = null;
			//根据文件格式(2003或者2007)来初始化
			if(isE2007)
				wb = new XSSFWorkbook(input);
			else
				wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);		//获得第一个表单
			Iterator<Row> rows = sheet.rowIterator();	//获得第一个表单的迭代器
			while (rows.hasNext()) {
				Row row = rows.next();	//获得行数据
				Iterator<Cell> cells = row.cellIterator();	//获得第一行的迭代器
				String fromFileName = "";
				String toFileName = "";
				String wenHao = "";
				String wenDangMingCheng = "";
				while (cells.hasNext()) {
					Cell cell = cells.next();
					
					if(0 == cell.getColumnIndex()){
						fromFileName = cell.getStringCellValue();
					}else if (1 == cell.getColumnIndex()) {
						toFileName = cell.getStringCellValue()+".pdf";
					}else if (2 == cell.getColumnIndex()) {
						wenHao = cell.getStringCellValue();
					}else if (6 == cell.getColumnIndex()) {
						wenDangMingCheng = cell.getStringCellValue();
					}
				}
				XWPFDocument doc = new XWPFDocument();
				
				
				
				
				XWPFParagraph p2 = doc.createParagraph();
		        p2.setAlignment(ParagraphAlignment.CENTER);
		        p2.setBorderBetween(Borders.SINGLE);
		        p2.setVerticalAlignment(TextAlignment.TOP);
		        
		        
		        XWPFRun r2 = p2.createRun();
		        r2.setBold(true);
		        r2.setText("国务院办公厅文件");
		        r2.setBold(true);
		        r2.setFontFamily("方正小标宋_GBK");
		        r2.setFontSize(23);
		        
		        XWPFParagraph p3 = doc.createParagraph();
		        p3.setAlignment(ParagraphAlignment.CENTER);
		        p3.setBorderBottom(Borders.SINGLE);
		        p3.setVerticalAlignment(TextAlignment.TOP);
		        XWPFRun r3 = p3.createRun();
		        r3.setBold(false);
		        r3.setText(wenHao);
		        r3.setFontFamily("方正小标宋_GBK");
		        r3.setFontSize(12);
		        
		        XWPFParagraph p1 = doc.createParagraph();
		        p1.setAlignment(ParagraphAlignment.CENTER);
		        p1.setBorderBetween(Borders.SINGLE);
		        p1.setVerticalAlignment(TextAlignment.TOP);
		        XWPFRun r1 = p1.createRun();
		        r1.setBold(true);
		        r1.setText(toFileName.replaceAll(".pdf", ""));
		        r1.setBold(true);
		        r1.setFontFamily("方正小标宋_GBK");
		        r1.setFontSize(20);
		        
		        FileOutputStream out = new FileOutputStream(toDocDir+wenDangMingCheng.replaceAll(".pdf", ".doc"));
		        doc.write(out);
		        out.close();
				
				File fromfile = new File(fromDir+fromFileName);
				if(fromfile.exists() && fromfile.isFile()){
					fromfile.renameTo(new File(toDir+wenDangMingCheng));
				}else{
					File fromfile2 = new File(fromDir+fromFileName.replaceAll("〔", "[").replaceAll("〕", "]"));
					if(fromfile2.exists() && fromfile2.isFile()){
						fromfile2.renameTo(new File(toDir+wenDangMingCheng));
					}else{
						System.out.println(fromFileName+"*******************"+wenDangMingCheng);
					}
				}
				
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
