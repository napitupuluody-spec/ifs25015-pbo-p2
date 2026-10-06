package domain.entity;

import java.util.Comparator;

/**
 * Kriteria pengurutan transaksi.
 * Setiap opsi membawa comparator-nya sendiri sehingga logika pengurutan
 * terpusat di domain, bukan tersebar sebagai angka ajaib di view/use case.
 */
public enum SortOption {
    /** Jumlah terbesar ditampilkan lebih dulu. */
    AMOUNT_DESC(Comparator.comparingLong(Transaction::getAmount).reversed()),

    /** Jumlah terkecil ditampilkan lebih dulu. */
    AMOUNT_ASC(Comparator.comparingLong(Transaction::getAmount)),

    /** Pemasukan ditampilkan lebih dulu, lalu pengeluaran. */
    INCOME_FIRST(Comparator.comparing(
            (Transaction t) -> t.getType() == TransactionType.INCOME ? 0 : 1)),

    /** Pengeluaran ditampilkan lebih dulu, lalu pemasukan. */
    EXPENSE_FIRST(Comparator.comparing(
            (Transaction t) -> t.getType() == TransactionType.EXPENSE ? 0 : 1));

    /** Comparator yang digunakan untuk mengurutkan daftar transaksi. */
    private final Comparator<Transaction> comparator;

    SortOption(Comparator<Transaction> comparator) {
        this.comparator = comparator;
    }

    /** Mengembalikan comparator yang sesuai dengan opsi ini. */
    public Comparator<Transaction> comparator() {
        return comparator;
    }
}
