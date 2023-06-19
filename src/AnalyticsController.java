import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;
import model_class.analysis;
import model_class.expense;
import model_class.income;

public class AnalyticsController {

    @FXML
    private AnchorPane root;

    @FXML
    private PieChart expense_chart;
    @FXML
    private AnchorPane side_anchor;

    @FXML
    private PieChart income_chart;
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
        Parent BudgetParent = FXMLLoader.load(getClass().getResource("Budget_Scene.fxml"));
        Scene BudgetScene = new Scene(BudgetParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(BudgetScene);
        window.show();
    }

    // =====================================================================================
    public void initialize() {
        try {
            Connection connection;
            PreparedStatement preparedStatement;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/expensedb", "root", "");
            preparedStatement = connection.prepareStatement("SELECT * FROM `expenseList`");
            System.out.println("Expense Analitics Connect success");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                List1.add(new expense(rs.getDate("Timestamp"),
                        rs.getString("Category"),
                        rs.getDouble("Total"),
                        rs.getString("Notes")));

            }
        } catch (Exception e) {
            System.out.println("connect failed");
        }
        // Mmebuat obeservable list untuk menampung data di piechart
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        // membuat hashmap
        Map<String, Double> categoryMap = new HashMap<>();

        // membuat perkondisian untuk mencegah category yang sama ditampilkan terpisah
        for (expense item : List1) {

            // mendapatkan nilai di objek
            String category = item.getCategory();
            double value = item.getTotal();
            // mengecek apakah ada dua kategori yanh sama
            if (categoryMap.containsKey(category)) {

                double currentTotal = categoryMap.get(category);
                // jika sama maka akan dijumlahkan
                double newTotal = currentTotal + value;
                categoryMap.put(category, newTotal);
            } else {
                categoryMap.put(category, value);
            }
        }
        // Setelah pemrosesan tadi maka perulangan for ini akan dieksekusi
        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            String category = entry.getKey();
            double value = entry.getValue();
            // menambahkan data kedalam chartData
            chartData.add(new PieChart.Data(category, value));
        }

        // mengambil data yang ada di piechart
        double Sum = chartData.stream().mapToDouble(PieChart.Data::getPieValue).sum();
        // perintah untuk menghitung presentasi yang ada berdasarkan piechartnya
        // menggunakan mehtod yang ada di model class analysis
        chartData.forEach(
                data -> data.setName(data.getName() + " (" + analysis.SumPercentage(Sum, data.getPieValue()) + ")"));

        expense_chart.setData(chartData);
        expense_chart.setLegendVisible(true);

        try {
            Connection connection2;
            PreparedStatement preparedStatement2;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection2 = DriverManager.getConnection("jdbc:mysql://localhost/incomedb", "root", "");
            preparedStatement2 = connection2.prepareStatement("SELECT * FROM `incomelist`");
            System.out.println("Income connect success");
            ResultSet rs = preparedStatement2.executeQuery();
            while (rs.next()) {
                List2.add(new income(rs.getDate("Timestamp"),
                        rs.getString("Category"),
                        rs.getDouble("Total"),
                        rs.getString("Notes")));

            }

        } catch (Exception e) {
            System.out.println("conncect failed");
        }
        // Mmebuat obeservable list untuk menampung data di piechart
        ObservableList<PieChart.Data> chartData2 = FXCollections.observableArrayList();
        // membuat hashmap
        Map<String, Double> categoryMap2 = new HashMap<>();
        // membuat perkondisian untuk mencegah category yang sama ditampilkan terpisah
        for (income item : List2) {
            String category2 = item.getCategory();
            double value = item.getTotal();
            // mengecek apakah ada dua kategori yanh sama
            if (categoryMap.containsKey(category2)) {
                
                // mendapatkan nilai di objek
                double currentTotal2 = categoryMap.get(category2);
                // jika sama maka akan dijumlahkan
                double newTotal2 = currentTotal2 + value;
                categoryMap2.put(category2, newTotal2);
            } else {

                categoryMap2.put(category2, value);
            }
        }
        // Setelah pemrosesan tadi maka perulangan for ini akan dieksekusi
        for (Map.Entry<String, Double> entry : categoryMap2.entrySet()) {
            String category2 = entry.getKey();
            double value2 = entry.getValue();
            chartData2.add(new PieChart.Data(category2, value2));
        }

        // mengambil data yang ada di piechart
        double Sum2 = chartData2.stream().mapToDouble(PieChart.Data::getPieValue).sum();
        // perintah untuk menghitung presentasi yang ada berdasarkan piechartnya
        // menggunakan mehtod yang ada di model class analysis
        chartData2.forEach(data -> data.setName(data.getName() +
                " (" + analysis.SumPercentage(Sum2, data.getPieValue())
                + ")"));

        income_chart.setData(chartData2);
        income_chart.setLegendVisible(true);
    }

}
