package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_07OcupacionPromedioXBarrio {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    // Sistema sin estaciones
    @Test
    public void testOcupacionPromedioSinEstacionesOk() {
        retorno = s.ocupacionPromedioXBarrio();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Un barrio sin bicicletas
    @Test
    public void testOcupacionPromedioUnBarrioVacioOk() {
        s.registrarEstacion("Estacion1", "Centro", 10);
        
        retorno = s.ocupacionPromedioXBarrio();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#0", retorno.getValorString());
    }
    
    // Un barrio con bicicletas
    @Test
    public void testOcupacionPromedioUnBarrioConBicisOk() {
        s.registrarEstacion("Estacion1", "Pocitos", 10);
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        retorno = s.ocupacionPromedioXBarrio();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Pocitos#30", retorno.getValorString());
    }
    
    // Varios barrios: orden alfabético
    @Test
    public void testOcupacionPromedioVariosBarriosOrdenAlfabeticoOk() {
        s.registrarEstacion("EstacionZ", "Pocitos", 10);
        s.registrarEstacion("EstacionA", "Centro", 20);
        s.registrarEstacion("EstacionM", "Aguada", 5);
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "EstacionZ");
        s.asignarBicicletaAEstacion("BIC002", "EstacionA");
        s.asignarBicicletaAEstacion("BIC003", "EstacionM");
        
        retorno = s.ocupacionPromedioXBarrio();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Aguada#20|Centro#5|Pocitos#10", retorno.getValorString());
    }
    
    // Varias estaciones en el mismo barrio
    @Test
    public void testOcupacionPromedioVariasEstacionesMismoBarrioOk() {
        s.registrarEstacion("Estacion1", "Centro", 10);
        s.registrarEstacion("Estacion2", "Centro", 10);
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion2");
        s.asignarBicicletaAEstacion("BIC003", "Estacion2");
        
        retorno = s.ocupacionPromedioXBarrio();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#15", retorno.getValorString());
    }
    
    // Redondeo hacia abajo
    @Test
    public void testOcupacionPromedioRedondeoOk() {
        s.registrarEstacion("Estacion1", "Centro", 3);
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        
        retorno = s.ocupacionPromedioXBarrio();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#33", retorno.getValorString());
    }
    
    // Redondeo hacia arriba
    @Test
    public void testOcupacionPromedioRedondeoHaciaArribaOk() {
        s.registrarEstacion("Estacion1", "Pocitos", 3);
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        retorno = s.ocupacionPromedioXBarrio();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Pocitos#67", retorno.getValorString());
    }
    
    // Ocupación 100%
    @Test
    public void testOcupacionPromedio100PorCientoOk() {
        s.registrarEstacion("Estacion1", "Centro", 2);
        
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion1");
        
        retorno = s.ocupacionPromedioXBarrio();
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("Centro#100", retorno.getValorString());
    }
}