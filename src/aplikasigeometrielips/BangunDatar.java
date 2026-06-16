/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public interface BangunDatar {
    double hitungLuas() throws Exception;
    double hitungKeliling() throws Exception;
    void multithread(int iterasi, int threadId, MultithreadListener listener);
}
