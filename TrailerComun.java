package empresa;

public class TrailerComun extends Transport{

	private double segCarga;
	public TrailerComun(double cargaMax, double capacidadMax, boolean frigorifico, double costoKM, double segCarga) {
		
		super(cargaMax, capacidadMax, frigorifico, costoKM);
		//Comprueba que se cumpla el IREP
		if(segCarga<0) {
			throw new RuntimeException("El seguro de carga no puede ser negativo");
		}
		
		this.segCarga=segCarga;
	}
	
	@Override
	public void asignarDestino(String destino, double distancia) {
		//Comprueba que se cumpla el IREP y se asigna el destino
		if(distancia>500) {
			throw new RuntimeException("No se puede realizar un viaje de mas de 500 KM");
		}else {
			super.asignarDestino(destino, distancia);
		}
		
	}
	@Override
	public double obtenerCosto(){ //El obtenerCosto pide el costo total a la clase padre y se le suma el extra
		return super.obtenerCosto()+segCarga;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this.getClass()==obj.getClass()) {
			return super.equals(obj);
		}else {
			return false;
		}
	
	}
	
	
}
