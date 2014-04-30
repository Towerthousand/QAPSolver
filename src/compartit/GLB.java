package compartit;

import java.util.*;

/**
 * Classe controladora per calcular la fita mitjançant l'algoritme
 * de Gilmore-Lawler per calcular la matriu de costos i l'algoritme 
 * hungar per finalment obtenir la fita.
 * 
 * @author Joan Vilatimó Quilez
 * 
 */

public class GLB {
    //Solucio
    public static double fita;
    public static double[][] lawler;

    
    /**
     * 
     * @param af
     * @param dist
     * @return 
     */
    
    public static double calcularFita(double[][] af, double[][] dist) {
        // Aplicar lawler per calcular matriu de costos
        calcularMatriuLawler(af, dist);
        return algoritmeHungar(lawler);
    }
    
    /**
     * 
     * Retorna la matriu de costos de gilmore-lawler aplicant l'algoritme de 
     * gilmore-lawler per un problema Koopmans-Beckman QAP(A,B,C) on A 
     * és la matriu d'afinitats(af), B la matriu de distancies(dist) i C és 0 i
     * les diagonals de A i B també són 0.
     * 
     * @param af
     * @param dist
     */
    private static void calcularMatriuLawler(double[][] af, double[][] dist) {
        int n = af[0].length;   //dist té el mateix tamany que af i les dues
                                // matrius són quadrades
        lawler = new double[n][n];
        // Calcular matrius afi i distj. Aquestes matrius contindràn 
        // els elements de la matriu af i dist treient les diagonals i ordenant
        // cada fila

        double[][] afi = new double[n][n-1];
        double[][] distj = new double[n][n-1];
        for(int i = 0; i<n; ++i){
            for(int j = 0; j<n; ++j) {
                if(i<j){
                    afi[i][j] = af[i][j];
                    distj[i][j] = dist[i][j];
                }
                else if(i>j){
                    afi[i][j-1] = af[i][j-1];
                    distj[i][j-1] = dist[i][j-1];
                }
            }
            // Ordenar cada fila de les matrius afi i distj
            Arrays.sort(afi[i]);
            Arrays.sort(distj,Collections.reverseOrder());
        }
        // Calcular producte escalar entre afi[i]*dist[j]
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int k = 0; k < n-1; ++k){
                    lawler[i][j] += afi[i][k]*distj[j][k];
                }
            }
        }
    }

    /**
     * 
     * calcula i retorna la fita utilitzant l'algoritme Hungar
     * suposa que lawler es una matriu quadrada
     * 
     * @param lawler
     * @return 
     */
    
    public static double algoritmeHungar(double[][] lawler) {
        double[][] matriu = new double[lawler.length][lawler[0].length];
        for(int i=0; i<lawler.length; i++)
            System.arraycopy(lawler[i], 0, matriu[i], 0, lawler[i].length);
                
        
        // restar el valor minim de totes les columnes i files
        restarMatriu(matriu);


        // els valors no negatius són les linies o columnes que tenen un 0 assignat
        // assignaFil té per cada fila la columna assignada que conté un 0
        // assignaCol té per cada columna la fila assignada que conté un 0
        int[] assignaFil = new int[matriu.length]; Arrays.fill(assignaFil,-1);
        int[] assignaCol = new int[matriu[0].length]; Arrays.fill(assignaCol,-1);
        int[] escollitFil = new int[matriu.length]; Arrays.fill(escollitFil,-1);

        // 1=marcada, 0=no marcada
        int[] filMarcades = new int[matriu.length];
        int[] colsMarcades = new int[matriu[0].length];

        // assigna qualsevol 0 que no tingui cap més 0 assignat a la mateixa fila
        // o columna
        assigna(matriu, assignaFil, assignaCol);
        marcarColAmbZeros(assignaCol,colsMarcades);

        while (!totMarcat(colsMarcades)) {
            
            int[] zeroNoMarcat = escollirZeroNoMarcat(matriu, escollitFil, filMarcades, colsMarcades);

            while (zeroNoMarcat == null) {
                // si no trobem 0s no marcats busquem el minim no marcat 
                // el sumem de totes les files marcades
                // i resta a totes les columnes no marcades
                crearZeros(matriu,filMarcades,colsMarcades);
                zeroNoMarcat = escollirZeroNoMarcat(matriu, escollitFil, filMarcades, colsMarcades);
            }

            
            int columna = assignaFil[zeroNoMarcat[0]];
            // comprova si hi ha un zero assignat en la fila del 0 que hem escollit
            if (columna == -1){
                // incrementem els zeros
                incrementaConjuntDeZerosAssignats(zeroNoMarcat, assignaFil, assignaCol, escollitFil);
                //comencem de nou
                Arrays.fill(escollitFil,-1);
                Arrays.fill(filMarcades,0);
                Arrays.fill(colsMarcades,0);
                marcarColAmbZeros(assignaCol,colsMarcades);
            } else {

                // marquem la fila que conté el zero escollit i desmarquem 
                // la columna del 0 que estava assignat 
                filMarcades[zeroNoMarcat[0]] = 1;
                colsMarcades[columna] = 0;
            }
        }

        // Agafem els 0s marcats de cada columna com les asignacions correctes
        // i sumem els valors assignats de cada posició per obtenir fita
        fita = 0;
        for (int i = 0; i < assignaCol.length;  i++) {
            fita += lawler[assignaCol[i]][i];
        }
        //int[][] retval = new int[matriu.length][];
        //for (int i = 0; i < assignaCol.length;  i++) {
        //    retval[i] = new int[]{assignaCol[i],i};
        //}
        return fita;

    }



    /**
     * Troba el element més petit de cada fila i cada columna de la matriu
     * i el resta de tots els elementes de cada fila o columna
     *
     * @param matriu
     * @return 
     */
    private static void restarMatriu(double[][] matriu) {

        for (int i = 0; i < matriu.length; i++) {

            // troba el valor minim de la fila
            double valMinFil = Double.MAX_VALUE;
            for (int j = 0; j < matriu[i].length; j++) {
                if (valMinFil > matriu[i][j]) {
                    valMinFil = matriu[i][j];
                }
            }

            // restem el valor mínim de la fila a tota la fila
            for (int j = 0; j < matriu[i].length; j++) {
                matriu[i][j] -= valMinFil;
            }
        }
        
        for (int i = 0; i < matriu[0].length; i++) {
            // troba el valor minim de la columna
            Double valMinCol = Double.MAX_VALUE;
            for (int j = 0; j < matriu.length; j++) {
                if (valMinCol > matriu[j][i]) {
                    valMinCol = matriu[j][i];
                }
            }

            // restem el valor mínim de la columna a tota la columna
            for (int j = 0; j < matriu.length; j++) {
                matriu[j][i] -= valMinCol;
            }

        }

    }

