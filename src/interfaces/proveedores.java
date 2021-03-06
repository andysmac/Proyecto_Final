/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class proveedores extends javax.swing.JFrame {
    VerificarCedula c = new VerificarCedula();
    public DefaultTableModel model;

    /**
     * Creates new form proveedores
     */
    public proveedores() {
        initComponents();
    }

    public void cargarDatos(){
             tablaProveedores.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                     @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tablaProveedores.getSelectedRow()!= -1){ 
                    int fila = tablaProveedores.getSelectedRow();
                    txtCedula.setText(tablaProveedores.getValueAt(fila, 0).toString());
                    txtNombre.setText(tablaProveedores.getValueAt(fila, 1).toString().concat(" ").concat(tablaProveedores.getValueAt(fila, 2).toString()));
                    txtApellido.setText(tablaProveedores.getValueAt(fila, 3).toString().concat(" ").concat(tablaProveedores.getValueAt(fila, 4).toString()));
                    txtDireccion.setText(tablaProveedores.getValueAt(fila, 5).toString());
                    txtTelefono.setText(tablaProveedores.getValueAt(fila, 6).toString());
                    txtRuc.setText(tablaProveedores.getValueAt(fila, 7).toString());
                        }
            }
        } ); 
        }
    
     public void limpiar(){
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtRuc.setText("");
        txtBuscarCedula.setText("");
    }
     
      public void bloquear(){
        txtCedula.setEnabled(true);
        txtNombre.setEnabled(true);
        txtApellido.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtRuc.setEnabled(true);
        btActualizar.setEnabled(false);
        btEliminar.setEnabled(false);
        btCancelar.setEnabled(false);        
     }

      public void desbloquear(){
        txtNombre.setEnabled(true);
        txtApellido.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtRuc.setEnabled(true);
        btActualizar.setEnabled(true);
        btEliminar.setEnabled(true);
        btCancelar.setEnabled(true); 
        txtBuscarCedula.setEnabled(true);
        
     }
    
          public void controlSoloLetras(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
        }
        }
         
          private boolean controlarCampos()
    {
        boolean ver = true;
        if(txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()
           || txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty() || txtRuc.getText().isEmpty())
        ver= false;
        return ver;
    }
    
          
          private String encontrarTxtVacios()
    {
        String vac="";
        if (txtCedula.getText().isEmpty()) {
            vac=vac.concat("Cedula\n");  
         }
          
         if (txtNombre.getText().isEmpty()) {
            vac=vac.concat("Nombre\n");  
         }
          
         if (txtApellido.getText().isEmpty()) {
            vac=vac.concat("Apellido\n");  
         }
          
         if (txtDireccion.getText().isEmpty()) {
            vac=vac.concat("Dirección\n");  
         }
         if (txtTelefono.getText().isEmpty()) {
             vac=vac.concat("Telefono\n");  
         }
         if (txtRuc.getText().isEmpty()) {
             vac=vac.concat("Ruc Empresa\n");  
         }
          return vac;
    }
          
          public void guardarProveedor() {
        
        if (txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un proveedor sin cedula");
            txtCedula.requestFocus();
        } else {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No puede ingresar un proveedor sin nombre");
                txtNombre.requestFocus();
            } else {
                if (txtApellido.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No puede ingresar un proveedor sin apellido");
                    txtApellido.requestFocus();
                } else {
                    if (txtDireccion.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No puede ingresar un proveedor sin direccion");
                        txtDireccion.requestFocus();

                    } else {

                        if (txtRuc.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No puede ingresar un proveedor sin RUC");
                            txtRuc.requestFocus();

                        } else {

                            if (txtTelefono.getText().isEmpty()) {
                                txtTelefono.setText("NINGUNO");
                            }
                            try {
                                Conexion cc=new Conexion();
                                Connection cn = cc.conectar();
                                String Fac_Nombre, Fac_Nombre2, Fac_Apellido, Fac_Apellido2, Fac_Direccion, Fac_Cedula, Fac_Telefono, Fac_RUC;
                                String sql = "";
                                Fac_Nombre = txtNombre.getText().split(" ")[0];
                                Fac_Apellido = txtApellido.getText().split(" ")[0];
                                Fac_Nombre2 = txtNombre.getText().split(" ")[1];
                                Fac_Apellido2 = txtApellido.getText().split(" ")[1];
                                Fac_Direccion = txtDireccion.getText();
                                Fac_Cedula = txtCedula.getText();
                                Fac_Telefono = txtTelefono.getText();
                                Fac_RUC = txtRuc.getText();
                                sql = "insert into proveedores(ci_pro,nom1_pro,nom2_pro,ape1_pro,ape2_pro,dir_pro,tel_pro,ruc_emr_pr)"
                                        + "values(?,?,?,?,?,?,?,?)";
                                PreparedStatement psw = cn.prepareStatement(sql);
                                psw.setString(1, Fac_Cedula);
                                psw.setString(2, Fac_Nombre);
                                psw.setString(3, Fac_Nombre2);
                                psw.setString(4, Fac_Apellido);
                                psw.setString(5, Fac_Apellido2);
                                psw.setString(6, Fac_Direccion);
                                psw.setString(7, Fac_Telefono);
                                psw.setString(8, Fac_RUC);
                                int n = psw.executeUpdate();
                                if (n > 0) {
                                    cargarTabla("");
                                    JOptionPane.showMessageDialog(null, "Se agrego un nuevo proveedor");
                                }

                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "ERROR: proveedor no ingresado   " + ex);
                            }
                        }
                    }
                }
            }

        }
    }
    
    public void cargarTabla(String dato) {
        Conexion cc=new Conexion();
        Connection cn = cc.conectar();
        String[] titulo = {"Cedula", "Nombre 1", "Nombre 2", "Apellido 1", "Apellido 2", "Direccion", "Telefono", "Ruc"};
        String[] registros = new String[8];
        String sql;
        sql = "SELECT * FROM PROVEEDORES";
        model = new DefaultTableModel(null, titulo);
        try {
             Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
            registros[0] = rs.getString("CI_PRO");
                registros[1] = rs.getString("NOM1_PRO");
                registros[2] = rs.getString("NOM2_PRO");
                registros[3] = rs.getString("APE1_PRO");
                registros[4] = rs.getString("APE2_PRO");
                registros[5] = rs.getString("DIR_PRO");
                registros[6] = rs.getString("TEL_PRO");
                registros[7] = rs.getString("RUC_EMR_PR");
                model.addRow(registros);
                tablaProveedores.setModel(model);
                }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "La tabla Proveedores tiene problemas al cargarse\n" + ex);
               }
    }
    
    public void actualizarProveedor()
    {
        Conexion cc=new Conexion();
        Connection cn = cc.conectar();
        String [] Nombre, Apellido;
        Nombre = txtNombre.getText().split(" ");
        Apellido = txtApellido.getText().split(" ");
        String sql;
        sql="update PROVEEDORES set  nom1_pro ='"+Nombre[0]+"',"
                +"nom2_pro='"+Nombre[1]+"',"
                +"ape1_pro='"+Apellido[0]+"',"
                +"ape2_pro='"+Apellido[1]+"',"
                +"dir_pro='"+txtDireccion.getText()+"',"
                +"tel_pro='"+txtTelefono.getText()+"',"
                +"ruc_emr_pr='"+txtRuc.getText()+"'"
                +"where ci_pro='"+txtCedula.getText()+"'";
        try {
            PreparedStatement psd=cn.prepareStatement(sql);
            int n=psd.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(this, "Se actualizo correctamente");
                cargarTabla("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar"+ex);
        }
    }
    
    public void eliminar(){
        Conexion cc=new Conexion();
        Connection cn = cc.conectar();
        if(JOptionPane.showConfirmDialog(new JFrame(),"Esta seguro que desea eliminar el dato","Borra registro",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
        {
        String sql = "";
        sql = "delete from proveedores where CI_PRO ='"+txtCedula.getText()+"'";
        try {
            PreparedStatement psd=cn.prepareStatement(sql);
            int n=psd.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Se borro el registro");
                cargarTabla("");
                limpiar();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Nose pudo borrar el registro");
        }
        }
    }
    private void crearBorde(Color c,JTextField t){
     t.setBorder(BorderFactory.createLineBorder(c)); 
  }

      public void controlCaracteres2(java.awt.event.KeyEvent evt) {
          char k=evt.getKeyChar();
          JTextField txt=(JTextField)evt.getComponent();
          if(txt.getText().length()>=13){
               crearBorde(Color.red,txt);
               evt.consume();
            }
    if(k<'0'||k>'9'){
            crearBorde(Color.ORANGE,txt);
            evt.consume(); 
        }else{
            if(!(txt.getText().length()>=13))
            crearBorde(Color.BLACK,txt);
        }
     }
      
      public void datos(){
        compras.txtCedula.setText(txtCedula.getText());
        compras.txtNombre.setText(txtNombre.getText());
        compras.txtApellido.setText(txtApellido.getText());
        compras.txtDireccion.setText(txtDireccion.getText());
        compras.txtTelefono.setText(txtTelefono.getText());
        compras.txtRUCEmp.setText(txtRuc.getText());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtRuc = new javax.swing.JTextField();
        btnIngresarProveedor = new javax.swing.JButton();
        btActualizar = new javax.swing.JButton();
        btEliminar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtBuscarCedula = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProveedores = new javax.swing.JTable();
        atras = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PROVEEDORES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 18))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Nombre");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Apellido");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Dirección");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Teléfono");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Ruc Empresa");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Cedula");

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

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoActionPerformed(evt);
            }
        });
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtApellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionActionPerformed(evt);
            }
        });
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });

        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });

        txtRuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRucKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(90, 90, 90)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtDireccion)
                                .addComponent(txtApellido)
                                .addComponent(txtNombre)
                                .addComponent(txtCedula, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                            .addComponent(txtTelefono, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtRuc))
                    .addGap(91, 91, 91)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(27, 27, 27)))
        );

        btnIngresarProveedor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnIngresarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        btnIngresarProveedor.setText("INGRESAR");
        btnIngresarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarProveedorActionPerformed(evt);
            }
        });

        btActualizar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/1 (1).png"))); // NOI18N
        btActualizar.setText("ACTUALIZAR");
        btActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btActualizarActionPerformed(evt);
            }
        });

        btEliminar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/DELETE1.png"))); // NOI18N
        btEliminar.setText("ELIMINAR");
        btEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEliminarActionPerformed(evt);
            }
        });

        btCancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/exi.png"))); // NOI18N
        btCancelar.setText("CANCELAR");
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "BUSCAR PROVEEDOR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Cedula");

        txtBuscarCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarCedulaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarCedulaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 67, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PROVEEDORES EXISTENTES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 12))); // NOI18N

        tablaProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaProveedores);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addContainerGap())
        );

        atras.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        atras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/atras.png"))); // NOI18N
        atras.setText("ATRAS");
        atras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                atrasMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(atras, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(101, 101, 101)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIngresarProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(btnIngresarProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(atras, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCedulaKeyReleased
        cargarTabla((txtBuscarCedula.getText().toUpperCase()));
        c.controlCaracteres(evt);
    }//GEN-LAST:event_txtBuscarCedulaKeyReleased

    private void txtBuscarCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCedulaKeyTyped
        c.controlCaracteres(evt);
        txtBuscarCedula.setText((txtBuscarCedula.getText().toUpperCase()));
    }//GEN-LAST:event_txtBuscarCedulaKeyTyped

    private void txtCedulaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaFocusLost
        c.validacion(evt);
    }//GEN-LAST:event_txtCedulaFocusLost

    private void txtCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyReleased

    }//GEN-LAST:event_txtCedulaKeyReleased

    private void txtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyTyped
        c.controlCaracteres(evt);
    }//GEN-LAST:event_txtCedulaKeyTyped

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        txtNombre.setText((txtNombre.getText().toUpperCase()));
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped

        controlSoloLetras(evt);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoActionPerformed

    private void txtApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyReleased
        txtApellido.setText((txtApellido.getText().toUpperCase()));
    }//GEN-LAST:event_txtApellidoKeyReleased

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped

        controlSoloLetras(evt);
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped
        char c = evt.getKeyChar();
        if((c < '0' || c > '9' ))
        if((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c != '.') && (c != '-') && (c != '_'))evt.consume();
    }//GEN-LAST:event_txtDireccionKeyTyped

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
        c.controlCaracteres(evt);
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void txtRucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucKeyTyped
        // TODO add your handling code here:
        controlCaracteres2(evt);
    }//GEN-LAST:event_txtRucKeyTyped

    private void btnIngresarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarProveedorActionPerformed
        guardarProveedor();
    }//GEN-LAST:event_btnIngresarProveedorActionPerformed

    private void btActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btActualizarActionPerformed
        actualizarProveedor();
    }//GEN-LAST:event_btActualizarActionPerformed

    private void btEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btEliminarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        bloquear();
        limpiar();
    }//GEN-LAST:event_btCancelarActionPerformed

    private void atrasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_atrasMouseClicked
        this.dispose();
        compras r=new compras(acceso.txtusuario.getText());
        menu.pane1.add(r);
        r.show();
        datos();
        compras.txtCodigoP.setEnabled(true);
        compras.btnNuevo.setEnabled(false);
        compras.btnAgregar.setEnabled(true);
        compras.btnEliminarP.setEnabled(true);
        compras.btnEditar.setEnabled(false);
    }//GEN-LAST:event_atrasMouseClicked

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
            java.util.logging.Logger.getLogger(proveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(proveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(proveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(proveedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new proveedores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel atras;
    public static javax.swing.JButton btActualizar;
    public static javax.swing.JButton btCancelar;
    public static javax.swing.JButton btEliminar;
    public static javax.swing.JButton btnIngresarProveedor;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProveedores;
    public static javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscarCedula;
    public static javax.swing.JTextField txtCedula;
    public static javax.swing.JTextField txtDireccion;
    public static javax.swing.JTextField txtNombre;
    public static javax.swing.JTextField txtRuc;
    public static javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
