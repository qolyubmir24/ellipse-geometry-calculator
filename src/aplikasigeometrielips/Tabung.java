package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class Tabung extends Elips implements BangunRuang, Runnable {
    public double vTinggiTabung;
    public double vVolumeTabung;
    public double vLuasPermukaanTabung;

    public Tabung(double vJariMinor, double vJariMayor, double tinggi) {
        super(vJariMinor, vJariMayor);
        this.vTinggiTabung = tinggi;
    }

    @Override
    public double hitungVolume() throws Exception {
        if (vTinggiTabung <= 0) throw new Exception("Tinggi harus lebih dari 0!");
        vVolumeTabung = super.hitungLuas() * vTinggiTabung; // Luas alas elips * tinggi
        return vVolumeTabung;
    }

    // Overloading
    public double hitungVolume(double vJariMinor, double vJariMayor, double tinggi) throws Exception {
        if (tinggi <= 0) throw new Exception("Tinggi harus lebih dari 0!");
        this.vTinggiTabung = tinggi;
        vVolumeTabung = super.hitungLuas(vJariMinor, vJariMayor) * vTinggiTabung; // Luas alas elips * tinggi
        return vVolumeTabung;
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        if (vTinggiTabung <= 0) throw new Exception("Tinggi harus lebih dari 0!");
        // Rumus: (2 * Luas Alas Elips) + (Keliling Elips * tinggi)
        vLuasPermukaanTabung = (2 * super.hitungLuas()) + (super.hitungKeliling() * vTinggiTabung);
        return vLuasPermukaanTabung;
    }

    // Overloading yang lebih aman dan bersih
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double tinggi) throws Exception {
        if (tinggi <= 0) throw new Exception("Tinggi harus lebih dari 0!");
        this.vTinggiTabung = tinggi;
        // Rumus: (2 * Luas Alas Elips) + (Keliling Elips * tinggi)
        vLuasPermukaanTabung = (2 * super.hitungLuas(vJariMinor, vJariMayor)) + (super.hitungKeliling(vJariMinor, vJariMayor) * vTinggiTabung);
        return vLuasPermukaanTabung;
    }


    @Override
    public void run() {
        if (listener == null) return;
        long startMs = System.currentTimeMillis();
        double dummyResult = 0;
        double finalLuasPermukaan = 0; // Diubah agar menampung Luas Permukaan Tabung
        double finalVol = 0;

        try {
            for (int i = 1; i <= iterasi; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                
                // PERBAIKAN: Memanggil perhitungan milik Tabung (Bukan alasnya saja)
                finalLuasPermukaan = hitungLuasPermukaan();
                finalVol = hitungVolume();
                
                dummyResult += finalVol + finalLuasPermukaan; 
                
                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[Tabung Elips-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            
            // PERBAIKAN: Mengirimkan Luas Permukaan Tabung & Volume Tabung ke listener
            listener.onDone(threadId, "Tabung Elips", finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Tabung Elips", 0, 0, 0, e.getMessage());
        }
    }
}