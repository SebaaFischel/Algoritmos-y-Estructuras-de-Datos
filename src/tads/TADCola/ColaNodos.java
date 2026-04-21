
package tads.TADCola;

public class ColaNodos<T extends Comparable> implements ICola<T> {
    private NodoCola<T> frente;
    private NodoCola<T> fondo;
    private int cantNodos;
    private int cantMaxima; // -1 significa ilimitada

    // Constructor con capacidad ilimitada
    public ColaNodos() {
        this.frente = null;
        this.fondo = null;
        this.cantNodos = 0;
        this.cantMaxima = -1; // Ilimitada
    }

    // Constructor con capacidad limitada
    public ColaNodos(int cantMaxima) {
        this.frente = null;
        this.fondo = null;
        this.cantNodos = 0;
        this.cantMaxima = cantMaxima;
    }

    @Override
    public boolean esVacia() {
        return cantNodos == 0;
    }

    @Override
    public boolean esLlena() {
        if (cantMaxima == -1) {
            return false; // Cola ilimitada nunca está llena
        }
        return cantNodos == cantMaxima;
    }

    @Override
    public void encolar(T dato) {
        if (!esLlena()) {
            NodoCola<T> nuevo = new NodoCola<>(dato);
            
            if (esVacia()) {
                frente = nuevo;
                fondo = nuevo;
            } else {
                fondo.setSiguiente(nuevo);
                fondo = nuevo;
            }
            cantNodos++;
        }
    }

    @Override
    public void desencolar() {
        if (!esVacia()) {
            if (cantNodos == 1) {
                frente = null;
                fondo = null;
            } else {
                frente = frente.getSiguiente();
            }
            cantNodos--;
        }
    }

    @Override
    public T frente() {
        if (!esVacia()) {
            return frente.getDato();
        }
        return null;
    }

    @Override
    public T fondo() {
        if (!esVacia()) {
            return fondo.getDato();
        }
        return null;
    }

    @Override
    public int cantElementos() {
        return cantNodos;
    }

    @Override
    public void vaciar() {
        frente = null;
        fondo = null;
        cantNodos = 0;
    }
    
    // Método auxiliar para obtener el frente y desencolarlo (similar a poptop de Pila)
    public T dequeue() {
        T dato = frente();
        desencolar();
        return dato;
    }
    
    public boolean contiene(T dato) {
        NodoCola<T> actual = frente;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    
        public NodoCola<T> getFrente() {
        return frente;
    }
}