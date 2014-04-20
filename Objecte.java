package common;
import localitzacio.*;
import java.util.*;
/**
 * @author David Casas
 */
public class Objecte {
    /**
     * @param ID Nombre identificador de l'objecte.
     */
    private int ID;
   
    /**
     * Crea un objecte.
     * @param i ID de l'objecte.
     */
    public Tarea(int i){
        ID = i;
    }
    
    /**
     * Retorna el codi identificador de l'objecte.
     */
    public int getID(){
		return ID;
	}
}