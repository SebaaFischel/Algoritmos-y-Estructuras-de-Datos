package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_07EliminarEstacion {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    // Camino feliz: eliminar estación vacía
    @Test
    public void testEliminarEstacionOk() {
        s.registrarEstacion("EstacionVacia", "Centro", 10);
        
        retorno = s.eliminarEstacion("EstacionVacia");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    // Error 1: parámetro null
    @Test
    public void testEliminarEstacionError1_Null() {
        retorno = s.eliminarEstacion(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 1: parámetro vacío
    @Test
    public void testEliminarEstacionError1_Vacio() {
        retorno = s.eliminarEstacion("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.eliminarEstacion("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 2: estación no existe
    @Test
    public void testEliminarEstacionError2_NoExiste() {
        retorno = s.eliminarEstacion("EstacionInexistente");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    // Error 3: estación con bicicletas ancladas
    @Test
    public void testEliminarEstacionError3_ConBicicletas() {
        s.registrarEstacion("Estacion1", "Pocitos", 5);
        s.registrarBicicleta("BIC001", "URBANA");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        
        retorno = s.eliminarEstacion("Estacion1");
        
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    // Error 3: estación con usuarios en cola de espera
    @Test
    public void testEliminarEstacionError3_ConColaAlquiler() {
        s.registrarEstacion("Estacion2", "Centro", 5);
        s.registrarUsuario("12345678", "Usuario1");
        s.alquilarBicicleta("12345678", "Estacion2");
        
        retorno = s.eliminarEstacion("Estacion2");
        
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}