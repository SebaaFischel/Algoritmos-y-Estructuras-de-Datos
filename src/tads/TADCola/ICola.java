/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tads.TADCola;

public interface ICola<T> {
    boolean esVacia(); 
    boolean esLlena();
    void encolar(T dato);
    void desencolar();
    T frente();
    T fondo();
    int cantElementos();
    void vaciar();
}