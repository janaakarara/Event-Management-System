import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

class Admin extends User implements Viewable {

    private Role role;
    private int workingHours;

    public Admin(){}
    public Admin(String username, String password, String dateOfBirth, Role role, int workingHours) {
        super(username, password, dateOfBirth);
        this.role = role;
        this.workingHours = workingHours;
    }

    // CREATE Category
    public void createCategory(String categoryName) {
        Category newCategory = new Category(categoryName);
        Database.categories.add(newCategory);
        System.out.println("Category '" + categoryName + "' created.");
    }

    // READ Categories (View all categories)
    public void showCategories() {
        System.out.println("Categories:");
        for (Category category : Database.categories) {
            System.out.println("- " + category.getName());
        }
    }

    // UPDATE Category (Change category name)
    public void updateCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Categories:");
        for (Category category : Database.categories) System.out.println(category);
        System.out.print("Enter current category name: ");
        String oldName = scanner.nextLine();
        for (Category category : Database.categories) {
            if (category.getName().equals(oldName)) {
                System.out.print("Enter new category name: ");
                String updatedName = scanner.nextLine();
                category.setName(updatedName);
                System.out.println("Category updated: '" + oldName + "' to '" + updatedName + "'");
                return;
            }
        }
        System.out.println("Category '" + oldName + "' not found.");
    }

    // DELETE Category
    public void deleteCategory(String categoryName) {
        for (Category category : Database.categories) {
            if (category.getName().equals(categoryName)) {
                Database.categories.remove(category);
                System.out.println("Category '" + categoryName + "' deleted.");
                return;
            }
        }
        System.out.println("Category '" + categoryName + "' not found.");
    }


    public void showAllData() {
        System.out.println("Rooms:");
        for (Room room : Database.rooms) System.out.println(room);
        System.out.println("Categories:");
        for (Category category : Database.categories) System.out.println(category);
        System.out.println("Events:");
        for (Event event : Database.events) System.out.println(event);
        System.out.println("Attendees:");
        for (Attendee attendee : Database.attendees) System.out.println(attendee);

    }

    public void addRoom(String roomName,int startTime, int endTime) {
        Room room=new Room(roomName);
        room.addTimeSlot(LocalTime.of(startTime,0),LocalTime.of(endTime,0));
        Database.rooms.add(room);
        System.out.println("Room added");
    }

    public void showDashboard()
    {
        Scanner scanner = new Scanner(System.in);
        int choice;

        while(true){
            System.out.println("\n===== Admin Dashboard =====");
            System.out.println("Welcome, " + username);
            System.out.println("1. Create a Category");
            System.out.println("2. Show all Categories");
            System.out.println("3. Update a Category");
            System.out.println("4. Delete a Category");
            System.out.println("5. Add Room");
            System.out.println("6. Show All Data");
            System.out.println("7. Logout");
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer


            if (choice == 1) {
                System.out.print("Enter new category name: ");
                String newCategory = scanner.nextLine();
                createCategory(newCategory);

            }
            else if (choice == 2) {
                showCategories();

            }
            else if (choice == 3) {
                updateCategory();

            }
            else if (choice == 4) {
                System.out.println("Categories:");
                for (Category category : Database.categories) System.out.println(category);
                System.out.print("Enter category name to delete: ");
                String deleteName = scanner.nextLine();
                deleteCategory(deleteName);

            } else if (choice==5) {
                System.out.println("Please enter room name");
                String roomName = scanner.nextLine();
                System.out.println("Enter available hours of room from-- ");
                int startTime = scanner.nextInt();
                System.out.println("Enter available hours of room --to' ");
                int endTime = scanner.nextInt();
                addRoom(roomName,startTime,endTime);

            } else if (choice == 6) {
                showAllData();

            }
            else if (choice == 7) {
                super.logout();
                break;

            }
            else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }
    public void Register(){
        //will not be an option for admin
    }
}