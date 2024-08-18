package edu.upvictoria.fpoo.COMANDOS;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DELETE {
    public void delete(String path, String tableName, String condition) {
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
                newContent.append(line).append("\n");
            }

            WHERE where = new WHERE(columnIndices);

            while ((line = reader.readLine()) != null) {
                String[] row = line.split("\t");
                if (!where.evaluateCondition(row, condition)) {
                    newContent.append(line).append("\n");
                    System.out.println("Fila eliminada");
                }
            }

        } catch(FileNotFoundException e){
            System.out.println("Tabla inexistente");
            return;
        }catch (IOException e) {
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
