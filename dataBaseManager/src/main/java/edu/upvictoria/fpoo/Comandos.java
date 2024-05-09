package edu.upvictoria.fpoo;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Comandos {
    public boolean comandos(String comandos){
        String [] sentencia = comandos.split(" ");
        try{
            if(sentencia[0].equals("CREATE")){
                CREATE(comandos);
                return true;
            }else if(sentencia[0].equals("INSERT") && sentencia[1].equals("INTO")){

            }else if( sentencia[0].equals("DROP")){
                Drop_Table(comandos);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Comando invalido");
        }
        return false;
    }

    private void Drop_Table(String comandos) {
    }

    private void CREATE(String comandos) {
    }

}