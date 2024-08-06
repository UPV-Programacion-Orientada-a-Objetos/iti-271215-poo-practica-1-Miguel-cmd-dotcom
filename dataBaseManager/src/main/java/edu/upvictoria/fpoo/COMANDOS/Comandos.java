package edu.upvictoria.fpoo;
import java.io.*;


public class Comandos {
    public boolean comandos(String comandos){
        String [] sentencia = comandos.split(" ");
        BufferedReader archi = new BufferedReader(new InputStreamReader(System.in));
        try{
            if(sentencia[0].equals("CREATE")){
                CREATE(comandos);
                return true;
            }else if(sentencia[0].equals("INSERT") && sentencia[1].equals("INTO")){

            }else if( sentencia[0].equals("DROP")){
                Drop_Table(comandos);
            }else if(sentencia[0].equals("SELECT")){
                Select_Table(comandos);
                return true;
            }else if(sentencia[0].equals("UPDATE")){
                Update_Table(comandos);
                return true;
            }

        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Comando invalido");
        }
        return false;
    }

    private void Update_Table(String comandos) {

    }

    private void Select_Table(String comandos) {

    }

    private void Drop_Table(String comandos) {

    }

    private void CREATE(String comandos) {
        String[] partes = comandos.split(" ");


        if (partes.length < 3) {
            System.out.println("Comando CREATE inválido. Uso correcto: CREATE <nombreTabla> <columnas>");
            return;
        }

        String nombreTabla = partes[1];
        String[] columnas = partes[2].split(",");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreTabla + ".csv"))) {
            for (int i = 0; i < columnas.length; i++) {
                writer.write(columnas[i]);
                if (i < columnas.length - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();
            System.out.println("Tabla '" + nombreTabla + "' creada exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al crear la tabla '" + nombreTabla + "': " + e.getMessage());
        }
    }

}