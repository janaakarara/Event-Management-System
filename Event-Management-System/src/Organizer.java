import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Organizer extends User {
    private Wallet wallet;

    public Organizer() {}

    public Organizer(String username, String password, String dob, double balance) {
        super(username, password, dob);
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
            System.out.println("===== Organizer Dashboard =====");
            System.out.println("Username: " + username);
            System.out.println("1. Create New Event");
            System.out.println("2. View My Events");
            System.out.println("3. Update Events");
            System.out.println("4. View My Balance");
            System.out.println("5. Delete Events");
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
                System.out.print("Enter event title: ");
                String title = scanner.nextLine();

                System.out.println("Available Rooms:");
                for (int i = 0; i < Database.rooms.size(); i++) {
                    System.out.println((i + 1) + ". " + Database.rooms.get(i).toString());
                }
                System.out.print("Choose a room (number): ");
                int roomChoice;
                try {
                    roomChoice = scanner.nextInt() - 1;
                    scanner.nextLine();
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Invalid input. Defaulting to first room.");
                    roomChoice = 0;
                }
                Room room = (roomChoice >= 0 && roomChoice < Database.rooms.size()) ? Database.rooms.get(roomChoice) : Database.rooms.get(0);

                // Prompt for time slot
                LocalTime startTime = null, endTime = null;
                try {
                    System.out.print("Enter event start time (HH:MM): ");
                    startTime = LocalTime.parse(scanner.nextLine());
                    System.out.print("Enter event end time (HH:MM): ");
                    endTime = LocalTime.parse(scanner.nextLine());
                    if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                        System.out.println("End time must be after start time. Event creation cancelled.");
                        continue;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid time format. Please use HH:MM (e.g., 14:30). Event creation cancelled.");
                    continue;
                }

                // Check room availability
                if (!room.isAvailable(startTime, endTime)) {
                    System.out.println("Selected room is not available for the specified time slot. Please choose another room or time.");
                    continue;
                }

                System.out.println("Available Categories:");
                for (int i = 0; i < Database.categories.size(); i++) {
                    System.out.println((i + 1) + ". " + Database.categories.get(i).getName());
                }
                System.out.print("Choose a category (number): ");
                int categoryChoice;
                try {
                    categoryChoice = scanner.nextInt() - 1;
                    scanner.nextLine();
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Invalid input. Defaulting to first category.");
                    categoryChoice = 0;
                }
                Category category = (categoryChoice >= 0 && categoryChoice < Database.categories.size()) ? Database.categories.get(categoryChoice) : Database.categories.get(0);

                System.out.print("Enter event price: ");
                double price;
                try {
                    price = scanner.nextDouble();
                    scanner.nextLine();
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Invalid price. Defaulting to 0.0.");
                    price = 0.0;
                }

                System.out.print("Enter maximum seats: ");
                int maxSeats;
                try {
                    maxSeats = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Invalid number. Defaulting to 50.");
                    maxSeats = 50;
                }

                // Create event, add time slot, remove from room's time slots, and reserve room
                Event newEvent = new Event(title, this, room, category, price, maxSeats);
                newEvent.addTimeSlot(startTime, endTime);
                room.removeTimeSlot(startTime, endTime); // Remove event's time slot from room
                room.reserve(); // Reserve room
                Database.events.add(newEvent);
                Database.eventCount++;
                System.out.println("Event created successfully: " + title);

            }
            else if (choice == 2) {
                System.out.println("My Events:");
                boolean hasEvents = false;
                for (Event event : Database.events) {
                    if (event.getOrganizer().getUsername().equals(this.username)) {
                        System.out.println(event.toString());
                        hasEvents = true;
                    }
                }
                if (!hasEvents) {
                    System.out.println("You have not created any events.");
                }
            }
            else if (choice == 3) {
                System.out.println("My Events:");
                boolean hasEvents = false;
                int index = 1;
                List<Event> myEvents = new ArrayList<>();
                for (Event event : Database.events) {
                    if (event.getOrganizer().getUsername().equals(this.username)) {
                        System.out.println(index + ". " + event.toString());
                        myEvents.add(event);
                        hasEvents = true;
                        index++;
                    }
                }
                if (!hasEvents) {
                    System.out.println("You have not created any events.");
                    continue;
                }

                System.out.print("Choose an event to update (number): ");
                int eventChoice;
                try {
                    eventChoice = scanner.nextInt() - 1;
                    scanner.nextLine();
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Invalid input. Update cancelled.");
                    continue;
                }

                if (eventChoice < 0 || eventChoice >= myEvents.size()) {
                    System.out.println("Invalid event selection. Update cancelled.");
                    continue;
                }

                Event eventToUpdate = myEvents.get(eventChoice);
                System.out.println("Updating event: " + eventToUpdate.getTitle());
                System.out.println("Current details: " + eventToUpdate.toString());
                System.out.println("Leave blank to keep current value.");

                // Update title
                System.out.print("Enter new event title (current: " + eventToUpdate.getTitle() + "): ");
                String newTitle = scanner.nextLine();
                if (!newTitle.trim().isEmpty()) {
                    eventToUpdate.setTitle(newTitle);
                }

                // Update room
                System.out.println("Available Rooms:");
                for (int i = 0; i < Database.rooms.size(); i++) {
                    System.out.println((i + 1) + ". " + Database.rooms.get(i).toString());
                }
                System.out.print("Choose a new room (number, enter 0 to keep current: " + eventToUpdate.getRoom().getName() + "): ");
                int roomChoice;
                Room newRoom = eventToUpdate.getRoom();
                boolean roomChanged = false;
                try {
                    roomChoice = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (roomChoice >= 0 && roomChoice < Database.rooms.size()) {
                        newRoom = Database.rooms.get(roomChoice);
                        roomChanged = true;
                    }
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Invalid input. Keeping current room.");
                }

                // Update time slot
                LocalTime newStartTime = eventToUpdate.getTimeSlots().isEmpty() ? null : eventToUpdate.getTimeSlots().get(0).getStartTime();
                LocalTime newEndTime = eventToUpdate.getTimeSlots().isEmpty() ? null : eventToUpdate.getTimeSlots().get(0).getEndTime();
                boolean timeChanged = false;
                try {
                    System.out.print("Enter new start time (HH:MM, current: " + (newStartTime != null ? newStartTime : "none") + "): ");
                    String startInput = scanner.nextLine();
                    System.out.print("Enter new end time (HH:MM, current: " + (newEndTime != null ? newEndTime : "none") + "): ");
                    String endInput = scanner.nextLine();
                    if (!startInput.trim().isEmpty() && !endInput.trim().isEmpty()) {
                        newStartTime = LocalTime.parse(startInput);
                        newEndTime = LocalTime.parse(endInput);
                        if (newEndTime.isBefore(newStartTime) || newEndTime.equals(newStartTime)) {
                            System.out.println("End time must be after start time. Keeping current time slot.");
                            newStartTime = eventToUpdate.getTimeSlots().isEmpty() ? null : eventToUpdate.getTimeSlots().get(0).getStartTime();
                            newEndTime = eventToUpdate.getTimeSlots().isEmpty() ? null : eventToUpdate.getTimeSlots().get(0).getEndTime();
                        } else {
                            timeChanged = true;
                        }
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid time format. Please use HH:MM (e.g., 14:30). Keeping current time slot.");
                }

                // Validate new room and time slot
                if ((roomChanged || timeChanged) && newStartTime != null && newEndTime != null) {
                    if (!newRoom.isAvailable(newStartTime, newEndTime)) {
                        System.out.println("Selected room is not available for the specified time slot. Update cancelled.");
                        continue;
                    }
                }

                // Update category
                System.out.println("Available Categories:");
                for (int i = 0; i < Database.categories.size(); i++) {
                    System.out.println((i + 1) + ". " + Database.categories.get(i).getName());
                }
                System.out.print("Choose a new category (number, enter 0 to keep current: " + eventToUpdate.getCategory().getName() + "): ");
                int categoryChoice;
                Category newCategory = eventToUpdate.getCategory();
                try {
                    categoryChoice = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (categoryChoice >= 0 && categoryChoice < Database.categories.size()) {
                        newCategory = Database.categories.get(categoryChoice);
                    }
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Invalid input. Keeping current category.");
                }

                // Update price
                System.out.print("Enter new event price (current: " + eventToUpdate.getPrice() + "): ");
                double newPrice = eventToUpdate.getPrice();
                try {
                    String priceInput = scanner.nextLine();
                    if (!priceInput.trim().isEmpty()) {
                        newPrice = Double.parseDouble(priceInput);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid price. Keeping current price.");
                }

                // Update max seats
                System.out.print("Enter new maximum seats (current: " + eventToUpdate.getMaxSeats() + "): ");
                int newMaxSeats = eventToUpdate.getMaxSeats();
                try {
                    String seatsInput = scanner.nextLine();
                    if (!seatsInput.trim().isEmpty()) {
                        newMaxSeats = Integer.parseInt(seatsInput);
                        if (newMaxSeats < eventToUpdate.getAvailableSeats()) {
                            System.out.println("New max seats cannot be less than current attendees. Keeping current max seats.");
                            newMaxSeats = eventToUpdate.getMaxSeats();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid number. Keeping current max seats.");
                }

                // Apply updates
                if (roomChanged || timeChanged) {
                    // Restore old time slot to old room
                    if (!eventToUpdate.getTimeSlots().isEmpty()) {
                        Room oldRoom = eventToUpdate.getRoom();
                        LocalTime oldStartTime = eventToUpdate.getTimeSlots().get(0).getStartTime();
                        LocalTime oldEndTime = eventToUpdate.getTimeSlots().get(0).getEndTime();
                        oldRoom.restoreTimeSlot(oldStartTime, oldEndTime);
                        oldRoom.release(); // Release old room
                    }
                    // Update room and time slot
                    eventToUpdate.setRoom(newRoom);
                    if (timeChanged && newStartTime != null && newEndTime != null) {
                        eventToUpdate.clearTimeSlots();
                        eventToUpdate.addTimeSlot(newStartTime, newEndTime);
                    }
                    newRoom.removeTimeSlot(newStartTime, newEndTime); // Remove new time slot
                    newRoom.reserve(); // Reserve new room
                }
                eventToUpdate.setCategory(newCategory);
                eventToUpdate.setPrice(newPrice);
                eventToUpdate.setMaxSeats(newMaxSeats);

                System.out.println("Event updated successfully: " + eventToUpdate.getTitle());

            }
            else if (choice == 4) {
                System.out.println("Current balance: " + this.getWallet().getBalance());
                System.out.println("Want to add balance? (yes/no)");
                String balanceChoice = scanner.nextLine();
                if (balanceChoice.equalsIgnoreCase("yes")) {
                    System.out.print("Enter amount: ");
                    double amount;
                    try {
                        amount = scanner.nextDouble();
                        scanner.nextLine();
                        this.getWallet().deposit(amount);
                        System.out.println("Balance updated. New balance: " + this.getWallet().getBalance());

                    } catch (Exception e) {
                        scanner.nextLine();
                        System.out.println("Invalid amount. Balance unchanged.");
                    }
                }
            }
            else if (choice == 5) {
                System.out.println("My Events:");
                boolean hasEvents = false;
                int index = 1;
                for (Event event : Database.events) {
                    if (event.getOrganizer().getUsername().equals(this.username)) {
                        System.out.println(index + ". " + event.toString());
                        hasEvents = true;
                        index++;
                    }
                }
                if (!hasEvents) {
                    System.out.println("You have not created any events.");
                    continue;
                }

                System.out.print("Choose an event to delete (number): ");
                int eventChoice;
                try {
                    eventChoice = scanner.nextInt() - 1;
                    scanner.nextLine();
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("Invalid input. Deletion cancelled.");
                    continue;
                }

                // Find the event to delete
                index = 0;
                Event eventToDelete = null;
                int eventIndex = -1;
                for (Event event : Database.events) {
                    if (event.getOrganizer().getUsername().equals(this.username)) {
                        if (index == eventChoice) {
                            eventToDelete = event;
                            eventIndex = Database.events.indexOf(event);
                            break;
                        }
                        index++;
                    }
                }

                if (eventToDelete != null) {
                    // Refund attendees
                    for (Attendee attendee : eventToDelete.getAttendees()) {
                        double refundAmount = eventToDelete.getPrice();
                        attendee.getWallet().refund(eventToDelete, refundAmount);
                        System.out.println("Refunded " + refundAmount + " to " + attendee.getUsername());
                    }
                    // Restore time slot to room
                    if (!eventToDelete.getTimeSlots().isEmpty()) {
                        LocalTime startTime = eventToDelete.getTimeSlots().get(0).getStartTime();
                        LocalTime endTime = eventToDelete.getTimeSlots().get(0).getEndTime();
                        eventToDelete.getRoom().restoreTimeSlot(startTime, endTime);
                    }
                    // Release room reservation
                    eventToDelete.getRoom().release();
                    // Remove event from database
                    Database.events.remove(eventIndex);
                    Database.eventCount--;
                    System.out.println("Event '" + eventToDelete.getTitle() + "' deleted successfully.");

                } else {
                    System.out.println("Invalid event selection. Deletion cancelled.");
                }
            }
            else if (choice == 6) {
                super.logout();
                return;
            }
            else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Getter for wallet
    public Wallet getWallet() {
        return wallet;
    }
}