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
    public BB(double[][] aff, double[][] dist){
        super(aff, dist);
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
        PriorityQueue<Node> s = new PriorityQueue<>();
        s.add(new Node(init_qap, v, 0));
        while(s.size() != 0){
            //System.out.println(s.size());
            Node n = s.poll();
            if(n.fita < cost){
                //cost = n.fita;
                //assign = n.currassign.clone();
                if (n.isAlmostSolved()){
                    System.out.println("im in " + cost + " "+ n.hashCode());
                    int[][] whatsleft = n.whatsLeft();
                    ArrayList<int[]> ss = permutations(whatsleft[1]);
                    for(int[] p : ss){
                        int[] current_asign = n.currassign.clone();
                        //System.out.println(Arrays.toString(assign) + " " + Arrays.toString(whatsleft[0]));
                        for(int i = 0; i<3; ++i){
                            current_asign[whatsleft[0][i]] = p[i];
                        }
                        double newcost = init_qap.costOf(current_asign);
                        if( newcost < cost){
                            assign = current_asign.clone();
                            cost = newcost;
                        }
                    }
                    
                } else  {
                    Node[] xx = n.branch();
                    for(Node sn : xx){
                        if(sn.fita < cost ) s.add(sn);
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
   
    private static Branch branching(Node n)
//    protected static Branch weakBranching(Node n)
    {
        return coreBranching(n, n.C);
    }
    
//    protected static Branch strongBranching(Node n)
//    {
//        double[][] U = new double[n.size()-1][n.size()-1];
//        for(int i = 0; i<n.size(); ++i){
//            for(int j = 0; j<n.size(); ++j){
//                QAP q = n.qap.reduced(i, j);
//                U[i][j] = GLB.calcularFita(q.freq, q.dist) - n.fita;
//            }
//        }
//        return coreBranching(n, U);
//    }
    
   /**
    *
    * @param n Node actual de l'espai de solucions.
    * @return Branca per la que es seguirà.
    */
   private static Branch coreBranching(Node n, double[][] CostMatrix)
   {
       double[] rowSum = new double[CostMatrix.length];
       double[] colSum = new double[CostMatrix.length];
       for (int i = 0; i < CostMatrix.length; i++) {
           for (int j = 0; j < CostMatrix.length; j++) {
               rowSum[i] += CostMatrix[i][j];
               colSum[j] += CostMatrix[i][j];
           }
       }
       int rowind = 0, colind = 0;
       double rowbest = rowSum[0], colbest = colSum[0];
       for (int i=1; i<CostMatrix.length; ++i){
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
    
    protected static class Node implements Comparable<Node>{
        public QAP qap;
        public double fita;
        public double[][] C;
        public int[] currassign;
        public double shift;
        
        public int size(){
            return qap.size();
        }
        public Node(QAP q, int[] v, double extracost){
            qap = q;
            currassign = v;
            shift = extracost;
            fita = GLB.calcularFita(this.qap.freq, this.qap.dist) + extracost;
            C = GLB.lawler.clone();
        }
        
        @Override
        public int compareTo(Node n)
        {
            if (this.fita > n.fita) return 1;
            else if (this.fita == n.fita) return 0;
            else return -1;
        }
        public Node[] branch(){ 
            Branch b = branching(this);
            Node[] res = new Node[qap.size()];
            if (b.isRowBranch)
                for(int i=0; i<qap.size(); ++i){
                    res[i] = new Node(qap.reduced(b.index,i), 
                            newAssign(currassign,b.index,i), shift + C[b.index][i]);
                }
            else
                for(int i=0; i<qap.size(); ++i){
                    res[i] = new Node(qap.reduced(i,b.index), 
                            newAssign(currassign,i,b.index), shift + C[i][b.index]);
                }
            return res;
        }
        public Boolean isAlmostSolved()
        {
            return (qap.size()<=3);
        }
        public int[][] whatsLeft()
        {
            int[] llocs = new int[qap.size()], objs = new int[qap.size()];
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
                    k++;
                }
            }
            int[][] res = {objs,llocs};
            return res;
        }
        
        private int[] newAssign(int[] v, int i, int j)
        {
            int[] res = v.clone();
            int[] checklist = new int[v.length];
            for(int k = 0; k<v.length; ++k){
                if(v[k]!=-1){
                    if(k<=i) ++i;
                    checklist[v[k]] = 1;
                }
            }
            for (int k=0; k<v.length; ++k){
                if(checklist[k] == 1 && k<=j) ++j;
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
