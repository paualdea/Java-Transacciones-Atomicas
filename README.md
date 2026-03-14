# Acceso a Datos: Transacciones Atómicas con JDBC

Este proyecto es una aplicación **Java** desarrollada como parte del **Enunciado 2: "Transacción atómica con commit y rollback"** de la Unidad de Trabajo 2 (UT2) del módulo **Acceso a Datos**.

La aplicación consiste en un sistema de ventas que usa transacciones atómicas para asegurar la integridad de los registros en la BD, o se confirman ambas operaciones, o se revierten por completo ante cualquier fallo.

## Características Principales

* **Atomicidad (ACID)**: Aplicación de transacciones atómicas, asegurando que la BD nunca quede con referencias vacías o inconsistencias.
* **Control de Transacciones**: Eliminación del `AutoCommit` para poder gestionar manualmente los cambios sobre la BD, en caso de fallo, revertimos los cambios con `rollback()`.
* **Seguridad con PreparedStatement**: Ejecución de operaciones sobre la BD usando `PreparedStatment` para evitar problemas de seguridad (inyecciones SQL) y tener más control.
* **Robustez en el código**: Control de excepciones para detectar errores SQL, de stock o identificadores que no existan.

## Estructura de la Base de Datos

El proyecto utiliza la base de datos `tiendaDB` con las siguientes tablas relacionadas:

### Tabla: `PRODUCTOS`
| Columna | Tipo de dato | Descripción |
| :--- | :--- | :--- |
| `id_producto` | `INT` | Clave primaria identificadora del producto. |
| `nombre` | `VARCHAR(100)` | Nombre del producto. |
| `stock` | `INT` | Cantidad disponible en almacén. |

### Tabla: `VENTAS`
| Columna | Tipo de dato | Descripción |
| :--- | :--- | :--- |
| `id_venta` | `INT` | Clave primaria de la venta. |
| `id_producto` | `INT` | Referencia al producto vendido (FK). |
| `unidades` | `INT` | Cantidad de unidades vendidas. |
| `fecha` | `DATE` | Fecha de la operación. |

## Estructura del Proyecto

* **`Main.java`**: Ejecuta el menú para realizar las acciones sobre la BD.
* **`Bd.java`**: Gestiona la creación y gestión de todas las operaciones sobre la BD.

## Instrucciones de Uso

Para ejecutar este programa, es necesario descargar el código fuente o el .jar de las releases.

---
Este proyecto sirve como evidencia del aprendizaje sobre el control de transacciones y la integridad referencial en entornos Java, de la asignatura ****
