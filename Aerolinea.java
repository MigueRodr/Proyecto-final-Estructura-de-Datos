import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Scanner;

/**
 * Clase principal Aerolinea
 * ─────────────────────────────────────────────────────────────────────────
 * Contiene el menú principal de consola y todas las operaciones del sistema.
 *
 * Estructuras de datos globales:
 *   - listaVuelos : ArrayList<Vuelo>  → lista de todos los vuelos registrados
 *   - historial   : Deque<String>     → pila LIFO para deshacer operaciones
 *
 * Cada Vuelo internamente tiene:
 *   - listaPasajeros : ArrayList<Pasajero>  → confirmados
 *   - listaEspera    : Queue<Pasajero>      → cola FIFO de espera
 *
 * Métodos recursivos implementados (Semana 3):
 *   1. buscarVueloPorCodigo     → búsqueda en lista de vuelos
 *   2. contarPasajerosEnVuelo   → conteo en lista de pasajeros
 *   3. contarPasajerosPorDestino→ conteo cruzando todos los vuelos
 *   4. buscarPasajeroPorDocumento → búsqueda en todos los vuelos
 * ─────────────────────────────────────────────────────────────────────────
 */
public class Aerolinea {

    // ── Estructuras de datos principales ──────────────────────
    private static ArrayList<Vuelo> listaVuelos    = new ArrayList<>();
    private static Deque<String>    historial       = new ArrayDeque<>(); // pila LIFO
    private static int              contadorId      = 1;
    private static Scanner          scanner         = new Scanner(System.in);

