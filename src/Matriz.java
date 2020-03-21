import java.awt.image.BufferedImage;

public class Matriz {

	short[][] matriz;
	int ancho;
	int alto;

	public Matriz(int ancho_, int alto_) {
		ancho = ancho_;
		alto = alto_;
		matriz = new short[ancho_][alto_];
		// System.out.println("ancho: "+ancho+"alto: "+alto);
	}

	public void crearMatriz(BufferedImage imagenEntrada) {
		alto = imagenEntrada.getHeight();
		ancho = imagenEntrada.getWidth();
	}

	public int getWidth() {
		return ancho;
	}

	public int getHeight() {
		return alto;
	}

	public boolean setPixel(int ancho_, int alto_, int nivelGris) {
		if (ancho_ > ancho || alto_ > alto) {
			System.out.println("Error en el metodo setRGB, parametros fuera de la matriz.");
			return false;
		}
		matriz[ancho_][alto_] = (short) nivelGris;
		return true;
	}

	public int getPixel(int ancho, int alto) {
		int pixel = matriz[ancho][alto];
		return pixel;
	}

	public BufferedImage getImagenGris() {
		BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		for (int alto_ = 0; alto_ < alto; alto_++) {
			for (int ancho_ = 0; ancho_ < ancho; ancho_++) {
				int valor = matriz[ancho_][alto_];
				int pixelRGB = (255 << 24 | valor << 16 | valor << 8 | valor);
				salida.setRGB(ancho_, alto_, pixelRGB);
			}
		}
		return salida;
	}
	
	public BufferedImage getImagenBinariaConRojo() {
		BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		for (int alto_ = 0; alto_ < alto; alto_++) {
			for (int ancho_ = 0; ancho_ < ancho; ancho_++) {
				int valor = matriz[ancho_][alto_];
				if (valor == 0 || valor == 1){
					int valorBinario = valor * 255;
					int pixelRGB = (255 << 24 | valorBinario << 16 | valorBinario << 8 | valorBinario);
					salida.setRGB(ancho_, alto_, pixelRGB);
				} 
				if(valor == 2){
					int pixelRGB = (255 << 24 | 255 << 16 | 104 << 8 | 20); //Naranja
//					int pixelRGB = (255 << 24 | 225 << 75 | 25 << 8 | 136); //
					salida.setRGB(ancho_, alto_, pixelRGB);
				}
				if(valor == 3){
					int pixelRGB = (255 << 24 | 255 << 16 | 0 << 8 | 0); // Rojo
					salida.setRGB(ancho_, alto_, pixelRGB);
				}
			}
		}
		return salida;
	}
	
}
