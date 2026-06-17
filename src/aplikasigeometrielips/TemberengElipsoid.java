package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class TemberengElipsoid extends BolaElipsoid implements BangunRuang, Runnable {
    public double tinggiPotongan; // Tinggi potongan dari ujung kutub (h)

    public TemberengElipsoid(double vJariMinor, double vJariMayor, double vJariKedalaman, double tinggiPotongan) {
        super(vJariMinor, vJariMayor, vJariKedalaman);
        if (tinggiPotongan <= 0 || tinggiPotongan > (2 * vJariKedalaman)) {
            throw new IllegalArgumentException("Tinggi potongan tidak valid. Harus lebih dari 0 dan maksimal diameter kedalaman (2 * r3).");
        }
        this.tinggiPotongan = tinggiPotongan;
    }

    @Override
    public double hitungVolume() throws Exception {
        double h = tinggiPotongan;
        double r3 = vJariKedalaman;
        // Rumus volume kubah elipsoid menggunakan integrasi kalkulus
        double pengali = (PI * vJariMayor * vJariMinor * Math.pow(h, 2)) / (3.0 * Math.pow(r3, 2));
        double sisa = (3.0 * r3) - h;
        vVolume = pengali * sisa;
        return vVolume;
    }

    // Overloading yang bersih dan menjaga konsistensi state objek
    public double hitungVolume(double vJariMinor, double vJariMayor, double vJariKedalaman, double tinggiPotongan) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || vJariKedalaman <= 0 || tinggiPotongan <= 0 || tinggiPotongan > (2 * vJariKedalaman)) {
            throw new IllegalArgumentException("Parameter input tidak valid.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        this.vJariKedalaman = vJariKedalaman;
        this.tinggiPotongan = tinggiPotongan;
        return hitungVolume();
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        // 1. Luas Selimut Lengkung Kubah (Aproksimasi rasio tinggi terhadap total permukaan elipsoid)
        double ratio = tinggiPotongan / (2.0 * vJariKedalaman);
        double capArea = super.hitungLuasPermukaan() * ratio;
        
        // 2. Luas Alas Datar Hasil Potongan berbentuk elips sempurna
        double baseArea = PI * vJariMayor * vJariMinor * (1.0 - Math.pow(1.0 - tinggiPotongan / vJariKedalaman, 2));
        
        vLuasPermukaan = capArea + baseArea;
        return vLuasPermukaan;
    }

    // Overloading yang bersih dan menjaga konsistensi state objek
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double vJariKedalaman, double tinggiPotongan) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || vJariKedalaman <= 0 || tinggiPotongan <= 0 || tinggiPotongan > (2 * vJariKedalaman)) {
            throw new IllegalArgumentException("Parameter input tidak valid.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        this.vJariKedalaman = vJariKedalaman;
        this.tinggiPotongan = tinggiPotongan;
        return hitungLuasPermukaan();
    }

    public double getTinggiPotongan() {
        return tinggiPotongan;
    }

    @Override
    public void run() {
        if (listener == null) return;
        long startMs = System.currentTimeMillis();
        double dummyResult = 0;
        double finalLuasPermukaan = 0; // PERBAIKAN: Penamaan variabel yang jelas untuk objek 3D
        double finalVol = 0;

        // Penamaan dinamis mendeteksi jika basis geometrinya bola sempurna
        String nama = isBolaSempurna() ? "Tembereng Bola" : "Tembereng Ellipsoid";

        try {
            for (int i = 1; i <= iterasi; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                
                // PERBAIKAN: Memanggil hitungLuasPermukaan() dari tembereng, bukan hitungLuas() elips 2D
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
            
            // PERBAIKAN: Mengirim data Luas Permukaan total yang valid ke listener
            listener.onDone(threadId, nama, finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, nama, 0, 0, 0, e.getMessage());
        }
    }
}