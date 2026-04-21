package sistemaAutogestion;

import dominio.*;
import tads.TADLista.ListaNodos;
import tads.TADLista.NodoLista;  
import tads.TADPila.PilaNodos;
import tads.TADCola.NodoCola;

public class Sistema implements IObligatorio {

    private ListaNodos<Usuario> usuarios;
    private ListaNodos<Estacion> estaciones;
    private ListaNodos<Bicicleta> bicicletas;
    private PilaNodos<Alquiler> historialAlquileres;

    @Override
    public Retorno crearSistemaDeGestion() {
        usuarios = new ListaNodos<>();
        estaciones = new ListaNodos<>();
        bicicletas = new ListaNodos<>();
        historialAlquileres = new PilaNodos<>();
        return Retorno.ok();
    }

    @Override
    public Retorno registrarEstacion(String nombre, String barrio, int capacidad) {
        if (nombre == null || barrio == null) {
            return Retorno.error1();
        }
        
        if (nombre.trim().isEmpty() || barrio.trim().isEmpty()) {
            return Retorno.error1();
        }
        
        if (capacidad <= 0) {
            return Retorno.error2();
        }

        Estacion estacionBuscada = new Estacion(nombre, barrio, capacidad);
        if (estaciones.obtenerElemento(estacionBuscada) != null) {
            return Retorno.error3();
        }

        estaciones.agregarFinal(estacionBuscada);
        return Retorno.ok();
    }

    @Override
    public Retorno registrarUsuario(String cedula, String nombre) {
        if (nombre == null || cedula == null) {
            return Retorno.error1();
        }
        
        if (nombre.trim().isEmpty() || cedula.trim().isEmpty()) {
            return Retorno.error1();
        }

        if (cedula.length() != 8 || !cedula.matches("\\d{8}")) {
            return Retorno.error2();
        }
        
        Usuario usuarioBuscado = new Usuario(cedula, nombre);
        if (usuarios.obtenerElemento(usuarioBuscado) != null) {
            return Retorno.error3();
        }
        
        usuarios.agregarOrd(usuarioBuscado);
        return Retorno.ok();
    }

    @Override
    public Retorno registrarBicicleta(String codigo, String tipo) {
        if (codigo == null || tipo == null) {
            return Retorno.error1();
        }
        
        if (codigo.trim().isEmpty() || tipo.trim().isEmpty()) {
            return Retorno.error1();
        }

        if (codigo.length() != 6) {
            return Retorno.error2();
        }
        
        Bicicleta.Tipo tipoBici;
        try {
            tipoBici = Bicicleta.Tipo.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Retorno.error3();
        }
        
        Bicicleta bicicletaBuscada = new Bicicleta(codigo, tipoBici);
        if (bicicletas.obtenerElemento(bicicletaBuscada) != null) {
            return Retorno.error4();
        }
        
        bicicletas.agregarOrd(bicicletaBuscada);
        return Retorno.ok();
    }

    @Override
    public Retorno marcarEnMantenimiento(String codigo, String motivo) {
        if (codigo == null || motivo == null) {
            return Retorno.error1();
        }
        
        if (codigo.trim().isEmpty() || motivo.trim().isEmpty()) {
            return Retorno.error1();
        }
        
        Bicicleta bicicletaBuscada = new Bicicleta(codigo, Bicicleta.Tipo.URBANA); 
        NodoLista<Bicicleta> nodo = bicicletas.obtenerElemento(bicicletaBuscada);
        
        if (nodo == null) {
            return Retorno.error2();
        }
        
        Bicicleta bici = (Bicicleta) nodo.getDato();
        
        if (bici.estaAlquilada()) {
            return Retorno.error3();
        }
        
        if (bici.estaEnMantenimiento()) {  
            return Retorno.error4();
        }
        
        bici.setEstado(Bicicleta.Estado.MANTENIMIENTO);
        bici.setMotivoMantenimiento(motivo);
        
        if (!bici.estaEnDeposito()) {
            Estacion estacion = bici.getEstacionActual();
            estacion.getBicicletasAncladas().borrarElemento(bici);
            bici.setEstacionActual(null); 
        }
        
        return Retorno.ok();
    }

