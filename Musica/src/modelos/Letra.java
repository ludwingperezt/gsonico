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
		if (linea.length()>9){
			int index=linea.indexOf("]");
			String t=linea.substring(1, index);
			long a,b,c;
			a=Long.parseLong(t.substring(0,2))*60000;
			b=Long.parseLong(t.substring(3,5))*1000;
			c=Long.parseLong(t.substring(6,index-1))*10;
			tiempo.add(a+b+c);
			texto.add(linea.substring(index+1)+"\n");		
		}
		else
			texto.add(linea+"\n");			
	}
	
	public String getTodaLetra()
	{
		Object[] todoTexto=texto.toArray();
		String Acum="";
		for (int i=0; i<todoTexto.length;i++)
			Acum+=todoTexto[i].toString();
		return Acum;
	}
	public String getProximaLinea(float TiempoActual) {
		// TODO Auto-generated method stub
		String retorno=null;
		Object[] todoTiempos=tiempo.toArray();
		for(int i=1; i<todoTiempos.length;i++)
		{
			float i0=Float.parseFloat(todoTiempos[i-1].toString());
			float i1=Float.parseFloat(todoTiempos[i].toString());
			if (TiempoActual>=i0)
					if(TiempoActual<i1)
					{
						retorno=texto.get(i-1);
						break;
					}
		}
		return retorno;
	}

}
