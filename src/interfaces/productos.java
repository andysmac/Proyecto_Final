package interfaces;

import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class productos extends javax.swing.JInternalFrame implements Runnable{

    public DefaultTableModel modelo;
    String nombreArhivo;
    private FileInputStream fis;
    public String ruta;
    public File archivo;
    private int longitudBytes;
    private String descripcion;
    Thread h1;
    SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yy");

    /**
     * Creates new form productos
     */
    public productos() {
        initComponents();                
        this.setLocation((menu.pane1.getWidth()/2-this.getWidth()/2), (menu.pane1.getHeight()/2-this.getHeight()/2));
        txtCodigo.setText(compras.txtCodigoP.getText());
        cargarTabla("");
        h1 = new Thread(this);
        h1.start();
        bloquear();
        cargarDatos();
        
//        generarCodigo();

    }

    public void cargarDatos() {
        tablaProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (tablaProductos.getSelectedRow() != -1) {
                    int fila = tablaProductos.getSelectedRow();
                    txtCodigo.setText(tablaProductos.getValueAt(fila, 0).toString().trim());
                    txtNombre.setText(tablaProductos.getValueAt(fila, 1).toString().trim());
                    txtMarca.setText(tablaProductos.getValueAt(fila, 2).toString().trim());
                    txtPreCom.setText(tablaProductos.getValueAt(fila, 3).toString().trim());
                    String fecha1 = tablaProductos.getValueAt(fila, 4).toString().trim();
                    Date date = null;
                    try {
                        txtFecha.setDate(fecha.parse(fecha1));
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(null, "Seleccione una fecha válida...");
                    }
                    txtPreVen.setText(tablaProductos.getValueAt(fila, 5).toString().trim());
                    modificar();
                }
            }
        });
    }

    public void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtPreCom.setText("");
        txtFecha.setDate(null);
        txtPreVen.setText("");
