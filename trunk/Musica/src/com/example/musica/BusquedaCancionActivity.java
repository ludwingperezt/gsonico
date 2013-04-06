package com.example.musica;

import java.util.ArrayList;

import modelos.BaseDatosHelper;
import modelos.Cancion;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class BusquedaCancionActivity extends Activity{
	
	private BaseDatosHelper baseDatos;
	private EditText texto;
	private ListView lista;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busquedacancion);
		
		crearConexion();
		Button boton = (Button)findViewById(R.id.botonBusqueda);		
		boton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				texto = (EditText)findViewById(R.id.txtBusqueda);				
				crearConexion();
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
				//mostrar(Integer.toString(seleccionado.get_id())+" "+seleccionado.getAutor()+" "+seleccionado.getTitulo());
				Intent i = new Intent();
				Bundle b = new Bundle();
				b.putString("seleccionado", seleccionado.getTitulo());
				i.putExtras(b);
				setResult(RESULT_OK,i);
				
				finish();
			}
			
		});
		
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
