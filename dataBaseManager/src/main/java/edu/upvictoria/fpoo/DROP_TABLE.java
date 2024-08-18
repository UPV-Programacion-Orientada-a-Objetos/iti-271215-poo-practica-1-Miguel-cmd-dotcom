package edu.upvictoria.fpoo.COMANDOS;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DROP_TABLE {
    public void drop(String path, String tabla) {
        drop(path, tabla.toUpperCase(), null);
    }

    public void drop(String path, String tabla, String dec) {
        String ppath = path + "/" + tabla + ".csv";
        String estructura = path + "/" + tabla + "_ESTRUCT.csv";
        System.out.println(estructura);
        try {
            File table = new File(ppath);

            if (dec == null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("¿Estás seguro de eliminar la tabla " + tabla + "? (S/n)");
                dec = br.readLine().toUpperCase();
            } else {
                dec = dec.toUpperCase();
            }

            if (dec.equals("S")) {
                boolean tableDeleted = forceDeleteFile(table);
                boolean estrucDeleted = forceDeleteFile(new File(estructura));

                if (tableDeleted && estrucDeleted) {
                    System.out.println("Se ha eliminado la tabla " + tabla);
                } else {
                    System.out.println("Error al eliminar la tabla " + tabla);
                    if (!tableDeleted) {
                        System.out.println("No se pudo eliminar el archivo de datos de la tabla.");
                    }
                    if (!estrucDeleted) {
                        System.out.println("No se pudo eliminar el archivo de estructura de la tabla.");
                    }
                }
            } else if (dec.equals("N")) {
                System.out.println("No se ha eliminado la tabla");
            } else {
                System.out.println("Acción no ejecutada");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean forceDeleteFile(File file) {
        boolean deleted = false;
        int attempts = 0;
        while (!deleted && attempts < 5) {
            System.gc();  // Sugerir al garbage collector que libere recursos
            deleted = file.delete();
            attempts++;
            if (!deleted) {
                try {
                    Thread.sleep(100);  // Espera un poco antes de intentar nuevamente
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return deleted;
    }
}
