/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class Conexion {
     Connection connect=null;
    public Connection conectar()
    {
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //connect=DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.100:1521:XE","system","system");
            connect=DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.101:1521:XE","factura","factura");
            JOptionPane.showMessageDialog(null, "Se conecto a la base de datos");
        } catch (Exception ex) {
         JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return connect;
    }
    
}
