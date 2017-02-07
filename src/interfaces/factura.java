package interfaces;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JRViewer;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.view.JRViewer;
//import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author usuario
 */
public class factura extends javax.swing.JInternalFrame {

    VerificarCedula c = new VerificarCedula();

    DefaultTableModel model;
    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

    public Conexion cc = new Conexion();
    public Connection cn = cc.conectar();

    //Double precio;
    String nombre;
    Double total = 0.00;
    int n = 0;
    int j = 0;
    int cantidadExistente = 0;
    double totalCompra = 0, iva = 0.0, subT = 0.0;
    public String exis, caja;
    Calendar fecha = Calendar.getInstance();

    public factura() {
        initComponents();
        numFc();
        cargarTablaProducto("");
        Inicio();
        
        txtUsuId.setText(acceso.txtusuario.getText());
        txtFecha.setDate(fecha.getTime());
//        btnIngresar.setEnabled(false);
        btnModificar.setEnabled(false);
    }

    public void Inicio() {
        txtNumFac.setEnabled(false);
        txtIva.setEditable(false);
        txtsub.setEditable(false);
        txtTotal.setEditable(false);
        Calendar cal = Calendar.getInstance();
        txtUsuId.setEditable(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnRealizarCompra.setEnabled(false);
        btnImprimir.setEnabled(false);
        Tproductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (Tproductos.getSelectedRow() != -1) {
                    int fila = Tproductos.getSelectedRow();

                    btnRealizarCompra.setEnabled(true);

                    if (txtCedula.getText().length() == 10 && txtNombre.getText().length() >= 3 && txtApellido.getText().length() >= 3 && txtDireccion.getText().length() >= 1 && txtTelefono.getText().length() >= 1) {
                        txtFacturaCantidad.setEnabled(true);
                    }
                }
            }
        });
    }

    public void limpiar() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtFacturaCantidad.setText("");
        txtNumFac.setText("");
        txtIva.setText("");
        txtsub.setText("");
        txtTotal.setText("");
        Tlista.removeAll();
    }

    public void controlSoloLetras(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
            evt.consume();
        }
    }

    public void controlDireccion(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '0' || c > '9') && c != '-') {
            evt.consume();
        }
    }

