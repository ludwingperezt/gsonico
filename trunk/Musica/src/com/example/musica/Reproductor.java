package com.example.musica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import javax.xml.transform.stream.StreamResult;

import modelos.BaseDatosHelper;
import modelos.Cancion;
import modelos.Letra;
import modelos.Metadatos;
import modelos.Playlist;
import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressWarnings("deprecation")
@SuppressLint({ "SdCardPath", "ShowToast" })
public class Reproductor extends ActivityGroup implements OnCompletionListener {
	MediaPlayer player;
	Chronometer tiempo;
	SeekBar barraCronometro;
	boolean enStop=false;
	long elapsed=0;
	int estado=0;
	int num_track=0;
	public static final String directorioMusica = "/mnt/sdcard/music/";
	//final String[] listado = ListadoArchivos.devolverListadoArchivosDirectorios(Reproductor.directorioMusica);
	//private Cancion seleccionada;
	//private Cancion anterior;
	private Cancion actual;
	//private Cancion siguiente;
	private String letraActual;
	private Playlist lista;
	//control de reproduccion
	private Stack<Cancion> anteriores = new Stack<Cancion>();
	private ArrayList<Cancion> siguientes;
	private boolean repetir=false;
	private boolean aleatorio=false;
	//
	private BaseDatosHelper conexionBaseDatos;
	private Bundle parametros;
	private TextView Meta;
	private TextView mostrarLetra;
	//
	private Random r = new Random();
	private Letra letraRola;
	
