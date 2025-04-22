import java.util.Scanner;
import java.util.Arrays;

public class Attendee extends User {
    private String address;
    private Gender gender;
    private String[] interests;
    private Wallet wallet;

    public Attendee() {}

    public Attendee(String username, String password, String dob, String address, Gender gender, String[] interests, double balance) {
        super(username, password, dob);
        this.address = address;
        this.gender = gender;
        this.interests = interests;
        this.wallet = new Wallet(balance);
    }

    @Override
    public void Register() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        this.username = scanner.nextLine();

        System.out.print("Enter password: ");
        this.password = scanner.nextLine();

        System.out.print("Enter date of birth (e.g., YYYY-MM-DD): ");
        this.dateOfBirth = scanner.nextLine();

        System.out.print("Enter address: ");
        this.address = scanner.nextLine();

        System.out.println("Enter gender (MALE/FEMALE/OTHER): ");
        String genderInput = scanner.nextLine().toUpperCase();
        try {
            this.gender = Gender.valueOf(genderInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid gender. Defaulting to OTHER.");
            this.gender = Gender.OTHER;
        }

        System.out.print("Enter interests (comma-separated, e.g., music, hiking, coding): ");
        String interestsInput = scanner.nextLine();
        this.interests = interestsInput.isEmpty() ? new String[0] : interestsInput.split(",\\s*");

        System.out.print("Enter initial wallet balance: ");
        double balance;
        try {
            balance = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
        } catch (Exception e) {
            System.out.println("Invalid balance. Defaulting to 0.0.");
            scanner.nextLine();
            balance = 0.0;
        }
        this.wallet = new Wallet(balance);
    }

    @Override
    public void showDashboard() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("===== Attendee Dashboard =====");
            System.out.println("Username: " + username);
            System.out.println("1. View Available Events");
            System.out.println("2. Join Event");
            System.out.println("3. View My Events");
            System.out.println("4. View My Balance");
            System.out.println("5. Refund Event");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 1) {
                System.out.println("Available Events:");
                for (Event event : Database.events) {
                    System.out.println(event.toString());
                }
            } else if (choice == 2) {
                for (Event event : Database.events) {
                    System.out.println(event.toString());
                }
                System.out.println("Enter the title of the event to join:");
                String eventTitle = scanner.nextLine();
                Event targetEvent = null;
                for (Event event : Database.events) {
                    if (event.getTitle().equalsIgnoreCase(eventTitle)) {
                        targetEvent = event;
                        break;
                    }
                }if (targetEvent != null) {
                    boolean registered = false;
                    for (Attendee attendee : targetEvent.getAttendees()) {
                        if (attendee.getUsername().equals(this.username)) {
                            registered = true;
                            break;
                        }
                    }
                    if (registered) {
                        System.out.println("You are already registered for this event.");
                    }
                    else if (targetEvent.getAvailableSeats() > 0 && wallet.getBalance() >= targetEvent.getPrice()) {
                        targetEvent.addAttendee(this);
                        wallet.withdraw(targetEvent,targetEvent.getPrice());
                        System.out.println("Successfully joined event: " + eventTitle);
                        System.out.println("Current Balance: " + wallet.getBalance());
                    } else {
                        System.out.println("Cannot join event. Insufficient balance or no seats available.");
                    }
                } else {
                    System.out.println("Event not found.");
                }
            } else if (choice == 3) {
                System.out.println("My Events:");
                boolean hasEvents = false;
                for (Event event : Database.events) {
                    for (Attendee attendee : event.getAttendees()) {
                        if (attendee.getUsername().equals(this.username)) {
                            System.out.println(event.toString());
                            hasEvents = true;
                            break;
                        }
                    }
                }
                if (!hasEvents) {
                    System.out.println("You are not registered for any events.");
                }
            }
            else if (choice == 4){
                System.out.println("current balance: ");
                System.out.println(this.getWallet().getBalance());
                System.out.println("Want to add balance? (yes/no)");
                String balanceChoice= scanner.nextLine();
                if (balanceChoice.equals("yes")) {
                    System.out.println("enter amount:" );
                    double amount= scanner.nextDouble();
                    this.getWallet().deposit(amount);
                }
                else this.showDashboard();
            }else if (choice == 5){
                System.out.println("current events: ");
                boolean hasEvents = false;
                for (Event event : Database.events) {
                    for (Attendee attendee : event.getAttendees()) {
                        if (attendee.getUsername().equals(this.username)) {
                            System.out.println(event.toString());
                            hasEvents = true;
                            break;
                        }
                    }
                }
                System.out.println("choose event to refund: ");
                String eventname= scanner.nextLine();
                scanner.nextLine();
                Event targetEvent = null;
                for (Event event : Database.events) {
                    if (event.getTitle().equalsIgnoreCase(eventname)) {
                        targetEvent = event;
                        break;
                    }
                }
                if (targetEvent == null) {
                    System.out.println("Event not found.");
                }
                double refundAmount = targetEvent.getPrice();
                System.out.println("refunded balance of event is: ");
                System.out.println(refundAmount);
                System.out.println("current balance: " + this.getWallet().getBalance());
                getWallet().refund(targetEvent, refundAmount);
            }

            else if (choice == 6) {
                logout();
                return;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Getters for accessing private fields
    public String getAddress() {
        return address;
    }

    public Gender getGender() {
        return gender;
    }

    public String[] getInterests() {
        return interests;
    }

    public Wallet getWallet() {
        return wallet;
    }

}