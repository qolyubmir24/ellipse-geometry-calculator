package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class BolaElipsoid extends Elips implements BangunRuang, Runnable {
    public double vJariKedalaman;

    // Konstruktor Ellipsoid (3 jari-jari)
    public BolaElipsoid(double vJariMinor, double vJariMayor, double vJariKedalaman) {
        super(vJariMinor, vJariMayor);
        if (vJariKedalaman <= 0) {
            throw new IllegalArgumentException("Jari-jari kedalaman (r3) harus lebih besar dari 0.");
        }
        this.vJariKedalaman = vJariKedalaman;
    }

    // Konstruktor Bola Sempurna (1 jari-jari)
    public BolaElipsoid(double r) {
        super(r, r);
        this.vJariKedalaman = r;
    }

    @Override
    public double hitungVolume() throws Exception {
        vVolume = (4.0 / 3.0) * PI * vJariMinor * vJariMayor * vJariKedalaman;
        return vVolume;
    }

    // Overloading yang bersih dan menjaga sinkronisasi data objek
    public double hitungVolume(double vJariMinor, double vJariMayor, double vJariKedalaman) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || vJariKedalaman <= 0) {
            throw new IllegalArgumentException("Jari-jari harus lebih besar dari 0.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        this.vJariKedalaman = vJariKedalaman;
        return hitungVolume();
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        double a = vJariMayor;
        double b = vJariMinor;
        double c = vJariKedalaman;
        
        if (isBolaSempurna()) {
            vLuasPermukaan = 4.0 * PI * a * a;
            return vLuasPermukaan;
        }
        
        // Rumus pendekatan Knud Thomsen (sangat akurat untuk elipsoid)
        double p = 1.6075;
        double term = (Math.pow(a * b, p) + Math.pow(a * c, p) + Math.pow(b * c, p)) / 3.0;
        vLuasPermukaan = 4.0 * PI * Math.pow(term, 1.0 / p);
        return vLuasPermukaan;
    }

    // Overloading yang bersih dan menjaga sinkronisasi data objek
    public double hitungLuasPermukaan(double vJariMinor, double vJariMayor, double vJariKedalaman) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0 || vJariKedalaman <= 0) {
            throw new IllegalArgumentException("Jari-jari harus lebih besar dari 0.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        this.vJariKedalaman = vJariKedalaman;
        return hitungLuasPermukaan();
    }

    public double getVJariKedalaman() {
        return vJariKedalaman;
    }

    public boolean isBolaSempurna() {
        return vJariMinor == vJariMayor && vJariMayor == vJariKedalaman;
    }

    @Override
    public void run() {
        if (listener == null) return;
        long startMs = System.currentTimeMillis();
        double dummyResult = 0;
        double finalLuasPermukaan = 0; // PERBAIKAN: Penamaan variabel yang jelas
        double finalVol = 0;
        
        // OPTIMASI: Nama dinamis berdasarkan bentuk aslinya
        String nama = isBolaSempurna() ? "Bola Sempurna" : "Ellipsoid";

        try {
            for (int i = 1; i <= iterasi; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                
                // PERBAIKAN: Memanggil hitungLuasPermukaan() milik Elipsoid, bukan hitungLuas() milik Elips 2D
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
            
            // PERBAIKAN: Mengirimkan data Luas Permukaan nyata ke listener
            listener.onDone(threadId, nama, finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, nama, 0, 0, 0, e.getMessage());
        }
    }
}