package compartit;

/**
 * @author Dani Torramilans
 * 
 */
public abstract class SolucionadorQAP {
	private final CalcularDistancies calcDist;
	private final CalcularAfinitats calcAfin;
	double[][] a;
	double[][] d;
	
	public SolucionadorQAP(CalcularAfinitats ca, CalcularDistancies cd) {
		calcDist = cd;
		calcAfin = ca;
		a = null;
		d = null;
	}

	public SolucionadorQAP(double[][] afinitats, double[][] distancies) {
		calcDist = null;
		calcAfin = null;
		a = afinitats;
		d = distancies;
	}
	public int[] solucionar(Objecte[] objs, Lloc[] llocs) throws Exception {
		if(calcDist == null) throw new Exception("S'ha de utilitzar una instancia de CalularAfinitats i CalcularDistancies per utilitzar aquesta funció");
		return calcularAssignacions(calcAfin.calcularMatriuAfinitats(objs),calcDist.calcularMatriuDistancies(llocs));
	}
 
	public int[] solucionar() throws Exception {
		if(a == null) throw new Exception("S'han de passar les dues matrius del QAP a la constructora per utilitzar aquesta funció");
		return calcularAssignacions(a,d);
	}
	
    protected abstract int[] calcularAssignacions(double[][] af, double[][] distancies) throws Exception;
}
