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
                if (n.isAlmostSolved()){
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
    private Branch Branching(Node n){
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
            //GLB g = new GLB();
            fita = GLB.calcularFita(this.qap.freq, this.qap.dist);
            //C = g.BLABLABLA Matriu Costes
        }
        public Node[] Branch(){
            Branch b = Branching(this);
            Node[] res = new Node[qap.size()];
            Map<Double,Node> map = new HashMap<Double,Node>();
            if (b.isRowBranch)
                for(int i=0; i<qap.size(); ++i){
                    Node n = new Node(qap.Reduced(b.index,i), newAssign(currassign,b.index,i));
                    map.put(n.fita, n);
                }
            else
                for(int i=0; i<qap.size(); ++i){
                    Node n = new Node(qap.Reduced(i,b.index), newAssign(currassign,i,b.index));
                    map.put(n.fita, n);
                }
            Double[] set = (Double[]) map.keySet().toArray();
            Arrays.sort(set, Collections.reverseOrder());
            for(int i=0; i<qap.size(); ++i){
                res[i] = map.get(set[i]);
            }
            return res;
        }
        public Boolean isAlmostSolved(){
            int count = 0;
            for(int x : currassign) if (x==-1) ++count;
            return (count<=3);
        }
        
        private int[] newAssign(int[] v, int i, int j){
            int[] res = v.clone();
            for(int k = 0; k<v.length; ++k){
                if(v[k]!=-1){
                    if(k<=i) ++i;
                    if(v[k]<=j) ++j;
                }
            }
            res[i]=j;
            return res;
        }
    }
    
    private class QAP{
        public double[][] dist;
        public double[][] freq;
        
        public QAP(double[][] distances, double[][] freq){}
        
        public int size(){
            return dist.length;
        }
        
        public double costOf(int[] pos){
            double res = 0;
            for (int i = 0; i<dist.length; i++){
                for (int j = 0; j<dist.length; j++){
                    res+=freq[i][j]*dist[pos[i]][pos[j]];
                }
            }
            return res;
        }
        
        public QAP Reduced(int i, int j){
            double[][] d = Reduce(dist,i,j);
            double[][] f = Reduce(freq,i,j);
            return new QAP(d,f);
        }
        
        private double[][] Reduce(double[][] m, int i, int j){
            double[][] res = new double[m.length-1][m.length-1];
            for(int ii = 0; ii<res.length; ii++){
                for(int jj = 0; jj<res[ii].length; jj++){
                    res[ii][jj] = m[ii < i ? ii : ii + 1][jj < j ? jj : jj + 1];
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
