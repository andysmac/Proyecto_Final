/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facturacion;

import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class Facturacion {
    public static void main(String[] args) {
        try {
            interfaces.acceso f = interfaces.acceso.class.newInstance();
            f.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se inicializo correctamente" + ex);
        }
    }
}
