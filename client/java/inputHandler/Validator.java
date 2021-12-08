package inputHandler;

import common.Command;
import domainObjectModel.*;

public class Validator {
    private final double MIN_PRICE = 0d;
    private final Long MAX_Y = 3L;
    private final Long MIN_CAPACITY = 0L;
    private final int MAX_STREET_LENGTH = 56;
    private final int MIN_ZIP_CODE_LENGTH = 9;

    public boolean validate(Command command){
        return (command.getCommand() != null || !command.getCommand().isEmpty() && !command.getArgument().isEmpty());
    }
    public boolean validate(Coordinates coordinates){
        return (coordinates.getY() != null && coordinates.getY() <= MAX_Y);
    }
    public boolean validate(Location location){
        return location.getZ()!=null;
    }
    public boolean validate(Address address){
        return address.getStreet().length() <= MAX_STREET_LENGTH &&
                address.getZipCode().length() >= MIN_ZIP_CODE_LENGTH &&
                address.getTown() != null &&
                validate(address.getTown());
    }
    public boolean validate(Venue venue){
        return venue.getName() != null && !venue.getName().isEmpty() &&
                (venue.getCapacity() == null || venue.getCapacity() > MIN_CAPACITY) &&
                venue.getType() != null &&
                (venue.getAddress() == null || validate(venue.getAddress()));
    }
    public boolean validate(Ticket ticket){
        return ticket.getName() != null && !ticket.getName().isEmpty() &&
                ticket.getCoordinates() != null && validate(ticket.getCoordinates()) &&
                ticket.getPrice() > MIN_PRICE &&
                (ticket.getComment() == null || !ticket.getComment().isEmpty()) &&
                ticket.getVenue() != null && validate(ticket.getVenue());
    }
}
