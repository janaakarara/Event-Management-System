import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Room {

    private String name;
    private boolean reserved;
    private List<TimeSlot> timeSlots;

    // Inner class to represent a time slot
    public static class TimeSlot {
        private LocalTime startTime;
        private LocalTime endTime;

        public TimeSlot(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }
    }

    public Room(String name) {
        this.name = name;
        this.reserved = false;
        this.timeSlots = new ArrayList<>();
    }

    public Room(String name, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.reserved = false;
        this.timeSlots = new ArrayList<>();
        this.timeSlots.add(new TimeSlot(startTime, endTime));
    }

    public boolean isAvailable(LocalTime startTime, LocalTime endTime) {
        if (reserved) {
            return false;
        }
        for (TimeSlot slot : timeSlots) {
            if (!startTime.isBefore(slot.startTime) && !endTime.isAfter(slot.endTime)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAvailable() {
        LocalTime now = LocalTime.now();
        for (TimeSlot slot : timeSlots) {
            if (!reserved && !now.isBefore(slot.startTime) && !now.isAfter(slot.endTime)) {
                return true;
            }
        }
        return false;
    }

    public void reserve() {
        this.reserved = true;
    }

    public void release() {
        this.reserved = false;
    }

    public void removeTimeSlot(LocalTime startTime, LocalTime endTime) {
        List<TimeSlot> newSlots = new ArrayList<>();
        Iterator<TimeSlot> iterator = timeSlots.iterator();
        while (iterator.hasNext()) {
            TimeSlot slot = iterator.next();
            // If event slot overlaps with room slot
            if (!(endTime.isBefore(slot.startTime) || startTime.isAfter(slot.endTime))) {
                // Add portion before event start, if it exists
                if (startTime.isAfter(slot.startTime)) {
                    newSlots.add(new TimeSlot(slot.startTime, startTime));
                }
                // Add portion after event end, if it exists
                if (endTime.isBefore(slot.endTime)) {
                    newSlots.add(new TimeSlot(endTime, slot.endTime));
                }
            } else {
                newSlots.add(slot);
            }
        }
        timeSlots.clear();
        timeSlots.addAll(newSlots);
    }

    public void restoreTimeSlot(LocalTime startTime, LocalTime endTime) {
        // Add the time slot back and merge overlapping or adjacent slots
        timeSlots.add(new TimeSlot(startTime, endTime));
        mergeTimeSlots();
    }

    private void mergeTimeSlots() {
        if (timeSlots.isEmpty()) return;
        // Sort slots by start time
        timeSlots.sort((s1, s2) -> s1.startTime.compareTo(s2.startTime));
        List<TimeSlot> merged = new ArrayList<>();
        TimeSlot current = timeSlots.get(0);
        for (int i = 1; i < timeSlots.size(); i++) {
            TimeSlot next = timeSlots.get(i);
            if (!current.endTime.isBefore(next.startTime)) {
                // Overlapping or adjacent: extend current slot if necessary
                if (next.endTime.isAfter(current.endTime)) {
                    current = new TimeSlot(current.startTime, next.endTime);
                }
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        timeSlots.clear();
        timeSlots.addAll(merged);
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void addTimeSlot(LocalTime startTime, LocalTime endTime) {
        timeSlots.add(new TimeSlot(startTime, endTime));
    }

    public void clearTimeSlots() {
        timeSlots.clear();
    }

    public static void viewAvailableRooms(List<Room> rooms) {
        System.out.println("Available Rooms:");
        boolean hasAvailable = false;
        for (Room room : rooms) {
            if (room.isAvailable()) {
                System.out.print(room.getName() + " - Available at: ");
                for (TimeSlot slot : room.timeSlots) {
                    System.out.print(slot.startTime + " to " + slot.endTime + "; ");
                }
                System.out.println();
                hasAvailable = true;
            }
        }
        if (!hasAvailable) {
            System.out.println("No rooms are currently available.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + " (");
        for (int i = 0; i < timeSlots.size(); i++) {
            TimeSlot slot = timeSlots.get(i);
            sb.append(slot.startTime).append(" - ").append(slot.endTime);
            if (i < timeSlots.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        sb.append("available");
        return sb.toString();
    }
}