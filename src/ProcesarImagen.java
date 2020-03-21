import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcesarImagen {
	static int margen = 1;
	static int margenM = 8;
	final int[][] nbrs = { { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 },
			{ 0, -1 } };
	final int[][][] nbrGroups = { { { 0, 2, 4 }, { 2, 4, 6 } }, { { 0, 2, 6 }, { 0, 4, 6 } } };
	List<Point> toWhite = new ArrayList<>();
	int arriba, abajo, derecha, izquierda;
	static int numeroPixelInicial = 50;
	int tarea;
	boolean ecuOnOff;

	public ProcesarImagen() {
		ecuOnOff = true;
	}
	public void setOnOff(boolean bool){
		ecuOnOff = bool;
	}
	
	public Matriz procesar(int tarea, BufferedImage entrada, Matriz matriz, int UmbralUsuario,
			ArrayList<Minutia> arrayMinutias) {
		switch (tarea) {
		case 1:
			return escalaGrises(entrada, matriz);
		case 2:
			if(ecuOnOff) return ecualizar(matriz);
			return escalaGrises(entrada, matriz); //TODO
		case 3:
			return binarizar(matriz, UmbralUsuario);
		case 4:
			return filtrar(matriz);
		case 5:
			calcularMargen(matriz);
			return adelgazamiento(matriz);
		case 6:
			return extraccionDeMinutias(matriz, arrayMinutias);
		}
		return matriz;
	}

	private Matriz escalaGrises(BufferedImage entrada, Matriz matriz) {
		for (int alto = 0; alto < entrada.getHeight(); alto++) {
			for (int ancho = 0; ancho < entrada.getWidth(); ancho++) {
				int rgb = entrada.getRGB(ancho, alto);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);
				int nivelGris = (r + g + b) / 3;
				if (!matriz.setPixel(ancho, alto, nivelGris)) {
					return matriz;
				}
			}
		}
		// System.out.println("fin metodo Escala grises.");
		return matriz;
	}
	
	public Matriz ecualizar(Matriz imagenentrada){
		int width = imagenentrada.getWidth();
		int height = imagenentrada.getHeight();
		Matriz imagenecualizada = new Matriz(width, height);
		int tampixel = width * height;
		int[] histograma = new int[256];
		// Calculamos frecuencia relativa de ocurrencia
		// de los distintos niveles de gris en la imagen
		for (int x = 1; x < width; x++) {
			for (int y = 1; y < height; y++) {
				int valor = imagenentrada.getPixel(x, y);
				histograma[valor]++;
			}
		}
		int sum = 0;
		// Construimos la Lookup table LUT
		float[] lut = new float[256];
		for (int i = 0; i < 256; ++i) {
				
			sum += histograma[i];
			lut[i] = sum * 255 / tampixel;
			
		}
		// Se transforma la imagen utilizando la tabla LUT

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int valor = imagenentrada.getPixel(x, y);
				int valorNuevo = (int) lut[valor];
				imagenecualizada.setPixel(x, y, valorNuevo);
			}
		}
		return imagenecualizada;

	}

	public int calcularUmbral(Matriz matriz) {
		int width = matriz.getWidth();
		int height = matriz.getHeight();

		double Umbral = 1;
		double sum = 0;
		int cont = 0;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				double valor = (double) matriz.getPixel(x, y);
				if (valor > 250)
					valor = valor * 0.5;
				sum = sum + valor * 0.8;
				cont++;
			}
		}
		Umbral = (sum / cont);
		return (int) Umbral;
	}

	private Matriz binarizar(Matriz matriz, int UmbralUsuario) {

		int width = matriz.getWidth();
		int height = matriz.getHeight();
		Matriz matrizSalida = new Matriz(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int valor = matriz.getPixel(x, y);
				if (valor < UmbralUsuario) {
					matrizSalida.setPixel(x, y, 0);
				} else {
					matrizSalida.setPixel(x, y, 1);
				}
			}
		}
		// System.out.println("fin metodo Binarizar.");
		return matrizSalida;
	}

	private Matriz filtrar(Matriz matriz) {

		int width = matriz.getWidth();
		int height = matriz.getHeight();
		Matriz matrizSalida = new Matriz(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				matrizSalida.setPixel(x, y, matriz.getPixel(x, y));
			}
		}
		for (int x = margen; x < width - margen; x++) {
			for (int y = margen; y < height - margen; y++) {
				boolean b = (matriz.getPixel(x, y - 1) == 0 ? false : true);
				boolean d = (matriz.getPixel(x - 1, y) == 0 ? false : true);
				boolean p = (matriz.getPixel(x, y) == 0 ? false : true);
				boolean e = (matriz.getPixel(x + 1, y) == 0 ? false : true);
				boolean g = (matriz.getPixel(x, y + 1) == 0 ? false : true);

				// Funcion 1
				boolean B1 = p | b && g && (d | e) | d && e && (b | g);

				matrizSalida.setPixel(x, y, (B1 == false) ? 0 : 1);
			}
		}
		for (int x = margen; x < width - margen; x++) {
			for (int y = margen; y < height - margen; y++) {
				boolean a = (matriz.getPixel(x - 1, y - 1) == 0 ? false : true);
				boolean b = (matriz.getPixel(x, y - 1) == 0 ? false : true);
				boolean c = (matriz.getPixel(x + 1, y - 1) == 0 ? false : true);
				boolean d = (matriz.getPixel(x - 1, y) == 0 ? false : true);
				boolean p = (matriz.getPixel(x, y) == 0 ? false : true);
				boolean e = (matriz.getPixel(x + 1, y) == 0 ? false : true);
				boolean f = (matriz.getPixel(x - 1, y + 1) == 0 ? false : true);
				boolean g = (matriz.getPixel(x, y + 1) == 0 ? false : true);
				boolean h = (matriz.getPixel(x + 1, y + 1) == 0 ? false : true);

				// Funcion 2
				boolean B2 = p && ((a | b | d) && (e | g | h) | (b | c | e) && (d | f | g));

				matrizSalida.setPixel(x, y, (B2 == false) ? 0 : 1);
			}
		}
		// System.out.println("fin metodo Filtrar.");
		return matrizSalida;
	}

	private int numNeighbors(int r, int c, Matriz matrizSalida) {
		int count = 0;
		for (int i = 0; i < nbrs.length - 1; i++)
			if (matrizSalida.getPixel(r + nbrs[i][1], c + nbrs[i][0]) == 0)
				count++;
		return count;
	}

	private int numTransitions(int r, int c, Matriz matrizSalida) {
		int count = 0;
		for (int i = 0; i < nbrs.length - 1; i++)
			if (matrizSalida.getPixel(r + nbrs[i][1], c + nbrs[i][0]) == 1) {
				if (matrizSalida.getPixel(r + nbrs[i + 1][1], c + nbrs[i + 1][0]) == 0)
					count++;
			}
		return count;
	}

	private boolean atLeastOneIsWhite(int r, int c, int step, Matriz matrizSalida) {
		int count = 0;
		int[][] group = nbrGroups[step];
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < group[i].length; j++) {
				int[] nbr = nbrs[group[i][j]];
				if (matrizSalida.getPixel(r + nbr[1], c + nbr[0]) == 1) {
					count++;
					break;
				}
			}
		return count > 1;
	}

	private Matriz adelgazamiento(Matriz matriz) {
		int width = matriz.getWidth();
		int height = matriz.getHeight();
		Matriz matrizSalida = new Matriz(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				matrizSalida.setPixel(x, y, matriz.getPixel(x, y));
			}
		}

		boolean firstStep = false;
		boolean hasChanged;
		do {
			hasChanged = false;
			firstStep = !firstStep;

			for (int x = margen; x < matrizSalida.getWidth() - margen; x++) {
				for (int y = margen; y < matrizSalida.getHeight() - margen; y++) {

					if (matrizSalida.getPixel(x, y) != 0)
						continue;

					int nn = numNeighbors(x, y, matrizSalida);
					if (nn < 2 || nn > 6)
						continue;

					if (numTransitions(x, y, matrizSalida) != 1)
						continue;

					if (!atLeastOneIsWhite(x, y, firstStep ? 0 : 1, matrizSalida))
						continue;

					toWhite.add(new Point(y, x));
					hasChanged = true;
				}
			}

			for (Point p : toWhite)
				matrizSalida.setPixel(p.y, p.x, 1);
			toWhite.clear();

		} while ((firstStep | hasChanged));

		// System.out.println("fin metodo Adelgazar.");
		return matrizSalida;
	}

	private Matriz extraccionDeMinutias(Matriz matriz, ArrayList<Minutia> arrayMinutias) {
		int width = matriz.getWidth();
		int height = matriz.getHeight();
		Matriz matrizSalida = new Matriz(width, height);
		// arrayMinutias= new ArrayList<Minutia>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				matrizSalida.setPixel(x, y, matriz.getPixel(x, y));
			}
		}

		for (int x = izquierda; x < derecha; x++) {
			for (int y = arriba; y < abajo; y++) {
				if (matrizSalida.getPixel(x, y) != 0)
					continue;
				int CN = 0;
				CN = numTransitions(x, y, matrizSalida);
				if (CN == 1 || CN == 3) {
					Minutia minutia = new Minutia(x, y, CN, 0);
					arrayMinutias.add(minutia);
					// matrizSalida.setPixel(x, y, 2);
				}

			}
		}
		System.out.println("Minutias encontradas: " + arrayMinutias.size() + ".");
		// pintarMinutias(matrizSalida, arrayMinutias);

		// System.out.println("fin metodo ExtraccionDeMinutias.");
		pintarMinutias(matrizSalida, arrayMinutias);
		return matrizSalida;
	}

	private void pintarMinutias(Matriz matrizSalida, ArrayList<Minutia> arrayMinutias) {
		for (int m = 0; m < arrayMinutias.size(); m++) {
			int xMinutia = arrayMinutias.get(m).getX();
			int yMinutia = arrayMinutias.get(m).getY();
			if (arrayMinutias.get(m).getTipo() == 1) {
				// matrizSalida.setPixel(xMinutia, yMinutia, 2);
				matrizSalida.setPixel(xMinutia + 1, yMinutia, 2);
				matrizSalida.setPixel(xMinutia, yMinutia + 1, 2);
				matrizSalida.setPixel(xMinutia + 1, yMinutia + 1, 2);
				matrizSalida.setPixel(xMinutia - 1, yMinutia, 2);
				matrizSalida.setPixel(xMinutia, yMinutia - 1, 2);
				matrizSalida.setPixel(xMinutia + 1, yMinutia - 1, 2);
				matrizSalida.setPixel(xMinutia - 1, yMinutia + 1, 2);
				matrizSalida.setPixel(xMinutia - 1, yMinutia - 1, 2);
			} else {
				// matrizSalida.setPixel(xMinutia, yMinutia, 3);
				matrizSalida.setPixel(xMinutia + 1, yMinutia, 3);
				matrizSalida.setPixel(xMinutia, yMinutia + 1, 3);
				matrizSalida.setPixel(xMinutia + 1, yMinutia + 1, 3);
				matrizSalida.setPixel(xMinutia - 1, yMinutia, 3);
				matrizSalida.setPixel(xMinutia, yMinutia - 1, 3);
				matrizSalida.setPixel(xMinutia + 1, yMinutia - 1, 3);
				matrizSalida.setPixel(xMinutia - 1, yMinutia + 1, 3);
				matrizSalida.setPixel(xMinutia - 1, yMinutia - 1, 3);
			}
		}
	}

	public Matriz calcularAngulo(Matriz matriz, ArrayList<Minutia> arrayMinutias){
		int anguloFinal = -360;
		int width = matriz.getWidth();
		int height = matriz.getHeight();
		Matriz matrizSalida = new Matriz(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				matrizSalida.setPixel(x, y, matriz.getPixel(x, y));
			}
		}

		String textoDelArchivo = "";

		for (int i = 0; i < arrayMinutias.size(); i++) {
			Minutia minutia = arrayMinutias.get(i);
			// int Gx=0;
			// int Gy=0;
			// double rad=0;
			// double angulo=0;
			if (minutia.getTipo() == 1) {
				int xActual = minutia.getX();
				int yActual = minutia.getY();
				for (int j = 0; j < 6; j++) {
					int xSiguiente = xActual;
					int ySiguiente = yActual;
					matrizSalida.setPixel(xActual, yActual, 2);
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							int xAux = xActual + x;
							int yAux = yActual + y;
							if (matrizSalida.getPixel(xAux, yAux) == 0) {
								// System.out.println("Procesando pixel:
								// "+xAux+","+yAux);
								xSiguiente = xAux;
								ySiguiente = yAux;
								matrizSalida.setPixel(xAux, yAux, 2);
							}
						}
					}
					xActual = xSiguiente;
					yActual = ySiguiente;
				}
				int Gx = (xActual - minutia.getX());
				int Gy = -(yActual - minutia.getY());

				double anguloRadadianes = Math.atan2(Gy, Gx);
				double angulo = Math.toDegrees(anguloRadadianes);
				anguloFinal = (int) angulo;

				textoDelArchivo = textoDelArchivo + "Minutia " + i + " (" + minutia.getX() + "," + minutia.getY()
						+ ") de tipo: " + minutia.getTipo() + " \tAngulo: " + anguloFinal + "\n";
			}
			
			if (minutia.getTipo() == 3) {
				int angulo1_X = -0;
				int angulo1_Y = -0;
				int angulo2_X = -0;
				int angulo2_Y = -0;
				int angulo3_X = -0;
				int angulo3_Y = -0;
				int angulo1 = -360;
				int angulo2 = -360;
				int angulo3 = -360;
				
				for(int k = 0; k<3; k++){
					int xActual = minutia.getX();
					int yActual = minutia.getY();
					for (int j = 0; j < 6; j++) {
						int xSiguiente = xActual;
						int ySiguiente = yActual;
						matrizSalida.setPixel(xActual, yActual, 2);
						boolean encontrado = false;
						for (int x = -1; x <= 1; x++) {
							for (int y = -1; y <= 1; y++) {
								int xAux = xActual + x;
								int yAux = yActual + y;
								if ((matrizSalida.getPixel(xAux, yAux) == 0)&&!encontrado) {
									encontrado = true;
									// System.out.println("Procesando pixel:
									// "+xAux+","+yAux);
									xSiguiente = xAux;
									ySiguiente = yAux;
									matrizSalida.setPixel(xAux, yAux, 2);
								}
							}
						}
						xActual = xSiguiente;
						yActual = ySiguiente;
					}
					int Gx = (xActual - minutia.getX());
					int Gy = -(yActual - minutia.getY());

					double anguloRadadianes = Math.atan2(Gy, Gx);
					double angulo = Math.toDegrees(anguloRadadianes);
					//System.out.println("Minutia " + i + " Angulo: " + angulo + "\n");
					
					switch (k){
						case 0:
							angulo1_X = xActual;
							angulo1_Y = yActual;
							angulo1 = (int)angulo;
							break;
						case 1:
							angulo2_X = xActual;
							angulo2_Y = yActual;
							angulo2 = (int)angulo;
							break;
						case 2:
							angulo3_X = xActual;
							angulo3_Y = yActual;
							angulo3 = (int)angulo;
							break;
					}
				}
				int xFinal=(angulo1_X-minutia.getX())+(angulo2_X-minutia.getX())+(angulo3_X-minutia.getX());
				int yFinal=(angulo1_Y-minutia.getY())+(angulo2_Y-minutia.getY())+(angulo3_Y-minutia.getY());

				int GxFinal = (xFinal);
				int GyFinal = -(yFinal);

				double anguloRadadianes = Math.atan2(GyFinal, GxFinal);
				double angulo = Math.toDegrees(anguloRadadianes);
				anguloFinal = (int) angulo;

				textoDelArchivo = textoDelArchivo +
						"Minutia " + i +
						" (" + minutia.getX() + "," + minutia.getY()+ ") de tipo: " + minutia.getTipo() +
						" \tAngulos = "+angulo1+" / "+angulo2+" / "+angulo3+
						" \tAngulo Final = "+anguloFinal+" Mediante la suma de las coordenadas polares\n";
			}
			
