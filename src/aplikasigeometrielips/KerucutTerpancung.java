package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class KerucutTerpancung extends Elips implements BangunRuang, Runnable {
    public double vJariMinorAtas;
    public double vJariMayorAtas;
    public double vTinggiKerucutTerpancung;
    public double vVolumeKerucutTerpancung;
    public double vLuasPermukaanKerucutTerpancung;

    public KerucutTerpancung(double vJariMinorBawah, double vJariMayorBawah, double vJariMinorAtas, double vJariMayorAtas, double tinggi) {
        super(vJariMinorBawah, vJariMayorBawah);
        this.vJariMinorAtas = vJariMinorAtas;
        this.vJariMayorAtas = vJariMayorAtas;
        this.vTinggiKerucutTerpancung = tinggi;
    }

    @Override
    public double hitungVolume() throws Exception {
        if (vJariMinorAtas <= 0 || vJariMayorAtas <= 0 || vTinggiKerucutTerpancung <= 0) {
            throw new Exception("Jari-jari atas dan tinggi harus lebih besar dari 0.");
        }

        vVolumeKerucutTerpancung = (vTinggiKerucutTerpancung / 3.0) * (super.hitungLuas() + (PI * vJariMayorAtas * vJariMinorAtas) + Math.sqrt(super.hitungLuas() * (PI * vJariMayorAtas * vJariMinorAtas)));
        return vVolumeKerucutTerpancung;
    }

    // Overloading Volume
    public double hitungVolume(double vJariMinorBawah, double vJariMayorBawah, double vJariMinorAtas, double vJariMayorAtas, double tinggi) throws Exception {
        // Validasi parameter input langsung
        if (vJariMinorBawah <= 0 || vJariMayorBawah <= 0 || vJariMinorAtas <= 0 || vJariMayorAtas <= 0 || tinggi <= 0) {
            throw new Exception("Semua jari-jari dan tinggi harus lebih besar dari 0.");
        }
        
        // Simpan parameter input ke dalam atribut class
        this.vJariMinorAtas = vJariMinorAtas;
        this.vJariMayorAtas = vJariMayorAtas;
        this.vTinggiKerucutTerpancung = tinggi;
        
        // Gunakan pemanggilan super versi overloading untuk menghitung luas alas
        // Hitung volume menggunakan atribut yang sudah di-update
        vVolumeKerucutTerpancung = (this.vTinggiKerucutTerpancung / 3.0) * (super.hitungLuas(vJariMinor, vJariMayor) + (PI * this.vJariMayorAtas * this.vJariMinorAtas) + Math.sqrt(super.hitungLuas(vJariMinor, vJariMayor) * (PI * this.vJariMayorAtas * this.vJariMinorAtas)));
        return vVolumeKerucutTerpancung;
    }

    @Override
    public double hitungLuasPermukaan() throws Exception {
        if (vJariMinorAtas <= 0 || vJariMayorAtas <= 0 || vTinggiKerucutTerpancung <= 0) {
            throw new Exception("Jari-jari atas dan tinggi harus lebih besar dari 0.");
        }
        double a1 = super.hitungLuas(); // Luas Alas Bawah
        double a2 = PI * vJariMayorAtas * vJariMinorAtas; // Luas Alas Atas
        
        double rBawah = (vJariMayor + vJariMinor) / 2.0;
        double rAtas = (vJariMayorAtas + vJariMinorAtas) / 2.0;
        
        double slantHeight = Math.sqrt(Math.pow(rBawah - rAtas, 2) + vTinggiKerucutTerpancung * vTinggiKerucutTerpancung);
        double luasSelimut = PI * (rBawah + rAtas) * slantHeight;
        
        vLuasPermukaanKerucutTerpancung = a1 + a2 + luasSelimut;
        return vLuasPermukaanKerucutTerpancung;
    }

    // Overloading Luas Permukaan (SEKARANG SUDAH MENGEMBALIKAN VARIABEL, BUKAN METHOD)
    public double hitungLuasPermukaan(double vJariMinorBawah, double vJariMayorBawah, double vJariMinorAtas, double vJariMayorAtas, double tinggi) throws Exception {
        // 1. Validasi parameter input langsung
        if (vJariMinorBawah <= 0 || vJariMayorBawah <= 0 || vJariMinorAtas <= 0 || vJariMayorAtas <= 0 || tinggi <= 0) {
            throw new Exception("Semua jari-jari dan tinggi harus lebih besar dari 0.");
        }

        // 2. Simpan parameter input ke dalam atribut class untuk menjaga konsistensi state objek
        this.vJariMinor = vJariMinorBawah;
        this.vJariMayor = vJariMayorBawah;
        this.vJariMinorAtas = vJariMinorAtas;
        this.vJariMayorAtas = vJariMayorAtas;
        this.vTinggiKerucutTerpancung = tinggi;
        
        // 3. Lakukan kalkulasi secara independen / langsung
        double a1 = super.hitungLuas(vJariMayorBawah, vJariMinorBawah); // Luas Alas Bawah
        double a2 = PI * this.vJariMayorAtas * this.vJariMinorAtas; // Luas Alas Atas
        
        double rBawah = (this.vJariMayor + this.vJariMinor) / 2.0;
        double rAtas = (this.vJariMayorAtas + this.vJariMinorAtas) / 2.0;
        
        double slantHeight = Math.sqrt(Math.pow(rBawah - rAtas, 2) + this.vTinggiKerucutTerpancung * this.vTinggiKerucutTerpancung);
        double luasSelimut = PI * (rBawah + rAtas) * slantHeight;
        
        // 4. Simpan ke variabel kelas dan return variabel tersebut
        vLuasPermukaanKerucutTerpancung = a1 + a2 + luasSelimut;
        return vLuasPermukaanKerucutTerpancung;
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
                
                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[Kerucut Terpancung Elips-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            
            listener.onDone(threadId, "Kerucut Terpancung Elips", finalLuasPermukaan, finalVol, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Kerucut Terpancung Elips", 0, 0, 0, e.getMessage());
        }
    }
}