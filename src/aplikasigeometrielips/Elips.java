package aplikasigeometrielips;

/**
 *
 * @author Mirwan Qolyubi
 */
public class Elips extends BangunGeometri implements BangunDatar, Runnable {
    
    public static final double PI = 3.14159265359;
    public double vJariMinor, vJariMayor;
    public double vKelilingElips, vLuasElips;
    
    public Elips(double vJariMinor, double vJariMayor) {
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
    }

    @Override
    public double hitungLuas() throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0) {
            throw new Exception("Jari sumbu minor atau mayor tidak boleh 0 atau kurang dari 0.");
        }
        vLuasElips = PI * vJariMayor * vJariMinor;
        return vLuasElips;
    }
    
    // Overloading
    public double hitungLuas(double vJariMinor, double vJariMayor) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0) {
            throw new Exception("Jari sumbu minor atau mayor tidak boleh 0 atau kurang dari 0.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        vLuasElips = PI * vJariMayor * vJariMinor;
        return vLuasElips;
    }

    @Override
    public double hitungKeliling() throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0) {
            throw new Exception("Jari sumbu minor atau mayor tidak boleh 0 atau kurang dari 0.");
        }
        
        // Rumus pendekatan Ramanujan untuk keliling elips (AI)
        vKelilingElips = PI * (3 * (vJariMayor + vJariMinor) - Math.sqrt((3 * vJariMayor + vJariMinor) * (vJariMayor + 3 * vJariMinor)));
        return vKelilingElips;
    }
    
    // Overloading yang lebih bersih
    public double hitungKeliling(double vJariMinor, double vJariMayor) throws Exception {
        if (vJariMinor <= 0 || vJariMayor <= 0) {
            throw new Exception("Jari sumbu minor atau mayor tidak boleh 0 atau kurang dari 0.");
        }
        this.vJariMinor = vJariMinor;
        this.vJariMayor = vJariMayor;
        vKelilingElips = PI * (3 * (vJariMayor + vJariMinor) - Math.sqrt((3 * vJariMayor + vJariMinor) * (vJariMayor + 3 * vJariMinor)));
        return vKelilingElips;
    }

    @Override
    public void run() {
        if (listener == null) return;
        long startMs = System.currentTimeMillis();
        double dummyResult = 0;
        double finalLuas = 0;
        double finalKeliling = 0;

        try {
            for (int i = 1; i <= iterasi; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                
                finalLuas = hitungLuas();
                finalKeliling = hitungKeliling(); 
                
                dummyResult += finalLuas + finalKeliling; // Mencegah dead-code elimination untuk keduanya
                
                listener.onProgress(threadId, (int)((i * 100.0) / iterasi), "[Elips-Th" + threadId + "] Proses jalan... Iterasi ke-" + i);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            long timeTaken = System.currentTimeMillis() - startMs;
            listener.onDone(threadId, "Elips", finalLuas, finalKeliling, timeTaken, null);
        } catch (Exception e) {
            listener.onDone(threadId, "Elips", 0, 0, 0, e.getMessage());
        }
    }
}