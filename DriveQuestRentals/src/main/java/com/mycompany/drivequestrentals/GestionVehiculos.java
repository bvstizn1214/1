package com.mycompany.drivequestrentals;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

public class GestionVehiculos {
    private List<Vehiculo> vehiculos;
    private static final String ARCHIVO_DATOS = "vehiculos.txt";
    private static final String ARCHIVO_CSV = "vehiculos.csv";
    private static final Pattern PATRON_PATENTE = Pattern.compile("^[A-Za-z]{4}\\d{2}$");
    
    public List<Vehiculo> listarVehiculosArriendoLargo() {
        List<Vehiculo> vehiculosArriendoLargo = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getDiasArriendo() > 7) { // Consideramos arriendo largo más de 7 días
                vehiculosArriendoLargo.add(vehiculo);
            }
        }
        return vehiculosArriendoLargo;
    }
    
    
    
    public GestionVehiculos() {
        this.vehiculos = new CopyOnWriteArrayList<>();
        cargarDatos();
        cargarDatosDesdeCSV();
    }
    
    private synchronized void cargarDatosDesdeCSV() {
        File archivo = new File(ARCHIVO_CSV);
        if (!archivo.exists()) {
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = br.readLine()) != null) {
                // Saltar la primera línea si contiene encabezados
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                
                String[] datos = linea.split(",");
                if (datos.length < 8) continue;
                
                try {
                    String tipo = datos[0].trim();
                    String patente = datos[1].trim();
                    String marca = datos[2].trim();
                    String modelo = datos[3].trim();
                    int año = Integer.parseInt(datos[4].trim());
                    double valorDiario = Double.parseDouble(datos[5].trim());
                    int diasArriendo = Integer.parseInt(datos[6].trim());
                    
                    // Validar formato de patente
                    if (!validarFormatoPatente(patente)) {
                        System.err.println("CSV: Patente inválida ignorada: " + patente);
                        continue;
                    }
                    
                    // Verificar si la patente ya existe
                    if (existePatente(patente)) {
                        System.err.println("CSV: Patente duplicada ignorada: " + patente);
                        continue;
                    }
                    
                    Vehiculo vehiculo;
                    if (tipo.equalsIgnoreCase("C")) {
                        double capacidadCarga = Double.parseDouble(datos[7].trim());
                        vehiculo = new VehiculoCarga(patente, marca, modelo, año, 
                                                   valorDiario, diasArriendo, capacidadCarga);
                    } else if (tipo.equalsIgnoreCase("P")) {
                        int capacidadPasajeros = Integer.parseInt(datos[7].trim());
                        vehiculo = new VehiculoPasajeros(patente, marca, modelo, año, 
                                                       valorDiario, diasArriendo, capacidadPasajeros);
                    } else {
                        System.err.println("CSV: Tipo de vehículo inválido ignorado: " + tipo);
                        continue;
                    }
                    
                    vehiculos.add(vehiculo);
                    
                } catch (NumberFormatException e) {
                    System.err.println("CSV: Error en formato de números, línea ignorada: " + linea);
                } catch (Exception e) {
                    System.err.println("CSV: Error procesando línea: " + linea);
                }
            }
            System.out.println("Datos CSV cargados exitosamente.");
            
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo CSV: " + e.getMessage());
        }
    }
    
     // Método público para cargar archivo CSV desde una ruta específica
           public synchronized int cargarArchivoCSV(String rutaArchivo) throws IOException {
        int vehiculosCargados = 0;
        List<Vehiculo> vehiculosTemp = new ArrayList<>();
        
        System.out.println("\nIniciando carga de archivo CSV...");
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0;
            
            // Primero, mostrar todo el contenido del archivo
            System.out.println("\nContenido del archivo CSV:");
            List<String> lineas = new ArrayList<>();
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                System.out.println("Línea " + numeroLinea + ": " + linea);
                lineas.add(linea);
            }
            
            // Luego procesar las líneas
            numeroLinea = 0;
            boolean algunVehiculoProcesado = false;
            
            for (String lineaActual : lineas) {
                numeroLinea++;
                
                // Saltar la primera línea si es encabezado
                if (numeroLinea == 1 && lineaActual.toLowerCase().contains("tipo")) {
                    System.out.println("\nSaltando línea de encabezado");
                    continue;
                }
                
                System.out.println("\nProcesando línea " + numeroLinea + ": " + lineaActual);
                
                String[] datos = lineaActual.split(",");
                if (datos.length < 8) {
                    System.out.println("Advertencia: Línea " + numeroLinea + " ignorada por formato incorrecto (se esperan 8 campos): " + lineaActual);
                    continue;
                }
                
                try {
                    String tipo = datos[0].trim();
                    String patente = datos[1].trim().toUpperCase();
                    String marca = datos[2].trim();
                    String modelo = datos[3].trim();
                    int año = Integer.parseInt(datos[4].trim());
                    double valorDiario = Double.parseDouble(datos[5].trim());
                    int diasArriendo = Integer.parseInt(datos[6].trim());
                    
                    System.out.println("Validando datos:");
                    System.out.println("- Tipo: " + tipo);
                    System.out.println("- Patente: " + patente);
                    System.out.println("- Marca: " + marca);
                    System.out.println("- Modelo: " + modelo);
                    System.out.println("- Año: " + año);
                    System.out.println("- Valor Diario: " + valorDiario);
                    System.out.println("- Días Arriendo: " + diasArriendo);
                    
                    // Validar formato de patente
                    if (!validarFormatoPatente(patente)) {
                        System.out.println("Advertencia: Patente inválida ignorada (debe ser 4 letras y 2 números): " + patente);
                        continue;
                    }
                    
                    // Verificar duplicados
                    if (existePatente(patente)) {
                        System.out.println("Advertencia: La patente ya existe en el sistema: " + patente);
                        continue;
                    }
                    
                    if (vehiculosTemp.stream().anyMatch(v -> v.getPatente().equalsIgnoreCase(patente))) {
                        System.out.println("Advertencia: La patente ya existe en los nuevos registros: " + patente);
                        continue;
                    }
                    
                    Vehiculo vehiculo;
                    if (tipo.equalsIgnoreCase("C")) {
                        double capacidadCarga = Double.parseDouble(datos[7].trim());
                        System.out.println("- Capacidad de carga: " + capacidadCarga + " toneladas");
                        vehiculo = new VehiculoCarga(patente, marca, modelo, año, 
                                                   valorDiario, diasArriendo, capacidadCarga);
                    } else if (tipo.equalsIgnoreCase("P")) {
                        int capacidadPasajeros = Integer.parseInt(datos[7].trim());
                        System.out.println("- Capacidad de pasajeros: " + capacidadPasajeros + " personas");
                        vehiculo = new VehiculoPasajeros(patente, marca, modelo, año, 
                                                       valorDiario, diasArriendo, capacidadPasajeros);
                    } else {
                        System.out.println("Advertencia: Tipo de vehículo inválido (debe ser C o P): " + tipo);
                        continue;
                    }
                    
                    vehiculosTemp.add(vehiculo);
                    algunVehiculoProcesado = true;
                    vehiculosCargados++;
                    System.out.println("✓ Vehículo con patente " + patente + " procesado exitosamente");
                    
                } catch (NumberFormatException e) {
                    System.out.println("Advertencia: Error en formato de números en línea " + numeroLinea + ": " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Advertencia: Error procesando línea " + numeroLinea + ": " + e.getMessage());
                }
            }
            
            // Actualizar la lista principal solo si se procesó al menos un vehículo
            if (algunVehiculoProcesado) {
                vehiculos.addAll(vehiculosTemp);
                guardarDatos();
                System.out.println("\nSe agregaron " + vehiculosCargados + " vehículos al sistema");
            } else {
                System.out.println("\nNo se agregaron vehículos nuevos al sistema");
            }
            
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo CSV: " + e.getMessage());
        }
        
        return vehiculosCargados;
    }
           
           

    // Método para verificar si existe una patente
    public boolean existePatente(String patente) {
        // Agregar depuración
        boolean existe = vehiculos.stream()
                .anyMatch(v -> v.getPatente().equalsIgnoreCase(patente));
        if (existe) {
            System.out.println("Patente " + patente + " encontrada en el sistema");
        }
        return existe;
    }
    
     // Método para validar el formato de la patente
    public static boolean validarFormatoPatente(String patente) {
        if (patente == null || patente.trim().isEmpty()) {
            return false;
        }
        return PATRON_PATENTE.matcher(patente.toUpperCase()).matches();
    }
    
    public void limpiarDatosEnMemoria() {
    vehiculos.clear(); // Limpia la lista en memoria
    System.out.println("Datos en memoria limpiados. Los archivos de persistencia se mantienen intactos.");
}
    
   // Método modificado para agregar un vehículo
    public synchronized boolean agregarVehiculo(Vehiculo vehiculo) {
        // Validar formato de patente
        if (!validarFormatoPatente(vehiculo.getPatente())) {
            throw new IllegalArgumentException(
                "La patente debe tener el formato AAAA11 (4 letras seguidas de 2 números)");
        }
        
        // Convertir a mayúsculas para estandarizar
        vehiculo.setPatente(vehiculo.getPatente().toUpperCase());
        
        // Verificar si ya existe
        if (existePatente(vehiculo.getPatente())) {
            return false;
        }
        
        boolean resultado = vehiculos.add(vehiculo);
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }
    
    
    // Método para buscar un vehículo por patente
    public Vehiculo buscarVehiculoPorPatente(String patente) {
        return vehiculos.stream()
                .filter(v -> v.getPatente().equalsIgnoreCase(patente))
                .findFirst()
                .orElse(null);
    }
    
    // Método para listar todos los vehículos
    public List<Vehiculo> listarVehiculos() {
        return new ArrayList<>(vehiculos);
    }
    
    // Método para obtener vehículos con arriendo largo (>= 7 días)
    public List<Vehiculo> obtenerVehiculosArriendoLargo() {
        List<Vehiculo> arriendosLargos = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getDiasArriendo() >= 7) {
                arriendosLargos.add(vehiculo);
            }
        }
        return arriendosLargos;
    }
    
    // Método para guardar datos en archivo
    private synchronized void guardarDatos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_DATOS))) {
            for (Vehiculo v : vehiculos) {
                StringBuilder sb = new StringBuilder();
                sb.append(v instanceof VehiculoCarga ? "C|" : "P|")
                  .append(v.getPatente()).append("|")
                  .append(v.getMarca()).append("|")
                  .append(v.getModelo()).append("|")
                  .append(v.getAnio()).append("|")
                  .append(v.getValorDiario()).append("|")
                  .append(v.getDiasArriendo()).append("|");
                
                if (v instanceof VehiculoCarga) {
                    sb.append(((VehiculoCarga) v).getCapacidadCarga());
                } else {
                    sb.append(((VehiculoPasajeros) v).getCapacidadPasajeros());
                }
                
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }
    
    // Método para cargar datos desde archivo
    private synchronized void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length < 8) continue;
                
                String tipo = datos[0];
                String patente = datos[1];
                String marca = datos[2];
                String modelo = datos[3];
                int año = Integer.parseInt(datos[4]);
                double valorDiario = Double.parseDouble(datos[5]);
                int diasArriendo = Integer.parseInt(datos[6]);
                
                if (tipo.equals("C")) {
                    double capacidadCarga = Double.parseDouble(datos[7]);
                    vehiculos.add(new VehiculoCarga(patente, marca, modelo, año, 
                                                  valorDiario, diasArriendo, capacidadCarga));
                } else {
                    int capacidadPasajeros = Integer.parseInt(datos[7]);
                    vehiculos.add(new VehiculoPasajeros(patente, marca, modelo, año, 
                                                      valorDiario, diasArriendo, capacidadPasajeros));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
        }
    }
}