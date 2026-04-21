package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_09UsuariosEnEspera {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarEstacion("Estacion1", "Centro", 2);
        s.registrarEstacion("Estacion2", "Pocitos", 5);
        s.registrarUsuario("12345678", "Usuario1");
        s.registrarUsuario("87654321", "Usuario2");
        s.registrarUsuario("11111111", "Usuario3");
        s.registrarUsuario("22222222", "Usuario4");
    }
    
    // Estación sin usuarios esperando
    @Test
    public void testUsuariosEnEsperaSinUsuariosOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        
        retorno = s.usuariosEnEspera("Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Estación inexistente
    @Test
    public void testUsuariosEnEsperaEstacionInexistenteOk() {
        retorno = s.usuariosEnEspera("EstacionNoExiste");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Nombre de estación null
    @Test
    public void testUsuariosEnEsperaNombreNullOk() {
        retorno = s.usuariosEnEspera(null);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Nombre de estación vacío
    @Test
    public void testUsuariosEnEsperaNombreVacioOk() {
        retorno = s.usuariosEnEspera("");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Un usuario en espera
    @Test
    public void testUsuariosEnEsperaUnUsuarioOk() {
        retorno = s.alquilarBicicleta("12345678", "Estacion1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        
        retorno = s.usuariosEnEspera("Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678", retorno.getValorString());
    }
    
    // Múltiples usuarios: orden FIFO
    @Test
    public void testUsuariosEnEsperaVariosUsuariosOrdenFIFOOk() {
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        s.alquilarBicicleta("22222222", "Estacion1");
        
        retorno = s.usuariosEnEspera("Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678|87654321|11111111|22222222", retorno.getValorString());
    }
    
    // Usuario sale de la cola al recibir bici
    @Test
    public void testUsuariosEnEsperaSalenAlLlegarBicisOk() {
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        retorno = s.usuariosEnEspera("Estacion1");
        assertEquals("12345678|87654321|11111111", retorno.getValorString());
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarEstacion("EstacionTemp", "Barrio", 5);
        s.asignarBicicletaAEstacion("BIC001", "EstacionTemp");
        
        s.registrarUsuario("99999999", "UsuarioAux");
        s.alquilarBicicleta("99999999", "EstacionTemp");
        s.devolverBicicleta("99999999", "Estacion1");
        
        retorno = s.usuariosEnEspera("Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("87654321|11111111", retorno.getValorString());
    }
    
    // Estación con bicis: nadie espera
    @Test
    public void testUsuariosEnEsperaConBicisDisponiblesOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.usuariosEnEspera("Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Usuarios en diferentes estaciones
    @Test
    public void testUsuariosEnEsperaDiferentesEstacionesOk() {
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion2");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        retorno = s.usuariosEnEspera("Estacion1");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678|11111111", retorno.getValorString());
        
        retorno = s.usuariosEnEspera("Estacion2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("87654321", retorno.getValorString());
    }
}