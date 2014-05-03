package compartit;
import java.util.*;
/**
 * Classe que implementa una solució del QAP, mitjançant el
 * Branch & Bound algorithm.
 * 
 * @author David Casas
 * Thanks to Nathan Brixius for his QAP explanations.
 */
public class BB extends SolucionadorQAP{
    
    private QAP init_qap;
    public BB(CalcularAfinitats a, CalcularDistancies d)
    {
        super(a, d);
    }

 
     /**
     *
     * @param afinitats Matriu d'afinitats (fluxos).
     * @param distancies Matriu de distàncies.
     * @return Retorna el vector d'assignacions "v", on v[i] = j vol dir 
     * que l'objecte 'i' te assignat el lloc 'j'.
     */
    @Override
    public int[] calcularAssignacions(double[][] afinitats, double[][] distancies)
    {
        init_qap = new QAP(distancies, afinitats);
        int[] assign = new int[afinitats.length]; //results
        double cost = Double.POSITIVE_INFINITY; //results.objective
        int[] v = new int[assign.length];
        for (int i = 0; i<v.length; ++i) v[i] = -1;
        Stack<Node> s = new Stack();
        s.push(new Node(init_qap, v, init_qap.size()));
        while(!s.empty()){
            Node n = s.pop();
            if(n.fita <= cost ){
                cost = n.fita;
                assign = n.currassign;
                if (n.isAlmostSolved()){
                    int[][] whatsleft = n.whatsLeft();
                    ArrayList<int[]> ss = permutations(whatsleft[1]);
                    for(int[] x : ss){
                        int[] xx = n.currassign.clone();
                        for(int i = 0; i<3; ++i){
                            xx[whatsleft[2][i]] = x[i];
                        }
                        double newcost = init_qap.costOf(xx);
                        if( newcost < cost){
                            assign = xx;
                            cost = newcost;
                        }
                    }
                    
                } else  {
                    for(Node sn : n.branch()){
                        s.push(sn);
                    }
                }
            }
        }
        return assign;
    }
    
    private ArrayList<int[]> permutations(int[] a)
    {
        ArrayList<int[]> ret = new ArrayList<>();
        permutation(a, 0, ret);
        return ret;
    }

    private void permutation(int[] a, int pos, ArrayList<int[]> list)
    {
        if(a.length - pos == 1)
            list.add(a.clone());
        else
            for(int i = pos; i < a.length; i++){
                swap(a, pos, i);
                permutation(a, pos+1, list);
                swap(a, pos, i);
            }
    }

    private void swap(int[] arr, int i, int j)
    {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    
    //IDEA PARA subclasses
    //====================
    //Implementar Strong Branching en una subclase.
    //--------------------------------------------------------
    //En esta clase: weak branching
    protected static Branch branching(Node n)
    {
        return coreBranching(n);
    }
    
    /**
     *
     * @param n Node actual de l'espai de solucions.
     * @return Branca per la que es seguirà.
     */
    protected static Branch coreBranching(Node n)
    {
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
     
    protected static class Node{
        public QAP qap;
        public double fita;
        public double[][] C;
        public int[] currassign;
        public int emptyspaces;
        
        public int size(){
            return qap.size();
        }
        public Node(QAP q, int[] v, int x){
            qap = q;
            currassign = v;
            fita = GLB.calcularFita(this.qap.freq, this.qap.dist);
            C = GLB.lawler.clone();
            emptyspaces = x;
        }
        public Node[] branch(){
            Branch b = branching(this);
            Node[] res = new Node[qap.size()];
            Map<Double,Node> map = new HashMap<>();
            if (b.isRowBranch)
                for(int i=0; i<qap.size(); ++i){
                    Node n = new Node(qap.reduced(b.index,i), 
                            newAssign(currassign,b.index,i), emptyspaces-1);
                    map.put(n.fita, n);
                }
            else
                for(int i=0; i<qap.size(); ++i){
                    Node n = new Node(qap.reduced(i,b.index), 
                            newAssign(currassign,i,b.index), emptyspaces-1);
                    map.put(n.fita, n);
                }
            Object[] set = map.keySet().toArray();
            Arrays.sort(set, Collections.reverseOrder());
            for(int i=0; i<qap.size(); ++i){
                res[i] = map.get(set[i]);
            }
            return res;
        }
        public Boolean isAlmostSolved()
        {
            return (emptyspaces<=3);
        }
        //PRE: Només hi ha 3 posicions a -1 en currassign
        public int[][] whatsLeft()
        {
            int[] llocs = new int[emptyspaces], objs = new int[emptyspaces];
            int[] checklist = new int[currassign.length];
            int k = 0;
            for(int i = 0; i<currassign.length; ++i){
                if(currassign[i]==-1){
                    objs[k] = i;
                    k++;
                }
                else{
                    checklist[currassign[i]] = 1;
                }
            }
            k=0;
            for(int i = 0; i<currassign.length; ++i){
                if(checklist[i]!=1){
                    llocs[k] = i;
                    ++k;
                }
            }
            int[][] res = {objs,llocs};
            return res;
        }
        
        private int[] newAssign(int[] v, int i, int j)
        {
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
    
    protected static class QAP{
        public double[][] dist;
        public double[][] freq;
        
        public QAP(double[][] distances, double[][] freqs)
        {
            dist = distances;
            freq = freqs;
        }
        
        public int size()
        {
            return dist.length;
        }
        
        public double costOf(int[] pos)
        {
            double res = 0;
            for (int i = 0; i<dist.length; i++){
                for (int j = 0; j<dist.length; j++){
                    res+=freq[i][j]*dist[pos[i]][pos[j]];
                }
            }
            return res;
        }
        
        public QAP reduced(int i, int j)
        {
            double[][] d = reduce(dist,i,j);
            double[][] f = reduce(freq,i,j);
            return new QAP(d,f);
        }
        
        private double[][] reduce(double[][] m, int i, int j)
        {
            double[][] res = new double[m.length-1][m.length-1];
            for(int ii = 0; ii<res.length; ii++){
                for(int jj = 0; jj<res[ii].length; jj++){
                    res[ii][jj] = m[ii < i ? ii : ii + 1][jj < j ? jj : jj + 1];
                }
            }
            return res;
        }
    }
    
    protected static class Branch{
        public Boolean isRowBranch;
        public int index;
        public Branch(Boolean b, int i)
        {
            isRowBranch = b;
            index = i;
        }
    }
    
}
