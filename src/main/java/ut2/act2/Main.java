package ut2.act2;

// IMPORTS
import java.util.Scanner;

public class Main {
    // Constante para el tiempo de espera
    final static int TIEMPO_ESPERA = 1250;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        int opcion = 0;

        // Creamos la BD creando una instancia de la clase Bd.java
        Bd bd = new Bd();

        // Mientras que salir sea false, seguir el bucle while
        while (!salir) {
            limpiarPantalla();
            System.out.print("\t\t.:TIENDA DB:.\n\n1. Crear venta\n2. Añadir stock\n3. Salir\n\nOpción: ");

            try {
                opcion = sc.nextInt();
            } catch (Exception e) {
                // Saltamos de línea para que no de errores de puntero
                sc.nextLine();

                System.err.println("Opción incorrecta\n");
                espera(0);
            }

            switch (opcion) {
                case 1:
                    int objeto = 0, cantidad = 0;

                    limpiarPantalla();

                    System.out.println("\t\t.:CREAR VENTA:.\n");
                    bd.listadoProductos();

                    // Recogemos el producto y la cantidad de la venta
                    try {
                        System.out.print("\nNumero producto: ");
                        objeto = sc.nextInt();
                        System.out.println("Cantidad: ");
                        cantidad = sc.nextInt();
                    } catch (Exception e) {
                        sc.nextLine();
                        System.err.println("Opción incorrecta");
                        espera(0);
                    }

                    // Si el producto existe y hay suficiente cantidad, entonces llamamos a la función de creación de venta
                    if (bd.existeProducto(objeto, cantidad)) {
                        bd.nuevaVenta();
                    } else {
                        System.out.println("\nEl producto no existe");
                        espera(0);
                    }

                    break;
                case 2:

                    break;
                case 3:
                    // Ponemos salir en true para romper el bucle while
                    salir = true;
                    break;
                default:
                    System.err.println("Opción incorrecta\n");
                    espera(0);
                    break;
            }
        }
    }

    /**
     * Esta función limpia la pantalla dependiendo del sistema operativo que tengas
     */
    public static void limpiarPantalla() {
        try {
            // Obtenemos el sistema operativo desde el que se ejecuta el programa
            String so = System.getProperty("os.name").toLowerCase();

            // Si es windows lanzamos el comando cls para borrar la pantalla
            if (so.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            // Si es Linux o Mac, lanzamos una secuencia de caracteres ANSI que limpia y borra la pantalla
            else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.err.println("Error al limpiar la pantalla.\n" + e.getMessage());
        }
    }

    /**
     * Esta función ejecuta un bloque de código que para la ejecución de espera TIEMPO_ESPERA segundos
     */
    public static void espera(int tiempo) {
        if (tiempo > 0) {
            try {
                Thread.sleep(tiempo);

            } catch (InterruptedException e) {
                System.err.println("No se ha podido hacer la pausa de " + tiempo);
            }
        } else {
            try {
                Thread.sleep(TIEMPO_ESPERA);

            } catch (InterruptedException e) {
                System.err.println("No se ha podido hacer la pausa de " + TIEMPO_ESPERA);
            }
        }
    }
}