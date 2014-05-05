package compartit;

import java.io.BufferedReader;
import java.io.FileReader;

public class TSDriver {
	//PER ACABAR. Simplement prova l'algorisme.
	private static double cost(int[] solution, double[][] a, double[][] d) {
		double res = 0;
		for (int i = 0; i < solution.length; i++){
			for (int j = 0; j < solution.length; j++){
				res+=a[i][j]*d[solution[i]][solution[j]];
			}
		}
		return res;
	}
	
	private static void printSolution(int[] solution, double[][] a, double[][] d) {
		System.out.print("Solution: [");
		for(int i = 0; i < solution.length; ++i) {
			if(i != 0) System.out.print(", ");
			System.out.print(solution[i]);
		}
		System.out.println("] Cost: " + cost(solution, a, d));
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br2 = new BufferedReader (new FileReader("entrada.txt"));
		int dim = Integer.parseInt(br2.readLine());
		System.out.println("Dimension del problema: "+dim);

		double[][] aff = new double[dim][dim];
		double[][] dist = new double[dim][dim];

		br2.readLine();
		for(int i = 0; i<dim; ++i){
			String[] x = br2.readLine().split(" ");
			int k = 0;
			for(int j = 0; j<dim; ++j){
				while(x[k].equals("")) ++k;
				aff[i][j] = Double.parseDouble(x[k]);
				k++;
			}
		}
		System.out.println("Matriz similitudes leida.");

		br2.readLine();
		for(int i = 0; i<dim; ++i){
			String[] x = br2.readLine().split(" ");
			int k =0;
			for(int j = 0; j<dim; ++j){
				while(x[k].equals("")) ++k;
				dist[i][j] = Double.parseDouble(x[k]);
				k++;
			}
		}
		System.out.println("Matriz distancias leida.");
		br2.close();
		TS ts = new TS(aff,dist);
		Solucio sol = new Solucio(ts);
		printSolution(sol.assignacions,aff,dist);
	}
}
