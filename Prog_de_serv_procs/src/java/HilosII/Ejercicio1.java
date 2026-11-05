package HilosII;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Ejercicio1 {

    static class Sesiones {
        private static final long INTERVALO_REFRESCO_MS = 10_000;

        private final Map<String, Long> inicios = new LinkedHashMap<>();
        private final List<String> avisos = new ArrayList<>();
        private boolean finalizado = false;

        public synchronized boolean login(String nombre) {
            String clave = nombre.toLowerCase();
            if (inicios.containsKey(clave)) {
                return false; // duplicado
            }
            inicios.put(clave, System.nanoTime());
            avisos.add(formatear(nombre) + " se ha conectado.");
            notifyAll();
            return true;
        }

        public synchronized boolean logout(String nombre) {
            Long inicio = inicios.remove(nombre.toLowerCase());
            if (inicio == null) {
                return false;
            }
            avisos.add(formatear(nombre) + " se ha desconectado (estuvo conectado "
                    + segundosDesde(inicio) + "s).");
            notifyAll();
            return true;
        }

        public synchronized void terminar() {
            finalizado = true;
            notifyAll();
        }

        public synchronized String esperarInforme() throws InterruptedException {
            if (avisos.isEmpty() && !finalizado) {
                wait(INTERVALO_REFRESCO_MS);
            }
            if (finalizado) {
                return null;
            }

            StringBuilder informe = new StringBuilder();
            for (String aviso : avisos) {
                informe.append(aviso).append('\n');
            }
            avisos.clear();

            for (Map.Entry<String, Long> e : inicios.entrySet()) {
                informe.append(formatear(e.getKey()))
                        .append(" conectado ")
                        .append(segundosDesde(e.getValue()))
                        .append("s\n");
            }
            return informe.toString();
        }

        private long segundosDesde(long inicioNanos) {
            return (System.nanoTime() - inicioNanos) / 1_000_000_000L;
        }

        private String formatear(String nombre) {
            return nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        }
    }

    static class Monitor extends Thread {
        private final Sesiones sesiones;

        Monitor(Sesiones sesiones) {
            super("Monitor");
            this.sesiones = sesiones;
        }

        @Override
        public void run() {
            try {
                String informe;
                while ((informe = sesiones.esperarInforme()) != null) {
                    if (!informe.isEmpty()) {
                        System.out.print(informe);
                        System.out.print("> ");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Sesiones sesiones = new Sesiones();
        Monitor monitor = new Monitor(sesiones);
        monitor.start();

        System.out.println("Comandos: login <nombre> | logout <nombre> | salir");
        System.out.print("> ");

        Scanner teclado = new Scanner(System.in);
        boolean salir = false;

        while (!salir && teclado.hasNextLine()) {
            String[] partes = teclado.nextLine().trim().split("\\s+");
            String comando = partes[0].toLowerCase();

            switch (comando) {
                case "login":
                    if (partes.length != 2) {
                        System.out.print("Uso: login <nombre>\n> ");
                    } else if (!sesiones.login(partes[1])) {
                        System.out.print("Error: el usuario '" + partes[1] + "' ya está conectado.\n> ");
                    }
                    break;

                case "logout":
                    if (partes.length != 2) {
                        System.out.print("Uso: logout <nombre>\n> ");
                    } else if (!sesiones.logout(partes[1])) {
                        System.out.print("Error: el usuario '" + partes[1] + "' no está conectado.\n> ");
                    }
                    break;

                case "salir":
                    salir = true;
                    break;

                case "":
                    System.out.print("> ");
                    break;

                default:
                    System.out.print("Comando desconocido. Use: login, logout o salir.\n> ");
            }
        }
        sesiones.terminar();
        try {
            monitor.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Proceso finalizado");
    }
}
