package com.example.musica;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import modelos.BaseDatosHelper;
import modelos.Cancion;
import modelos.Metadatos;
import modelos.Playlist;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;
//import android.os.Bundle;
//import android.content.res.AssetFileDescriptor;
//import android.content.res.AssetManager;


@SuppressWarnings("unused")
@SuppressLint({ "SdCardPath", "ShowToast" })
public class MainActivity extends Activity implements OnCompletionListener{

	private BaseDatosHelper baseDatos;
	private EditText texto;
	private EditText textoArtista;
	private EditText textoAlbum;
	private ListView lista;
	private ListView listaArtista;
	private ListView listaAlbum;
	private ListView listaPlayLists;
	public static final String KEY_CANCION_SELECCIONADA = "seleccionada";
	public static final String KEY_PLAYLIST_SELECCIONADA = "playlistseleccionado";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		crearConexionBaseDatos();
		//verifica que no se hayan insertado canciones a la db para llenarla
		
		//this.llenarBaseDatos(Reproductor.directorioMusica);
		/*
		Button player = (Button) findViewById(R.id.cerrar);
		player.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent nuevoPlayer = new Intent(v.getContext(),Reproductor.class);
				startActivityForResult(nuevoPlayer, 0);	
			}
		});*/
		
		//Control de los TABS
		Resources res = getResources();
		 
		TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
		tabs.setup();
		 
		TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("GSonic",
		    res.getDrawable(android.R.drawable.ic_btn_speak_now));
		tabs.addTab(spec);
		 
		spec=tabs.newTabSpec("mitab2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Artistas",
		    res.getDrawable(android.R.drawable.ic_menu_info_details));
		tabs.addTab(spec);
		 
		spec=tabs.newTabSpec("mitab3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("Album",
		    res.getDrawable(android.R.drawable.ic_popup_disk_full));
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab4");
		spec.setContent(R.id.tab4);
		spec.setIndicator("Busqueda",
		    res.getDrawable(android.R.drawable.ic_menu_search));
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab5");
		spec.setContent(R.id.tab5);
		spec.setIndicator("Listas de Reproduccion",
		    res.getDrawable(android.R.drawable.ic_dialog_map));
		tabs.addTab(spec);
		
		tabs.setCurrentTab(0);
		
		//PESTAÑA DE CANCIONES Y BUSQUEDA
		//listar todas las canciones
		listarCanciones();
		
		Button boton = (Button)findViewById(R.id.regresar);		
		boton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				texto = (EditText)findViewById(R.id.txtBusqueda);				
				crearConexionBaseDatos();
		        ArrayList<Cancion> canciones = baseDatos.buscarCanciones(texto.getText().toString());				
		        ListView lv = (ListView)findViewById(R.id.lista);		             
		        ItemCancionAdapter adapter = new ItemCancionAdapter(getActivity(), canciones);		             
		        lv.setAdapter(adapter);		     */   
				//String value = verificar(texto.getText().toString());
				//mostrar(value);				
				
