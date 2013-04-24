package modelos;

import java.util.Queue;

public class Letra {
	private Queue<Long> tiempo;
	private Queue<String> texto;
	
	public long getProximoTiempo()
	{
		return tiempo.poll();
	}
	
	public String getProximaLinea()
	{
		return texto.poll();
	}
	//formato "[mm:ss.xx] LETRA"
	public void agregarLineaTexto(String linea)
	{
		int index=linea.indexOf("]");
		String t=linea.substring(1, index-1);
		long a,b,c;
		a=Integer.parseInt(t.substring(0,1))*60000;
		b=Integer.parseInt(t.substring(3,4))*1000;
		c=Integer.parseInt(t.substring(6,7))*100;
		tiempo.add(a+b+c);
		texto.add(linea.substring(index+1));
		
	}

}
