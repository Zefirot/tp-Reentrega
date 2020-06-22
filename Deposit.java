package empresa;

import java.util.ArrayList;
import java.util.Iterator;

public class Deposit {

	private ArrayList<Paquet> paquetes;
	private boolean refrigeracion;
	private double capacidadMax;
	private boolean tercerizado; //Si es true pertenece a la empresa y si es false es tercerizado.
	private double costoPorTonelada;
	
	
	public Deposit(double capacidadMax, boolean refrigeracion, boolean tercerizado, double costoPorTonelada){
		
		//Comprueba que se cumpla el IREP
		if(capacidadMax<=0) {
			throw new RuntimeException("La capacidad maxima no puede ser menor o igual a cero");
		}
		if(costoPorTonelada<0) {
			throw new RuntimeException("El costo por tonelada no puede ser negativo");
		}
		
		this.paquetes = new ArrayList<Paquet>();
		this.refrigeracion=refrigeracion;
		this.capacidadMax=capacidadMax;
		this.tercerizado=tercerizado;  
		this.costoPorTonelada=costoPorTonelada;
		
	}
	
	public void agregarPaquete(Paquet paquete) {
		paquetes.add(paquete);
	}
	
	public double cargarTransporte(Transport transporte) {
		
		double volumenTotal=0;
		
		Iterator<Paquet> it = paquetes.iterator();
		
		if(this.getRefrigeracion() && this.esTercerizado()) {
			double pesoTotal=0;
			
			
			while(it.hasNext()) { //Se recorre todo el array de paquetes
				
				Paquet paquete = it.next();
				
				if(paquete.getDestino().equals(transporte.getDestino())&& //Se comprueba que el paquete se puede almacenar en el transporte
						paquete.getPeso()<=transporte.getCarga() && 
						paquete.getVolumen()<=transporte.getCapacidad()) {
					
					transporte.cargarPaquete(paquete);
					volumenTotal+=paquete.getVolumen();
					pesoTotal+=paquete.getPeso();
					it.remove();
				}
				
			}
		
			transporte.costoTonelada(calcularCosto(pesoTotal)); //Se le suma un extra al transporte por el tipo de deposito(Tercerizado, frio)
			return volumenTotal;
		}
		
		
		while(it.hasNext()){ //Se recorre todo el array de paquetes
			
			Paquet paquete = (Paquet) it.next();
			
			if(paquete.getDestino().equals(transporte.getDestino()) &&  //Se comprueba que el paquete se puede almacenar en el transporte
					paquete.getPeso()<=transporte.getCarga() && 
					paquete.getVolumen()<=transporte.getCapacidad()) {
				
				transporte.cargarPaquete(paquete);
				volumenTotal+=paquete.getVolumen();
				it.remove();
			}
			
		}
		
		return volumenTotal;  
	}
	

	
	private double calcularCosto(double cargaTotal) {

		double costo=0;
		
		while(cargaTotal>=1000) {    //Por cada 1000kg cargados se le suma el costo por tonelada
			costo += costoPorTonelada;
			cargaTotal -= 1000;  
		}

		cargaTotal = (cargaTotal*100)/1000;  //Se saca el porcentaje de lo que queda de carga

		costo +=  (costoPorTonelada*cargaTotal)/100;  //Se realiza el calculo de cuanto porciento se le debe agregar al costo total


		return costo;

	}
	
	@Override
	public String toString() {
		StringBuilder cadena= new StringBuilder();
		cadena.append("Refrigeracion:"+refrigeracion+" ")
		.append("CapacidadMax:"+String.valueOf(capacidadMax)+" ")
		.append("Tercerizado:"+tercerizado+" ")
		.append("Costo-Por-Tonelada:"+String.valueOf(costoPorTonelada)+" ");
		
		if(tienePaquetes()) {
			cadena.append("\nLos paquetes que contiene son:\n\n");
			for(Paquet paquete : paquetes) {
				cadena.append("   "+paquete.toString()+"\n\n");
			}
		}else {
			cadena.append("\nEste deposito no contiene paquetes\n");
		}
		
		
		return cadena.toString();
		
	}
	
	//Gets
	public boolean getRefrigeracion() {
		return refrigeracion;
	}
	public boolean esTercerizado(){
		return tercerizado;
	}
	public double getCapacidadMax() {
		return capacidadMax;
	}
	public double costoPorTonelada() {
		return costoPorTonelada;
	}
	public boolean tienePaquetes() {
		return paquetes.size()!=0? true:false;
	}
	
}
