package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class JuringElipsoid extends BolaElipsoid implements BangunRuang, Runnable {
    public double sudut; // Sudut juring dalam derajat (0 - 360)

    public JuringElipsoid(double vJariMinor, double vJariMayor, double vJariKedalaman, double sudut) {
        super(vJariMinor, vJariMayor, vJariKedalaman);
        if (sudut <= 0 || sudut > 360) {
            throw new IllegalArgumentException("Sudut juring harus di antara 0 hingga 360 derajat.");
        }
        this.sudut = sudut;
    }

    @Override
    public double hitungVolume() throws Exception {
        // Volume juring adalah rasio sudut terhadap volume total elipsoid
        vVolume = (sudut / 360.0) * super.hitungVolume();
        return vVolume;
    }

    // Overloading yang bersih dan menjaga konsistensi state objek
    public double hitungVolume(double vJariMinor, double vJariMayor, double vJariKedalaman, double sudut) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || vJariKedalaman <= 0 || sudut <= 0 || sudut > 360) {
            throw new IllegalArgumentException("Parameter input tidak valid.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        this.vJariKedalaman = vJariKedalaman;
        this.sudut = sudut;
        return hitungVolume();
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        // 1. Luas Kulit Lengkung (Sesuai proporsi sudut juring)
        double capArea = (sudut / 360.0) * super.hitungLuasPermukaan();
        
        // 2. Luas Dua Dinding Datar Internal hasil potongan juring
        // Dinding berbentuk setengah elips dari pertemuan jari-jari utama dengan jari-jari kedalaman
        double luasDinding1 = PI * vJariMayor * vJariKedalaman;
        double luasDinding2 = PI * vJariMinor * vJariKedalaman;
        double flatArea = luasDinding1 + luasDinding2;
        
        // Jika sudut juring membentuk lingkaran penuh (360 derajat), tidak ada dinding datar eksternal
        if (sudut == 360.0) {
            vLuasPermukaan = capArea;
        } else {
            vLuasPermukaan = capArea + flatArea;
        }
        
        return vLuasPermukaan;
    }

    // Overloading yang bersih dan menjaga konsistensi state objek
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double vJariKedalaman, double sudut) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || vJariKedalaman <= 0 || sudut <= 0 || sudut > 360) {
            throw new IllegalArgumentException("Parameter input tidak valid.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        this.vJariKedalaman = vJariKedalaman;
        this.sudut = sudut;
        return hitungLuasPermukaan();
    }

    public double getSudut() {
        return sudut;
    }

    @Override
    public void run() {
        if (listener == null) return;
        long startMs = System.currentTimeMillis();
        double dummyResult = 0;
        double finalLuasPermukaan = 0; // PERBAIKAN: Penamaan variabel yang jelas untuk 3D
        double finalVol = 0;

        // Nama dinamis menyesuaikan jika juring berupa bola sempurna atau elipsoid
        String nama = isBolaSempurna() ? "Juring Bola" : "Juring Ellipsoid";

        try {
            for (int i = 1; i <= iterasi; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                
                // PERBAIKAN: Memanggil hitungLuasPermukaan() milik juring, bukan hitungLuas() milik Elips 2D
                finalLuasPermukaan = hitungLuasPermukaan();
                finalVol = hitungVolume();
                
                dummyResult += finalVol + finalLuasPermukaan; 
                
                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[" + nama + "-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            
            // PERBAIKAN: Mengirimkan data Luas Permukaan total yang benar ke listener
            listener.onDone(threadId, nama, finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, nama, 0, 0, 0, e.getMessage());
        }
    }
}