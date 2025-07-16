package com.mycompany.drivequestrentals;

public abstract class Vehiculo implements ICalculable {
    // Atributos comunes para todos los vehículos
    protected String patente;
    protected String marca;
    protected String modelo;
    protected int año;
    protected double valorDiario;
    protected int diasArriendo;
    
    // Constructor vacío
    public Vehiculo() {
    }
    
    // Constructor con parámetros
    public Vehiculo(String patente, String marca, String modelo, int año, 
                   double valorDiario, int diasArriendo) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.valorDiario = valorDiario;
        this.diasArriendo = diasArriendo;
    }
    
    // Método abstracto que deberán implementar las clases hijas
    public abstract String mostrarDatos();
    
    // Getters y Setters
    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return año;
    }

    public void setAnio(int año) {
        this.año = año;
    }

    public double getValorDiario() {
        return valorDiario;
    }

    public void setValorDiario(double valorDiario) {
        this.valorDiario = valorDiario;
    }

    public int getDiasArriendo() {
        return diasArriendo;
    }

    public void setDiasArriendo(int diasArriendo) {
        this.diasArriendo = diasArriendo;
    }
}