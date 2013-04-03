package com.example.musica;

import java.io.File;


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
}
