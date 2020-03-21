
public class Minutia {
	private int X;
	private int Y;
	private int tipo;
	private int angulo;

	public Minutia(int x_, int y_, int tipo_, int angulo_) {
		X = x_;
		Y = y_;
		tipo = tipo_;
		angulo = angulo_;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

}
