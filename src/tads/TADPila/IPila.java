package tads.TADPila;

public interface IPila<T> {
    public boolean esVacia();
    public void push(T n);
    public void pop();
    public T top();
    public T poptop();
    public void vaciar();
    public int cantElementos();
    public void mostrar();
}