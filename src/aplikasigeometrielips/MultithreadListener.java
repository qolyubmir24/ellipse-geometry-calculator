/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public interface MultithreadListener {
    void onProgress(int threadId, int progressPercent, String logMsg);
    void onDone(int threadId, String namaBangun, double finalLuas, double finalVolume, long timeTakenMs, String errorMessage);
}
