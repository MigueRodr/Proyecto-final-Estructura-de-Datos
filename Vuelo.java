import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase Vuelo
 * Representa un vuelo con su información básica, la lista de pasajeros
 * confirmados (ArrayList) y la cola de espera (Queue).
 *
 * Estructuras de datos usadas:
 *   - listaPasajeros : ArrayList<Pasajero> → acceso por índice, recorrido recursivo
 *   - listaEspera    : Queue<Pasajero>     → comportamiento FIFO (el primero en esperar
 *                                            es el primero en obtener cupo)
 */
public class Vuelo {

    // ── Atributos ──────────────────────────────────────────────
    private String codigoVuelo;
    private String origen;
    private String destino;
    private int capacidadMaxima;
    private ArrayList<Pasajero> listaPasajeros; // Semana 2
    private Queue<Pasajero>    listaEspera;     // Semana 3

    // ── Constructor ────────────────────────────────────────────
    public Vuelo(String codigoVuelo, String origen, String destino, int capacidadMaxima) {
        this.codigoVuelo     = codigoVuelo;
        this.origen          = origen;
        this.destino         = destino;
        this.capacidadMaxima = capacidadMaxima;
        this.listaPasajeros  = new ArrayList<>();
        this.listaEspera     = new LinkedList<>();
    }

    // ── Getters ────────────────────────────────────────────────
    public String getCodigoVuelo()              { return codigoVuelo; }
    public String getOrigen()                   { return origen; }
    public String getDestino()                  { return destino; }
    public int getCapacidadMaxima()             { return capacidadMaxima; }
    public ArrayList<Pasajero> getListaPasajeros() { return listaPasajeros; }
    public Queue<Pasajero>     getListaEspera() { return listaEspera; }

    // ── Métodos de negocio ─────────────────────────────────────

    /** Retorna true si el vuelo ya alcanzó su capacidad máxima. */
    public boolean estaLleno() {
        return listaPasajeros.size() >= capacidadMaxima;
    }

    /**
     * Agrega un pasajero a la lista confirmada si hay cupo.
     * @return true si se agregó, false si el vuelo está lleno.
     */
    public boolean agregarPasajero(Pasajero p) {
        if (estaLleno()) return false;
        listaPasajeros.add(p);
        return true;
    }

    /**
     * Agrega un pasajero a la cola de espera (FIFO).
     * Se usa cuando el vuelo está lleno.
     */
    public void agregarAEspera(Pasajero p) {
        listaEspera.offer(p);
    }

    /**
     * Cancela la reserva de un pasajero buscándolo por documento.
     * Si había alguien en lista de espera, lo mueve automáticamente
     * a la lista confirmada (libera el cupo).
     *
     * @return el Pasajero cancelado, o null si no se encontró.
     */
    public Pasajero cancelarReserva(String documento) {
        for (int i = 0; i < listaPasajeros.size(); i++) {
            if (listaPasajeros.get(i).getDocumento().equals(documento)) {
                Pasajero cancelado = listaPasajeros.remove(i);
                // Si hay alguien en espera, se le da el cupo liberado
                if (!listaEspera.isEmpty()) {
                    Pasajero siguiente = listaEspera.poll(); // dequeue FIFO
                    listaPasajeros.add(siguiente);
                    System.out.println("  → El pasajero en espera "
                        + siguiente.getNombre() + " fue movido a lista confirmada.");
                }
                return cancelado;
            }
        }
        return null; // no se encontró el pasajero
    }

    /**
     * Procesa manualmente el primer pasajero de la lista de espera
     * (solo si hay cupo disponible).
     * @return el Pasajero procesado, o null si no había cupo o lista vacía.
     */
    public Pasajero procesarEspera() {
        if (listaEspera.isEmpty() || estaLleno()) return null;
        Pasajero p = listaEspera.poll();
        listaPasajeros.add(p);
        return p;
    }

    /** Muestra en consola los pasajeros confirmados. */
    public void mostrarPasajeros() {
        if (listaPasajeros.isEmpty()) {
            System.out.println("  (No hay pasajeros confirmados en este vuelo)");
        } else {
            for (Pasajero p : listaPasajeros) {
                System.out.println(p);
            }
        }
    }

    /** Muestra en consola la cola de espera. */
    public void mostrarListaEspera() {
        if (listaEspera.isEmpty()) {
            System.out.println("  (No hay pasajeros en lista de espera)");
        } else {
            int pos = 1;
            for (Pasajero p : listaEspera) {
                System.out.println("  Posición " + pos + ": " + p.getNombre()
                    + " | Documento: " + p.getDocumento());
                pos++;
            }
        }
    }

    // ── toString ───────────────────────────────────────────────
    @Override
    public String toString() {
        return "  Código: " + codigoVuelo
             + " | Origen: " + origen
             + " | Destino: " + destino
             + " | Ocupación: " + listaPasajeros.size() + "/" + capacidadMaxima
             + " | En espera: " + listaEspera.size();
    }
}
