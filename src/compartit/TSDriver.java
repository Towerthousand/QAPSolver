package compartit;

import java.io.BufferedReader;
import java.io.FileReader;

public class TSDriver {
	//PER ACABAR. Simplement prova l'algorisme.
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
		ts.maxTabuListSize = 100;
		ts.numIterations = 1000000;
		Solucio sol = new Solucio(ts);
		ts.printSolution(sol.assignacions);
	}
}