//        lbFoto.setIcon(null);
//        txtRuta.setText("");
        txtBuscarProducto.setText("");
    }

    public void bloquear() {
        txtCodigo.setEnabled(true);
        txtNombre.setEnabled(false);
        txtMarca.setEnabled(false);
        txtPreCom.setEnabled(false);
        txtFecha.setEnabled(false);
        txtPreVen.setEnabled(false);
//        lbFoto.setEnabled(false);
//        btFoto.setEnabled(false);
        //btNuevo.setEnabled(true);
        btGuardar.setEnabled(false);
        btActualizar.setEnabled(false);
        btCancelar.setEnabled(false);
//        txtRuta.setEnabled(false);
//        btFoto.setEnabled(false);

    }

    public void desbloquear() {
//        txtCodigo.setEnabled(false);
        txtNombre.setEnabled(true);
        txtMarca.setEnabled(true);
        txtPreCom.setEnabled(true);
        txtFecha.setEnabled(true);
        txtPreVen.setEnabled(true);
//        lbFoto.setEnabled(true);
//        btFoto.setEnabled(true);
//        txtRuta.setEnabled(true);
        //btNuevo.setEnabled(false);
        btGuardar.setEnabled(true);
        btActualizar.setEnabled(false);

        btCancelar.setEnabled(true);
    }

    public void modificar() {
        txtCodigo.setEnabled(false);
        txtNombre.setEnabled(true);
        txtMarca.setEnabled(true);
        txtPreCom.setEnabled(true);
        txtFecha.setEnabled(true);
        txtPreVen.setEnabled(true);
        //btNuevo.setEnabled(false);
        btGuardar.setEnabled(false);
        btActualizar.setEnabled(true);

        btCancelar.setEnabled(true);
    }

    public void Numeros(final JTextField a) {
        a.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
//                int limite=10;
                char B = evt.getKeyChar();
                if (!Character.isDigit(B)) {
                    evt.consume();
                    getToolkit().beep();
                }
//                if(a.getText().length()==limite)
//                {
//                    
//                  evt.setKeyChar((char)KeyEvent.VK_CLEAR);
//                    
//                }

            }
        });
    }

    public void controlSoloNumeros(java.awt.event.KeyEvent evt) {
        int limite = 10;
        char c = evt.getKeyChar(); //Obtiene caracteres desde teclado, coge la tecla que digito Caracter por caracter
        if ((c < '0' || c > '9') && (c != '.'))//Valida que el caracter solo este entre 0 y 9, ademas acepta el punto
        {
            evt.consume(); //Metodo consume.- Que las teclas que presione fuera de rango no se permitan escribir
        }
        if (txtPreCom.getText().length() == limite) //Valida el numero de caracteres a ingresar
        {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR); //Borra caracteres fueras del limite
        }
    }

    public void controlSoloLetras(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        //if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
        if (Character.isDigit(c)) {
            evt.consume();
        }
    }

    private boolean controlarCampos() {
        boolean ver = true;
        if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtMarca.getText().isEmpty()
                || txtPreCom.getText().isEmpty() || txtPreVen.getText().isEmpty()) {
            ver = false;
        }
        return ver;
    }

    private String encontrarTxtVacios() {
        String vac = "";
        if (txtNombre.getText().isEmpty()) {
            vac = vac.concat("Nombre\n");
        }

        if (txtMarca.getText().isEmpty()) {
            vac = vac.concat("Marca\n");
        }

        if (txtPreCom.getText().isEmpty()) {
            vac = vac.concat("Precio Unitario\n");
        }

        if (txtFecha.getDate() == null) {
            vac = vac.concat("Fecha\n");
        }
        if (txtPreVen.getText().isEmpty()) {
            vac = vac.concat("Existencia\n");
        }
        return vac;
    }

    public void generarCodigo() {
        Conexion cc=new Conexion();
        Connection cn = cc.conectar();
        String sql = "SELECT * FROM PRODUCTOS";
        int cod = 1;
        try {
            Statement psw = cn.createStatement();
            ResultSet rs = psw.executeQuery(sql);
            while (rs.next()) {
                cod++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error.... en el codigo del producto");
        }
        txtCodigo.setText(String.valueOf(cod));
    }


    public void guardar() {
        Conexion cc=new Conexion();
        Connection cn = cc.conectar();
        if (controlarCampos() == true) {
            PreparedStatement pst;
            try {
                pst = cn.prepareStatement("INSERT INTO PRODUCTOS(COD_PROD,NOM_PROD,MAR_PROD,PREXUC_PROD,FEC_CAD_PRO,PREXUV_PROD,EXISTENCIA)"
                        + "VALUES(?,?,?,?,?,?,?)");
                pst.setString(1, txtCodigo.getText());
                pst.setString(2, txtNombre.getText());
                pst.setString(3, txtMarca.getText());
                pst.setDouble(4, Double.valueOf(txtPreCom.getText()));
                pst.setString(5, fecha.format(txtFecha.getDate()));
                pst.setDouble(6, Double.valueOf(txtPreVen.getText()));
                pst.setDouble(7, 0);
                int i = pst.executeUpdate();
                if (i > 0) {
                    JOptionPane.showMessageDialog(null, "EL PRODUCTO HA SIDO REGISTRADO");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: EL PRODUCTO NO HA SIDO REGISTRADO" + ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese datos en los siguientes campos:\n" + encontrarTxtVacios());
        }

    }

    public void Modificar() {
        
        if (txtFecha.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fecha");
            txtFecha.requestFocus();
        } else if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el nombre");
            txtNombre.requestFocus();
        } else if (txtPreCom.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el precio de compra");
            txtPreCom.requestFocus();
        } else {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql;

            sql = "UPDATE productos "
                    + "SET NOM_PROD = '" + txtNombre.getText()
                    + "',MAR_PROD='" + txtMarca.getText()
                    + "',PREXUC_PROD = " + txtPreCom.getText()
                    + ",FEC_CAD_PRO='" + fecha.format(txtFecha.getDate())
                    + "',PREXUV_PROD = " + txtPreVen.getText()
                    + "WHERE COD_PROD ='" + txtCodigo.getText() + "'";
            try {
                PreparedStatement ps = cn.prepareStatement(sql);
                int n = ps.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se actualizaron " + n + " fila(s)");
                    cargarTabla("");
                    limpiar();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void actualizarProducto() {
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql;
        sql = "UPDATE productos "
                + "SET NOM_PROD = '" + txtNombre.getText()
                + "',MAR_PROD='" + txtMarca.getText()
                + "',PREXUC_PROD = " + txtPreCom.getText()
                + ",FEC_CAD_PRO='" + fecha.format(txtFecha.getDate())
                + "',PREXUV_PROD = " + txtPreVen.getText()
                + "WHERE COD_PROD ='" + txtCodigo.getText() + "'";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Se actualizo correctamente..");
                cargarTabla("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar " + ex);
        }
    }

    private void actualizarporTabla() {
        Conexion cc=new Conexion();
        Connection cn = cc.conectar();
        int fila = tablaProductos.getSelectedRow();
        String sql = "";
        if (fila != -1) {
            sql = "UPDATE PRODUCTOS "
                    + "SET nom_prod='" + tablaProductos.getValueAt(fila, 1).toString().trim().toUpperCase() + "',"
                    + "mar_prod='" + tablaProductos.getValueAt(fila, 2).toString().trim().toUpperCase() + "',"
                    + "prexuv_prod='" + tablaProductos.getValueAt(fila, 3).toString().trim() + "', "
                    + "fec_cad_pro='" + tablaProductos.getValueAt(fila, 4).toString().trim() + "',"
                    + "prexuc_prod='" + tablaProductos.getValueAt(fila, 5).toString().trim() + "'"
                    + "WHERE COD_PROD='" + tablaProductos.getValueAt(fila, 0).toString() + "' ";

            try {
                PreparedStatement psw = cn.prepareStatement(sql);
                int n = psw.executeUpdate();

                if (n > 0) {
//                JOptionPane.showMessageDialog(this, ":)");
                    cargarTabla("");
                    limpiar();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }

    public void cargarTabla(String Dato) {
        Conexion cc=new Conexion();
        Connection cn = cc.conectar();
        String[] titulo = {"Codigo", "Nombre", "Marca", "Precio Compra", "Fecha Caducidad", "Precio Venta", "Stock"};
        String[] registros = new String[7];
        String sql = "select * from productos where NOM_PROD LIKE '%" + Dato + "%' order by NOM_PROD";
        modelo = new DefaultTableModel(null, titulo);
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("COD_PROD");
                registros[1] = rs.getString("NOM_PROD");
                registros[2] = rs.getString("MAR_PROD");
                registros[3] = rs.getString("PREXUC_PROD");
                registros[4] = rs.getString("FEC_CAD_PRO");
                registros[5] = rs.getString("PREXUV_PROD");
                registros[6] = rs.getString("EXISTENCIA");
                modelo.addRow(registros);
            }
            tablaProductos.setModel(modelo);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "La tabla productos tiene problemas al cargarse\n" + ex);
        }
    }
    
    public void datos1(){
        compras.txtCodigoP.setText(txtCodigo.getText());
        compras.txtNombreP.setText(txtNombre.getText());
        compras.txtPrecio.setText(txtPreCom.getText());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        txtPreCom = new javax.swing.JTextField();
        txtPreVen = new javax.swing.JTextField();
        txtFecha = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        btGuardar = new javax.swing.JButton();
        btActualizar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        Codigo = new javax.swing.JLabel();
        txtBuscarProducto = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();

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
        jScrollPane1.setViewportView(jTable1);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setTitle("PRODUCTOS");
        setPreferredSize(new java.awt.Dimension(713, 662));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PRODUCTOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Codigo");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Marca");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Precio Compra");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Fecha Caducidad");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Precio Venta");

        txtCodigo.setEditable(false);
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });
        txtCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoFocusLost(evt);
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

        txtMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMarcaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMarcaKeyTyped(evt);
            }
        });

        txtPreCom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPreComActionPerformed(evt);
            }
        });
        txtPreCom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPreComKeyTyped(evt);
            }
        });

        txtPreVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPreVenActionPerformed(evt);
            }
        });
        txtPreVen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPreVenKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtPreCom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addComponent(txtMarca, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigo)
                    .addComponent(txtPreVen)
                    .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(txtPreCom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPreVen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/guardar.png"))); // NOI18N
        btGuardar.setText(" GUARDAR");
        btGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGuardarActionPerformed(evt);
            }
        });

        btActualizar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/1 (1).png"))); // NOI18N
        btActualizar.setText("ACTUALIZAR");
        btActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btActualizarActionPerformed(evt);
            }
        });

        btCancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exi.png"))); // NOI18N
        btCancelar.setText("CANCELAR");
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                    .addComponent(btActualizar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btGuardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btGuardar)
                .addGap(18, 18, 18)
                .addComponent(btActualizar)
                .addGap(18, 18, 18)
                .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        Codigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Codigo.setText("Codigo");

        txtBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProductoActionPerformed(evt);
            }
        });
        txtBuscarProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(106, Short.MAX_VALUE)
                .addComponent(Codigo)
                .addGap(50, 50, 50)
                .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Codigo)
                    .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Productos Existentes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaProductosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablaProductosKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tablaProductos);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/atras.png"))); // NOI18N
        jLabel7.setText("ATRAS");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(14, 14, 14)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
    bloquear();
    limpiar();
}//GEN-LAST:event_btCancelarActionPerformed

