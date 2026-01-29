package io;

/**
 *Clase para validar que la matriz este lista para procesarla
 * */

public class MatrixValidator {

    //Metodo que encapsula todos los metodos para validar
    public static void validar(int[][] matriz) {
        validarNoNula(matriz);
        validarIDS(matriz);
        validarHijos(matriz);
    }

    //Metodo para validar que la matriz no este nula
    private static void validarNoNula(int[][] matriz) {
        if (matriz == null || matriz.length == 0) {
            throw new IllegalArgumentException("Error: Matriz vacia o nula.");
        }
    }

    //Metodo para validar que los IDs coincidan con los existentes
    private static void validarIDS(int matriz[][]){
        for(int i = 0; i < matriz.length; i++){
            if(matriz[i][0] != i){
                throw new IllegalArgumentException("El ID no coincide, ya que se espera el numero "+ i+ " y esta el numero: "+ matriz[i][0]);
            }
        }
    }

    //Metodo para validar que los hijos existan y que esten en un rango permitido
    private static void validarHijos(int matriz [][]){
        int n= matriz.length;

        for(int i = 0; i < n; i++){
            int izq = matriz[i][2];
            int der = matriz[i][3];

            if (izq != -1 && (izq < 0 || izq >= n)) {
                throw new IllegalArgumentException("Hijo izquierdo inválido en fila " + i);
            }

            if(der != -1 && (der < 0 || der >= n)) {
                throw new IllegalArgumentException("Hijo derecho invalido en la fila: "+ i);
            }
        }
    }


}