//    public factura(String cedula,String nombre,String apellido,String direccion,String telefono) {
//        initComponents();     
//        txtCedula.setText(cedula);
//        txtNombre.setText(nombre);
//        txtApellido.setText(apellido);
//        txtDireccion.setText(direccion);     
//    }
    public void numFc() {
        String sql = "SELECT * FROM FACTURAS ";
        int numFac = 1;
        try {
            Statement psw = cn.createStatement();
            ResultSet rs = psw.executeQuery(sql);
            while (rs.next()) {
                numFac++;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "error en el numero de factura");
}
        txtNumFac.setText(String.valueOf(numFac));
    }

    public void guardarFactura() {
        if (txtNumFac.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Numero de factura no ingresado");
        } else if (txtUsuId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Empleado no cargado");
            txtUsuId.requestFocus();
            txtUsuId.setEnabled(true);
        } else if (txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresa cliente");
            txtCedula.setEnabled(true);
            txtCedula.requestFocus();
        } else if (txtFecha.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Fecha no cargada");
            txtFecha.setEnabled(true);
            txtFecha.requestFocus();
        } else {
            String fecha, cedulaU;
            int numero;
            double sub, iva, tot;
            String cedula;
            String sql = "";
            numero = Integer.valueOf(txtNumFac.getText());
            fecha = f.format(txtFecha.getDate());
            cedula = txtCedula.getText();
            cedulaU = txtUsuId.getText();
            sub = Double.valueOf(txtsub.getText());
            iva = Double.valueOf(txtIva.getText());
            tot = Double.valueOf(txtTotal.getText());
            try {
                sql = "insert into facturas(num_fac, fec_fac, ci_cli_fac,ci_emp_fac,sub_tot_fac,iva_fac,total_pagar)values(?,?,?,?,?,?,?)";
                PreparedStatement psw = cn.prepareStatement(sql);
                psw.setInt(1, numero);
                psw.setString(2, fecha);
                psw.setString(3, cedula);
                psw.setString(4, cedulaU);
                psw.setDouble(5, sub);
                psw.setDouble(6, iva);
                psw.setDouble(7, tot);
                int n = psw.executeUpdate();

                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se inserto correctamente la factura");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "No se inserto corretamente la factura" + ex);
            }
        }
    }

    public void GuardarC() {
        Tproductos.setEnabled(true);
        txtFacturaCantidad.setEnabled(true);
        txtCedula.setEnabled(false);
        txtNombre.setEnabled(false);
        txtApellido.setEnabled(false);
        txtDireccion.setEnabled(false);
//        btnIngresar.setEnabled(false);
        txtBuscarNombrePro.setEnabled(true);

    }

    public void cargarTablaProducto(String Dato) {
        String[] titulos = {"Codigo", "Nombre", "Marca", "Precio Unidad", "Fecha Caducidad", "Existencia"};
        String[] registros = new String[6];
        String sql = "SELECT * FROM PRODUCTOS WHERE COD_PROD LIKE'%" + Dato + "%' ORDER BY COD_PROD";
        model = new DefaultTableModel(null, titulos);
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("COD_PROD");
                registros[1] = rs.getString("NOM_PROD");
                registros[2] = rs.getString("MAR_PROD");
                registros[3] = rs.getString("PREXUV_PROD");
                registros[4] = rs.getString("FEC_CAD_PRO");
                registros[5] = rs.getString("EXISTENCIA");
                model.addRow(registros);
                Tproductos.setModel(model);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    int aux ;
    public void agregarCompra() {

//        DefaultTableModel modelo = new DefaultTableModel() {
//            @Override
//            public boolean isCellEditable(int rowIndex, int vColIndex) {
//                return false;
//            }
//        };
        aux= Tproductos.getSelectedRow();
        cantidadExistente = 0;
        if (aux == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto", "Alerta", JOptionPane.WARNING_MESSAGE);
        } else if (txtFacturaCantidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad");
            txtFacturaCantidad.requestFocus();
        } else {

            String Id = Tproductos.getValueAt(aux, 0).toString();
            String nombre = Tproductos.getValueAt(aux, 1).toString();
            String precio = Tproductos.getValueAt(aux, 3).toString();
            int cantidad = Integer.valueOf(txtFacturaCantidad.getText());

            String DatoId = Id;

            String sql = "select existencia from productos where cod_prod = '" + DatoId + "'";

            try {
                Statement psw = cn.createStatement();
                ResultSet rs = psw.executeQuery(sql);
                while (rs.next()) {
                    cantidadExistente = rs.getInt("existencia");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "La cantidad no existe");
            }

            if ((cantidad > cantidadExistente) || (cantidad == 0)) {
                JOptionPane.showMessageDialog(null, "La cantidad maxima existente es " + cantidadExistente + " No se puede vender", "Alerta", JOptionPane.WARNING_MESSAGE);
                txtFacturaCantidad.setText("");
                txtFacturaCantidad.requestFocus();
            } else {
                int contLista = Tlista.getRowCount();
                int auxiliarLista = 0;
//                if (contLista != 0) {
//                    int datoBuscar = Integer.valueOf(DatoId);
//                    for (int l = 0; l < contLista; l++) {
//                        String Idlista = Tlista.getValueAt(l, 0).toString();
//                        int datoBuscar2 = Integer.valueOf(Idlista);
//                        if (datoBuscar == datoBuscar2) {
//                            auxiliarLista = 1;
//                        }
//                    }
//                }
                if (auxiliarLista == 1) {
                    JOptionPane.showMessageDialog(null, "El producto ya esta en la lista");
                    txtFacturaCantidad.setText("");
                } else {
                    n = n + 1;
                    String aux2 = precio;
                    float tot = Float.valueOf(aux2);
                    tot = tot * cantidad;
                    total = tot + total;
                    double newTotal = Math.rint(total * 1000) / 1000;
                    String aux3 = String.valueOf(newTotal);
                    txtTotal.setText(aux3);
                    iva = newTotal * 0.12;
                    double newIva = Math.rint(iva * 1000) / 1000;
                    txtIva.setText(String.valueOf(newIva));
                    subT = newTotal - iva;
                    double newSub = Math.rint(subT * 1000) / 1000;
                    txtsub.setText(String.valueOf(newSub));

                    DefaultTableModel temp = (DefaultTableModel) Tlista.getModel();
                    Object nuevo[] = {temp.getRowCount() + 1, "", "", "", ""};
                    temp.addRow(nuevo);
                    
                    Tlista.setValueAt(Id, j, 0);
                    Tlista.setValueAt(nombre, j, 1);
                    Tlista.setValueAt(cantidad, j, 2);
                    Tlista.setValueAt(precio, j, 3);
                    Tlista.setValueAt(tot, j, 4);
                    j++;
                    cargarTablaProducto("");
                }
                totalCompra = Double.valueOf(txtTotal.getText());
            }
        }

    }

//      public void detalle() {
//        if (txtNumFac.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "No puede ingresar una factura sin numero");
//        } else if (txtUsuId.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "Usted No se a Identificado como Usuario");
//            txtUsuId.requestFocus();
//            txtUsuId.setEditable(true);
//        } else if (txtCedula.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(null, "No puede ingresar una facutura sin cedula");
//            txtCedula.setEnabled(true);
//            txtCedula.requestFocus();
//        } else if (txtFecha.getDate() == null) {
//            JOptionPane.showMessageDialog(null, "No puede ingresar una factura sin fecha");
//            txtFecha.requestFocus();
//        } else {
//            GuardarDetalle();
//        }
//    }
    
    public void GuardarDetalle() {
        try {

            try {
                guardarFactura();
                int fila = Tlista.getRowCount();
                String sql = "insert into det_facturas (num_fac_p,cod_prod_p,can_prod ) values (?,?,?)";
                PreparedStatement psw = cn.prepareStatement(sql);
                for (int i = 0; i <= fila; i++) {
                    String codigoProducto = Tlista.getValueAt(i, 0).toString();
                    String cantidad = Tlista.getValueAt(i, 2).toString();
                    String factura = txtNumFac.getText();
                    psw.setInt(1, Integer.valueOf(factura));
                    psw.setString(2, codigoProducto);
                    psw.setDouble(3, Double.valueOf(cantidad));
                    psw.executeUpdate();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "no insertado");
            }
        } catch (Exception ex) {

        }
    }

    public void eliminarProdDetalle() {
        String sql = "";
        DefaultTableModel modelo = (DefaultTableModel) Tlista.getModel();
        int fila = Tlista.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila");
        } else {
            String canTlista = Tlista.getValueAt(fila, 2).toString(); //Selecciona la cantidad del producto del detalle
            String codTlista = Tlista.getValueAt(fila, 0).toString(); // Selecciona el codigo de la tabla detalle el codigo del produc
//          int precio1=Integer.parseInt(Tlista.getValueAt(fila, 3).toString());
            
            double valorcalc = Double.valueOf(Tlista.getValueAt(fila, 4).toString());
            double a = totalCompra - valorcalc;
            double b = a * 0.12;
            double c = a - b;
            JOptionPane.showMessageDialog(null, valorcalc);
            modelo.removeRow(fila); //limina la fila seleccionada  
            txtTotal.setText(String.valueOf(a));
            txtIva.setText(String.valueOf(b));
            txtsub.setText(String.valueOf(c));
            total=Double.valueOf(txtTotal.getText());
            iva=Double.valueOf(txtIva.getText());
            subT=Double.valueOf(txtsub.getText());
            try {
                Statement st = cn.createStatement();
                String sql1 = "";
                sql1 = "SELECT EXISTENCIA FROM PRODUCTOS WHERE COD_PROD='" + codTlista + "'";
                ResultSet rs = st.executeQuery(sql1);
                while (rs.next()) {
                    exis = rs.getString("EXISTENCIA");
//                 JOptionPane.showMessageDialog(null, "La existencia es "+a);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR EXISTENCIA ");
            }
            int aumenta = Integer.valueOf(exis) + Integer.valueOf(canTlista);
            j--;
            if(Tlista.getRowCount()==0){
            txtTotal.setText(String.valueOf(0));
            txtIva.setText(String.valueOf(0));
            txtsub.setText(String.valueOf(0));
            total=0.0;
            iva=0;
            subT=0;
            }
//            try {
//                sql = "UPDATE PRODUCTOS SET EXISTENCIA=" + aumenta + " where COD_PROD='" + codTlista + "'";
//                PreparedStatement pst = cn.prepareStatement(sql);
//                int n = pst.executeUpdate();
//                if (n > 0) {
////                JOptionPane.showMessageDialog(null, "Existencia Aumento");
//                    cargarTablaProducto("");
//                }
//                j--;
//            } catch (SQLException ex) {
////            JOptionPane.showMessageDialog(null, "ERROR No se aumento"+aumenta);
//            }
        }
    }

//    public void resta() {
//        try {
//            String sql = "";
//            PreparedStatement pst = cn.prepareStatement(sql);
//            int resta;
//            int fila = Tproductos.getSelectedRow();
//            resta = Integer.valueOf(Tproductos.getValueAt(fila, 2).toString()) - Integer.valueOf(txtFacturaCantidad.getText());
//            System.out.println(resta);
//            sql = "UPDATE PRODUCTOS SET EXISTENCIA=" + resta + " where COD_PROD='" + Tproductos.getValueAt(fila, 0) + "'";
//            int n = pst.executeUpdate();
//            if (n > 0) {
//                JOptionPane.showMessageDialog(null, "Existencia Disminuyo");
//                cargarTablaProducto("");
//            }
//
//        } catch (SQLException ex) {
//            Logger.getLogger(factura.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    public void disminuir() {
//        
//        String sql = "";
//        int fil;
//        fil = aux;
//        String canTprod = txtFacturaCantidad.getText(); //Selecciona la cantidad del producto del detalle
//        JOptionPane.showMessageDialog(null, fil);
//        String codTprod = Tproductos.getValueAt(fil, 0).toString(); // Selecciona el codigo de la tabla detalle el codigo del produc 
//        try {
//            Statement st = cn.createStatement();
//            String sql1 = "";
//            sql1 = "SELECT EXISTENCIA FROM PRODUCTOS WHERE COD_PROD='" + codTprod + "'";
//            ResultSet rs = st.executeQuery(sql1);
//            while (rs.next()) {
//                exis = rs.getString("EXISTENCIA");
//                JOptionPane.showMessageDialog(null, "La existencia es " + exis);
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "ERROR EXISTENCIA ");
//        }
//        int disminuye = Integer.valueOf(exis) - Integer.valueOf(canTprod);
//        try {
//            sql = "UPDATE PRODUCTOS SET EXISTENCIA=" + disminuye + " where COD_PROD='" + codTprod + "'";
//            PreparedStatement pst = cn.prepareStatement(sql);
//            int n = pst.executeUpdate();
//            if (n > 0) {
//                JOptionPane.showMessageDialog(null, "Existencia Disminuyo");
//                cargarTablaProducto("");
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "ERROR No se aumento");
//        }
//
//    }

    public void CedulaV() {
        if (txtCedula.getText().length() <= 9) {
            txtNombre.setText("");
            txtApellido.setText("");
            txtDireccion.setText("");
            txtTelefono.setText("");
            //btnIngresar.setEnabled(false);
            btnModificar.setEnabled(false);
        } else if (txtCedula.getText().length() == 10) {
            
            String Dato = txtCedula.getText();
            String sql = "select * from clientes  where ci_cli like'%" + Dato + "%'";
            boolean existe=false;
            try {
                Statement psw = cn.createStatement();
                ResultSet rs = psw.executeQuery(sql);
                while (rs.next()) {
                    existe = true;
                    if (existe == true) {
                    txtNombre.setText(rs.getString("nom1_cli").concat(" ").concat(rs.getString("nom2_cli")));
                    txtApellido.setText(rs.getString("ape1_cli").concat(" ").concat(rs.getString("ape2_cli")));
                    txtDireccion.setText(rs.getString("dir_cli"));
                    txtTelefono.setText(rs.getString("tel_cli"));
                     btnModificar.setEnabled(true);
                }
                }
                if (existe == false) {
                    clientes p = new clientes();
                    menu.pane1.add(p);
                    p.show();
                    this.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
           
        }
    }
    
    
    
//
//    private void reporte() {
//        Conexion cc = new Conexion();
//        Connection cn = cc.conectar();
//        Map parametro=new HashMap();
//        parametro.put("numero", txtNumFac.getText());
//        try {
//            JasperReport reporte = JasperCompileManager.compileReport("C:\\Reportes\\factura.jrxml");
//            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, cn);
//            JInternalFrame frame = new JInternalFrame();
//            frame.getContentPane().add(new JRViewer(print));
//            frame.pack();
//            frame.setClosable(true);
//            menu.pane1.add(frame);
//            frame.setMaximum(true);
//            frame.setVisible(true);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//    }
    
    private void limpiarT(){
        DefaultTableModel modelo = (DefaultTableModel) Tlista.getModel();
        int n=Tlista.getRowCount();
        for(int i=0;i<n;i++){
        modelo.removeRow(0);
        total = 0.0;
        iva=0;
        subT=0;
        j--;
        }
    }
    
    public void datos(){
        clientes.txtNombre.setText(txtNombre.getText());
        clientes.txtApellido.setText(txtApellido.getText());
        clientes.txtDireccion.setText(txtDireccion.getText());
        clientes.txtTelefono.setText(txtTelefono.getText());
        clientes.atras.setVisible(true);
        interfaces.clientes.btnActualizar.setVisible(true);
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JButton();
        btnRealizarCompra = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tproductos = new javax.swing.JTable();
        txtFacturaCantidad = new javax.swing.JTextField();
        panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tlista = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtsub = new javax.swing.JTextField();
        txtIva = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtBuscarNombrePro = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNumFac = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtFecha = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        txtUsuId = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setClosable(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Cedula");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Apellido");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Direccion");

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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtApellidoKeyReleased(evt);
            }
        });

        txtDireccion.setEditable(false);
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Telefono");

        txtTelefono.setEditable(false);

        btnModificar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/edit_1.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApellido, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre)
                    .addComponent(txtCedula, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(txtTelefono))
                .addGap(18, 18, 18)
                .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/DELETE1.png"))); // NOI18N
        btnEliminar.setText("ELIMINAR PRODUCTO");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnRealizarCompra.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRealizarCompra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/AGRE.png"))); // NOI18N
        btnRealizarCompra.setText("   COMPRA");
        btnRealizarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarCompraActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/exi.png"))); // NOI18N
        btnCancelar.setText("    CANCELAR");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Files-New-File-icon (2).png"))); // NOI18N
        btnNuevo.setText("       NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnImprimir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/1470533078_BT_printer.png"))); // NOI18N
        btnImprimir.setText("     IMPRIMIR");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btnsalir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnsalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/salir_icono.png"))); // NOI18N
        btnsalir.setText("SALIR");
        btnsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRealizarCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnsalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRealizarCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnsalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PRODUCTOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N

        Tproductos.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        Tproductos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(Tproductos);

        txtFacturaCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFacturaCantidadKeyTyped(evt);
            }
        });

        panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Tlista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codido", "Nombre", "Cantidad", "Precio Unidad", "Total"
            }
        ));
        jScrollPane1.setViewportView(Tlista);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("SubTotal");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("IVA");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Total a Pagar");

        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(32, 32, 32))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIva, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(txtTotal)
                            .addComponent(txtsub))
                        .addGap(49, 49, 49)))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtsub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel19.setText("Cantidad");

        btnAgregar.setText("Seleccionar  Producto");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        btnAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAgregarKeyPressed(evt);
            }
        });

        jLabel18.setText("Buscar");

        txtBuscarNombrePro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarNombreProKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarNombrePro, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(txtFacturaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnAgregar)
                        .addGap(0, 293, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtBuscarNombrePro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(txtFacturaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("RUC: 1804983102001");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Num");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("FACTURA DE VENTA");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Fecha");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Cajero");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNumFac, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(txtUsuId))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(jLabel14)
                        .addGap(0, 153, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(35, 35, 35))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txtNumFac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Sitka Small", 1, 16)); // NOI18N
        jLabel9.setText("SUPERMARKET  \"TOTORAS\"");

        jLabel10.setText("AMBATO - ECUADOR");

        jLabel11.setText("Telefono 0981089745");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(25, 25, 25))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(8, 8, 8)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
    JOptionPane.showMessageDialog(null, "FIN");
}//GEN-LAST:event_btnCancelarActionPerformed

