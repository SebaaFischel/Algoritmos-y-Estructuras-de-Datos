package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_08AsignarBicicletaAEstacion {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarEstacion("Estacion1", "Centro", 5);
        s.registrarEstacion("Estacion2", "Pocitos", 3);
    }
    
    // Asignar bicicleta desde depósito
    @Test
    public void testAsignarBicicletaDesdeDepositoOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        
        retorno = s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    // Mover bicicleta de una estación a otra
    @Test
    public void testAsignarBicicletaDesdeOtraEstacionOk() {
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        retorno = s.asignarBicicletaAEstacion("BIC002", "Estacion2");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    // Asignar bicicleta a la misma estación donde ya está
    @Test
    public void testAsignarBicicletaYaEnLaMismaEstacionOk() {
        s.registrarBicicleta("BIC003", "URBANA");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        retorno = s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    // Error 1: parámetros null
    @Test
    public void testAsignarBicicletaError1_ParametrosNull() {
        retorno = s.asignarBicicletaAEstacion(null, "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.asignarBicicletaAEstacion("BIC001", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 1: parámetros vacíos
    @Test
    public void testAsignarBicicletaError1_ParametrosVacios() {
        retorno = s.asignarBicicletaAEstacion("", "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.asignarBicicletaAEstacion("BIC001", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 2: bicicleta no existe
    @Test
    public void testAsignarBicicletaError2_BiciNoExiste() {
        retorno = s.asignarBicicletaAEstacion("BICI99", "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    // Error 2: bicicleta en mantenimiento
    @Test
    public void testAsignarBicicletaError2_BiciEnMantenimiento() {
        s.registrarBicicleta("BIC004", "URBANA");
        s.marcarEnMantenimiento("BIC004", "Frenos rotos");
        
        retorno = s.asignarBicicletaAEstacion("BIC004", "Estacion1");
        
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    // Error 2: bicicleta alquilada
    @Test
    public void testAsignarBicicletaError2_BiciAlquilada() {
        s.registrarBicicleta("BIC005", "URBANA");
        s.asignarBicicletaAEstacion("BIC005", "Estacion1");
        s.registrarUsuario("12345678", "Usuario1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.asignarBicicletaAEstacion("BIC005", "Estacion2");
        
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    // Error 3: estación no existe
    @Test
    public void testAsignarBicicletaError3_EstacionNoExiste() {
        s.registrarBicicleta("BIC006", "URBANA");
        
        retorno = s.asignarBicicletaAEstacion("BIC006", "EstacionInexistente");
        
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    // Error 4: estación sin espacio disponible
    @Test
    public void testAsignarBicicletaError4_EstacionLlena() {
        s.registrarEstacion("EstacionPequena", "Centro", 2);
        s.registrarBicicleta("BIC007", "URBANA");
        s.registrarBicicleta("BIC008", "MOUNTAIN");
        s.registrarBicicleta("BIC009", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC007", "EstacionPequena");
        s.asignarBicicletaAEstacion("BIC008", "EstacionPequena");
        
        retorno = s.asignarBicicletaAEstacion("BIC009", "EstacionPequena");
        
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }
}