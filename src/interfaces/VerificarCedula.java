
package interfaces;
import java.awt.Color;
import javax.swing.*;

public class VerificarCedula {
    Herramientas h=new Herramientas();
    public boolean valida=false;

    
     public void validacion(java.awt.event.FocusEvent evt) {
         JTextField txt=(JTextField)evt.getComponent();
         String texto=txt.getText().trim();
         if (texto.length()==0) {
            txt.requestFocus();
         }else{
             if(texto.length()<10){
            crearBorde(Color.red, txt);
            //h.mensaje(null, "El numero de cédula debe tener 10 caracteres", "Advertencia", 1500); 
            txt.requestFocus();
            valida=false;
            }else{
             boolean verificador=validadorDeCedula(texto);
         if(verificador==false){
             txt.getToolkit().beep();
             crearBorde(Color.red,txt);
             txt.requestFocus();
             h.mensaje(null, "N° de cédula no válido", "Advertencia", 1500);
             valida=false;
         }
         else{
              valida=true;
             crearBorde(Color.BLACK,txt);  
         }
}
 
         }
              
                   
}
     
     public void controlCaracteres(java.awt.event.KeyEvent evt) {
        char k=evt.getKeyChar();
        JTextField txt=(JTextField)evt.getComponent();
        if(txt.getText().length()>=10){
           crearBorde(Color.red,txt);
           evt.consume();
        }
        
        if(k<'0'||k>'9'){
            crearBorde(Color.ORANGE,txt);
            evt.consume(); 
        }else{
            if(!(txt.getText().length()>=10))
            crearBorde(Color.BLACK,txt);
        }
     }
     
  private void crearBorde(Color c,JTextField t){
     t.setBorder(BorderFactory.createLineBorder(c)); 
  }
  public boolean validadorDeCedula(String cedula) {
    boolean cedulaCorrecta = false;
 
      try {
        if (cedula.length() == 10) {
        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
        if (tercerDigito < 6) {
        // Coeficientes de validación cédula
        // El decimo digito se lo considera dígito verificador
        int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
        int verificador = Integer.parseInt(cedula.substring(9,10));
        int suma = 0;
        int digito = 0;
        for (int i = 0; i < (cedula.length() - 1); i++) {
        digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
        suma += ((digito % 10) + (digito / 10));
        }
 
        if ((suma % 10 == 0) && (suma % 10 == verificador)) {
            cedulaCorrecta = true;
        }
        else if ((10 - (suma % 10)) == verificador) {
            cedulaCorrecta = true;
        } else {
            cedulaCorrecta = false;
        }
        } else {
            cedulaCorrecta = false;
        }
        } else {
            cedulaCorrecta = false;
        }
      } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
        cedulaCorrecta = false;
}
 
return cedulaCorrecta;
}  
}
