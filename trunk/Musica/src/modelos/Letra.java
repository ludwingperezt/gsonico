package modelos;

import java.util.LinkedList;

public class Letra {
	private LinkedList<Long> tiempo;
	private LinkedList<String> texto;
	public Letra()
	{
		tiempo=new LinkedList<Long>();
		texto=new LinkedList<String>();
	}
	public long getProximoTiempo()
	{
		return tiempo.getFirst();
	}
	
	public String getProximaLinea()
	{
		return texto.getFirst();
	}
	//formato "[mm:ss.xx] LETRA"
	public void agregarLineaTexto(String linea)
	{
		int index=linea.indexOf("]");
		String t=linea.substring(1, index);
		long a,b,c;
		a=Long.parseLong(t.substring(0,1))*60000;
		b=Long.parseLong(t.substring(3,4))*1000;
		c=Long.parseLong(t.substring(6,7))*10;
		tiempo.add(a+b+c);
		texto.add(linea.substring(index+1)+"\n");		
	}
	
	public String getTodaLetra()
	{
		Object[] todoTexto=texto.toArray();
		String Acum="";
		for (int i=0; i<todoTexto.length;i++)
			Acum+=todoTexto[i].toString();
		return Acum;
	}

}
