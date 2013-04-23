package modelos;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatosHelper extends SQLiteOpenHelper {
	
	public static String nombreDB = "biblioteca";
	public static int versionDB = 1;
	public static String TABLA_CANCION = "cancion";
	public static String TABLA_PLAYLIST = "playlist";
	public static String TABLA_PLAYLIST_CANCION = "playlistcancion";
	public static String COLUMNA_ARTISTA = "Artista";
	public static String COLUMNA_ALBUM = "Album";
	public static String COLUMNA_GENERO = "Genero";
	
	private String tablaCancion = "CREATE  TABLE cancion (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , Titulo TEXT, Artista TEXT, Album TEXT, Genero TEXT, Year TEXT, NumeroPista TEXT, ArchivoAudio TEXT, ArchivoLetra TEXT)";
	private String tablaPlaylist = "CREATE  TABLE playlistcancion (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , idCancion INTEGER, idPlaylist INTEGER, idCancionActual INTEGER, idPlaylistActual INTEGER,FOREIGN KEY(idCancion) REFERENCES cancion(_id), FOREIGN KEY(idPlaylist) REFERENCES playlist(_id))";
	private String tablaPlaylistCancion = "CREATE  TABLE playlist (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , Nombre TEXT, FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP, Aleatoria BOOL, Repetir BOOL)";

	public BaseDatosHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public BaseDatosHelper(Context contexto){
		super(contexto, BaseDatosHelper.nombreDB, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(tablaCancion);
		db.execSQL(tablaPlaylist);
		db.execSQL(tablaPlaylistCancion);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Inserta una canción en la base de datos, solamente si no existe, verificando mediante la dirección del archivo
	 * @param mCancion
	 */
	public void insertarCancion(Cancion mCancion){
		//inserta la cancion solamente si no existe en la db
		if (this.existeCancion(mCancion.getArchivoAudio())==false){
			SQLiteDatabase baseDatos = this.getWritableDatabase();
			
			ContentValues parametros = new ContentValues();
			parametros.put("Titulo", mCancion.getTitulo());
			parametros.put("Artista", mCancion.getArtista());
			parametros.put("Album", mCancion.getAlbum());
			parametros.put("Genero", mCancion.getGenero());
			parametros.put("Year", mCancion.getYear());
			parametros.put("NumeroPista", mCancion.getNumeroPista());
			parametros.put("ArchivoAudio", mCancion.getArchivoAudio());
			parametros.put("ArchivoLetra", mCancion.getArchivoLetra());
			baseDatos.insert(BaseDatosHelper.TABLA_CANCION, null, parametros);
			
			baseDatos.close();
		}		
	}
	
	public void insertarCancionDirecto(Cancion mCancion){
		//inserta la cancion solamente si no existe en la db
		
			SQLiteDatabase baseDatos = this.getWritableDatabase();
			
			ContentValues parametros = new ContentValues();
			parametros.put("Titulo", mCancion.getTitulo());
			parametros.put("Artista", mCancion.getArtista());
			parametros.put("Album", mCancion.getAlbum());
			parametros.put("Genero", mCancion.getGenero());
			parametros.put("Year", mCancion.getYear());
			parametros.put("NumeroPista", mCancion.getNumeroPista());
			parametros.put("ArchivoAudio", mCancion.getArchivoAudio());
			parametros.put("ArchivoLetra", mCancion.getArchivoLetra());
			baseDatos.insert(BaseDatosHelper.TABLA_CANCION, null, parametros);
			
			baseDatos.close();
				
	}
	
	public void verificarItems(){
		ArrayList<Cancion> lista = this.obtenerCanciones();
		for (Cancion c:lista){
			File f = new File(c.getArchivoAudio());
			if (f.exists()==false){
				this.eliminarCancion(c);
			}
		}
		//verificar playlists tambien
	}
	
	
	/**
	 * Modifica los datos de una canción, dentro de la db
	 * @param mCancion
	 */
	public void modificarCancion(Cancion mCancion){
		SQLiteDatabase baseDatos = this.getWritableDatabase();
		
		ContentValues parametros = new ContentValues();
		parametros.put("Titulo", mCancion.getTitulo());
		parametros.put("Artista", mCancion.getArtista());
		parametros.put("Album", mCancion.getAlbum());
		parametros.put("Genero", mCancion.getGenero());
		parametros.put("Year", mCancion.getYear());
		parametros.put("NumeroPista", mCancion.getNumeroPista());
		parametros.put("ArchivoAudio", mCancion.getArchivoAudio());
		parametros.put("ArchivoLetra", mCancion.getArchivoLetra());
		baseDatos.update(BaseDatosHelper.TABLA_CANCION, parametros, "_id = ?", new String[]{Integer.toString(mCancion.get_id())});
		
		baseDatos.close();
	}
	
	/**
	 * Funcion para insertar un nuevo Playlist a la base de datos. 
	 * Solamente inserta si el playlist con el nombre especificado no existe en la db, retorna el ID del playlist insertado,
	 * si la lista ya existe o si no se pudo insertar, retorna -1
	 * @param mPlaylist: objeto con los datos del playlist
	 */
	public int insertarPlaylist(Playlist mPlaylist){
		if (this.exsitePlaylist(mPlaylist.getNombre())==false){
			SQLiteDatabase baseDatos = this.getWritableDatabase();
			
			ContentValues parametros = new ContentValues();
			//Nombre TEXT, FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP, Aleatoria BOOL, Repetir BOOL
			parametros.put("Nombre", mPlaylist.getNombre());
			parametros.put("Aleatoria", mPlaylist.isAleatoria());
			parametros.put("Repetir", mPlaylist.isRepetir());
			int param = (int)(baseDatos.insert(BaseDatosHelper.TABLA_PLAYLIST, null, parametros));
			baseDatos.close();
			return param;
		}
		else
			return -1;
	}
	
	/**
	 * Funcion para modificar un playlist
	 * @param mPlaylist
	 */
	public void modificarPlaylist(Playlist mPlaylist){
		SQLiteDatabase baseDatos = this.getWritableDatabase();
		
		ContentValues parametros = new ContentValues();
		//Nombre TEXT, FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP, Aleatoria BOOL, Repetir BOOL
		parametros.put("Nombre", mPlaylist.getNombre());
		parametros.put("Aleatoria", mPlaylist.isAleatoria());
		parametros.put("Repetir", mPlaylist.isRepetir());
		baseDatos.update(BaseDatosHelper.TABLA_PLAYLIST, parametros, "_id = ?", new String[]{Integer.toString(mPlaylist.get_id())});
		baseDatos.close();
	}
	
	//
	/**
	 * Función que inserta una sola cancion, en una lista de reproducción
	 * @param lista: la lista de reproducción existente a la cual se va a agregar la canción
	 * @param cancion: la canción que se desea agregar
	 */
	public void insertarCancionEnPlaylist(Playlist lista, Cancion cancion){
		SQLiteDatabase baseDatos = this.getWritableDatabase();
		//_id INTEGER 
		//idCancion INTEGER, 
		//idPlaylist INTEGER, 
		//idCancionActual INTEGER, 
		//idPlaylistActual INTEGER
		ContentValues parametros = new ContentValues();
		parametros.put("idCancion", cancion.get_id());
		parametros.put("idPlaylist", lista.get_id());
		parametros.put("idCancionActual", cancion.get_id());
		parametros.put("idPlaylistActual", lista.get_id());
		int param = (int)(baseDatos.insert(BaseDatosHelper.TABLA_PLAYLIST_CANCION, null, parametros));
		baseDatos.close();
	}
	
	/**
	 * Función que inserta una lista de canciones en una lista de reproducción
	 * @param lista: La lista de reproducción existente a la cual se le quieren asignar canciones
	 * @param canciones: La lista de canciones a agregar
	 */
	public void insertarListaCancionesEnPlaylist(Playlist lista, ArrayList<Cancion> canciones){
		if (canciones!=null){
			SQLiteDatabase baseDatos = this.getWritableDatabase();
			for (Cancion c:canciones){
				ContentValues parametros = new ContentValues();
				parametros.put("idCancion", c.get_id());
				parametros.put("idPlaylist", lista.get_id());
				parametros.put("idCancionActual", c.get_id());
				parametros.put("idPlaylistActual", lista.get_id());
				baseDatos.insert(BaseDatosHelper.TABLA_PLAYLIST_CANCION, null, parametros);
			}
			baseDatos.close();
		}
	}
	
	/**
	 * Obtiene un Playlist por su ID
	 * @param id: Id del playlist buscado
	 * @return: Playlist encontrado. Null si no se encontraron resultados
	 */
	public Playlist obtenerPlaylistPorID(int id) {
		// TODO Auto-generated method stub
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		Playlist lista = null;
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_PLAYLIST+" WHERE _id = "+Integer.toString(id);
		
		Cursor c = baseDatos.rawQuery(consulta,null);	
		if (c.getCount()!=0){
			c.moveToFirst();
			lista = new Playlist();
			lista.set_id(c.getInt(0));
			lista.setNombre(c.getString(1));
			lista.setFechaCreacion(c.getString(2));
			if (c.getInt(3)==1)
				lista.setAleatoria(true);
			else
				lista.setAleatoria(false);
			if (c.getInt(4)==1)
				lista.setRepetir(true);
			else
				lista.setRepetir(false);
			//SELECT c.* FROM cancion c INNER JOIN playlistcancion lc ON (lc.idCancion = c._id) WHERE lc.idPlaylist = ?
			String consulta2 = "SELECT c.* FROM "+BaseDatosHelper.TABLA_CANCION+" c INNER JOIN "+BaseDatosHelper.TABLA_PLAYLIST_CANCION+" lc ON (lc.idCancion = c._id) WHERE lc.idPlaylist = "+Integer.toString(lista.get_id());
			Cursor c2 = baseDatos.rawQuery(consulta2, null);
			ArrayList<Cancion> lstCanciones = new ArrayList<Cancion>();
			c2.moveToFirst();
		  	while (c2.isAfterLast()==false){
		  		Cancion temporal = new Cancion();
				temporal.set_id(c2.getInt(0));
		  		temporal.setTitulo(c2.getString(1));
		  		temporal.setArtista(c2.getString(2));
		  		temporal.setAlbum(c2.getString(3));
		  		temporal.setGenero(c2.getString(4));
		  		temporal.setYear(c2.getString(5));
		  		temporal.setNumeroPista(c2.getString(6));
		  		temporal.setArchivoAudio(c2.getString(7));
		  		temporal.setArchivoLetra(c2.getString(8)); 
		  		lstCanciones.add(temporal);
		  	}
			c2.close();
			c.close();
			lista.setListaCanciones(lstCanciones);
		}
		
		baseDatos.close();
		return lista;
		
	}
	
	/**
	 * Elimina una canción de una lista de reproducción
	 * @param cancion
	 */
	public void eliminarCancionDePlaylist(Cancion cancion){
		SQLiteDatabase baseDatos = this.getWritableDatabase();
		String consulta = "DELETE FROM "+BaseDatosHelper.TABLA_PLAYLIST_CANCION+" WHERE idCancion = "+Integer.toString(cancion.get_id());
		baseDatos.execSQL(consulta);
		
		baseDatos.close();
	}
	
	/**
	 * Elimina una lista de reproducción de la base de datos.
	 * @param lista
	 */
	public void eliminarPlaylist(Playlist lista){
		SQLiteDatabase baseDatos = this.getWritableDatabase();
		
		String consulta1 = "DELETE FROM "+BaseDatosHelper.TABLA_PLAYLIST_CANCION+" WHERE idPlaylist = "+Integer.toString(lista.get_id());
		String consulta = "DELETE FROM "+BaseDatosHelper.TABLA_PLAYLIST+" WHERE _id = "+Integer.toString(lista.get_id());
		
		baseDatos.execSQL(consulta1);
		baseDatos.execSQL(consulta);

		baseDatos.close();
	}
	
	/**
	 * Obtiene la lista de todos los playlists en la base de datos
	 * @return
	 */
	public ArrayList<Playlist> obtenerTodosPlaylist(){
		ArrayList<Playlist> listas = new ArrayList<Playlist>();
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_PLAYLIST;		
		Cursor c = baseDatos.rawQuery(consulta,null);
		
		listas = this.llenarLista(c);
		
		baseDatos.close();
		return listas;
	}
	
	/**
	 * Función que verifica si ya existe un playlist en la base de datos, en base al nombre del playlist
	 * @param nombre
	 * @return
	 */
	public boolean existePlaylist(String nombre){
		boolean confirmacion = false;
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_PLAYLIST+" WHERE Nombre = '"+nombre+"'";
		Cursor c = baseDatos.rawQuery(consulta,null);	
		if (c.getCount()!=0)
			confirmacion = true;
		baseDatos.close();
		return confirmacion;
	}
	
	/**
	 * Función de búsqueda de playlists.
	 * Funciona de forma similar a la busqueda de canciones
	 * @param nombre: el nombre del playlist a buscar
	 * @return: Set de resultados de la búsqueda
	 */
	public ArrayList<Playlist> busquedaPlaylist(String nombre){
		
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		ArrayList<Playlist> items = new ArrayList<Playlist>();
		// Titulo TEXT, Artista TEXT, Album TEXT, Genero TEXT, Year TEXT, NumeroPista TEXT
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_PLAYLIST+" WHERE Nombre LIKE '%"+nombre+"%'";
		Cursor c = baseDatos.rawQuery(consulta,null);
	  	
	  	c.moveToFirst();
	  	
	  	items = llenarLista(c);
	  	  
	  	c.close();
	  	baseDatos.close();
		return items;
		
	}
	
	private ArrayList<Cancion> obtenerListaDeCancionesPlaylist(Playlist lista){
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		String consulta2 = "SELECT c.* FROM "+BaseDatosHelper.TABLA_CANCION+" c INNER JOIN "+BaseDatosHelper.TABLA_PLAYLIST_CANCION+" lc ON (lc.idCancion = c._id) WHERE lc.idPlaylist = "+Integer.toString(lista.get_id());
		Cursor c2 = baseDatos.rawQuery(consulta2, null);
		ArrayList<Cancion> lstCanciones = new ArrayList<Cancion>();
		c2.moveToFirst();
		
		while (c2.isAfterLast()==false){
		 	Cancion temporal = new Cancion();
			temporal.set_id(c2.getInt(0));
		  	temporal.setTitulo(c2.getString(1));
		  	temporal.setArtista(c2.getString(2));
		  	temporal.setAlbum(c2.getString(3));
		  	temporal.setGenero(c2.getString(4));
		  	temporal.setYear(c2.getString(5));
		  	temporal.setNumeroPista(c2.getString(6));
		  	temporal.setArchivoAudio(c2.getString(7));
		  	temporal.setArchivoLetra(c2.getString(8)); 
		  	lstCanciones.add(temporal);
		}  	
		c2.close();
		return lstCanciones;
		
	}
	
	private ArrayList<Playlist> llenarLista(Cursor c){
		ArrayList<Playlist> listas = new ArrayList<Playlist>();
		c.moveToFirst();		
		while (c.isAfterLast()==false){
			Playlist lista = new Playlist();
			lista.set_id(c.getInt(0));
			lista.setNombre(c.getString(1));
			lista.setFechaCreacion(c.getString(2));
			if (c.getInt(3)==1)
				lista.setAleatoria(true);
			else
				lista.setAleatoria(false);
			if (c.getInt(4)==1)
				lista.setRepetir(true);
			else
				lista.setRepetir(false);
			//decidir si la siguiente lista se incluye o se llama en el momento de comenzar a reproducir el playlist
			//lista.setListaCanciones(this.obtenerListaDeCancionesPlaylist(lista));
			
			listas.add(lista);
		}
		return listas;
	}
	
	/**
	 * Función que verifica si ya existe una cancion en la base de datos, en base al nombre del archivo
	 * @param direccion
	 * @return
	 */
	public boolean existeCancion (String direccion){
		boolean confirmacion = false;
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_CANCION+" WHERE ArchivoAudio = '"+direccion+"'";
		Cursor c = baseDatos.rawQuery(consulta,null);	
		if (c.getCount()!=0)
			confirmacion = true;
		baseDatos.close();
		return confirmacion;
	}

	public boolean exsitePlaylist(String nombre){
		boolean confirmacion = false;
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_PLAYLIST+" WHERE Nombre LIKE '"+nombre+"'";
		Cursor c = baseDatos.rawQuery(consulta,null);	
		if (c.getCount()!=0)
			confirmacion = true;
		baseDatos.close();
		return confirmacion;
	}
	//consultas

	public ArrayList<Cancion> buscarCanciones(String parametro) {
		// TODO Auto-generated method stub
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		ArrayList<Cancion> items = new ArrayList<Cancion>();
		// Titulo TEXT, Artista TEXT, Album TEXT, Genero TEXT, Year TEXT, NumeroPista TEXT
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_CANCION+" WHERE Titulo LIKE '%"+parametro+"%' OR Artista LIKE '%"+parametro+"%' OR Album LIKE '%"+parametro+"%' OR Genero LIKE '%"+parametro+"%' OR Year LIKE '%"+parametro+"%' OR NumeroPista LIKE '%"+parametro+"%'";
		Cursor c = baseDatos.rawQuery(consulta,null);
	  	
	  	c.moveToFirst();
	  	while (c.isAfterLast()==false){
	  		Cancion temporal = new Cancion();
	  		//_id Titulo TEXT, Artista TEXT, Album TEXT, Genero TEXT, Year TEXT, 
	  		//NumeroPista TEXT, ArchivoAudio TEXT, ArchivoLetra TEXT)";
	  		temporal.set_id(c.getInt(0));
	  		temporal.setTitulo(c.getString(1));
	  		temporal.setArtista(c.getString(2));
	  		temporal.setAlbum(c.getString(3));
	  		temporal.setGenero(c.getString(4));
	  		temporal.setYear(c.getString(5));
	  		temporal.setNumeroPista(c.getString(6));
	  		temporal.setArchivoAudio(c.getString(7));
	  		temporal.setArchivoLetra(c.getString(8));
	  		items.add(temporal);
	  		c.moveToNext();
	  	}
	  	  
	  	c.close();
	  	baseDatos.close();
		return items;
	}
	
	
	/**
	 * Método que verifica si hay canciones en la base de datos, si no hay se asume que debe recorrer el directorio de música
	 * e insertar las canciones a la base de datos.
	 * @return
	 */
	public boolean existenCanciones(){
		boolean valor = true;
		SQLiteDatabase db = this.getReadableDatabase();
		
		String consulta = "SELECT count(*) as 'conteo' FROM "+BaseDatosHelper.TABLA_CANCION;
		int conteo = 0;
		Cursor c = db.rawQuery(consulta,null);	  	
	  	c.moveToFirst();	  	
	  	conteo = c.getInt(0);
	  	
	  	if (conteo==0){
	  		valor = false;
	  	}
		db.close();
		
		return valor;
	}
	/**
	 * Funcion para obtener una cancion específica
	 * @param id
	 * @return
	 */
	public Cancion obtenerCancion(int id){
		Cancion temporal=null;
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_CANCION+" WHERE _id = "+Integer.toString(id);
		Cursor c = baseDatos.rawQuery(consulta,null);	
		if (c.getCount()!=0){
			c.moveToFirst();
			temporal = new Cancion();
			temporal.set_id(c.getInt(0));
	  		temporal.setTitulo(c.getString(1));
	  		temporal.setArtista(c.getString(2));
	  		temporal.setAlbum(c.getString(3));
	  		temporal.setGenero(c.getString(4));
	  		temporal.setYear(c.getString(5));
	  		temporal.setNumeroPista(c.getString(6));
	  		temporal.setArchivoAudio(c.getString(7));
	  		temporal.setArchivoLetra(c.getString(8)); 
		}
		baseDatos.close();
		return temporal;
	}
	

	/**
	 * Usar esta funcion cuando se encuentre una cancion que ya no exista en el sistema de archivos
	 * @param c
	 */
	public void eliminarCancion(Cancion c){
		SQLiteDatabase baseDatos = this.getWritableDatabase();
		
		String consulta1 = "DELETE FROM "+BaseDatosHelper.TABLA_PLAYLIST_CANCION+" WHERE idCancion = "+Integer.toString(c.get_id());
		String consulta = "DELETE FROM "+BaseDatosHelper.TABLA_CANCION+" WHERE _id = "+Integer.toString(c.get_id());
		
		baseDatos.execSQL(consulta1);
		baseDatos.execSQL(consulta);
		
		baseDatos.close();
	}

	
	
	public ArrayList<Cancion> obtenerCanciones() {
		// TODO Auto-generated method stub
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		ArrayList<Cancion> items = new ArrayList<Cancion>();
		// Titulo TEXT, Artista TEXT, Album TEXT, Genero TEXT, Year TEXT, NumeroPista TEXT
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_CANCION;
		Cursor c = baseDatos.rawQuery(consulta,null);
	  	
	  	c.moveToFirst();
	  	while (c.isAfterLast()==false){
	  		Cancion temporal = new Cancion();
	  		//_id Titulo TEXT, Artista TEXT, Album TEXT, Genero TEXT, Year TEXT, 
	  		//NumeroPista TEXT, ArchivoAudio TEXT, ArchivoLetra TEXT)";
	  		temporal.set_id(c.getInt(0));
	  		temporal.setTitulo(c.getString(1));
	  		temporal.setArtista(c.getString(2));
	  		temporal.setAlbum(c.getString(3));
	  		temporal.setGenero(c.getString(4));
	  		temporal.setYear(c.getString(5));
	  		temporal.setNumeroPista(c.getString(6));
	  		temporal.setArchivoAudio(c.getString(7));
	  		temporal.setArchivoLetra(c.getString(8));
	  		items.add(temporal);
	  		c.moveToNext();
	  	}
	  	  
	  	c.close();
	  	baseDatos.close();
		return items;
	}
	
	public boolean ultimoId(int id){
		boolean valor = true;
		SQLiteDatabase db = this.getReadableDatabase();
		
		String consulta = "SELECT max(_id) as 'ultimo' FROM "+BaseDatosHelper.TABLA_CANCION;
		int conteo = 0;
		Cursor c = db.rawQuery(consulta,null);	  	
	  	c.moveToFirst();	  	
	  	conteo = c.getInt(0);
	  	
	  	if (id<conteo){
	  		valor = false;
	  	}
		db.close();
		
		return valor;
	}
	
	/**
	 * Función para obtener una lista de items, que pueden ser artista, album o genero.
	 * @param columna: lista con los items pertenecientes a la columna enviada como parametro
	 * @return: lista de items de la columna
	 */
	public ArrayList<String> obtenerPor(String columna) {
		// TODO Auto-generated method stub
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		ArrayList<String> items = new ArrayList<String>();
		String consulta = "SELECT "+columna+" FROM "+BaseDatosHelper.TABLA_CANCION+" GROUP BY "+columna;
		Cursor c = baseDatos.rawQuery(consulta,null);
	  	
	  	c.moveToFirst();
	  	while (c.isAfterLast()==false){
	  		String tmpArtist = c.getString(0);
	  		items.add(tmpArtist);
	  		c.moveToNext();
	  	}
	  	  
	  	c.close();
	  	baseDatos.close();
		return items;
	}
	
	/**
	 * Funcion que busca un item dentro de una columna, que puede ser artista, genero o album
	 * @param columna dentro de la que se busca el item
	 * @param parametro: item buscado
	 * @return: lista de items encontrados
	 */
	public ArrayList<String> buscarPorColumna(String columna, String parametro) {
		// TODO Auto-generated method stub
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		ArrayList<String> items = new ArrayList<String>();
		String consulta = "SELECT "+columna+" FROM "+BaseDatosHelper.TABLA_CANCION+" WHERE "+columna+" like '%"+ parametro +"%' GROUP BY "+columna;
		Cursor c = baseDatos.rawQuery(consulta,null);
	  	
	  	c.moveToFirst();
	  	while (c.isAfterLast()==false){
	  		String tmpArtist = c.getString(0);
	  		items.add(tmpArtist);
	  		c.moveToNext();
	  	}
	  	  
	  	c.close();
	  	baseDatos.close();
		return items;
	}
	
	/**
	 * Funcion que busca todas las canciones definidas por un parámetro de busqueda dentro de una columna específica
	 * puede ser artista, album o género. 
	 * @param columna: aquella que define la búsqueda
	 * @param parametro: el parámetro buscado
	 * @return Lista de canciones encontradas
	 */
	public ArrayList<Cancion> buscarCancionesPor(String columna, String parametro) {
		// TODO Auto-generated method stub
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		ArrayList<Cancion> items = new ArrayList<Cancion>();
		
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_CANCION+" WHERE "+columna+" = '"+parametro+"'";
		Cursor c = baseDatos.rawQuery(consulta,null);
	  	
	  	c.moveToFirst();
	  	while (c.isAfterLast()==false){
	  		Cancion temporal = new Cancion();
	  		temporal.set_id(c.getInt(0));
	  		temporal.setTitulo(c.getString(1));
	  		temporal.setArtista(c.getString(2));
	  		temporal.setAlbum(c.getString(3));
	  		temporal.setGenero(c.getString(4));
	  		temporal.setYear(c.getString(5));
	  		temporal.setNumeroPista(c.getString(6));
	  		temporal.setArchivoAudio(c.getString(7));
	  		temporal.setArchivoLetra(c.getString(8));
	  		items.add(temporal);
	  		c.moveToNext();
	  	}
	  	  
	  	c.close();
	  	baseDatos.close();
		return items;
	}
}
