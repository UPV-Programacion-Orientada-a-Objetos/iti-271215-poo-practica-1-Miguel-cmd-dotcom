package edu.upvictoria.fpoo.COMANDOS;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UPDATE {
    public void update(String path, String tableName, String setClause, String condition) {
        String pathTable = path.endsWith("/") ? path + tableName + ".csv" : path + "/" + tableName + ".csv";
        File file = new File(pathTable);

        Map<String, Integer> columnIndices = new HashMap<>();
        StringBuilder newContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                String[] headers = line.split("\t");
                for (int i = 0; i < headers.length; i++) {
                    columnIndices.put(headers[i].trim().toUpperCase(), i);
                }
                newContent.append(line).append("\n"); // Agregar encabezados al nuevo contenido
            }

            WHERE where = new WHERE(columnIndices);
            String[] setExpressions = setClause.split(",");
            Map<String, String> updateValues = new HashMap<>();
            for (String expr : setExpressions) {
                String[] parts = expr.trim().split("=");
                if (parts.length == 2) {
                    updateValues.put(parts[0].trim(), parts[1].trim().replaceAll("^'|'$", ""));
                }
            }

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                if (where.evaluateCondition(fields, condition)) { // Usar arreglo de cadenas para evaluación
                    for (Map.Entry<String, String> entry : updateValues.entrySet()) {
                        String columnName = entry.getKey();
                        String newValue = entry.getValue();
                        Integer index = columnIndices.get(columnName.toUpperCase());
                        if (index != null && index < fields.length) {
                            fields[index] = newValue;
                            System.out.println("Fila actualizada");
                        }
                    }
                    newContent.append(String.join("\t", fields)).append("\n");
                } else {
                    newContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(newContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
