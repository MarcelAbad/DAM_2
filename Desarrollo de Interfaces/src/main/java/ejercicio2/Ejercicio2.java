package ejercicio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio2 {
    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dir", "/w");
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
    }
}