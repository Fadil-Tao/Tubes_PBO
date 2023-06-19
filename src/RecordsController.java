
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model_class.expense;
import model_class.income;

public class RecordsController {

    @FXML
    private AnchorPane root;
    @FXML
    private AnchorPane side_anchor;

    @FXML
    private Label ExpenseText;
    @FXML
    private Label incomeText;

    // ====================================== Tabel pengeluaran atribute
    // ======================================
    @FXML
    public TableView<expense> table_expense = new TableView<expense>();
    @FXML
    TableColumn<expense, Date> Expense_Date;

    @FXML
    TableColumn<expense, String> Expense_Category;
    @FXML
    TableColumn<expense, Double> Expense_Ammount;

    // ====================================== tabel pemasukan atribute
    // ======================================
    @FXML
    private TableView<income> table_income;
    @FXML
    private TableColumn<income, Double> Income_Ammount;

    @FXML
    private TableColumn<income, String> Income_Category;

    @FXML
    private TableColumn<income, Date> Income_Date;

    // ===================================================================================================
    // Membuat ObservableList List1 untuk menyimpan daftar objek expense.
    ObservableList<expense> List1 = FXCollections.observableArrayList();
    // Membuat ObservableList List2 untuk menyimpan daftar objek income.
    ObservableList<income> List2 = FXCollections.observableArrayList();

    // ====================================== Menu dashboard

    // ======================================

    // Ketika tombol analisis di klik maka akan berubah scene dan fungsi ini akan
    // bekerja
    @FXML
    void Analytics_Clicked(ActionEvent event) throws Exception {
        Parent analyticsParent = FXMLLoader.load(getClass().getResource("Analytics_Scene.fxml"));
        Scene AnalyticsScene = new Scene(analyticsParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AnalyticsScene);
        window.show();
    }

