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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class SeleccionCancionesPlaylist extends Activity{
	
	private BaseDatosHelper baseDatos;
	private static  EditText texto;
	private ListView lista;
	private ArrayList<Cancion> cancionesSeleccionadas = new ArrayList<Cancion>();
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seleccion_canciones_playlist);
		
		crearConexion();
		listaCanciones();
		Button btnGuardar = (Button)findViewById(R.id.botonGuardarPlaylist);
		lista = (ListView)findViewById(R.id.listaCancionesPlaylist);
		
		lista.setAdapter(new ArrayAdapter<Cancion>(this, android.R.layout.simple_list_item_multiple_choice, baseDatos.obtenerCanciones()));
		lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lista.setItemsCanFocus(false);
		
		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			CheckedTextView ctv = (CheckedTextView)arg1;
			}
		});
		
		btnGuardar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				texto = (EditText) findViewById(R.id.txtNombrePlayList);
				int tamaño = baseDatos.obtenerTodosPlaylist().size();
				
				SparseBooleanArray sparseBooleanArray = lista.getCheckedItemPositions();
				if (sparseBooleanArray!=null){
					for (int i=0; i<lista.getCount(); i++){
						Cancion cancionSelec = (Cancion)lista.getItemAtPosition(i);
						if (sparseBooleanArray.get(i) == true){
							if (cancionSelec!=null)
								cancionesSeleccionadas.add(cancionSelec);
						}		
					}
				
				Playlist playlistNueva = new Playlist();
				playlistNueva.setListaCanciones(cancionesSeleccionadas);
				if(texto.getText().length()==0){
					Toast.makeText(getApplicationContext(), "El nombre de la lista sera: Lista de Reproduccion "+String.valueOf(tamaño), Toast.LENGTH_LONG).show();
					playlistNueva.setNombre("Lista de Reproduccion "+String.valueOf(tamaño));
				}					
				if (baseDatos.existePlaylist(texto.getText().toString())){
					Toast.makeText(getApplicationContext(), "Ya existe una lista de reproduccion con ese nombre.", Toast.LENGTH_LONG).show();
					texto.setText("");
				}
				else
					playlistNueva.setNombre(texto.getText().toString());
				if (cancionesSeleccionadas.size()!=0 && playlistNueva.getNombre()!=null){
					int index = baseDatos.insertarPlaylist(playlistNueva);
					if (index!=-1)
						Toast.makeText(getApplicationContext(), "Lista insertada con éxito", Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(getApplicationContext(), "No se ha guardado la lista de reproduccion", Toast.LENGTH_LONG).show();
				}
				
			}			
		});
		
		texto = (EditText) findViewById(R.id.txtNombrePlayList);
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
	
	private void listaCanciones(){
		crearConexion();
        // Obtenemos la lista de canciones
        ArrayList<Cancion> canciones = baseDatos.obtenerCanciones();				
        ListView lv = (ListView)findViewById(R.id.listaCancionesPlaylist);		             
        ItemCancionAdapter adapter = new ItemCancionAdapter(getActivity(), canciones);		             
        lv.setAdapter(adapter);	
	}

}
