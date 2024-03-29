package com.example.musica;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import modelos.BaseDatosHelper;
import modelos.Cancion;
import modelos.Metadatos;


public class ListadoArchivos {
	//Al m�todo le pasamos el directorio donde vamos a buscar todos los archivos/directorios que tenga.
	public static String[] devolverListadoArchivosDirectorios(String directorioPrincipal){
		
		/*Para hacer una b�squeda selectiva en el list instanciamos a la clase SoloIm�genes, que ser� la encargada de mostrar los archivos
		* que tengan en su nombre los filtros de b�squeda indicados
		*/
		String[] archivos = new File(directorioPrincipal).list(new Filtro());
		
		//Para guardar en el array todos los archivos/directorios encontrados de la ruta indicada sin tener en cuenta el filtro.
		//String[] archivos = new File(directorioPrincipal).list();
		
		//Devuelve el array de String con los archivos/directorios encontrados en la b�squeda.
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
            	String direccion = f.getAbsolutePath();
            	Cancion mCancion = mMeta.leerEtiquetasCancion(direccion);
            	if (direccion.contains("'")==false){
            		db.insertarCancion(mCancion);
            	}
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
	
	public static Bitmap getAlbumArtSmall(String direccionCancion){
		Bitmap bmp = null;
		File f = new File(direccionCancion);
		File directorioPadre = f.getParentFile();
		String [] archivos = directorioPadre.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				String d = name.toLowerCase();
				//if ((name.contains("AlbumArt"))&&(name.contains("Small")))
				if (d.contains("albumart"))
                    return true;
                else
                    return false;
			}
		});
		if (archivos!=null){
			if (archivos.length>0){
				bmp = BitmapFactory.decodeFile(directorioPadre.getAbsolutePath()+File.separator+archivos[0]);
			}
		}
		return bmp;
	}
	
	public static Bitmap getAlbumArtLarge(String direccion){
		Bitmap bmp = null;
		File f = new File(direccion);
		File directorioPadre = f.getParentFile();
		String [] archivos = directorioPadre.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				if ((name.contains("AlbumArt"))&&(name.contains("Large")))
                    return true;
                else
                    return false;
			}
		});
		if (archivos!=null){
			if (archivos.length>0){
				bmp = BitmapFactory.decodeFile(directorioPadre.getAbsolutePath()+File.separator+archivos[0]);
			}
		}
		return bmp;
	}
}
