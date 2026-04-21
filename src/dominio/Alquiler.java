package dominio;

public class Alquiler implements Comparable<Alquiler> {
    private String codigoBicicleta;
    private String cedulaUsuario;
    private String estacionOrigen;
    private String estacionDestino;
    
    public Alquiler(String codigoBicicleta, String cedulaUsuario, String estacionOrigen) {
        this.codigoBicicleta = codigoBicicleta;
        this.cedulaUsuario = cedulaUsuario;
        this.estacionOrigen = estacionOrigen;
        this.estacionDestino = null;
    }
    
    // Getters
    public String getCodigoBicicleta() {
        return codigoBicicleta;
    }
    
    public String getCedulaUsuario() {
        return cedulaUsuario;
    }
    
    public String getEstacionOrigen() {
        return estacionOrigen;
    }
    
    public String getEstacionDestino() {
        return estacionDestino;
    }
    
    // Setter
    public void setEstacionDestino(String estacionDestino) {
        this.estacionDestino = estacionDestino;
    }
    
    public boolean estaActivo() {
        return estacionDestino == null;
    }
    
    @Override
    public String toString() {
        return codigoBicicleta + "#" + cedulaUsuario + "#" + estacionOrigen;
    }
    
    @Override
    public int compareTo(Alquiler otro) {
        return this.codigoBicicleta.compareTo(otro.codigoBicicleta);
    }
}