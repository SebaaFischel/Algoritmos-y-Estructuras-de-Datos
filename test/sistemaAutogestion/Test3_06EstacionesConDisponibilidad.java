package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_06EstacionesConDisponibilidad {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarEstacion("Estacion1", "Centro", 10);
        s.registrarEstacion("Estacion2", "Pocitos", 5);
        s.registrarEstacion("Estacion3", "Cordón", 8);
    }
    
    // Error 1: n igual a cero
    @Test
    public void testEstacionesConDisponibilidadError1_NCero() {
        retorno = s.estacionesConDisponibilidad(0);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 1: n igual a uno
    @Test
    public void testEstacionesConDisponibilidadError1_NUno() {
        retorno = s.estacionesConDisponibilidad(1);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 1: n negativo
    @Test
    public void testEstacionesConDisponibilidadError1_NNegativo() {
        retorno = s.estacionesConDisponibilidad(-5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Ninguna estación cumple
    @Test
    public void testEstacionesConDisponibilidadCeroEstacionesOk() {
        retorno = s.estacionesConDisponibilidad(2);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero());
    }
    
    // Algunas estaciones cumplen
    @Test
    public void testEstacionesConDisponibilidadAlgunasEstacionesOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        s.registrarBicicleta("BIC004", "URBANA");
        s.registrarBicicleta("BIC005", "MOUNTAIN");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion2");
        s.asignarBicicletaAEstacion("BIC003", "Estacion2");
        s.asignarBicicletaAEstacion("BIC004", "Estacion2");
        s.asignarBicicletaAEstacion("BIC005", "Estacion3");
        
        retorno = s.estacionesConDisponibilidad(2);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorEntero());
    }
    
    // Todas las estaciones cumplen
    @Test
    public void testEstacionesConDisponibilidadTodasLasEstacionesOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        s.registrarBicicleta("BIC004", "URBANA");
        s.registrarBicicleta("BIC005", "MOUNTAIN");
        s.registrarBicicleta("BIC006", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        s.asignarBicicletaAEstacion("BIC004", "Estacion2");
        s.asignarBicicletaAEstacion("BIC005", "Estacion2");
        s.asignarBicicletaAEstacion("BIC006", "Estacion2");
        
        retorno = s.estacionesConDisponibilidad(2);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(2, retorno.getValorEntero());
    }
    
    // n=2 es el mínimo válido
    @Test
    public void testEstacionesConDisponibilidadN2MinimoCasoValido() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        retorno = s.estacionesConDisponibilidad(2);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorEntero());
    }
    
    // Sistema sin estaciones
    @Test
    public void testEstacionesConDisponibilidadSinEstaciones() {
        IObligatorio sistemaVacio = new Sistema();
        sistemaVacio.crearSistemaDeGestion();
        
        retorno = sistemaVacio.estacionesConDisponibilidad(2);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero());
    }
}