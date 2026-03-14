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
            Main.espera(0);
        }
    }

    /**
     * Función para crear una venta cómo transacción indivisible

     * @param id
     * Recibimos el ID del producto a vender
     * @param cantidad
     * Recibimos la cantidad del producto a vender
     */
    public void nuevaVenta (int id, int cantidad) {
        // Creamos la sentencia que crea la venta, añadiendo la fecha con date('now')
        String venta = "INSERT INTO VENTAS (id_producto, unidades, fecha) VALUES (?, ?, date('now'));";
        // Creamos la sentencia que actualiza el stock tras hacer la venta
        String stock = "UPDATE PRODUCTOS SET stock = stock - ? WHERE id_producto = ?;";

        // Crearemos un try-with-resoures anidado, donde primero desactivamos el auto-commit para evitar inserciones peligrosas
        try (Connection c = DriverManager.getConnection(url)) {
            // Desactivamos auto-commit
            c.setAutoCommit(false);

            // Creamos try-with-resources anidado
            try (PreparedStatement psVenta = c.prepareStatement(venta);
                PreparedStatement psStock = c.prepareStatement(stock))
            {
                // Bindeamos los valores a la sentencia de venta
                psVenta.setInt(1, id);
                psVenta.setInt(2, cantidad);
                psVenta.executeUpdate();

                // Bindeamos los valores a la sentencia del stock
                psStock.setInt(1, cantidad);
                psStock.setInt(2, id);
                psStock.executeUpdate();

                // Si llegamos aquí sin errores, podemos confirmar la transacción manualmente
                c.commit();
                System.out.println("\nTransacción indivisible completada");
                Main.espera(3000);
            }
            // En caso de que falle la transacción indivisible, cancelamos...
            catch (SQLException e) {
                System.err.println("\nError en la venta, deshaciendo...");
                // Hacemos un rollback() para revertir la inserción peligrosa (en caso de haberse completado)
                c.rollback();
                Main.espera(3000);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            Main.espera(0);
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
                System.out.println("ID " + rs.getInt(1) + " - " + rs.getString(2) + ", " + rs.getInt(3) + " unidades");
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
        // Obtenemos el número real de stock del producto
        String sentencia2 = "SELECT stock FROM PRODUCTOS WHERE id_producto = ?;";

        // Estructura try-with-resources
        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sentencia);
             PreparedStatement ps2 = c.prepareStatement(sentencia2)
        ) {
            // Bindeamos los datos a las sentencias y ejecutamos el ResultSet
            ps.setInt(1, id);
            ps2.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Si el producto existe, comprobamos si hay la cantidad suficiente
                ResultSet rs2 = ps2.executeQuery();

                // Si hay 1 usuario con ese ID y hay la suficiente cantidad demandada, entonces...
                if (rs.getInt(1) > 0 && (cantidad > 0 && rs2.getInt(1) >= cantidad)) {
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
