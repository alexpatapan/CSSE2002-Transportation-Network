package vehicles;

import exceptions.EmptyRouteException;
import exceptions.IncompatibleTypeException;
import exceptions.OverCapacityException;
import exceptions.TransportFormatException;
import passengers.Passenger;
import routes.Route;
import stops.Stop;
import utilities.Writeable;
import java.util.ArrayList;
import java.util.List;

/**
 * A base public transport vehicle in the transportation network.
 */
public abstract class PublicTransport implements Writeable {
    // the passengers currently on board the vehicle
    private List<Passenger> passengers;

    // the place the vehicle is currently stopped
    private Stop currentLocation;

    // the maximum passengers allowed on board the vehicle
    private int capacity;

    // the vehicle's identifier
    private int id;

    // the route the vehicle follows
    private Route route;

    /**
     * Creates a new public transport vehicle with the given id, capacity, and
     * route.
     *
     * <p>The vehicle should initially have no passengers on board, and should be placed
     * at the beginning of the given route (given by {@link Route#getStartStop()}).
     * If the route is empty, the current location should be stored as null.
     *
     * <p> If the given capacity is negative, 0 should be stored as the capacity
     * instead (meaning no passengers will be allowed on board this vehicle).
     *
     * @param id The identifying number of the vehicle.
     * @param capacity The maximum number of passengers allowed on board.
     * @param route The route the vehicle follows. Note that the given route should
     *              never be null (@require route != null), and thus will not be
     *              tested with a null value.
     */
    public PublicTransport(int id, int capacity, Route route) {
        this.passengers = new ArrayList<>();
        this.capacity = capacity < 0 ? 0 : capacity;
        this.id = id;
        this.route = route;
        try {
            this.currentLocation = route.getStartStop();
        } catch (EmptyRouteException e) {
            this.currentLocation = null;
        }
    }

    /**
     * Returns the route this vehicle is on.
     *
     * @return The route this vehicle is on.
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Returns the id of this vehicle.
     *
     * @return The id of this vehicle.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the current location of this vehicle.
     *
     * @return The stop this vehicle is currently located at, or null if it is not
     *         currently located at a stop.
     */
    public Stop getCurrentStop() {
        return currentLocation;
    }

    /**
     * Returns the number of passengers currently on board this vehicle.
     *
     * @return The number of passengers in the vehicle.
     */
    public int passengerCount() {
        return passengers.size();
    }

    /**
     * Returns the maximum number of passengers allowed on this vehicle.
     *
     * @return The maximum capacity of the vehicle.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the type of this vehicle, as determined by the type of the route it
     * is on (i.e. The type returned by {@link Route#getType()}).
     *
     * @return The type of this vehicle.
     */
    public String getType() {
        return route.getType();
    }

    /**
     * Returns the passengers currently on-board this vehicle.
     *
     * <p>No specific order is required for the passenger objects in the returned
     * list.
     *
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The passengers currently on the public transport vehicle.
     */
    public List<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    /**
     * Adds the given passenger to this vehicle.
     *
     * <p>If the passenger is null, the method should return without adding it
     * to the vehicle.
     *
     * <p>If the vehicle is already at (or over) capacity, an exception should
     * be thrown and the passenger should not be added to the vehicle.
     *
     * @param passenger The passenger boarding the vehicle.
     * @throws OverCapacityException If the vehicle is already at (or over) capacity.
     */
    public void addPassenger(Passenger passenger) throws OverCapacityException {
        if (passenger == null) {
            return;
        }

        if (passengers.size() >= capacity) {
            throw new OverCapacityException();
        }
        passengers.add(passenger);
    }

    /**
     * Removes the given passenger from the vehicle.
     *
     * <p>If the passenger is null, or is not on board the vehicle, the method should
     * return false, and should not have any effect on the passengers currently
     * on the vehicle.
     *
     * @param passenger The passenger disembarking the vehicle.
     * @return True if the passenger was successfully removed, false otherwise (including
     *         the case where the given passenger was not on board the vehicle to
     *         begin with).
     */
    public boolean removePassenger(Passenger passenger) {
        return passengers.remove(passenger);
    }

    /**
     * Empties the vehicle of all its current passengers, and returns all the passengers
     * who were removed.
     *
     * <p>No specific order is required for the passenger objects in the returned
     * list.
     *
     * <p>If there are no passengers currently on the vehicle, the method just
     * returns an empty list.
     *
     * <p>Modifying the returned list should not result in changes to the internal
     * state of the class.
     *
     * @return The passengers who used to be on the vehicle.
     */
    public List<Passenger> unload() {
        List<Passenger> leaving = passengers;
        passengers = new ArrayList<>();
        return leaving;
    }

    /**
     * Updates the current location of the vehicle to be the given stop.
     *
     * <p>If the given stop is null, or is not on this public transport's route
     * the current location should remain unchanged.
     *
     * @param stop The stop the vehicle has travelled to.
     */
    public void travelTo(Stop stop) {
        if (!route.getStopsOnRoute().contains(stop)) {
            return;
        }

        currentLocation = stop == null ? currentLocation : stop;
    }

    /**
     * Creates a string representation of a public transport vehicle in the format:
     *
     * <p>'{type} number {id} ({capacity}) on route {route}'
     *
     * <p>without the surrounding quotes, and where {type} is replaced by the type of
     * the vehicle, {id} is replaced by the id of the vehicle, {capacity} is replaced
     * by the maximum capacity of the vehicle, and {route} is replaced by the route
     * number of the route the vehicle is on. For example:
     *
     * <p>bus number 1 (30) on route 1
     *
     * @return A string representation of the vehicle.
     */
    @Override
    public String toString() {
        return getType() + " number " + id + " (" + capacity + ") on route " + route.getRouteNumber();
    }

