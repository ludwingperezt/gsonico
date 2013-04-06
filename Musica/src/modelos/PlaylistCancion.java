package modelos;

public class PlaylistCancion {
	
	private int _id; 
	private int idCancion;
	private int idPlaylist; 
	private int idCancionActual;
	private int idPlaylistActual;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getIdCancion() {
		return idCancion;
	}
	public void setIdCancion(int idCancion) {
		this.idCancion = idCancion;
	}
	public int getIdPlaylist() {
		return idPlaylist;
	}
	public void setIdPlaylist(int idPlaylist) {
		this.idPlaylist = idPlaylist;
	}
	public int getIdCancionActual() {
		return idCancionActual;
	}
	public void setIdCancionActual(int idCancionActual) {
		this.idCancionActual = idCancionActual;
	}
	public int getIdPlaylistActual() {
		return idPlaylistActual;
	}
	public void setIdPlaylistActual(int idPlaylistActual) {
		this.idPlaylistActual = idPlaylistActual;
	}

}
