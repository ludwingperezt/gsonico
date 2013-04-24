package com.example.musica;

import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.Parser;

import modelos.BaseDatosHelper;
import modelos.Cancion;
import modelos.Playlist;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class SeleccionCancionesPlaylist extends Activity{
	
	private BaseDatosHelper baseDatos;
	private EditText texto;
	private ListView lista;
	private ArrayList<Cancion> cancionesSeleccionadas = new ArrayList<Cancion>();
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seleccion_canciones_playlist);
		
		crearConexion();
		Button btnGuardar = (Button)findViewById(R.id.guardar);
		lista = (ListView)findViewById(R.id.listaCancionesPlaylist);
		btnGuardar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				texto = (EditText)findViewById(R.id.txtBusqueda);
				int tamaño = baseDatos.obtenerTodosPlaylist().size();
				
				SparseBooleanArray sparseBooleanArray = lista.getCheckedItemPositions();
				for (int i=0; i<lista.getCount(); i++){
					Cancion cancionSelec = (Cancion)lista.getItemAtPosition(i);
					if (sparseBooleanArray.get(i) == true){
						if (cancionSelec!=null)
							cancionesSeleccionadas.add(cancionSelec);
					}		
				}
				
				Playlist playlistNueva = new Playlist();
				playlistNueva.setListaCanciones(cancionesSeleccionadas);
				if(texto.length()==0){
					Toast.makeText(getApplicationContext(), "El nombre de la lista sera: Lista de Reproduccion "+String.valueOf(tamaño), Toast.LENGTH_LONG).show();
					playlistNueva.setNombre("Lista de Reproduccion "+String.valueOf(tamaño));
				}					
				if (baseDatos.existePlaylist(texto.getText().toString())){
					Toast.makeText(getApplicationContext(), "Ya existe una lista de reproduccion con ese nombre.", Toast.LENGTH_LONG).show();
					texto.setText("");
				}
				else
					playlistNueva.setNombre(texto.getText().toString());
				if (cancionesSeleccionadas.size()!=0 && playlistNueva.getNombre()!=null)
					baseDatos.insertarPlaylist(playlistNueva);
				else
					Toast.makeText(getApplicationContext(), "No se ha guardado la lista de reproduccion", Toast.LENGTH_LONG).show();
			}			
		});
		
		texto = (EditText) findViewById(R.id.txtBusqueda);
		lista = (ListView) findViewById(R.id.listaCancionesPlaylist);
		
		
		
	}
	
	private void crearConexion() {
		// TODO Auto-generated method stub
		if (this.baseDatos==null)
			this.baseDatos = new BaseDatosHelper(this);
	}
	
	private Activity getActivity(){
		return this;
	}
	
	private void busqueda(){
		texto = (EditText)findViewById(R.id.txtBusqueda);
		crearConexion();
        // Obtenemos la lista de canciones
        ArrayList<Cancion> canciones = baseDatos.buscarCanciones(texto.getText().toString());				
        ListView lv = (ListView)findViewById(R.id.lista);		             
        ItemCancionAdapter adapter = new ItemCancionAdapter(getActivity(), canciones);		             
        lv.setAdapter(adapter);	
	}

}
