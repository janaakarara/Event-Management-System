import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Event implements java.io.Serializable {
    private String title;
    private Organizer organizer;
    private transient Room room; // Marked transient due to non-serializable Room
    private String roomName; // Store room name for serialization
    private Category category;
    private double price;
    private ArrayList<Attendee> attendees;
    private int maxSeats;
    private int attendeeCount;
    private List<Room.TimeSlot> timeSlots;

    public Event(String title, Organizer organizer, Room room, Category category, double price, int maxSeats) {
        this.title = title;
        this.organizer = organizer;
        this.room = room;
        this.roomName = room.getName();
        this.category = category;
        this.price = price;
        this.maxSeats = maxSeats;
        this.attendees = new ArrayList<>();
        this.attendeeCount = 0;
        this.timeSlots = new ArrayList<>();
    }

    public List<Room.TimeSlot> getTimeSlots() {
        return new ArrayList<>(timeSlots); // Return a copy to prevent external modification
    }

    public void addTimeSlot(LocalTime startTime, LocalTime endTime) {
        timeSlots.add(new Room.TimeSlot(startTime, endTime));
    }

    public void clearTimeSlots() {
        timeSlots.clear();
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        this.roomName = room.getName();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public double getPrice() {
        return price;
    }

    public void addAttendee(Attendee a) {
        if (attendeeCount < maxSeats) {
            attendees.add(a);
            attendeeCount++;
        } else {
            System.out.println("No available seats for this event.");
        }
    }

    public ArrayList<Attendee> getAttendees() {
        return new ArrayList<>(attendees); // Return a copy to prevent external modification
    }

    public int getAvailableSeats() {
        return maxSeats - attendeeCount;
    }

    public void printAttendees() {
        System.out.println("Attendees for event: " + title);
        for (int i = 0; i < attendeeCount; i++) {
            if (attendees.get(i) != null) {
                System.out.println("- " + attendees.get(i).getUsername());
            }
        }
    }

    @Override
    public String toString() {
        String timeSlotInfo = timeSlots.isEmpty() ? "No time slot assigned" :
                "Start Time: " + timeSlots.get(0).getStartTime() + ", End Time: " + timeSlots.get(0).getEndTime();
        return title + " | Category: " + category.getName() + " | Available seats: " +
                getAvailableSeats() + " | Price: " + price + " | " + timeSlotInfo +
                " | Room: " + roomName;
    }

    // Serialization hooks to handle transient room field
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        // Restore room reference by matching roomName
        for (Room r : Database.rooms) {
            if (r.getName().equals(roomName)) {
                this.room = r;
                break;
            }
        }
        if (room == null) {
            System.out.println("Warning: Room " + roomName + " not found in Database.rooms.");
        }
    }
}