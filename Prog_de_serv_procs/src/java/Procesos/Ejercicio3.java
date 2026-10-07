package Procesos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Ejercicio3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introduce el comando a ejecutar (ej: dir, dir /w, ipconfig): ");
        String comando = sc.nextLine();

        if (comando.isBlank()) {
            System.err.println("Error: no has introducido ningún comando.");
            sc.close();
            return;
        }

        String[] partesComando = comando.trim().split("\\s+");

        String[] comandoCompleto = new String[2 + partesComando.length];
        comandoCompleto[0] = "cmd.exe";
        comandoCompleto[1] = "/c";
        System.arraycopy(partesComando, 0, comandoCompleto, 2, partesComando.length);

        ProcessBuilder pb = new ProcessBuilder(comandoCompleto);
        pb.redirectErrorStream(true);

        System.out.println("Ejecutando: " + String.join(" ", pb.command()));

        try {
            Process proceso = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }

            int codigoSalida = proceso.waitFor();
            System.out.println("El proceso terminó con código: " + codigoSalida);

        } catch (IOException e) {
            System.err.println("Error: el comando no existe o no se pudo ejecutar.");
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("El proceso fue interrumpido.");
            Thread.currentThread().interrupt();
        }

        sc.close();
    }
}