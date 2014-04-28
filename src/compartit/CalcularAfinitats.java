package compartit;

/**
*
* @author Dani Torramilans
*/
public abstract class CalcularAfinitats {
	public CalcularAfinitats() {
	}
   /**
     * 
     * Retorna l'afinitat entre dos elements a, b
     * 
     * @param a
     * @param b
     * @return afinitat
     */
	protected abstract double afinitat(Objecte a, Objecte b); //Que cada grup implementi el seu
   /**
     * 
     * Retorna la matriu d'afinitats a partir de la funció d'afinitat
     * 
     * @param objs El vector d'objectes
     * @return MatriuAfinitats
     */
	public double[][] calcularMatriuAfinitats(Objecte[] objs) {
        double[][] MatriuAfinitats = new double[objs.length][objs.length]; 
        for (int i = 0; i < objs.length; ++i) {
        	MatriuAfinitats[i][i] = 0;
            for (int j = i+1; j < objs.length; ++j) {
                double dist = afinitat(objs[i], objs[j]);  
                MatriuAfinitats[i][j] = dist;
                MatriuAfinitats[j][i] = dist;
            }
        }
        return MatriuAfinitats;
	}
}
