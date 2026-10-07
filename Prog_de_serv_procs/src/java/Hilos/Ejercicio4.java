package Hilos;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Ejercicio4 {

    public static void main(String[] args) throws InterruptedException {
        final int TOTAL_NUMEROS = 20;
        final int CAPACIDAD = 5;

        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(CAPACIDAD);

        Thread productor = new Thread(() -> {
            Random random = new Random();
            try {
                for (int i = 0; i < TOTAL_NUMEROS; i++) {
                    int numero = random.nextInt(100);
                    cola.put(numero); // espera si la cola está llena
                    System.out.println("Productor generó: " + numero + "  | tamaño cola: " + cola.size());
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumidor = new Thread(() -> {
            try {
                for (int i = 0; i < TOTAL_NUMEROS; i++) {
                    int numero = cola.take(); // espera si la cola está vacía
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
