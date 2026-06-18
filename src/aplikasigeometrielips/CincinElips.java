package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class CincinElips extends Elips implements BangunRuang, Runnable {
    public double R; // Jari-jari lintasan putar dari pusat toroid ke pusat penampang elips
    public double vVolumeCincinElips;
    public double vLuasPermukaanCincinElips;

    public CincinElips(double vJariMinor, double vJariMayor, double R) {
        super(vJariMinor, vJariMayor);
        this.R = R;
    }

    @Override
    public double hitungVolume() throws Exception {
        if (R <= 0) throw new Exception("Jari-jari lintasan (R) harus lebih dari 0!");
        // Teorema Pappus: Luas Penampang Elips * Keliling Lintasan Putar (2 * PI * R) (AI)
        // super.hitungLuas() akan memvalidasi vJariMinor dan vJariMayor
        vVolumeCincinElips = super.hitungLuas() * 2.0 * PI * R;
        return vVolumeCincinElips;
    }

    // Overloading yang aman dan menjaga konsistensi state objek
    public double hitungVolume(double vJariMinor, double vJariMayor, double R) throws Exception {
        if (R <= 0) throw new Exception("Jari-jari lintasan (R) harus lebih dari 0!");
        this.R = R;
        // super.hitungLuas(overload) memvalidasi dan mengupdate vJariMinor & vJariMayor
        vVolumeCincinElips = super.hitungLuas(vJariMinor, vJariMayor) * 2.0 * PI * R;
        return vVolumeCincinElips;
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        if (R <= 0) throw new Exception("Jari-jari lintasan (R) harus lebih dari 0!");
        // Teorema Pappus: Keliling Penampang Elips * Keliling Lintasan Putar (2 * PI * R)
        // super.hitungKeliling() akan memvalidasi vJariMinor dan vJariMayor
        vLuasPermukaanCincinElips = super.hitungKeliling() * 2.0 * PI * R;
        return vLuasPermukaanCincinElips;
    }

    // Overloading yang aman dan menjaga konsistensi state objek
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double R) throws Exception {
        if (R <= 0) throw new Exception("Jari-jari lintasan (R) harus lebih dari 0!");
        this.R = R;
        // super.hitungKeliling(overload) memvalidasi dan mengupdate vJariMinor & vJariMayor
        vLuasPermukaanCincinElips = super.hitungKeliling(vJariMinor, vJariMayor) * 2.0 * PI * R;
        return vLuasPermukaanCincinElips;
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
            listener.onDone(threadId, "Cincin Elips (Torus)", finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Cincin Elips (Torus)", 0, 0, 0, e.getMessage());
        }
    }
}