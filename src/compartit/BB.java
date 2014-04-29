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
    public int[] calcularAssignacions(double[][] afinitats, double[][] distancies){
        int[] assign = new int[afinitats.length];
        double cost = 0;
        Stack<Node> s = new Stack();
        s.push(new Node(new QAP(distancies, afinitats)));
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
    
    private Branch Branching(Node n, double fita, double[][] S){
        double[] rowSum = new double[n.size()];
        double[] colSum = new double[n.size()];
        for (int i = 0; i < n.size(); i++) {
            for (int j = 0; j < n.size(); j++) {
                rowSum[i] += S[i][j];
                colSum[j] += S[i][j];
            }
        }     
        int rowind = 0, colind = 0;
        double rowbest = rowSum[0], colbest = colSum[0];
        for (int i=1; i<n.size(); ++i){
            if(rowbest < rowSum[i]){
                rowbest = rowSum[i];
                rowind = i;
            }
            if(colbest < colSum[i]){
                colbest = colSum[i];
                colind = i;
            }
        }
        
        if(rowbest > colbest){
            return new Branch(true, rowind);
        }
        else{
            return new Branch(false, colind);
        } 
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
    
    private class Branch{
        public Boolean isRowBranch;
        public int index;
        public Branch(Boolean b, int i){
            isRowBranch = b;
            index = i;
        }
    }
    
}
