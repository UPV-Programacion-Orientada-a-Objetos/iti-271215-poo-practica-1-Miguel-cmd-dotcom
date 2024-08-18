package edu.upvictoria.fpoo.COMANDOS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CREATE {

    public void crear_estruc_tabla(String path, String tabla, String columnas) {
        boolean NO = true;
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        tabla = tabla.toUpperCase();
        String tablas = path + tabla + "_ESTRUCT.csv";
        String[] columna_individual = columnas.trim().split(",");
        List<String[]> columnasList = new ArrayList<>();

        for (String col : columna_individual) {
            col = col.trim();
            String[] datos = col.split("\\s+");

            if (datos.length >= 2 && validar_tipo_de_datos(datos[1])) {
                String nombreColumna = datos[0].toUpperCase();
                String tipoDato = datos[1].toUpperCase();
                String nullConstraint = "NULL"; // Default to NULL
                String primaryKey = ""; // Default to empty

                for (int i = 2; i < datos.length; i++) {
                    if (datos[i].equalsIgnoreCase("NOT") && (i + 1) < datos.length && datos[i + 1].equalsIgnoreCase("NULL")) {
                        nullConstraint = "NOT NULL";
                        i++;
                    } else if (datos[i].equalsIgnoreCase("PRIMARY") && (i + 1) < datos.length && datos[i + 1].equalsIgnoreCase("KEY")) {
                        primaryKey = "PRIMARY KEY";
                        i++;
                    }
                }

                columnasList.add(new String[]{nombreColumna, nullConstraint, tipoDato, primaryKey});
            } else {
                System.out.println("Tipo de dato no admitido o insuficientes datos");
                NO = false;
                break;
            }
        }

        if (NO) {
            try {
                File archiv = new File(tablas);
                //System.out.println(archiv.getAbsolutePath());
                if (archiv.createNewFile()) {
                    System.out.println("Tabla creada");
                } else {
                    System.out.println("Tabla ya existente");
                }

                try (FileWriter writer = new FileWriter(archiv)) {
                    writer.write("COLUMNA,NULL?,TIPO,\n");
                    for (String[] colData : columnasList) {
                        writer.write(String.join(",", colData) + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            crear_tabla(columnas, tabla, path);
        }
    }

    public boolean validar_tipo_de_datos(String dato) {
        Pattern p = Pattern.compile("VARCHAR\\s*\\(\\d+\\)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(dato);
        if (m.find()) {
            return true;
        }
        if (dato.equalsIgnoreCase("INT") || dato.equalsIgnoreCase("NUMERIC") || dato.equalsIgnoreCase("NUMBER") || dato.equalsIgnoreCase("CHAR")|| dato.equalsIgnoreCase("FLOAT")) {
            return true;
        }
        return false;
    }

    public void crear_tabla(String columnas, String table, String path) {
        path = path.endsWith("/") ? path : path + "/";
        table = table.toUpperCase();
        String tablaPath = path + table + ".csv";
        try {
            File tablaCSV = new File(tablaPath);
            if (tablaCSV.createNewFile()) {
                System.out.println("Tabla creada");
            } else {
                System.out.println("Tabla existente con ese nombre");
            }
            try (FileWriter writer = new FileWriter(tablaCSV)) {
                String[] columna_individual = columnas.trim().split(",");
                for (int i = 0; i < columna_individual.length; i++) {
                    String[] dato = columna_individual[i].trim().split("\\s+");
                    writer.write(dato[0].toUpperCase()); // Convertir a mayúsculas
                    if (i < columna_individual.length - 1) {
                        writer.write("\t");
                    }
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}