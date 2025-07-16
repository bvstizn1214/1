package com.mycompany.drivequestrentals;

public interface ICalculable {
    // Constantes para cálculos
    double IVA = 0.19; // 19%
    double DESCUENTO_CARGA = 0.07; // 7%
    double DESCUENTO_PASAJEROS = 0.12; // 12%
    
    // Método para calcular y mostrar boleta
    String generarBoleta();
}