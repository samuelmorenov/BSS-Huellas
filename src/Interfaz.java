
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Dialog.ModalExclusionType;

@SuppressWarnings("serial")
public class Interfaz extends JFrame implements ActionListener {
	private JFrame frmProyectoBss;
	private JLabel labelImagenOriginal, labelImagenProcesada;
	private JButton Boton_Abrir;
	private JButton Boton_Procesar, Boton_Anterior, btnEscalaDeGrises, btnEcualizar, btnBinarizar, btnFiltrar,
			btnAdelgazamiento, btnExtraccionDeMinutias, btnUmbralAutomatico, tglbtnecualizar;
	private JPanel panelComponentes, panelImagenOriginal, panelImagenProcesada, panelMenu;
	private JFileChooser fileChooser;
	private JSlider Barra_Umbralizar;
	private JPanel panelImagenes;
	private JPanel panelUmbralizar;
	private JLabel ValorUmbralizar;
	BufferedImage imagenEntrada;
	ArrayList<Matriz> array;
	ArrayList<Minutia> arrayMinutias;
	Matriz matriz;
	ImageIcon iconOriginal;
	ProcesarImagen procesar;
	int tarea;
	int umbralizar;
	boolean arrayLleno;
	boolean onoff;
	static int numTareas = 6;
	private JButton btnAngulos;
	private JPanel panelRetroProce;
	private JPanel panelLog;
	private JTextArea textArea;
	private boolean usuarioTocaBarra;
	private boolean inicioLleno;
	private static int anchoMaximo = 390;
	private static int altoMaximo = 500;


