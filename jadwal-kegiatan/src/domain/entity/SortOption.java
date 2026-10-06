package domain.entity;

import java.util.Comparator;
import java.util.List;

/**
 * Kriteria pengurutan kegiatan.
 * Setiap opsi membawa comparator-nya sendiri sehingga logika pengurutan
 * terpusat di domain, bukan tersebar sebagai angka ajaib di view/use case.
 */
public enum SortOption {
    /** Urutkan berdasarkan urutan hari dalam seminggu (Senin - Minggu), lalu waktu. */
    DAY(Comparator.comparingInt((Activity a) -> dayIndex(a.getDay()))
            .thenComparing(Activity::getTime)),

    /** Urutkan berdasarkan waktu (00:00 - 23:59), lalu urutan hari. */
    TIME(Comparator.comparing(Activity::getTime)
            .thenComparingInt((Activity a) -> dayIndex(a.getDay()))),

    /** Urutkan judul dari A ke Z (case-insensitive). */
    TITLE_ASC(Comparator.comparing(Activity::getTitle, String.CASE_INSENSITIVE_ORDER)),

    /** Urutkan judul dari Z ke A (case-insensitive). */
    TITLE_DESC(Comparator.comparing(Activity::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());

    /** Urutan hari dalam seminggu yang menjadi acuan pengurutan. */
    private static final List<String> DAYS =
            List.of("senin", "selasa", "rabu", "kamis", "jumat", "sabtu", "minggu");

    /** Comparator yang digunakan untuk mengurutkan daftar kegiatan. */
    private final Comparator<Activity> comparator;

    SortOption(Comparator<Activity> comparator) {
        this.comparator = comparator;
    }

    /** Mengembalikan comparator yang sesuai dengan opsi ini. */
    public Comparator<Activity> comparator() {
        return comparator;
    }

    /** Mengubah nama hari menjadi indeks urutan; hari tak dikenal diletakkan paling akhir. */
    private static int dayIndex(String day) {
        int index = DAYS.indexOf(day.trim().toLowerCase());
        return index >= 0 ? index : DAYS.size();
    }
}
