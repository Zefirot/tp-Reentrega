package empresa;

public abstract class TransportAbs {

	public abstract void cargarPaquete(Paquet paquete);
	
	public abstract void asignarDestino(String destino, double distancia);
	
	public abstract void iniciarViaje();
	
	public abstract void finalizarViaje();
	
	public abstract boolean equals(Object obj);
	
	public abstract String toString();

	public abstract double obtenerCosto();
}
