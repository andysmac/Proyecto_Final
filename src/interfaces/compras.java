/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import static interfaces.factura.txtCedula;
import static interfaces.factura.txtNombre;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author THAMBATO1
 */
public class compras extends javax.swing.JInternalFrame {
VerificarCedula c = new VerificarCedula();
    SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yy");
    
    int cod = 0;
    String cedulap;
    //Thread h1;

    
    public Conexion cc = new Conexion();
    public Connection cn = cc.conectar();

    float n = 0;
    float iva = 0;
    float total = 0;
    int j = 0;
    float porcentaje = 0.14f;

    /**
     * Creates new form compras
     */
    public compras(String id) {
        initComponents();
         this.setLocation((menu.pane1.getWidth()/2-this.getWidth()/2), (menu.pane1.getHeight()/2-this.getHeight()/2));
        //h1 = new Thread(this);
        //h1.start();  
        txtUsuId.setEnabled(false);
        txtNumFac.setEnabled(false);
        txtIva.setEditable(false);
        txtsub.setEditable(false);
        txtTotal.setEditable(false);
        Calendar cal = Calendar.getInstance();
        txtUsuId.setEditable(true);
        Calendar c2 = new GregorianCalendar();
        txtFecha.setCalendar(c2);
        txtFecha.setEnabled(false);
        cargarCodigo();
        txtUsuId.setText(id);
        btnEditar.setEnabled(false);
        btnAgregar.setEnabled(false);
        btnEliminarP.setEnabled(false);
        btnCompra.setEnabled(false);
    }
public void limpiar() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDireccion.setText("");
        txtNumFac.setText("");
        txtIva.setText("");
        txtsub.setText("");
        txtTotal.setText("");
    }

    public void controlSoloLetras(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        // if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') || c == ' ') {
        if (Character.isDigit(c)) {
            evt.consume();
        }
    }

    public void controlDireccion(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '0' || c > '9') && c != '-' || (c == KeyEvent.VK_SPACE)) {
            evt.consume();
        }
    }

