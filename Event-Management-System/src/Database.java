import java.time.LocalTime;
import java.util.*;


public class Database {


    //primary database
    public static int roomCount = 0;
    public static int eventCount = 0;
    public static int attendeeCount = 0;
    public static int organizerCount = 0;
    public static int adminCount = 0;
    public static int categoryCount = 0;

    public static ArrayList<Room> rooms = new ArrayList<>();
    public static ArrayList<Event> events = new ArrayList<>();
    public static ArrayList<Attendee> attendees = new ArrayList<>();
    public static ArrayList<Organizer> organizers = new ArrayList<>();
    public static ArrayList<Category> categories = new ArrayList<>();

    //adding static admins
    public static Admin admin1= new Admin("1","1","9 July 2005", Role.SUPER_ADMIN, 5);
    public static Admin admin2= new Admin("2","2","9 July 2005", Role.SUPER_ADMIN, 5);
    public static Admin admin3= new Admin("3","3","9 July 2005", Role.SUPER_ADMIN, 5);

    public static Admin[] adminList = {admin1, admin2, admin3};
    // Method to add dummy data
    public static void addTestData() {

        organizers.add(new Organizer("Jana karara", "J112", "1990-01-15", 5000.0));
        organizers.add(new Organizer("Noreen Tawfik", "N113", "1985-08-22", 1000.0));
        organizers.add(new Organizer("Rawan amin", "R114", "1992-03-10", 750.0));
        organizers.add(new Organizer("Amira Botros", "A115", "1988-11-30", 200.0));
        organizers.add(new Organizer("Razan elbanna", "R116", "1995-07-19", 1500.0));
        organizerCount += 5; // Update organizer count

        categories.add(new Category("Conference"));
        categories.add(new Category("Workshop"));
        categories.add(new Category("Seminar"));
        categories.add(new Category("Networking"));
        categories.add(new Category("Training"));
        categoryCount = categories.size(); // Update categoryCount

        Attendee A1 = new Attendee("farah", "pass123", "2005-18-03", "Nasr city", Gender.FEMALE, new String[]{"music", "hiking", "coding"}, 100.50);
        Attendee A2 = new Attendee("jana", "pass456", "2006-15-01", "tagmoaa", Gender.FEMALE, new String[]{"reading", "travel", "yoga"}, 250.75);
        attendees.add(A1);
        attendees.add(A2);

        // Room 1: Room A with two time slots
        Room room1 = new Room("Room A");
        room1.addTimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0));
        room1.addTimeSlot(LocalTime.of(13, 0), LocalTime.of(17, 0));
        rooms.add(room1);
        roomCount++;

        // Room 2: Room B with one time slot
        Room room2 = new Room("Room B", LocalTime.of(8, 0), LocalTime.of(16, 0));
        rooms.add(room2);
        roomCount++;

        // Room 3: Room C with three time slots
        Room room3 = new Room("Room C");
        room3.addTimeSlot(LocalTime.of(10, 0), LocalTime.of(13, 0));
        room3.addTimeSlot(LocalTime.of(14, 0), LocalTime.of(16, 0));
        room3.addTimeSlot(LocalTime.of(17, 0), LocalTime.of(19, 0));
        rooms.add(room3);
        roomCount++;

        // Room 4: Room D with one time slot
        Room room4 = new Room("Room D", LocalTime.of(9, 0), LocalTime.of(18, 0));
        rooms.add(room4);
        roomCount++;

        // Room 5: Room E with two time slots
        Room room5 = new Room("Room E");
        room5.addTimeSlot(LocalTime.of(8, 30), LocalTime.of(11, 30));
        room5.addTimeSlot(LocalTime.of(13, 30), LocalTime.of(16, 30));
        rooms.add(room5);
        roomCount++;


        // Adding dummy events
        Event event1 = new Event("Tech Conference", organizers.get(0), rooms.get(0), categories.get(0), 150.0, 100);
        event1.addAttendee(A1);
        event1.addAttendee(A2);
        events.add(event1);
        eventCount++;

        Event event2 = new Event("Java Workshop", organizers.get(1), rooms.get(1), categories.get(1), 75.0, 50);
        event2.addAttendee(A1);
        events.add(event2);
        eventCount++;

        Event event3 = new Event("AI Seminar", organizers.get(2), rooms.get(2), categories.get(2), 50.0, 30);
        events.add(event3);
        eventCount++;

        Event event4 = new Event("Networking Night", organizers.get(3), rooms.get(3), categories.get(3), 25.0, 80);
        event4.addAttendee(A2);
        events.add(event4);
        eventCount++;

        Event event5 = new Event("Data Science Training", organizers.get(4), rooms.get(4), categories.get(4), 200.0, 40);
        events.add(event5);
        eventCount++;
    }


}