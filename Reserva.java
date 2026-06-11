/**
 * Clase Reserva
 * Representa una operación realizada en el sistema: puede ser una reserva
 * confirmada, una entrada a lista de espera o una cancelación.
 *
 * Se usa en la pila de historial (Deque<Reserva>) de Aerolinea
 * para poder deshacer la última operación realizada.
 *
 * Atributos:
 *   - pasajero : el pasajero involucrado en la operación
 *   - vuelo    : el vuelo al que pertenece la operación
 *   - tipo     : "RESERVA", "ESPERA" o "CANCELACION"
 */
public class Reserva {

    // ── Atributos ──────────────────────────────────────────────
    private Pasajero pasajero;
    private Vuelo    vuelo;
    private String   tipo;

    // ── Constructor ────────────────────────────────────────────
    public Reserva(Pasajero pasajero, Vuelo vuelo, String tipo) {
        this.pasajero = pasajero;
        this.vuelo    = vuelo;
        this.tipo     = tipo;
    }

    // ── Getters ────────────────────────────────────────────────
    public Pasajero getPasajero() { return pasajero; }
    public Vuelo    getVuelo()    { return vuelo; }
    public String   getTipo()     { return tipo; }

    // ── toString ───────────────────────────────────────────────
    @Override
    public String toString() {
        return "  " + tipo
             + " → Pasajero: " + pasajero.getNombre()
             + " | Documento: " + pasajero.getDocumento()
             + " | Vuelo: " + vuelo.getCodigoVuelo();
    }
}
