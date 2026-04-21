package tads.TADLista;

public class ListaNodos<T extends Comparable> implements ILista<T> {

    private NodoLista<T> lista;
    private int cantidad;

    public ListaNodos() {
        lista = null;
        cantidad = 0;
    }

    @Override
    public boolean esVacia() {
        return lista == null;
    }

    @Override
    public void agregarInicio(T n) {
        NodoLista<T> nuevo = new NodoLista<>(n);
        nuevo.setSiguiente(lista);
        lista = nuevo;
        cantidad++;
    }

    @Override
    public void agregarFinal(T n) {
        if (esVacia()) {
            agregarInicio(n);
        } else {
            NodoLista<T> aux = lista;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            NodoLista<T> nuevo = new NodoLista<>(n);
            aux.setSiguiente(nuevo);
            cantidad++;
        }
    }

    @Override
    public void borrarInicio() {
        if (!esVacia()) {
            lista = lista.getSiguiente();
            cantidad--;
        }
    }

    @Override
    public void borrarFin() {
        if (!esVacia()) {
            if (lista.getSiguiente() == null) {
                vaciar();
            } else {
                NodoLista<T> aux = lista;
                while (aux.getSiguiente().getSiguiente() != null) {
                    aux = aux.getSiguiente();
                }
                aux.setSiguiente(null);
                cantidad--;
            }
        }
    }

    @Override
    public void vaciar() {
        lista = null;
        cantidad = 0;
    }

    @Override
    public String mostrar() {
        if (esVacia()) return "";
        StringBuilder sb = new StringBuilder();
        NodoLista<T> aux = lista;
        int contador = 0;
        while (aux != null) {
            sb.append(aux.getDato().toString());
            contador++;
            if (contador < cantidad) sb.append("|");
            aux = aux.getSiguiente();
        }
        return sb.toString();
    }

    @Override
    public void agregarOrd(T n) {
        if (lista == null || n.compareTo(lista.getDato()) <= 0) {
            agregarInicio(n);
        } else {
            NodoLista<T> aux = lista;
            while (aux.getSiguiente() != null && aux.getSiguiente().getDato().compareTo(n) < 0) {
                aux = aux.getSiguiente();
            }
            NodoLista<T> nuevo = new NodoLista<>(n);
            nuevo.setSiguiente(aux.getSiguiente());
            aux.setSiguiente(nuevo);
            cantidad++;
        }
    }

    @Override
    public void borrarElemento(T n) {
        if (lista != null) {
            if (lista.getDato().equals(n)) {
                borrarInicio();
            } else {
                NodoLista<T> aux = lista;
                while (aux.getSiguiente() != null && !aux.getSiguiente().getDato().equals(n)) {
                    aux = aux.getSiguiente();
                }
                if (aux.getSiguiente() != null) {
                    aux.setSiguiente(aux.getSiguiente().getSiguiente());
                    cantidad--;
                }
            }
        }
    }

    @Override
    public int cantElementos() {
        return cantidad;
    }

    @Override
    public NodoLista<T> obtenerElemento(T n) {
        NodoLista<T> ret = lista;
        while (ret != null) {
            if (ret.getDato().equals(n)) return ret;
            ret = ret.getSiguiente();
        }
        return null;
    }

    @Override
    public void mostrarREC(NodoLista l) {
        if (l != null) {
            mostrarREC(l.getSiguiente());
            System.out.println(l.getDato());
        }
    }

    public boolean contiene(T dato) {
        NodoLista<T> aux = lista;
        while (aux != null) {
            if (aux.getDato().equals(dato)) return true;
            aux = aux.getSiguiente();
        }
        return false;
    }
    public NodoLista<T> getLista() {
        return this.lista;
    }
}