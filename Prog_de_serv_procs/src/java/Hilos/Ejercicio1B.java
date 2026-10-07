package Hilos;

public class Ejercicio1B {

        static class Contador {
            private int valor = 0;

            public synchronized void incrementar() {
                valor++;
            }

            public int getValor() {
                return valor;
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
