/**
 * Clase Pasajero
 * Representa a una persona que puede ser registrada en un vuelo.
 *
 * Atributos:
 *   - id          : identificador único autoincremental
 *   - nombre      : nombre completo del pasajero
 *   - documento   : número de documento de identidad (cédula, pasaporte, etc.)
 *   - nacionalidad: país de origen del pasajero
 */
public class Pasajero {

    // ── Atributos ──────────────────────────────────────────────
    private int id;
    private String nombre;
    private String documento;
    private String nacionalidad;

    // ── Constructor ────────────────────────────────────────────
    public Pasajero(int id, String nombre, String documento, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.nacionalidad = nacionalidad;
    }

    // ── Getters ────────────────────────────────────────────────
    public int getId()             { return id; }
    public String getNombre()      { return nombre; }
    public String getDocumento()   { return documento; }
    public String getNacionalidad(){ return nacionalidad; }

    // ── toString ───────────────────────────────────────────────
    @Override
    public String toString() {
        return "  ID: " + id
             + " | Nombre: " + nombre
             + " | Documento: " + documento
             + " | Nacionalidad: " + nacionalidad;
    }
}
