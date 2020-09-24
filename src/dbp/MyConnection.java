
package dbp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        
        String strurl = "jdbc:sqlserver://DESKTOP-95BLMHF\\SQLEXPRESS:1433;databaseName=Automobile";
        // To make a connection
        try { 
            
            con = DriverManager.getConnection(strurl, "pjunga", "junga");
            JOptionPane.showMessageDialog(null, "Connected...");
            
            return con;
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Failed");    
            
            return null;
        }
        
    }
    
}