//    public compras(String cedula, String nombre, String apellido, String direccion, String telefono) {
//        initComponents();
//        txtCedula.setText(cedula);
//        txtNombre.setText(nombre);
//        txtApellido.setText(apellido);
//        txtDireccion.setText(direccion);
//    }
//    public void numFc() {
//        String sql = "SELECT * FROM FACTURAS ";
//        int numFac = 1;
//        try {
//            Statement psw = cn.createStatement();
//            ResultSet rs = psw.executeQuery(sql);
//            while (rs.next()) {
//                numFac++;
//            }
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "error en el numero de factura");
//        }
//        txtNumFac.setText(String.valueOf(numFac));
//    }
//    public Date convertirFecha(String fech) throws ParseException {
//        java.sql.Date sqlDate = null;
//        try {
//
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//            java.util.Date utilDate = formatter.parse(fech);
//            sqlDate = new java.sql.Date(utilDate.getTime());
//            return sqlDate;
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return sqlDate;
//        }
//    }
    public void calculoSubTotales(String compra) {
        float subtotal = 0;
        float iva = 0, porcentaje = 0.14f;
        float total = 0;
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql = "SELECT SUM(SUBT) "
                + "FROM SUB_TEMP "
                + "WHERE NUM_COMP=" + compra + " "
                + "GROUP BY NUM_COMP";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            ResultSet rs = psd.executeQuery();
            while (rs.next()) {
                subtotal = rs.getFloat(1);
                txtsub.setText(String.valueOf(subtotal));
            }
            iva = subtotal * porcentaje;
            txtIva.setText(String.valueOf(iva));
            total = subtotal + iva;
            txtTotal.setText(String.valueOf(total));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se puedo totalizar " + ex);
        }
    }

    public void cargarCodigo() {
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql = "SELECT COMPRASEC.NEXTVAL FROM DUAL ";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            ResultSet rs = psd.executeQuery();
            while (rs.next()) {
                cod = rs.getInt(1);
                txtNumFac.setText(String.valueOf(cod));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se puede seleccionar el codigo " + ex);
        }
    }

    public void guardarCompra() {
        if (txtUsuId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Usted No se a Identificado como Usuario");
            txtUsuId.requestFocus();
        } else if (txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No puede ingresar una facutura sin cedula");
            txtCedula.requestFocus();
        } else {
            if (txtFecha.getDate() == null) {
                JOptionPane.showMessageDialog(null, "No puede ingresar una facutura sin Fecha");
                txtFecha.requestFocus();
            }
            if (txtCedula.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se puede ingresar factura sin proveedor");
                txtCedula.requestFocus();
            } else {
                String Usu_Id;
                Double sub, iva, tot;
                String Fac_Cedula;
                String sql = "";
                Fac_Cedula = txtCedula.getText();
                Usu_Id = txtUsuId.getText();
//                    sub = Double.valueOf(txtsub.getText());
                //                  iva = Double.valueOf(txtIva.getText());
                //                tot = Double.valueOf(txtTotal.getText());
                try {
                    sql = "INSERT INTO COMPRAS(NUM_FAC_C, FEC_FAC_C, CI_PRO_C,CI_EMP_C,SUB_TOT_C,IVA_C,TOTAL_PAGAR_C)"
                            + "values(?,?,?,?,?,?,?)";
                    //cargarCodigo();
                    PreparedStatement psw = cn.prepareStatement(sql);
                    psw.setInt(1, cod);
                    psw.setString(2, fecha.format(txtFecha.getDate()));
                    psw.setString(3, Fac_Cedula);
                    psw.setString(4, Usu_Id);
                    psw.setDouble(5, Double.valueOf(txtsub.getText()));
                    psw.setDouble(6, Double.valueOf(txtIva.getText()));
                    psw.setDouble(7, Double.valueOf(txtTotal.getText()));
                    int n = psw.executeUpdate();
                    if (n > 0) {
                        JOptionPane.showMessageDialog(null, "Se inserto correctamente la factura");
                        bloq();
                    }
                    //GuardarDetalle();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "No se inserto corretamente la factura" + ex);
                }
            }
        }
    }

    public void cargarTablaProducto() {
        
        String cantidad = txtCantidad.getValue().toString();
        float suma = 0;
        suma = (Float.valueOf(txtPrecio.getText())) * (Float.valueOf(cantidad));
        if (txtCantidad.getValue().equals(0)) {
            JOptionPane.showMessageDialog(null, "DEBE INGRESAR CANTIDAD");
            txtCantidad.requestFocus();
        } else {
            DefaultTableModel temp = (DefaultTableModel) tblDetalle.getModel();            
            Object nuevo[] = new Object[5];           
            temp.addRow(nuevo);
            
            tblDetalle.setValueAt(txtCodigoP.getText(), j, 0);
            tblDetalle.setValueAt(txtNombreP.getText(), j, 1);
            tblDetalle.setValueAt(txtPrecio.getText(), j, 2);
            tblDetalle.setValueAt(cantidad, j, 3);
            tblDetalle.setValueAt(String.valueOf(suma), j, 4);
            
            j++; 
            
            Valores();
            
            int s = JOptionPane.showConfirmDialog(null, "Desea Ingresar Otro Producto");
            if (s == 0) {
            txtCodigoP.setText("");
            txtNombreP.setText("");
            txtPrecio.setText("");
            txtCantidad.setValue(0);
            txtCodigoP.requestFocus();
        }
        if (s == 1) {
        txtCodigoP.setText("");
        txtNombreP.setText("");
        txtPrecio.setText("");
        txtCantidad.setValue(0);
        btnAgregar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnCompra.setEnabled(true);
        }               
        }
    }

    public void Valores() {
        float porcentaje = 0.14f;
        n = n + Float.valueOf(tblDetalle.getValueAt(tblDetalle.getRowCount()-1, 4).toString());
        txtsub.setText(String.valueOf(n));
        //txtsub.setText("hola");
        iva = n * porcentaje;
        txtIva.setText(String.valueOf(iva));
        total = n + iva;
        txtTotal.setText(String.valueOf(total));
    }
    
   
    
    

    public void bloq() {
        txtCedula.setEnabled(false);
        txtNombre.setEnabled(false);
        txtApellido.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtRUCEmp.setEnabled(false);

        txtUsuId.setEnabled(false);
        txtFecha.setEnabled(false);
    }

    public void CargarCeP() {
        if (txtCedula.getText().length() <= 9) {
            txtNombre.setText("");
            txtApellido.setText("");
            txtDireccion.setText("");
            txtTelefono.setText("");
            txtRUCEmp.setText(" ");
        } else if (txtCedula.getText().length() == 10) {

            String Dato = txtCedula.getText();
            String sql = "select * from proveedores  where ci_pro like '%" + Dato + "%'";
            boolean existe = false;
            try {
                Statement psw = cn.createStatement();
                ResultSet rs = psw.executeQuery(sql);
                while (rs.next()) {
                    existe = true;
                    if (existe == true) {
                        txtNombre.setText(rs.getString("nom1_pro") + " " + rs.getString("nom2_pro"));
                        txtApellido.setText(rs.getString("ape1_pro") + " " + rs.getString("ape2_pro"));
                        txtTelefono.setText(rs.getString("tel_pro"));
                        txtDireccion.setText(rs.getString("dir_pro"));
                        txtRUCEmp.setText(rs.getString("RUC_EMR_PR"));
                        txtCedula.setEditable(false);
                        btnEditar.setEnabled(true);
                        txtCodigoP.setEnabled(true);
                        txtCodigoP.requestFocus();
//                 txtBuscarNombrePro.setEnabled(true);  
//                 Tproductos.setEnabled(true);
                    }
                }
                if (existe == false) {
                    proveedores p = new proveedores();
                    menu.pane1.add(p);
                    p.show();
                    this.dispose();
                }
            } catch (Exception ex) {

            }
        }
    }

    public void ProductoV() {
        if (txtCodigoP.getText().length() <= 3) {
            txtNombreP.setText("");
            txtPrecio.setText("");
        } else if (txtCodigoP.getText().length() == 13) {
            String Dato = txtCodigoP.getText();
            String sql = "select * from productos where cod_prod like'%" + Dato + "%'";
            boolean existe = false;
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                Statement psw = cn.createStatement();
                ResultSet rs = psw.executeQuery(sql);
                while (rs.next()) {
                    existe = true;
                    if (existe == true) {
                        txtNombreP.setText(rs.getString("nom_prod"));
                        txtPrecio.setText(rs.getString("prexuc_prod"));                    
                    }
                    txtCantidad.requestFocus();
                }
                if (existe == false) {
                    //JOptionPane.showMessageDialog(null, "El producto no existe");
                    productos p = new productos();
                    menu.pane1.add(p);
                    p.show();
                    this.dispose();
                    productos.btGuardar.setEnabled(true);
                    productos.btActualizar.setEnabled(true);
                    productos.btCancelar.setEnabled(true);
                    productos.txtFecha.setEnabled(true);
                    productos.txtMarca.setEnabled(true);
                    productos.txtNombre.setEnabled(true);
                    productos.txtPreCom.setEnabled(true);
                    productos.txtPreVen.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error");
            }
        }
    }

