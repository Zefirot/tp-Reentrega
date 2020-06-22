package empresa;

public class Flete extends Transport {

	
	private int acomp;
	private double costoPorAcomp;
	
	public Flete(double cargaMax, double capacidadMax, double costoKM, int acomp, double costoPorAcomp) {
		
		super(cargaMax,capacidadMax,false,costoKM);
		//Comprueba que se cumpla el IREP
		if(acomp<0) {
			throw new RuntimeException("Los acompañantes no pueden estar en negativo");
		}
		if(costoPorAcomp<0) {
			throw new RuntimeException("El costo por acompañante no puede ser negativo");
		}
		
		this.acomp=acomp;
		this.costoPorAcomp=costoPorAcomp;
		
	}
	
	@Override
	public double obtenerCosto() {
		return super.obtenerCosto()+(acomp*costoPorAcomp); //El obtenerCosto pide el costo total a la clase padre y se le suma el extra
	}
	
	
}
