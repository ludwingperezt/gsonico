package com.example.musica;
import java.util.ArrayList;

import modelos.Cancion;
import modelos.Playlist;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ItemPlayListAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Playlist> items;
	
	public ItemPlayListAdapter(Activity activity, ArrayList<Playlist> items) {
	    this.activity = activity;
	    this.items = items;
	}
	
	public ItemPlayListAdapter(Activity activity, ArrayList<Playlist> items, Context contexto) {
	    this.activity = activity;
	    this.items = items;
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
	      vi = inflater.inflate(R.layout.lista_artista_item, null);
	    }
	             
	    Playlist item = items.get(position);
	    
	    TextView txtTitulo = (TextView) vi.findViewById(R.id.artistaNombre);
	    if (item==null){
	    	txtTitulo.setText("Lista");	         
	    }
	    else{
	    	if (item.getNombre().equals(""))
	    	txtTitulo.setText(item.getNombre());	         
	    }
	    return vi;
	}

}