private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
    eliminarProdDetalle();
}//GEN-LAST:event_btnEliminarActionPerformed

private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
    agregarCompra();
    txtFacturaCantidad.setText(null);
    cargarTablaProducto("");
    btnEliminar.setEnabled(true);
    btnCancelar.setEnabled(true);
}//GEN-LAST:event_btnAgregarActionPerformed

private void btnAgregarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAgregarKeyPressed

//    agregarCompra();
}//GEN-LAST:event_btnAgregarKeyPressed

private void txtCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyReleased
    CedulaV();
}//GEN-LAST:event_txtCedulaKeyReleased

private void txtBuscarNombreProKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarNombreProKeyReleased
    cargarTablaProducto(txtBuscarNombrePro.getText());
    txtBuscarNombrePro.setText((txtBuscarNombrePro.getText().toUpperCase()));
}//GEN-LAST:event_txtBuscarNombreProKeyReleased

private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed
// TODO add your handling code here:
    if (txtCedula.getText().length() == 10 && txtNombre.getText().length() >= 1 && txtApellido.getText().length() >= 1 && txtDireccion.getText().length() >= 1) {
        //txtFacturaCantidad.setEnabled(true); 
        cargarTablaProducto("");
        txtNombre.setEnabled(false);
    } else if (txtNombre.getText().trim().length() >= 1) {
        txtNombre.setEnabled(false);
        Tproductos.setEnabled(true);
    } else {
        txtNombre.setEnabled(true);
//                txtNombre.requestFocus();
    }
}//GEN-LAST:event_txtCedulaActionPerformed

