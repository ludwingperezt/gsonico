package com.example.musica;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemArtistaAdapter extends BaseAdapter{
	
	protected Activity activity;
	protected ArrayList<String> items;
	
	public ItemArtistaAdapter(Activity activity, ArrayList<String> items){
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi=convertView;
        
	    if(convertView == null) {
	      LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      vi = inflater.inflate(R.layout.lista_artista_item, null);
	    }
	             
	    String item = items.get(position);
	    
	    TextView txtTitulo = (TextView) vi.findViewById(R.id.artistaNombre);
	    if ((item.equals(""))||(item==null)){
	    	txtTitulo.setText("Artista desconocido");	         
	    }
	    else{
	    	txtTitulo.setText(item);	         
	    }
	    return vi;
	}

}