//			if (minutia.getTipo() == 3) {
//				int angulo1 = -360;
//				int angulo2 = -360;
//				int angulo3 = -360;
//				
//				for(int k = 0; k<3; k++){
//					int xActual = minutia.getX();
//					int yActual = minutia.getY();
//					for (int j = 0; j < 6; j++) {
//						int xSiguiente = xActual;
//						int ySiguiente = yActual;
//						matrizSalida.setPixel(xActual, yActual, 2);
//						boolean encontrado = false;
//						for (int x = -1; x <= 1; x++) {
//							for (int y = -1; y <= 1; y++) {
//								int xAux = xActual + x;
//								int yAux = yActual + y;
//								if ((matrizSalida.getPixel(xAux, yAux) == 0)&&!encontrado) {
//									encontrado = true;
//									// System.out.println("Procesando pixel:
//									// "+xAux+","+yAux);
//									xSiguiente = xAux;
//									ySiguiente = yAux;
//									matrizSalida.setPixel(xAux, yAux, 2);
//								}
//							}
//						}
//						xActual = xSiguiente;
//						yActual = ySiguiente;
//					}
//					int Gx = (xActual - minutia.getX());
//					int Gy = -(yActual - minutia.getY());
//
//					double anguloRadadianes = Math.atan2(Gy, Gx);
//					double angulo = Math.toDegrees(anguloRadadianes);
//					//System.out.println("Minutia " + i + " Angulo: " + angulo + "\n");
//					
//					switch (k){
//						case 0:
//							angulo1 = (int)angulo;
//							break;
//						case 1:
//							angulo2 = (int)angulo;
//							break;
//						case 2:
//							angulo3 = (int)angulo;
//							break;
//					}
//				}
//				anguloFinal = (angulo1+angulo2+angulo3)/3;
//
//				textoDelArchivo = textoDelArchivo +
//						"Minutia " + i +
//						" (" + minutia.getX() + "," + minutia.getY()+ ") de tipo: " + minutia.getTipo() +
//						" \tAngulos = "+angulo1+" / "+angulo2+" / "+angulo3+
//						" \tAngulo Final = "+anguloFinal+"\n";
//			}
			
			arrayMinutias.get(i).setAngulo(anguloFinal);
		}
		
		FileWriter fichero;
		try {
			fichero = new FileWriter("AngulosMinutias.txt");
			fichero.write(textoDelArchivo);
			fichero.close();
			System.out.println("Archivo 'AngulosMinutias.txt' creado y lleno");
			if (arrayMinutias.size() < 10) {
				System.out.println(textoDelArchivo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matrizSalida;
	}

//	private Matriz recortarImagen(Matriz matriz) {
//		int width = matriz.getWidth();
//		int height = matriz.getHeight();
//		Matriz matrizSalida = new Matriz(width, height);
//		for (int x = 0; x < width; x++) {
//			for (int y = 0; y < height; y++) {
//				matrizSalida.setPixel(x, y, matriz.getPixel(x, y));
//			}
//		}
//		calcularMargen(matrizSalida);
//		// limpiarMargenes(matrizSalida);
//		return matrizSalida;
//	}
//
//	private void limpiarMargenes(Matriz matriz) {
//		int height = matriz.getHeight();
//		int width = matriz.getWidth();
//		int margenDeLimpieza = margenM;
//		for (int x = 0; x < width; x++) {
//			for (int y = 0; y < height; y++) {
//				if ((x < (izquierda - margenDeLimpieza)) || (x > (derecha + margenDeLimpieza))
//						|| (y < (arriba - margenDeLimpieza)) || (y > (abajo + margenDeLimpieza))) {
//					matriz.setPixel(x, y, 1);
//				}
//			}
//		}
//	}

	private void calcularMargen(Matriz matriz) {
		int height = matriz.getHeight();
		int width = matriz.getWidth();
		// Ventor ancho
		int[] sumaAncho = new int[width];
		for (int i = 0; i < width; i++) {
			sumaAncho[i] = 0;
		}
		// Vector alto
		int[] sumaAlto = new int[height];
		for (int i = 0; i < height; i++) {
			sumaAlto[i] = 0;
		}
		// Suma
		for (int x = margenM; x < (width - margenM); x++) {
			for (int y = margenM; y < (height - margenM); y++) {
				if (matriz.getPixel(x, y) == 0) {
					sumaAncho[x] = sumaAncho[x] + 1;
					sumaAlto[y] = sumaAlto[y] + 1;
				}

			}
		}

		boolean arribaEncontrado = false;
		int i = 0;
		while (i < height && !arribaEncontrado) {
			if (sumaAlto[i] > numeroPixelInicial) {
				arribaEncontrado = true;
				arriba = i;
			}
			i++;
		}

		boolean abajoEncontrado = false;
		i = height - 1;
		while (i > 0 && !abajoEncontrado) {
			if (sumaAlto[i] > numeroPixelInicial) {
				abajoEncontrado = true;
				abajo = i;
			}
			i--;
		}
		boolean izquierdaEncontrado = false;
		i = 0;
		while (i < width && !izquierdaEncontrado) {
			if (sumaAncho[i] > numeroPixelInicial) {
				izquierdaEncontrado = true;
				izquierda = i;
			}
			i++;
		}
		boolean derechaEncontrado = false;
		i = width - 1;
		while (i > 0 && !derechaEncontrado) {
			if (sumaAncho[i] > numeroPixelInicial) {
				derechaEncontrado = true;
				derecha = i;
			}
			i--;
		}

		// Retoque de los bordes por si son demasiado grandes
		int altoMinimo = (height / 3);
		int altoActual = (abajo) - (arriba);
		// System.out.print("Alto actual: "+altoActual+" ");
		if ((altoActual < altoMinimo) || !arribaEncontrado || !abajoEncontrado) {
			arriba = margenM;
			abajo = matriz.getHeight() - margenM;
			// System.out.print("Alto minimo: "+altoMinimo+"... forzando... ");
		}
		int anchoMinimo = (width / 3);
		int anchoActual = (derecha) - (izquierda);
		// System.out.print("Ancho actual: "+anchoActual+" ");
		if ((anchoActual < anchoMinimo) || !derechaEncontrado || !izquierdaEncontrado) {
			izquierda = margenM;
			derecha = matriz.getWidth() - margenM;
			// System.out.print("Ancho minimo: "+anchoMinimo+"... forzando...
			// ");
		}

		// System.out.println("\nBorde de Arriba: "+arriba+" Borde de Abajo:
		// "+(height-abajo)+" Borde de Izquierda: "+izquierda+" Borde de
		// Derecha: "+(width-derecha));
	}
}