private void txtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyTyped
// TODO add your handling code here:
    c.controlCaracteres(evt);
}//GEN-LAST:event_txtCedulaKeyTyped

private void txtCedulaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaFocusLost
// TODO add your handling code here:
    // c.validacion(evt);
}//GEN-LAST:event_txtCedulaFocusLost

private void btnRealizarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarCompraActionPerformed
    GuardarDetalle();
    cargarTablaProducto("");
    btnCancelar.setEnabled(false);
    btnEliminar.setEnabled(false);
    btnImprimir.setEnabled(true);
//    cajachicha();

}//GEN-LAST:event_btnRealizarCompraActionPerformed

private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtTotalActionPerformed

private void txtFacturaCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFacturaCantidadKeyTyped
// TODO add your handling code here:
    c.controlCaracteres(evt);
}//GEN-LAST:event_txtFacturaCantidadKeyTyped

private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
// TODO add your handling code here:
    txtNombre.setText((txtNombre.getText().toUpperCase()));
}//GEN-LAST:event_txtNombreKeyReleased

private void txtApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyReleased
// TODO add your handling code here:
    txtApellido.setText((txtApellido.getText().toUpperCase()));
}//GEN-LAST:event_txtApellidoKeyReleased

private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
// TODO add your handling code here:
    controlSoloLetras(evt);
}//GEN-LAST:event_txtNombreKeyTyped

