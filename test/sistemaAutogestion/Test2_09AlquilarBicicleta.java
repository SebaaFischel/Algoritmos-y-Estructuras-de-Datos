package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_09AlquilarBicicleta {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarEstacion("Estacion1", "Centro", 5);
        s.registrarUsuario("12345678", "Usuario1");
        s.registrarUsuario("87654321", "Usuario2");
    }
    
    // Alquilar bicicleta con disponibilidad
    @Test
    public void testAlquilarBicicletaConBiciDisponibleOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        
        retorno = s.alquilarBicicleta("12345678", "Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    // Alquilar sin bicis disponibles: usuario queda en cola
    @Test
    public void testAlquilarBicicletaSinBiciDisponible_UsuarioQuedaEnCola() {
        retorno = s.alquilarBicicleta("12345678", "Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        Retorno usuariosEspera = s.usuariosEnEspera("Estacion1");
        assertEquals("12345678", usuariosEspera.getValorString());
    }
    
    // Error 1: parámetros null
    @Test
    public void testAlquilarBicicletaError1_ParametrosNull() {
        retorno = s.alquilarBicicleta(null, "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.alquilarBicicleta("12345678", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 1: parámetros vacíos
    @Test
    public void testAlquilarBicicletaError1_ParametrosVacios() {
        retorno = s.alquilarBicicleta("", "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
        
        retorno = s.alquilarBicicleta("12345678", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }
    
    // Error 2: usuario no existe
    @Test
    public void testAlquilarBicicletaError2_UsuarioNoExiste() {
        retorno = s.alquilarBicicleta("99999999", "Estacion1");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }
    
    // Error 3: estación no existe
    @Test
    public void testAlquilarBicicletaError3_EstacionNoExiste() {
        retorno = s.alquilarBicicleta("12345678", "EstacionInexistente");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
    
    // Usuario ya tiene bici alquilada: no hace nada
    @Test
    public void testAlquilarBicicleta_UsuarioYaTieneBiciAlquilada() {
        s.registrarBicicleta("BIC002", "URBANA");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.alquilarBicicleta("12345678", "Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }
    
    // Usuario ya en cola: no se duplica
    @Test
    public void testAlquilarBicicleta_UsuarioYaEnColaNoSeDuplica() {
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.alquilarBicicleta("12345678", "Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        Retorno usuariosEspera = s.usuariosEnEspera("Estacion1");
        assertEquals("12345678", usuariosEspera.getValorString());
    }
    
    // Varios usuarios en cola: orden FIFO
    @Test
    public void testAlquilarBicicleta_VariosUsuariosEnCola_OrdenFIFO() {
        s.registrarUsuario("11111111", "Usuario3");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        Retorno usuariosEspera = s.usuariosEnEspera("Estacion1");
        assertEquals("12345678|87654321|11111111", usuariosEspera.getValorString());
    }
}