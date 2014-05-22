package compartit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class TSDriver {
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
		System.out.print("Solució trobada: [");
		for(int i = 0; i < solution.length; ++i) {
			if(i != 0) System.out.print(", ");
			System.out.print(solution[i]);
		}
		System.out.println("] Cost: " + cost(solution, a, d));
	}

	private static void printMatrix(double[][] matrix) {
		for(int i = 0; i < matrix.length; ++i) {
			for(int j = 0; j < matrix.length; ++j) { 
				if(j != 0) System.out.print("\t");
				System.out.print(matrix[i][j]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		boolean exit = false;
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		while(!exit) {
			System.out.println("Hi han jocs de prova predefinits (ordenats per mida): entrada0.txt, entrada1.txt, entrada2.txt, entrada3.txt entrada4.txt");
			System.out.print("Introdueix el nom del fitxer del joc de prova: ");
			String filename = cin.readLine();
			BufferedReader br2 = new BufferedReader (new FileReader(filename));
			int dim = Integer.parseInt(br2.readLine());
			System.out.println("Dimension del problema: " + dim);

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
			System.out.println("Matriu afinitats:");
			printMatrix(aff);

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
			System.out.println("Matriu distancies:");
			printMatrix(dist);
			br2.readLine();
			System.out.print("Solucio esperada: ");
			System.out.println(br2.readLine());
			br2.close();

			//SOLUCIONA
			System.out.println("Calculant...");
			TS ts = new TS(aff,dist);
			Solucio sol = new Solucio(ts);
			printSolution(sol.assignacions,aff,dist);
			System.out.print("Vols provar un altre joc de prova? (escriu 'q' per sortir): ");
			exit = (cin.readLine().equals("q"));
		}
		cin.close();
		System.out.println("Fi del driver pel Tabu Search");
	}
}
