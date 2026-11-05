package ProcesosII;

import java.io.IOException;

public class Ejercicio1 {

    public static void main(String[] args) {
        String[] comando = (args.length > 0) ? args : new String[] { "notepad" };

        ProcessBuilder pb = new ProcessBuilder(comando);

        try {
            Process proceso = pb.start();
            long pid = proceso.pid();
            System.out.println("Proceso lanzado: " + String.join(" ", comando));
            System.out.println("PID del proceso: " + pid);

        } catch (IOException e) {
            System.err.println("ERROR: el proceso '" + comando[0] + "' no existe o no se pudo lanzar.");
            System.err.println("Detalle: " + e.getMessage());
        }
    }
}
