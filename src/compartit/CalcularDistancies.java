package compartit;

/**
 *
 * @author Joan Vilatimó
 */
public class CalcularDistancies {
	
	public CalcularDistancies() {
	}
	
   /**
     * 
     * Retorna la matriu de distancies a partir del vector de llocs de tots
     * els objectes
     * 
     * @param llocs
     * @return MatriuDistancies
     */
    public double[][] calcularMatriuDistancies(Lloc[] llocs) {
        double[][] MatriuDistancies = new double[llocs.length][llocs.length]; 
        for (int i = 0; i < llocs.length; ++i) {
            MatriuDistancies[i][i] = 0;
            for (int j = i+1; j < llocs.length; ++j) {
                double dist = Math.pow(llocs[i].getPosicioX() - llocs[j].getPosicioX(), 2);
                dist += Math.pow(llocs[i].getPosicioY() - llocs[j].getPosicioY(), 2);
                dist += Math.pow(llocs[i].getPosicioZ() - llocs[j].getPosicioZ(), 2);
                dist = Math.sqrt(dist);   
                MatriuDistancies[i][j] = dist;
                MatriuDistancies[j][i] = dist;
            }
        }
        return MatriuDistancies;
    }
}
