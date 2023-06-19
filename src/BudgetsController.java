import java.sql.Connection;
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model_class.Budgets;
import model_class.expense;
import model_class.income;

public class BudgetsController {
    @FXML
    private Label budget_label;

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane side_anchor;
    // menginisiasi objek budget menjadi 0
    Budgets budget = new Budgets(0);

     // ===================================================================================================
    // Membuat ObservableList List1 untuk menyimpan daftar objek expense.
    ObservableList<expense> List1 = FXCollections.observableArrayList();
    // Membuat ObservableList List2 untuk menyimpan daftar objek income.
    ObservableList<income> List2 = FXCollections.observableArrayList();

    // ====================================== Menu dashboard
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

    public void initialize() {
        refresh();
    }
    // proses untuk mengambil dari mysql dan menginisiasi nya menjadi objek
    public void refresh() {
        try {
            Connection connection;
            PreparedStatement preparedStatement;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/expensedb", "root", "");
            preparedStatement = connection.prepareStatement("SELECT * FROM `expenseList`");
            System.out.println("Expense Connect success");
            ResultSet rs = preparedStatement.executeQuery();
            try {
                while (rs.next()) {
                    //pembuatan objek
                    List1.add(new expense(rs.getDate("Timestamp"),
                            rs.getString("Category"),
                            rs.getDouble("Total"),
                            rs.getString("Notes")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("connect failed");
        }
        try {
            Connection connection;
            PreparedStatement preparedStatement;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/incomedb", "root", "");
            preparedStatement = connection.prepareStatement("SELECT * FROM `incomeList`");
            System.out.println("income Connect success");
            ResultSet rs2 = preparedStatement.executeQuery();

            try {
                while (rs2.next()) {
                    //pembuatan objek
                    List2.add(new income(rs2.getDate("Timestamp"),
                            rs2.getString("Category"),
                            rs2.getDouble("Total"),
                            rs2.getString("Notes")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("connect failed");
        }
        double totalExpense = TotaledExpense();
        double totalIncome = TotaledIncome();

        double currentBalance = budget.calculateBalance(totalExpense, totalIncome);
        budget_label.setText("Rp" + String.valueOf(currentBalance)+"0");
    }
    // method yang digunakan untuk menghitung total pengeluaran
    private double TotaledExpense() {
        double totalExpense = 0.0;
        for (expense item : List1) {
            double expenseAmount = item.getTotal();
            totalExpense += expenseAmount;
        }
        // akan mengembalikan value berupa double
        return totalExpense;
    }
    // method yang digunakan untuk menghitung total pemasukan
    private double TotaledIncome() {
        double totalincome = 0.0;
        for (income item : List2) {
            double incomeAmount = item.getTotal();
            totalincome += incomeAmount;
        }
        // akan mengembalikan value berupa double
        return totalincome;
    }
}
