package compartit;
import java.util.*;
import java.io.*;

/**
 *
 * @author david.casas.vidal
 */
public class DriverBB {
    private static BB bb = null;
    private static QAP q = null;
    private static double[][] aff = null;
    private static double[][] dist = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            boolean quit = false;
            while ( (line = br.readLine()) != null && !quit){
                String[] com = line.split(" ");
                switch(com[0]){
                    case "readQAP" :
                    {
                        try{
                            System.out.println("Introduce nombre del archivo:");
                            String l = br.readLine();
                            BufferedReader br2 = new BufferedReader (new FileReader(l));
                            String[] s = br2.readLine().split(" ");
                            int index=0;
                            for(int i = 0; i < s.length; ++i){
                                if(!(s[i].equals("") || s[i].equals(" "))) index = i;
                            }
                            int dim = Integer.parseInt(s[index]);
                            System.out.println("Dimension del problema: "+dim);

                            aff = new double[dim][dim];
                            dist = new double[dim][dim];

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
                            System.out.println("Matriz flujos leida.");

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
                            
                            q = new QAP(aff,dist);
                            
                        } catch (IOException e){
                            System.out.println("No se encuentra el archivo.\n"
                                    + "Lectura abortada.");
                        }
                        break;
                    }
                    case "makeBB":
                    {
                        if (q==null){
                            System.out.println("Cal llegir un problema abans de crear"
                                    + "el solucionador.\n"
                                    + "Fes: readQAP");
                        }
                        bb = new BB(q.freq, q.dist);
                        System.out.println("Clase BB instanciada.");
                    }
                    case "solve":
                    {
                        if(bb==null){
                            System.out.println("Cal instanciar BB abans de resoldre.\n"
                                    + "Fes: makeBB");
                        }
                        else{
                            int[] x = bb.calcularAssignacions(aff,dist);
                            System.out.println("El resultat es: ");
                            System.out.println(x.toString());
                            System.out.println("El cost associat es: ");
                            System.out.println(q.costOf(x));
                        }
                    }
                    case "quit":
                    {
                        quit = true;
                        System.out.println("Adiós!");
                    }
                    default:
                    {
                        System.out.println("Comanda desconeguda.");
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}