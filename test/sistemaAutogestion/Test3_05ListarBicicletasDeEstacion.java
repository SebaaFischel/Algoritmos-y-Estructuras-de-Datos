package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_05ListarBicicletasDeEstacion {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarEstacion("Estacion1", "Centro", 10);
        s.registrarEstacion("Estacion2", "Pocitos", 5);
    }
    
    // Estación sin bicicletas
    @Test
    public void testListarBicicletasEstacionVaciaOk() {
        retorno = s.listarBicicletasDeEstacion("Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Estación con una bicicleta
    @Test
    public void testListarBicicletasEstacionConUnaBiciOk() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        
        retorno = s.listarBicicletasDeEstacion("Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("BIC001", retorno.getValorString());
    }
    
    // Varias bicicletas: orden alfabético creciente
    @Test
    public void testListarBicicletasEstacionOrdenCrecienteOk() {
        s.registrarBicicleta("ZZZ999", "URBANA");
        s.registrarBicicleta("AAA111", "MOUNTAIN");
        s.registrarBicicleta("MMM555", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("ZZZ999", "Estacion1");
        s.asignarBicicletaAEstacion("AAA111", "Estacion1");
        s.asignarBicicletaAEstacion("MMM555", "Estacion1");
        
        retorno = s.listarBicicletasDeEstacion("Estacion1");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("AAA111|MMM555|ZZZ999", retorno.getValorString());
    }
    
    // Nombre de estación null
    @Test
    public void testListarBicicletasEstacionNull() {
        retorno = s.listarBicicletasDeEstacion(null);
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Nombre de estación vacío
    @Test
    public void testListarBicicletasEstacionVacia() {
        retorno = s.listarBicicletasDeEstacion("");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Estación inexistente
    @Test
    public void testListarBicicletasEstacionInexistente() {
        retorno = s.listarBicicletasDeEstacion("EstacionInexistente");
        
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }
    
    // Bicicletas en diferentes estaciones
    @Test
    public void testListarBicicletasMultiplesEstaciones() {
        s.registrarBicicleta("BIC001", "URBANA");
        s.registrarBicicleta("BIC002", "MOUNTAIN");
        s.registrarBicicleta("BIC003", "ELECTRICA");
        
        s.asignarBicicletaAEstacion("BIC001", "Estacion1");
        s.asignarBicicletaAEstacion("BIC002", "Estacion2");
        s.asignarBicicletaAEstacion("BIC003", "Estacion1");
        
        retorno = s.listarBicicletasDeEstacion("Estacion1");
        assertEquals("BIC001|BIC003", retorno.getValorString());
        
        retorno = s.listarBicicletasDeEstacion("Estacion2");
        assertEquals("BIC002", retorno.getValorString());
    }
}