	// public static void main(String arg[])
	// {
	// //Bloque try-catch para atrapar los errores
	// try
	// {
	// //Look and Feel para la aplicacion dependiendo del sistema operativo
	// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// //Inicia la aplicacion
	// new Imagen();
	// }
	// Construtor de la clase
	public Interfaz() {
		array = new ArrayList<Matriz>();
		arrayMinutias = new ArrayList<Minutia>();
		procesar = new ProcesarImagen();
		arrayLleno = false;
		tarea = -1;
		usuarioTocaBarra = false;
		inicioLleno = false;
		onoff = true;

		// Frame
		frmProyectoBss = new JFrame("Ventana");
		frmProyectoBss.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frmProyectoBss.setResizable(false);
		frmProyectoBss.setTitle("Proyecto BSS");
		frmProyectoBss.getContentPane().setLayout(new BorderLayout());

		/** Creacion de componentes */

		// Botones
		Boton_Abrir = new JButton("Abrir archivo");
		Boton_Abrir.setActionCommand("Abrir");
		Boton_Abrir.addActionListener(this);
		
		
		//Panel de Menu
		panelMenu = new JPanel();
		panelMenu.setBorder(new LineBorder(new Color(0, 0, 0)));
		frmProyectoBss.getContentPane().add(panelMenu, BorderLayout.EAST);
		GridBagLayout gbl_panelMenu = new GridBagLayout();
		gbl_panelMenu.columnWidths = new int[]{114, 0};
		gbl_panelMenu.rowHeights = new int[]{25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelMenu.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelMenu.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelMenu.setLayout(gbl_panelMenu);
		
		
		
		//Botones derecha
		btnEscalaDeGrises = new JButton("Escala de Grises");
		btnEscalaDeGrises.setEnabled(false);
		btnEscalaDeGrises.setActionCommand("btnEscalaDeGrises");
		btnEscalaDeGrises.addActionListener(this);

		GridBagConstraints gbc_btnEscalaDeGrises = new GridBagConstraints();
		gbc_btnEscalaDeGrises.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEscalaDeGrises.anchor = GridBagConstraints.NORTH;
		gbc_btnEscalaDeGrises.insets = new Insets(0, 0, 5, 0);
		gbc_btnEscalaDeGrises.gridx = 0;
		gbc_btnEscalaDeGrises.gridy = 0;
		panelMenu.add(btnEscalaDeGrises, gbc_btnEscalaDeGrises);

		btnEcualizar = new JButton("Ecualizar");
		btnEcualizar.setEnabled(false);
		btnEcualizar.setActionCommand("btnEcualizar");
		btnEcualizar.addActionListener(this);

		GridBagConstraints gbc_btnEcualizar = new GridBagConstraints();
		gbc_btnEcualizar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEcualizar.anchor = GridBagConstraints.NORTH;
		gbc_btnEcualizar.insets = new Insets(0, 0, 5, 0);
		gbc_btnEcualizar.gridx = 0;
		gbc_btnEcualizar.gridy = 1;
		panelMenu.add(btnEcualizar, gbc_btnEcualizar);

		btnBinarizar = new JButton("Binarizar");
		btnBinarizar.setEnabled(false);
		btnBinarizar.setActionCommand("btnBinarizar");
		btnBinarizar.addActionListener(this);

		GridBagConstraints gbc_btnBinarizar = new GridBagConstraints();
		gbc_btnBinarizar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBinarizar.anchor = GridBagConstraints.NORTH;
		gbc_btnBinarizar.insets = new Insets(0, 0, 5, 0);
		gbc_btnBinarizar.gridx = 0;
		gbc_btnBinarizar.gridy = 2;
		panelMenu.add(btnBinarizar, gbc_btnBinarizar);

		btnFiltrar = new JButton("Filtrar");
		btnFiltrar.setEnabled(false);
		btnFiltrar.setActionCommand("btnFiltrar");
		btnFiltrar.addActionListener(this);

		GridBagConstraints gbc_btnFiltrar = new GridBagConstraints();
		gbc_btnFiltrar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnFiltrar.anchor = GridBagConstraints.NORTH;
		gbc_btnFiltrar.insets = new Insets(0, 0, 5, 0);
		gbc_btnFiltrar.gridx = 0;
		gbc_btnFiltrar.gridy = 3;
		panelMenu.add(btnFiltrar, gbc_btnFiltrar);

		btnAdelgazamiento = new JButton("Adelgazamiento");
		btnAdelgazamiento.setEnabled(false);
		btnAdelgazamiento.setActionCommand("btnAdelgazamiento");
		btnAdelgazamiento.addActionListener(this);

		GridBagConstraints gbc_btnAdelgazamiento = new GridBagConstraints();
		gbc_btnAdelgazamiento.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdelgazamiento.anchor = GridBagConstraints.NORTH;
		gbc_btnAdelgazamiento.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdelgazamiento.gridx = 0;
		gbc_btnAdelgazamiento.gridy = 4;
		panelMenu.add(btnAdelgazamiento, gbc_btnAdelgazamiento);

		btnExtraccionDeMinutias = new JButton("Minutias");
		btnExtraccionDeMinutias.setEnabled(false);
		btnExtraccionDeMinutias.setActionCommand("btnExtraccionDeMinutias");
		btnExtraccionDeMinutias.addActionListener(this);

		GridBagConstraints gbc_btnExtraccionDeMinutias = new GridBagConstraints();
		gbc_btnExtraccionDeMinutias.insets = new Insets(0, 0, 5, 0);
		gbc_btnExtraccionDeMinutias.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExtraccionDeMinutias.anchor = GridBagConstraints.NORTH;
		gbc_btnExtraccionDeMinutias.gridx = 0;
		gbc_btnExtraccionDeMinutias.gridy = 5;
		panelMenu.add(btnExtraccionDeMinutias, gbc_btnExtraccionDeMinutias);
		
		btnAngulos = new JButton("Angulos");
		btnAngulos.setEnabled(false);
		btnAngulos.setActionCommand("btnAngulos");
		btnAngulos.addActionListener(this);
		
		GridBagConstraints gbc_btnAngulos = new GridBagConstraints();
		gbc_btnAngulos.anchor = GridBagConstraints.NORTH;
		gbc_btnAngulos.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAngulos.insets = new Insets(0, 0, 5, 0);
		gbc_btnAngulos.gridx = 0;
		gbc_btnAngulos.gridy = 6;
		panelMenu.add(btnAngulos, gbc_btnAngulos);

		// Barra Umbralizar
		Barra_Umbralizar = new JSlider();
		Barra_Umbralizar.setPaintLabels(true);
		Barra_Umbralizar.setValue(100);
		Barra_Umbralizar.setMinorTickSpacing(20);
		Barra_Umbralizar.setMinimum(0);
		Barra_Umbralizar.setMaximum(254);
		Barra_Umbralizar.setPaintTicks(true);
		// Texto del valor
		ValorUmbralizar = new JLabel("Valor :" + Barra_Umbralizar.getValue()+" ");

		Barra_Umbralizar.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				umbralizar = Barra_Umbralizar.getValue();
				ValorUmbralizar.setText("Valor :0" + umbralizar + " ");
				if(umbralizar<=9)  ValorUmbralizar.setText("Valor :00" + umbralizar + " ");
				if(umbralizar>=100) ValorUmbralizar.setText("Valor :" + umbralizar + " ");
				usuarioTocaBarra = true;
//				if (tarea >= 3) {
//					llenarArray();
//					pintarTarea(tarea);
//				}
			}
		});
		int timerDelay = 500;
		new Timer(timerDelay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tarea >= 3 && usuarioTocaBarra) {
					System.out.println("Actualizando valor de Umbralizar: "+umbralizar);
					pintarTarea(tarea);
					usuarioTocaBarra = false;
				}
			}
		}).start();
		
		// Panel de Umbralizar
		panelUmbralizar = new JPanel();
		GridBagConstraints gbc_panelUmbralizar = new GridBagConstraints();
		gbc_panelUmbralizar.insets = new Insets(0, 0, 5, 0);
		gbc_panelUmbralizar.gridx = 0;
		gbc_panelUmbralizar.gridy = 7;
		panelMenu.add(panelUmbralizar, gbc_panelUmbralizar);
		panelUmbralizar.setBorder(BorderFactory.createTitledBorder("Umbral"));
		panelUmbralizar.setLayout(new BorderLayout());
		panelUmbralizar.add(Barra_Umbralizar, BorderLayout.CENTER);
		panelUmbralizar.add(ValorUmbralizar, BorderLayout.EAST);
						
		btnUmbralAutomatico = new JButton("Umbral Automatico");
		panelUmbralizar.add(btnUmbralAutomatico, BorderLayout.NORTH);
		btnUmbralAutomatico.setActionCommand("UmbralAutomatico");
		btnUmbralAutomatico.addActionListener(this);
		btnUmbralAutomatico.setEnabled(false);
		
		tglbtnecualizar = new JButton("Ecualizar ON");
		GridBagConstraints gbc_tglbtnecualizar = new GridBagConstraints();
		gbc_tglbtnecualizar.insets = new Insets(0, 0, 5, 0);
		gbc_tglbtnecualizar.gridx = 0;
		gbc_tglbtnecualizar.gridy = 8;
		tglbtnecualizar.setActionCommand("EcualizarOnOff");
		tglbtnecualizar.addActionListener(this);
		tglbtnecualizar.setEnabled(false);
		panelMenu.add(tglbtnecualizar, gbc_tglbtnecualizar);
		
		panelRetroProce = new JPanel();
		GridBagConstraints gbc_panelRetroProce = new GridBagConstraints();
		gbc_panelRetroProce.insets = new Insets(0, 0, 5, 0);
		gbc_panelRetroProce.fill = GridBagConstraints.BOTH;
		gbc_panelRetroProce.gridx = 0;
		gbc_panelRetroProce.gridy = 9;
		panelMenu.add(panelRetroProce, gbc_panelRetroProce);
		
		Boton_Anterior = new JButton("Retroceder");
		panelRetroProce.add(Boton_Anterior);
		Boton_Anterior.setActionCommand("Anterior");
		Boton_Anterior.addActionListener(this);
		Boton_Anterior.setEnabled(false);
		
		Boton_Procesar = new JButton("Procesar archivo");
		panelRetroProce.add(Boton_Procesar);
		Boton_Procesar.setActionCommand("Procesar");
		Boton_Procesar.addActionListener(this);
		Boton_Procesar.setEnabled(false);
		

		

		// Panel de componentes
		panelComponentes = new JPanel();
		panelComponentes.setLayout(new BorderLayout());
		panelComponentes.add(Boton_Abrir, BorderLayout.NORTH);
		panelComponentes.setBorder(BorderFactory.createTitledBorder("Abre la imagen..."));
		frmProyectoBss.getContentPane().add(panelComponentes, BorderLayout.NORTH);
		



		// Imagenes
		labelImagenOriginal = new JLabel();
		labelImagenProcesada = new JLabel();

		// Panel Imagen Original
		panelImagenOriginal = new JPanel();
		panelImagenOriginal.setLayout(new BorderLayout());
		panelImagenOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Original"));
		panelImagenOriginal.add(labelImagenOriginal, BorderLayout.WEST);

		// Panel Imagen Procesada
		panelImagenProcesada = new JPanel();
		panelImagenProcesada.setLayout(new BorderLayout());
		panelImagenProcesada.setBorder(BorderFactory.createTitledBorder("Copia Imagen Original"));
		panelImagenProcesada.add(labelImagenProcesada, BorderLayout.EAST);

		// Panel Imagenes
		panelImagenes = new JPanel();
		panelImagenes.add(panelImagenOriginal);
		panelImagenes.add(panelImagenProcesada);
		frmProyectoBss.getContentPane().add(panelImagenes, BorderLayout.CENTER);
		
		panelLog = new JPanel();
		frmProyectoBss.getContentPane().add(panelLog, BorderLayout.SOUTH);
		
		
		// Panel de texto
		textArea = new JTextArea(8, 120);//Filas, Columnas
		textArea.setEditable(false);
		class CustomOutputStream extends OutputStream {
		    private JTextArea textArea;

		    public CustomOutputStream(JTextArea textArea) {
		        this.textArea = textArea;
		    }

		    @Override
		    public void write(int b) throws IOException {
		        // redirects data to the text area
		        textArea.append(String.valueOf((char)b));
		        // scrolls the text area to the end of data
		        textArea.setCaretPosition(textArea.getDocument().getLength());
		        // keeps the textArea up to date
		        textArea.update(textArea.getGraphics());
		    }
		}
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		
		
		System.setOut(printStream);
		System.setErr(printStream);
		
		panelLog.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.SOUTH);

		// Estableciendo visibilidad, tamaño y cierrre de la aplicacion
		frmProyectoBss.setVisible(true);
		frmProyectoBss.setBounds(490, 130, 1111, 800);
		frmProyectoBss.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creando FileChooser
		fileChooser = new JFileChooser("Huellas");
		// Añadiendole un filtro
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG", "jpg", "png");
		fileChooser.setFileFilter(filter);
	}

	// Metodo de accion para el boton
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if ("Abrir".equals(e.getActionCommand())) {
			int regresaValor = fileChooser.showOpenDialog(null); // Valor que tomara el fileChoose
			if (regresaValor == JFileChooser.APPROVE_OPTION) { // Accion del fileChooser
				File archivoElegido = fileChooser.getSelectedFile(); // Crear propiedades para ser utilizadas por fileChooser 
				String direccion = archivoElegido.getPath();// Obteniendo la direccion del archivo
				try {
					abrirArchivo(direccion);

				} catch (Exception es) {
					labelImagenOriginal.disable();
					labelImagenProcesada.disable();
					JOptionPane.showMessageDialog(null, "Upss!! error abriendo la imagen " + es);
				}
			}
		}
		if ("Procesar".equals(e.getActionCommand())) {
			if(tarea < numTareas){
				tarea++;
				System.out.println("Procesando tarea: "+tarea);
				pintarTarea(tarea);
			}
		}
		if ("Anterior".equals(e.getActionCommand())) {
			if (tarea > 1) {
				tarea--;
				pintarTarea(tarea);
			}
		}
		
		if ("UmbralAutomatico".equals(e.getActionCommand())) {
			umbralizar = procesar.calcularUmbral(array.get(2));
			Barra_Umbralizar.setValue(umbralizar);
			ValorUmbralizar.setText("Valor :0" + umbralizar + " ");
			if(umbralizar<=9)  ValorUmbralizar.setText("Valor :00" + umbralizar + " ");
			if(umbralizar>=100) ValorUmbralizar.setText("Valor :" + umbralizar + " ");
			System.out.println("Calculado valor automatico de umbral: "+umbralizar);
			if (tarea >= 3) {
				pintarTarea(tarea);
			}
		}
		if ("btnEscalaDeGrises".equals(e.getActionCommand())) {
			pintarTarea(1);
		}
		if ("btnEcualizar".equals(e.getActionCommand())) {
			pintarTarea(2);
		}
		if ("btnBinarizar".equals(e.getActionCommand())) {
			pintarTarea(3);
		}
		if ("btnFiltrar".equals(e.getActionCommand())) {
			pintarTarea(4);
		}
		if ("btnAdelgazamiento".equals(e.getActionCommand())) {
			pintarTarea(5);
		}
		if ("btnExtraccionDeMinutias".equals(e.getActionCommand())) {
			pintarTarea(6);
		}
		if ("btnAngulos".equals(e.getActionCommand())) {
			procesar.calcularAngulo(array.get(5), arrayMinutias);
				
		}
		if ("EcualizarOnOff".equals(e.getActionCommand())) {
			ecualizarOnOff();
			pintarTarea(tarea);
		}
		
	}
	private void ecualizarOnOff(){
		if(onoff){
			onoff = false;
			tglbtnecualizar.setText("Ecualizar OFF");
		}
		else{
			onoff = true;
			tglbtnecualizar.setText("Ecualizar ON");
		}
		procesar.setOnOff(onoff);
		inicioLleno = false;
		
	}
	public void stateChanged(ChangeEvent e) {
		umbralizar = Barra_Umbralizar.getValue();
		System.out.println(umbralizar);
	}
	private static BufferedImage resize(BufferedImage img) { 

		int anchoInicial = img.getWidth();
		int altoInicial = img.getHeight();

		if(!(altoInicial> altoMaximo || anchoInicial>anchoMaximo)){
			return img;
		}
		//throw new Exception("\nImagen demasiado grande. \nLa Imagen es: ["+anchoActual+"x"+altoActual+"] y el maximo es: [390x500]. ");
	    
		//Calcular tamaño escalado
		int anchoNuevo = anchoInicial;
		int altoNuevo = altoInicial;
		int i = 10;
		while((anchoNuevo > anchoMaximo || altoNuevo > altoMaximo)&&(i>0)){
			i--;
			if(anchoNuevo < anchoMaximo){
				anchoNuevo = (anchoNuevo*altoMaximo)/altoNuevo;
				altoNuevo = altoMaximo; //390
			}
			else{
				
				altoNuevo = (altoNuevo*anchoMaximo)/anchoNuevo;
				anchoNuevo = anchoMaximo; //500
			}
		}
		System.out.println("Imagen redimensionada de: "+anchoInicial+"x"+altoInicial+" a "+anchoNuevo+"x"+altoNuevo);
		
		//Cambiar tamaño
		Image tmp = img.getScaledInstance(anchoNuevo, altoNuevo, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(anchoNuevo, altoNuevo, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	private void abrirArchivo(String direccion) throws Exception{
		// Obtiene la direccion del archivo y lo instancia en icon
		// ImageIcon icon;// = new ImageIcon(direccion);
		textArea.setText("");
		imagenEntrada = ImageIO.read(new File(direccion));
		imagenEntrada = resize(imagenEntrada);
		onoff = false;
		ecualizarOnOff();
		
		inicioLleno = false;
		iconOriginal = new ImageIcon(imagenEntrada);
		// Setea el labelImagen con el archivo obtenido
		labelImagenOriginal.setIcon(iconOriginal);
		labelImagenProcesada.setIcon(iconOriginal);
		arrayMinutias = new ArrayList<Minutia>();
		umbralizar = Barra_Umbralizar.getValue();
		tarea = 0;
		
		pintarTarea(0);
		
		

	}
	private void llenarArray(){
		if(!inicioLleno){
			matriz = new Matriz(imagenEntrada.getWidth(), imagenEntrada.getHeight());
			Matriz matrizAux = matriz;
			array.add(0, matrizAux);
			matrizAux = procesar.procesar(1, imagenEntrada, array.get(0), umbralizar, arrayMinutias);
			array.add(1, matrizAux);
			matrizAux = procesar.procesar(2, imagenEntrada, array.get(1), umbralizar, arrayMinutias);
			array.add(2, matrizAux);
			inicioLleno = true;
		}
		int i = 2;
		if(tarea==6) arrayMinutias = new ArrayList<Minutia>();
		while (i-1 < tarea-1){
			i++;
			Matriz matrizAux;
			matrizAux = procesar.procesar(i, imagenEntrada, array.get(i - 1), umbralizar, arrayMinutias);
			array.add(i, matrizAux);
		}
	}
	private void pintarTarea(int especifica){
		tarea = especifica;
		if(tarea == -1){
			return;
		}
		llenarArray();
		ImageIcon ico1 = iconOriginal;
		ImageIcon ico2 = iconOriginal;
		switch (tarea) {
		case 0:
			ico1 = iconOriginal;
			ico2 = iconOriginal;
			break;
		case 1:
			ico1 = iconOriginal;
			ico2 = new ImageIcon(array.get(tarea).getImagenGris());
			break;
		case 2:
			ico1 = new ImageIcon(array.get(tarea-1).getImagenGris());
			ico2 = new ImageIcon(array.get(tarea).getImagenGris());
			break;
		case 3:
			ico1 = new ImageIcon(array.get(tarea-1).getImagenGris());
			ico2 = new ImageIcon(array.get(tarea).getImagenBinariaConRojo());
			break;
		case 4:
			ico1 = new ImageIcon(array.get(tarea-1).getImagenBinariaConRojo());
			ico2 = new ImageIcon(array.get(tarea).getImagenBinariaConRojo());
			break;
		case 5:
			ico1 = new ImageIcon(array.get(tarea-1).getImagenBinariaConRojo());
			ico2 = new ImageIcon(array.get(tarea).getImagenBinariaConRojo());
			break;
		case 6:
			ico1 = new ImageIcon(array.get(tarea-1).getImagenBinariaConRojo());
			ico2 = new ImageIcon(array.get(tarea).getImagenBinariaConRojo());
			break;
		default:
			return;
		}
		labelImagenOriginal.setIcon(ico1);
		labelImagenProcesada.setIcon(ico2);
		cambiarNombres();
	}
	private void cambiarNombres() {

		switch (tarea) {
		case 0:
			panelImagenOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Original"));
			panelImagenProcesada.setBorder(BorderFactory.createTitledBorder("Copia Imagen Original"));
			
			Boton_Anterior.setEnabled(false);
			Boton_Procesar.setEnabled(true);
			btnUmbralAutomatico.setEnabled(false);
			tglbtnecualizar.setEnabled(true);
			
			btnEscalaDeGrises.setEnabled(true);
			btnEcualizar.setEnabled(true);
			btnBinarizar.setEnabled(true);
			btnFiltrar.setEnabled(true);
			btnAdelgazamiento.setEnabled(true);
			btnExtraccionDeMinutias.setEnabled(true);
			btnAngulos.setEnabled(false);
			
			
			break;
		case 1:
			panelImagenOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Original"));
			panelImagenProcesada.setBorder(BorderFactory.createTitledBorder("Imagen Grises"));
			
			Boton_Anterior.setEnabled(true);
			Boton_Procesar.setEnabled(true);
			btnUmbralAutomatico.setEnabled(false);
			tglbtnecualizar.setEnabled(true);
			
			btnEscalaDeGrises.setEnabled(false);
			btnEcualizar.setEnabled(true);
			btnBinarizar.setEnabled(true);
			btnFiltrar.setEnabled(true);
			btnAdelgazamiento.setEnabled(true);
			btnExtraccionDeMinutias.setEnabled(true);
			btnAngulos.setEnabled(false);
			break;
		case 2:
			panelImagenOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Grises"));
			panelImagenProcesada.setBorder(BorderFactory.createTitledBorder("Imagen Ecualizada"));
			
			Boton_Anterior.setEnabled(true);
			Boton_Procesar.setEnabled(true);
			btnUmbralAutomatico.setEnabled(false);
			tglbtnecualizar.setEnabled(true);
			
			btnEscalaDeGrises.setEnabled(true);
			btnEcualizar.setEnabled(false);
			btnBinarizar.setEnabled(true);
			btnFiltrar.setEnabled(true);
			btnAdelgazamiento.setEnabled(true);
			btnExtraccionDeMinutias.setEnabled(true);
			btnAngulos.setEnabled(false);
			break;
		case 3:
			panelImagenOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Ecualizada"));
			panelImagenProcesada.setBorder(BorderFactory.createTitledBorder("Imagen Binarizada"));
			
			Boton_Anterior.setEnabled(true);
			Boton_Procesar.setEnabled(true);
			btnUmbralAutomatico.setEnabled(true);
			tglbtnecualizar.setEnabled(true);
			
			btnEscalaDeGrises.setEnabled(true);
			btnEcualizar.setEnabled(true);
			btnBinarizar.setEnabled(false);
			btnFiltrar.setEnabled(true);
			btnAdelgazamiento.setEnabled(true);
			btnExtraccionDeMinutias.setEnabled(true);
			btnAngulos.setEnabled(false);
			break;
		case 4:
			panelImagenOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Binarizada"));
			panelImagenProcesada.setBorder(BorderFactory.createTitledBorder("Imagen Filtrada"));
			
			Boton_Anterior.setEnabled(true);
			Boton_Procesar.setEnabled(true);
			btnUmbralAutomatico.setEnabled(true);
			tglbtnecualizar.setEnabled(true);
			
			btnEscalaDeGrises.setEnabled(true);
			btnEcualizar.setEnabled(true);
			btnBinarizar.setEnabled(true);
			btnFiltrar.setEnabled(false);
			btnAdelgazamiento.setEnabled(true);
			btnExtraccionDeMinutias.setEnabled(true);
			btnAngulos.setEnabled(false);
			break;
		case 5:
			panelImagenOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Filtrada"));
			panelImagenProcesada.setBorder(BorderFactory.createTitledBorder("Imagen Adelgazada"));
			
			Boton_Anterior.setEnabled(true);
			Boton_Procesar.setEnabled(true);
			btnUmbralAutomatico.setEnabled(true);
			tglbtnecualizar.setEnabled(true);
			
			btnEscalaDeGrises.setEnabled(true);
			btnEcualizar.setEnabled(true);
			btnBinarizar.setEnabled(true);
			btnFiltrar.setEnabled(true);
			btnAdelgazamiento.setEnabled(false);
			btnExtraccionDeMinutias.setEnabled(true);
			btnAngulos.setEnabled(false);
			break;
		case 6:
			panelImagenOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Adelgazada"));
			panelImagenProcesada.setBorder(BorderFactory.createTitledBorder("Imagen Extracción de Minutias"));
			
			Boton_Anterior.setEnabled(true);
			Boton_Procesar.setEnabled(false);
			btnUmbralAutomatico.setEnabled(true);
			tglbtnecualizar.setEnabled(true);
			
			btnEscalaDeGrises.setEnabled(true);
			btnEcualizar.setEnabled(true);
			btnBinarizar.setEnabled(true);
			btnFiltrar.setEnabled(true);
			btnAdelgazamiento.setEnabled(true);
			btnExtraccionDeMinutias.setEnabled(false);
			btnAngulos.setEnabled(true);
			break;
		default:
			break;
		}
	}
}