    @Override
    public Retorno repararBicicleta(String codigo) {
        if (codigo == null) {
            return Retorno.error1();
        }
        
        if (codigo.trim().isEmpty()) {
            return Retorno.error1();
        }
        
        Bicicleta bicicletaBuscada = new Bicicleta(codigo, Bicicleta.Tipo.URBANA);
        NodoLista<Bicicleta> nodo = bicicletas.obtenerElemento(bicicletaBuscada);
        
        if (nodo == null) {
            return Retorno.error2();
        }
        
        Bicicleta bici = (Bicicleta) nodo.getDato();
        
        if (!bici.estaEnMantenimiento()) {
            return Retorno.error3();
        }
        bici.setEstado(Bicicleta.Estado.DISPONIBLE);
        bici.setMotivoMantenimiento(null);
        
        return Retorno.ok();
    }

    @Override
    public Retorno eliminarEstacion(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Retorno.error1();
        }
        
        Estacion estacionBuscada = new Estacion(nombre, "", 0);
        NodoLista<Estacion> nodo = estaciones.obtenerElemento(estacionBuscada);
        
        if (nodo == null) {
            return Retorno.error2();
        }
        
        Estacion estacion = nodo.getDato();
        
        if (estacion.getBicicletasAncladas().cantElementos() > 0 || 
            estacion.tieneUsuariosEsperando()) {
            return Retorno.error3();
        }
        
