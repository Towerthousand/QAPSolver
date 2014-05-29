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
                            
                            q = new QAP(dist,aff);
                            
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
                        break;
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
                            System.out.println(Arrays.toString(x));
                            System.out.println("El cost associat es: ");
                            System.out.println(q.costOf(x));
                        }
                        break;  
                    }
                    case "quit":
                    {
                        quit = true;
                        System.out.println("Adiós!\n <press ENTER to exit>");
                        break;
                    }
                    case "costOf":
                    {
                        if (q==null)
                            System.out.println("Cal llegir un cas!\n"
                                    + "Fes: readQAP");
                        else if (com.length != q.size()+1)
                            System.out.println("Nombre d'arguments erroni.");
                        else{
                            int[] x = new int[q.size()];
                            for (int i = 0; i<q.size(); ++i) x[i]=Integer.parseInt(com[i+1]);
                            double b = q.costOf(x);
                            System.out.println("El cost de l'assignacio és: "+b);
                        }
                        break;
                    }
                    case "showQAP":
                    {
                        if (q==null)
                            System.out.println("Cal llegir un cas!\n"
                                    + "Fes: readQAP");
                        System.out.println("Mida del problema actual: "+q.size());
                        System.out.println("\nMatriu Fluxos: ");
                        for(int i=0; i<q.size(); ++i) System.out.println(Arrays.toString(q.freq[i]));
                        System.out.println("\nMatriu Distancies: ");
                        for(int i=0; i<q.size(); ++i) System.out.println(Arrays.toString(q.dist[i]));
                        break;
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