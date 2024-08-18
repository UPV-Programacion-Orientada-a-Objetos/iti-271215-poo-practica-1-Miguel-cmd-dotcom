package edu.upvictoria.fpoo.COMANDOS;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SELECT {
    private List<String[]> loadData(String path, String tableName) {
        List<String[]> data = new ArrayList<>();
        String pathtabla = path + "/" + tableName + ".csv";
        if (hasTableData(path, tableName)) {
            try (BufferedReader br = new BufferedReader(new FileReader(pathtabla))) {
                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line.split("\t"));
                }
            } catch (FileNotFoundException e) {
                System.out.println("Tabla no encontrada: " + pathtabla);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("La tabla " + tableName + " no contiene datos");
        }
        return data;
    }

    public static List<String[]> readTableData(String path, String tableName) {
        List<String[]> data = new ArrayList<>();
        String pathtabla = path + "/" + tableName + ".csv";
        try (BufferedReader br = new BufferedReader(new FileReader(pathtabla))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split("\t"));
            }
        } catch (FileNotFoundException e) {
            // System.out.println("File not found: " + pathtabla);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public boolean hasTableData(String path, String tableName) {
        List<String[]> data = readTableData(path, tableName);
        return data.size() > 1;
    }

    public void select(String path, String tableName, String columns, String condition) {
        List<String[]> data = loadData(path, tableName);
        if (data.isEmpty()) {
            System.out.println("Tabla vacía: " + tableName);
            return;
        }

        String[] colum = columns.split(",");
        int[] colIndices = new int[colum.length];
        Arrays.fill(colIndices, -1);
        String[] header = data.get(0);

        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            columnIndexMap.put(header[i].trim().toUpperCase(), i);
        }

        boolean allColumnsFound = true;

        for (int j = 0; j < colum.length; j++) {
            colum[j] = colum[j].trim().toUpperCase();
            if (columnIndexMap.containsKey(colum[j])) {
                colIndices[j] = columnIndexMap.get(colum[j]);
            } else {
                System.out.println("Columna inexistente: " + colum[j]);
                allColumnsFound = false;
            }
        }

        if (!allColumnsFound) {
            return;
        }

        WHERE where = new WHERE(columnIndexMap);
        boolean dataFound = false;

        // Check if any row matches the condition
        for (String[] row : data.subList(1, data.size())) {
            if (where.evaluateCondition(row, condition)) {
                dataFound = true;
                break;
            }
        }

        if (dataFound) {
            System.out.println("Resultados de la búsqueda:");
            for (int colIndex : colIndices) {
                if (colIndex != -1) {
                    System.out.print(header[colIndex] + "\t");
                }
            }
            System.out.println();

            // Print matching rows
            for (String[] row : data.subList(1, data.size())) {
                if (where.evaluateCondition(row, condition)) {
                    for (int colIndex : colIndices) {
                        if (colIndex != -1 && colIndex < row.length) {
                            System.out.print(row[colIndex] + "\t");
                        }
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("Datos no encontrados");
        }
    }

    public void selectAll(String path, String tableName, String condition) {
        List<String[]> data = loadData(path, tableName);
        if (data.isEmpty()) {
            System.out.println("Tabla sin datos: " + tableName);
            return;
        }

        String[] header = data.get(0);
        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            columnIndexMap.put(header[i].trim().toUpperCase(), i);
        }

        WHERE where = new WHERE(columnIndexMap);
        boolean dataFound = false;

        // Check if any row matches the condition
        for (String[] row : data.subList(1, data.size())) {
            if (where.evaluateCondition(row, condition)) {
                dataFound = true;
                break;
            }
        }

        if (dataFound) {
            System.out.println("Resultados de la búsqueda:");
            System.out.println(String.join("\t", header));

            for (String[] row : data.subList(1, data.size())) {
                if (where.evaluateCondition(row, condition)) {
                    System.out.println(String.join("\t", row));
                }
            }
        } else {
            System.out.println("Datos no encontrados");
        }
    }

    public void select(String path, String tableName) {
        List<String[]> data = loadData(path, tableName);
        if (data.isEmpty()) {
            //System.out.println("No data found for table: " + tableName);
            return;
        }

        for (String[] row : data) {
            for (String column : row) {
                System.out.print(column + "\t");
            }
            System.out.println();
        }
    }

    public void select(String path, String tableName, String columns) {
        String[] colum = columns.split(",");
        int[] colIndices = new int[colum.length];
        Arrays.fill(colIndices, -1);

        if (!hasTableData(path, tableName)) {
            System.out.println("La tabla " + tableName + " no contiene datos");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path + "/" + tableName + ".csv"))) {
            String line;
            boolean headerRead = false;
            String[] header = null;
            Map<String, Integer> columnIndexMap = new HashMap<>();

            while ((line = br.readLine()) != null) {
                String[] datos = line.split("\t");

                if (!headerRead) {
                    header = datos;
                    for (int i = 0; i < datos.length; i++) {
                        columnIndexMap.put(datos[i].trim().toUpperCase(), i);
                    }

                    boolean allColumnsFound = true;
                    for (int j = 0; j < colum.length; j++) {
                        colum[j] = colum[j].trim().toUpperCase();
                        if (columnIndexMap.containsKey(colum[j])) {
                            colIndices[j] = columnIndexMap.get(colum[j]);
                        } else {
                            System.out.println("Columna no encontrada: " + colum[j]);
                            allColumnsFound = false;
                        }
                    }

                    if (!allColumnsFound) {
                        return;
                    }

                    // Imprimir los nombres de las columnas seleccionadas
                    for (int colIndex : colIndices) {
                        if (colIndex != -1) {
                            System.out.print(header[colIndex] + "\t");
                        }
                    }
                    System.out.println();

                    headerRead = true;
                    continue;
                }

                boolean dataFound = false;
                for (int colIndex : colIndices) {
                    if (colIndex != -1 && colIndex < datos.length) {
                        dataFound = true;
                        System.out.print(datos[colIndex] + "\t");
                    }
                }
                System.out.println();

                if (!dataFound) {
                    System.out.println("Datos no encontrados");
                }
            }
        } catch (IOException e) {
            System.out.println("La tabla no existe");
        }
    }
}

