package commandHandler.utils;

import domainObjectModel.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Reads the JSON file and returns ready-made collection. Stores collection in file
 * **/
public class FileManager {
    private String filePath;
    public FileManager(String filePath){
        this.filePath = filePath;
    }
    /**
     * Returns the array of JSON objects from .json file
     * @return JSONArray
     * **/
    public JSONArray getJSON() {
        JSONParser parser = new JSONParser();
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String fileText = "";
            while (scanner.hasNextLine()) {
                fileText += scanner.nextLine();
            }
            if(fileText.isEmpty()){
                return new JSONArray();
            }
            JSONArray result = (JSONArray) parser.parse(fileText);
            return result;
        }
        catch(FileNotFoundException e){
            Logger.error("File not found");
            return new JSONArray();
        }catch (ParseException e){
            Logger.error(e.getMessage());
            Logger.error("Failed to parse the file");
            return new JSONArray();
        }
    }
    /**
     * Reads JSON and returns the collection
     * @return Map
     * **/
    public Map<Integer, Ticket> load(){
        Map<Integer, Ticket> result = new TreeMap<Integer, Ticket>();
        JSONArray arr = getJSON();
        if (arr == null){
            Logger.error("JSON array is null in FileManager");
            System.exit(0);
        }
        for (Object obj : arr){
            JSONObject JSONTicket = (JSONObject) obj;
                JSONObject JSONCoordinates = (JSONObject) JSONTicket.get("coordinates");
                JSONObject JSONVenue = (JSONObject) JSONTicket.get("venue");
                JSONObject JSONAddress = (JSONObject) JSONVenue.get("address");
                JSONObject JSONLocation = (JSONObject) JSONAddress.get("location");

                Coordinates coordinates = new Coordinates(
                        (Double)JSONCoordinates.get("x"),
                        (Long)JSONCoordinates.get("y")
                );
                Location town = new Location(
                        (int)(long)JSONLocation.get("x"),
                        (float)(double) JSONLocation.get("y"),
                        (Double) JSONLocation.get("z")
                );
                Address address = new Address(
                        (String) JSONAddress.get("street"),
                        (String) JSONAddress.get("zipCode"),
                        town
                );
                Venue venue = new Venue(
                        (String) JSONVenue.get("name"),
                        (Long) JSONVenue.get("capacity"),
                        VenueType.valueOf((String)JSONVenue.get("type")),
                        address
                );
                Ticket ticket = new Ticket(
                        (String) JSONTicket.get("name"),
                        coordinates,
                        (Double) JSONTicket.get("price"),
                        (String) JSONTicket.get("comment"),
                        (Boolean) JSONTicket.get("refundable"),
                        TicketType.valueOf((String) JSONTicket.get("type")),
                        venue
                );

                result.put(ticket.getId(), ticket);
        }
        return result;
    }
/**
 * Make JSON out of array of Ticket
 * @return String JSON
 * **/
    private String getJSONString(Ticket[] tickets){
        JSONArray jsonArray = new JSONArray();
        for (Ticket ticket : tickets){
            JSONObject jsonTown = new JSONObject();
            jsonTown.put("x", ticket.getVenue().getAddress().getTown().getX());
            jsonTown.put("y", ticket.getVenue().getAddress().getTown().getY());
            jsonTown.put("z", ticket.getVenue().getAddress().getTown().getZ());

            JSONObject jsonAddress = new JSONObject();
            jsonAddress.put("street", ticket.getVenue().getAddress().getStreet());
            jsonAddress.put("zipCode", ticket.getVenue().getAddress().getZipCode());
            jsonAddress.put("location", jsonTown);

            JSONObject jsonVenue = new JSONObject();
            jsonVenue.put("name", ticket.getVenue().getName());
            jsonVenue.put("capacity", ticket.getVenue().getCapacity());
            jsonVenue.put("type", ticket.getVenue().getType().toString());
            jsonVenue.put("address", jsonAddress);

            JSONObject jsonCoordinates = new JSONObject();
            jsonCoordinates.put("x", ticket.getCoordinates().getX());
            jsonCoordinates.put("y", ticket.getCoordinates().getY());

            JSONObject jsonTicket = new JSONObject();
            jsonTicket.put("name", ticket.getName());
            jsonTicket.put("coordinates", jsonCoordinates);
            jsonTicket.put("price", ticket.getPrice());
            jsonTicket.put("comment", ticket.getComment());
            jsonTicket.put("refundable", ticket.isRefundable());
            jsonTicket.put("type", ticket.getTicketType().toString());
            jsonTicket.put("venue", jsonVenue);

            jsonArray.add(jsonTicket);
        }
        return jsonArray.toJSONString();
    }
/**
 * Write collection in JSON in file
 * **/
    public void save(CollectionManager collectionManager){
        Ticket[] tickets = collectionManager.readAll();
        String JSONString = getJSONString(tickets);

        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath));
            out.write(JSONString);
            out.flush();
            out.close();
        } catch (IOException ioException) {
            Logger.error("incorrect file path in FileManager save()");
            System.exit(0);
        }
    }
}
