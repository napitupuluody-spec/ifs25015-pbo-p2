package framework.view;

import adapter.presenter.FinancePresenter;
import domain.entity.SortOption;
import domain.entity.Transaction;
import domain.entity.TransactionType;
import framework.util.InputUtil;
import java.util.List;
import usecase.FinanceUseCase;

/**
 * Tampilan konsol aplikasi catatan keuangan.
 * Menerima input user, memanggil use case, dan menampilkan hasil via presenter.
 * Layer ini tidak mengandung logika bisnis — hanya menangani interaksi user.
 */
public class FinanceView {
    /** Use case yang menjalankan operasi bisnis. */
    private final FinanceUseCase financeUseCase;

    /** Presenter yang memformat hasil ke output layar. */
    private final FinancePresenter presenter;

    public FinanceView(FinanceUseCase financeUseCase, FinancePresenter presenter) {
        this.financeUseCase = financeUseCase;
        this.presenter = presenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            // Tampilkan daftar transaksi dan saldo terkini sebelum menu
            presenter.showTransactions(
                    financeUseCase.getAllTransactions(), financeUseCase.getBalance());
            printMenu();

            String input = InputUtil.input("Pilih");
            switch (input) {
                case "1" -> addTransaction(TransactionType.INCOME);
                case "2" -> addTransaction(TransactionType.EXPENSE);
                case "3" -> searchTransaction();
                case "4" -> sortTransaction();
                case "5" -> presenter.showBalance(financeUseCase.getBalance());
                case "6" -> removeTransaction();
                case "x" -> running = false;
                default -> presenter.showInvalidChoice();
            }

            if (running) {
                System.out.println();
            }
        }
    }

    /** Mencetak opsi menu ke layar. */
    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Tambah Pemasukan");
        System.out.println("2. Tambah Pengeluaran");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Lihat Saldo");
        System.out.println("6. Hapus");
        System.out.println("x. Keluar");
    }

    /** Form tambah transaksi (pemasukan atau pengeluaran). */
    private void addTransaction(TransactionType type) {
        System.out.println(type == TransactionType.INCOME
                ? "[Tambah Pemasukan]" : "[Tambah Pengeluaran]");

        String description = InputUtil.input("Keterangan (x Jika Batal)");
        if (description.equals("x")) {
            return;
        }

        String strAmount = InputUtil.input("Jumlah");
        if (strAmount.equals("x")) {
            return;
        }

        Long amount = parseAmount(strAmount);
        if (amount == null) {
            presenter.showInvalidAmount();
            return;
        }

        presenter.showAddSuccess(financeUseCase.addTransaction(description, amount, type));
    }

    /** Form hapus transaksi berdasarkan ID. */
    private void removeTransaction() {
        System.out.println("[Hapus Transaksi]");
        String strId = InputUtil.input("ID Transaksi (x Jika Batal)");

        if (strId.equals("x")) {
            return;
        }

        Integer id = parseId(strId);
        if (id == null) {
            return;
        }

        if (financeUseCase.removeTransaction(id)) {
            presenter.showRemoveSuccess();
        } else {
            presenter.showRemoveFailed(id);
        }
    }

    /** Form cari transaksi berdasarkan keterangan. */
    private void searchTransaction() {
        System.out.println("[Cari Transaksi]");
        String keyword = InputUtil.input("Kata Kunci (x Jika Batal)");

        if (!keyword.equals("x")) {
            presenter.showSearchResults(financeUseCase.searchTransactions(keyword), keyword);
        }
    }

    /** Form urutkan transaksi berdasarkan pilihan user. */
    private void sortTransaction() {
        System.out.println("[Urutkan Transaksi]");
        System.out.println("1. Jumlah (Terkecil)");
        System.out.println("2. Jumlah (Terbesar)");
        System.out.println("3. Pemasukan Dulu");
        System.out.println("4. Pengeluaran Dulu");
        System.out.println("x. Batal");

        String input = InputUtil.input("Pilih");
        if (input.equals("x")) {
            return;
        }

        // Konversi input angka ke enum domain
        SortOption option = mapSortOption(input);
        if (option == null) {
            presenter.showInvalidSortOption();
            return;
        }

        printSorted(financeUseCase.sortTransactions(option));
    }

    /** Mencetak daftar transaksi terurut (tanpa saldo). */
    private void printSorted(List<Transaction> list) {
        System.out.println("Daftar Transaksi (Terurut):");
        if (list.isEmpty()) {
            System.out.println("- Belum ada transaksi!");
            return;
        }
        for (Transaction t : list) {
            System.out.printf("%d | %s | Rp %d | %s%n", t.getId(), t.getDescription(),
                    t.getAmount(),
                    t.getType() == TransactionType.INCOME ? "Pemasukan" : "Pengeluaran");
        }
    }

    /**
     * Mengonversi input string menjadi ID.
     *
     * @return ID jika valid, null jika tidak valid (error sudah ditampilkan)
     */
    private Integer parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            presenter.showInvalidId();
            return null;
        }
    }

    /**
     * Mengonversi input string menjadi jumlah uang.
     *
     * @return jumlah jika numerik dan lebih dari 0, null jika tidak valid
     */
    private Long parseAmount(String value) {
        try {
            long amount = Long.parseLong(value);
            return amount > 0 ? amount : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Memetakan pilihan menu (1-4) ke {@link SortOption} domain. */
    private SortOption mapSortOption(String input) {
        return switch (input) {
            case "1" -> SortOption.AMOUNT_ASC;
            case "2" -> SortOption.AMOUNT_DESC;
            case "3" -> SortOption.INCOME_FIRST;
            case "4" -> SortOption.EXPENSE_FIRST;
            default -> null;
        };
    }
}