    // Ketika tombol Records di klik maka akan berubah scene dan fungsi ini akan
    // bekerja
    @FXML
    void Records_Clicked(ActionEvent event) throws Exception {
        Parent RecordsParent = FXMLLoader.load(getClass().getResource("Record_Scene.fxml"));
        Scene RecordsScene = new Scene(RecordsParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(RecordsScene);
        window.show();
    }

    // Ketika tombol Budgets di klik maka akan berubah scene dan fungsi ini akan
    // bekerja
    @FXML
    void budgets_Clicked(ActionEvent event) throws Exception {
        Parent RecordsParent = FXMLLoader.load(getClass().getResource("Budget_Scene.fxml"));
        Scene RecordsScene = new Scene(RecordsParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(RecordsScene);
        window.show();
    }

    // ====================================== tombol untuk catatan pengeluaran dan
    // pemasukan
    // ======================================

    // Ketika tombol + yang berada di panel pengeluaran diklik maka akan memunculkan
    // sebuah scene pop up
    @FXML
    void Add_Expense(ActionEvent event) throws Exception {
        Parent root1 = FXMLLoader.load(getClass().getResource("Expense_Scene.fxml"));
        Scene scene = new Scene(root1);
        Stage childStage = new Stage();
        childStage.setScene(scene);
        childStage.setTitle("Expense");
        childStage.show();
    }

    // Ketika tombol + yang berada di panel pemasukan diklik maka akan memunculkan
    // sebuah scene pop up
    @FXML
    void Add_Income(ActionEvent event) throws Exception {
        Parent root1 = FXMLLoader.load(getClass().getResource("Income_Scene.fxml"));
        Scene scene = new Scene(root1);
        Stage childStage = new Stage();
        childStage.setScene(scene);
        childStage.setTitle("Income");
        childStage.show();
    }

    // method ini adalah yang method utama dan yang terdepan ketika dipanggil
    public void initialize() {
        // memasukkan method refresh()
        refresh();
    }

    // method refresh digunakan untuk menampilkan objek objek pemasukan dan
    // pengeluaran kedalam table view
    public void refresh() {
        List1.clear();
        List2.clear();

        // menghubungkan data yang ada di mysql menggunakan method expense.getdata()
        // yang ada di model_class
        ResultSet rs = expense.getData();

        // proses pembuatan objek
        try {
            while (rs.next()) {
                // Data data ada di dalam mysql digunakan untuk menjadi attribute objek
                List1.add(new expense(rs.getDate("Timestamp"),
                        rs.getString("Category"),
                        rs.getDouble("Total"),
                        rs.getString("Notes")));
                table_expense.setItems(List1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // menggunakan attribute attribute yang ada di objek dan menampilkannya dalam
        // table view
        Expense_Date.setCellValueFactory(new PropertyValueFactory<>("Timestamp"));
        Expense_Category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        Expense_Ammount.setCellValueFactory(new PropertyValueFactory<>("Total"));
        calculateTotalExpense();

        // menghubungkan data yang ada di mysql menggunakan method income.getdata()
        // yang ada di model_class
        ResultSet rs2 = income.getData();

        // proses pembuatan objek
        try {
            while (rs2.next()) {
                // Data data ada di dalam mysql digunakan untuk menjadi attribute objek
                List2.add(new income(rs2.getDate("Timestamp"),
                        rs2.getString("Category"),
                        rs2.getDouble("Total"),
                        rs2.getString("Notes")));
                table_income.setItems(List2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // menampilkan attribute objek ke dalam table view
        Income_Date.setCellValueFactory(new PropertyValueFactory<>("Timestamp"));
        Income_Category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        Income_Ammount.setCellValueFactory(new PropertyValueFactory<>("Total"));
        TotaledIncome();

        // menambahkan method method yang diperlukan
        showDetails();
        delete_income();
        delete_expense();
    }

    // method ini digunakan untuk menghitung total pengeluaran
    private double calculateTotalExpense() {
        double totalExpense = 0.0;
        for (expense item : List1) {
            double expenseAmount = item.getTotal();
            totalExpense += expenseAmount;
        }

        // menyesuaikan text yang ada di panel
        ExpenseText.setText("Rp" + String.valueOf(totalExpense) + "0");
        System.out.println(totalExpense);
        return totalExpense;
    }

    // method ini digunakan untuk menghitung total pemasukan
    private double TotaledIncome() {
        double totalincome = 0.0;
        for (income item : List2) {
            double incomeAmount = item.getTotal();
            totalincome += incomeAmount;
        }
        // menyesuaikan text yang ada di panel
        incomeText.setText("Rp" + String.valueOf(totalincome) + "0");
        System.out.println(totalincome);
        return totalincome;
    }

    // perintah ini berfungsi untuk menghapus data
    public void delete_expense() {
        // membuat tombol popup
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().add(deleteMenuItem);

        table_expense.setContextMenu(contextMenu);

        // serangkaian perintah untuk menghapus baris di table
        table_expense.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                expense selectedItem = table_expense.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    deleteMenuItem.setOnAction(actionEvent -> {
                        if (selectedItem != null) {
                            Date selectedDate = selectedItem.getTimestamp();
                            String selectedCategory = selectedItem.getCategory();
                            double selectedTotal = selectedItem.getTotal();
                            try {
                                // melakukan penghapusan di database mysql
                                Connection connection;
                                PreparedStatement preparedStatement;
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                connection = DriverManager.getConnection("jdbc:mysql://localhost/expensedb", "root",
                                        "");
                                String sql = "DELETE FROM expenselist WHERE Timestamp = ? AND Category = ? AND Total = ?";
                                preparedStatement = connection.prepareStatement(sql);

                                try {
                                    preparedStatement.setDate(1, selectedDate);
                                    preparedStatement.setString(2, selectedCategory);
                                    preparedStatement.setDouble(3, selectedTotal);
                                    int rowsDeleted = preparedStatement.executeUpdate();

                                    if (rowsDeleted > 0) {
                                        System.out.println("Row deleted successfully.");
                                    }
                                    refresh();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("deleting failed");
                                }
                            } catch (Exception e) {
                                System.out.println("connect failed");
                            }
                        }
                    });
                    contextMenu.show(table_expense, event.getScreenX(), event.getScreenY());
                }
            }
        });

    }

    public void delete_income() {

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().add(deleteMenuItem);

        table_income.setContextMenu(contextMenu);

        table_income.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                income selectedItem = table_income.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    deleteMenuItem.setOnAction(actionEvent -> {
                        if (selectedItem != null) {
                            Date selectedDate = selectedItem.getTimestamp();
                            String selectedCategory = selectedItem.getCategory();
                            double selectedTotal = selectedItem.getTotal();
                            try {

                                Connection connection;
                                PreparedStatement preparedStatement;
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                connection = DriverManager.getConnection("jdbc:mysql://localhost/incomedb", "root", "");
                                String sql = "DELETE FROM incomelist WHERE Timestamp = ? AND Category = ? AND Total = ?";
                                preparedStatement = connection.prepareStatement(sql);

                                try {
                                    preparedStatement.setDate(1, selectedDate);
                                    preparedStatement.setString(2, selectedCategory);
                                    preparedStatement.setDouble(3, selectedTotal);
                                    int rowsDeleted = preparedStatement.executeUpdate();

                                    if (rowsDeleted > 0) {
                                        System.out.println("Row deleted successfully.");
                                    }
                                    refresh();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("deleting failed");
                                }
                            } catch (Exception e) {
                                System.out.println("connect failed");
                            }
                        }
                    });
                    contextMenu.show(table_income, event.getScreenX(), event.getScreenY());
                }
            }

        });

    }

    // method untuk menampilkan detail
    public void showDetails() {
        // untuk tabel pengeluaran
        table_expense.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                expense selectedExpense = table_expense.getSelectionModel().getSelectedItem();
                // membuat data
                if (selectedExpense != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Expense Details");
                    alert.setHeaderText(null);
                    alert.setContentText("~~~expense Details~~~\n" +
                            "Date          : " + selectedExpense.getTimestamp() + "\n" +
                            "Category   : " + selectedExpense.getCategory() + "\n" +
                            "Amount     : " + selectedExpense.getTotal() + "\n" +
                            "Notes         : " + selectedExpense.getNotes());
                    String cssPath = getClass().getResource("details.css").toExternalForm();
                    alert.getDialogPane().getStyleClass().add("alert-dialog-pane");
                    alert.getDialogPane().getStylesheets().add(cssPath);
                    alert.showAndWait();

                }
            }
        });

        // untuk tabel pemasukan
        table_income.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                income selectedIncome = table_income.getSelectionModel().getSelectedItem();
                if (selectedIncome != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Income Details");
                    alert.setHeaderText(null);
                    alert.setContentText("~~~Income Details~~~\n" +
                            "Date: " + selectedIncome.getTimestamp() + "\n" +
                            "Category: " + selectedIncome.getCategory() + "\n" +
                            "Amount: " + selectedIncome.getTotal() + "\n" +
                            "Notes: " + selectedIncome.getNotes());
                    String cssPath = getClass().getResource("details.css").toExternalForm();
                    alert.getDialogPane().getStyleClass().add("alert-dialog-pane");
                    alert.getDialogPane().getStylesheets().add(cssPath);
                    alert.showAndWait();
                }
            }
        });

    }

}