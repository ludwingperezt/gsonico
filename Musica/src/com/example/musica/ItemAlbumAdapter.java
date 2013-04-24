package com.example.musica;

import java.io.File;
import java.util.ArrayList;

import modelos.BaseDatosHelper;
import modelos.Cancion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAlbumAdapter extends BaseAdapter {
	
	protected Activity activity;
	protected ArrayList<String> items;
	protected Context contexto;
	
	/*
	public ItemAlbumAdapter(Activity activity, ArrayList<String> items){
		this.activity = activity;
	    this.items = items;
	}*/
	
	public ItemAlbumAdapter(Activity activity, ArrayList<String> items, Context contexto){
		this.activity = activity;
	    this.items = items;
	    this.contexto = contexto;
	}
	
	public void setContext(Context context){
		this.contexto = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View vi=convertView;
        
	    if(convertView == null) {
	      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      vi = inflater.inflate(R.layout.lista_album_item, null);
	    }
	             
	    String item = items.get(position);
	    
	    if (item==null){
	    	TextView txtTitulo = (TextView) vi.findViewById(R.id.albumTitulo);
		    txtTitulo.setText("Album desconocido");
		    
		    TextView txtArtista = (TextView) vi.findViewById(R.id.albumArtista);
		    txtArtista.setText("Artista desconocido");
	    }
	    else{
	    	if (items.equals("")){
	    		TextView txtTitulo = (TextView) vi.findViewById(R.id.albumTitulo);
			    txtTitulo.setText("Album desconocido");
			    
			    TextView txtArtista = (TextView) vi.findViewById(R.id.albumArtista);
			    txtArtista.setText("Artista desconocido");
	    	}
	    	else{
	    		TextView txtTitulo = (TextView) vi.findViewById(R.id.albumTitulo);
			    txtTitulo.setText(item);	
			    
			    BaseDatosHelper db = new BaseDatosHelper(this.contexto);
			    
			  //HABRÁ PROBLEMAS CUANDO EXISTAN DOS ALBUMS CON EL MISMO NOMBRE, PORQUE LA IMAGEN Y EL ARTISTA SE OBTIENE DE LA PRIMERA CANCION
			    ArrayList<Cancion> canciones = db.buscarCancionesPor(BaseDatosHelper.COLUMNA_ALBUM, item);
			    Cancion tmp = canciones.get(0);
			    if (tmp!=null){	    
				    ImageView image = (ImageView) vi.findViewById(R.id.albumIcono);
				    Bitmap bmp = ListadoArchivos.getAlbumArtSmall(tmp.getArchivoAudio());
				    if (bmp!=null)
				    	image.setImageBitmap(bmp);
				    else{
				    	image.setImageResource(R.drawable.icono);
				    }
				    
				    TextView txtArtista = (TextView) vi.findViewById(R.id.albumArtista);
				    txtArtista.setText(tmp.getArtista());
			    }
			    else{
			    	TextView txtArtista = (TextView) vi.findViewById(R.id.albumArtista);
				    txtArtista.setText("Artista desconocido");
			    }
	    	}
	    }
	    
		return vi;
	}

}