        estaciones.borrarElemento(estacion);
        return Retorno.ok();
    }

    @Override
    public Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion) {
        if (codigo == null || nombreEstacion == null || 
            codigo.trim().isEmpty() || nombreEstacion.trim().isEmpty()) {
            return Retorno.error1();
        }
        
        Bicicleta bicicletaBuscada = new Bicicleta(codigo, Bicicleta.Tipo.URBANA);
        NodoLista<Bicicleta> nodoBici = bicicletas.obtenerElemento(bicicletaBuscada);
        
        if (nodoBici == null) {
            return Retorno.error2();
        }
        
        Bicicleta bici = nodoBici.getDato();
        
        if (!bici.estaDisponible()) {
            return Retorno.error2();
        }
        
        Estacion estacionBuscada = new Estacion(nombreEstacion, "", 0);
        NodoLista<Estacion> nodoEstacion = estaciones.obtenerElemento(estacionBuscada);
        
        if (nodoEstacion == null) {
            return Retorno.error3();
        }
        
        Estacion estacion = nodoEstacion.getDato();
        
        if (!bici.estaEnDeposito() && bici.getEstacionActual().equals(estacion)) {
            return Retorno.ok();
        }
        
        if (!estacion.tieneEspacioLibre()) {
            return Retorno.error4();
        }
        
        if (!bici.estaEnDeposito()) {
            Estacion estacionAnterior = bici.getEstacionActual();
            estacionAnterior.getBicicletasAncladas().borrarElemento(bici);
            procesarColasAnclaje(estacionAnterior);
        }
        
        estacion.getBicicletasAncladas().agregarFinal(bici);
        bici.setEstacionActual(estacion);
        
        return Retorno.ok();
    }

    @Override
    public Retorno alquilarBicicleta(String cedula, String nombreEstacion) {
        if (cedula == null || nombreEstacion == null || 
            cedula.trim().isEmpty() || nombreEstacion.trim().isEmpty()) {
            return Retorno.error1();
        }
        
        Usuario usuarioBuscado = new Usuario(cedula, "");
        NodoLista<Usuario> nodoUsuario = usuarios.obtenerElemento(usuarioBuscado);
        
        if (nodoUsuario == null) {
            return Retorno.error2();
        }
        
        Usuario usuario = nodoUsuario.getDato();
        
        Estacion estacionBuscada = new Estacion(nombreEstacion, "", 0);
        NodoLista<Estacion> nodoEstacion = estaciones.obtenerElemento(estacionBuscada);
        
        if (nodoEstacion == null) {
            return Retorno.error3();
        }
        
        Estacion estacion = nodoEstacion.getDato();
        
        if (usuario.tieneAlquiler()) {
            return Retorno.ok();
        }
        
        if (estacion.getColaEsperaAlquiler().contiene(usuario)) {
            return Retorno.ok();
        }
        
        if (estacion.tieneBicicletasDisponibles()) {
            NodoLista<Bicicleta> nodoBici = estacion.getBicicletasAncladas().getLista();
            Bicicleta bici = nodoBici.getDato();
            
            estacion.getBicicletasAncladas().borrarInicio();
            
            bici.setEstado(Bicicleta.Estado.ALQUILADA);
            bici.setEstacionActual(null);
            usuario.setBicicletaAlquilada(bici);
            usuario.incrementarAlquileres();
            bici.incrementarVecesAlquilada();
            
            Alquiler alquiler = new Alquiler(bici.getCodigo(), cedula, nombreEstacion);
            historialAlquileres.push(alquiler);
            
            procesarColasAnclaje(estacion);
        } else {
            estacion.getColaEsperaAlquiler().encolar(usuario);
        }
        
        return Retorno.ok();
    }

    @Override
    public Retorno devolverBicicleta(String cedula, String nombreEstacionDestino) {
        if (cedula == null || nombreEstacionDestino == null || 
            cedula.trim().isEmpty() || nombreEstacionDestino.trim().isEmpty()) {
            return Retorno.error1();
        }
        
        Usuario usuarioBuscado = new Usuario(cedula, "");
        NodoLista<Usuario> nodoUsuario = usuarios.obtenerElemento(usuarioBuscado);
        
        if (nodoUsuario == null || !nodoUsuario.getDato().tieneAlquiler()) {
            return Retorno.error2();
        }
        
        Usuario usuario = nodoUsuario.getDato();
        
        Estacion estacionBuscada = new Estacion(nombreEstacionDestino, "", 0);
        NodoLista<Estacion> nodoEstacion = estaciones.obtenerElemento(estacionBuscada);
        
        if (nodoEstacion == null) {
            return Retorno.error3();
        }
        
        Estacion estacionDestino = nodoEstacion.getDato();
        Bicicleta bici = usuario.getBicicletaAlquilada();
        
        if (estacionDestino.tieneEspacioLibre()) {
            PilaNodos<Alquiler> pilaTemp = new PilaNodos<>();
            boolean encontrado = false;
            
            while (!historialAlquileres.esVacia() && !encontrado) {
                Alquiler alq = historialAlquileres.poptop();
                
                if (alq.getCedulaUsuario().equals(cedula) && alq.estaActivo()) {
                    alq.setEstacionDestino(nombreEstacionDestino);
                    encontrado = true;
                }
                
                pilaTemp.push(alq);
            }
            
            while (!pilaTemp.esVacia()) {
                historialAlquileres.push(pilaTemp.poptop());
            }
            
            usuario.setBicicletaAlquilada(null);
            
            if (!estacionDestino.getColaEsperaAlquiler().esVacia()) {
                Usuario siguienteUsuario = estacionDestino.getColaEsperaAlquiler().dequeue();
                
                siguienteUsuario.setBicicletaAlquilada(bici);
                siguienteUsuario.incrementarAlquileres();
                bici.incrementarVecesAlquilada();
                
                bici.setEstado(Bicicleta.Estado.ALQUILADA);
                bici.setEstacionActual(null);
                
                Alquiler nuevoAlquiler = new Alquiler(bici.getCodigo(), siguienteUsuario.getCedula(), nombreEstacionDestino);
                historialAlquileres.push(nuevoAlquiler);
            } else {
                bici.setEstado(Bicicleta.Estado.DISPONIBLE);
                bici.setEstacionActual(estacionDestino);
                estacionDestino.getBicicletasAncladas().agregarFinal(bici);
            }
            
            procesarColasAnclaje(estacionDestino);
        } else {
            estacionDestino.getColaEsperaAnclaje().encolar(usuario);
        }
        
        return Retorno.ok();
    }

    @Override
    public Retorno deshacerUltimosRetiros(int n) {
        if (n <= 0) {
            return Retorno.error1();
        }
        
        StringBuilder resultado = new StringBuilder();
        PilaNodos<Alquiler> pilaTemp = new PilaNodos<>();
        int deshechos = 0;
        boolean primero = true;
        
        while (!historialAlquileres.esVacia() && deshechos < n) {
            Alquiler alquiler = historialAlquileres.poptop();
            
            if (alquiler.estaActivo()) {
                Usuario usuarioBuscado = new Usuario(alquiler.getCedulaUsuario(), "");
                NodoLista<Usuario> nodoUsuario = usuarios.obtenerElemento(usuarioBuscado);
                
                if (nodoUsuario != null && nodoUsuario.getDato().tieneAlquiler()) {
                    Usuario usuario = nodoUsuario.getDato();
                    Bicicleta bici = usuario.getBicicletaAlquilada();
                    
                    if (bici.getCodigo().equals(alquiler.getCodigoBicicleta())) {
                        Estacion estacionBuscada = new Estacion(alquiler.getEstacionOrigen(), "", 0);
                        NodoLista<Estacion> nodoEstacion = estaciones.obtenerElemento(estacionBuscada);
                        
                        if (nodoEstacion != null) {
                            Estacion estacionOrigen = nodoEstacion.getDato();
                            
                            if (estacionOrigen.tieneEspacioLibre()) {
                                usuario.decrementarAlquileres();
                                bici.decrementarVecesAlquilada();
                                usuario.setBicicletaAlquilada(null);
                                
                                bici.setEstado(Bicicleta.Estado.DISPONIBLE);
                                bici.setEstacionActual(estacionOrigen);
                                estacionOrigen.getBicicletasAncladas().agregarFinal(bici);
                            } else {
                                estacionOrigen.getColaEsperaAnclaje().encolar(usuario);
                            }
                            
                            alquiler.setEstacionDestino("DESHECHO");
                            
                            if (!primero) {
                                resultado.append("|");
                            }
                            resultado.append(alquiler.toString());
                            primero = false;
                            deshechos++;
                        }
                    }
                }
            }
            
            pilaTemp.push(alquiler);
        }
        
        while (!pilaTemp.esVacia()) {
            historialAlquileres.push(pilaTemp.poptop());
        }
        
        return Retorno.ok(resultado.toString());
    }

    @Override
    public Retorno obtenerUsuario(String cedula) {
        if (cedula == null) {
            return Retorno.error1();
        }
        
        if (cedula.trim().isEmpty()) {
            return Retorno.error1();
        }
        
        if (cedula.length() != 8 || !cedula.matches("\\d{8}")) {
            return Retorno.error2();
        }
        
        Usuario usuarioBuscado = new Usuario(cedula, ""); 
        NodoLista<Usuario> nodo = usuarios.obtenerElemento(usuarioBuscado);
        
        if (nodo == null) {
            return Retorno.error3();
        }
        
        Usuario usuario = (Usuario) nodo.getDato();
        
        return Retorno.ok(usuario.toString());
    }

    @Override
    public Retorno listarUsuarios() {
        return Retorno.ok(usuarios.mostrar()); 
    }

    private String listarBicisEnDepositoREC(NodoLista<Bicicleta> nodo) {
        if (nodo == null) return "";

        Bicicleta bici = nodo.getDato();
        String actual = "";
        if (bici.estaEnDeposito()) {
            actual = bici.getCodigo() + "#" + bici.getTipo() + "#" +
                     (bici.estaEnMantenimiento() ? "Mantenimiento" : "Disponible");
        }

        String resto = listarBicisEnDepositoREC(nodo.getSiguiente());

        if (!actual.isEmpty() && !resto.isEmpty()) return actual + "|" + resto;
        if (!actual.isEmpty()) return actual;
        return resto;
    }

    @Override
    public Retorno listarBicisEnDeposito() {
        String resultado = listarBicisEnDepositoREC(bicicletas.getLista());
        return Retorno.ok(resultado);  
    }

    @Override
    public Retorno informaciónMapa(String[][] mapa) {
        if (mapa == null || mapa.length == 0 || mapa[0].length == 0) {
            return Retorno.ok("0#ambas|no existe");
        }
        
        int filas = mapa.length;
        int columnas = mapa[0].length;
        
        int[] estacionesPorFila = new int[filas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (mapa[i][j] != null && !mapa[i][j].equals("0") && !mapa[i][j].trim().isEmpty()) {
                    estacionesPorFila[i]++;
                }
            }
        }

        int[] estacionesPorColumna = new int[columnas];
        for (int j = 0; j < columnas; j++) {
            for (int i = 0; i < filas; i++) {
                if (mapa[i][j] != null && !mapa[i][j].equals("0") && !mapa[i][j].trim().isEmpty()) {
                    estacionesPorColumna[j]++;
                }
            }
        }

        int maxFilas = 0;
        for (int i = 0; i < filas; i++) {
            if (estacionesPorFila[i] > maxFilas) {
                maxFilas = estacionesPorFila[i];
            }
        }

        int maxColumnas = 0;
        for (int j = 0; j < columnas; j++) {
            if (estacionesPorColumna[j] > maxColumnas) {
                maxColumnas = estacionesPorColumna[j];
            }
        }
        int maximoAbsoluto;
        String tipoMaximo;
        
        if (maxFilas > maxColumnas) {
            maximoAbsoluto = maxFilas;
            tipoMaximo = "fila";
        } else if (maxColumnas > maxFilas) {
            maximoAbsoluto = maxColumnas;
            tipoMaximo = "columna";
        } else {
            maximoAbsoluto = maxFilas; 
            tipoMaximo = "ambas";
        }
        
        String parte1 = maximoAbsoluto + "#" + tipoMaximo;
        
        boolean existeTresConsecutivas = false;
        
        if (columnas >= 3) {
            for (int j = 0; j <= columnas - 3; j++) {
                if (estacionesPorColumna[j] < estacionesPorColumna[j + 1] && 
                    estacionesPorColumna[j + 1] < estacionesPorColumna[j + 2]) {
                    existeTresConsecutivas = true;
                    break;
                }
            }
        }
        
        String parte2 = existeTresConsecutivas ? "existe" : "no existe";
        
        String resultado = parte1 + "|" + parte2;
        return Retorno.ok(resultado);
    }

    @Override
    public Retorno listarBicicletasDeEstacion(String nombreEstacion) {
        if (nombreEstacion == null || nombreEstacion.trim().isEmpty()) {
            return Retorno.ok("");
        }
        
        Estacion estacionBuscada = new Estacion(nombreEstacion, "", 0);
        NodoLista<Estacion> nodoEstacion = estaciones.obtenerElemento(estacionBuscada);
        
        if (nodoEstacion == null) {
            return Retorno.ok("");
        }
        
        Estacion estacion = nodoEstacion.getDato();
        
        ListaNodos<String> codigosOrdenados = new ListaNodos<>();
        NodoLista<Bicicleta> nodoBici = estacion.getBicicletasAncladas().getLista();
        
        while (nodoBici != null) {
            codigosOrdenados.agregarOrd(nodoBici.getDato().getCodigo());
            nodoBici = nodoBici.getSiguiente();
        }
        
        StringBuilder resultado = new StringBuilder();
        NodoLista<String> nodoCodigo = codigosOrdenados.getLista();
        
        while (nodoCodigo != null) {
            resultado.append(nodoCodigo.getDato());
            nodoCodigo = nodoCodigo.getSiguiente();
            if (nodoCodigo != null) {
                resultado.append("|");
            }
        }
        
        return Retorno.ok(resultado.toString());
    }

    @Override
    public Retorno estacionesConDisponibilidad(int n) {
        if (n <= 1) {
            return Retorno.error1();
        }
        
        int contador = 0;
        NodoLista<Estacion> nodo = estaciones.getLista();
        
        while (nodo != null) {
            Estacion estacion = nodo.getDato();
            if (estacion.getBicicletasDisponibles() > n) {
                contador++;
            }
            nodo = nodo.getSiguiente();
        }
        
        return Retorno.ok(contador);
    }

    @Override
    public Retorno ocupacionPromedioXBarrio() {
        ListaNodos<BarrioAux> barriosAux = new ListaNodos<>();
        
        NodoLista<Estacion> nodo = estaciones.getLista();
        
        while (nodo != null) {
            Estacion estacion = nodo.getDato();
            String barrio = estacion.getBarrio();
            
            BarrioAux barrioEncontrado = buscarBarrio(barriosAux, barrio);
            
            if (barrioEncontrado == null) {
                BarrioAux nuevoBarrio = new BarrioAux(barrio);
                nuevoBarrio.capacidadTotal += estacion.getCapacidad();
                nuevoBarrio.bicicletasTotal += estacion.getBicicletasDisponibles();
                barriosAux.agregarOrd(nuevoBarrio);
            } else {
                barrioEncontrado.capacidadTotal += estacion.getCapacidad();
                barrioEncontrado.bicicletasTotal += estacion.getBicicletasDisponibles();
            }
            
            nodo = nodo.getSiguiente();
        }
        
        StringBuilder resultado = new StringBuilder();
        NodoLista<BarrioAux> nodoBarrio = barriosAux.getLista();
        boolean primero = true;
        
        while (nodoBarrio != null) {
            BarrioAux barrio = nodoBarrio.getDato();
            
            if (!primero) resultado.append("|");
            
            int porcentaje = (int) Math.round((double) barrio.bicicletasTotal / barrio.capacidadTotal * 100);
            resultado.append(barrio.nombre).append("#").append(porcentaje);
            
            primero = false;
            nodoBarrio = nodoBarrio.getSiguiente();
        }
        
        return Retorno.ok(resultado.toString());
    }

    private BarrioAux buscarBarrio(ListaNodos<BarrioAux> barrios, String nombreBarrio) {
        NodoLista<BarrioAux> nodo = barrios.getLista();
        while (nodo != null) {
            if (nodo.getDato().nombre.equals(nombreBarrio)) {
                return nodo.getDato();
            }
            nodo = nodo.getSiguiente();
        }
        return null;
    }

    private class BarrioAux implements Comparable<BarrioAux> {
        String nombre;
        int capacidadTotal;
        int bicicletasTotal;
        
        public BarrioAux(String nombre) {
            this.nombre = nombre;
            this.capacidadTotal = 0;
            this.bicicletasTotal = 0;
        }
        
        @Override
        public int compareTo(BarrioAux otro) {
            return this.nombre.compareTo(otro.nombre);
        }
    }

