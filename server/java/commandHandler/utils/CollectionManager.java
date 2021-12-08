package commandHandler.utils;

import dataAccessObjects.CoordinatesDAO;
import dataAccessObjects.LocationDAO;
import dataAccessObjects.TicketDAO;
import domainObjectModel.Coordinates;
import domainObjectModel.Ticket;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Manages the collection
 * **/
public class CollectionManager {

    private Map<Integer, Ticket> collection;
    private Date creationDate = new Date();
    private TicketDAO ticketDAO;
    private CoordinatesDAO coordinatesDAO;
    private LocationDAO locationDAO;

    public CollectionManager(TicketDAO ticketDAO, LocationDAO locationDAO, CoordinatesDAO coordinatesDAO){
        this.ticketDAO = ticketDAO;
        this.locationDAO = locationDAO;
        this.coordinatesDAO = coordinatesDAO;
        List<Ticket> tickets = ticketDAO.getAll();
        TreeMap<Integer, Ticket> map = new TreeMap<>();
        if (tickets != null){
            HashMap<Integer, Ticket> hashMap = (HashMap<Integer, Ticket>) tickets.stream().collect(Collectors.toMap(Ticket::getId, ticket->ticket));
            map.putAll(hashMap);
        } else {
            map = new TreeMap<>();
        }
        this.collection = Collections.synchronizedMap(map);
    }
    public int getSize(){
        return this.collection.size();
    }
    public Class getCollectionClass(){
        return this.collection.getClass();
    }
    public Date getCreationDate(){
        return this.creationDate;
    }
    /**
     * Create new entry in collection with specified id
     * @see Ticket
     * **/
    public void create(Integer key, Ticket ticket){
        ticket.setId(key);
        this.ticketDAO.create(ticket);
        this.collection.put(key, ticket);
    }
    /**
     * Get the ticket by its id
     * @see Ticket
     * **/
    public Ticket read(Integer key){
        return this.collection.get(key);
    }
    /**
     * Get all tickets
     * **/
    public Ticket[] readAll(){
        return this.collection.values().toArray(new Ticket[this.collection.values().size()]);
    }
    /**
     * Update information about ticket by its key
     * **/
    public void update(Integer key, Ticket ticket){
        this.ticketDAO.update(key, ticket);
        this.collection.replace(key, ticket);
    }
    /**
     * Delete ticket by its key
     * **/
    public void delete(Integer key){
        this.ticketDAO.delete(key);
        this.collection.remove(key);
    }
    /**
     * Clear the collection
     * **/
    public void deleteAll(){
        this.locationDAO.deleteAll();
        this.coordinatesDAO.deleteAll();
        this.collection.clear();
    }
    public void removeIf(Predicate<Ticket> predicate){
        ArrayList<Integer> toDelete = new ArrayList<>();
        List<Ticket> values = new ArrayList<>(collection.values());
        for (int i = 0; i<collection.size(); i++){
            if (predicate.test(values.get(i))){
                delete(values.get(i).getId());
                toDelete.add(values.get(i).getId());
            }
        }
        for (int i = 0; i<toDelete.size(); i++){
            collection.remove(toDelete.get(i));
        }
    }
    /**
     * @returns true is collection contains such key
     * **/
    public boolean contains(Integer key){
        return this.collection.containsKey(key);
    }
    /**
     * @returns iterator of collection
     * **/
    public Iterator getIterator(){
        return this.collection.entrySet().iterator();
    }
    /**
     * @returns stream of collection
     * **/
    public Stream<Ticket> getStream(){
        return this.collection.values().stream();
    }

}
