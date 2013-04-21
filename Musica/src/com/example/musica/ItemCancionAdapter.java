package com.example.musica;

import java.util.ArrayList;


import modelos.Cancion;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemCancionAdapter extends BaseAdapter {
	
	protected Activity activity;
	protected ArrayList<Cancion> items;
	
	public ItemCancionAdapter(Activity activity, ArrayList<Cancion> items) {
	    this.activity = activity;
	    this.items = items;
	}

	public ArrayList<Cancion> getItems(){
		return this.items;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return items.get(position).get_id();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi=convertView;
        
	    if(convertView == null) {
	      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      vi = inflater.inflate(R.layout.lista_item, null);
	    }
	             
	    Cancion item = items.get(position);
	         
	    //para la imagen:
	    /*ImageView image = (ImageView) vi.findViewById(R.id.imagen);
	    int imageResource = activity.getResources().getIdentifier(item.getRutaImagen(), null, activity.getPackageName());
	    image.setImageDrawable(activity.getResources().getDrawable(imageResource));*/
	         
	    TextView txtTitulo = (TextView) vi.findViewById(R.id.titulo);
	    txtTitulo.setText(item.getTitulo());
	         
	    TextView txtArtista = (TextView) vi.findViewById(R.id.artista);
	    txtArtista.setText(item.getArtista());
	    
	    TextView txtAlbum = (TextView) vi.findViewById(R.id.album);
	    txtAlbum.setText(item.getAlbum());
	    
	    TextView txtGenero = (TextView) vi.findViewById(R.id.genero);
	    txtGenero.setText(item.getGenero());
	    
	    TextView txtYear = (TextView) vi.findViewById(R.id.year);
	    //txtYear.setText(item.getYear());
	    txtYear.setText(Integer.toString(item.get_id()));
	    return vi;
	}

}
