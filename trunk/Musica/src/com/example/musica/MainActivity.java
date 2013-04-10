package com.example.musica;

import java.io.File;
import java.io.IOException;

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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;
//import android.os.Bundle;
//import android.content.res.AssetFileDescriptor;
//import android.content.res.AssetManager;
import android.widget.Chronometer;
import android.os.*;


@SuppressLint({ "SdCardPath", "ShowToast" })
public class MainActivity extends Activity implements OnCompletionListener{
	MediaPlayer player;
	Chronometer tiempo;
	SeekBar barraCronometro;
	long elapsed=0;
	int estado=0;
	int num_track=0;
	public static final String directorioMusica = "/mnt/sdcard/music/";
	final String[] listado = ListadoArchivos.devolverListadoArchivosDirectorios(MainActivity.directorioMusica);
	
	private BaseDatosHelper conexionBaseDatos;
	
	public void inicializarCronometro()
	{
		elapsed=0;
		tiempo.setBase(SystemClock.elapsedRealtime());
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		spec.setContent(R.id.tab2);
		spec.setIndicator("Artistas",
		    res.getDrawable(android.R.drawable.ic_popup_disk_full));
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab4");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Canciones",
		    res.getDrawable(android.R.drawable.ic_dialog_map));
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab5");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Listas",
		    res.getDrawable(android.R.drawable.ic_dialog_map));
		tabs.addTab(spec);
		
		tabs.setCurrentTab(0);
		
		//verifica que no se hayan insertado canciones a la db para llenarla
		this.llenarBaseDatos(MainActivity.directorioMusica);
		
		Button pausa = (Button) findViewById(R.id.play_pause);
		Button siguiente = (Button) findViewById(R.id.next);
		Button detener = (Button) findViewById(R.id.stop);
		Button buscar = (Button) findViewById(R.id.botonBuscar);
		
		barraCronometro=(SeekBar)findViewById(R.id.SBTrayecto);
		
		
		//configurando cronometro...
		tiempo=(Chronometer)findViewById(R.id.tiempo);
		
		inicializarCronometro();
		
		
		pausa.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					if (estado == 1) {
						player.start();
						tiempo.setBase(SystemClock.elapsedRealtime()-elapsed);
						tiempo.start();
						
						elapsed=0;
						estado = 0;
					}
					else{ 
						player.pause();
						estado = 1;
						tiempo.stop();
						elapsed=SystemClock.elapsedRealtime()-tiempo.getBase();
					}
			}
		});
		
		siguiente.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				player.reset();
				num_track=num_track+1;
				try{					
					player.setDataSource("/mnt/sdcard/music/" + listado[num_track]);
					player.prepare();
					player.start();
					Toast.makeText(getApplicationContext(),"Duracion: "+ player.getDuration(), Toast.LENGTH_LONG).show();
					insertarCancion("/mnt/sdcard/music/" + listado[num_track]); //inserta la cancion a la base de datos
					inicializarCronometro();
					tiempo.start();
					//insertarCancion("/mnt/sdcard/music/" + listado[num_track]); //inserta la cancion a la base de datos
				}catch(Exception e){	
				}
			}
		});
		
		detener.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				player.stop();
				inicializarCronometro();
			}
		});
		
		
		buscar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent nuevaActividad = new Intent(arg0.getContext(),BusquedaCancionActivity.class);
				startActivityForResult(nuevaActividad, 0);		
				
			}
		});
		
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		player = new MediaPlayer();
		
		try{
			player.setDataSource("/mnt/sdcard/music/" + listado[num_track]);
			player.prepare();
			inicializarCronometro();
			//player.start();
			player.setOnCompletionListener(this);
		}catch(Exception e){	
		}
		
		tiempo.setOnChronometerTickListener(new OnChronometerTickListener() {
			
			@Override
			public void onChronometerTick(Chronometer arg0) {
				// TODO Auto-generated method stub
				float actual=SystemClock.elapsedRealtime()-tiempo.getBase();
				float duracion=player.getDuration();
				float p=(actual/duracion)*100;				
				barraCronometro.setProgress((int) (p));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Button sig = (Button) findViewById(R.id.next);
		if (num_track==listado.length-1)
			player.stop();
		else
			sig.performClick();
	}
	
	private void insertarCancion(String direccion){
		this.crearConexionBaseDatos();
		Metadatos mMeta = new Metadatos();
		Cancion mCancion = mMeta.leerEtiquetasCancion(direccion);
		this.conexionBaseDatos.insertarCancion(mCancion);
	}
	
	private void crearConexionBaseDatos(){
		if (this.conexionBaseDatos==null)
			conexionBaseDatos = new BaseDatosHelper(this);
	}
	
	private static float aMinutos(long milli)
	{
		float ret=(float) (milli*1.666666666666666666666666667*Math.pow(10,-5));
		return ret;
	}
	
	
	public void llenarBaseDatos(String directorio){
		File f = new File(directorio);
		if (f.exists()){
			this.crearConexionBaseDatos();
			if (this.conexionBaseDatos.existenCanciones()==false){ //si no hay canciones, que insterte todo en la base de datos
				ListadoArchivos.recorrerDirectorios(directorio,this);
			}
		}
	}
}
