package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_06RepararBicicleta {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarBicicleta("ABC123", "URBANA");
        s.registrarBicicleta("DEF456", "MOUNTAIN");
    }
    
@Test
public void repararBicicletaOk() {
    s.registrarBicicleta("ABC123", "URBANA");
    s.marcarEnMantenimiento("ABC123", "Rueda pinchada");
    retorno = s.repararBicicleta("ABC123");
    assertEquals(Retorno.Resultado.OK, retorno.getResultado());
}

@Test
public void repararBicicletaError01() {
    retorno = s.repararBicicleta(null);
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    
    retorno = s.repararBicicleta("");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
}

@Test
public void repararBicicletaError02() {
    retorno = s.repararBicicleta("XYZ999");
    assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
}

@Test
public void repararBicicletaError03() {
    s.registrarBicicleta("ABC123", "URBANA");
    retorno = s.repararBicicleta("ABC123");
    assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
}
}