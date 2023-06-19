package model_class;

public class Budgets {
    private double balance ;
    private double expense ;
    private double income;
    // Membuat konstruktor untuk objek budgets nantinya 
    public Budgets(double initBalance) {
        this.balance = initBalance;
    }

    // membuat setter dan getter 
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
    public double getExpense() {
        return expense;
    }
    public double getIncome() {
        return income;
    }
    // proses perhtungan
    public double calculateBalance(double expense , double income){
        double calculatedBalance = getBalance() - expense + income;
        return calculatedBalance;
    }
    
}
