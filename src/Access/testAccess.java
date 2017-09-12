package Access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class testAccess {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ConnectAccessFile();
	}
	
	public static void ConnectAccessFile() throws Exception   
    {  
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  
        /** 
         * 直接连接access文件。 
         */  
        String dbur1 = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=D://用户目录//Documents//test.mdb";  
        Connection conn = DriverManager.getConnection(dbur1, "username", "");  
        Statement stmt = conn.createStatement();  
        ResultSet rs = stmt.executeQuery("select * from EMP");  
        while (rs.next()) {  
            System.out.println(rs.getString(1));  
        }  
        rs.close();  
        stmt.close();  
        conn.close();  
    }  

}
