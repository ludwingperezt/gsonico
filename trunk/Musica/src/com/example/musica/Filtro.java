package com.example.musica;
import java.io.File;
import java.io.FilenameFilter;


public class Filtro implements FilenameFilter {
	@Override
	public boolean accept(File directorio, String nombreArchivo) {
		/*Devuelve true si el final del archivo coincide con alguna de las extensiones que le indicamos, 
		* en caso contrario devuelve false y no se tiene en cuenta el archivo
		*/
		if(nombreArchivo.endsWith(".mp3"))
			return true;
		
		return false;
	}
}
