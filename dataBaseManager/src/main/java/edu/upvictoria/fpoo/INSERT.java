package edu.upvictoria.fpoo.COMANDOS;

import java.io.*;
import java.util.*;

public class INSERT {
    public void insertImplicit(String path, String datos, String tabla) {
        String pathTable = path.endsWith("/") ? path + tabla + ".csv" : path + "/" + tabla + ".csv";
        String pathStructure = path.endsWith("/") ? path + tabla + "_ESTRUCT.csv" : path + "/" + tabla + "_ESTRUCT.csv";
        File file = new File(pathTable);

        if (!file.exists() || file.length() == 0) {
            System.out.println("Error: No se pueden insertar datos sin encabezados en un archivo vacío.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             FileWriter writer = new FileWriter(pathTable, true)) {

            String header = br.readLine();
            if (header == null || header.isEmpty()) {
                System.out.println("Error: El archivo CSV no tiene encabezado.");
                return;
            }

            String[] headerColumns = header.split("\t");
            String[] dataColumns = parseDataColumns(datos, ',');

            if (dataColumns.length > headerColumns.length) {
                System.out.println("Error de inserción: se especificaron más valores de los que hay columnas.");
                return;
            }

            String[] finalDataColumns = new String[headerColumns.length];
            for (int i = 0; i < headerColumns.length; i++) {
                if (i < dataColumns.length) {
                    finalDataColumns[i] = dataColumns[i];
                } else {
                    finalDataColumns[i] = "NULL";
                }
            }

            if (!validateDataTypes(pathStructure, finalDataColumns)) {
                System.out.println("Error de inserción: los tipos de datos no coinciden.");
                return;
            }

            for (int i = 0; i < headerColumns.length; i++) {
                if (finalDataColumns[i].equals("NULL")) {
                    if (isNotNullableColumn(pathStructure, headerColumns[i].trim().toUpperCase())) {
                        System.out.println("Error de inserción: la columna " + headerColumns[i] + " no puede ser NULL.");
                        return;
                    }
                }
            }

            if (file.length() > 0 && !fileEndsWithNewLine(file)) {
                writer.write("\n");
            }

            writer.write(String.join("\t", finalDataColumns) + "\n");
            System.out.println("Inserción correcta");

        } catch (IOException e) {
            System.out.println("Error al acceder al archivo: " + e.getMessage());
        }
    }

    public void insertExplicit(String path, String columnas, String datos, String tabla) {
        String pathTable = path.endsWith("/") ? path + tabla + ".csv" : path + "/" + tabla + ".csv";
        String pathStructure = path.endsWith("/") ? path + tabla + "_ESTRUCT.csv" : path + "/" + tabla + "_ESTRUCT.csv";
        File file = new File(pathTable);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        if (!file.exists()) {
            System.out.println("Error: La tabla especificada no existe.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             FileWriter writer = new FileWriter(pathTable, true)) {

            String header = br.readLine();
            if (header == null || header.isEmpty()) {
                System.out.println("Error: El archivo CSV no tiene encabezado.");
                return;
            }

            String[] headerColumns = header.split("\t");
            String[] insertColumns = parseDataColumns(columnas, ',');
            String[] dataColumns = parseDataColumns(datos, ',');

            // Verificar si las columnas especificadas existen en la tabla
            if (!validateColumnsExist(headerColumns, insertColumns)) {
                System.out.println("Error de inserción: columnas inexisntentes");
                return;
            }

            if (insertColumns.length != dataColumns.length) {
                System.out.println("Error de inserción: el número de columnas no coincide.");
                return;
            }

            Map<String, String> dataMap = new HashMap<>();
            for (int i = 0; i < insertColumns.length; i++) {
                dataMap.put(insertColumns[i].trim().toUpperCase(), dataColumns[i].trim());
            }

            String[] finalDataColumns = new String[headerColumns.length];
            for (int i = 0; i < headerColumns.length; i++) {
                String columnName = headerColumns[i].trim().toUpperCase();
                if (dataMap.containsKey(columnName)) {
                    finalDataColumns[i] = dataMap.get(columnName);
                } else {
                    finalDataColumns[i] = "NULL";
                }
            }

            if (!validateDataTypes(pathStructure, finalDataColumns)) {
                System.out.println("Error de inserción: los tipos de datos no coinciden.");
                return;
            }

            for (int i = 0; i < headerColumns.length; i++) {
                if (finalDataColumns[i].equals("NULL")) {
                    if (isNotNullableColumn(pathStructure, headerColumns[i].trim().toUpperCase())) {
                        System.out.println("Error de inserción: la columna " + headerColumns[i] + " no puede ser NULL.");
                        return;
                    }
                }
            }

            if (file.length() > 0 && !fileEndsWithNewLine(file)) {
                writer.write("\n");
            }

            writer.write(String.join("\t", finalDataColumns) + "\n");
            System.out.println("Inserción correcta");

        } catch (IOException e) {
            System.out.println("Error al acceder al archivo: " + e.getMessage());
        }
    }

    private boolean validateColumnsExist(String[] headerColumns, String[] insertColumns) {
        // Convertir headerColumns a un Set para verificación rápida
        Set<String> headerSet = new HashSet<>(Arrays.asList(headerColumns));
        for (String column : insertColumns) {
            if (!headerSet.contains(column.trim().toUpperCase())) {
                return false; // Si alguna columna no existe en el encabezado, retorna false
            }
        }
        return true; // Todas las columnas especificadas existen en el encabezado
    }
    private boolean validateDataTypes(String pathStructure, String[] dataColumns) {
        try (BufferedReader br = new BufferedReader(new FileReader(pathStructure))) {
            // Skip header
            String line = br.readLine();
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] struct = line.split(",");
                String dataType = struct[2].trim();
                if (!struct[0].trim().isEmpty()) {  // Check if column is not null
                    if (!dataColumns[index].equalsIgnoreCase("NULL")) {
                        if (dataType.startsWith("VARCHAR")) {
                            int maxLength = Integer.parseInt(dataType.substring(dataType.indexOf('(') + 1, dataType.indexOf(')')));
                            if (dataColumns[index].length() > maxLength) {
                                System.out.println("Error de tipo de datos: el valor en la columna " + index + " excede la longitud máxima para VARCHAR.");
                                return false;
                            }
                        } else if (dataType.equalsIgnoreCase("INT")) {
                            try {
                                Integer.parseInt(dataColumns[index]);
                            } catch (NumberFormatException e) {
                                System.out.println("Error de tipo de datos: el valor en la columna " + index + " no es un entero válido.");
                                return false;
                            }
                        } else if (dataType.equalsIgnoreCase("FLOAT")) {
                            try {
                                Float.parseFloat(dataColumns[index]);
                            } catch (NumberFormatException e) {
                                System.out.println("Error de tipo de datos: el valor en la columna " + index + " no es un número decimal válido.");
                                return false;
                            }
                        } else if (dataType.equalsIgnoreCase("NUMERIC") || dataType.equalsIgnoreCase("NUMBER")) {
                            try {
                                Double.parseDouble(dataColumns[index]);
                            } catch (NumberFormatException e) {
                                System.out.println("Error de tipo de datos: el valor en la columna " + index + " no es un número válido.");
                                return false;
                            }
                        } else if (dataType.equalsIgnoreCase("CHAR")) {
                            if (dataColumns[index].length() != 1) {
                                System.out.println("Error de tipo de datos: el valor en la columna " + index + " no es un carácter válido.");
                                return false;
                            }
                        }
                    }
                    index++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la estructura de la tabla: " + e.getMessage());
            return false;
        }

        return true;
    }

    private boolean isNotNullableColumn(String pathStructure, String columnName) {
        try (BufferedReader br = new BufferedReader(new FileReader(pathStructure))) {
            // Skip header
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] struct = line.split(",");
                if (struct[0].trim().equalsIgnoreCase(columnName) && struct[1].trim().equalsIgnoreCase("NOT NULL")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la estructura de la tabla: " + e.getMessage());
        }

        return false;
    }

    private String[] parseDataColumns(String datos, char delimiter) {
        List<String> columns = new ArrayList<>();
        StringBuilder currentColumn = new StringBuilder();
        boolean inQuotes = false;
        for (char c : datos.toCharArray()) {
            if (c == '\'') {
                inQuotes = !inQuotes;
            } else if (c == delimiter && !inQuotes) {
                columns.add(currentColumn.toString().trim());
                currentColumn.setLength(0);
            } else {
                currentColumn.append(c);
            }
        }
        columns.add(currentColumn.toString().trim());
        return columns.toArray(new String[0]);
    }

    private boolean fileEndsWithNewLine(File file) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            if (raf.length() == 0) return false;
            raf.seek(raf.length() - 1);
            int lastByte = raf.read();
            return lastByte == '\n';
        }
    }
}
