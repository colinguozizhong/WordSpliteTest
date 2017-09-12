

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jsoup.Connection;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.org.apache.bcel.internal.generic.Select;

public class Ba {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		// 开始插入数据
		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		String URL = "jdbc:oracle:thin:@192.168.20.190:1521:orcl"; // orcl为数据库的SID
		String Username = "dss"; // 用户名
		String Password = "dss"; // 密码
		java.sql.Connection con = DriverManager.getConnection(URL, Username,
				Password);
		
		PreparedStatement pstmt = con
				.prepareStatement("select url,fa_lv_fa_gui_id from XZZF_J_FLFG where  DELETEFLAG='01' and url is not null");
		ResultSet rs = pstmt.executeQuery();
		
		String urlString;
		
		int count = 0;
		
		while(rs.next()) { 
			  urlString = rs.getString(1);
			  String id = rs.getString(2);
			  int pos = 0;
			  
			 // System.out.println(urlString);
			  Connection connSub = null;
				Response responseSub = null;
				try {
				//	System.out.println("1");
					connSub = Jsoup.connect(urlString);
				//	System.out.println("2");
					Request requestSub = connSub.request();
					requestSub.timeout(9999999);
				//	System.out.println("3");
					responseSub = connSub.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			//	System.out.println("4");
				Document docSub = responseSub.parse();
				// 得到子页面所有的width为100%的table标签
			//	System.out.println("5");
				Elements tables = docSub.select("table[width='100%']");

			//	System.out.println("6");
				// 以空格分割内容直接分割出所有网页的内容
				// new SimpleDateFormat("yyyy年yy月dd日").parse();
				String[] splitTabStrs = tables.get(0).text().split(" ");
				List<String> splitTabList = Arrays.asList(splitTabStrs);
				Map<String, Object> map = new HashMap<String, Object>();
				int index = -1;
				index = splitTabList.indexOf("颁布日期：");
				if(splitTabStrs[index + 1].trim().contains("年")){
					map.put("FA_BU_RI_QI", new SimpleDateFormat("yyyy年MM月dd日")
					.parse(splitTabStrs[index + 1].replace(" ", "").trim()));
				}else{
					map.put("FA_BU_RI_QI", null);
				}
				
				index = splitTabList.indexOf("施行日期：");
				if(splitTabStrs[index + 1].trim().contains("年")){
					map.put("SHI_SHI_RI_QI", new SimpleDateFormat("yyyy年MM月dd日")
					.parse(splitTabStrs[index + 1].replace(" ", "").trim()));
				}else{
					map.put("SHI_SHI_RI_QI", null);
				}
				
				//System.out.println("第" + (++pos) + "行");
				/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sql = "update XZZF_J_FLFG set FA_BU_RI_QI='";
				sql = sql + sdf.format((Date) map.get("FA_BU_RI_QI"));
				sql = sql + "',SHI_SHI_RI_QI='" + sdf.format((Date) map.get("SHI_SHI_RI_QI"));
				sql = sql + "',DELETEFLAG='00' where fa_lv_fa_gui_id = '"+id+"';";
				System.out.println(sql);*/
				System.out.println(count);count++;
				PreparedStatement pstmt2 = con
						.prepareStatement("update XZZF_J_FLFG set FA_BU_RI_QI=?,SHI_SHI_RI_QI=?,DELETEFLAG='00',UPDATETIME=sysdate where fa_lv_fa_gui_id = ? ");
				if(null == map.get("FA_BU_RI_QI")){
					pstmt2.setTimestamp(1,null);
				}else{
					pstmt2.setTimestamp(1,
							new Timestamp(((Date) map.get("FA_BU_RI_QI")).getTime()));
				}
				if(null == map.get("SHI_SHI_RI_QI")){
					pstmt2.setTimestamp(2,null);
				}else{
					pstmt2.setTimestamp(2,
							new Timestamp(((Date) map.get("SHI_SHI_RI_QI")).getTime()));
				}
				
				pstmt2.setString(3,id);
				pstmt2.execute();
				pstmt2.close();
				
				System.gc();
			/*	docSub = null;
				tables = null;
				splitTabStrs = null;
				splitTabList = null;
				map = null;*/
				
				/*if(count>200){
					count = 0;
					System.gc();
					System.out.println("垃圾回收");
				}else{
					count ++;
				}*/
				// 遍历每一个要采集的子页面的链接并进行数据采集
			} 
		pstmt.close();
		con.close();
		System.out.println("结束");
	}
}
