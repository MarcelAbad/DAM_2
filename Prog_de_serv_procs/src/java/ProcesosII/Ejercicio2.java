package ProcesosII;

import java.io.IOException;

public class Ejercicio2 {

    public static void main(String[] args) {
        String[] comando;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            comando = new String[] { "ping", "-n", "11", "127.0.0.1" };
        } else {
            comando = new String[] { "sleep", "10" };
        }

        if (args.length > 0) {
            comando = args;
        }

        ProcessBuilder pb = new ProcessBuilder(comando);

        try {
            Process proceso = pb.start();
            System.out.println("Proceso iniciado con PID: " + proceso.pid());
            System.out.println("Esperando 3 segundos antes de matarlo...");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.err.println("La espera fue interrumpida: " + e.getMessage());
                Thread.currentThread().interrupt();
            }

            if (proceso.isAlive()) {
                proceso.destroy();
                System.out.println("Proceso destruido con destroy().");

                try {
                    proceso.waitFor();
                    System.out.println("Código de salida: " + proceso.exitValue());
                } catch (InterruptedException e) {
                    System.err.println("Interrumpido esperando la finalización: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("El proceso ya había terminado antes de los 3 segundos.");
            }

        } catch (IOException e) {
            System.err.println("ERROR: no se pudo lanzar el proceso '" + comando[0] + "'.");
            System.err.println("Detalle: " + e.getMessage());
        }
    }
}