				busqueda();
			}			
		});
		
		texto = (EditText) findViewById(R.id.txtBusqueda);
		texto.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub	
				busqueda();			
				return false;
			}
		});
		
		lista = (ListView)findViewById(R.id.lista);
		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Cancion seleccionado = (Cancion)lista.getItemAtPosition(position);
				Intent nuevaActividad = new Intent(arg0.getContext(),Reproductor.class);
				Bundle b = new Bundle();
				//////////////////////////////////////////////////////////////////////
				Playlist resultado = new Playlist();
				resultado.setNombre("Playlist");
				ItemCancionAdapter adaptador = (ItemCancionAdapter)lista.getAdapter();
				resultado.setListaCanciones(adaptador.getItems());
				//b.putSerializable(MainActivity.KEY_PLAYLIST_SELECCIONADA, resultado);
				/////////////////////////////////////////////////////////////////////				
				b.putInt(MainActivity.KEY_CANCION_SELECCIONADA, seleccionado.get_id());
				nuevaActividad.putExtras(b);
				startActivity(nuevaActividad);
			}
			
		});
		
		//MANEJO DE LA PESTAÑA DE ARTISTA
		//listar
		listarArtistas();
		
		Button botonBusquedaArtista = (Button)findViewById(R.id.botonBusquedaArtista);
		botonBusquedaArtista.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				busquedaArtista();
				
			}
		});
		
		textoArtista = (EditText)findViewById(R.id.textArtista);
		textoArtista.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				busquedaArtista();
				return false;
			}
		});
		
		listaArtista = (ListView) findViewById(R.id.listaArtistas);
		listaArtista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				String seleccion = (String)listaArtista.getItemAtPosition(position);
				Playlist mPlaylist = new Playlist();
				mPlaylist.setNombre(seleccion);
				//mostrarTexto(seleccion);
				mPlaylist.setListaCanciones(baseDatos.buscarCancionesPor(BaseDatosHelper.COLUMNA_ARTISTA, seleccion));
				Intent reproductorActivity = new Intent(arg0.getContext(),Reproductor.class);
				Bundle parametros = new Bundle();
				//PASAR EL PLAYLIST AL REPRODUCTOR
				parametros.putSerializable(MainActivity.KEY_PLAYLIST_SELECCIONADA, mPlaylist);
				reproductorActivity.putExtras(parametros);
				startActivity(reproductorActivity);
			}
		});
		
		//MANEJO DE LA PESTAÑA DE ALBUM
		listarAlbums();
		
		Button botonBusquedaAlbum = (Button)findViewById(R.id.botonBusquedaAlbum);
		botonBusquedaAlbum.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				busquedaAlbum();
			}
		});
		
		textoAlbum = (EditText)findViewById(R.id.textAlbum);
		textoAlbum.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				busquedaAlbum();
				return false;
			}
		});
		
		listaAlbum = (ListView) findViewById(R.id.listaAlbum);
		listaAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				String seleccion = (String)listaAlbum.getItemAtPosition(position);
				Playlist mPlaylist = new Playlist();
				mPlaylist.setNombre(seleccion);
				//mostrarTexto(seleccion);				
				mPlaylist.setListaCanciones(baseDatos.buscarCancionesPor(BaseDatosHelper.COLUMNA_ALBUM, seleccion));
				Intent reproductorActivity = new Intent(arg0.getContext(),Reproductor.class);
				Bundle parametros = new Bundle();
				//PASAR EL PLAYLIST AL REPRODUCTOR
				parametros.putSerializable(MainActivity.KEY_PLAYLIST_SELECCIONADA, mPlaylist);
				reproductorActivity.putExtras(parametros);
				//startActivity(reproductorActivity);

			}
		});

		
		//MANEJO DE PLAYLIST
		
		listarArtistas();
		
		Button btnBusquedaPlaylists = (Button)findViewById(R.id.btnbuscarplaylist);
		btnBusquedaPlaylists.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				busquedaPlayLists();
			}
		});
		
		texto = (EditText)findViewById(R.id.textAlbum);
		texto.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				busquedaPlayLists();
				return false;
			}
		});
		
		listaPlayLists = (ListView) findViewById(R.id.listaplaylists);
		listaPlayLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Playlist mPlaylist = (Playlist)listaPlayLists.getItemAtPosition(position);
				Intent reproductorActivity = new Intent(arg0.getContext(),Reproductor.class);
				Bundle parametros = new Bundle();
				//PASAR EL PLAYLIST AL REPRODUCTOR
				parametros.putSerializable(MainActivity.KEY_PLAYLIST_SELECCIONADA, mPlaylist);
				reproductorActivity.putExtras(parametros);
				//startActivity(reproductorActivity);

			}
		});
		
		//
		
		Button botonActualizarDB = (Button)findViewById(R.id.btnUpdate);
		botonActualizarDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText et = (EditText)findViewById(R.id.txtRuta);
				String direccion = et.getText().toString();
				llenarBaseDatos(direccion);
				listarAlbums();
				listarArtistas();
				listarCanciones();
			}
		});
		
		Button cerrar = (Button)findViewById(R.id.botonSalir);
		cerrar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//finish();
				System.exit(0);
				
			}
		});
	}
	
	private void mostrarTexto(String tx){
		Toast.makeText(this, tx, Toast.LENGTH_SHORT).show();
	}
	
	private void listarPlayLists() {
		// TODO Auto-generated method stub
		ArrayList<String> playlists = baseDatos.obtenerPor(BaseDatosHelper.TABLA_PLAYLIST);
		ListView lv = (ListView)findViewById(R.id.listaplaylists);
		ItemAlbumAdapter adapter = new ItemAlbumAdapter(getActivity(),playlists,this);
		lv.setAdapter(adapter);
	}
	
	private void listarAlbums() {
		// TODO Auto-generated method stub
		ArrayList<String> albums = baseDatos.obtenerPor(BaseDatosHelper.COLUMNA_ALBUM);
		ListView lv = (ListView)findViewById(R.id.listaAlbum);
		ItemAlbumAdapter adapter = new ItemAlbumAdapter(getActivity(),albums,this);
		lv.setAdapter(adapter);
	}

	private void listarArtistas() {
		// TODO Auto-generated method stub
		ArrayList<String> artistas = baseDatos.obtenerPor(BaseDatosHelper.COLUMNA_ARTISTA);
		ListView lv = (ListView)findViewById(R.id.listaArtistas);
		ItemArtistaAdapter adapter = new ItemArtistaAdapter(getActivity(),artistas);
		lv.setAdapter(adapter);
	}

	private void crearConexionBaseDatos(){
		if (this.baseDatos==null)
			baseDatos = new BaseDatosHelper(this);
	}
	public void llenarBaseDatos(String directorio){
		File f = new File(directorio);
		if (f.exists()){
			this.crearConexionBaseDatos();
			//if (this.baseDatos.existenCanciones()==false){ //si no hay canciones, que insterte todo en la base de datos
				ListadoArchivos.recorrerDirectorios(directorio,this);
			//}
			//verificar integridad de la base de datos
				baseDatos.verificarItems();
		}
	}
	
	private Activity getActivity(){
		return this;
	}
	
	private void busqueda(){
		texto = (EditText)findViewById(R.id.txtBusqueda);
		crearConexionBaseDatos();
        // Obtenemos la lista de canciones
        ArrayList<Cancion> canciones = baseDatos.buscarCanciones(texto.getText().toString());				
        ListView lv = (ListView)findViewById(R.id.lista);		             
        ItemCancionAdapter adapter = new ItemCancionAdapter(getActivity(), canciones);		             
        lv.setAdapter(adapter);	
	}
	
	public void listarCanciones(){
		ArrayList<Cancion> canciones = baseDatos.obtenerCanciones();				
        ListView lv = (ListView)findViewById(R.id.lista);		             
        ItemCancionAdapter adapter = new ItemCancionAdapter(getActivity(), canciones);		             
        lv.setAdapter(adapter);	
	}
	
	private void busquedaArtista(){
		textoArtista = (EditText)findViewById(R.id.textArtista);				
		crearConexionBaseDatos();
        ArrayList<String> artistas = baseDatos.buscarPorColumna(BaseDatosHelper.COLUMNA_ARTISTA, textoArtista.getText().toString());
        ListView lv = (ListView)findViewById(R.id.listaArtistas);
        ItemArtistaAdapter adapter = new ItemArtistaAdapter(getActivity(), artistas);
        lv.setAdapter(adapter);
	}
	
	private void busquedaAlbum(){
		textoAlbum = (EditText)findViewById(R.id.textAlbum);
		crearConexionBaseDatos();
		ArrayList<String> albums = baseDatos.buscarPorColumna(BaseDatosHelper.COLUMNA_ALBUM, textoAlbum.getText().toString());
		ListView lv = (ListView)findViewById(R.id.listaAlbum);
		ItemAlbumAdapter adapter = new ItemAlbumAdapter(getActivity(), albums,this);
		lv.setAdapter(adapter);
	}
	
	private void busquedaPlayLists(){
		texto = (EditText)findViewById(R.id.txtnombreplaylist);				
		crearConexionBaseDatos();
        ArrayList<String> playlists = baseDatos.buscarPorColumna(BaseDatosHelper.TABLA_PLAYLIST, texto.getText().toString());
        ListView lv = (ListView)findViewById(R.id.listaplaylists);
        ItemArtistaAdapter adapter = new ItemArtistaAdapter(getActivity(), playlists);
        lv.setAdapter(adapter);
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}
		
}