	public void inicializarCronometro()
	{
		
		try {
			cargarLetra();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "No se encontro el archivo de letra especificado.", Toast.LENGTH_LONG).show();
		}		
		elapsed=0;
		tiempo.setBase(SystemClock.elapsedRealtime());
	}
	
	@SuppressLint({ "SdCardPath", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reproducir);
		this.crearConexionBaseDatos();
		//Control de los TABS
		
		Resources res = getResources();
		 
		TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
		TabHost.TabSpec spec;
		tabs.setup(this.getLocalActivityManager());
		Intent intent;
		 
		spec=tabs.newTabSpec("mitab1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Escuchando...",
		    res.getDrawable(android.R.drawable.ic_media_play));
		tabs.addTab(spec);
		 
		spec=tabs.newTabSpec("mitab2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Letra",
		    res.getDrawable(android.R.drawable.ic_menu_info_details));
		tabs.addTab(spec);
		
		intent = new Intent().setClass(this, SeleccionCancionesPlaylist.class);
		spec = tabs.newTabSpec("mitab3").setIndicator("Nueva Lista de Reproduccion",res.getDrawable(android.R.drawable.list_selector_background)).setContent(intent);
		tabs.addTab(spec);
		
		tabs.setCurrentTab(0);
		Meta=(TextView)findViewById(R.id.txtInfo);
		mostrarLetra=(TextView)findViewById(R.id.letra);
		this.parametros = getIntent().getExtras();
		if (this.parametros!=null){
			//this.seleccionada = conexionBaseDatos.obtenerCancion(this.parametros.getInt(MainActivity.KEY_CANCION_SELECCIONADA));
			//this.lista = conexionBaseDatos.obtenerPlaylistPorID(this.parametros.getInt(MainActivity.KEY_PLAYLIST_SELECCIONADA));
			this.lista = (Playlist)this.parametros.getSerializable(MainActivity.KEY_PLAYLIST_SELECCIONADA);
			this.siguientes = this.lista.getListaCanciones();
		}
		
		Button pausa = (Button) findViewById(R.id.play_pause);
		Button siguiente = (Button) findViewById(R.id.next);		
		Button detener = (Button) findViewById(R.id.stop);
		Button cerrar = (Button) findViewById(R.id.cerrar);
		Button atras= (Button) findViewById(R.id.atras);
		ToggleButton BtnAleatorio=(ToggleButton)findViewById(R.id.tbtnAleat);
		ToggleButton BtnRepetir=(ToggleButton)findViewById(R.id.tbtnRepetir);
		siguiente.setBackgroundResource(getResources().getIdentifier("drawable/skip_forward32",null, getPackageName()));
		atras.setBackgroundResource(getResources().getIdentifier("drawable/skip_backward32",null, getPackageName()));
		detener.setBackgroundResource(getResources().getIdentifier("drawable/stop32",null, getPackageName()));
		pausa.setBackgroundResource(getResources().getIdentifier("drawable/pause48",null, getPackageName()));
		
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
						v.setBackgroundResource(getResources().getIdentifier("drawable/pause48" ,null, getPackageName()));
					}
					else{ 
						player.pause();
						estado = 1;
						tiempo.stop();
						elapsed=SystemClock.elapsedRealtime()-tiempo.getBase();
						v.setBackgroundResource(getResources().getIdentifier("drawable/play48" ,null, getPackageName()));
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
					if (actual!=null){
						Cancion tmp;
						if (enStop==false){
							tmp = getNext();
						}
						else{
							tmp = actual; //???????????? no se si esta linea está correcta, no se que hace
							enStop = false;
						}
						
						if (tmp!=null){
							anteriores.push(actual);
							actual = tmp;
							player.setDataSource(actual.getArchivoAudio());
							player.prepare();
							player.start();
							Toast.makeText(getApplicationContext(),"Duracion: "+ aMinutos(player.getDuration())+ " Minutos", Toast.LENGTH_LONG).show();
							Meta.setText(actual.getTitulo()+" - "+ actual.getArtista());
							Button pausa = (Button) findViewById(R.id.play_pause);
							pausa.setBackgroundResource(getResources().getIdentifier("drawable/pause48" ,null, getPackageName()));
							inicializarCronometro();
							tiempo.start();	
							//pausa.setBackgroundResource(getResources().getIdentifier("drawable/pause48" ,null, getPackageName()));
						}
					}
					/*
					if (actual!=null){
						//crearConexionBaseDatos();
						Cancion tmp;
						if (enStop==false)
							tmp = conexionBaseDatos.obtenerCancion(actual.get_id()+1);
						else{
							tmp = conexionBaseDatos.obtenerCancion(actual.get_id());
							enStop=false;
						}
							
						if (tmp!=null){
							anterior = actual;
							actual = tmp;
							player.setDataSource(actual.getArchivoAudio());
							player.prepare();
							player.start();
							//Toast.makeText(getApplicationContext(),"Duracion: "+ player.getDuration(), Toast.LENGTH_LONG).show();
							//Toast.makeText(getApplicationContext(),"ID: "+ Integer.toString(actual.get_id()), Toast.LENGTH_LONG).show();
							Toast.makeText(getApplicationContext(),"Duracion: "+ aMinutos(player.getDuration())+ " Minutos", Toast.LENGTH_LONG).show();
							Meta.setText(actual.getTitulo()+" - "+ actual.getArtista());
							inicializarCronometro();
							tiempo.start();	
						}
					}	*/				
				}catch(Exception e){	
				}
			}
		});
		
		atras.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				player.reset();
				num_track--;
				if (num_track<0)
					num_track=0;
				else
					siguientes.add(0, actual);
				try{
					if (!anteriores.empty())
						actual=anteriores.pop();					
					player.setDataSource(actual.getArchivoAudio());
					player.prepare();
					player.start();
					Toast.makeText(getApplicationContext(),"Duracion: "+ aMinutos(player.getDuration())+ " Minutos", Toast.LENGTH_LONG).show();
					Meta.setText(actual.getTitulo()+" - "+ actual.getArtista());
					Button pausa = (Button) findViewById(R.id.play_pause);
					pausa.setBackgroundResource(getResources().getIdentifier("drawable/pause48" ,null, getPackageName()));
					inicializarCronometro();
					tiempo.start();	
				}
				catch (Exception e) {
					// TODO: handle exception
					}
			}
		});
		
		detener.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button pausa = (Button) findViewById(R.id.play_pause);
				pausa.setBackgroundResource(getResources().getIdentifier("drawable/play48" ,null, getPackageName()));
				inicializarCronometro();
				player.stop();
			}
		});
		
		/*
		buscar.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent nuevaActividad = new Intent(arg0.getContext(),BusquedaCancionActivity.class);
				startActivityForResult(nuevaActividad, 0);		
				
			}
		});
		*/
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		player = new MediaPlayer();
		
		try{
			if (this.siguientes!=null){
				this.actual = this.getNext();
				if (actual!=null){
					File f = new File(actual.getArchivoAudio());
					if (f.exists()){
						player.setDataSource(actual.getArchivoAudio());
						player.prepare();
						inicializarCronometro();
						player.start();
						tiempo.start();
						Toast.makeText(getApplicationContext(),"Duracion: "+ aMinutos(player.getDuration())+ " Minutos", Toast.LENGTH_LONG).show();
						Meta.setText(actual.getTitulo()+" - "+ actual.getArtista());
						player.setOnCompletionListener(this);
					}
				}
			}
			/*
			if (this.seleccionada!=null){
				File f = new File(this.seleccionada.getArchivoAudio());
				if (f.exists()){
					this.actual = seleccionada;
					player.setDataSource(this.seleccionada.getArchivoAudio());
					player.prepare();
					inicializarCronometro();
					player.start();
					tiempo.start();
					//Toast.makeText(getApplicationContext(),"ID: "+ Integer.toString(actual.get_id()), Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(),"Duracion: "+ aMinutos(player.getDuration())+ " Minutos", Toast.LENGTH_LONG).show();
					Meta.setText(actual.getTitulo()+" - "+ actual.getArtista());
					player.setOnCompletionListener(this);
				}				
			}*/			
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
				String prox=letraRola.getProximaLinea(actual);
				if (prox!=null)
					if (!prox.isEmpty()){
						TextView lblLetra=(TextView)findViewById(R.id.lblLetra);
						lblLetra.setText(prox);
					}
			}
		});
		
		cerrar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				player.stop();
				inicializarCronometro();
				finish();
			}
		});
		
		BtnRepetir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	repetir=true;
		        } else {
		        	repetir=false;
		        }
		    }
		});
		
		BtnAleatorio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	aleatorio=true;
		        } else {
		            aleatorio=false;
		        }
		    }
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}
	

	public void onCompletion(MediaPlayer arg0) {
		Button sig = (Button) findViewById(R.id.next);
		//if (num_track==listado.length-1)
		//this.crearConexionBaseDatos();
		/*
		if (conexionBaseDatos.ultimoId(actual.get_id())==true){
			player.stop();
			
		}
		else{
			enStop=true;
			sig.performClick();
		}*/
		if (siguientes==null)
			player.stop();
		else{
			if (this.siguientes.size()==0){
				if (this.repetir){
					this.siguientes.addAll(this.anteriores);
					this.anteriores.clear();
					enStop=true;
					sig.performClick();
				}
				else
					player.stop();
			}
			else{
				enStop=true;
				sig.performClick();
			}
		}			
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
	
	private void cargarLetra() throws IOException{
		if (actual!=null){
		String rutaLetra=actual.getArchivoAudio().replace("mp3", "lrc");
		File f = new File(rutaLetra);
		letraRola=new Letra();
		if (f.exists()){
			letraActual=rutaLetra;
			BufferedReader br= new BufferedReader(new FileReader(letraActual));
			String linea;			
			while ((linea=br.readLine())!=null){
				letraRola.agregarLineaTexto(linea);
			}
			mostrarLetra.setText(letraRola.getTodaLetra());
		}
		else
			mostrarLetra.setText("LETRA NO DISPONIBLE");
		}
		
	}
	
	private Cancion getNext(){
		Cancion c = null;
		
		if (this.aleatorio){			
			if (this.siguientes!=null){
				if (this.siguientes.size()>0){
					int index = r.nextInt(this.siguientes.size());
					c = this.siguientes.remove(index);
				}
			}
			
		}
		else{
			if (this.siguientes!=null){
				if (this.siguientes.size()>0){
					c = this.siguientes.remove(0);
				}
			}
		}
		
		return c;
	}
	/*
	private void reproducir(){
		try{
			if (this.siguientes!=null){
				this.actual = this.getNext();
				if (actual!=null){
					File f = new File(actual.getArchivoAudio());
					if (f.exists()){
						player.setDataSource(actual.getArchivoAudio());
						player.prepare();
						inicializarCronometro();
						player.start();
						tiempo.start();
						Toast.makeText(getApplicationContext(),"Duracion: "+ aMinutos(player.getDuration())+ " Minutos", Toast.LENGTH_LONG).show();
						Meta.setText(actual.getTitulo()+" - "+ actual.getArtista());
						player.setOnCompletionListener(this);
					}
				}
			}
		}
		catch (Exception ex){
			
		}
		
	}*/
	/*
	public void llenarBaseDatos(String directorio){
		File f = new File(directorio);
		if (f.exists()){
			this.crearConexionBaseDatos();
			if (this.conexionBaseDatos.existenCanciones()==false){ //si no hay canciones, que insterte todo en la base de datos
				ListadoArchivos.recorrerDirectorios(directorio,this);
			}
		}
	}*/
}