    /**
     * Encodes this vehicle as a string.
     *
     * @return This vehicle encoded as a string.
     */
    public String encode() {
        return getType() + "," + id + "," + capacity + "," +
                route.getRouteNumber();
    }

    /**
     * Creates a new public transport object based on the given string
     * representation.
     *
     * @param transportString The string to decode.
     * @param existingRoutes The routes which currently exist in the transport
     *                       network.
     * @return The decoded public transport object (a Bus, Train, or Ferry,
     * depending on the type given in the string).
     * @throws TransportFormatException If the given string or existingRoutes
     * list is null, or the string is otherwise incorrectly formatted
     * (according to the encode() representation).
     */
    public static PublicTransport decode(String transportString,
                                         List<Route> existingRoutes)
            throws TransportFormatException {

        // a null string or list is incorrectly formatted
        if ((transportString == null) || (existingRoutes == null)) {
            throw new TransportFormatException();
        }

        // a correctly formatted string should have exactly 4 commas
        countCommas(transportString);

        String[] transportInfo = transportString.split(",");

        int id = stringToInt(transportInfo[1]);
        int capacity = stringToInt(transportInfo[2]);
        int routeNumber = stringToInt(transportInfo[3]);

        // retrieve the route specified by the routeNumber
        Route route = findRoute(routeNumber, existingRoutes);

        // if the given route type does not match the given vehicle type, there
        // is an error in formatting
        if (!route.getType().equals(transportInfo[0])) {
            throw new TransportFormatException();
        }

        // create an instance of the given vehicle specified in the string
        PublicTransport transport;
        if (transportInfo[0].equals("ferry") && transportInfo.length == 4) {

            // a ferry may not have a specified ferryType
            transport = createInstance(transportInfo[0], id, capacity, route,
                    null);
        } else try {
            transport = createInstance(transportInfo[0], id, capacity, route,
                    transportInfo[4]);
        } catch (ArrayIndexOutOfBoundsException e) {

            // an out of bounds Exception implies that the string is incorrectly
            // formatted.
            throw new TransportFormatException();
        }

        // add the vehicle to the route
        addTransportToRoute(route, transport);

        return transport;
    }

    /**
     * Create an instance of the given vehicle type.
     *
     * @param type The type of vehicle to be created.
     * @param id The id of the vehicle.
     * @param capacity The capacity of the vehicle.
     * @param route The route the vehicle is on.
     * @param extra Either the registrationNumber, ferryType or CarriageCount
     * depending on the vehicle type.
     * @return An instance of the newly created vehicle.
     */
    private static PublicTransport createInstance(String type, int id,
                                                  int capacity, Route route,
                                                  String extra)
            throws TransportFormatException {
        switch (type) {
            case "bus":
                return new Bus(id, capacity, route, extra);
            case "ferry":
                return new Ferry(id, capacity, route, extra);
            case "train":
                try {
                    return new Train(id, capacity, route,
                            Integer.parseInt(extra.trim()));
                } catch (NumberFormatException e) {
                    throw new TransportFormatException();
                }
            default:

                // if the type is not one of bus, ferry, or train there is an
                // error in formatting
                throw new TransportFormatException();
        }
    }

    /**
     * Search through the existingRoutes for the routeNumber given in the
     * decoded string and return it.
     *
     * @param routeNumber The routeNumber given in the decoded string.
     * @param existingRoutes A list of existing routes.
     * @throws TransportFormatException if the routeNumber does not match any
     * existing routes.
     */
    private static Route findRoute(int routeNumber, List<Route> existingRoutes)
            throws TransportFormatException {
        for (Route j : existingRoutes) {
            if (j.getRouteNumber() == routeNumber) {
                return j;
                }
            }
        throw new TransportFormatException();
    }

    /**
     * Attempt to convert a given String to an Integer.
     *
     * @param string The string to be converted into an integer.
     * @return The integer format of the given string.
     * @throws TransportFormatException if the given string cannot
     * be converted into an integer.
     */
    private static int stringToInt(String string)
            throws TransportFormatException {
        try {
            return Integer.parseInt(string.trim());
        } catch (NumberFormatException e) {

            // if the string cannot be parsed to an integer there is an error
            throw new TransportFormatException();
        }
    }

    /**
     * A private method to count that there are the correct number of commas
     * in the given string.
     *
     * @param transportString the string to compare the number of delimiters of.
     * @throws TransportFormatException if the incorrect number of delimiters
     * appear (and thus is in the wrong format).
     */
    private static void countCommas(String transportString)
            throws TransportFormatException {

        // to find number of delimiters, we remove them from the string
        // and compare the difference in length to that of the original length
        int countCommas = transportString.length() -
                transportString.replaceAll(",","").length();

        if (countCommas != 4) {
            throw new TransportFormatException();
        }
    }

    /**
     * Adds a given PublicTransport to the given Route.
     *
     * @param route The route which the transport is to be added to
     * @param transport The given transport to add to the route
     * @throws TransportFormatException if the transport cannot be added to the
     * route.
     */
    private static void addTransportToRoute(Route route, PublicTransport transport)
            throws TransportFormatException {
        try {
            route.addTransport(transport);
        } catch (EmptyRouteException | IncompatibleTypeException e) {
            throw new TransportFormatException();
        }
    }
}