/**
 * 
 * per cada columna troba el primer 0
 * si no hi ha cap altre 0 assignat a la fila llavors asigna el 0 
 * a la columna i fila corresponent
 * 
 * @param matriu
 * @param assignaFil
 * @param assignaCol 
 */
    private static void assigna(double[][] matriu, int[] assignaFil, int[] assignaCol) {


        int [] filZeroAssignat = new int[matriu.length];
        int [] colZeroAssignat = new int[matriu[0].length];

        for (int i = 0; i < matriu.length; i++) {
            for (int j = 0; j < matriu[i].length; j++) {
                if (matriu[i][j] == 0 && filZeroAssignat[i] == 0 && colZeroAssignat[j] == 0) {
                    assignaFil[i] = j;
                    assignaCol[j] = i;
                    filZeroAssignat[i] = 1;
                    colZeroAssignat[j] = 1;
                    break; // salta a la següent fila
                }
            }
        }
    }


    /**
     * 
     * marca les columnes que tenen un 0 assignat
     * 
     * @param assignaCol
     * @param colsMarcades 
     */
    
    private static void marcarColAmbZeros(int[] assignaCol, int[] colsMarcades) {
        for (int i = 0; i < assignaCol.length; i++) {
            if (assignaCol[i] == -1) {
                colsMarcades[i] = 0;
            }
            else {
                colsMarcades[i] = 1;
            }
        }
    }

    /**
     * 
     * retorna cert si totes les columnes estan marcades
     * 
     * @param colsMarcades
     * @return 
     */
    private static boolean totMarcat(int[] colsMarcades) {
        for (int i = 0; i < colsMarcades.length; ++i) {
            if (colsMarcades[i] == 0) return false;
        }
        return true;
    }
    
    
    /**
     * 
     * retorna la posició a la matriu d'un 0 d'una fila i columna no marcada
     * 
     * @param matriu
     * @param escollitFil
     * @param filMarcades
     * @param colsMarcades
     * @return 
     */
    
    private static int[] escollirZeroNoMarcat(double matriu[][], int[] escollitFil,
                                       int[] filMarcades, int[] colsMarcades) {


        // busca un zero d'una fila i columna no marcada i l'escull com a 
        // possible per assignar
        for (int i = 0; i < matriu.length; i++) {
            if (filMarcades[i] == 0) {
                for (int j = 0; j < matriu[i].length; j++) {
                    if (matriu[i][j] == 0 && colsMarcades[j] == 0) {
                        //es 0 i ni la columna ni la fila estàn marcades
                        escollitFil[i] = j;
                        return new int[]{i,j};
                    }
                }
            } 
        }
        return null; //no s'ha trobat cap 0

    }

    /**
     * 
     * busquem element més petit no marcat de la matriu
     * el sumem a totes les files marcades
     * i el restem a totes les columnes no marcades
     * 
     * @param matriu
     * @param filMarcades
     * @param colsMarcades 
     */
    
    private static void crearZeros(double[][] matriu, int[] filMarcades, int[] colsMarcades) {

        // busca el valor minim no marcat
        double minNoMarcat = Double.MAX_VALUE;
        for (int i = 0; i < matriu.length; i++) {
            if (filMarcades[i] == 0) {
                for (int j = 0; j < matriu[i].length; j++) {
                    if (colsMarcades[j] == 0 && matriu[i][j] < minNoMarcat) {
                        minNoMarcat = matriu[i][j];
                    }
                }
            }
        }

        // suma el valor minim no marcat a totes les files marcades de la matriu
        for (int i = 0; i < filMarcades.length; i++) {
            if (filMarcades[i] == 1) {
                for (int j = 0; j < matriu[i].length; j++) {
                        matriu[i][j] += minNoMarcat;
                }
            }
        }

        // resta el valor minim no marcat a totes les columnes no marcades de la matriu
        for (int i = 0; i < colsMarcades.length; i++) {
            if (colsMarcades[i] == 0) {
                for (int j = 0; j < matriu.length; j++) {
                    matriu[j][i] -= minNoMarcat;
                }
            }
        }
    }
        
  
    
    /**
     * 
     * 
     * 
     * @param zeroNoMarcat
     * @param asignaFil
     * @param asignaCol
     * @param escollitFil 
     */
    private static void incrementaConjuntDeZerosAssignats(int[] zeroNoMarcat, int[] assignaFil,
                                            int[] assignaCol, int[] escollitFil) {

        // crea una sequencia de zeros alternativa
        int i, j;
        j = zeroNoMarcat[1];

        List<int[]> sequenciaZeros = new ArrayList<int[]>(); //sequencia de zeros
        sequenciaZeros.add(zeroNoMarcat);
        boolean paired;
        while (true) {
            i = assignaCol[j];
            paired = (i != -1); //comprova que si hi ha un 0 a la mateixa columna
            if (!paired) break;
            sequenciaZeros.add(new int[]{i,j});

            j = escollitFil[i];
            paired = (j != -1);//comprova que si hi ha un 0 a la mateixa fila
            if (!paired) break;
            sequenciaZeros.add(new int[]{i,j});

        }


        // desassigna cada zero assignat de la sequenca
        // i assigna cada zero escollit de la sequencia
        for (int[] zero : sequenciaZeros) {
            if (assignaCol[zero[1]] == zero[0]) {
                assignaCol[zero[1]] = -1;
                assignaFil[zero[0]] = -1;
            }
            if (escollitFil[zero[0]] == zero[1]) {
                assignaFil[zero[0]] = zero[1];
                assignaCol[zero[1]] = zero[0];
            }
        }

    }
}