@Override
public Retorno rankingTiposPorUso() {
    int[] contadores = new int[3];
    String[] tipos = {"URBANA", "MOUNTAIN", "ELECTRICA"};
    
    NodoLista<Bicicleta> nodo = bicicletas.getLista();
    
    while (nodo != null) {
        Bicicleta bici = nodo.getDato();
        int vecesAlquilada = bici.getVecesAlquilada();
        
        switch (bici.getTipo()) {
            case URBANA:
                contadores[0] += vecesAlquilada;
                break;
            case MOUNTAIN:
                contadores[1] += vecesAlquilada;
                break;
            case ELECTRICA:
                contadores[2] += vecesAlquilada;
                break;
        }
        
        nodo = nodo.getSiguiente();
    }
    
    for (int i = 0; i < 3; i++) {
        for (int j = i + 1; j < 3; j++) {
            boolean hayQueIntercambiar = false;
            
            if (contadores[j] > contadores[i]) {
                hayQueIntercambiar = true;
            } else if (contadores[j] == contadores[i] && tipos[j].compareTo(tipos[i]) < 0) {
                hayQueIntercambiar = true;
            }
            
            if (hayQueIntercambiar) {
                int tempContador = contadores[i];
                contadores[i] = contadores[j];
                contadores[j] = tempContador;
                
                String tempTipo = tipos[i];
                tipos[i] = tipos[j];
                tipos[j] = tempTipo;
            }
        }
    }
    
    String textoFinal = "";
    for (int i = 0; i < 3; i++) {
        if (i > 0) textoFinal += "|";
        textoFinal += tipos[i] + "#" + contadores[i];
    }
    
    return Retorno.ok(textoFinal);
}

