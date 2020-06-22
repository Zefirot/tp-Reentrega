package empresa;

public class MegaTrailer extends Transport {

	
	private double segCarga;
	private double costoComida;
	private double costoFijo;
	
	public MegaTrailer(double cargaMax, double capacidadMax, boolean frigorifico,  double costoKM, double segCarga, double costoFijo, double
			costoComida) {
		
		super(cargaMax, capacidadMax, frigorifico, costoKM);
		//Comprueba que se cumpla el IREP
		if(segCarga<0) {
			throw new RuntimeException("El seguro de carga no puede ser negativo");
		}
		if(costoComida<0) {
			throw new RuntimeException("El costo por la comida no puede ser negativo");
		}
		if(costoFijo<0) {
			throw new RuntimeException("El costo fijo no puede ser negativo");
		}
		
		this.segCarga=segCarga;
		this.costoComida=costoComida;
		this.costoFijo=costoFijo;
	}

	
	@Override
	public void asignarDestino(String destino, double distancia) {
		//Comprueba que se cumpla el IREP y se asigna el destino
		if(distancia<500) { 
			throw new RuntimeException("No se puede realizar un viaje de menos de 500 KM");
		}else {
			super.asignarDestino(destino, distancia);
		}
	}
	
	@Override
	public double obtenerCosto() {
		return super.obtenerCosto()+segCarga+costoComida+costoFijo; //El obtenerCosto pide el costo total a la clase padre y se le suma el extra
	}
	
}