    // ══════════════════════════════════════════════════════════
    //  PUNTO DE ENTRADA
    // ══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1: menuVuelos();      break;
                case 2: menuPasajeros();   break;
                case 3: menuListaEspera(); break;
                case 4: menuHistorial();   break;
                case 5: menuReportes();    break;
                case 0: System.out.println("\n¡Hasta luego!\n"); break;
                default: System.out.println("\nOpción no válida. Intente de nuevo.\n");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN DE AEROLÍNEA    ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Gestionar Vuelos                 ║");
        System.out.println("║  2. Gestionar Pasajeros y Reservas   ║");
        System.out.println("║  3. Listas de Espera (Colas)         ║");
        System.out.println("║  4. Historial y Deshacer (Pilas)     ║");
        System.out.println("║  5. Reportes (Recursividad)          ║");
        System.out.println("║  0. Salir                            ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // ══════════════════════════════════════════════════════════
    //  MENÚ 1 — GESTIONAR VUELOS
    // ══════════════════════════════════════════════════════════
    private static void menuVuelos() {
        int opcion;
        do {
            System.out.println("\n--- Gestionar Vuelos ---");
            System.out.println("1. Registrar nuevo vuelo");
            System.out.println("2. Listar todos los vuelos");
            System.out.println("3. Buscar vuelo por código");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1: registrarVuelo();  break;
                case 2: listarVuelos();    break;
                case 3: buscarVueloMenu(); break;
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /** Solicita datos y registra un nuevo vuelo. */
    private static void registrarVuelo() {
        System.out.println("\n-- Registrar Vuelo --");
        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();

        if (buscarVueloPorCodigo(listaVuelos, codigo, 0) != null) {
            System.out.println("Ya existe un vuelo con ese código.");
            return;
        }

        System.out.print("Origen: ");
        String origen = scanner.nextLine().trim();
        System.out.print("Destino: ");
        String destino = scanner.nextLine().trim();
        int capacidad = leerEntero("Capacidad máxima: ");

        Vuelo v = new Vuelo(codigo, origen, destino, capacidad);
        listaVuelos.add(v);

        // Registrar en historial (pila)
        historial.push("VUELO_REGISTRADO:" + codigo);
        System.out.println("✔ Vuelo " + codigo + " registrado correctamente.");
    }

    /** Lista todos los vuelos registrados. */
    private static void listarVuelos() {
        System.out.println("\n-- Lista de Vuelos --");
        if (listaVuelos.isEmpty()) {
            System.out.println("No hay vuelos registrados.");
            return;
        }
        for (Vuelo v : listaVuelos) {
            System.out.println(v);
        }
    }

    /** Busca un vuelo por código y lo muestra. */
    private static void buscarVueloMenu() {
        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        Vuelo v = buscarVueloPorCodigo(listaVuelos, codigo, 0);
        if (v != null) {
            System.out.println("Vuelo encontrado:");
            System.out.println(v);
        } else {
            System.out.println("No se encontró el vuelo con código: " + codigo);
        }
    }

    // ══════════════════════════════════════════════════════════
    //  MENÚ 2 — GESTIONAR PASAJEROS Y RESERVAS
    // ══════════════════════════════════════════════════════════
    private static void menuPasajeros() {
        int opcion;
        do {
            System.out.println("\n--- Gestionar Pasajeros y Reservas ---");
            System.out.println("1. Registrar pasajero en un vuelo");
            System.out.println("2. Cancelar reserva de pasajero");
            System.out.println("3. Mostrar pasajeros confirmados de un vuelo");
            System.out.println("4. Mostrar lista de espera de un vuelo");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1: registrarPasajero();         break;
                case 2: cancelarReserva();            break;
                case 3: mostrarPasajerosConfirmados(); break;
                case 4: mostrarListaEspera();          break;
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /**
     * Registra un pasajero en un vuelo.
     * Si hay cupo → lo agrega a lista confirmada.
     * Si está lleno → lo agrega a la cola de espera.
     * En ambos casos registra la operación en la pila de historial.
     */
    private static void registrarPasajero() {
        System.out.println("\n-- Registrar Pasajero --");
        if (listaVuelos.isEmpty()) {
            System.out.println("No hay vuelos disponibles. Registre un vuelo primero.");
            return;
        }

        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
        if (vuelo == null) {
            System.out.println("No se encontró el vuelo: " + codigo);
            return;
        }

        System.out.print("Nombre del pasajero: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Número de documento: ");
        String documento = scanner.nextLine().trim();
        System.out.print("Nacionalidad: ");
        String nacionalidad = scanner.nextLine().trim();

        Pasajero p = new Pasajero(contadorId++, nombre, documento, nacionalidad);

        if (!vuelo.estaLleno()) {
            vuelo.agregarPasajero(p);
            historial.push("RESERVA:" + documento + ":" + codigo);
            System.out.println("✔ Pasajero " + nombre + " registrado en vuelo " + codigo + ".");
        } else {
            // Vuelo lleno → cola de espera (Queue FIFO)
            vuelo.agregarAEspera(p);
            historial.push("ESPERA:" + documento + ":" + codigo);
            System.out.println("⚠ Vuelo lleno. " + nombre + " fue agregado a la lista de espera.");
        }
    }

    /**
     * Cancela la reserva de un pasajero.
     * Si había alguien en cola de espera, Vuelo.cancelarReserva() lo mueve
     * automáticamente a la lista confirmada.
     * Registra la cancelación en la pila de historial.
     */
    private static void cancelarReserva() {
        System.out.println("\n-- Cancelar Reserva --");
        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
        if (vuelo == null) {
            System.out.println("No se encontró el vuelo: " + codigo);
            return;
        }

        System.out.print("Documento del pasajero: ");
        String documento = scanner.nextLine().trim();
        Pasajero cancelado = vuelo.cancelarReserva(documento);

        if (cancelado != null) {
            historial.push("CANCELACION:" + documento + ":" + codigo);
            System.out.println("✔ Reserva cancelada para: " + cancelado.getNombre());
        } else {
            System.out.println("No se encontró un pasajero con ese documento en el vuelo.");
        }
    }

    private static void mostrarPasajerosConfirmados() {
        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
        if (vuelo == null) { System.out.println("Vuelo no encontrado."); return; }
        System.out.println("Pasajeros confirmados en vuelo " + codigo + ":");
        vuelo.mostrarPasajeros();
    }

    private static void mostrarListaEspera() {
        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
        if (vuelo == null) { System.out.println("Vuelo no encontrado."); return; }
        System.out.println("Lista de espera del vuelo " + codigo + ":");
        vuelo.mostrarListaEspera();
    }

    // ══════════════════════════════════════════════════════════
    //  MENÚ 3 — LISTA DE ESPERA (COLAS)
    // ══════════════════════════════════════════════════════════
    private static void menuListaEspera() {
        int opcion;
        do {
            System.out.println("\n--- Listas de Espera (Colas) ---");
            System.out.println("1. Agregar pasajero a lista de espera manualmente");
            System.out.println("2. Procesar lista de espera (dar cupo al primero)");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1: agregarAEsperaManual(); break;
                case 2: procesarEspera();        break;
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /** Agrega manualmente un pasajero a la cola de espera de un vuelo. */
    private static void agregarAEsperaManual() {
        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
        if (vuelo == null) { System.out.println("Vuelo no encontrado."); return; }

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Documento: ");
        String documento = scanner.nextLine().trim();
        System.out.print("Nacionalidad: ");
        String nacionalidad = scanner.nextLine().trim();

        Pasajero p = new Pasajero(contadorId++, nombre, documento, nacionalidad);
        vuelo.agregarAEspera(p);
        historial.push("ESPERA:" + documento + ":" + codigo);
        System.out.println("✔ " + nombre + " agregado a la lista de espera del vuelo " + codigo + ".");
    }

    /**
     * Procesa manualmente la cola de espera de un vuelo:
     * saca al primer pasajero de la cola (FIFO) y lo mueve
     * a la lista confirmada si hay cupo disponible.
     */
    private static void procesarEspera() {
        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
        if (vuelo == null) { System.out.println("Vuelo no encontrado."); return; }

        Pasajero p = vuelo.procesarEspera();
        if (p != null) {
            historial.push("RESERVA:" + p.getDocumento() + ":" + codigo);
            System.out.println("✔ " + p.getNombre() + " fue movido de espera a confirmado en vuelo " + codigo + ".");
        } else if (vuelo.getListaEspera().isEmpty()) {
            System.out.println("No hay pasajeros en lista de espera para este vuelo.");
        } else {
            System.out.println("El vuelo aún está lleno, no se puede procesar la espera.");
        }
    }

    // ══════════════════════════════════════════════════════════
    //  MENÚ 4 — HISTORIAL Y DESHACER (PILAS)
    // ══════════════════════════════════════════════════════════
    private static void menuHistorial() {
        int opcion;
        do {
            System.out.println("\n--- Historial y Deshacer (Pilas) ---");
            System.out.println("1. Ver historial de operaciones");
            System.out.println("2. Deshacer última operación");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1: verHistorial();           break;
                case 2: deshacerUltimaOperacion(); break;
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /** Muestra todas las operaciones registradas en la pila (LIFO: la más reciente primero). */
    private static void verHistorial() {
        System.out.println("\n-- Historial de Operaciones --");
        if (historial.isEmpty()) {
            System.out.println("No hay operaciones registradas.");
            return;
        }
        int i = 1;
        for (String op : historial) {
            System.out.println("  " + i + ". " + formatearOperacion(op));
            i++;
        }
    }

    /**
     * Deshace la última operación registrada en la pila.
     *
     * Operaciones que se pueden deshacer:
     *   - RESERVA       → cancela la reserva del pasajero en el vuelo
     *   - CANCELACION   → vuelve a agregar el pasajero al vuelo (si hay cupo)
     *   - ESPERA        → saca al pasajero de la cola de espera
     *   - VUELO_REGISTRADO → elimina el vuelo si no tiene pasajeros
     */
    private static void deshacerUltimaOperacion() {
        if (historial.isEmpty()) {
            System.out.println("No hay operaciones para deshacer.");
            return;
        }

        // Leer el tope de la pila (LIFO)
        String operacion = historial.pop();
        String[] partes = operacion.split(":");
        String tipo = partes[0];

        System.out.println("Deshaciendo: " + formatearOperacion(operacion));

        switch (tipo) {
            case "RESERVA": {
                // Deshacer una reserva = cancelar al pasajero del vuelo
                String documento = partes[1];
                String codigo    = partes[2];
                Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
                if (vuelo != null) {
                    Pasajero p = vuelo.cancelarReserva(documento);
                    if (p != null) {
                        System.out.println("✔ Reserva de " + p.getNombre() + " deshecha del vuelo " + codigo + ".");
                    } else {
                        System.out.println("No se encontró el pasajero para deshacer.");
                    }
                }
                break;
            }
            case "CANCELACION": {
                // Deshacer una cancelación = volver a agregar al pasajero
                // Buscamos al pasajero entre todos los vuelos o lo creamos de nuevo
                String documento = partes[1];
                String codigo    = partes[2];
                Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
                if (vuelo != null && !vuelo.estaLleno()) {
                    // Buscar al pasajero en algún otro vuelo para recuperar sus datos
                    Pasajero encontrado = buscarPasajeroPorDocumento(listaVuelos, documento, 0, 0);
                    if (encontrado != null) {
                        vuelo.agregarPasajero(encontrado);
                        System.out.println("✔ Pasajero " + encontrado.getNombre() + " restaurado en vuelo " + codigo + ".");
                    } else {
                        System.out.println("No se pudieron recuperar los datos del pasajero. Regístrelo manualmente.");
                    }
                } else {
                    System.out.println("No se puede restaurar: vuelo lleno o no encontrado.");
                }
                break;
            }
            case "ESPERA": {
                // Deshacer un ingreso a lista de espera = sacar de la cola
                String documento = partes[1];
                String codigo    = partes[2];
                Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
                if (vuelo != null) {
                    // Reconstruir la cola sin ese pasajero
                    java.util.Queue<Pasajero> nueva = new java.util.LinkedList<>();
                    boolean encontrado = false;
                    for (Pasajero p : vuelo.getListaEspera()) {
                        if (!encontrado && p.getDocumento().equals(documento)) {
                            encontrado = true; // saltamos a este pasajero (lo removemos)
                        } else {
                            nueva.offer(p);
                        }
                    }
                    vuelo.getListaEspera().clear();
                    vuelo.getListaEspera().addAll(nueva);
                    System.out.println(encontrado
                        ? "✔ Pasajero removido de la lista de espera."
                        : "No se encontró el pasajero en la lista de espera.");
                }
                break;
            }
            case "VUELO_REGISTRADO": {
                // Deshacer registro de vuelo = eliminarlo si está vacío
                String codigo = partes[1];
                Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
                if (vuelo != null) {
                    if (vuelo.getListaPasajeros().isEmpty() && vuelo.getListaEspera().isEmpty()) {
                        listaVuelos.remove(vuelo);
                        System.out.println("✔ Vuelo " + codigo + " eliminado del sistema.");
                    } else {
                        System.out.println("No se puede deshacer: el vuelo ya tiene pasajeros registrados.");
                        historial.push(operacion); // devolvemos a la pila
                    }
                }
                break;
            }
            default:
                System.out.println("Tipo de operación desconocido: " + tipo);
        }
    }

    /** Convierte el formato interno de la pila a texto legible. */
    private static String formatearOperacion(String op) {
        String[] p = op.split(":");
        switch (p[0]) {
            case "RESERVA":          return "RESERVA   → Pasajero doc=" + p[1] + " en vuelo " + p[2];
            case "CANCELACION":      return "CANCELACIÓN → Pasajero doc=" + p[1] + " en vuelo " + p[2];
            case "ESPERA":           return "ESPERA    → Pasajero doc=" + p[1] + " en vuelo " + p[2];
            case "VUELO_REGISTRADO": return "VUELO REGISTRADO → " + p[1];
            default:                 return op;
        }
    }

    // ══════════════════════════════════════════════════════════
    //  MENÚ 5 — REPORTES CON RECURSIVIDAD
    // ══════════════════════════════════════════════════════════
    private static void menuReportes() {
        int opcion;
        do {
            System.out.println("\n--- Reportes (Recursividad) ---");
            System.out.println("1. Contar pasajeros de un vuelo (recursivo)");
            System.out.println("2. Contar pasajeros que van a un destino (recursivo)");
            System.out.println("3. Buscar pasajero por documento en todos los vuelos (recursivo)");
            System.out.println("0. Volver");
            opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1: reporteContarPasajerosVuelo();    break;
                case 2: reporteContarPorDestino();         break;
                case 3: reporteBuscarPorDocumento();       break;
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void reporteContarPasajerosVuelo() {
        System.out.print("Código del vuelo: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        Vuelo vuelo = buscarVueloPorCodigo(listaVuelos, codigo, 0);
        if (vuelo == null) { System.out.println("Vuelo no encontrado."); return; }
        int total = contarPasajerosEnVuelo(vuelo.getListaPasajeros(), 0);
        System.out.println("Total de pasajeros confirmados en vuelo " + codigo + ": " + total);
    }

    private static void reporteContarPorDestino() {
        System.out.print("Destino a consultar: ");
        String destino = scanner.nextLine().trim();
        int total = contarPasajerosPorDestino(listaVuelos, destino, 0);
        System.out.println("Total de pasajeros que viajan a " + destino + ": " + total);
    }

    private static void reporteBuscarPorDocumento() {
        System.out.print("Número de documento a buscar: ");
        String documento = scanner.nextLine().trim();
        Pasajero encontrado = buscarPasajeroPorDocumento(listaVuelos, documento, 0, 0);
        if (encontrado != null) {
            System.out.println("Pasajero encontrado:");
            System.out.println(encontrado);
        } else {
            System.out.println("No se encontró ningún pasajero con documento: " + documento);
        }
    }

    // ══════════════════════════════════════════════════════════
    //  MÉTODOS RECURSIVOS LINEALES
    //  (una sola llamada recursiva por nivel)
    // ══════════════════════════════════════════════════════════

    /**
     * RECURSIVO 1 — Busca un vuelo por código en la lista de vuelos.
     *
     * Caso base:    el índice llega al final de la lista → retorna null (no encontrado).
     * Caso recursivo: si el vuelo en la posición actual tiene el código buscado
     *                 → retornarlo; si no → avanzar al siguiente índice.
     *
     * @param lista  Lista global de vuelos.
     * @param codigo Código a buscar.
     * @param indice Posición actual (inicia en 0).
     * @return El Vuelo encontrado, o null si no existe.
     */
    public static Vuelo buscarVueloPorCodigo(ArrayList<Vuelo> lista, String codigo, int indice) {
        // Caso base: se recorrió toda la lista sin encontrar
        if (indice >= lista.size()) return null;
        // Si el vuelo actual coincide, lo retornamos
        if (lista.get(indice).getCodigoVuelo().equals(codigo)) return lista.get(indice);
        // Caso recursivo: buscar en el resto de la lista
        return buscarVueloPorCodigo(lista, codigo, indice + 1);
    }

    /**
     * RECURSIVO 2 — Cuenta los pasajeros confirmados en un vuelo.
     *
     * Caso base:    el índice llega al tamaño de la lista → retorna 0.
     * Caso recursivo: cuenta 1 por el pasajero actual + llama con índice + 1.
     *
     * @param lista  Lista de pasajeros del vuelo.
     * @param indice Posición actual (inicia en 0).
     * @return Número total de pasajeros.
     */
    public static int contarPasajerosEnVuelo(ArrayList<Pasajero> lista, int indice) {
        // Caso base: no hay más pasajeros que contar
        if (indice >= lista.size()) return 0;
        // Caso recursivo: 1 (pasajero actual) + el conteo del resto
        return 1 + contarPasajerosEnVuelo(lista, indice + 1);
    }

    /**
     * RECURSIVO 3 — Cuenta los pasajeros que van a un destino dado,
     *               sumando todos los vuelos de la lista.
     *
     * Caso base:    el índice llega al final de la lista de vuelos → retorna 0.
     * Caso recursivo: si el vuelo actual va al destino buscado, suma sus pasajeros
     *                 + llamada con el siguiente vuelo; si no, solo avanza.
     *
     * @param listaV  Lista global de vuelos.
     * @param destino Destino a buscar.
     * @param indice  Posición actual en la lista de vuelos (inicia en 0).
     * @return Total de pasajeros que van a ese destino.
     */
    public static int contarPasajerosPorDestino(ArrayList<Vuelo> listaV, String destino, int indice) {
        // Caso base: se recorrieron todos los vuelos
        if (indice >= listaV.size()) return 0;

        Vuelo actual = listaV.get(indice);
        int pasajerosDeEsteVuelo = 0;

        // Si este vuelo va al destino buscado, contamos sus pasajeros recursivamente
        if (actual.getDestino().equalsIgnoreCase(destino)) {
            pasajerosDeEsteVuelo = contarPasajerosEnVuelo(actual.getListaPasajeros(), 0);
        }

        // Caso recursivo: sumar este vuelo + el resto de vuelos
        return pasajerosDeEsteVuelo + contarPasajerosPorDestino(listaV, destino, indice + 1);
    }

    /**
     * RECURSIVO 4 — Busca un pasajero por documento recorriendo todos los vuelos.
     *
     * Caso base 1: se recorrieron todos los vuelos → retorna null.
     * Caso base 2: se recorrieron todos los pasajeros del vuelo actual → avanza al siguiente vuelo.
     * Caso recursivo: si el pasajero actual tiene el documento buscado → retornarlo;
     *                 si no → avanzar al siguiente pasajero.
     *
     * @param listaV     Lista global de vuelos.
     * @param documento  Documento a buscar.
     * @param vueloIdx   Índice del vuelo actual (inicia en 0).
     * @param pasajeroIdx Índice del pasajero dentro del vuelo actual (inicia en 0).
     * @return El Pasajero encontrado, o null si no existe.
     */
    public static Pasajero buscarPasajeroPorDocumento(
            ArrayList<Vuelo> listaV, String documento, int vueloIdx, int pasajeroIdx) {

        // Caso base 1: se terminaron los vuelos
        if (vueloIdx >= listaV.size()) return null;

        ArrayList<Pasajero> pasajeros = listaV.get(vueloIdx).getListaPasajeros();

        // Caso base 2: se terminaron los pasajeros de este vuelo → pasar al siguiente vuelo
        if (pasajeroIdx >= pasajeros.size()) {
            return buscarPasajeroPorDocumento(listaV, documento, vueloIdx + 1, 0);
        }

        // Si el pasajero actual tiene el documento buscado, lo retornamos
        if (pasajeros.get(pasajeroIdx).getDocumento().equals(documento)) {
            return pasajeros.get(pasajeroIdx);
        }

        // Caso recursivo: buscar en el siguiente pasajero del mismo vuelo
        return buscarPasajeroPorDocumento(listaV, documento, vueloIdx, pasajeroIdx + 1);
    }

    // ══════════════════════════════════════════════════════════
    //  UTILIDADES
    // ══════════════════════════════════════════════════════════

    /** Lee un entero desde consola con manejo de errores. */
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }
    }
}
