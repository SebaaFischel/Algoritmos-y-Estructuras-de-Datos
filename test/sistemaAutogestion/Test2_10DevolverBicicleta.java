package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_10DevolverBicicleta {
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
    }
    
    // Devolver bicicleta con espacio y sin usuarios esperando
    @Test
    public void testDevolverBicicletaConEspacioYSinEsperaOk() {
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.devolverBicicleta("12345678", "Estacion2");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        Retorno bicisEstacion2 = s.listarBicicletasDeEstacion("Estacion2");
        assertEquals("BIC001", bicisEstacion2.getValorString());
    }
    
    // Devolver con espacio y usuario esperando: entrega directa
    @Test
    public void testDevolverBicicletaConEspacioYUsuarioEsperandoAlquiler() {
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion2");
        
        retorno = s.devolverBicicleta("12345678", "Estacion2");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        Retorno bicisEstacion2 = s.listarBicicletasDeEstacion("Estacion2");
        assertEquals("", bicisEstacion2.getValorString());
        
        Retorno usuariosEspera = s.usuariosEnEspera("Estacion2");
        assertEquals("", usuariosEspera.getValorString());
    }
    
    // Devolver sin espacio: usuario queda en cola de anclaje
    @Test
    public void testDevolverBicicletaSinEspacio_UsuarioQuedaEnColaAnclaje() {
        s.registrarEstacion("EstacionPequena", "Centro", 1);
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC002", "EstacionPequena");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.devolverBicicleta("12345678", "EstacionPequena");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    // Error 1: parámetros null
    @Test
    public void testDevolverBicicletaError1_ParametrosNull() {
        retorno = s.devolverBicicleta(null, "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.devolverBicicleta("12345678", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 1: parámetros vacíos
    @Test
    public void testDevolverBicicletaError1_ParametrosVacios() {
        retorno = s.devolverBicicleta("", "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.devolverBicicleta("12345678", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 2: usuario no existe
    @Test
    public void testDevolverBicicletaError2_UsuarioNoExiste() {
        retorno = s.devolverBicicleta("99999999", "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    // Error 2: usuario sin bicicleta alquilada
    @Test
    public void testDevolverBicicletaError2_UsuarioNoTieneBici() {
        retorno = s.devolverBicicleta("12345678", "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    // Error 3: estación no existe
    @Test
    public void testDevolverBicicletaError3_EstacionNoExiste() {
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.devolverBicicleta("12345678", "EstacionInexistente");
        
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    // Varios usuarios esperando: se respeta orden FIFO
    @Test
    public void testDevolverBicicleta_VariosUsuariosEsperandoAlquiler_FIFO() {
        s.registrarUsuario("11111111", "Usuario3");
        s.alquilarBicicleta("87654321", "Estacion2");
        s.alquilarBicicleta("11111111", "Estacion2");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        s.devolverBicicleta("12345678", "Estacion2");
        
        Retorno usuariosEspera = s.usuariosEnEspera("Estacion2");
        assertEquals("11111111", usuariosEspera.getValorString());
    }
}