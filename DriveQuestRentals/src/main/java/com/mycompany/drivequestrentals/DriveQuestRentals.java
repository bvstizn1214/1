package com.mycompany.drivequestrentals;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class DriveQuestRentals {
    private static GestionVehiculos gestionVehiculos = new GestionVehiculos();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    int opcion;
    
    do {
        try {
            System.out.println("\n=== Sistema DriveQuest Rentals ===");
            System.out.println("1. Gestión de Vehículos");
            System.out.println("2. Facturación");
            System.out.println("3. Limpiar datos en memoria"); // Nueva opción
            System.out.println("4. Salir");
            System.out.print("\nSeleccione una opción: ");
            
            opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1:
                    mostrarMenuGestionVehiculos();
                    break;
                case 2:
                    mostrarMenuFacturacion();
                    break;
                case 3:
                    gestionVehiculos.limpiarDatosEnMemoria();// Llama al nuevo método
                    break;
                case 4:
                    System.out.println("¡Gracias por usar el Sistema DriveQuest Rentals!");
                    return;
                default:
                    System.out.println("Opción inválida. Por favor, intente nuevamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor, ingrese un número válido.");
            opcion = 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            opcion = 0;
        }
    } while (opcion != 4);
    
    scanner.close();
}
    
    private static void mostrarMenuGestionVehiculos() {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Vehículos ===");
            System.out.println("1. Agregar Nuevo Vehículo");
            System.out.println("2. Listar Vehículos");
            System.out.println("3. Listar Vehículos con Arriendos Largos");
            System.out.println("4. Cargar Vehículos desde CSV");
            System.out.println("5. Volver al Menú Principal");
            System.out.print("\nSeleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        agregarVehiculo();
                        break;
                    case 2:
                        listarVehiculos();
                        break;
                    case 3:
                        listarVehiculosArriendoLargo();
                        break;
                    case 4:
                        cargarVehiculosDesdeCSV();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Opción inválida. Por favor, intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número válido.");
                opcion = 0;
            }
        } while (opcion != 5);
    }
    
     private static void mostrarMenuFacturacion() {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Facturación ===");
            System.out.println("1. Generar y Mostrar Boleta de Arriendo");
            System.out.println("2. Guardar Boletas en Archivo");
            System.out.println("3. Volver al Menú Principal");
            System.out.print("\nSeleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        generarBoleta();
                        break;
                    case 2:
                        guardarBoletasEnArchivo();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Opción inválida. Por favor, intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número válido.");
                opcion = 0;
            }
        } while (opcion != 3);
    }
    
    private static void guardarBoletasEnArchivo() {
        System.out.println("\n=== Guardar Boletas en Archivo ===");
        
        String nombreArchivo = "boletas_" + 
                             LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                             ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            List<Vehiculo> vehiculos = gestionVehiculos.listarVehiculos();
            
            if (vehiculos.isEmpty()) {
                System.out.println("No hay vehículos registrados para generar boletas.");
                return;
            }

            writer.println("=== REGISTRO DE BOLETAS DE ARRIENDO ===");
            writer.println("Fecha de generación: " + 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Usuario: " + System.getProperty("user.name"));
            writer.println("----------------------------------------\n");

            int numeroFactura = 1;
            double totalGeneral = 0;

            for (Vehiculo vehiculo : vehiculos) {
                writer.println("BOLETA N°: " + numeroFactura);
                writer.println("Fecha: " + LocalDate.now());
                writer.println("----------------------------------------");
                writer.println("DATOS DEL VEHÍCULO:");
                writer.println(vehiculo.mostrarDatos());
                writer.println("----------------------------------------");
                writer.println("CÁLCULO DEL ARRIENDO:");
                
                double subtotal = vehiculo.getValorDiario() * vehiculo.getDiasArriendo();
                double descuento;
                
                // Determinar el descuento según el tipo de vehículo
                if (vehiculo instanceof VehiculoCarga) {
                    descuento = subtotal * 0.07; // 7% para vehículos de carga
                    writer.println("Descuento Vehículo de Carga (7%)");
                } else {
                    descuento = subtotal * 0.12; // 12% para vehículos de pasajeros
                    writer.println("Descuento Vehículo de Pasajeros (12%)");
                }
                
                double subtotalConDescuento = subtotal - descuento;
                double iva = subtotalConDescuento * 0.19;
                double total = subtotalConDescuento + iva;
                totalGeneral += total;

                writer.printf("Valor Diario: $%,.0f%n", vehiculo.getValorDiario());
                writer.printf("Días de Arriendo: %d%n", vehiculo.getDiasArriendo());
                writer.printf("Subtotal: $%,.0f%n", subtotal);
                writer.printf("Descuento: $%,.0f%n", descuento);
                writer.printf("Subtotal con Descuento: $%,.0f%n", subtotalConDescuento);
                writer.printf("IVA (19%%): $%,.0f%n", iva);
                writer.printf("Total: $%,.0f%n", total);
                writer.println("========================================\n");
                
                numeroFactura++;
            }

            writer.println("RESUMEN GENERAL");
            writer.println("----------------------------------------");
            writer.printf("Total Boletas Generadas: %d%n", (numeroFactura - 1));
            writer.printf("Monto Total: $%,.0f%n", totalGeneral);
            writer.println("========================================");

            System.out.println("\nBoletas guardadas exitosamente en el archivo: " + nombreArchivo);
            
        } catch (IOException e) {
            System.out.println("Error al guardar las boletas: " + e.getMessage());
        }
    }
     
        private static void agregarVehiculo() {
        try {
            System.out.println("\n=== Agregar Nuevo Vehículo ===");
            System.out.println("Seleccione el tipo de vehículo:");
            System.out.println("1. Vehículo de Carga");
            System.out.println("2. Vehículo de Pasajeros");
            System.out.print("\nSeleccione una opción: ");
            
            int tipo = Integer.parseInt(scanner.nextLine());
            
            if (tipo != 1 && tipo != 2) {
                System.out.println("Tipo de vehículo inválido.");
                return;
            }
            
            System.out.println("\nIngrese los datos del vehículo:");
            System.out.println("Formato de patente: AAAA11 (4 letras seguidas de 2 números)");
            
            String patente;
            while (true) {
                System.out.print("Patente: ");
                patente = scanner.nextLine().toUpperCase();
                
                if (!GestionVehiculos.validarFormatoPatente(patente)) {
                    System.out.println("Error: La patente debe tener el formato AAAA11 " +
                                     "(4 letras seguidas de 2 números)");
                    continue;
                }
                
                if (gestionVehiculos.existePatente(patente)) {
                    System.out.println("Error: Ya existe un vehículo con esa patente.");
                    continue;
                }
                
                break;
            }
            
            System.out.print("Marca: ");
            String marca = scanner.nextLine();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine();
            System.out.print("Año: ");
            int año = Integer.parseInt(scanner.nextLine());
            System.out.print("Valor diario de arriendo: $");
            double valorDiario = Double.parseDouble(scanner.nextLine());
            System.out.print("Días de arriendo: ");
            int diasArriendo = Integer.parseInt(scanner.nextLine());
            
            Vehiculo vehiculo;
            
            if (tipo == 1) {
                System.out.print("Capacidad de carga (toneladas): ");
                double capacidadCarga = Double.parseDouble(scanner.nextLine());
                vehiculo = new VehiculoCarga(patente, marca, modelo, año, valorDiario, 
                                           diasArriendo, capacidadCarga);
            } else {
                System.out.print("Capacidad de pasajeros: ");
                int capacidadPasajeros = Integer.parseInt(scanner.nextLine());
                vehiculo = new VehiculoPasajeros(patente, marca, modelo, año, valorDiario, 
                                               diasArriendo, capacidadPasajeros);
            }
            
            if (gestionVehiculos.agregarVehiculo(vehiculo)) {
                System.out.println("\n¡Vehículo agregado exitosamente!");
                System.out.println("Patente registrada: " + patente);
            } else {
                System.out.println("\nError: No se pudo agregar el vehículo.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor, ingrese valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al agregar el vehículo: " + e.getMessage());
        }
    }
    
        private static void listarVehiculos() {
        int opcion;
        do {
            System.out.println("\n=== Lista de Vehículos ===");
            System.out.println("1. Listar Vehículos de Carga");
            System.out.println("2. Listar Vehículos de Pasajeros");
            System.out.println("3. Volver al Menú Anterior");
            System.out.print("\nSeleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        listarVehiculosPorTipo(true); // true para vehículos de carga
                        break;
                    case 2:
                        listarVehiculosPorTipo(false); // false para vehículos de pasajeros
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Opción inválida. Por favor, intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número válido.");
                opcion = 0;
            }
        } while (opcion != 3);
    }

    private static void listarVehiculosPorTipo(boolean esCarga) {
        List<Vehiculo> vehiculos = gestionVehiculos.listarVehiculos();
        List<Vehiculo> vehiculosFiltrados = new ArrayList<>();
        
        for (Vehiculo v : vehiculos) {
            if (esCarga && v instanceof VehiculoCarga) {
                vehiculosFiltrados.add(v);
            } else if (!esCarga && v instanceof VehiculoPasajeros) {
                vehiculosFiltrados.add(v);
            }
        }
        
        if (vehiculosFiltrados.isEmpty()) {
            System.out.println("\nNo hay vehículos de " + 
                             (esCarga ? "carga" : "pasajeros") + " registrados.");
            return;
        }
        
        System.out.println("\n=== Vehículos de " + (esCarga ? "Carga" : "Pasajeros") + " ===");
        for (Vehiculo vehiculo : vehiculosFiltrados) {
            System.out.println("\n" + vehiculo.mostrarDatos());
            System.out.println("------------------------");
        }
    }
    
    private static void listarVehiculosArriendoLargo() {
        List<Vehiculo> vehiculos = gestionVehiculos.obtenerVehiculosArriendoLargo();
        if (vehiculos.isEmpty()) {
            System.out.println("\nNo hay vehículos con arriendos largos (≥7 días).");
            return;
        }
        
        System.out.println("\n=== Vehículos con Arriendos Largos ===");
        for (Vehiculo vehiculo : vehiculos) {
            System.out.println("\n" + vehiculo.mostrarDatos());
            System.out.println("------------------------");
        }
    }
    
            private static void cargarVehiculosDesdeCSV() {
        System.out.println("\n=== Cargar Vehículos desde CSV ===");
        System.out.println("El archivo CSV debe tener el siguiente formato:");
        System.out.println("Tipo,Patente,Marca,Modelo,Año,ValorDiario,DiasArriendo,Capacidad");
        System.out.println("Ejemplo:");
        System.out.println("C,ABCD12,Toyota,Hilux,2022,50000,5,2.5");
        System.out.println("P,WXYZ34,Hyundai,H1,2023,45000,3,12");
        System.out.println("\nDonde:");
        System.out.println("- Tipo: C para vehículo de carga, P para vehículo de pasajeros");
        System.out.println("- Patente: Formato AAAA11 (4 letras y 2 números)");
        System.out.println("- Capacidad: Toneladas para carga, Número de pasajeros para pasajeros");
        
        System.out.println("\nNota: El archivo debe estar en la carpeta del proyecto");
        System.out.print("\nIngrese el nombre del archivo CSV (ejemplo: vehiculos.csv): ");
        String nombreArchivo = scanner.nextLine();
        
        try {
            int resultados = gestionVehiculos.cargarArchivoCSV(nombreArchivo);
            System.out.println("\nSe cargaron " + resultados + " vehículos exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error durante la carga: " + e.getMessage());
        }
    }
    
        private static void generarBoleta() {
        System.out.println("\n=== Generar y Mostrar Boleta de Arriendo ===");
        
        List<Vehiculo> vehiculos = gestionVehiculos.listarVehiculos();
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehículos registrados para generar boleta.");
            return;
        }

        System.out.println("\nVehículos disponibles:");
        for (int i = 0; i < vehiculos.size(); i++) {
            System.out.println((i + 1) + ". " + vehiculos.get(i).getPatente() + 
                             " - " + vehiculos.get(i).getMarca() + 
                             " " + vehiculos.get(i).getModelo());
        }

        System.out.print("\nSeleccione el número del vehículo para generar la boleta: ");
        try {
            int seleccion = Integer.parseInt(scanner.nextLine());
            if (seleccion < 1 || seleccion > vehiculos.size()) {
                System.out.println("Selección inválida.");
                return;
            }

            Vehiculo vehiculoSeleccionado = vehiculos.get(seleccion - 1);
            
            System.out.println("\n========= BOLETA DE ARRIENDO =========");
            System.out.println("Fecha: " + LocalDate.now());
            System.out.println("----------------------------------------");
            System.out.println("DATOS DEL VEHÍCULO:");
            System.out.println(vehiculoSeleccionado.mostrarDatos());
            System.out.println("----------------------------------------");
            System.out.println("CÁLCULO DEL ARRIENDO:");
            
            double subtotal = vehiculoSeleccionado.getValorDiario() * 
                            vehiculoSeleccionado.getDiasArriendo();
            
            // Calcular descuento según el tipo de vehículo
            double descuento;
            if (vehiculoSeleccionado instanceof VehiculoCarga) {
                descuento = subtotal * 0.07; // 7% para vehículos de carga
                System.out.println("Descuento Vehículo de Carga (7%)");
            } else {
                descuento = subtotal * 0.12; // 12% para vehículos de pasajeros
                System.out.println("Descuento Vehículo de Pasajeros (12%)");
            }
            
            double subtotalConDescuento = subtotal - descuento;
            double iva = subtotalConDescuento * 0.19;
            double total = subtotalConDescuento + iva;

            System.out.printf("Valor Diario: $%,.0f%n", vehiculoSeleccionado.getValorDiario());
            System.out.printf("Días de Arriendo: %d%n", vehiculoSeleccionado.getDiasArriendo());
            System.out.printf("Subtotal: $%,.0f%n", subtotal);
            System.out.printf("Descuento: $%,.0f%n", descuento);
            System.out.printf("Subtotal con Descuento: $%,.0f%n", subtotalConDescuento);
            System.out.printf("IVA (19%%): $%,.0f%n", iva);
            System.out.printf("Total: $%,.0f%n", total);
            System.out.println("========================================");

        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor, ingrese un número válido.");
        }
    }
   
}