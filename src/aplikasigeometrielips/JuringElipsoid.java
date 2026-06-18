package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class JuringElipsoid extends BolaElipsoid implements BangunRuang, Runnable {
    public double sudut; // Sudut juring dalam derajat (0 - 360)
    public double vVolumeJuringElipsoid;
    public double vLuasPermukaanJuringElipsoid;
    public double capArea;
    public double flatArea;
    public double luasDinding1;
    public double luasDinding2;

    public JuringElipsoid(double vJariMinor, double vJariMayor, double vJariKedalaman, double sudut) {
        super(vJariMinor, vJariMayor, vJariKedalaman);
        this.sudut = sudut;
    }

    @Override
    public double hitungVolume() throws Exception {
        if (sudut <= 0 || sudut > 360) {
            throw new Exception("Sudut juring harus di antara 0 hingga 360 derajat.");
        }
        // Volume juring adalah rasio sudut terhadap volume total elipsoid
        // super.hitungVolume() akan memvalidasi semua jari-jari
        vVolumeJuringElipsoid = (sudut / 360.0) * super.hitungVolume();
        return vVolumeJuringElipsoid;
    }

    // Overloading yang bersih dan menjaga konsistensi state objek
    public double hitungVolume(double vJariMinor, double vJariMayor, double vJariKedalaman, double sudut) throws Exception {
        if (sudut <= 0 || sudut > 360) throw new Exception("Sudut juring harus di antara 0 hingga 360 derajat.");
        this.sudut = sudut;
        // super.hitungVolume(overload) memvalidasi dan mengupdate semua jari-jari
        vVolumeJuringElipsoid = (sudut / 360.0) * super.hitungVolume(vJariMinor, vJariMayor, vJariKedalaman);
        return vVolumeJuringElipsoid;
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        if (sudut <= 0 || sudut > 360) {
            throw new Exception("Sudut juring harus di antara 0 hingga 360 derajat.");
        }
        // 1. Luas Kulit Lengkung (Sesuai proporsi sudut juring)
        // super.hitungLuasPermukaan() akan memvalidasi semua jari-jari
        capArea = (sudut / 360.0) * super.hitungLuasPermukaan();

        // 2. Luas Dua Dinding Datar Internal hasil potongan juring
        // Dinding berbentuk setengah elips dari pertemuan jari-jari utama dengan jari-jari kedalaman
        luasDinding1 = PI * vJariMayor * vJariKedalaman;
        luasDinding2 = PI * vJariMinor * vJariKedalaman;
        flatArea = luasDinding1 + luasDinding2;

        // Jika sudut juring membentuk lingkaran penuh (360 derajat), tidak ada dinding datar eksternal
        if (sudut == 360.0) {
            vLuasPermukaanJuringElipsoid = capArea;
        } else {
            vLuasPermukaanJuringElipsoid = capArea + flatArea;
        }

        return vLuasPermukaanJuringElipsoid;
    }

    // Overloading yang bersih dan menjaga konsistensi state objek
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double vJariKedalaman, double sudut) throws Exception {
        if (sudut <= 0 || sudut > 360) throw new Exception("Sudut juring harus di antara 0 hingga 360 derajat.");
        this.sudut = sudut;
        // super.hitungLuasPermukaan(overload) memvalidasi dan mengupdate semua jari-jari
        capArea = (sudut / 360.0) * super.hitungLuasPermukaan(vJariMinor, vJariMayor, vJariKedalaman);
        luasDinding1 = PI * this.vJariMayor * this.vJariKedalaman;
        luasDinding2 = PI * this.vJariMinor * this.vJariKedalaman;
        flatArea = luasDinding1 + luasDinding2;
        if (sudut == 360.0) {
            vLuasPermukaanJuringElipsoid = capArea;
        } else {
            vLuasPermukaanJuringElipsoid = capArea + flatArea;
        }
        return vLuasPermukaanJuringElipsoid;
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

                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[Juring Ellipsoid-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            listener.onDone(threadId, "Juring Ellipsoid", finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Juring Ellipsoid", 0, 0, 0, e.getMessage());
        }
    }
}