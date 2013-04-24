package com.example.musica;
import java.util.ArrayList;

import modelos.Cancion;
import modelos.Playlist;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ItemPlayListAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Playlist> items;
	
	public ItemPlayListAdapter(Activity activity, ArrayList<Playlist> items) {
	    this.activity = activity;
	    this.items = items;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
