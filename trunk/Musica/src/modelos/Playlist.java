package modelos;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Playlist {
	
	private int _id;
	private String nombre;
	private GregorianCalendar fechaCreacion;
	private boolean aleatoria;
	private boolean repetir;
	
	private ArrayList<Cancion> listaCanciones;
	
	public Playlist(){
		this.listaCanciones = new ArrayList<Cancion>();
	}
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isAleatoria() {
		return aleatoria;
	}
	public void setAleatoria(boolean aleatoria) {
		this.aleatoria = aleatoria;
	}
	public boolean isRepetir() {
		return repetir;
	}
	public void setRepetir(boolean repetir) {
		this.repetir = repetir;
	}
	public ArrayList<Cancion> getListaCanciones() {
		return listaCanciones;
	}
	public void setListaCanciones(ArrayList<Cancion> listaCanciones) {
		this.listaCanciones = listaCanciones;
	}

}