@Override
public Retorno usuariosEnEspera(String nombreEstacion) {
    if (nombreEstacion == null || nombreEstacion.trim().isEmpty()) {
        return Retorno.ok("");
    }
    
    Estacion estacionBuscada = new Estacion(nombreEstacion, "", 0);
    NodoLista<Estacion> nodoEstacion = estaciones.obtenerElemento(estacionBuscada);
    
    if (nodoEstacion == null) {
        return Retorno.ok("");
    }
    
    Estacion estacion = nodoEstacion.getDato();
    
    String textoFinal = "";
    NodoCola<Usuario> usuarioActual = estacion.getColaEsperaAlquiler().getFrente();
    boolean esPrimero = true;
    
    while (usuarioActual != null) {
        if (!esPrimero) textoFinal += "|";
        textoFinal += usuarioActual.getDato().getCedula();
        esPrimero = false;
        usuarioActual = usuarioActual.getSiguiente();
    }
    
    return Retorno.ok(textoFinal);
}

@Override
public Retorno usuarioMayor() {
    if (usuarios.esVacia()) {
        return Retorno.ok("");
    }
    
    NodoLista<Usuario> nodo = usuarios.getLista();
    Usuario usuarioMayor = nodo.getDato();
    int maxAlquileres = usuarioMayor.getCantidadAlquileres();
    
    nodo = nodo.getSiguiente();
    
    while (nodo != null) {
        Usuario usuario = nodo.getDato();
        int alquileres = usuario.getCantidadAlquileres();
        
        if (alquileres > maxAlquileres) {
            usuarioMayor = usuario;
            maxAlquileres = alquileres;
        } else if (alquileres == maxAlquileres && 
                   usuario.getCedula().compareTo(usuarioMayor.getCedula()) < 0) {
            usuarioMayor = usuario;
        }
        
        nodo = nodo.getSiguiente();
    }
    
    return Retorno.ok(usuarioMayor.getCedula());
}

