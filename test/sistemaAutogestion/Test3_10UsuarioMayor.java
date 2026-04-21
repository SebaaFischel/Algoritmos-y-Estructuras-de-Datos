package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_10UsuarioMayor {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarEstacion("Estacion1", "Centro", 10);
    }
    
    // Sistema sin usuarios
    @Test
    public void testUsuarioMayorSinUsuariosOk() {
        retorno = s.usuarioMayor();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Un usuario sin alquileres
    @Test
    public void testUsuarioMayorUnoSinAlquileresOk() {
        s.registrarUsuario("12345678", "Usuario1");
        
        retorno = s.usuarioMayor();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678", retorno.getValorString());
    }
    
    // Usuario con alquileres
    @Test
    public void testUsuarioMayorUnoConAlquileresOk() {
        s.registrarUsuario("12345678", "Usuario1");
        s.registrarBicicleta("BIC001", "URBANA");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.usuarioMayor();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678", retorno.getValorString());
    }
    
    // Ganador claro: un usuario con más alquileres
    @Test
    public void testUsuarioMayorGanadorClaroOk() {
        s.registrarUsuario("12345678", "Usuario1");
        s.registrarUsuario("87654321", "Usuario2");
        s.registrarUsuario("11111111", "Usuario3");
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        s.alquilarBicicleta("87654321", "Estacion1");
        s.devolverBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.devolverBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        
        s.devolverBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.usuarioMayor();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("87654321", retorno.getValorString());
    }
    
    // Empate: desempata por cédula menor
    @Test
    public void testUsuarioMayorEmpateDesempatePorCedulaOk() {
        s.registrarUsuario("55555555", "UsuarioC");
        s.registrarUsuario("22222222", "UsuarioA");
        s.registrarUsuario("88888888", "UsuarioD");
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        s.alquilarBicicleta("55555555", "Estacion1");
        s.alquilarBicicleta("22222222", "Estacion1");
        s.alquilarBicicleta("88888888", "Estacion1");
        
        s.devolverBicicleta("55555555", "Estacion1");
        s.devolverBicicleta("22222222", "Estacion1");
        s.devolverBicicleta("88888888", "Estacion1");
        
        s.alquilarBicicleta("55555555", "Estacion1");
        s.alquilarBicicleta("22222222", "Estacion1");
        s.alquilarBicicleta("88888888", "Estacion1");
        
        retorno = s.usuarioMayor();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("22222222", retorno.getValorString());
    }
    
    // Todos con cero: desempate por cédula
    @Test
    public void testUsuarioMayorTodosCeroAlquileresOk() {
        s.registrarUsuario("99999999", "UsuarioZ");
        s.registrarUsuario("11111111", "UsuarioA");
        s.registrarUsuario("55555555", "UsuarioM");
        
        retorno = s.usuarioMayor();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("11111111", retorno.getValorString());
    }
    
    // Después de deshacer: contador decrementado
    @Test
    public void testUsuarioMayorDespuesDeDeshacerOk() {
        s.registrarUsuario("12345678", "Usuario1");
        s.registrarUsuario("87654321", "Usuario2");
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.devolverBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        s.devolverBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        
        retorno = s.usuarioMayor();
        assertEquals("12345678", retorno.getValorString());
        
        s.deshacerUltimosRetiros(1);
        
        retorno = s.usuarioMayor();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678", retorno.getValorString());
    }
    
    // Deshacer puede cambiar al usuario mayor
    @Test
    public void testUsuarioMayorCambiaAlDeshacerOk() {
        s.registrarUsuario("12345678", "Usuario1");
        s.registrarUsuario("87654321", "Usuario2");
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        
        s.devolverBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.devolverBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        
        retorno = s.usuarioMayor();
        assertEquals("87654321", retorno.getValorString());
        
        s.deshacerUltimosRetiros(2);
        
        retorno = s.usuarioMayor();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("12345678", retorno.getValorString());
    }
}