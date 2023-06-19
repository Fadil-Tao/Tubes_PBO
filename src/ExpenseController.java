import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExpenseController {

    // properti java fx
    @FXML
    private ChoiceBox<String> Category_field;

    @FXML
    private DatePicker Date_Field;

    @FXML
    private TextArea Notes_Field;

    @FXML
    private TextField Total_Field;
    @FXML
    private Button eight;

    @FXML
    private Button five;

    @FXML
    private Button four;

    @FXML
    private Button nine;

    @FXML
    private Button one;

    @FXML
    private Button seven;

    @FXML
    private Button six;

    @FXML
    private Button three;

    @FXML
    private Button two;

    @FXML
    private Button zero;

    public void initialize() {
        refresh();
    }

    // method untuk fungsi tombol
    public void refresh() {
        zero.setOnAction(e -> Total_Field.appendText("0"));
        one.setOnAction(e -> Total_Field.appendText("1"));
        two.setOnAction(e -> Total_Field.appendText("2"));
        three.setOnAction(e -> Total_Field.appendText("3"));
        four.setOnAction(e -> Total_Field.appendText("4"));
        five.setOnAction(e -> Total_Field.appendText("5"));
        six.setOnAction(e -> Total_Field.appendText("6"));
        seven.setOnAction(e -> Total_Field.appendText("7"));
        eight.setOnAction(e -> Total_Field.appendText("8"));
        nine.setOnAction(e -> Total_Field.appendText("9"));

        Total_Field.setEditable(false);

        
        constraint();
        setCategory();
    }
    // method untuk set category apa saja yang tersedia
    public void setCategory(){
        Category_field.getItems().add("Makanan");
        Category_field.getItems().add("Pakaian");
        Category_field.getItems().add("Rumah");
        Category_field.getItems().add("Kendaraan");
        Category_field.getItems().add("Hiburan");
    }
    // untuk mendapatkan value categori dari Category_field
    public String categoryGet() {
        String category_Value;
        category_Value = Category_field.getValue();
        return category_Value;
    }

    // sebuah pembatasan untuk datepicker sehingga user tidak bisa memilih tanggal
    // di masa depan
    public void constraint() {
        Date_Field.setDayCellFactory(picker -> new DateCell() {

            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #cccccc;"); 
                }
            }
        });
    }
    // Untuk mendapatkan value dari datepicker
    public Date dateGet() {

        LocalDate value = Date_Field.getValue();
        if (value != null) {
            return java.sql.Date.valueOf(value);
        } else {
            return null;
        }
    }

    // untuk mendapatkan value dari notes_field
    public String notesGet() {
        String Notes_value = null;
        Notes_value = Notes_Field.getText();
        return Notes_value;
    }

    // untuk mendapatkan value dari total_field
    public Double totalGet() {
        String total = Total_Field.getText();
        if (!total.isEmpty()) {
            try {
                double doubleValue = Double.parseDouble(total);
                return doubleValue;
            } catch (NumberFormatException e) {
                // Handle the case where the string cannot be parsed as a double
                System.out.println("Invalid input. Unable to convert the string to a double.");
            }
        }

        return 0.0;
    }

    // method untuk menambahkan data data tadi ke mysql
    public void AddExpenseToMysql() {
        // perkondisian untuk mengecek agar semua nya sudah diisi
        if (totalGet() != null && dateGet() != null && categoryGet() != null && notesGet() != null) {
            
            try {
                Connection connection;
                PreparedStatement pst;
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost/expensedb", "root", "");
                String sql = "INSERT INTO `expenselist` (`Timestamp`, `Category`, `Total`, `Notes`) VALUES (?, ?, ?, ?)";
                pst = connection.prepareStatement(sql);
                try {
                    pst.setDate(1, dateGet());
                    pst.setString(2, categoryGet());
                    pst.setDouble(3, totalGet());
                    pst.setString(4, notesGet());
                    pst.execute();
                    System.out.println("adding data to mysql table success");
                    clear();
    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Save successful!");
                    alert.showAndWait();
    
                } catch (Exception e) {
                    System.out.println("adding data to mysql table failed");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("connect failed");
            }
        } else {
             Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Data Tidak Lengkap");
                    alert.setHeaderText(null);
                    alert.setContentText("Mohon Isi semua");
                    alert.showAndWait();
        }
    }
    // membersihkan semua field 
    public void clear() {
        Category_field.setValue(null); 
        Date_Field.setValue(null); 
        Notes_Field.clear(); 
        Total_Field.clear();
    }
    // meghapus angka di total field 
    public void deleteCharacter() {
        String currentText = Total_Field.getText();
        if (!currentText.isEmpty()) {
            String newText = currentText.substring(0, currentText.length() - 1);
            Total_Field.setText(newText);
        }
    }
}
