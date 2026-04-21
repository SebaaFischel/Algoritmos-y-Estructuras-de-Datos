package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_04RegistrarBicicleta {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
    @Test
public void registrarBicicletaOk() {
    retorno = s.registrarBicicleta("ABC123", "URBANA");
    assertEquals(Retorno.Resultado.OK, retorno.getResultado());
}

@Test
public void registrarBicicletaError01() {
    retorno = s.registrarBicicleta(null, "URBANA");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    
    retorno = s.registrarBicicleta("", "URBANA");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
}

@Test
public void registrarBicicletaError02() {
    retorno = s.registrarBicicleta("ABC", "URBANA");
    assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    
    retorno = s.registrarBicicleta("ABC1234", "URBANA");
    assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
}

@Test
public void registrarBicicletaError03() {
    retorno = s.registrarBicicleta("ABC123", "DEPORTIVA");
    assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
}

@Test
public void registrarBicicletaError04() {
    s.registrarBicicleta("ABC123", "URBANA");
    retorno = s.registrarBicicleta("ABC123", "MOUNTAIN");
    assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
}
}