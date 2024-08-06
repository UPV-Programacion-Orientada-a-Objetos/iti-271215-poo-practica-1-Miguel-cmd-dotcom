package edu.upvictoria.fpoo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


    public class SELECT {
        private String tableName;
        private List<String> columns;

        public SELECT(String command) {
            parseCommand(command);
        }


        private void parseCommand(String command) {
            String[] partes = command.split(" ");
            if (partes.length < 4 || !partes[0].equalsIgnoreCase("SELECT")) {
                throw new IllegalArgumentException("Comando SELECT inválido. Uso correcto: SELECT <columnas> FROM <nombreTabla>");
            }

            String columnasStr = partes[1];
            this.columns = List.of(columnasStr.split(","));
            if (!partes[2].equalsIgnoreCase("FROM")) {
                throw new IllegalArgumentException("Comando SELECT inválido. Uso correcto: SELECT <columnas> FROM <nombreTabla>");
            }
            this.tableName = partes[3];
        }

        public void execute() {
            try (BufferedReader reader = new BufferedReader(new FileReader(tableName + ".csv"))) {
                String headerLine = reader.readLine();
                if (headerLine == null) {
                    throw new IOException("Tabla '" + tableName + "' está vacía o no existe.");
                }

                String[] headerColumns = headerLine.split(",");
                List<Integer> columnIndexes = new ArrayList<>();
                for (String column : columns) {
                    boolean found = false;
                    for (int i = 0; i < headerColumns.length; i++) {
                        if (headerColumns[i].equalsIgnoreCase(column)) {
                            columnIndexes.add(i);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException("Columna '" + column + "' no encontrada en la tabla '" + tableName + "'.");
                    }
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    for (int index : columnIndexes) {
                        System.out.print(data[index] + "\t");
                    }
                    System.out.println();
                }
            } catch (IOException e) {
                System.out.println("Error al leer la tabla '" + tableName + "': " + e.getMessage());
            }
        }
    }

