package domain.entity;

/**
 * Jenis transaksi keuangan.
 * Berada di layer domain karena merupakan bagian dari aturan inti aplikasi.
 */
public enum TransactionType {
    /** Uang masuk (menambah saldo). */
    INCOME,

    /** Uang keluar (mengurangi saldo). */
    EXPENSE
}
