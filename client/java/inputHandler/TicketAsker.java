package inputHandler;

import common.Command;
import common.User;
import domainObjectModel.*;
import utils.Logger;
import utils.UserInput;
import utils.UserOutput;

import java.util.Scanner;

/**
 * Gets all information about ticket or its components from user
 * **/
public class TicketAsker {
    private UserInput userInput;
    private Validator validator;
    private final String YES = "y";
    private final String NO = "n";
    public TicketAsker(UserInput userInput){
        this.userInput = userInput;
        this.validator = new Validator();
    }
    private Command askCommand(){
        Command result = new Command();
        String[] command = userInput.askCommand();
        if(command == null){
            return null;
        }
        result.setCommand(command[0]);
        result.setArgument(command[1]);
        return result;
    }
    private String askPropertyString(String request){
        return userInput.askPropertyString(request);
    }
    private Integer askPropertyInt(String request){
        return userInput.askPropertyInt(request);
    }
    private Coordinates askCoordinates() throws NullPointerException{
        Double x = Double.valueOf(askPropertyInt("Enter x"));
        Long y;
        do{
            y = Long.valueOf(askPropertyInt("Enter y"));
            if (y > Coordinates.MAX_Y){
                Logger.error("y must be less than " + Coordinates.MAX_Y);
            }
        } while (y > Coordinates.MAX_Y);
        Coordinates coordinates = new Coordinates(x,y);
        return coordinates;
    }
    private Location askLocation(){
        Location location = new Location(
                askPropertyInt("Enter x"),
                Float.valueOf(askPropertyInt("Enter y")),
                Double.valueOf(askPropertyInt("Enter z"))
        );
        return location;
    }
    private Address askAddress(){
        String name;
        do{
            name = askPropertyString("Enter the name of the street");
            if (name.length() > Address.MAX_STREET_LENGTH){
                Logger.error("Street name must be less than " + Address.MAX_STREET_LENGTH + " symbols");
            }
        } while (name.length() > Address.MAX_STREET_LENGTH);
        String zipCode;
        do{
            zipCode = askPropertyString("Enter the zip code of the street");
            if (zipCode.length() < Address.MIN_ZIP_CODE_LENGTH){
                Logger.error("Zip code must be more than " + Address.MIN_ZIP_CODE_LENGTH + " symbols");
            }
        } while (zipCode.length() < Address.MIN_ZIP_CODE_LENGTH);
        Address address = new Address(
                name,
                zipCode,
                askValidatedLocation()
        );
        return address;
    }
    private Venue askVenue(){
        String venueType;
        VenueType type;
        while(true){
            try{
                venueType = askPropertyString("Enter the venue type");
                type = VenueType.valueOf(venueType);
                break;
            } catch (IllegalArgumentException e){
                UserOutput.println("Incorrect venue type");
            }
        }
        Long capacity;
        do{
            capacity = Long.valueOf(askPropertyInt("Enter the capacity"));
            if (capacity < Venue.MIN_CAPACITY) {
                Logger.error("Capacity must be more than " + Venue.MIN_CAPACITY);
            }
        } while (capacity < Venue.MIN_CAPACITY);

        Venue venue = new Venue(
                askPropertyString("Enter the name of the venue"),
                capacity,
                type,
                askValidatedAddress()
        );
        return venue;
    }
    private Ticket askTicket() throws NullPointerException{
        String ticketType;
        TicketType type;
        while(true){
            try{
                ticketType = askPropertyString("Enter the ticket type");
                type = TicketType.valueOf(ticketType);
                break;
            } catch (IllegalArgumentException e){
                UserOutput.println("Incorrect ticket type");
            }
        }
        Double price;
        do{
            price = Double.valueOf(askPropertyInt("Enter the price"));
            if (price < Ticket.MIN_PRICE){
                Logger.error("Price must be more than " + Ticket.MIN_PRICE);
            }
        } while (price < Ticket.MIN_PRICE);
        Ticket ticket = new Ticket(
                askPropertyString("Enter the name of the ticket"),
                askValidatedCoordinates(),
                price,
                askPropertyString("Enter the comment"),
                userInput.ask("Is it refundable?[y/n]"),
                type,
                askValidatedVenue()
        );
        return ticket;
    }
    public Command askValidatedCommand(){
        Command command;
        do{
            command = askCommand();
            if(command == null){
                return null;
            }
            if(!validator.validate(command)){
                UserOutput.println("Incorrect command");
                if (!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(command));
        return command;
    }

    public Ticket askValidatedTicket(User creator) throws NullPointerException{
        Ticket ticket;
        do{
            ticket = askTicket();
            ticket.setCreator(creator.getLogin());
            if(!validator.validate(ticket)){
                UserOutput.println("Incorrect ticket data");
                if (!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(ticket));
        return ticket;
    }
    public Venue askValidatedVenue(){
        Venue venue;
        do{
            venue = askVenue();
            if(!validator.validate(venue)){
                UserOutput.println("Incorrect venue data");
                if (!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(venue));
        return venue;
    }
    public Address askValidatedAddress(){
        Address address;
        do{
            address = askAddress();
            if(!validator.validate(address)){
                UserOutput.println("Incorrect venue data");
                if (!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(address));
        return address;
    }
    public Location askValidatedLocation(){
        Location location;
        do{
            location = askLocation();
            if(!validator.validate(location)){
                UserOutput.println("Incorrect venue data");
                if (!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(location));
        return location;
    }
    public Coordinates askValidatedCoordinates() throws NullPointerException{
        Coordinates coordinates;
        do{
            coordinates = askCoordinates();
            if(!validator.validate(coordinates)){
                UserOutput.println("Incorrect venue data");
                if (!userInput.isInteractive()){
                    return null;
                }
            }
        } while(!validator.validate(coordinates));
        return coordinates;
    }
}