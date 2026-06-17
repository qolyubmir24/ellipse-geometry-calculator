package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class Kerucut extends Elips implements BangunRuang, Runnable {
    public double vTinggiKerucut;
    public double vGarisPelukis;
    public double vLuasSelimutKerucut;
    public double vVolumeKerucut;
    public double vLuasPermukaanKerucut;

    public Kerucut(double vJariMinor, double vJariMayor, double tinggi) {
        super(vJariMinor, vJariMayor);
        this.vTinggiKerucut = tinggi;
    }

    @Override
    public double hitungVolume() throws Exception {
        if (vTinggiKerucut <= 0) throw new Exception("Tinggi harus lebih dari 0!");
        // Rumus: 1/3 * Luas Alas Elips * Tinggi
        vVolumeKerucut = (super.hitungLuas() * vTinggiKerucut) / 3.0;
        return vVolumeKerucut;
    }

    // Overloading yang bersih dan aman dari risiko parameter terbalik
    public double hitungVolume(double vJariMinor, double vJariMayor, double tinggi) throws Exception {
        if (tinggi <= 0) throw new Exception("Tinggi harus lebih dari 0!");
        this.vTinggiKerucut = tinggi;
        // Rumus: 1/3 * Luas Alas Elips * Tinggi
        vVolumeKerucut = (super.hitungLuas(vJariMinor, vJariMayor) * vTinggiKerucut) / 3.0;
        return vVolumeKerucut;
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        if (vTinggiKerucut <= 0) throw new Exception("Tinggi harus lebih dari 0!");
        
        // Menghitung pendekatan garis pelukis rata-rata (AI)
        vGarisPelukis = Math.sqrt(
            Math.pow(vTinggiKerucut, 2) +
            Math.pow((vJariMayor + vJariMinor) / 2.0, 2)
        );

        // Luas Selimut = 0.5 * Keliling Alas * Garis Pelukis
        vLuasSelimutKerucut = 0.5 * super.hitungKeliling() * vGarisPelukis;
        
        // Luas Permukaan = Luas Alas Elips + Luas Selimut
        vLuasPermukaanKerucut = super.hitungLuas() + vLuasSelimutKerucut;
        return vLuasPermukaanKerucut;
    }

    // Overloading yang bersih dan aman
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double tinggi) throws Exception {
        if (tinggi <= 0) throw new Exception("Tinggi harus lebih dari 0!");
        this.vTinggiKerucut = tinggi;
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        // Menghitung pendekatan garis pelukis rata-rata (AI)
        vGarisPelukis = Math.sqrt(
            Math.pow(vTinggiKerucut, 2) +
            Math.pow((vJariMayor + vJariMinor) / 2.0, 2)
        );

        // Luas Selimut = 0.5 * Keliling Alas * Garis Pelukis
        vLuasSelimutKerucut = 0.5 * super.hitungKeliling() * vGarisPelukis;
        
        // Luas Permukaan = Luas Alas Elips + Luas Selimut
        vLuasPermukaanKerucut = super.hitungLuas() + vLuasSelimutKerucut;
        return vLuasPermukaanKerucut;
    }

    @Override
    public void run() {
        if (listener == null) return;
        long startMs = System.currentTimeMillis();
        double dummyResult = 0;
        double finalLuasPermukaan = 0; 
        double finalVol = 0;

        try {
            for (int i = 1; i <= iterasi; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                
                // PERBAIKAN: Memanggil hitungLuasPermukaan() milik Kerucut, bukan hitungLuas() alas elips
                finalLuasPermukaan = hitungLuasPermukaan();
                finalVol = hitungVolume();
                
                dummyResult += finalVol + finalLuasPermukaan; 
                
                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[Kerucut Elips-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            
            // PERBAIKAN: Mengirimkan Luas Permukaan nyata dan Volume ke listener
            listener.onDone(threadId, "Kerucut Elips", finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Kerucut Elips", 0, 0, 0, e.getMessage());
        }
    }
}