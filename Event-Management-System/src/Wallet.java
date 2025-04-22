public class Wallet {
    private double balance;


    public Wallet(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount < 0) {
            System.out.println("Deposit amount cannot be negative.");
            return false;
        }
        balance += amount;
        System.out.println("new balance: ");
        System.out.println(balance);
        return true;
    }

    public boolean withdraw(Event targetEvent, double amount) {
        if (amount < 0) {
            System.out.println("Withdrawal amount cannot be negative.");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient balance for withdrawal.");
            return false;
        }
        balance -= amount;
        targetEvent.getOrganizer().getWallet().balance+=amount;
        return true;
    }

    public boolean refund(Event targetEvent, double amount) {
        if (amount < 0) {
            System.out.println("Refund amount cannot be negative.");
            return false;
        }
        if (amount > targetEvent.getOrganizer().getWallet().getBalance()) {
            System.out.println("Organizer has insufficient balance to process refund.");
            return false;
        }
        balance += amount;
        targetEvent.getOrganizer().getWallet().balance -= amount;
        System.out.println("Refund processed. Balance of User: " + balance);
        return true;
    }
}