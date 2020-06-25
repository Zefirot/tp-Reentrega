package empresa;

import java.util.ArrayList;

public abstract class Transport implements TransportInter {
 
	private ArrayList<Paquet> paquetes; 
	private double cargaMax;
	private double auxCargaMax; //Estas nuevas variables guardan el valor con el que se inicializo la clase
	private double capacidadMax;
	private double auxCapacidadMax; //Estas nuevas variables guardan el valor con el que se inicializo la clase
	private boolean refrigeracion;
	private double costoKM;
	private String destino;
	private double distancia;
	private boolean enViaje;
	private double costoTonelada;
	private double costoTotal;

	public Transport(double carMax, double capacidadMax, boolean refrigeracion, double costoKM) {
		
		//Comprueba que se cumpla con el IREP
		if(carMax<=0 || capacidadMax <=0) {
			throw new RuntimeException("El peso o el volumen maximo del transporte no puede ser menor igual a cero");
		}
		if(costoKM<=0) {
			throw new RuntimeException("El costo por kilometro no puede ser menor igual a cero");
		}
		
		this.destino="''";
		this.cargaMax=carMax;
		this.auxCargaMax=carMax;
		this.capacidadMax=capacidadMax;
		this.auxCapacidadMax=capacidadMax;
		this.refrigeracion=refrigeracion;
		this.paquetes=new ArrayList<Paquet>();
		this.enViaje=false;
		this.costoKM=costoKM;
		this.costoTotal=0;
		this.costoTonelada=0;
		
	}
	
	public void cargarPaquete(Paquet paquete) { //Modifica los valores de cargaMax y capacidadMax.
		paquetes.add(paquete);
		cargaMax -= paquete.getPeso();
		capacidadMax -= paquete.getVolumen();
	}
	
	public void asignarDestino(String destino, double distancia){
		//Comprueba que se cumpla el IREP
		if(destino.equals("")) {
			throw new RuntimeException("No se puede asignar un destino vacio al transporte");
		}
		if(distancia<0) {
			throw new RuntimeException("La distancia asignada no puede ser negativa");
		}
		this.destino=destino;
		this.distancia=distancia;
		this.costoTotal=(distancia*costoKM);
	}
	

	public void iniciarViaje() {
		if(this.getDestino().equals("")) {
			throw new RuntimeException("No se puede iniciar el viaje sin tener un destino asignado");
		}else {
			this.enViaje=true;
		}
	}
	
	public void finalizarViaje() { //Se reinician todas las variables a como empezo el transporte
		this.destino="''";
		this.costoTotal=0;
		this.paquetes=new ArrayList<Paquet>();
		this.capacidadMax=auxCapacidadMax;
		this.cargaMax=auxCargaMax;
		this.enViaje=false;
	}
	
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		Transport other = (Transport) obj;
		
		return paquetes.size()==other.paquetes.size() 
				&& this.getDestino().equals(other.getDestino())
				&& this.mismosPaquetes(other.paquetes);
		
	}
	
	private boolean mismosPaquetes(ArrayList<Paquet> paquetes) { //O(n^2)
		
		for(Paquet paquete1 : this.paquetes) { //Se recorre el primer array de paquetes --O(n)
			
			boolean sonIguales=false;
			
			for(Paquet paquete2 : paquetes) {  //Se recorre el array de paquetes que se le pasa como paramtro --O(n)
				
				sonIguales = sonIguales || paquete1.equals(paquete2);
				
			}
			
			if(sonIguales==false) {  //Si sonIguales no encuentre algun paquete igual significa que ya de por si existe un paquete diferente y los transportes son diferentes.
				return false;
			}
			
		}
		
		return true;	
		
	}
	
	@Override
	public String toString() {
		
		StringBuilder cadena= new StringBuilder();
		
		cadena.append("CargaMax:"+String.valueOf(auxCargaMax)+" ")
		.append("Carga-Actual:"+String.valueOf(cargaMax)+" ")
		.append("CapacidadMax:"+String.valueOf(auxCapacidadMax)+" ")
		.append("Capacidad-Actual:"+String.valueOf(capacidadMax)+" ")
		.append("Refrigeracion:"+refrigeracion+" ")
		.append("CostoKM:"+String.valueOf(costoKM)+" ")
		.append("Destino:"+destino+" ")
		.append("Esta-en-viaje:"+enViaje+" ");
		
		if(tieneMercaderia()) {
			cadena.append("\nLos paquetes que contiene son:\n\n");
			for(Paquet paquete : paquetes) {
				cadena.append("   "+paquete.toString()+"\n");
			}
			cadena.append("\n");
		}else {
			cadena.append("\nEste Transporte no posee paquetes\n\n");
		}
		
		
		return cadena.toString();
	
	}
	
	
	//Gets
	public double obtenerCosto() {
		return costoTotal+costoTonelada;  //Si es 0 costoTonelada entonces no influye en nada
	}
	
	public String getDestino(){
		return destino;
	}
	public double getCapacidad() {
		return capacidadMax;
	}
	public double getCarga() {
		return cargaMax;
	}
	public boolean getRefrigeracion() {
		return refrigeracion;
	}
	public boolean getEstaEnViaje() {
		return enViaje;
	}
	public boolean tieneMercaderia() {
		return paquetes.size()!=0;
	}
	public void costoTonelada(double costoTonelada) {
		this.costoTonelada+=costoTonelada;
	}
	
}
