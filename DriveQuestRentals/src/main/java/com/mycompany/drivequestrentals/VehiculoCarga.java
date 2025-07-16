package com.mycompany.drivequestrentals;

public class VehiculoCarga extends Vehiculo {
    private double capacidadCarga;
    
    // Constructor vacío
    public VehiculoCarga() {
        super();
    }
    
    // Constructor con parámetros
    public VehiculoCarga(String patente, String marca, String modelo, int anio, 
                        double valorDiario, int diasArriendo, double capacidadCarga) {
        super(patente, marca, modelo, anio, valorDiario, diasArriendo);
        this.capacidadCarga = capacidadCarga;
    }
    
    @Override
    public String mostrarDatos() {
        return "Vehículo de Carga:\n" +
               "Patente: " + patente + "\n" +
               "Marca: " + marca + "\n" +
               "Modelo: " + modelo + "\n" +
               "Año: " + anio + "\n" +
               "Valor Diario: $" + valorDiario + "\n" +
               "Días de Arriendo: " + diasArriendo + "\n" +
               "Capacidad de Carga: " + capacidadCarga + " toneladas";
    }
    
    @Override
    public String generarBoleta() {
        double subtotal = valorDiario * diasArriendo;
        double descuento = subtotal * DESCUENTO_CARGA;
        double subtotalConDescuento = subtotal - descuento;
        double montoIVA = subtotalConDescuento * IVA;
        double total = subtotalConDescuento + montoIVA;
        
        return "\n=== BOLETA DE ARRIENDO ===\n" +
               "Vehículo de Carga - Patente: " + patente + "\n" +
               "Días de arriendo: " + diasArriendo + "\n" +
               "Valor diario: $" + valorDiario + "\n" +
               "Subtotal: $" + subtotal + "\n" +
               "Descuento (7%): -$" + descuento + "\n" +
               "Subtotal con descuento: $" + subtotalConDescuento + "\n" +
               "IVA (19%): $" + montoIVA + "\n" +
               "Total a pagar: $" + total;
    }
    
    public double getCapacidadCarga() {
        return capacidadCarga;
    }
    
    public void setCapacidadCarga(double capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }
}