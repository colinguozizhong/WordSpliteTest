import java.io.File;
import java.io.FileInputStream;
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


public class renameFile {
	
	private static String toDir = "C:\\Users\\Administrator\\Desktop\\Desktop\\ZCWJ\\";
	
	private static String fromDir = "C:\\Users\\Administrator\\Desktop\\Desktop\\皖政办秘\\";
	
	public static void main(String[] args) {
		readXml("C:\\Users\\Administrator\\Desktop\\Desktop\\皖政办秘.xlsx");
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
				while (cells.hasNext()) {
					Cell cell = cells.next();
					
					if(0 == cell.getColumnIndex()){
						fromFileName = cell.getStringCellValue();
					}else if (1 == cell.getColumnIndex()) {
						toFileName = cell.getStringCellValue()+".doc";
					}else if (2 == cell.getColumnIndex()) {
						wenHao = cell.getStringCellValue();
					}
				}
				
				
				File fromfile = new File(fromDir+fromFileName);
				if(fromfile.exists() && fromfile.isFile()){
					//System.out.println(toDir+toFileName+" exist");
					fromfile.renameTo(new File(toDir+toFileName));
				}else{
					//System.out.println(fromDir+fromFileName+" not exist");
					File fromfile2 = new File(fromDir+fromFileName.replaceAll("〔", "[").replaceAll("〕", "]"));
					if(fromfile2.exists() && fromfile2.isFile()){
						fromfile2.renameTo(new File(toDir+toFileName));
					}else{
						System.out.println(fromFileName+"*******************"+toFileName);
					}
					
				}
				
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
