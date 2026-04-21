package dominio;

public class Usuario implements Comparable<Usuario> {
    private String cedula; 
    private String nombre;
    private Bicicleta bicicletaAlquilada; 
    private int cantidadAlquileres;
    
    public Usuario(String cedula, String nombre) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.bicicletaAlquilada = null;
        this.cantidadAlquileres = 0;
    }
    
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public Bicicleta getBicicletaAlquilada() { return bicicletaAlquilada; }
    public void setBicicletaAlquilada(Bicicleta bicicleta) { this.bicicletaAlquilada = bicicleta; }
    public int getCantidadAlquileres() { return cantidadAlquileres; }
    public void incrementarAlquileres() { this.cantidadAlquileres++; }
    public void decrementarAlquileres() { this.cantidadAlquileres--; }
    
    public boolean tieneAlquiler() {
        return bicicletaAlquilada != null;
    }
    
    @Override
    public int compareTo(Usuario otro) {
        return this.nombre.compareTo(otro.nombre);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Usuario) {
            return this.cedula.equals(((Usuario) obj).cedula);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return nombre + "#" + cedula;
    }
}