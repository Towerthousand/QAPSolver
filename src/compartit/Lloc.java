package compartit;

public class Lloc {
	private int posicioX;
	private int posicioY;
	private int posicioZ;
   /**
     * Creadora
     * 
     * @param x posició X inicial
     * @param y posició Y inicial
     * @param z posició Z inicial
     * 
     */
	public Lloc(int x, int y, int z) {
		setPosicioX(x);
		setPosicioY(y);
		setPosicioZ(z);
	}
   /**
     * Retorna la posicio X
     * 
     */
	public int getPosicioX() {
		return posicioX;
	}
   /**
     * Assigna la posicio X
     * 
     * @param posicioX
     * 
     */
	public void setPosicioX(int posicioX) {
		this.posicioX = posicioX;
	}
   /**
     * Retorna la posicio Z
     * 
     */
	public int getPosicioZ() {
		return posicioZ;
	}
   /**
     * Assigna la posicio Z
     * 
     * @param posicioZ
     * 
     */
	public void setPosicioZ(int posicioZ) {
		this.posicioZ = posicioZ;
	}
   /**
     * Retorna la posicio Z
     * 
     */
	public int getPosicioY() {
		return posicioY;
	}
   /**
     * Assigna la posicio Y
     * 
     * @param posicioY
     * 
     */
	public void setPosicioY(int posicioY) {
		this.posicioY = posicioY;
	}
}
