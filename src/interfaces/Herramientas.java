
package interfaces;
import java.awt.Component;
import javax.swing.*;


public class Herramientas {
    private static JOptionPane option = new JOptionPane("",
JOptionPane.INFORMATION_MESSAGE);

private static JDialog dialogo = null;

public void mensaje(Component padre, String texto,
	String titulo, final long tiempo){

    option.setMessage(texto);
    if ( null == dialogo )
    	{
		dialogo = option.createDialog(padre, titulo);
    	}
    else
    	{
		dialogo.setTitle(titulo);
    	}

    Thread hilo = new Thread()
    {
	public void run()
	{
	    	try{
	    	    Thread.sleep(tiempo);
	    	    if ( dialogo.isVisible() )
	    	    {
	    		dialogo.setVisible(false);
	    	    }
	    	}
	    	catch ( InterruptedException e )
	    	{
	    	    e.printStackTrace();
	    	}
	}
    };
    hilo.start();

    dialogo.setVisible(true);
		}    
}
