package modelos;

public class Cancion {
	
	private int _id;
	private String titulo;
	private String artista; 
	private String album;
	private String genero;
	private String year;
	private String numeroPista;
	private String archivoAudio;
	private String archivoLetra;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getArtista() {
		return artista;
	}
	public void setArtista(String artista) {
		this.artista = artista;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getNumeroPista() {
		return numeroPista;
	}
	public void setNumeroPista(String numeroPista) {
		this.numeroPista = numeroPista;
	}
	public String getArchivoAudio() {
		return archivoAudio;
	}
	public void setArchivoAudio(String archivoAudio) {
		this.archivoAudio = archivoAudio;
	}
	public String getArchivoLetra() {
		return archivoLetra;
	}
	public void setArchivoLetra(String archivoLetra) {
		this.archivoLetra = archivoLetra;
	}
	public String toString(){
    	String total = this.getTitulo()+" "+this.getArtista()+" "+this.getAlbum();
    	return total;
    }
}
