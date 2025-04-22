import java.util.Scanner;

public class User implements Viewable {
    protected String username;
    protected String password;
    protected String dateOfBirth;
    public User() {}

    public User(String username, String password, String dateOfBirth) {
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String toString() {
        return username;
    }

    public void showDashboard() {
        Scanner scanner = new Scanner(System.in);
        User loggedInUser = null;

        while (true) {
            System.out.println("===== Welcome =====");
            System.out.println("Choose your role:");
            System.out.println("1. Organizer");
            System.out.println("2. Admin");
            System.out.println("3. Attendee");
            System.out.println("4. Register");
            System.out.println("5. Exit");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a number.\n");
                continue;
            }

            boolean success = false; //to check successful login

            if (choice == 1) {
                String inputUsername = loginUsername(scanner);
                String inputPassword = loginPassword(scanner);
                for (Organizer org : Database.organizers) {
                    if (org.getUsername().equals(inputUsername) && org.getPassword().equals(inputPassword)){
                        loggedInUser = org;
                        success = true;
                        break;
                    }
                }
            }
            else if (choice == 2) {
                String inputUsername = loginUsername(scanner);
                String inputPassword = loginPassword(scanner);
                for (Admin admin : Database.adminList) {
                    if (admin.getUsername().equals(inputUsername) && admin.getPassword().equals(inputPassword)) {
                        loggedInUser = admin;
                        success = true;
                        break;
                    }
                }
            }
            else if (choice == 3) {
                String inputUsername = loginUsername(scanner);
                String inputPassword = loginPassword(scanner);
                for (Attendee attendee : Database.attendees) {
                    if (attendee.getUsername().equals(inputUsername) && attendee.getPassword().equals(inputPassword)) {
                        loggedInUser = attendee;
                        success = true;
                        break;
                    }
                }
            }
            else if (choice == 4) {
                Register();
                continue;
            }

            else if (choice == 5) {
                System.out.println("Exiting system. Goodbye!");
                return;
            }
            else {
                System.out.println("Invalid choice. Try again.\n");
                continue;
            }

            if (!success) {
                System.out.println("Invalid credentials. Try again.\n");
                continue;
            }

            break;
        }

        // Show the dashboard of the logged-in user
        if (loggedInUser != null) {
            System.out.println("Login successful! Username: " + loggedInUser.getUsername());
            loggedInUser.showDashboard();
        }
    }

    public void logout() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Confirm logout (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Logged out successfully.");
            User usercall=new User();
            usercall.showDashboard();
        }
        else {
            System.out.println("Logout cancelled.");
            this.showDashboard();
        }


    }

    public String loginUsername(Scanner scanner) {
        System.out.print("Enter Username: ");
        String inputUsername = scanner.nextLine();
        return (inputUsername);
    }

    public String loginPassword(Scanner scanner) {
        System.out.print("Enter Password: ");
        String inputPassword = scanner.nextLine();
        return (inputPassword);
    }

    public void Register() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===== Register =====");
        System.out.println("Choose your role:");
        System.out.println("1. Organizer");
        System.out.println("2. Attendee");

        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        if (choice == 1) {
            Organizer organizer = new Organizer();
            organizer.Register();
            Database.organizers.add(organizer);
            Database.organizerCount++;
            System.out.println("Organizer registered successfully!");
        } else if (choice == 2) {
            Attendee attendee = new Attendee();
            attendee.Register();
            Database.attendees.add(attendee);
            Database.attendeeCount++;
            System.out.println("Attendee registered successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }
}