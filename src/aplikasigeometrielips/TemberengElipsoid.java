package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class TemberengElipsoid extends BolaElipsoid implements BangunRuang, Runnable {
    public double tinggiPotongan; // Tinggi potongan dari ujung kutub (h)
    public double vVolumeTemberengElipsoid;
    public double vLuasPermukaanTemberengElipsoid;
    public double capArea;
    public double baseArea;
    public double ratio;

    public TemberengElipsoid(double vJariMinor, double vJariMayor, double vJariKedalaman, double tinggiPotongan) {
        super(vJariMinor, vJariMayor, vJariKedalaman);
        this.tinggiPotongan = tinggiPotongan;
    }

    @Override
    public double hitungVolume() throws Exception {
        if (tinggiPotongan <= 0 || tinggiPotongan > (2 * vJariKedalaman)) {
            throw new Exception("Tinggi potongan tidak valid. Harus lebih dari 0 dan maksimal diameter kedalaman (2 * r3).");
        }

        // Rumus volume kubah elipsoid menggunakan integrasi kalkulus (AI)
        vVolumeTemberengElipsoid = ((super.hitungLuas() * Math.pow(tinggiPotongan, 2)) / (3.0 * Math.pow(vJariKedalaman, 2))) * ((3.0 * vJariKedalaman) - tinggiPotongan);
        return vVolumeTemberengElipsoid;
    }

    // Overloading yang bersih dan menjaga konsistensi state objek
    public double hitungVolume(double vJariMinor, double vJariMayor, double vJariKedalaman, double tinggiPotongan) throws Exception {
        if (tinggiPotongan <= 0 || tinggiPotongan > (2 * vJariKedalaman)) {
            throw new Exception("Tinggi potongan tidak valid. Harus lebih dari 0 dan maksimal diameter kedalaman (2 * r3).");
        }
        this.vJariKedalaman = vJariKedalaman;
        this.tinggiPotongan = tinggiPotongan;

        // Rumus volume kubah elipsoid menggunakan integrasi kalkulus
        // super.hitungLuas(overload) memvalidasi dan mengupdate vJariMinor & vJariMayor
        vVolumeTemberengElipsoid = (super.hitungLuas(vJariMinor, vJariMayor) * Math.pow(tinggiPotongan, 2) / (3.0 * Math.pow(vJariKedalaman, 2))) * ((3.0 * vJariKedalaman) - tinggiPotongan);
        return vVolumeTemberengElipsoid;
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        if (tinggiPotongan <= 0 || tinggiPotongan > (2 * vJariKedalaman)) {
            throw new Exception("Tinggi potongan tidak valid. Harus lebih dari 0 dan maksimal diameter kedalaman (2 * r3).");
        }
        // 1. Luas Selimut Lengkung Kubah (Aproksimasi rasio tinggi terhadap total permukaan elipsoid)
        // super.hitungLuasPermukaan() akan memvalidasi semua jari-jari
        ratio = tinggiPotongan / (2.0 * vJariKedalaman);
        capArea = super.hitungLuasPermukaan() * ratio;

        // 2. Luas Alas Datar Hasil Potongan berbentuk elips sempurna
        baseArea = PI * vJariMayor * vJariMinor * (1.0 - Math.pow(1.0 - tinggiPotongan / vJariKedalaman, 2));

        vLuasPermukaanTemberengElipsoid = capArea + baseArea;
        return vLuasPermukaanTemberengElipsoid;
    }

    // Overloading yang bersih dan menjaga konsistensi state objek
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double vJariKedalaman, double tinggiPotongan) throws Exception {
        if (tinggiPotongan <= 0 || tinggiPotongan > (2 * vJariKedalaman)) {
            throw new Exception("Tinggi potongan tidak valid. Harus lebih dari 0 dan maksimal diameter kedalaman (2 * r3).");
        }
        this.tinggiPotongan = tinggiPotongan;
        ratio = tinggiPotongan / (2.0 * vJariKedalaman);
        // super.hitungLuasPermukaan(overload) memvalidasi dan mengupdate semua jari-jari
        capArea = super.hitungLuasPermukaan(vJariMinor, vJariMayor, vJariKedalaman) * ratio;
        baseArea = PI * this.vJariMayor * this.vJariMinor * (1.0 - Math.pow(1.0 - tinggiPotongan / this.vJariKedalaman, 2));
        vLuasPermukaanTemberengElipsoid = capArea + baseArea;
        return vLuasPermukaanTemberengElipsoid;
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

                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[Tembereng Ellipsoid-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            listener.onDone(threadId, "Tembereng Ellipsoid", finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Tembereng Ellipsoid", 0, 0, 0, e.getMessage());
        }
    }
}