private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
    controlSoloLetras(evt);
}//GEN-LAST:event_txtApellidoKeyTyped

private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped
// TODO add your handling code here:
    controlDireccion(evt);
}//GEN-LAST:event_txtDireccionKeyTyped

private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
    limpiar();
    numFc();
    limpiarT();
}//GEN-LAST:event_btnNuevoActionPerformed

private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
//reporte();
    try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            Map parametro = new HashMap();
            parametro.put("numFac", txtNumFac.getText());
            JasperReport reporte = JasperCompileManager.compileReport("src/Reportes/factu.jrxml");
            JasperPrint print = JasperFillManager.fillReport(reporte, parametro, cc.conectar());
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
}//GEN-LAST:event_btnImprimirActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        this.dispose();
        clientes cliente = new clientes();
        menu.pane1.add(cliente);
        cliente.show();
        cliente.btnGuardar.setEnabled(false);
        cliente.atras.setVisible(false);
        datos();
        
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnsalirActionPerformed

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
            java.util.logging.Logger.getLogger(factura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(factura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(factura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(factura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new factura().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tlista;
    private javax.swing.JTable Tproductos;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRealizarCompra;
    private javax.swing.JButton btnsalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panel;
    public static javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscarNombrePro;
    public static javax.swing.JTextField txtCedula;
    public static javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtFacturaCantidad;
    private com.toedter.calendar.JDateChooser txtFecha;
    private javax.swing.JTextField txtIva;
    public static javax.swing.JTextField txtNombre;
    public static javax.swing.JTextField txtNumFac;
    public static javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTotal;
    public static javax.swing.JTextField txtUsuId;
    private javax.swing.JTextField txtsub;
    // End of variables declaration//GEN-END:variables
}
