package compartit;

/**
 *
 * @author 
 */
public class CalcularDistancies {
        /**
     * 
     * Retorna la matriu de distancies a partir del vector de llocs de tots
     * els objectes
     * 
     * @param l
     * @return MatriuDistancies
     */
    public static double[][] calcularMatriuDistancies(Lloc[] l) {
        double[][] MatriuDistancies = new double[l.length][l.length]; 
        for (int i = 0; i<l.length; ++i) {
            MatriuDistancies[i][i] = 0;
            for (int j = i+1; j<l.length; ++j) {
                double dist = Math.pow(l[i].posicioX - l[i+1].posicioX, 2);
                dist += Math.pow(l[i].posicioY - l[i+1].posicioY, 2);
                dist = Math.sqrt(dist);   
                MatriuDistancies[i][j] = dist;
                MatriuDistancies[j][i] = dist;
            }
        }
        return MatriuDistancies;
    }
}
