package domainObjectModel;

import commandHandler.utils.Logger;

/**
 * Venue of the ticket
 * **/
public class Venue {
    private int id;
    private String name;
    private Long capacity;
    private final Long MIN_CAPACITY = 0L;
    private VenueType type;
    private Address address;

    public Venue(String name, Long capacity, VenueType type, Address address) {
        if (name == null || name.isEmpty() || (capacity != null && capacity <= MIN_CAPACITY) || type == null){
            Logger.error("incorrect data in Venue constructor");
            System.exit(0);
        }
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id < 0L){
            Logger.error("negative id in Venue setter");
            System.exit(0);
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()){
            Logger.error("name is null or empty in Venue setter");
            System.exit(0);
        }
        this.name = name;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        if (capacity != null && capacity <= MIN_CAPACITY){
            Logger.error("capacity is not more than " + MIN_CAPACITY + " in Venue setter");
            System.exit(0);
        }
        this.capacity = capacity;
    }

    public VenueType getType() {
        return type;
    }

    public void setType(VenueType type) {
        if (type == null){
            Logger.error("type is null in Venue setter");
            System.exit(0);
        }
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "\nid=" + id +
                ", \nname='" + name + '\'' +
                ", \ncapacity=" + capacity +
                ", \ntype=" + type +
                ", \naddress=" + address.toString() +
                '}';
    }
}
