package compartit;

/**
 *
 * @author 
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
                double dist = Math.pow(llocs[i].getPosicioX() - llocs[i+1].getPosicioX(), 2);
                dist += Math.pow(llocs[i].getPosicioY() - llocs[i+1].getPosicioY(), 2);
                dist += Math.pow(llocs[i].getPosicioZ() - llocs[i+1].getPosicioZ(), 2);
                dist = Math.sqrt(dist);   
                MatriuDistancies[i][j] = dist;
                MatriuDistancies[j][i] = dist;
            }
        }
        return MatriuDistancies;
    }
}
