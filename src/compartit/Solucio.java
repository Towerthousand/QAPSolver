package compartit;

public class Solucio {
	public int[] assignacions; //s'actualitzarà cada cop que es truqui a recalcular
    private final SolucionadorQAP solucionador;
	public Solucio(SolucionadorQAP s, Objecte[] objs, Lloc[] llocs) throws Exception { //li passes un objecte BB o un Heuristic
    	solucionador = s;
		assignacions = solucionador.solucionar(objs, llocs);
    }

	public Solucio(SolucionadorQAP s) throws Exception { //li passes un objecte BB o un Heuristic
    	solucionador = s;
    	assignacions = solucionador.solucionar();
    }
	
	public void recalcular(Objecte[] objs, Lloc[] llocs) throws Exception {
		assignacions = solucionador.solucionar(objs, llocs);
	}
}
