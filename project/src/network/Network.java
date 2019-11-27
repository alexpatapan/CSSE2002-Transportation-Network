package network;

import exceptions.TransportFormatException;
import routes.Route;
import stops.Stop;
import vehicles.PublicTransport;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the transportation network, and manages all of the various
 * components therein.
 */
public class Network {

    // a list of stops on the network
    private List<Stop> stops;

    // a list of vehicles on the network
    private List<PublicTransport> vehicles;

    // a list of routes on the network
    private List<Route> routes;

    /**
     * Creates a new empty Network with no stops, vehicles, or routes.
     */
    public Network() {
        stops = new ArrayList<>();
        vehicles = new ArrayList<>();
        routes = new ArrayList<>();
    }

    /**
     * Creates a new Network from information contained in the file indicated
     * by the given filename.
     *
     * @param filename The name of the file to load the network from.
     */
    public Network(String filename) throws IOException,
            TransportFormatException {

        // the other constructor is called to initialise lists
        this();

        if (filename == null) {
            throw new IOException();
        }

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();

        // the readAndDecode method is called 3 times to decode the stop, route
        // and vehicle objects
        readAndDecode(line, reader, "stop");
        line = reader.readLine();
        readAndDecode(line, reader, "route");
        line = reader.readLine();
        readAndDecode(line, reader, "vehicle");

        // the file should not end with multiple line breaks
        if (reader.readLine() != null) {
            throw new TransportFormatException();
        }

        // the file we are reading from must be closed
        reader.close();
    }

    /**
     * Read the lines given from the file and decode them to the appropriate
     * type. The first line should give the number of objects to decode, with
     * proceeding lines giving an encoded string format of each object.
     *
     * @param line The current line the reader is on.
     * @param reader The BufferedReader used to read from the file.
     * @param type The type of object which will be decoded.
     * @throws TransportFormatException when the file has been incorrectly
     * formatted.
     * @throws IOException when there is an unresolved issue when attempting
     * to read from the file.
     */
    private void readAndDecode(String line, BufferedReader reader, String type)
            throws TransportFormatException, IOException {

        // find the number of lines below to decode
        int num;
        try {
            num = Integer.parseInt(line.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new TransportFormatException();
        }

        //read through the appropriate number of lines as specified
        for (int i = 0; i < num; i++) {
            line = reader.readLine();

            // decode and add the appropriate object to the network
            switch (type) {
                case "stop":
                    addStop(Stop.decode(line));
                    break;
                case "route":
                    addRoute(Route.decode(line, stops));
                    break;
                case "vehicle":
                    addVehicle(PublicTransport.decode(line, routes));
            }
        }
    }

    /**
     * Adds the given route to the network.
     *
     * @param route The route to add to the network.
     */
    public void addRoute(Route route) {
        if (route != null) {
            routes.add(route);
        }
    }

    /**
     * Adds the given stop to the transportation network.
     *
     * @param stop The stop to add to the network.
     */
    public void addStop(Stop stop) {
        if (stop != null) {
            stops.add(stop);
        }
    }

    /**
     * Adds multiple stops to the transport network.
     *
     * @param stops The stops to add to the network.
     */
    public void addStops(List<Stop> stops) {
        if (!stops.contains(null)) {
            this.stops.addAll(stops);
        }
    }

    /**
     * If the given vehicle is null, it should not be added to the network.
     *
     * @param vehicle The vehicle to add to the network.
     */
    public void addVehicle(PublicTransport vehicle) {
        if (vehicle != null) {
            vehicles.add(vehicle);
        }
    }

    /**
     * Gets all the routes in this network.
     *
     * @return All the routes in the network.
     */
    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    /**
     * Gets all of the stops in this network.
     *
     * @return All the stops in the network.
     */
    public List<Stop> getStops() {
        return new ArrayList<>(stops);
    }

    /**
     * Gets all the vehicles in this transportation network.
     *
     * @return All the vehicles in the transportation network.
     */
    public List<PublicTransport> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    /**
     * Saves this network to the file indicated by the given filename.
     *
     * @param filename The name of the file to save the network to.
     * @throws IOException If there are any IO errors whilst writing to
     * the file.
     */
    public void save(String filename)
            throws IOException {
        if (filename == null) {
            return;
        }
        File file = new File(filename);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        // write the stops, then routes then vehicles to the file.
        writeListToFile(stops, writer);
        writeListToFile(routes, writer);
        writeListToFile(vehicles, writer);

        writer.close();
    }

    /**
     * Given a list of objects, encode each object of the list to string
     * format and write to the given file.
     *
     * @param list The list of objects to be encoded in the file.
     * @param writer The writer used to write to the file.
     * @throws IOException If there an error in writing to the file.
     */
    private void writeListToFile(List list, BufferedWriter writer)
            throws IOException {
        writer.write("" + list.size());
        writer.newLine();

        // check to see the type of object given in the list so it can be
        // encoded to the correct format
        for (Object i : list) {
            if (i instanceof Route) {
                Route j = (Route) i;
                writer.write(j.encode());
            } else if (i instanceof Stop) {
                Stop j = (Stop) i;
                writer.write(j.encode());
            } else {

                // we know it is an instance of PublicTransport if it is
                // not an instance of Route or Stop
                PublicTransport j = (PublicTransport) i;
                writer.write(j.encode());
            }
            writer.newLine();
            }
    }
}
