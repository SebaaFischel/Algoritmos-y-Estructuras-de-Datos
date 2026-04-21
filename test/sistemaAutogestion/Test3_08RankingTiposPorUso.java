package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_08RankingTiposPorUso {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarEstacion("Estacion1", "Centro", 10);
        s.registrarUsuario("12345678", "Usuario1");
        s.registrarUsuario("87654321", "Usuario2");
        s.registrarUsuario("11111111", "Usuario3");
    }
    
    // Sistema sin bicicletas
    @Test
    public void testRankingSinBicicletasRegistradasOk() {
        retorno = s.rankingTiposPorUso();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ELECTRICA#0|MOUNTAIN#0|URBANA#0", retorno.getValorString());
    }
    
    // Bicicletas registradas sin alquileres
    @Test
    public void testRankingTodosCeroOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        retorno = s.rankingTiposPorUso();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ELECTRICA#0|MOUNTAIN#0|URBANA#0", retorno.getValorString());
    }
    
    // Empate triple: orden alfabético
    @Test
    public void testRankingEmpateTresTiposOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        retorno = s.rankingTiposPorUso();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ELECTRICA#1|MOUNTAIN#1|URBANA#1", retorno.getValorString());
    }
    
    // Empate parcial: dos tipos empatan
    @Test
    public void testRankingEmpateDosTiposOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        s.registrarBicicleta("BIC004", "URBANA");
        s.registrarBicicleta("BIC005", "URBANA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        s.asignarBicicletaAEstacion("BIC004", "Estacion1");
        s.asignarBicicletaAEstacion("BIC005", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        s.devolverBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        s.devolverBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("12345678", "Estacion1");
        
        retorno = s.rankingTiposPorUso();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("URBANA#3|ELECTRICA#1|MOUNTAIN#1", retorno.getValorString());
    }
    
    // Orden descendente sin empates
    @Test
    public void testRankingOrdenDescendenteOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        s.registrarBicicleta("BIC004", "URBANA");
        s.registrarBicicleta("BIC005", "URBANA");
        s.registrarBicicleta("BIC006", "MOUNTAIN");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        s.asignarBicicletaAEstacion("BIC004", "Estacion1");
        s.asignarBicicletaAEstacion("BIC005", "Estacion1");
        s.asignarBicicletaAEstacion("BIC006", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        s.devolverBicicleta("12345678", "Estacion1");
        s.devolverBicicleta("87654321", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        
        s.devolverBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        
        retorno = s.rankingTiposPorUso();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("URBANA#3|MOUNTAIN#2|ELECTRICA#1", retorno.getValorString());
    }
    
    // Solo un tipo registrado
    @Test
    public void testRankingSoloUnTipoRegistradoOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "URBANA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        
        retorno = s.rankingTiposPorUso();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("URBANA#2|ELECTRICA#0|MOUNTAIN#0", retorno.getValorString());
    }
    
    // Después de deshacer alquileres
    @Test
    public void testRankingDespuesDeDeshacerOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        s.deshacerUltimosRetiros(2);
        
        retorno = s.rankingTiposPorUso();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("URBANA#1|ELECTRICA#0|MOUNTAIN#0", retorno.getValorString());
    }
    
    // Múltiples bicicletas del mismo tipo
    @Test
    public void testRankingVariasBicisMismoTipoOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "URBANA");
        s.registrarBicicleta("BIC003", "MOUNTAIN");
        s.registrarBicicleta("BIC004", "MOUNTAIN");
        s.registrarBicicleta("BIC005", "ELECTRICA");
        s.registrarBicicleta("BIC006", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        s.asignarBicicletaAEstacion("BIC004", "Estacion1");
        s.asignarBicicletaAEstacion("BIC005", "Estacion1");
        s.asignarBicicletaAEstacion("BIC006", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        s.devolverBicicleta("12345678", "Estacion1");
        s.devolverBicicleta("87654321", "Estacion1");
        s.devolverBicicleta("11111111", "Estacion1");
        
        s.alquilarBicicleta("12345678", "Estacion1");
        s.alquilarBicicleta("87654321", "Estacion1");
        s.alquilarBicicleta("11111111", "Estacion1");
        
        retorno = s.rankingTiposPorUso();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("ELECTRICA#2|MOUNTAIN#2|URBANA#2", retorno.getValorString());
    }
}