package empresa;

public class Paquet {

	private float peso;
	private double volumen;
	private boolean refrigeracion;
	private String destino;
	private StringBuilder cadena;

	public Paquet( String destino,float peso, double volumen, boolean refrigeracion) {
		//Comprueba que se cumpla el IREP
		if(destino.equals("")) {
			throw new RuntimeException("El destino del paquete no puede estar vacio");
		}
		if(peso<0) {
			throw new RuntimeException("El peso del paquete no puede ser negativo");
		}
		if(volumen<0) {
			throw new RuntimeException("El volumen del paquete no puede ser negativo");
		}
		
		this.peso=peso;
		this.volumen=volumen;
		this.refrigeracion=refrigeracion;
		this.destino=destino;
		this.cadena=new StringBuilder();
		
		this.cadena.append("Destino:"+destino).append(" ")
		.append("Peso:"+String.valueOf(peso)).append(" ")
		.append("Volumen:"+String.valueOf(volumen)).append(" ")
		.append("Refrigeracion:"+refrigeracion).append(" ");
		
	}
	
	//Gets
	public float getPeso() {
		return peso;
	}
	public double getVolumen() {
		return volumen;
	}
	public boolean getRefrigeracion() {
		return refrigeracion;
	}
	public String getDestino() {
		return destino;
	}
	
	@Override
	public String toString() {
		return cadena.toString();
	}
	
}
