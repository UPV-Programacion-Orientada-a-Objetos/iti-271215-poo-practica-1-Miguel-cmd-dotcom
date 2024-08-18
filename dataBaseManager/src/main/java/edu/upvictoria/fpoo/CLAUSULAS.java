package edu.upvictoria.fpoo.COMANDOS;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLAUSULAS {
    String path$;

    /**
     *
     * @param clausula
     * @return
     */
    public Boolean clausula(String clausula){
        String [] sentencia=clausula.split(" ");

        int tam=sentencia.length;
        try{
            if(sentencia[0].toUpperCase().equals("USE")){
                String [] path=sentencia[1].split(";");
                USE(path[0]);
                return true;
            }else if(sentencia[0].toUpperCase().equals("SHOW")&&sentencia[1].toUpperCase().equals("TABLES")){
              //  SHOW_TABLES(path$);
                return true;
            }else if(sentencia[0].toUpperCase().equals("CREATE")){
                CREATE(clausula);
                return true;
            }else if(sentencia[0].toUpperCase().equals("DROP")&&sentencia[1].toUpperCase().equals("TABLE")) {
               // DROP_TABLE(clausula);
            }else if(sentencia[0].toUpperCase().equals("INSERT")){
               // INSERT_INTO(clausula);
                return true;
            }else if(sentencia[0].toUpperCase().equals("SELECT")){
                SELECT(clausula);
                return true;
            }else if(sentencia[0].toUpperCase().equals("UPDATE")) {
              //  UPDATE(clausula);
                return true;
            }else if(sentencia[0].toUpperCase().equals("DELETE")&&sentencia[1].toUpperCase().equals("FROM")){
               // DELETE(clausula);
                return true;
            }else{
                System.out.println("Comando desconocido");
            }
        }catch( ArrayIndexOutOfBoundsException e){
            System.out.println("objeto no encontrado");
        }
        return false;
    }

    /**
     *
     * @param path
     */
    public void USE(String path){

        File dbPath = new File(path);

        if (dbPath.exists()) {
            System.out.println("Path de la base de datos: " + path);
            path$ = path;
        } else {
            System.out.println("Error: El path especificado no existe.");
        }
    }

    /**
     *
     * @param path
     */


    /**
     *
     * @param clausula
     */
    public void CREATE(String clausula){
        Pattern p=Pattern.compile("CREATE TABLE (\\w+) \\(((?:[^()]+|\\((?:[^()]+|\\([^()]*\\))*\\))+)\\)", Pattern.CASE_INSENSITIVE);
        Matcher m=p.matcher(clausula);
        if(m.find()){

            CREATE crear=new CREATE();
            crear.crear_estruc_tabla(path$,m.group(1),m.group(2).trim());
        }else{
            System.out.println("Sintaxis Incorrecta");
        }
    }

    /**
     *
     * @param clausula
     */


    /**
     *
     * @param clausula
     */


    public void SELECT(String clausula){
        boolean t = false;
        SELECT select = new SELECT();

        Pattern p0 = Pattern.compile("SELECT \\* FROM (\\w+) WHERE (.+)", Pattern.CASE_INSENSITIVE);
        Matcher m0 = p0.matcher(clausula);
        if (m0.find() && !t) {
            //System.out.println(m0.group(2));
            select.selectAll(path$, m0.group(1).toUpperCase(), m0.group(2));
            t = true;
        }

        Pattern p3 = Pattern.compile("SELECT ([^*]+) FROM (\\w+) WHERE (.+)", Pattern.CASE_INSENSITIVE);
        Matcher m3 = p3.matcher(clausula);
        if (m3.find()) {
            select.select(path$, m3.group(2).toUpperCase(), m3.group(1).toUpperCase(), m3.group(3));
            t = true;
        }

        Pattern p1 = Pattern.compile("SELECT ([^*]+) FROM (\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher m1 = p1.matcher(clausula);
        if (m1.find() && !t) {
            select.select(path$, m1.group(2).toUpperCase(), m1.group(1).toUpperCase());
            t = true;
        }

        Pattern p2 = Pattern.compile("SELECT \\* FROM (\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher m2 = p2.matcher(clausula);
        if (m2.find() && !t) {
            select.select(path$, m2.group(1).toUpperCase());
            t = true;
        }


        if (!t) {
            System.out.println("Sintaxis Incorrecta");
        }
    }


}