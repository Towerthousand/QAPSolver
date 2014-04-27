package compartit;
import java.util.*;
/**
 * @author David Casas
 * 
 * Thanks to Nathan Brixius for his QAP explanations.
 */
public class BB {
    /**
     * @param 
     */
    private double fita;
    private final GLB glb = new GLB();
   
    /**
     * Creadora
     */
    public BB(){}
	
    /**
     * Retorna el vector d'assignacions
     */
    public int[] calcularAssignacions(double[][] af, Vector<Lloc> l){
        int[] assign = new int[l.size()];
        double cost = 0;
        Stack<Node> s = new Stack();
        s.push(new Node(new QAP(calcDist(l), af)));
        while(!s.empty()){
            Node n = s.pop();
            if(n.fita <= cost ){
                if (true/*EASY BITACHES*/){
                    //ENUMERATE BIATCHES
                }
                else{
                    n.fita = glb.calcularFita(n.qap.freq, n.qap.dist);
                    if (n.fita <= cost){
                        Node[] vn = new Node[0];
                        for(Node sn : vn){
                            s.push(sn);
                        }
                    }
                }
            }
        }
        return assign;
    }
    
    private class Node{
        public QAP qap;
        public double fita;
        public int size(){
            return qap.size();
        }
        public Node(QAP q){
            qap = q;
            //GLB g = new GLB();
            //fita = g.calcularFita(this.qap.freq, this.qap.dist);
        }
    }
    
    private class QAP{
        public double[][] dist;
        public double[][] freq;
        public double offset;
        
        public QAP(double[][] distances, double[][] freq){}
        
        public int size(){
            return dist.length;
        }
        
        public double costOf(int[] pos){
            double res = offset;
            for (int i = 0; i<dist.length; i++){
                for (int j = 0; j<dist.length; j++){
                    res+=freq[i][j]*dist[pos[i]][pos[j]];
                }
            }
            return res;
        }
    }
    
    private double[][] calcDist(Vector<Lloc> l){
        double[][] dist = new double[l.size()][l.size()];
        for (int i = 0; i<l.size(); ++i){
            for (int j = i; j<l.size(); ++j){
                int x = l.elementAt(i).getX() - l.elementAt(j).getX();
                int y = l.elementAt(i).getY() - l.elementAt(j).getY();
                double d = x*x + y*y;
                d = Math.sqrt(d);
                dist[i][j] = d;
                dist[j][i] = d;
            }
        }
        return dist; 
    }
    
}
