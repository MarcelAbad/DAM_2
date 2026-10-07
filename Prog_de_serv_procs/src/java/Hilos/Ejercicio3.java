package Hilos;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Ejercicio3 {

    static class Buffer {
        private final List<Integer> lista = new LinkedList<>();
        private final int capacidad;

        public Buffer(int capacidad) {
            this.capacidad = capacidad;
        }

        public synchronized void producir(int numero) throws InterruptedException {

            while (lista.size() == capacidad) {
                wait();
            }
            lista.add(numero);
            System.out.println("Productor generó: " + numero + "  | tamaño lista: " + lista.size());
            notify();
        }

        public synchronized int consumir() throws InterruptedException {

            while (lista.isEmpty()) {
                wait();
            }
            int numero = lista.remove(0);
            notify();
            return numero;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int TOTAL_NUMEROS = 20;
        final int CAPACIDAD = 5;

        Buffer buffer = new Buffer(CAPACIDAD);

        Thread productor = new Thread(() -> {
            Random random = new Random();
            try {
                for (int i = 0; i < TOTAL_NUMEROS; i++) {
                    buffer.producir(random.nextInt(100));
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumidor = new Thread(() -> {
            try {
                for (int i = 0; i < TOTAL_NUMEROS; i++) {
                    int numero = buffer.consumir();
                    System.out.println("    Consumidor eliminó: " + numero);
                    Thread.sleep(250);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        productor.start();
        consumidor.start();

        productor.join();
        consumidor.join();

        System.out.println("Fin del programa");
    }
}
