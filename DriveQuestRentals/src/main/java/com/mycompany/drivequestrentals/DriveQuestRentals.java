package com.mycompany.drivequestrentals;

import java.util.Scanner;

public class DriveQuestRentals {
    private static GestionVehiculos gestionVehiculos = new GestionVehiculos();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        MenuManager menuManager = new MenuManager(gestionVehiculos, scanner);
        menuManager.mostrarMenuPrincipal();
        scanner.close();
    }
}