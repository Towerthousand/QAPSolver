package compartit;

/**
 * @author Dani Torramilans
 * 
 */
public abstract class SolucionadorQAP {
	public int[] assignacions = new int[0]; //s'actualitzarà cada cop que es truqui calcularAssignacions
    public SolucionadorQAP(){}
    public abstract int[] calcularAssignacions(double[][] af, double[][] distancies);
}
