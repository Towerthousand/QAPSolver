package compartit;

/**
 * Helper class for BB.
 * @author david.casas.vidal
 */
 public class QAP{
        protected double[][] dist;
        protected double[][] freq;
        //cost[i][j] cost of assigning object i to place j
        protected double[][] cost;
        //cost so-far
        protected double shift;
        
        public QAP(double[][] distances, double[][] freqs)
        {
            dist = distances;
            freq = freqs;
            cost = new double[dist.length][dist.length];
            shift = 0;
        }
        
        public QAP(double[][] distances, double[][] freqs, double[][] costs, double shifts)
        {
            dist = distances;
            freq = freqs;
            cost = costs;
            shift = shifts;
        }
        
        public QAP(QAP q)
        {
        	dist = q.dist;
        	freq = q.freq;
        	cost = q.cost;
        	shift = q.shift;
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
            double[][] d = reduce(dist,j,j);
            double[][] f = reduce(freq,i,i);
            double[][] c = reduce(cost,i,j);
            
            for (int k = 0; k<d.length; ++k){
            	for (int l = 0; l<d.length; ++l){
            		c[k][l] += 
                                2
                                * dist[l < j ? l : l + 1][j]
                                * freq[i][k < i ? k : k + 1];
            	}
            }
            
            double s = shift + cost[i][j];
            
            return new QAP(d,f,c,s);
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
    
