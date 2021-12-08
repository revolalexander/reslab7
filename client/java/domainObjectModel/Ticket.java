package domainObjectModel;

import utils.Logger;

import java.util.Date;
import java.util.Objects;

/**
 * Main element in collection
 * **/
public class Ticket{
    private int id;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private double price;
    public static final double MIN_PRICE = 0D;
    private String comment;
    private boolean refundable;
    private TicketType ticketType;
    private Venue venue;
    private String creator;

    public Ticket(String name, Coordinates coordinates, double price, String comment, boolean refundable, TicketType ticketType, Venue venue){
        if (name == null || name.isEmpty() || coordinates == null || price <= 0 || (comment != null && comment.isEmpty())){
            Logger.error("incorrect data in Ticket constructor");
            System.exit(0);
        }
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.price = price;
        this.comment = comment;
        this.refundable = refundable;
        this.ticketType = ticketType;
        this.venue = venue;
        this.id = -1;
    }
    public Ticket(String name, Date creationDate, Coordinates coordinates, double price, String comment, boolean refundable, TicketType ticketType, Venue venue){
        if (name == null || name.isEmpty() || coordinates == null || price <= 0 || (comment != null && comment.isEmpty())){
            Logger.error("incorrect data in Ticket constructor");
            System.exit(0);
        }
        this.name = name;
        this.creationDate = creationDate;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.price = price;
        this.comment = comment;
        this.refundable = refundable;
        this.ticketType = ticketType;
        this.venue = venue;
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0){
            Logger.error("negative id in Ticket setter");
            System.exit(0);
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty()){
            Logger.error("name value is null or empty in Ticket setter");
            System.exit(0);
        }
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if(coordinates == null){
            Logger.error("coordinates value is null in Ticket setter");
            System.exit(0);
        }
        this.coordinates = coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price <= MIN_PRICE){
            Logger.error("price is not bigger than 0 in Ticket setter");
            System.exit(0);
        }
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if(comment!=null && comment.isEmpty()){
            Logger.error("comment is empty");
            return;
        }
        this.comment = comment;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Venue getVenue() {
        return venue;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setVenue(Venue venue) {
        if(venue == null){
            Logger.error("venue is null in Ticket setter");
            return;
        }
        this.venue = venue;
    }

    @Override
    public String toString() {
        return "\nTicket{" +
                "\nid=" + id +
                ", \nname='" + name + '\'' +
                ", \ncoordinates=" + coordinates.toString() +
                ", \ncreationDate=" + creationDate +
                ", \nprice=" + price +
                ", \ncomment='" + comment + '\'' +
                ", \nrefundable=" + refundable +
                ", \nticketType=" + ticketType +
                ", \nvenue=" + venue.toString() +
                ", \ncreator=" + creator +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return getId() == ticket.getId() && Double.compare(ticket.getPrice(), getPrice()) == 0 && isRefundable() == ticket.isRefundable() && getName().equals(ticket.getName()) && getCoordinates().equals(ticket.getCoordinates()) && getCreationDate().equals(ticket.getCreationDate()) && Objects.equals(getComment(), ticket.getComment()) && getTicketType() == ticket.getTicketType() && Objects.equals(getVenue(), ticket.getVenue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCoordinates(), getCreationDate(), getPrice(), getComment(), isRefundable(), getTicketType(), getVenue());
    }

    public int compareTo(Ticket ticket){
        return this.getName().length() - ticket.getName().length();
    }
    public int compare(Ticket ticket1, Ticket ticket2) {
        return ticket1.compareTo(ticket2);
    }
}
