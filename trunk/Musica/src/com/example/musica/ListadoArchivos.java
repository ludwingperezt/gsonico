package com.example.musica;

import java.io.File;


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
}
