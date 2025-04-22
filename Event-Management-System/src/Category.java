public class Category {
    public String name;


    public Category(String name) {
        this.name = name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }


}