package adapter.presenter;

import domain.entity.Transaction;
import domain.entity.TransactionType;
import java.util.List;

/**
 * Presenter yang memformat data dari use case menjadi output layar.
 * Memisahkan logika tampilan dari entity, use case, dan view sehingga
 * format output bisa diubah tanpa menyentuh domain.
 */
public class FinancePresenter {

    /** Mengubah jenis transaksi menjadi label yang mudah dibaca. */
    private String typeLabel(TransactionType type) {
        return type == TransactionType.INCOME ? "Pemasukan" : "Pengeluaran";
    }

    /** Memformat satu transaksi menjadi baris teks untuk ditampilkan. */
    private String format(Transaction t) {
        return String.format("%d | %s | Rp %d | %s",
                t.getId(), t.getDescription(), t.getAmount(), typeLabel(t.getType()));
    }

    /**
     * Helper umum untuk menampilkan daftar transaksi.
     * Menampilkan pesan kosong jika list tidak berisi data.
     */
    private void printList(List<Transaction> list, String header, String emptyMessage) {
        System.out.println(header);

        if (list.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }

        for (Transaction t : list) {
            System.out.println(format(t));
        }
    }

    /** Menampilkan daftar semua transaksi beserta saldo. */
    public void showTransactions(List<Transaction> list, long balance) {
        printList(list, "Daftar Transaksi:", "- Belum ada transaksi!");
        System.out.printf("Saldo: Rp %d%n", balance);
    }

    /** Menampilkan hasil pencarian berdasarkan kata kunci. */
    public void showSearchResults(List<Transaction> list, String keyword) {
        printList(list, "Hasil Pencarian: \"" + keyword + "\"", "- Transaksi tidak ditemukan!");
    }

    /** Menampilkan daftar transaksi yang sudah diurutkan beserta saldo. */
    public void showSortedTransactions(List<Transaction> list, long balance) {
        printList(list, "Daftar Transaksi (Terurut):", "- Belum ada transaksi!");
        System.out.printf("Saldo: Rp %d%n", balance);
    }

    /** Menampilkan saldo saat ini. */
    public void showBalance(long balance) {
        System.out.printf("Saldo saat ini: Rp %d%n", balance);
    }

    /** Menampilkan pesan sukses setelah menambah transaksi. */
    public void showAddSuccess(Transaction t) {
        System.out.printf("Berhasil menambah transaksi: %s%n", format(t));
    }

    /** Menampilkan pesan sukses setelah menghapus transaksi. */
    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus transaksi.");
    }

    /** Menampilkan pesan gagal saat menghapus transaksi. */
    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus transaksi dengan ID: %d.%n", id);
    }

    /** Menampilkan pesan saat pilihan menu tidak dikenali. */
    public void showInvalidChoice() {
        System.out.println("[!] Pilihan tidak dimengerti.");
    }

    /** Menampilkan pesan saat ID yang dimasukkan tidak valid. */
    public void showInvalidId() {
        System.out.println("[!] ID tidak valid!");
    }

    /** Menampilkan pesan saat jumlah uang tidak valid. */
    public void showInvalidAmount() {
        System.out.println("[!] Jumlah tidak valid!");
    }

    /** Menampilkan pesan saat opsi pengurutan tidak valid. */
    public void showInvalidSortOption() {
        System.out.println("[!] Pilihan tidak valid!");
    }
}
