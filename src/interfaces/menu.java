
package interfaces;

import interfaces.Empleados;
import static interfaces.factura.txtNumFac;
import java.awt.Image;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JRViewer;

public class menu extends javax.swing.JFrame {

    public menu() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        pane1.setBorder(new ImagenFondo());
        ImageIcon icono= new ImageIcon(getClass().getResource("..//imagenes//carrito.png"));
        Image image = icono.getImage();
        this.setIconImage(image);
        }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuInicio = new javax.swing.JMenu();
        miVentas = new javax.swing.JMenuItem();
        miCompras = new javax.swing.JMenuItem();
        menuUsuarios = new javax.swing.JMenu();
        miUsuarios = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/servicios.png"))); // NOI18N
        menuInicio.setText("SERVICIOS");

        miVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/shop.png"))); // NOI18N
        miVentas.setText("VENTAS");
        miVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miVentasActionPerformed(evt);
            }
        });
        menuInicio.add(miVentas);

        miCompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/comprar.png"))); // NOI18N
        miCompras.setText("COMPRAS");
        miCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miComprasActionPerformed(evt);
            }
        });
        menuInicio.add(miCompras);

        jMenuBar1.add(menuInicio);

        menuUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/bd.png"))); // NOI18N
        menuUsuarios.setText("GESTION DE BASE");

        miUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/usuarios.png"))); // NOI18N
        miUsuarios.setText("Usuarios Sistema");
        miUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miUsuariosActionPerformed(evt);
            }
        });
        menuUsuarios.add(miUsuarios);

        jMenuBar1.add(menuUsuarios);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/reports.png"))); // NOI18N
        jMenu2.setText("REPORTES");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/empleados.png"))); // NOI18N
        jMenuItem1.setText("EMPLEADOS");
        jMenu2.add(jMenuItem1);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clientes.png"))); // NOI18N
        jMenuItem3.setText("CLIENTES");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/productos.png"))); // NOI18N
        jMenuItem4.setText("PRODUCTOS");
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/closeseeion.png"))); // NOI18N
        jMenu1.setText("CERRAR SESION");

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cambiaruser.png"))); // NOI18N
        jMenuItem2.setText("Cambiar Sesion");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/salir.png"))); // NOI18N
        jMenuItem6.setText("Salir");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pane1, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pane1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void miComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miComprasActionPerformed
    String cedula = this.getTitle().split(":")[1];
    compras c = new compras(cedula);
    menu.pane1.add(c);
    c.toFront();
    c.setVisible(true);
}//GEN-LAST:event_miComprasActionPerformed

private void miVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miVentasActionPerformed
    factura f = new factura();
    pane1.add(f);
    f.setLocation((menu.pane1.getWidth()/2-f.getWidth()/2), (menu.pane1.getHeight()/2-f.getHeight()/2));
    f.show();
    
}//GEN-LAST:event_miVentasActionPerformed

    private void miUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miUsuariosActionPerformed
        try {
            Empleados e = new Empleados();
            pane1.add(e);
            e.show();
           e.btGuardarU.setVisible(false);
        } catch (ParseException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_miUsuariosActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.dispose();
        acceso a = new acceso();
        a.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
//            Map parametro = new HashMap();
//            parametro.put("numFac", txtNumFac.getText());
            JasperReport reporte = JasperCompileManager.compileReport("src/Reportes/clientes.jrxml");
            JasperPrint print = JasperFillManager.fillReport(reporte,null, cc.conectar());
             //JasperViewer.viewReport(print, false);
             JInternalFrame interno = new JInternalFrame("Reporte");
            interno.getContentPane().add(new JRViewer(print));
            menu.pane1.add(interno);
            interno.pack();
            interno.setClosable(true);
            interno.setMaximizable(true);
            interno.setResizable(true);
            interno.setVisible(true);
            interno.setResizable(true);
            interno.setSize(menu.pane1.getSize());
}catch(Exception ex){
    JOptionPane.showMessageDialog(null, ex);
}

    }//GEN-LAST:event_jMenuItem3ActionPerformed

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
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new menu().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem6;
    public static javax.swing.JMenu menuInicio;
    public static javax.swing.JMenu menuUsuarios;
    public javax.swing.JMenuItem miCompras;
    public javax.swing.JMenuItem miUsuarios;
    public javax.swing.JMenuItem miVentas;
    public static javax.swing.JDesktopPane pane1;
    // End of variables declaration//GEN-END:variables
}
