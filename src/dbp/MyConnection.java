
package dbp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
 
public class MyConnection {
    
    public static Connection con = null;
    
    public static Connection makeConnection() {
        
        // Load and register the Driver
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            JOptionPane.showMessageDialog(null, "Dvien is loaded and Registered");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Dvien is a problem is loaded and Registered");
        }
        
        Properties properties = new Properties();

	File file = new File("src/dbp/dbproperties.properties");
        String dburl = null, dbid = null, dbpw = null;
        try {
                //Jdbc002_PropertiesTest 클래스의 위치에 properties 파일 저장
                properties.load(new FileInputStream(file.getAbsolutePath()));
                dburl = properties.getProperty("dburl");
                dbid = properties.getProperty("dbid");
                dbpw = properties.getProperty("dbpw"); 
                
        } catch (IOException e) {
                System.out.println("fail to read properties:" + e);
        }
        
        // To make a connection
        try { 
            
            con = DriverManager.getConnection(dburl, dbid, dbpw);
            JOptionPane.showMessageDialog(null, "Connected...");
            
            return con;
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Failed");    
            
            return null;
        }
        
    }
    
}
