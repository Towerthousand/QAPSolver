package compartit;

public class Solucio {
	public int[] assignacions; //s'actualitzarà cada cop que es truqui a recalcular
    private final SolucionadorQAP solucionador;
	public Solucio(SolucionadorQAP s, Objecte[] objs, Lloc[] llocs) { //li passes un objecte BB o un Heuristic
    	solucionador = s;
    	recalcular(objs, llocs);
    }
	
	public void recalcular(Objecte[] objs, Lloc[] llocs) {
		assignacions = solucionador.solucionar(objs, llocs);
	}
}