private void txtPreComActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPreComActionPerformed
    controlSoloNumeros(null);
}//GEN-LAST:event_txtPreComActionPerformed

private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
    controlSoloLetras(evt);
    txtNombre.setText((txtNombre.getText().toUpperCase()));
}//GEN-LAST:event_txtNombreKeyTyped

private void txtMarcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarcaKeyTyped
    controlSoloLetras(evt);
    txtMarca.setText((txtMarca.getText().toUpperCase()));
}//GEN-LAST:event_txtMarcaKeyTyped

private void txtPreComKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreComKeyTyped
//    controlSoloNumeros(evt);
}//GEN-LAST:event_txtPreComKeyTyped

private void txtCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtCodigoFocusLost

private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
// TODO add your handling code here:
    txtNombre.setText((txtNombre.getText().toUpperCase()));
}//GEN-LAST:event_txtNombreKeyReleased

private void txtMarcaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarcaKeyReleased
// TODO add your handling code here:
    txtMarca.setText((txtMarca.getText().toUpperCase()));
}//GEN-LAST:event_txtMarcaKeyReleased

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void btGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuardarActionPerformed
        guardar();
        //    generarCodigo();
        cargarTabla("");
        bloquear();
        limpiar();
 
    }//GEN-LAST:event_btGuardarActionPerformed

    private void btActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btActualizarActionPerformed
        //actualizarProducto();
        Modificar();
        bloquear();
    }//GEN-LAST:event_btActualizarActionPerformed

    private void txtBuscarProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyTyped
        //    txtBuscarProducto.setText((txtBuscarProducto.getText().toUpperCase()));

        //    cargarTabla(txtBuscarProducto.getText());
    }//GEN-LAST:event_txtBuscarProductoKeyTyped

    private void tablaProductosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProductosKeyPressed

    }//GEN-LAST:event_tablaProductosKeyPressed

    private void tablaProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProductosKeyReleased
        if (evt.getKeyChar() == KeyEvent.VK_TAB) {
            //guardar();
        }
    }//GEN-LAST:event_tablaProductosKeyReleased

    private void txtPreVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPreVenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreVenActionPerformed

    private void txtPreVenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreVenKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreVenKeyTyped

    private void txtBuscarProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyReleased
    cargarTabla(txtBuscarProducto.getText().toUpperCase());
    }//GEN-LAST:event_txtBuscarProductoKeyReleased

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
     this.dispose();
        compras r=new compras(acceso.txtusuario.getText());
        menu.pane1.add(r);
        r.show();
        datos1();
        compras.txtCedula.setEnabled(true);
        compras.btnAgregar.setEnabled(true);
        compras.btnNuevo.setEnabled(false);
        compras.btnEditar.setEnabled(false);
    }//GEN-LAST:event_jLabel7MouseClicked

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
            java.util.logging.Logger.getLogger(productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new productos().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Codigo;
    public static javax.swing.JButton btActualizar;
    public static javax.swing.JButton btCancelar;
    public static javax.swing.JButton btGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscarProducto;
    private javax.swing.JTextField txtCodigo;
    public static com.toedter.calendar.JDateChooser txtFecha;
    public static javax.swing.JTextField txtMarca;
    public static javax.swing.JTextField txtNombre;
    public static javax.swing.JTextField txtPreCom;
    public static javax.swing.JTextField txtPreVen;
    // End of variables declaration//GEN-END:variables


    @Override
    public void run() {
        Thread ct = Thread.currentThread();

        while (ct == h1) {
            cargarTabla("");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
    }
}
