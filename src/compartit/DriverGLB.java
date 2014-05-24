package compartit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Joan Vilatimó Quilez
 */
public class DriverGLB {
    /**
     * 
     * @param args the command line arguments
     * @throws java.io.IOException
    */
    
    private static void printMatrix(double[][] matrix) {
        for(int i = 0; i < matrix.length; ++i) {
            for(int j = 0; j < matrix.length; ++j) { 
		if(j != 0) System.out.print("\t");
		System.out.print(matrix[i][j]);
            }
            System.out.println();
	}
    }
    
    public static void main(String[] args) throws IOException {
        boolean exit = false;
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        while(!exit) {
            System.out.println("Driver Gilmore-Lawler Bound (GLB)");
            System.out.print("Introdueix el nom del fitxer del joc de prova: ");
            String filename = entrada.readLine();
            double fita;
            double[][] aff, dist, cost;
            try {
                BufferedReader br2 = new BufferedReader (new FileReader(filename));
                int dim = Integer.parseInt(br2.readLine());
                System.out.println("Dimensio del problema: " + dim);
                aff = new double[dim][dim];
                dist = new double[dim][dim];
                cost = new double[dim][dim];
                //llegeix primera matriu (A/afinitats)
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
                System.out.println("Matriu A (afinitats):");
                printMatrix(aff);
                //llegeix la segona matriu (B/distancies)
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
                System.out.println("Matriu B (distancies):");
                printMatrix(dist);
                //llegeix la tercera matriu (C/costos)
                br2.readLine();
                for(int i = 0; i<dim; ++i){
                    String[] x = br2.readLine().split(" ");
                    int k =0;
                    for(int j = 0; j<dim; ++j){
                        while(x[k].equals("")) ++k;
                        cost[i][j] = Double.parseDouble(x[k]);
                        k++;
                    }
                }
                System.out.println("Matriu C (costos):");
                printMatrix(cost);
                //algoritme
                System.out.println("Calculant...");
                fita = GLB.calcularFita(aff,dist,cost);
                System.out.println("Fita = " + fita);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print("Introdueix el nom d'un fitxer del joc de prova o bé escriu 'q' per sortir: ");
            exit = (entrada.readLine().equals("q"));
        }
    }
}