private void procesarColasAnclaje(Estacion estacion) {
    while (estacion.tieneEspacioLibre() && !estacion.getColaEsperaAnclaje().esVacia()) {
        
        Usuario usuarioEsperando = estacion.getColaEsperaAnclaje().dequeue();
        Bicicleta bici = usuarioEsperando.getBicicletaAlquilada();

        PilaNodos<Alquiler> pilaTemp = new PilaNodos<>();
        boolean encontrado = false;
        boolean vieneDeDeshacer = false;
        
        while (!historialAlquileres.esVacia() && !encontrado) {
            Alquiler alq = historialAlquileres.poptop();

            if (alq.getCedulaUsuario().equals(usuarioEsperando.getCedula()) && 
                bici.getCodigo().equals(alq.getCodigoBicicleta())) {

                if ("DESHECHO".equals(alq.getEstacionDestino())) {
                    vieneDeDeshacer = true;
                    
                    alq.setEstacionDestino(estacion.getNombre());
                    encontrado = true;
                }

                else if (alq.estaActivo()) {
                    vieneDeDeshacer = false;
                    alq.setEstacionDestino(estacion.getNombre());
                    encontrado = true;
                }
            }
            
            pilaTemp.push(alq);
        }

        while (!pilaTemp.esVacia()) {
            historialAlquileres.push(pilaTemp.poptop());
        }

        if (vieneDeDeshacer) {
            usuarioEsperando.decrementarAlquileres();
            bici.decrementarVecesAlquilada();
        }

        usuarioEsperando.setBicicletaAlquilada(null);

        if (!estacion.getColaEsperaAlquiler().esVacia()) {
            Usuario siguienteUsuario = estacion.getColaEsperaAlquiler().dequeue();
            
            siguienteUsuario.setBicicletaAlquilada(bici);
            siguienteUsuario.incrementarAlquileres();
            bici.incrementarVecesAlquilada();
            
            bici.setEstado(Bicicleta.Estado.ALQUILADA);
            bici.setEstacionActual(null);
            
            Alquiler nuevoAlquiler = new Alquiler(bici.getCodigo(), siguienteUsuario.getCedula(), estacion.getNombre());
            historialAlquileres.push(nuevoAlquiler);
            
        } else {
            bici.setEstado(Bicicleta.Estado.DISPONIBLE);
            bici.setEstacionActual(estacion);
            estacion.getBicicletasAncladas().agregarFinal(bici);
        }
    }
}

}