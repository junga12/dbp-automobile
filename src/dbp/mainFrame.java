
package dbp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class mainFrame extends javax.swing.JFrame {
    
    // to make a connection with database
    Connection con = MyConnection.makeConnection(); 
    
    JTextField[] tf = new JTextField[10];
    JLabel[] lf = new JLabel[10];
    
    public mainFrame() {
        initComponents();
        addNodestoTree();
        
        tf[0] = jTextField1;
        tf[1] = jTextField2;
        tf[2] = jTextField3;
        tf[3] = jTextField4;
        tf[4] = jTextField5;
        tf[5] = jTextField6;
        tf[6] = jTextField7;
        tf[7] = jTextField8;
        tf[8] = jTextField9;
        tf[9] = jTextField10;
        
        lf[0] = jLabel1;
        lf[1] = jLabel2;
        lf[2] = jLabel3;
        lf[3] = jLabel4;
        lf[4] = jLabel5;
        lf[5] = jLabel6;
        lf[6] = jLabel7;
        lf[7] = jLabel8;
        lf[8] = jLabel9;
        lf[9] = jLabel10;
        
        jTextField11.setText("");
    }
    
    private void claearTextFields() {
        int noc = jTable1.getColumnCount();
        for(int i = 0; i < noc; i++) {
                tf[i].setText(" ");
        }
    }
    
    private void temp() {
        String st = mySelectedNode();
        
        
        try {
            
        } catch(Exception e) {
            jTextArea1.append("Error in addAutomobile()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }

    // Search a record
    private void showSearch(String st) {
        // get the String that I want to find
        String searchString = jTextField11.getText();
        
        // set the appropriate Coloumn_Name according to the table
        String mColoumnName = "";
        if(st.equals("Automobile")) 
            mColoumnName = "model";
        else if(st.equals("Company")) 
            mColoumnName = "name";
        else if(st.equals("Customer")) 
            mColoumnName = "first_name";
        else if(st.equals("Department")) 
            mColoumnName = "name";
        else if(st.equals("Employee")) 
            mColoumnName = "first_name";
        else if(st.equals("Subcontractor")) 
            mColoumnName = "name";
        else jTextArea1.append("Error in showSearch()...." + "\n");
        
        String query = "select * from "+ st +" where " + mColoumnName + " like '%" + searchString + "%';" ;	
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            // execute select query and set to ResultSet and show the table
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            jTable1.setModel(rsToTableModel(rs));
            
            jTable1.setRowSelectionInterval(0, 0);
            
            jTextArea1.append("Finish the search..." + "\n");
            pst.close();
            rs.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in showSearch()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    // delete a record using ResultSet Method
    private void deleteCustomerRS() {
        int rownum = jTable1.getSelectedRow();
        String id = jTable1.getModel().getValueAt(rownum, 0).toString();
        String query = " select * from Customer";
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            
            rs.absolute(rownum+1);
            rs.deleteRow();
            rs.moveToCurrentRow();
            
            jTextArea1.append(" row is deleted..." + "\n");
            pst.close();
            rs.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in deleteCustomerRS()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    // delete a record by calling a stroed procedure
    private void deleteCustomerSP() {
        int rownum = jTable1.getSelectedRow();
        String id = jTable1.getModel().getValueAt(rownum, 0).toString();
        String query = "{call uspDeleteCustomer(?) }";
        CallableStatement cst = null;
        
        try {
            cst = con.prepareCall(query);
            cst.setString(1, id);
            cst.execute();
            
            jTextArea1.append(" row is deleted... " + "\n");
            cst.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in deleteCustomerSP()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }

    // delete using delete Statement
    private void deleteCustomer() {
        int rownum = jTable1.getSelectedRow();
        String id = jTable1.getModel().getValueAt(rownum, 0).toString();
        String query = "delete from Customer where C_number = ?";
        PreparedStatement pst = null;
        
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, id);
            pst.executeUpdate();
            jTextArea1.append(" row is deleted... " + "\n");
            pst.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in addAutomobile()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    // update a record using ResultSet method
    private void updateCustomerRS() {
        int rownum = jTable1.getSelectedRow();
        String query = "select * from Customer";
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            pst = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            
            rs.absolute(rownum+1);
            
            rs.updateString(2, jTextField2.getText().toString());
            rs.updateString(3, jTextField3.getText().toString());
            rs.updateString(4, jTextField4.getText().toString());
            rs.updateString(5, jTextField5.getText().toString());
            rs.updateString(6, jTextField6.getText().toString());
            
            rs.updateRow();
            rs.moveToCurrentRow();
            
            jTextArea1.append(" row is updated..." + "\n");
            pst.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in updateCustomerRS()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    
    // update a record calling stored procedure
    private void updateCustomerSP() {
        int rownum = jTable1.getSelectedRow();
        String id = jTable1.getModel().getValueAt(rownum, 0).toString();
        String query = "{call uspUpdateCustomer(?, ?, ?, ?, ?, ?) }";
        CallableStatement cst = null;
        
        try {
            cst = con.prepareCall(query);
            
            cst.setString(1, id);
            cst.setString(2, jTextField2.getText().toString());
            cst.setString(3, jTextField3.getText().toString());
            cst.setString(4, jTextField4.getText().toString());
            cst.setString(5, jTextField5.getText().toString());
            cst.setString(6, jTextField6.getText().toString());
            
            cst.execute();
            jTextArea1.append(" row is updated..." + "\n");
            cst.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in updateCustomerSP()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    
    // update record using update Statement
    private void updateAutomobileUS() {
        int rownum = jTable1.getSelectedRow();
        String id = jTable1.getModel().getValueAt(rownum, 0).toString();
        String query = "update Automobile set model = ?, color = ?, sale_price = ?, stock = ?, discount_rate = ?, original_price = ? where serial_number = ?";
        PreparedStatement pst = null;
        
        try {
            pst = con.prepareStatement(query);
            
            pst.setString(1, jTextField2.getText().toString());
            pst.setString(2, jTextField3.getText().toString());
            pst.setString(3, jTextField4.getText().toString());
            pst.setString(4, jTextField5.getText().toString());
            pst.setString(5, jTextField6.getText().toString());
            pst.setString(6, jTextField7.getText().toString());
            pst.setString(7, id);
            
            pst.executeUpdate();
            jTextArea1.append(" row is updated..." + "\n");
            pst.close();
                        
        } catch(Exception e) {
            jTextArea1.append("Error in updateFacultyUS()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    // insert record using RecultSet Method
    private void addAutomobileRS() {
        String st = mySelectedNode();
        String query = "select * from Automobile";
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            pst = con.prepareCall(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
            
            rs.moveToInsertRow();
            rs.updateString(1, jTextField1.getText());
            rs.updateString(2, jTextField2.getText());
            rs.updateString(3, jTextField3.getText());
            rs.updateString(4, jTextField4.getText());
            rs.updateString(5, jTextField5.getText());
            rs.updateString(6, jTextField6.getText());
            rs.updateString(7, jTextField7.getText());
            
            rs.insertRow();
            rs.moveToCurrentRow();
            
            jTextArea1.append(" row is added..." + "\n");
            
            pst.close();
            rs.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in addAutomobileRS()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
        
    }
    
    // insert Record calling a Stored Procedure
    private void addAutomobileSP() {
        String query = "{call uspAddAutomobile(?, ?, ? ,? ,? ,?)}";
        CallableStatement cst = null;
        
        try {
            cst = con.prepareCall(query);
            
            cst.setString(1, jTextField1.getText());
            cst.setString(2, jTextField2.getText());
            cst.setString(3, jTextField3.getText());
            cst.setString(4, jTextField4.getText());
            cst.setString(5, jTextField5.getText());
            cst.setString(6, jTextField6.getText());
            cst.setString(7, jTextField7.getText());
            
            cst.execute();
            jTextArea1.append(" row(s) are added..." + "\n");
            cst.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in addAutomobileSP()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    
    // insert/add into a record using insert into statement in any Table
    private void addRecord(String tname) {
        int num = 0;
        
        String query = "insert into " + tname + " values (";
        int noc = jTable1.getColumnCount();
        for(int i = 1; i < noc; i++)
            query += "?, ";
        query += "?)";
        
        String query2 = "select COLUMN_NAME, DATA_TYPE from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = '" + tname + "' ";
        
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        
        try {
            
            pst1 = con.prepareStatement(query2);
            rs = pst1.executeQuery();
            pst2 = con.prepareStatement(query);
            
            rs.next();
            for(int i =1; i <= noc; i++, rs.next()) {
                String cType = rs.getString("DATA_TYPE").toString();
                
                if(cType == "int")
                    pst2.setInt(i, Integer.parseInt(tf[i-1].getText().toString()));
                else if (cType == "numeric")
                    pst2.setDouble(i, Double.parseDouble(tf[i-1].getText().toString()));
                else if(cType == "float")
                    pst2.setFloat(i, Float.parseFloat(tf[i-1].getText().toString()));
                else
                    pst2.setString(i, tf[i-1].getText().toString());
            } 
            
            num = pst2.executeUpdate();
            jTextArea1.append(num + " row(s) are added..." + "\n");
            pst2.close();
            pst1.close();
            rs.close();
            
        } catch(Exception e) {
            jTextArea1.append("Error in addRecord()... in Table" + tname + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    // Insert/add a record using insert into statement
    private void addAutomobile() {
        int num = 0;
        String query = "insert into Automobile values(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = null;
        
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, jTextField1.getText());
            pst.setString(2, jTextField2.getText());
            pst.setString(3, jTextField3.getText());
            pst.setString(4, jTextField4.getText());
            pst.setString(5, jTextField5.getText());
            pst.setString(6, jTextField6.getText());
            pst.setString(7, jTextField7.getText());
            
            num = pst.executeUpdate();
            jTextArea1.append(num + " row(s) are added ..." + "\n");
            pst.close();
            
        } catch(SQLException e) {
            jTextArea1.append("Error in addAutomobile()...." + "\n");
            jTextArea1.append(e.getMessage() + "\n");
        }
    }
    
    // function to showData into the fields.
    private void showFields() {
        this.clearFields();
        
        int selectedRow = jTable1.getSelectedRow();
        int noc = jTable1.getColumnCount();
        for(int i = 0; i < noc; i++) {
            if(jTable1.getValueAt(selectedRow, i) == null)
                tf[i].setText("");
            else {
                tf[i].setText(jTable1.getValueAt(selectedRow, i).toString());
                tf[i].setVisible(true);
            }
            
            lf[i].setText(jTable1.getColumnName(i));
            lf[i].setVisible(rootPaneCheckingEnabled);
        }
    }
    
    // function to clear all fiedls
    private void clearFields() {
        for(int i = 0; i < tf.length; i++) {
            tf[i].setText(" ");
//            lf[i].setVisible(false);
            lf[i].setText(" ");
            lf[i].setVisible(false);
        }
        
    }
    
    // Function to move data from RS to jTable Model
    private TableModel rsToTableModel(ResultSet rs) {
        
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            // data for colums
            int noc = rsmd.getColumnCount();
            Vector cnames = new Vector();
            for(int i = 1; i <= noc; i++) {
                //rs는 1부터 시작
                cnames.addElement(rsmd.getColumnLabel(i));
            }
            
            // data for rows
            Vector rows = new Vector();
            while(rs.next()) {
                Vector newRow = new Vector();
                for(int i = 1; i <= noc; i++)
                    newRow.addElement(rs.getObject(i));
                
                rows.addElement(newRow);
            }
           return new DefaultTableModel(rows, cnames);
           
        } catch (Exception e) {
            System.err.println("  " + "Problem with rsToTableModel()...");
            return  null;
        }
    }
    
    // show Data into a jTable component upon selection of a tree node
    private void showData(String st) {
        String query = "  select * from "+st+"  ";
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try{
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            jTable1.setModel(rsToTableModel(rs));
            
            jTable1.setRowSelectionInterval(0, 0);
            
        } catch(Exception e) {
            System.err.println(" " + "problem with showData() function");
        }
    }
    
    // fucntion selects the caption of the selected leaf node
    private String mySelectedNode() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
        String ns = node.getUserObject().toString();
        return ns;
    }

    // function to modify the nodes in the jTree component
    private void addNodestoTree() {
     
        DefaultTreeModel treeModel = (DefaultTreeModel)jTree1.getModel();
        
       
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Automobile");
        DefaultMutableTreeNode tables = new DefaultMutableTreeNode("Tables");
        DefaultMutableTreeNode report = new DefaultMutableTreeNode("Report");
        DefaultMutableTreeNode Utilities = new DefaultMutableTreeNode("Utilities");
        DefaultMutableTreeNode about = new DefaultMutableTreeNode("About");
        
        
        treeModel.setRoot(root);
        root.add(tables);
        root.add(report);
        root.add(Utilities);
        root.add(about);
        
        
        DefaultMutableTreeNode t1 = new DefaultMutableTreeNode(Tables.Automobile);
        DefaultMutableTreeNode t2 = new DefaultMutableTreeNode(Tables.Company);
        DefaultMutableTreeNode t3 = new DefaultMutableTreeNode(Tables.Customer);
        DefaultMutableTreeNode t4 = new DefaultMutableTreeNode(Tables.Department);
        DefaultMutableTreeNode t5 = new DefaultMutableTreeNode(Tables.Employee);
        DefaultMutableTreeNode t6 = new DefaultMutableTreeNode(Tables.Subcontractor);
        
        tables.add(t1);
        tables.add(t2);
        tables.add(t3);
        tables.add(t4);
        tables.add(t5);
        tables.add(t6);
        
        treeModel.reload();
    }   
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField11 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));

        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setText("New");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Delete");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Print");

        jButton6.setText("Search");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTextField11.setText("jTextField11");
        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jPanel3.setBackground(new java.awt.Color(153, 255, 255));
        jPanel3.setBorder(new javax.swing.border.MatteBorder(null));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(204, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setText("jTextField2");

        jTextField3.setText("jTextField3");

        jTextField4.setText("jTextField4");

        jTextField5.setText("jTextField5");

        jTextField6.setText("jTextField6");

        jTextField7.setText("jTextField7");

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");

        jLabel6.setText("jLabel6");

        jLabel7.setText("jLabel7");

        jTextField8.setText("jTextField8");

        jTextField9.setText("jTextField9");

        jTextField10.setText("jTextField10");

        jLabel8.setText("jLabel8");

        jLabel9.setText("jLabel9");

        jLabel10.setText("jLabel10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(104, 104, 104)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addComponent(jTextField9)
                    .addComponent(jTextField6)
                    .addComponent(jTextField5)
                    .addComponent(jTextField4)
                    .addComponent(jTextField3)
                    .addComponent(jTextField2)
                    .addComponent(jTextField7)
                    .addComponent(jTextField1)
                    .addComponent(jTextField8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addContainerGap())
        );

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // New botton
        claearTextFields();
    }//GEN-LAST:event_jButton1ActionPerformed


    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        String ns = mySelectedNode();
        showData(ns);
        showFields();
    }//GEN-LAST:event_jTree1ValueChanged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        this.showFields();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // add boutton
        
        String st = mySelectedNode();
//        addRecord(st);
        if(st == Tables.Automobile.toString())
//            addAutomobile();
//            addAutomobileSP();
            addAutomobileRS();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Update button
        
        String st = mySelectedNode();
        if(st == Tables.Automobile.toString())
            updateAutomobileUS();
        if(st == Tables.Customer.toString())
//            updateCustomerSP();
            updateCustomerRS();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // delete button
        String st = mySelectedNode();
        if(st == Tables.Customer.toString())
//            deleteCustomer();
//            deleteCustomerSP();
            deleteCustomerRS();
        else
            deleteCustomer();
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Search Button
        
        String ns = mySelectedNode();
        showSearch(ns);
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
