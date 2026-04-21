# 🚲 Sistema de Autogestión de Bicicletas — Obligatorio AED

Sistema de gestión de alquiler de bicicletas desarrollado como trabajo obligatorio para la materia **Algoritmos y Estructuras de Datos (AED)**.

Implementa un sistema completo de autogestión con estaciones, usuarios, bicicletas y alquileres, utilizando **estructuras de datos implementadas desde cero** (sin usar colecciones de Java).

---

## 🚀 Funcionalidades

- Registro de estaciones, usuarios y bicicletas
- Asignación de bicicletas a estaciones
- Alquiler y devolución de bicicletas
- Marcado de bicicletas en mantenimiento y reparación
- Deshacer últimos retiros (usando Pila)
- Lista de espera de usuarios (usando Cola)
- Consultas y reportes:
  - Listar usuarios y bicicletas en depósito
  - Información del mapa de estaciones
  - Estaciones con disponibilidad
  - Ocupación promedio por barrio
  - Ranking de tipos de bicicleta por uso
  - Usuario con mayor cantidad de alquileres

---

## 🏗️ Estructura del proyecto

```
Obl-AED-ago25/
├── src/
│   ├── dominio/                    # Entidades del sistema
│   │   ├── Alquiler.java
│   │   ├── Bicicleta.java
│   │   ├── Estacion.java
│   │   └── Usuario.java
│   ├── sistemaAutogestion/         # Lógica principal
│   │   ├── IObligatorio.java       # Interfaz con todas las operaciones requeridas
│   │   ├── Retorno.java            # Objeto de retorno estandarizado
│   │   └── Sistema.java            # Implementación del sistema
│   └── tads/                       # Estructuras de datos implementadas desde cero
│       ├── TADLista/               # Lista simplemente enlazada
│       ├── TADListaDoble/          # Lista doblemente enlazada
│       ├── TADPila/                # Pila con nodos
│       └── TADCola/                # Cola con nodos
└── test/
    └── sistemaAutogestion/         # Tests unitarios por funcionalidad
```

---

## 🧠 Estructuras de datos implementadas

Todas las estructuras fueron implementadas manualmente usando nodos, sin utilizar `ArrayList`, `LinkedList`, `Stack` ni ninguna colección de Java.

| TAD | Uso en el sistema |
|---|---|
| **Lista simple** | Listado de bicicletas, estaciones y usuarios |
| **Lista doble** | Navegación bidireccional de registros |
| **Pila** | Deshacer últimos retiros de bicicletas |
| **Cola** | Lista de espera de usuarios para alquiler |

---

## 🧪 Tests

El proyecto incluye **20 tests unitarios** organizados en dos grupos:

**Grupo 2 — Operaciones del sistema**
- Crear sistema, registrar estaciones, usuarios y bicicletas
- Marcar en mantenimiento, reparar, eliminar estaciones
- Asignar bicicletas, alquilar, devolver
- Deshacer últimos retiros

**Grupo 3 — Consultas y reportes**
- Obtener usuario, listar usuarios y bicicletas
- Información del mapa, estaciones con disponibilidad
- Ocupación promedio por barrio
- Ranking de tipos por uso
- Usuarios en espera, usuario con mayor uso

---

## 🛠️ Tecnologías

- **Lenguaje:** Java
- **Testing:** JUnit
- **Paradigma:** Orientado a Objetos
- **Estructuras:** Implementación manual de TADs (Lista, Lista Doble, Pila, Cola)

---

## 👨‍💻 Autor

- [SebaaFischel](https://github.com/SebaaFischel)
