package compartit;
import java.util.*;
import java.io.*;

/**
 *
 * @author david.casas.vidal
 */
public class DriverBB {
    private static BB.QAP q = null;
    private static BB.Node n = null;
    private static BB.Branch b = null;
    private static BB brunchy = null;
    private static BB.Node[] descent = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ( (line = br.readLine()) != null){
                String[] com = line.split(" ");
                switch(com[0]){
                    case "CalcularAssignacions" :
                    {
                        if (brunchy == null){
                            System.out.println("No has hecho makeBB!");
                        }
                        else if (q == null){
                            System.out.println("No has introducido ningun QAP!");
                        }
                        else{
                            int[] x = brunchy.calcularAssignacions(q.freq, q.dist);
                            System.out.println("L'assignació és:");
                            System.out.println(Arrays.toString(x));
                        }
                        break;
                    }
                    case "makeBB" :
                    {
                        brunchy = new BB(new Basura(), new CalcularDistancies());
                        System.out.println("BB creado!");
                        break;
                    }
                    case "branchDecision" :
                    {
                        if (brunchy == null){
                            System.out.println("Debes crear un BB para poder usar este metodo");
                        }
                        else if(n == null){
                            System.out.println("Debe existir un nodo sobre el que calcular la"
                                    + "branching decision.");
                        }
                        else{
                            b = brunchy.branching(n);
                            System.out.println("Index = "+b.index);
                            System.out.println("IsRowBranch = "+b.isRowBranch);
                        }
                        break;
                    }
                    case "doBranch" :
                    {
                        if (n == null) {
                            System.out.println("Debe existir un nodo sobre el que"
                                    + "realizar el branching");
                        }
                        else{
                            descent = n.branch();
                            System.out.println("Se han creado los nodos hijos");
                            System.out.println("Numero de hijos: "+descent.length);
                        }
                        break;
                    }
                    case "focusChild" :
                    {
                        if (descent == null){
                            System.out.println("No has hecho ningun doBranch!");
                        }
                        else if(com.length != 2){
                            System.out.println("Necesita un argumento: qué hijo quieres ver?");
                        }
                        else{
                            int x = Integer.parseInt(com[1]);
                            if(x < 0 || x > descent.length){
                                System.out.println("Índice incorrecto!");
                            }
                            else{
                                n = descent[x];
                                System.out.println("Focus set on child # "+x);
                            }
                        }
                        break;
                    }
                    case "makeNode" :
                    {
                        if (q == null){
                            System.out.println("Debes crear un QAP para ver su tamaño.");
                        }
                        else{
                            int[] v = new int[q.size()];
                            for (int i = 0; i<v.length; ++i) v[i] = -1;
                            n = new BB.Node(q, v, q.size());
                        }
                        System.out.println("Nodo creado!");
                        break;
                    }
                    case "nodeSize" :
                    {
                        if ( n == null){
                            System.out.println("Debes crear un nodo para ver su tamaño");
                        }
                        else System.out.println(n.size());
                        break;
                    }
                    case "currAssign" :
                    {
                        if (n == null){
                            System.out.println("Debes crear un nodo par ver su asignación");
                        }
                        else System.out.println(Arrays.toString(n.currassign));
                        break;
                    }
                    case "isAlmostSolved" :
                    {
                        if (n == null){
                            System.out.println("Debes crear un nodo par ver si está "
                                    + "casi resuelto");
                        }
                        else System.out.println(n.isAlmostSolved());
                        break;
                    }
                    case "whatsLeft":
                    {
                        if (n == null){
                            System.out.println("Debes crear un nodo par ver qué "
                                    + "objetos y lugares faltan por asignar");
                        }
                        else{
                            int[][] x = n.whatsLeft();
                            System.out.println("objs "+Arrays.toString(x[0]));
                            System.out.println("llocs "+Arrays.toString(x[1]));
                        }
                        break;
                    }
                    case "showBound":
                    {
                        if(n == null){
                            System.out.println("Debes crear un nodo para poder"
                                    + " ver su cota");
                        }
                        else{
                            System.out.println("La cota es "+n.fita);
                        }
                        break;
                    }
                    case "showCostMatrix":
                    {
                        if(n == null){
                            System.out.println("Debes crear un nodo para poder"
                                    + " ver su matriz de costes");
                        }
                        else{
                            for(int i=0; i<n.C.length; i++){
                                System.out.println(Arrays.toString(n.C[i]));
                            }
                        }
                        break;
                    }
                    case "QAP" :
                    {
                        try{
                            System.out.println("Introduce nombre del archivo:");
                            String l = br.readLine();
                            BufferedReader br2 = new BufferedReader (new FileReader(l));
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

                            q = new BB.QAP(dist, aff);
                            System.out.println("QAP ha sido creado");
                        } catch (IOException e){
                            System.out.println("No se encuentra el archivo.");
                        }
                        break;
                    }
                    case "showNodeAff":
                    {
                        if (n == null){
                            System.out.println("Debes crear un nodo para ver su matriz"
                                    + " de afinidades.");
                        }
                        else{
                            for(int i=0; i<n.size(); ++i)
                                System.out.println(Arrays.toString(n.qap.freq[i]));
                        }
                        break;
                    }
                    case "showAff":
                    {
                        if (q == null){
                            System.out.println("Debes crear un QAP para ver su matriz"
                                    + " de afinidades.");
                        }
                        else{
                            for(int i=0; i<q.size(); ++i)
                                System.out.println(Arrays.toString(q.freq[i]));
                        }
                        break;
                    }
                    case "showNodeDist":
                    {
                        if (q == null){
                            System.out.println("Debes crear un nodo para ver su matriz"
                                    + " de distancias.");
                        }
                        else{
                            for(int i=0; i<n.size(); ++i)
                                System.out.println(Arrays.toString(n.qap.dist[i]));
                        }
                        break;
                    }
                    case "showDist":
                    {
                        if (q == null){
                            System.out.println("Debes crear un QAP para ver su matriz"
                                    + " de distancias.");
                        }
                        else{
                            for(int i=0; i<q.size(); ++i)
                                System.out.println(Arrays.toString(q.dist[i]));
                        }
                        break;
                    }
                    case "costOf":
                    {
                        if(q == null){
                            System.out.println("Has de crear un QAP abans de buscar-ne el cost.");
                        }
                        else if(com.length != q.size()+1){
                            System.out.println("Una assignacio ha de tenir exactament"
                                    + q.size() +" elements");
                        } else {
                            int[] v = new int[q.size()];
                            for(int i=0; i<q.size(); ++i)
                                v[i] = Integer.parseInt(com[i+1]);
                            double b = q.costOf(v);
                            System.out.println("El cost de l'assignacio "+Arrays.toString(v)+" es "+
                                    b+".");
                        }
                        break;
                    }
                    case "qapSize":
                    {
                        if(q == null){
                            System.out.println("Has de crear un QAP abans de buscar-ne la mida.");
                        }
                        else
                            System.out.println("La mida del problema és: "+q.size());
                        break;
                    }
                    case "reduce":
                    {
                        if(q == null){
                            System.out.println("Has de crear un QAP abans de buscar-ne la mida.");
                        }
                        else if(com.length != 3){
                            System.out.println("Cal dir quina fila i columna seran eliminades");
                        }
                        else{
                            q = q.reduced(Integer.parseInt(com[1]), Integer.parseInt(com[2]));
                            System.out.println("Fila "+com[1]+" i columna "+com[2]+" eliminades.");                           
                        }
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