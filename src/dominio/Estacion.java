package dominio;

import tads.TADLista.ListaNodos;
import tads.TADCola.ColaNodos;

public class Estacion implements Comparable<Estacion> {
    private String nombre;
    private String barrio;
    private int capacidad; 
    private ListaNodos<Bicicleta> bicicletasAncladas;
private ColaNodos<Usuario> colaEsperaAlquiler;
private ColaNodos<Usuario> colaEsperaAnclaje;
    
public Estacion(String nombre, String barrio, int capacidad) {
    this.nombre = nombre;
    this.barrio = barrio;
    this.capacidad = capacidad;
    this.bicicletasAncladas = new ListaNodos<>();
    this.colaEsperaAlquiler = new ColaNodos<>();
    this.colaEsperaAnclaje = new ColaNodos<>();
}

    public String getNombre() { return nombre; }
    public String getBarrio() { return barrio; }
    public int getCapacidad() { return capacidad; }
    public ListaNodos<Bicicleta> getBicicletasAncladas() { return bicicletasAncladas; }
public ColaNodos<Usuario> getColaEsperaAlquiler() { return colaEsperaAlquiler; }
public ColaNodos<Usuario> getColaEsperaAnclaje() { return colaEsperaAnclaje; }
    
    public int getBicicletasDisponibles() {
        return bicicletasAncladas.cantElementos();
    }
    
    public int getAnclajesLibres() {
        return capacidad - bicicletasAncladas.cantElementos();
    }
    
    public boolean tieneEspacioLibre() {
        return getAnclajesLibres() > 0;
    }
    
    public boolean tieneBicicletasDisponibles() {
        return getBicicletasDisponibles() > 0;
    }
    
    public boolean tieneUsuariosEsperando() {
        return !colaEsperaAlquiler.esVacia() || !colaEsperaAnclaje.esVacia();
    }
    
    public double getPorcentajeOcupacion() {
        return (double) getBicicletasDisponibles() / capacidad * 100;
    }
    
    @Override
    public int compareTo(Estacion otra) {
        return this.nombre.compareTo(otra.nombre);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Estacion) {
            return this.nombre.equals(((Estacion) obj).nombre);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + barrio + ")";
    }
}
