package Hilos;

import java.util.concurrent.locks.ReentrantLock;

public class Ejercicio2 {

    static class Contador {
        private int valor = 0;
        private final ReentrantLock lock = new ReentrantLock();

        public void incrementar() {
            lock.lock();
            try {
                valor++;
            } finally {
                lock.unlock();
            }
        }

        public int getValor() {
            lock.lock();
            try {
                return valor;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int NUM_HILOS = 10;
        final int INCREMENTOS = 1000;

        Contador contador = new Contador();

        Thread[] hilos = new Thread[NUM_HILOS];

        for (int i = 0; i < NUM_HILOS; i++) {
            hilos[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTOS; j++) {
                    contador.incrementar();
                }
            });
            hilos[i].start();
        }

        for (Thread hilo : hilos) {
            hilo.join();
        }

        System.out.println("Valor esperado: " + (NUM_HILOS * INCREMENTOS));
        System.out.println("Valor obtenido: " + contador.getValor());
    }
}