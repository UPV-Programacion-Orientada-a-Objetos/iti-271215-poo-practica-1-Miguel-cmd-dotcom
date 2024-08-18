package edu.upvictoria.fpoo.COMANDOS;

import java.io.File;

public class SHOW_TABLES {
    public void SHOW(String path){
        File base_Datos=new File(path);
        File[] tablas=base_Datos.listFiles();
        if(tablas!=null){
            for(File tabla: tablas){
                String fileName = tabla.getName();
                if(!tabla.getName().contains("_")&&tabla.getName()!=null){
                    String tableName = fileName.substring(0, fileName.lastIndexOf('.'));
                    System.out.println(tableName);
                }
            }
        }else{
            System.out.println("Tabla vacia \n");
        }
    }
}
