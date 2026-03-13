package ut2.act2;

// IMPORTS
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bd {
    // Dirección BD SQLite
    final String url = "jdbc:sqlite:tiendaDB.db";

    /**
     * Función constructora de la clase, que sirve para crear o identificar las tablas de la BD
     */
    public Bd () {
        // Creamos la sentencia para crear la TABLA productos en la BD
        String productos = "CREATE TABLE IF NOT EXISTS PRODUCTOS(id_producto INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(100), stock INTEGER);";
        String ventas = "CREATE TABLE IF NOT EXISTS VENTAS(id_venta INTEGER PRIMARY KEY AUTOINCREMENT, unidades INTEGER, fecha DATE, FOREIGN KEY(id_producto) REFERENCES PRODUCTOS(id_producto));";

        // Implementamos un try-with-resources para no desperdiciar recursos
        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(productos);
             PreparedStatement ps2 = c.prepareStatement(ventas))
        {
            // Ejecutamos la creación de las tablas siguiendo las sentencias
            ps.executeUpdate();
            ps2.executeUpdate();
        }
        // En caso de que falle la conexión a la BD, controlamos la excepción (SQLException)
        catch (SQLException e) {
            // Mandamos el error usando System.err para no ensuciar la salida
            System.err.println("Error SQL: " + e.getMessage());
        }
    }
}
