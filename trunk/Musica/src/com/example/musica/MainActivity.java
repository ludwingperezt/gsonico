package com.example.musica;

import java.io.File;
import java.util.ArrayList;

import modelos.BaseDatosHelper;
import modelos.Cancion;
import modelos.Metadatos;
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
	private ListView lista;
	public static final String KEY_CANCION_SELECCIONADA = "seleccionada";
	public static final String KEY_PLAYLIST_SELECCIONADA = "playlistseleccionado";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//verifica que no se hayan insertado canciones a la db para llenarla
		this.llenarBaseDatos(Reproductor.directorioMusica);
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
		spec.setIndicator("Escuchando...",
		    res.getDrawable(android.R.drawable.ic_btn_speak_now));
		tabs.addTab(spec);
		 
		spec=tabs.newTabSpec("mitab2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Letra",
		    res.getDrawable(android.R.drawable.ic_menu_info_details));
		tabs.addTab(spec);
		 
		spec=tabs.newTabSpec("mitab3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("Artistas",
		    res.getDrawable(android.R.drawable.ic_popup_disk_full));
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab4");
		spec.setContent(R.id.tab4);
		spec.setIndicator("Canciones",
		    res.getDrawable(android.R.drawable.ic_dialog_map));
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab5");
		spec.setContent(R.id.tab5);
		spec.setIndicator("Listas",
		    res.getDrawable(android.R.drawable.ic_dialog_map));
		tabs.addTab(spec);
		
		tabs.setCurrentTab(0);
		
		Button boton = (Button)findViewById(R.id.regresar);		
		boton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				texto = (EditText)findViewById(R.id.txtBusqueda);				
				crearConexionBaseDatos();
		        ArrayList<Cancion> canciones = baseDatos.buscarCanciones(texto.getText().toString());				
		        ListView lv = (ListView)findViewById(R.id.lista);		             
		        ItemCancionAdapter adapter = new ItemCancionAdapter(getActivity(), canciones);		             
		        lv.setAdapter(adapter);		        
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
				b.putParcelable(MainActivity.KEY_CANCION_SELECCIONADA, seleccionado);
				nuevaActividad.putExtras(b);
				startActivity(nuevaActividad);
			}
			
		});

	}
	
	private void crearConexionBaseDatos(){
		if (this.baseDatos==null)
			baseDatos = new BaseDatosHelper(this);
	}
	public void llenarBaseDatos(String directorio){
		File f = new File(directorio);
		if (f.exists()){
			this.crearConexionBaseDatos();
			if (this.baseDatos.existenCanciones()==false){ //si no hay canciones, que insterte todo en la base de datos
				ListadoArchivos.recorrerDirectorios(directorio,this);
			}
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

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}
		
}
