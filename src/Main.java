import javax.swing.UIManager;

public class Main {
	public static void main(String arg[]) {
		// Bloque try-catch para atrapar los errores
		try {
			// Look and Feel para la aplicacion dependiendo del sistema
			// operativo
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Inicia la aplicacion
		new Interfaz();
	}
}
