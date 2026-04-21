package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_11DeshacerUltimosRetiros {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarEstacion("Estacion1", "Centro", 5);
        s.registrarEstacion("Estacion2", "Pocitos", 3);
        s.registrarUsuario("12345678", "Usuario1");
        s.registrarUsuario("87654321", "Usuario2");
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
    }
    
    // Error 1: n igual a cero
    @Test
    public void testDeshacerUltimosRetirosError1_NCero() {
        retorno = s.deshacerUltimosRetiros(0);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 1: n negativo
    @Test
    public void testDeshacerUltimosRetirosError1_NNegativo() {
        retorno = s.deshacerUltimosRetiros(-5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Deshacer un alquiler
    @Test
    public void testDeshacerUnAlquilerOk() {
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.deshacerUltimosRetiros(1);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("BIC001#12345678#Estacion1", retorno.getValorString());
        
        Retorno bicisEstacion1 = s.listarBicicletasDeEstacion("Estacion1");
        assertEquals("BIC001", bicisEstacion1.getValorString());
    }
    
    // Deshacer dos alquileres
    @Test
    public void testDeshacerDosAlquileresOk() {
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        
        retorno = s.deshacerUltimosRetiros(2);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        String resultado = retorno.getValorString();
        assertTrue(resultado.contains("BIC002#87654321#Estacion1"));
        assertTrue(resultado.contains("BIC001#12345678#Estacion1"));
    }
    
    // Deshacer más alquileres de los que existen
    @Test
    public void testDeshacerMasQueAlquileresExistentes() {
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.deshacerUltimosRetiros(5);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("BIC001#12345678#Estacion1", retorno.getValorString());
    }
    
    // Deshacer solo alquileres activos (ignora finalizados)
    @Test
    public void testDeshacerSoloAlquileresActivos_NoFinalizados() {
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        
        s.alquilarBicicleta("87654321", "Estacion1");
        s.devolverBicicleta("87654321", "Estacion2");
        
        s.asignarBicicletaAEstacion("BIC002", "Estacion2");
        s.alquilarBicicleta("87654321", "Estacion2");
        
        retorno = s.deshacerUltimosRetiros(2);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        String resultado = retorno.getValorString();
        assertTrue(resultado.contains("BIC002#87654321#Estacion2"));
        assertTrue(resultado.contains("BIC001#12345678#Estacion1"));
    }
    
    // Deshacer con estación origen llena: usuario queda en cola
    @Test
    public void testDeshacerConEstacionOrigenLlena_UsuarioEnColaAnclaje() {
        s.registrarEstacion("EstacionPequena", "Centro", 1);
        s.asignarBicicletaAEstacion("BIC001", "EstacionPequena");
        s.alquilarBicicleta("12345678", "EstacionPequena");
        
        s.registrarBicicleta("BIC003", "ELECTRICA");
        s.asignarBicicletaAEstacion("BIC003", "EstacionPequena");
        
        retorno = s.deshacerUltimosRetiros(1);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("BIC001#12345678#EstacionPequena", retorno.getValorString());
        
        Retorno bicisEstacion = s.listarBicicletasDeEstacion("EstacionPequena");
        assertEquals("BIC003", bicisEstacion.getValorString());
        
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        Retorno bicisEstacionDespues = s.listarBicicletasDeEstacion("EstacionPequena");
        assertEquals("BIC001", bicisEstacionDespues.getValorString());
    }
    
    // Deshacer sin alquileres activos
    @Test
    public void testDeshacerSinAlquileresActivos() {
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        s.devolverBicicleta("12345678", "Estacion2");
        
        retorno = s.deshacerUltimosRetiros(1);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Deshacer con cola de anclaje: usuario mantiene bici
    @Test
    public void testDeshacerConColaAnclaje_UsuarioMantieneBici() {
        s.registrarEstacion("EstacionMini", "Centro", 1);
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "EstacionMini");
        s.alquilarBicicleta("12345678", "EstacionMini");
        
        s.asignarBicicletaAEstacion("BIC003", "EstacionMini");
        
        retorno = s.deshacerUltimosRetiros(1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        s.registrarBicicleta("BIC004", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC004", "Estacion2");
        s.alquilarBicicleta("12345678", "Estacion2");
        
        Retorno bicisEstacion2 = s.listarBicicletasDeEstacion("Estacion2");
        assertEquals("BIC004", bicisEstacion2.getValorString());
    }
    
    // Deshacer con múltiples usuarios en cola de anclaje
    @Test
    public void testDeshacerMultiplesUsuariosEnColaAnclaje() {
        s.registrarEstacion("EstacionDoble", "Centro", 2);
        s.registrarBicicleta("BIC003", "ELECTRICA");
        s.registrarBicicleta("BIC004", "MOUNTAIN");
        
        s.asignarBicicletaAEstacion("BIC001", "EstacionDoble");
        s.alquilarBicicleta("12345678", "EstacionDoble");
        
        s.asignarBicicletaAEstacion("BIC002", "EstacionDoble");
        s.alquilarBicicleta("87654321", "EstacionDoble");
        
        s.asignarBicicletaAEstacion("BIC003", "EstacionDoble");
        s.asignarBicicletaAEstacion("BIC004", "EstacionDoble");
        
        retorno = s.deshacerUltimosRetiros(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        s.asignarBicicletaAEstacion("BIC003", "Estacion2");
        
        Retorno bicis = s.listarBicicletasDeEstacion("EstacionDoble");
        assertTrue(bicis.getValorString().contains("BIC002"));
        assertFalse(bicis.getValorString().contains("BIC001"));
    }
}