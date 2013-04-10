package com.example.musica;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;

import modelos.BaseDatosHelper;
import modelos.Cancion;
import modelos.Metadatos;


public class ListadoArchivos {
	//Al método le pasamos el directorio donde vamos a buscar todos los archivos/directorios que tenga.
	public static String[] devolverListadoArchivosDirectorios(String directorioPrincipal){
		
		/*Para hacer una búsqueda selectiva en el list instanciamos a la clase SoloImágenes, que será la encargada de mostrar los archivos
		* que tengan en su nombre los filtros de búsqueda indicados
		*/
		String[] archivos = new File(directorioPrincipal).list(new Filtro());
		
		//Para guardar en el array todos los archivos/directorios encontrados de la ruta indicada sin tener en cuenta el filtro.
		//String[] archivos = new File(directorioPrincipal).list();
		
		//Devuelve el array de String con los archivos/directorios encontrados en la búsqueda.
		return archivos;
	}
	
	public static void recorrerDirectorios(String dir, Context contexto){
        File directorio = new File(dir);
        File[] listaArchivos = directorio.listFiles();
        File[] listaMp3 = directorio.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				String nombre = filename.toLowerCase();
                if (nombre.endsWith("mp3"))
                    return true;
                else
                    return false;
			}
		});
        
        if (listaMp3!=null){
            for (File f:listaMp3){
            	//insertar a la db
            	BaseDatosHelper db = new BaseDatosHelper(contexto);
            	Metadatos mMeta = new Metadatos();
        		Cancion mCancion = mMeta.leerEtiquetasCancion(f.getAbsolutePath());
        		db.insertarCancionDirecto(mCancion);
            }
        }
        
        if (listaArchivos!=null){
            for (File f:listaArchivos){
                if (f.isDirectory()){
                    ListadoArchivos.recorrerDirectorios(f.getAbsolutePath(),contexto);
                }
            }
        }
    }
}
