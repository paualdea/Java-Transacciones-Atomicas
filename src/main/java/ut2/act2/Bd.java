package ut2.act2;

// IMPORTS
import java.sql.*;

public class Bd {
    // Dirección BD SQLite
    final String url = "jdbc:sqlite:tiendaDB.db";

    /**
     * Función constructora de la clase, que sirve para crear o identificar las tablas de la BD
     */
    public Bd () {
       // Implementamos un try-with-resources para no desperdiciar recursos
        try (Connection c = DriverManager.getConnection(url);
            Statement st = c.createStatement())
        {
            // Creamos las tablas PRODUCTOS y VENTAS
            st.executeUpdate("DROP TABLE IF EXISTS PRODUCTOS");
            st.executeUpdate("CREATE TABLE PRODUCTOS(id_producto INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(100), stock INTEGER)");
            st.executeUpdate("DROP TABLE IF EXISTS VENTAS");
            st.executeUpdate("CREATE TABLE VENTAS(id_venta INTEGER PRIMARY KEY AUTOINCREMENT, id_producto INTEGER, unidades INTEGER, fecha DATE, FOREIGN KEY(id_producto) REFERENCES PRODUCTOS(id_producto))");

            // Metemos los datos
            st.executeUpdate("INSERT INTO PRODUCTOS (nombre, stock) VALUES ('Escoba', 3)");
            st.executeUpdate("INSERT INTO PRODUCTOS (nombre, stock) VALUES ('Cubo metálico', 5)");
            st.executeUpdate("INSERT INTO PRODUCTOS (nombre, stock) VALUES ('Cuerda', 4)");
        }
        // En caso de que falle la conexión a la BD, controlamos la excepción (SQLException)
        catch (SQLException e) {
            // Mandamos el error usando System.err para no ensuciar la salida
            System.err.println("Error SQL: " + e.getMessage());
        }
    }



    /**
     * Esta función imprime por pantalla todos los productos y cantidades que hay, usando la tabla PRODUCTOS
     */
    public void listadoProductos () {
        String sentencia = "SELECT * FROM PRODUCTOS;";
        int numeroProductos = 0;

        // Estructura try-with-resources
        try (Connection c = DriverManager.getConnection(url);
            PreparedStatement ps = c.prepareStatement(sentencia);
             ResultSet rs = ps.executeQuery())
        {
            while(rs.next()){
                System.out.println("ID: " + rs.getInt(1) + " - " + rs.getString(2) + ". " + rs.getInt(3) + "unidades");
                numeroProductos++;
            }
            System.out.println("\n " + numeroProductos + " productos");
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
        }
    }

    /**
     * Función booleana que devuelve si el ID del producto existe en la tabla.

     * @param id
     * Recibe cómo parametro el ID introducido por el usuario
     * @return existe
     * Devuelve la variable boolean existe
     */
    public boolean existeProducto(int id, int cantidad) {
        boolean existe = false;
        // Contamos el número de usuarios con ese ID (1)
        String sentencia = "SELECT count(*) FROM PRODUCTOS WHERE id_producto = ?;";
        // Obtenemos el numero real de stock del producto
        String sentencia2 = "SELECT stock FROM PRODUCTOS WHERE id_producto = ?;";

        // Estructura try-with-resources
        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sentencia);
             PreparedStatement ps2 = c.prepareStatement(sentencia2);
        ) {
            // Bindeamos los datos a las sentencias y ejecutamos el ResultSet
            ps.setInt(1, id);
            ps2.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Si el producto existe, comprobamos si hay la cantidad suficiente
                ResultSet rs2 = ps2.executeQuery();

                // Si hay 1 usuario con ese ID y hay la suficiente cantidad demandada, entonces...
                if (rs.getInt(1) > 0 && rs2.getInt(1) >= cantidad) {
                    existe = true;
                }
                // Si existe, pero no hay suficiente cantidad, entonces...
                else if (rs.getInt(1) > 0) {
                    System.out.println("\nNo hay suficiente stock");
                    Main.espera(3000);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
        }

        return existe;
    }
}
