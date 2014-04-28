package compartit;

/**
 * @author Dani Torramilans
 * 
 */
public abstract class SolucionadorQAP {
	private final CalcularDistancies calcDist;
	private final CalcularAfinitats calcAfin;
	public SolucionadorQAP(CalcularAfinitats a, CalcularDistancies d) {
		calcDist = d;
		calcAfin = a;
	}
	public int[] solucionar(Objecte[] objs, Lloc[] llocs) {
		return calcularAssignacions(calcAfin.calcularMatriuAfinitats(objs),calcDist.calcularMatriuDistancies(llocs));
	}
    protected abstract int[] calcularAssignacions(double[][] af, double[][] distancies);
}
