import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Main {
    public static void main(String[] args) {
        Database.addTestData();
        User user= new User();
        user.showDashboard();


    }
}