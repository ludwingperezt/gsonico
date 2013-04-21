package modelos;

import java.io.File;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.ID3Tag;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v1.ID3V1_1Tag;
import org.blinkenlights.jid3.v2.ID3V2_3_0Tag;

public class Metadatos {
    
    private String direccion = "";
    
    private String titulo = "";
    private String artista = "";
    private String album = "";
    private String comentario = "";
    private String genero = "";
    private String year = "";
    private int numeroPista = 0;
    
    /**
     * Funcion para leer las etiquetas ID3 de metadatos para MP3
     * @param direccion: ruta del archivo que se va a leer
     */
    public void leerEtiquetas(String direccion){
        try {            
            this.setDireccion(direccion);
            MediaFile archivo = new MP3File(new File(direccion));
            ID3Tag[] tags = archivo.getTags();
            for (ID3Tag o: tags){
                if (o instanceof ID3V1_1Tag){
                    leerEtiquetas_ID3_v1_1(o);
                }                
                else if (o instanceof ID3V1_0Tag){
                    leerEtiquetas_ID3_v1(o);
                }
                else if (o instanceof ID3V2_3_0Tag){
                    leerEtiquetas_ID3_v2_3(o);
                }
            }
            
        } catch (ID3Exception ex) {
            
        }
    }
    
    /**
     * Funcion para leer las etiquetas ID3 de metadatos para MP3, que devuelve un objeto Cancion
     * @param direccion: ruta del archivo que se va a leer
     * @return Cancion, objeto que representa una entidad cancion
     */
    public Cancion leerEtiquetasCancion(String direccion){
    	this.leerEtiquetas(direccion);
    	
    	Cancion cancion = new Cancion();
    	
    	cancion.setAlbum(this.getAlbum().replaceAll("'", ""));
    	cancion.setArchivoAudio(this.getDireccion());
    	cancion.setArtista(this.getArtista().replaceAll("'", ""));
    	cancion.setGenero(this.getGenero().replaceAll("'", ""));
    	cancion.setNumeroPista(Integer.toString(this.getNumeroPista()));
    	cancion.setTitulo(this.getTitulo().replaceAll("'", ""));
    	cancion.setYear(this.getYear().replaceAll("'", ""));
    	
    	return cancion;
    }
    
    private void leerEtiquetas_ID3_v2_3(ID3Tag t){
        ID3V2_3_0Tag etiqueta = (ID3V2_3_0Tag)t;
        
        if (etiqueta.getAlbum()!=null){
            this.setAlbum(etiqueta.getAlbum());
        }
        if (etiqueta.getArtist()!=null){
            this.setArtista(etiqueta.getArtist());
        }
        if (etiqueta.getComment()!=null){
            this.setComentario(etiqueta.getComment());
        }
        if (etiqueta.getTitle()!=null){
            this.setTitulo(etiqueta.getTitle());
        }
        if (etiqueta.getGenre()!=null){
            this.setGenero(etiqueta.getGenre());
        }
        
        try {
            int tn = etiqueta.getTrackNumber();
            this.setNumeroPista(tn);
        } catch (ID3Exception ex1) {
                      
        }
        
        try {
            int  yy = etiqueta.getYear();
            this.setYear(Integer.toString(yy));
        } catch (ID3Exception ex) {
            
            
        }
    }
    
    private void leerEtiquetas_ID3_v1(ID3Tag t){
        ID3V1_0Tag etiqueta = (ID3V1_0Tag)t;
        if (etiqueta.getAlbum()!=null){
            this.setAlbum(etiqueta.getAlbum());
        }
        if (etiqueta.getArtist()!=null){
            this.setArtista(etiqueta.getArtist());
        }
        if (etiqueta.getComment()!=null){
            this.setComentario(etiqueta.getComment());
        }
        if (etiqueta.getTitle()!=null){
            this.setTitulo(etiqueta.getTitle());
        }
        if (etiqueta.getYear()!=null){
            this.setYear(etiqueta.getYear());
        } 
    }
    
    private void leerEtiquetas_ID3_v1_1(ID3Tag o) {
        ID3V1_1Tag etiqueta = (ID3V1_1Tag)o;
        if (etiqueta.getAlbum()!=null){
            this.setAlbum(etiqueta.getAlbum());
        }
        if (etiqueta.getArtist()!=null){
            this.setArtista(etiqueta.getArtist());
        }
        if (etiqueta.getComment()!=null){
            this.setComentario(etiqueta.getComment());
        }
        if (etiqueta.getTitle()!=null){
            this.setTitulo(etiqueta.getTitle());
        }
        if (etiqueta.getYear()!=null){
            this.setYear(etiqueta.getYear());
        }
        this.setNumeroPista(etiqueta.getAlbumTrack());
    }
    
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
    public int getNumeroPista() {
        return numeroPista;
    }

    public void setNumeroPista(int numeroPista) {
        this.numeroPista = numeroPista;
    }
    
    public String toString(){
    	String total = this.getTitulo()+" "+this.getArtista()+" "+this.getAlbum();
    	return total;
    }
    
}
