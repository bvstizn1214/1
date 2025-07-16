package com.mycompany.drivequestrentals;

public class VehiculoPasajeros extends Vehiculo {
    private int capacidadPasajeros;
    
    // Constructor vacío
    public VehiculoPasajeros() {
        super();
    }
    
    // Constructor con parámetros
    public VehiculoPasajeros(String patente, String marca, String modelo, int año, 
                           double valorDiario, int diasArriendo, int capacidadPasajeros) {
        super(patente, marca, modelo, año, valorDiario, diasArriendo);
        this.capacidadPasajeros = capacidadPasajeros;
    }
    
    @Override
    public String mostrarDatos() {
        return "Vehículo de Pasajeros:\n" +
               "Patente: " + patente + "\n" +
               "Marca: " + marca + "\n" +
               "Modelo: " + modelo + "\n" +
               "Año: " + año + "\n" +
               "Valor Diario: $" + valorDiario + "\n" +
               "Días de Arriendo: " + diasArriendo + "\n" +
               "Capacidad de Pasajeros: " + capacidadPasajeros + " personas";
    }
    
    @Override
    public String generarBoleta() {
        double subtotal = valorDiario * diasArriendo;
        double descuento = subtotal * DESCUENTO_PASAJEROS;
        double subtotalConDescuento = subtotal - descuento;
        double montoIVA = subtotalConDescuento * IVA;
        double total = subtotalConDescuento + montoIVA;
        
        return "\n=== BOLETA DE ARRIENDO ===\n" +
               "Vehículo de Pasajeros - Patente: " + patente + "\n" +
               "Días de arriendo: " + diasArriendo + "\n" +
               "Valor diario: $" + valorDiario + "\n" +
               "Subtotal: $" + subtotal + "\n" +
               "Descuento (12%): -$" + descuento + "\n" +
               "Subtotal con descuento: $" + subtotalConDescuento + "\n" +
               "IVA (19%): $" + montoIVA + "\n" +
               "Total a pagar: $" + total;
    }
    
    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }
    
    public void setCapacidadPasajeros(int capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }
}