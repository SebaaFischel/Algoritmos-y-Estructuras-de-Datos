package dominio;

public class Bicicleta implements Comparable<Bicicleta> {
    public enum Tipo {
        URBANA, MOUNTAIN, ELECTRICA
    }
    
    public enum Estado {
        DISPONIBLE, ALQUILADA, MANTENIMIENTO
    }
    
    private String codigo; 
    private Tipo tipo;
    private Estado estado;
    private Estacion estacionActual; 
    private String motivoMantenimiento; 
    private int vecesAlquilada;
    
    public Bicicleta(String codigo, Tipo tipo) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = Estado.DISPONIBLE;
        this.estacionActual = null; 
        this.vecesAlquilada = 0;
    }

    public String getCodigo() { return codigo; }
    public Tipo getTipo() { return tipo; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Estacion getEstacionActual() { return estacionActual; }
    public void setEstacionActual(Estacion estacion) { this.estacionActual = estacion; }
    public String getMotivoMantenimiento() { return motivoMantenimiento; }
    public void setMotivoMantenimiento(String motivo) { this.motivoMantenimiento = motivo; }
    public int getVecesAlquilada() { return vecesAlquilada; }
    public void incrementarVecesAlquilada() { this.vecesAlquilada++; }
    public void decrementarVecesAlquilada() { this.vecesAlquilada--; }
    
    public boolean estaDisponible() {
        return estado == Estado.DISPONIBLE;
    }
    
        public boolean estaEnMantenimiento() {
        return estado == Estado.MANTENIMIENTO;
    }
        
        public boolean estaAlquilada() {
        return estado == Estado.ALQUILADA;
    }
    
    public boolean estaEnDeposito() {
        return estacionActual == null;
    }
    
    @Override
    public int compareTo(Bicicleta otra) {
        return this.codigo.compareTo(otra.codigo);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bicicleta) {
            return this.codigo.equals(((Bicicleta) obj).codigo);
        }
        return false;
    }
    @Override
    public String toString() {
    String estadoStr;
    switch (estado) {
        case ALQUILADA:
            estadoStr = "Alquilada";
            break;
        case MANTENIMIENTO:
            estadoStr = "En Mantenimiento";
            break;
        default:
            estadoStr = "Disponible";
            break;
    }
    return codigo + "#" + tipo + "#" + estadoStr;
}
   
}