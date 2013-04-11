package modelos;

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
		if (this.exsiteCancion(mCancion.getArchivoAudio())==false){
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
	 * Funcion para insertar un nuevo Playlist a la base de datos. Solamente inserta si el playlist con el nombre especificado no existe en la db
	 * @param mPlaylist
	 */
	public void insertarPlaylist(Playlist mPlaylist){
		if (this.exsitePlaylist(mPlaylist.getNombre())==false){
			SQLiteDatabase baseDatos = this.getWritableDatabase();
			
			ContentValues parametros = new ContentValues();
			//Nombre TEXT, FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP, Aleatoria BOOL, Repetir BOOL
			parametros.put("Nombre", mPlaylist.getNombre());
			parametros.put("Aleatoria", mPlaylist.isAleatoria());
			parametros.put("Repetir", mPlaylist.isRepetir());
			baseDatos.insert(BaseDatosHelper.TABLA_PLAYLIST, null, parametros);
			baseDatos.close();
		}
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
	public void insertarCancionEnPlaylist(PlaylistCancion mPlaylistCancion){
		
	}
	
	/**
	 * Función que verifica si ya existe una cancion en la base de datos, en base al nombre del archivo
	 * @param direccion
	 * @return
	 */
	public boolean exsiteCancion (String direccion){
		boolean confirmacion = false;
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		String consulta = "SELECT * FROM "+BaseDatosHelper.TABLA_CANCION+" WHERE ArchivoAudio = '"+direccion+"'";
		Cursor c = baseDatos.rawQuery(consulta,null);	
		if (c.getCount()!=0)
			confirmacion = true;
		baseDatos.close();
		return confirmacion;
	}
	/**
	 * Función que verifica si ya existe un playlist en la base de datos, en base al nombre del playlist
	 * @param nombre
	 * @return
	 */
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
		SQLiteDatabase baseDatos = this.getReadableDatabase();
		
		baseDatos.close();
	}

	public Playlist obtenerLista(int id) {
		// TODO Auto-generated method stub
		return null;
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

}
