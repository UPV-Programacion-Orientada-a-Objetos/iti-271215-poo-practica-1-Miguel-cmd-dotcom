package edu.upvictoria.fpoo;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Comandos {
    private static Map<String, Runnable> comando = new HashMap<String, Runnable>();
comando.put("show TBALES", this::showTables);
comando.put("DROP TABLE", () -> {
        try{

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    });

}