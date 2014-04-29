package compartit;
import java.util.*;
/**
 * @author David Casas
 * 
 * Thanks to Nathan Brixius for his QAP explanations.
 */
public class BB extends SolucionadorQAP{
    /**
     * @param 
     */
    private double fita;
    private final GLB glb = new GLB();
    private QAP qap_init;

    public BB(CalcularAfinitats a, CalcularDistancies d) {
        super(a, d);
    }

    /**
     * Retorna el vector d'assignacions
     */
    public int[] calcularAssignacions(double[][] afinitats, double[][] distancies){
        qap_init = new QAP(distancies, afinitats);
        int[] assign = new int[afinitats.length]; //results
        double cost = Double.POSITIVE_INFINITY; //results.objective
        int[] v = new int[assign.length];
        for(int x : v) x = -1;
        Stack<Node> s = new Stack();
        s.push(new Node(qap_init, v));
        while(!s.empty()){
            Node n = s.pop();
            if(n.fita <= cost ){
                cost = n.fita;
                assign = n.currassign;
                if (true/*ONLY 3 POSITIONS LEFT TO ASSIGN*/){
                    /*BACKTRACK THEM AND PICK BEST*/
                } else  {
                    Node[] vn = n.Branch(); 
                    for(Node sn : n.Branch()){
                        s.push(sn);
                    }
                }
            }
        }
        return assign;
    }
    


    //IDEA PARA BB1 y BB2
    //===================
    //Convertir esta en abstract y implementar Weak Branching
    //y Strong Branching en clases distintas.
    //--------------------------------------------------------
    //Ahora mismo: weak branching
    protected Branch Branching(Node n){
        return CoreBranching(n);
    }
    
    private Branch CoreBranching(Node n){
        double[] rowSum = new double[n.C.length];
        double[] colSum = new double[n.C.length];
        for (int i = 0; i < n.C.length; i++) {
            for (int j = 0; j < n.C.length; j++) {
                rowSum[i] += n.C[i][j];
                colSum[j] += n.C[i][j];
            }
        }     
        int rowind = 0, colind = 0;
        double rowbest = rowSum[0], colbest = colSum[0];
        for (int i=1; i<n.C.length; ++i){
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
        public double[][] C;
        public int[] currassign;
        
        public int size(){
            return qap.size();
        }
        public Node(QAP q, int[] v){
            qap = q;
            currassign = v;
            GLB g = new GLB();
            fita = g.calcularFita(this.qap.freq, this.qap.dist);
            //C = g.BLABLABLA Matriu Costes
        }
        public Node[] Branch(){
            Branch b = Branching(this);
            // HAY QUE HACERLO
            //REMEMBER TO SORT THEM 
            //(recommend using a map to place them when finding and then sorting
            // the keyset. Then getting stuff from map in that order.)
            return new Node[0];
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
