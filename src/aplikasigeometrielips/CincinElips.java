package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class CincinElips extends Elips implements BangunRuang, Runnable {
    public double R; // Jari-jari lintasan putar dari pusat toroid ke pusat penampang elips

    public CincinElips(double vJariMinor, double vJariMayor, double R) {
        super(vJariMinor, vJariMayor);
        if (R <= 0) {
            throw new IllegalArgumentException("Jari-jari lintasan (R) harus lebih besar dari 0.");
        }
        this.R = R;
    }

    @Override
    public double hitungVolume() throws Exception {
        // Teorema Pappus: Luas Penampang Elips * Keliling Lintasan Putar (2 * PI * R)
        vVolume = super.hitungLuas() * 2.0 * PI * R;
        return vVolume;
    }

    // Overloading yang aman dan menjaga konsistensi state objek
    public double hitungVolume(double vJariMinor, double vJariMayor, double R) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || R <= 0) {
            throw new IllegalArgumentException("Semua parameter harus lebih besar dari 0.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        this.R = R;
        return hitungVolume();
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        // Teorema Pappus: Keliling Penampang Elips * Keliling Lintasan Putar (2 * PI * R)
        vLuasPermukaan = super.hitungKeliling() * 2.0 * PI * R;
        return vLuasPermukaan;
    }

    // Overloading yang aman dan menjaga konsistensi state objek
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double R) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || R <= 0) {
            throw new IllegalArgumentException("Semua parameter harus lebih besar dari 0.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        this.R = R;
        return hitungLuasPermukaan();
    }

    public double getR() {
        return R;
    }

    @Override
    public void run() {
        if (listener == null) return;
        long startMs = System.currentTimeMillis();
        double dummyResult = 0;
        double finalLuasPermukaan = 0; // PERBAIKAN: Menggunakan penamaan eksplisit untuk Luas Permukaan Torus 3D
        double finalVol = 0;

        try {
            for (int i = 1; i <= iterasi; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                
                // PERBAIKAN: Memanggil hitungLuasPermukaan() total 3D cincin, bukan hitungLuas() alas 2D
                finalLuasPermukaan = hitungLuasPermukaan();
                finalVol = hitungVolume();
                
                dummyResult += finalVol + finalLuasPermukaan; 
                
                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[Cincin Elips-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            
            // PERBAIKAN: Mengirimkan Luas Permukaan Torus sesungguhnya ke listener
            listener.onDone(threadId, "Cincin Elips (Torus)", finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Cincin Elips (Torus)", 0, 0, 0, e.getMessage());
        }
    }
}