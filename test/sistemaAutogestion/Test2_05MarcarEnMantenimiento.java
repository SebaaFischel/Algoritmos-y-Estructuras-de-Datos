package sistemaAutogestion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_05MarcarEnMantenimiento {
    private Retorno retorno;
    private final IObligatorio s = new Sistema();
    
    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
        s.registrarBicicleta("ABC123", "URBANA");
        s.registrarBicicleta("DEF456", "MOUNTAIN");
    }
    
@Test
public void marcarEnMantenimientoOk() {
    s.registrarBicicleta("ABC123", "URBANA");
    retorno = s.marcarEnMantenimiento("ABC123", "Rueda pinchada");
    assertEquals(Retorno.Resultado.OK, retorno.getResultado());
}

@Test
public void marcarEnMantenimientoError01() {
    retorno = s.marcarEnMantenimiento(null, "Rueda pinchada");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    
    retorno = s.marcarEnMantenimiento("", "Rueda pinchada");
    assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
}

@Test
public void marcarEnMantenimientoError02() {
    // bici inexistente
    retorno = s.marcarEnMantenimiento("XYZ999", "Rueda pinchada");
    assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
}


@Test
public void marcarEnMantenimientoError04() {
    s.registrarBicicleta("ABC123", "URBANA");
    s.marcarEnMantenimiento("ABC123", "Rueda pinchada");
    retorno = s.marcarEnMantenimiento("ABC123", "Freno roto");
    assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
}
}