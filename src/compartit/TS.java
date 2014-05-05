package compartit;
import java.util.ArrayList;
import java.util.Random;
/**
 * Classe que implementa una solució heurística al QAP, utilitzant una
 * Tabu Search.
 * 
 * @author Dani Torramilans
 */
public class TS extends SolucionadorQAP {
	public class Swap {
		public int x;
		public int y;
		public Swap(int a, int b) {
			x = a;
			y = b;
		}

		@Override
		public boolean equals(Object object) {
			boolean same = false;
			if (object != null && object instanceof Swap)
				same = (x == ((Swap) object).x && y == ((Swap) object).y);
			return same;
		}
	}

	public TS(CalcularAfinitats a, CalcularDistancies d) {
        super(a, d);
    }
	
	public TS(double[][] afinitats, double[][] distancies) {
        super(afinitats, distancies);
    }
	
	public int numIterations = 5;
	public int maxTabuListSize = 3;
	private int N;
	private double[][] FLOW;
	private double[][] DIST;
	
	// Implementing Fisher–Yates shuffle
	private void shuffleArray(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	public void printSolution(int[] solution) {
		System.out.print("Solution: [");
		for(int i = 0; i < N; ++i) {
			if(i != 0) System.out.print(", ");
			System.out.print(solution[i]);
		}
		System.out.println("] Cost: " + cost(solution));
	}

	private void checkInputMatrices() throws Exception {
		for(int i = 0; i < N; ++i) {
			if(FLOW[i][i] != 0.0) throw new Exception("FLOW matrix diagonal not zero at element " + i);
			if(DIST[i][i] != 0.0) throw new Exception("DIST matrix diagonal not zero at element " + i);
			for(int j = 0; j < i; ++j) {
				if(FLOW[i][j] != FLOW[j][i]) throw new Exception("FLOW matrix is not simmetric at elements " + i + ", " + j + "." );
				if(DIST[i][j] != DIST[j][i]) throw new Exception("DIST matrix is not simmetric at elements " + i + ", " + j + "." );
			}
		}
	}

	private double cost(int[] solution) {
		double res = 0;
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				res+=FLOW[i][j]*DIST[solution[i]][solution[j]];
			}
		}
		return res;
	}

	private ArrayList<int[]> neighbours(int[] solution) {
		ArrayList<int[]> neighbours = new ArrayList<int[]>(); 
		for(int i = 0; i < N; ++i){
			for(int j = i+1; j < N; ++j) {
				int[] newSol = new int[N];
				System.arraycopy(solution, 0, newSol, 0, N);
				int temp = newSol[i];
				newSol[i] = newSol[j];
				newSol[j] = temp;
				neighbours.add(newSol);
			}
		}
		return neighbours;
	}

	private Swap getDiff(int[] s1, int[] s2) {
		Swap swap = new Swap(-1,-1); 
		for(int i = 0; i < N; ++i) {
			if(s1[i] != s2[i]) {
				swap.x = i;
				break;
			}
		}
		if(swap.x != -1){
			for(int i = swap.x+1; i < N; ++i) {
				if(s1[i] != s2[i]) {
					swap.y = i;
					break;
				}
			}
		}
		return swap;
	}

	private int[] getLocalBest(ArrayList<int[]> candidateList) throws Exception{
		if(candidateList.size() == 0) throw new Exception("candidate list empty");
		int[] sol = candidateList.get(0);
		double cost = cost(sol);
		for(int i = 0; i < candidateList.size(); ++i) {
			double c2 = cost(candidateList.get(i));
			if(cost > c2) {
				cost = c2;
				sol = candidateList.get(i);
			}
		}
		return sol;
	}

	@Override
	protected int[] calcularAssignacions(double[][] af, double[][] distancies) throws Exception {
		N = af.length;
		FLOW = af;
		DIST = distancies;
		checkInputMatrices();
		
		int[] bestSol = new int[N];
		for(int i = 0; i < N; ++i) bestSol[i] = i;
		shuffleArray(bestSol); //initial, random solution
		
		ArrayList<Swap> tabuList = new ArrayList<Swap>();
		ArrayList<int[]> candidateList = new ArrayList<int[]>();
		int notGettingBetter = 0;
		for(int iter = 0; iter < numIterations && notGettingBetter != numIterations/5; ++iter) {
			candidateList.clear();
			ArrayList<int[]> neighbours = neighbours(bestSol);
			for(int i = 0; i < neighbours.size(); ++i) {
				if(tabuList.contains(getDiff(bestSol, neighbours.get(i))) == false || cost(bestSol) > cost(neighbours.get(i)))
					candidateList.add(neighbours.get(i));
			}
			int[] localBest = getLocalBest(candidateList);
			if(cost(localBest) < cost(bestSol)) {
				tabuList.add(getDiff(localBest, bestSol));
				bestSol = localBest;
				notGettingBetter = 0;
			}
			else ++notGettingBetter;
			if((notGettingBetter > 5 || tabuList.size() > maxTabuListSize) && tabuList.size() > 0) tabuList.remove(0);
		}
		return bestSol;
	}
}