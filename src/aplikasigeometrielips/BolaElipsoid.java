package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class BolaElipsoid extends Elips implements BangunRuang, Runnable {
    public double vJariKedalaman;
    public double vVolumeBolaElipsoid;
    public double vLuasPermukaanBolaElipsoid;
    public double p = 1.6075; // Rumus pendekatan Knud Thomsen
    public double term; // Rumus pendekatan Knud Thomsen

    // Konstruktor Ellipsoid (3 jari-jari)
    public BolaElipsoid(double vJariMinor, double vJariMayor, double vJariKedalaman) {
        super(vJariMinor, vJariMayor);
        this.vJariKedalaman = vJariKedalaman;
    }

    @Override
    public double hitungVolume() throws Exception {
        if (vJariKedalaman <= 0) throw new Exception("Jari-jari kedalaman harus lebih besar dari 0.");
        // super.hitungLuas() sudah memvalidasi vJariMinor dan vJariMayor
        vVolumeBolaElipsoid = (4.0 / 3.0) * super.hitungLuas() * vJariKedalaman;
        return vVolumeBolaElipsoid;
    }

    // Overloading yang bersih dan menjaga sinkronisasi data objek
    public double hitungVolume(double vJariMinor, double vJariMayor, double vJariKedalaman) throws Exception {
        if (vJariKedalaman <= 0) throw new Exception("Jari-jari kedalaman harus lebih besar dari 0.");
        this.vJariKedalaman = vJariKedalaman;
        // super.hitungLuas(overload) memvalidasi dan mengupdate vJariMinor & vJariMayor
        vVolumeBolaElipsoid = (4.0 / 3.0) * super.hitungLuas(vJariMinor, vJariMayor) * vJariKedalaman;
        return vVolumeBolaElipsoid;
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || vJariKedalaman <= 0) {
            throw new Exception("Semua jari-jari harus lebih besar dari 0.");
        }

        // Rumus pendekatan Knud Thomsen (sangat akurat untuk elipsoid)
        term = (Math.pow(vJariMayor * vJariMinor, p) + Math.pow(vJariMayor * vJariKedalaman, p) + Math.pow(vJariMinor * vJariKedalaman, p)) / 3.0;
        vLuasPermukaanBolaElipsoid = 4.0 * PI * Math.pow(term, 1.0 / p);
        return vLuasPermukaanBolaElipsoid;
    }

    // Overloading yang bersih dan menjaga sinkronisasi data objek
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double vJariKedalaman) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || vJariKedalaman <= 0) {
            throw new Exception("Semua jari-jari harus lebih besar dari 0.");
        }
        
        // Rumus pendekatan Knud Thomsen (sangat akurat untuk elipsoid)
        term = (Math.pow(vJariMayor * vJariMinor, p) + Math.pow(vJariMayor * vJariKedalaman, p) + Math.pow(vJariMinor * vJariKedalaman, p)) / 3.0;
        vLuasPermukaanBolaElipsoid = 4.0 * PI * Math.pow(term, 1.0 / p);
        return vLuasPermukaanBolaElipsoid;
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

                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[Ellipsoid-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            listener.onDone(threadId, "Ellipsoid", finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Ellipsoid", 0, 0, 0, e.getMessage());
        }
    }
}