/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public abstract class BangunGeometri {
    // Variabel Multithreading yang dipakai bersama
    int iterasi = 1;
    int threadId = 1;
    MultithreadListener listener;

    // Method pengaturan yang diwariskan ke semua class
    public void multithread(int iterasi, int threadId, MultithreadListener listener) {
        this.iterasi = iterasi;
        this.threadId = threadId;
        this.listener = listener;
    }
}
