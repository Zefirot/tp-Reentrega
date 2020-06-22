package empresa;


import java.util.ArrayList;
import java.util.HashMap;

public class Empresa {

	private String CUIT;
	private String nombre;
	private HashMap<String,Double> destinos;
	private HashMap<String,Transport> transportes;
	private ArrayList<Deposit> depositos;
	private int cantDepositos;
	
	
	public Empresa(String CUIT, String nombre) {
		
		//Se controla el IREP
		if(CUIT.equals("")) {
			throw new RuntimeException("El CUIT de la empresa no puede estar vacio");
		}
		if(nombre.equals("")) {
			throw new RuntimeException("El nombre de la empresa no puede estar vacio");
		}
		
		this.CUIT=CUIT;
		this.nombre=nombre;
		this.destinos=new HashMap<String,Double>();
		this.transportes = new HashMap<String,Transport>();
		this.depositos = new ArrayList<Deposit>();
		this.cantDepositos=0;
		
	}
	
	
	public int agregarDeposito(double capacidad, boolean refrigeracion, boolean posesion) {
		
		if(refrigeracion==true && posesion==false) {  //Se controla que se este usando la funcion correcta
			throw new RuntimeException("El deposito ingresado no puede ser tercerizado frio");
		}
		Deposit nuevo = new Deposit(capacidad,refrigeracion,posesion,0);  //Se ingresa 0 porque no tiene coste por tonelada
		
		depositos.add(nuevo);
		
		cantDepositos += 1;
		
		return cantDepositos;
	}
	
	
	public int agregarDepTercerizFrio(double capacidad, double costoPorTonelada) {
		
		Deposit nuevo = new Deposit(capacidad, true, false, costoPorTonelada); //Se ponen los valores true porque ya se sabe que es frio y tercerizado
		
		depositos.add(nuevo);
		
		cantDepositos += 1;
		
		return cantDepositos;
	}
	
	public boolean incorporarPaquete(String destino, float peso, double volumen, boolean refrigeracion) {
	
		Paquet nuevo = new Paquet(destino,peso,volumen,refrigeracion);
	
		boolean almacenado=false;
		
		for(Deposit deposito : depositos) {
			
			if(deposito.getRefrigeracion()==nuevo.getRefrigeracion()) {
				deposito.agregarPaquete(nuevo);
				almacenado=true;
			}
			
		}
		
		return almacenado;  //Si no se encuentra ningun deposito que pueda almacenarlo entonces retorna false
		
	}
	
	public void agregarDestino(String destino, double distancia) {
		
		//Se controla el IREP
		if(destino.equals("")) {
			throw new RuntimeException("No se puede agregar un destino sin nombre");
		}
		if(distancia<0) {
			throw new RuntimeException("No se puede agregar un destino con distancia negativa");
		}
		
		destinos.put(destino, distancia);
	}
	

	public void asignarDestino(String idTrans, String destino) {
		
		if(!pertenece(idTrans)) {
			throw new RuntimeException("El id proporcionado no se encuentra registrado");
		}
		if(!existeDestino(destino)) {
			throw new RuntimeException("El destino proporcionado no se encuentra registrado");
		}
		
		for(String ids : transportes.keySet()) {
			
			if(ids.equals(idTrans)) {  //Primero se busca el transporte
				
				if(transportes.get(ids).tieneMercaderia()) {
					throw new RuntimeException("El transporte ya contiene mercaderia cargada");
				}
				
				
				for(String lugar: destinos.keySet()) { //Despues se busca el destino para asignarlo
					if(lugar.equals(destino)) {
						
						if(transportes.get(ids).getClass()==MegaTrailer.class) { //A continuacion se compara la clase para saber a que transporte se le asigna el destino
							
							MegaTrailer transporte = (MegaTrailer) transportes.get(ids);
							transporte.asignarDestino(destino, destinos.get(lugar));
					
						}
						
						if(transportes.get(ids).getClass()==TrailerComun.class) {
							
							TrailerComun transporte = (TrailerComun) transportes.get(ids);
							transporte.asignarDestino(destino, destinos.get(lugar));
							
							
						}
						
						transportes.get(ids).asignarDestino(destino, destinos.get(lugar));
						
					}
				}
				
			}
			
		}
		
		
	}
	
	private boolean existeDestino(String destino) { //Funcion que se utiliza para controlar que el destino ya esta agregado antes de asignar
		
		boolean existe = false;
		
		for(String lugar: destinos.keySet()) {
			existe = existe || lugar.toString().equals(destino);
		}
		return existe;
		
	}
	
	
	//Agregar Transportes
	public void agregarTrailer(String id, float cargaMax, float capacidadMax, boolean refrigeracion, int costoKM, int seguroDeCarga) {
		
		if(pertenece(id)) {
			throw new RuntimeException("El id proporcionado ya se encuentra registrado");
		}
		if(id.equals("")) {
			throw new RuntimeException("No se puede agregar un transporte sin id");
		}
		
		TrailerComun nuevo = new TrailerComun(cargaMax,capacidadMax,refrigeracion,costoKM,seguroDeCarga);
		transportes.put(id, nuevo);
		
	}
	public void agregarMegaTrailer(String id, double cargaMax, double capacidadMax, boolean refrigeracion, double costoKM, double seguroDeCarga, double costoFijo, double costoComida) {
		
		if(pertenece(id)) {
			throw new RuntimeException("El id proporcionado ya se encuentra registrado");
		}
		if(id.equals("")) {
			throw new RuntimeException("No se puede agregar un transporte sin id");
		}
		
		MegaTrailer nuevo = new MegaTrailer(cargaMax,capacidadMax, refrigeracion,costoKM,seguroDeCarga,costoFijo,costoComida);
		
		transportes.put(id, nuevo);
	}
	