//    public void cargarP() {
//        Conexion cc = new Conexion();
//        Connection cn = cc.conectar();
//        //WHERE CI_PRO ='" + txtCedula.getText() + "'
//        String sql = "SELECT CI_PRO FROM PROVEEDORES WHERE CI_PRO ='" + txtCedula.getText() + "'";
//        try {
//            PreparedStatement psd = cn.prepareStatement(sql);
//            ResultSet rs = psd.executeQuery();
//            while (rs.next()) {
//                cedulap = rs.getString("CI_PRO");
//                if (cedulap.isEmpty()) {
//                    cedulap = "n";
//                } else {
//                    cedulap = "s";
//                }
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "ERROR: " + ex);
//        }
//    }
    
    public void GuardarDetalle() {
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql, producto;
        String cant;
        int codi = Integer.valueOf(txtNumFac.getText());
        sql = "INSERT INTO det_compras(NUM_FAC_PC,COD_PROD_PC,CAN_PROD) VALUES(?,?,?)";
        for (int i = 0; i < tblDetalle.getRowCount(); i++) {

            
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setInt(1, codi);
                psd.setString(2, String.valueOf(tblDetalle.getValueAt(i, 0)));
                psd.setString(3, String.valueOf(tblDetalle.getValueAt(i, 3)));
                int n = psd.executeUpdate();
                if (n > 0) {
                    //JOptionPane.showMessageDialog(null, "Compra guardada con éxito");
                    //cargarTablaProducto("");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        //JOptionPane.showMessageDialog(null, tblDetalle.getRowCount());
    }


    
    public void EliminarDetalle() {
        
        DefaultTableModel model = (DefaultTableModel) tblDetalle.getModel();
        
        int a = tblDetalle.getSelectedRow();
        Double x=Double.valueOf(tblDetalle.getValueAt(a,4).toString());
        
        //JOptionPane.showMessageDialog(null, x);
        
            
        
      //int x = tblDetalle.getRowCount();
        if (a < 0) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar una fila de la tabla");
        } else {             
         int confirmar = JOptionPane.showConfirmDialog(null,
         "Esta seguro que desea Eliminar el registro? ");
         if (JOptionPane.OK_OPTION == confirmar) {
               
             double z=Double.valueOf(txtsub.getText())-x;
             double b=z*porcentaje;
             double c=z+b;
             txtsub.setText(String.valueOf(z));
             txtIva.setText(String.valueOf(b));
             txtTotal.setText(String.valueOf(c));   
             n=Float.valueOf(txtsub.getText());
             iva=Float.valueOf(txtIva.getText());
             total=Float.valueOf(txtTotal.getText());
             
                model.removeRow(a);    
                
                JOptionPane.showMessageDialog(null,
                        "Registro Eliminado");
                j--;
               
                //n=0;
                //n=Integer.valueOf(txtsub.getText())-Integer.valueOf(x);
                //txtsub.setText(String.valueOf(n));
                //JOptionPane.showMessageDialog(null, n);
            
        }    
             
             
         }
            
                      
            

    }
    
    private void limpiarT(){
        DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();
        int r=tblDetalle.getRowCount();
        for(int i=0;i<r;i++){
        modelo.removeRow(0);
        total = 0;
        iva=0;
        n=0;
        j--;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtNumFac = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtUsuId = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtsub = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtIva = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtRUCEmp = new javax.swing.JTextField();
        btnEditar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtCodigoP = new javax.swing.JTextField();
        txtNombreP = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        btnAgregar = new javax.swing.JButton();
        btnCompra = new javax.swing.JButton();
        btnEliminarP = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        txtFecha = new com.toedter.calendar.JDateChooser();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DATOS COMPRA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Num");

        txtNumFac.setEnabled(false);
        txtNumFac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumFacActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Fecha");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Cajero");

        txtUsuId.setEnabled(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "NOMBRE", "PRECIOU", "CANTIDAD", "TOTAL"
            }
        ));
        jScrollPane1.setViewportView(tblDetalle);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Subtotal");

        txtsub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsubActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("IVA");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Total");

        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel5))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtsub, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(172, 172, 172)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsub, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DATOS PROVEEDOR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Cedula");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Nombres");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Apellidos");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Direccion");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Telefono");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("RUC Empresa");

        txtCedula.setEnabled(false);
        txtCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaActionPerformed(evt);
            }
        });
        txtCedula.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCedulaFocusLost(evt);
            }
        });
        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCedulaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaKeyTyped(evt);
            }
        });

        txtNombre.setEditable(false);
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtApellido.setEditable(false);
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtApellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        txtDireccion.setEditable(false);
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });

        txtTelefono.setEditable(false);

        txtRUCEmp.setEditable(false);

        btnEditar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/button-rotate-ccw (1).png"))); // NOI18N
        btnEditar.setText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jLabel10))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtRUCEmp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTelefono, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEditar)
                .addGap(89, 89, 89))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRUCEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEditar)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DATOS PRODUCTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Codigo ");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Nombre");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Precio Unitario");

        txtCodigoP.setEnabled(false);
        txtCodigoP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoPActionPerformed(evt);
            }
        });
        txtCodigoP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoPKeyReleased(evt);
            }
        });

        txtNombreP.setEditable(false);

        txtPrecio.setEditable(false);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Cantidad");

        txtCantidad.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCantidad.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNombreP)
                                .addComponent(txtCodigoP, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtCodigoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        btnAgregar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/AGRE.png"))); // NOI18N
        btnAgregar.setText("AGREGAR");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnCompra.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/FACTURITA.png"))); // NOI18N
        btnCompra.setText("COMPRAR");
        btnCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompraActionPerformed(evt);
            }
        });

        btnEliminarP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEliminarP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/BASU.png"))); // NOI18N
        btnEliminarP.setText("ELIMINAR PRODUCTO");
        btnEliminarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("PRODUCTOS COMPRADOS");

        btnNuevo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/New Folder_1.png"))); // NOI18N
        btnNuevo.setText("NUEVA COMPRA");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnEliminarP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCompra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(55, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCompra)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarP))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        txtFecha.setEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtNumFac, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(479, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtFecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtNumFac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumFacActionPerformed

    }//GEN-LAST:event_txtNumFacActionPerformed

    private void txtsubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsubActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsubActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed

    }//GEN-LAST:event_txtCedulaActionPerformed

    private void txtCedulaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaFocusLost
        // TODO add your handling code here:
        c.validacion(evt);
    }//GEN-LAST:event_txtCedulaFocusLost

    private void txtCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyReleased
        CargarCeP();
    }//GEN-LAST:event_txtCedulaKeyReleased

    private void txtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyTyped
        // TODO add your handling code here:
        c.controlCaracteres(evt);
    }//GEN-LAST:event_txtCedulaKeyTyped

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped

    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoKeyReleased

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped

    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped

    }//GEN-LAST:event_txtDireccionKeyTyped

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        proveedores p = new proveedores();
        menu.pane1.add(p);
        p.show();
        proveedores.txtCedula.setText(txtCedula.getText());
        proveedores.txtNombre.setText(txtNombre.getText());
        proveedores.txtApellido.setText(txtApellido.getText());
        proveedores.txtDireccion.setText(txtDireccion.getText());
        proveedores.txtTelefono.setText(txtTelefono.getText());
        proveedores.txtRuc.setText(txtRUCEmp.getText());
        proveedores.btnIngresarProveedor.setEnabled(false);
        proveedores.btActualizar.setEnabled(false);
        proveedores.btEliminar.setEnabled(false);
        proveedores.btCancelar.setEnabled(false);

        this.dispose();
        compras.txtCodigoP.setEnabled(true);
        compras.btnNuevo.setEnabled(false);
        compras.btnAgregar.setEnabled(true);
    }//GEN-LAST:event_btnEditarActionPerformed

    private void txtCodigoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoPActionPerformed

    private void txtCodigoPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoPKeyReleased
        ProductoV();
    }//GEN-LAST:event_txtCodigoPKeyReleased

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        cargarTablaProducto();
        btnEliminarP.setEnabled(true);
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompraActionPerformed
        guardarCompra();
        GuardarDetalle();
        tblDetalle.setEnabled(false);
        txtsub.setText("");
        txtIva.setText("");
        txtTotal.setText("");
        btnNuevo.setEnabled(true);
        btnAgregar.setEnabled(false);
        btnCompra.setEnabled(false);
        btnEliminarP.setEnabled(false);
        limpiarT();
        //calculoSubTotales(txtNumFac.getText());
    }//GEN-LAST:event_btnCompraActionPerformed

    private void btnEliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPActionPerformed
        EliminarDetalle();
        //calculoSubTotales(txtNumFac.getText());
    }//GEN-LAST:event_btnEliminarPActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        txtCedula.setEnabled(true);
        txtCedula.requestFocus();
        txtCedula.setEditable(true);
        btnNuevo.setEnabled(false);
        btnAgregar.setEnabled(true);
        txtCedula.setText("");
        txtApellido.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtRUCEmp.setText("");
        limpiarT();
    }//GEN-LAST:event_btnNuevoActionPerformed

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
            java.util.logging.Logger.getLogger(compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new compras().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnAgregar;
    public static javax.swing.JButton btnCompra;
    public static javax.swing.JButton btnEditar;
    public static javax.swing.JButton btnEliminarP;
    public static javax.swing.JButton btnNuevo;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblDetalle;
    public static javax.swing.JTextField txtApellido;
    private javax.swing.JSpinner txtCantidad;
    public static javax.swing.JTextField txtCedula;
    public static javax.swing.JTextField txtCodigoP;
    public static javax.swing.JTextField txtDireccion;
    private com.toedter.calendar.JDateChooser txtFecha;
    private javax.swing.JTextField txtIva;
    public static javax.swing.JTextField txtNombre;
    public static javax.swing.JTextField txtNombreP;
    public static javax.swing.JTextField txtNumFac;
    public static javax.swing.JTextField txtPrecio;
    public static javax.swing.JTextField txtRUCEmp;
    public static javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTotal;
    public static javax.swing.JTextField txtUsuId;
    private javax.swing.JTextField txtsub;
    // End of variables declaration//GEN-END:variables
}
