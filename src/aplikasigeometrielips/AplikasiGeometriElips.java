package aplikasigeometrielips;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Mirwan Qolyubi
 */
public class AplikasiGeometriElips {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=================================================");
        System.out.println("        KALKULATOR GEOMETRI ELIPS SYSTEM         ");
        System.out.println("=================================================");

        while (running) {
            System.out.println("\n=== KALKULATOR GEOMETRI ELIPS ===");
            System.out.println("1. Elips (2D)");
            System.out.println("2. Tabung Elips (3D)");
            System.out.println("3. Kerucut Elips (3D)");
            System.out.println("4. Kerucut Terpancung Elips (3D)");
            System.out.println("5. Cincin Elips / Torus (3D)");
            System.out.println("6. Ellipsoid / Bola (3D)");
            System.out.println("7. Stress Test Multithreading");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu (0-7): ");

            String input = scanner.nextLine().trim();
            try {
                switch (input) {
                    case "1": {
                        System.out.println("\n--- 📐 INPUT DIMENSI ELIPS 2D ---");
                        double minor = parseDoubleInput(scanner, "Masukkan Jari-jari Minor (r1) (cm): ");
                        double mayor = parseDoubleInput(scanner, "Masukkan Jari-jari Mayor (r2) (cm): ");
                        printHasil(new Elips(minor, mayor));
                        break;
                    }
                    case "2": {
                        System.out.println("\n--- 🛢️ INPUT DIMENSI TABUNG ELIPS ---");
                        double minor = parseDoubleInput(scanner, "Masukkan Jari-jari Minor (r1) (cm): ");
                        double mayor = parseDoubleInput(scanner, "Masukkan Jari-jari Mayor (r2) (cm): ");
                        double t = parseDoubleInput(scanner, "Masukkan Tinggi Tabung (t) (cm): ");
                        printHasil(new Tabung(minor, mayor, t));
                        break;
                    }
                    case "3": {
                        System.out.println("\n--- 📐 INPUT DIMENSI KERUCUT ELIPS ---");
                        double minor = parseDoubleInput(scanner, "Masukkan Jari-jari Minor (r1) (cm): ");
                        double mayor = parseDoubleInput(scanner, "Masukkan Jari-jari Mayor (r2) (cm): ");
                        double t = parseDoubleInput(scanner, "Masukkan Tinggi Kerucut (t) (cm): ");
                        printHasil(new Kerucut(minor, mayor, t));
                        break;
                    }
                    case "4": {
                        System.out.println("\n--- 📐 INPUT DIMENSI KERUCUT TERPANCUNG ---");
                        double minor = parseDoubleInput(scanner, "Masukkan Jari-jari Minor Alas (r1) (cm): ");
                        double mayor = parseDoubleInput(scanner, "Masukkan Jari-jari Mayor Alas (r2) (cm): ");
                        double minorAtas = parseDoubleInput(scanner, "Masukkan Jari Minor Atap (r1_atas) (cm): ");
                        double mayorAtas = parseDoubleInput(scanner, "Masukkan Jari Mayor Atap (r2_atas) (cm): ");
                        double t = parseDoubleInput(scanner, "Masukkan Tinggi (t) (cm): ");
                        printHasil(new KerucutTerpancung(minor, mayor, minorAtas, mayorAtas, t));
                        break;
                    }
                    case "5": {
                        System.out.println("\n--- 🍩 INPUT DIMENSI CINCIN ELIPS ---");
                        double minor = parseDoubleInput(scanner, "Masukkan Jari-jari Minor (r1) (cm): ");
                        double mayor = parseDoubleInput(scanner, "Masukkan Jari-jari Mayor (r2) (cm): ");
                        double rLintasan = parseDoubleInput(scanner, "Masukkan Jari-jari Lintasan Putar (R) (cm): ");
                        printHasil(new CincinElips(minor, mayor, rLintasan));
                        break;
                    }
                    case "6": {
                        menuEllipsoid(scanner);
                        break;
                    }
                    case "7": {
                        mulaiStressTest(scanner);
                        break;
                    }
                    case "0": {
                        System.out.print("Yakin ingin keluar? (y/n): ");
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        if (confirm.equals("y") || confirm.equals("ya")) {
                            running = false;
                            System.out.println("Terima kasih telah menggunakan Kalkulator Geometri!");
                        }
                        break;
                    }
                    default:
                        System.out.println("Pilihan tidak valid! Masukkan angka 0-7.");
                }
            } catch (Exception e) {
                System.out.println("\n🚨 Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void menuEllipsoid(Scanner scanner) throws Exception {
        System.out.println("\n--- 📐 INPUT DIMENSI ELLIPSOID 3D ---");
        double minor = parseDoubleInput(scanner, "Masukkan Jari-jari Minor (r1) (cm): ");
        double mayor = parseDoubleInput(scanner, "Masukkan Jari-jari Mayor (r2) (cm): ");
        double r3 = parseDoubleInput(scanner, "Masukkan Jari-jari Kedalaman (r3) (cm): ");

        System.out.print("\nApakah ingin memotong Ellipsoid ini? (y/n): ");
        String potong = scanner.nextLine().trim().toLowerCase();
        if (potong.equals("y") || potong.equals("ya")) {
            System.out.println("1. Potong Sudut (Juring)");
            System.out.println("2. Potong Tinggi (Tembereng)");
            System.out.print("Pilih jenis potongan (1-2): ");
            String jenisPotong = scanner.nextLine().trim();
            if (jenisPotong.equals("1")) {
                double sudut = parseDoubleInput(scanner, "Masukkan Sudut Potong (derajat, 0-360): ");
                if (sudut > 360.0) sudut = 360.0;
                printHasil(new JuringElipsoid(minor, mayor, r3, sudut));
            } else if (jenisPotong.equals("2")) {
                double h = parseDoubleInput(scanner, "Masukkan Tinggi Potongan (h) (cm): ");
                if (h > 2 * r3) {
                    System.out.printf("  🚨 Tinggi potongan tidak boleh melebihi diameter (2 * r3 = %.2f cm)!\n", 2 * r3);
                } else {
                    printHasil(new TemberengElipsoid(minor, mayor, r3, h));
                }
            } else {
                System.out.println("Batal memotong. Menampilkan Ellipsoid utuh.");
                printHasil(new BolaElipsoid(minor, mayor, r3));
            }
        } else {
            printHasil(new BolaElipsoid(minor, mayor, r3));
        }
    }

    private static void mulaiStressTest(Scanner scanner) {
        System.out.println("\n--- Setup Stress Test Multithreading ---");
        System.out.println("Pilih bangun untuk dites:");
        System.out.println("1. Elips (2D)");
        System.out.println("2. Tabung Elips");
        System.out.println("3. Kerucut Elips");
        System.out.println("4. Kerucut Terpancung Elips");
        System.out.println("5. Cincin Elips");
        System.out.println("6. Ellipsoid Utuh");
        System.out.println("7. Juring Ellipsoid");
        System.out.println("8. Tembereng Ellipsoid");
        int jenis = parseIntInput(scanner, "Pilih jenis bangun (1-8): ", 1, 8);

        int threads = parseIntInput(scanner, "Masukkan jumlah thread per bangun (default 4): ", 1, 100);
        int iterasi = parseIntInput(scanner, "Masukkan jumlah iterasi per thread (default 500): ", 1, 100000);

        System.out.println("\n=================================================");
        System.out.println("  MULAI STRESS TEST MULTITHREADING (KONKUREN)    ");
        System.out.println("  Jumlah Thread      : " + threads);
        System.out.println("  Iterasi per Thread : " + iterasi);
        System.out.println("=================================================\n");

        long globalStartTime = System.currentTimeMillis();
        final AtomicInteger activeThreadsCount = new AtomicInteger(threads);
        final int totalThreads = threads;

        MultithreadListener listener = new MultithreadListener() {
            @Override
            public void onProgress(int id, int progressPercent, String logMsg) {
                System.out.println(logMsg + " (" + progressPercent + "%)");
            }

            @Override
            public void onDone(int id, String shapeName, double finalLuas, double finalVolume, long timeTakenMs, String error) {
                if (error == null) {
                    System.out.printf("[%s-Th%d] [OK] Luas Permukaan/Akhir: %.2f", shapeName, id, finalLuas);
                    if (finalVolume > 0) {
                        System.out.printf(", Volume Akhir: %.2f", finalVolume);
                    }
                    System.out.printf(" | Waktu: %d ms.\n", timeTakenMs);
                } else {
                    System.err.printf("[%s-Th%d] Error: %s\n", shapeName, id, error);
                }
                
                if (activeThreadsCount.decrementAndGet() == 0) {
                    long totalTime = System.currentTimeMillis() - globalStartTime;
                    System.out.println("-------------------------------------------------");
                    System.out.println("Semua proses (" + totalThreads + " Thread) selesai dalam " + totalTime + " ms.");
                    System.out.println("-------------------------------------------------");
                }
            }
        };

        // Spawn threads
        for (int t = 0; t < threads; t++) {
            final int threadId = t + 1;
            Elips bangun;
            
            // Dummy valid dimensions
            double r1 = 3.0;
            double r2 = 5.0;
            double r3 = 4.0;
            double h = 10.0;

            switch (jenis) {
                case 1: bangun = new Elips(r1, r2); break;
                case 2: bangun = new Tabung(r1, r2, h); break;
                case 3: bangun = new Kerucut(r1, r2, h); break;
                case 4: bangun = new KerucutTerpancung(r1, r2, 1.5, 2.5, h); break;
                case 5: bangun = new CincinElips(r1, r2, 8.0); break;
                case 6: bangun = new BolaElipsoid(r1, r2, r3); break;
                case 7: bangun = new JuringElipsoid(r1, r2, r3, 120.0); break;
                case 8: bangun = new TemberengElipsoid(r1, r2, r3, 2.0); break;
                default: bangun = new Elips(r1, r2);
            }

            bangun.multithread(iterasi, threadId, listener);
            Thread threadObj = new Thread(bangun);
            threadObj.start();
        }

        // Synchronous waiting loop untuk mengunci menu CLI sampai stress test rampung
        while (activeThreadsCount.get() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public static void printHasil(BangunGeometri b) {
        System.out.println("\n=============================================");
        System.out.println("                HASIL PERHITUNGAN            ");
        System.out.println("=============================================");
        try {
            if (b instanceof Elips) {
                Elips el = (Elips) b;
                System.out.println("Nama Bangun           : " + getNamaBangun(el));
                System.out.printf("Jari-jari Minor (r1)  : %.4f cm\n", el.vJariMinor);
                System.out.printf("Jari-jari Mayor (r2)  : %.4f cm\n", el.vJariMayor);
                System.out.printf("Luas Alas/Penampang   : %.4f cm²\n", el.hitungLuas());
                System.out.printf("Keliling Alas         : %.4f cm\n", el.hitungKeliling());
                
                // Urutan dari yang paling spesifik (Subclass terbawah)
                if (el instanceof JuringElipsoid) {
                    JuringElipsoid jr = (JuringElipsoid) el;
                    System.out.printf("Jari-jari Kedalaman(r3): %.4f cm\n", jr.getVJariKedalaman());
                    System.out.printf("Sudut Potongan Sektor : %.1f derajat\n", jr.getSudut());
                } else if (el instanceof TemberengElipsoid) {
                    TemberengElipsoid tm = (TemberengElipsoid) el;
                    System.out.printf("Jari-jari Kedalaman(r3): %.4f cm\n", tm.getVJariKedalaman());
                    System.out.printf("Tinggi Potongan Kubah : %.4f cm\n", tm.getTinggiPotongan());
                } else if (el instanceof BolaElipsoid) {
                    BolaElipsoid be = (BolaElipsoid) el;
                    System.out.printf("Jari-jari Kedalaman(r3): %.4f cm\n", be.getVJariKedalaman());
                } else if (el instanceof Tabung) {
                    Tabung tb = (Tabung) el;
                    System.out.printf("Tinggi Tabung (t)     : %.4f cm\n", tb.getTinggi());
                } else if (el instanceof Kerucut) {
                    Kerucut kc = (Kerucut) el;
                    System.out.printf("Tinggi Kerucut (t)    : %.4f cm\n", kc.getTinggi());
                } else if (el instanceof KerucutTerpancung) {
                    KerucutTerpancung kt = (KerucutTerpancung) el;
                    System.out.printf("Jari Minor Atap (r1_a): %.4f cm\n", kt.getVJariMinorAtas());
                    System.out.printf("Jari Mayor Atap (r2_a): %.4f cm\n", kt.getVJariMayorAtas());
                    System.out.printf("Tinggi Terpancung (t) : %.4f cm\n", kt.getTinggi());
                } else if (el instanceof CincinElips) {
                    CincinElips cn = (CincinElips) el;
                    System.out.printf("Jari Lintasan Torus(R): %.4f cm\n", cn.getR());
                }

                // Cetak Luas Permukaan & Volume jika mendukung BangunRuang 3D
                if (el instanceof BangunRuang) {
                    BangunRuang br = (BangunRuang) el;
                    System.out.printf("Volume Benda 3D       : %.4f cm³\n", br.hitungVolume());
                    System.out.printf("Luas Permukaan 3D     : %.4f cm²\n", br.hitungLuasPermukaan());
                }
            }
        } catch (Exception e) {
            System.out.println("🚨 Error saat menghitung: " + e.getMessage());
        }
        System.out.println("=============================================");
    }

    public static String getNamaBangun(Elips el) {
        if (el instanceof Tabung) return "Tabung Elips";
        if (el instanceof Kerucut) return "Kerucut Elips";
        if (el instanceof KerucutTerpancung) return "Kerucut Terpancung Elips";
        if (el instanceof CincinElips) return "Cincin Elips (Torus)";
        if (el instanceof JuringElipsoid) {
            return ((JuringElipsoid) el).isBolaSempurna() ? "Juring Bola Sempurna" : "Juring Ellipsoid";
        }
        if (el instanceof TemberengElipsoid) {
            return ((TemberengElipsoid) el).isBolaSempurna() ? "Tembereng Bola Sempurna" : "Tembereng Ellipsoid";
        }
        if (el instanceof BolaElipsoid) {
            return ((BolaElipsoid) el).isBolaSempurna() ? "Bola Sempurna" : "Ellipsoid Utuh";
        }
        return "Elips (Alas 2D)";
    }

    private static double parseDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("  🚨 Input tidak boleh kosong!");
                continue;
            }
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println("  🚨 Input harus bernilai positif!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  🚨 Input harus berupa angka desimal valid!");
            }
        }
    }

    private static int parseIntInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("  🚨 Input tidak boleh kosong!");
                continue;
            }
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.printf("  🚨 Input harus di antara %d dan %d!\n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  🚨 Input harus berupa angka bulat valid!");
            }
        }
    }
}