	public void agregarFlete(String id, float cargaMax, float capacidadMax, int costoKM, int acomp, int costoFijo) {
		
		if(pertenece(id)) {
			throw new RuntimeException("El id proporcionado ya se encuentra registrado");
		}
		if(id.equals("")) {
			throw new RuntimeException("No se puede agregar un transporte sin id");
		}
		
		Flete nuevo = new Flete(cargaMax,capacidadMax,costoKM,acomp, costoFijo);
		
		transportes.put(id, nuevo);
	}
	//--------------------------------------------
	
	
	public double cargarTransporte(String id) { //Buscar Camion, buscar deposito y cargar hasta el maximo posible VOLUMEN
		//Se controla que el transporte exista dentro de la empresa
		if(!pertenece(id)) {
			throw new RuntimeException("El id proporcionado no se encuentra registrado");
		}
		
		double cargaTotal=0;
		for(String ids: transportes.keySet()) { //Primero, se busca el transporte
			if( ids.equals(id) ) {
				
				//Se chequea que el transporte cumpla con lo pedido
				if(transportes.get(ids).getEstaEnViaje()==true ) {
					throw new RuntimeException("El transporte ya se encuentra en viaje");
				}
				if(transportes.get(ids).getDestino()=="") {
					throw new RuntimeException("El transporte no tiene destino asignado");
				}
				
				//Segundo, se buscan los depositos para cargar el transporte
				for(Deposit deposito : depositos) {
					//Se chequea el tipo de deposito
					if(deposito.getRefrigeracion()==transportes.get(ids).getRefrigeracion()) {
						
						if(deposito.getRefrigeracion() && !deposito.noEsTercerizado()) {
							cargaTotal += deposito.cargarTransporteTercerizFrio(transportes.get(ids));
						}else {
							cargaTotal += deposito.cargarTransporte(transportes.get(ids)); //El deposito se encarga de cargar el transporte
						}
						
					}
				}
			}
		}
		
		return cargaTotal;
	}
	
	
	private boolean pertenece(String id) {  //Metodo que comprueba si existe el id que se le pasa como parametro
		
		boolean pertenece=false;
		
		for(String ids: transportes.keySet()) {
			pertenece = pertenece || ids.toString().equals(id);
		}
		
		return pertenece;
		
	}
	
	public double obtenerCostoViaje(String id) {
		
		if(!pertenece(id)) {
			throw new RuntimeException("El id proporcionado no se encuentra registrado");
		}
		
		double costoTotal=0;
		
		for(String ids : transportes.keySet()) { //Se busca primero el transporte
			
			if(ids.equals(id)) {
				//Luego se comparan las clases para saber el precio de ese transporte
				if(transportes.get(ids).getClass()==Flete.class) {
					
					Flete transporte = (Flete) transportes.get(ids);
					costoTotal = transporte.obtenerCosto();
				}
				
				if(transportes.get(ids).getClass()==MegaTrailer.class) {

					MegaTrailer transporte = (MegaTrailer) transportes.get(ids);
					costoTotal = transporte.obtenerCosto();
				}
				
				if(transportes.get(ids).getClass()==TrailerComun.class) {

					TrailerComun transporte = (TrailerComun) transportes.get(ids);
					costoTotal = transporte.obtenerCosto();
				}
				
			}
			
		}
		
		return costoTotal;
		
	}
	
	public void iniciarViaje(String id) {
		
		if(!pertenece(id)) {
			throw new RuntimeException("La id proporcionada no se encuentra registrada");
		}
		
		
		for(String ids : transportes.keySet()) { //Se busca el transporte
			
			if(ids.equals(id) && transportes.get(ids).tieneMercaderia()) {  //Se comprueba que el transporte cumpla con lo pedido
				
				transportes.get(ids).iniciarViaje();
				
			}else {
				throw new RuntimeException("El transporte solicitado no contiene mercaderia cargada");
			}
			
		}
		
		
	}
	
	public void finalizarViaje(String id) {
		
		if(!pertenece(id)) {
			throw new RuntimeException("La id proporcionada no se encuentra registrada");
		}
		
		
		for(String ids : transportes.keySet()) {
			
			if(ids.equals(id)) {
				
				transportes.get(ids).finalizarViaje();
				
			}
			
		}
		
		
	}
	
	public String obtenerTransporteIgual(String id) { //--La complejidad de dicho algoritmo es de O(n)
		
		Transport nuevo=null;
		
		
		for(String ids : transportes.keySet()) {//Primero se busca el transporte  --O(n)
			if(id.equals(ids)) {
				nuevo = transportes.get(ids);
			}
		}
		
		
		for(String ids : transportes.keySet()) {//Luego se buscan otros transportes que tengan otros id --O(n)
			
			if(!id.equals(ids) //--O(1)
					&& nuevo.getClass()==transportes.get(ids).getClass() 
					&& nuevo.equals(transportes.get(ids))) {
				return ids;
			}

		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder cadena= new StringBuilder();
		cadena.append("CUIT-de-la-empresa:"+CUIT+" ").append("Nombre-de-la-empresa:"+nombre+"\n");
		
		
		cadena.append("\nDestinos: \n\n");
		
		for(String destino : destinos.keySet()) {
			cadena.append("Destino:"+destino+" ").append("Distancia:"+String.valueOf(destinos.get(destino))+"\n");
		}
		
		cadena.append("\nDepositos: \n\n");
		for(Deposit depos : depositos) {
			cadena.append(depos.toString());
		}
		
		cadena.append("\nTransportes: \n\n");
		for(String ids : transportes.keySet()) {
			cadena.append("Id:"+ids+" ").append(transportes.get(ids).toString());
		}
		
		
		return cadena.toString();
	}
	
	
	
}
