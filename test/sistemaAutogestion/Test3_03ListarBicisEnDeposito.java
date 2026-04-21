package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_03ListarBicisEnDeposito {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }
    
@Test
public void listarBicisEnDepositoOk() {
    s.registrarBicicleta("ABC123", "URBANA");
    s.registrarBicicleta("DEF456", "MOUNTAIN");
    retorno = s.listarBicisEnDeposito();
    assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    assertTrue(retorno.getValorString().contains("ABC123#URBANA#Disponible"));
}

@Test
public void listarBicisEnDepositoConMantenimiento() {
    s.registrarBicicleta("ABC123", "URBANA");
    s.marcarEnMantenimiento("ABC123", "Rueda pinchada");
    retorno = s.listarBicisEnDeposito();
    assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    assertTrue(retorno.getValorString().contains("ABC123#URBANA#Mantenimiento"));
}
}