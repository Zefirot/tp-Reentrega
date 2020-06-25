package empresa;

public interface TransportInter {

	public void cargarPaquete(Paquet paquete);
	
	public void asignarDestino(String destino, double distancia);
	
	public void iniciarViaje();
	
	public void finalizarViaje();
	
	public boolean equals(Object obj);
	
	public String toString();

	public double obtenerCosto();
	
}
	
	
