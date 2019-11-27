package network;

import exceptions.TransportFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import routes.BusRoute;
import routes.FerryRoute;
import routes.Route;
import routes.TrainRoute;
import stops.Stop;
import vehicles.Bus;
import vehicles.Ferry;
import vehicles.PublicTransport;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class NetworkTest {

    private Network network;

    @Before
    public void setUp() {
        network = new Network();
    }

    @After
    public void tearDown() {
        network = null;
    }

    @Test
    public void testNoParameterConstructor() {
        Network network = new Network();
        assertEquals(new ArrayList<Stop>(), network.getStops());
        assertEquals(new ArrayList<Route>(), network.getRoutes());
        assertEquals(new ArrayList<PublicTransport>(), network.getVehicles());
    }

    @Test
    public void testFilenameConstructor() throws IOException,
            TransportFormatException  {
        Network network = new Network("test/networkFiles/NetworkTest1.txt");
        ArrayList<Stop> expectedStops = new ArrayList<Stop>(Arrays.asList(
                Stop.decode("stop0:0:1"),
                Stop.decode("stop1:-1:0"),
                Stop.decode("stop2:4:2"),
                Stop.decode("stop3:2:-8")));
        ArrayList<Route> expectedRoutes = new ArrayList<Route>(Arrays.asList(
                Route.decode("train,red,1:stop0|stop2|stop1", expectedStops),
                Route.decode("bus,blue,2:stop1|stop3|stop0", expectedStops),
                Route.decode("ferry,orange,3:stop1",expectedStops)));
        ArrayList<PublicTransport> expectedVehicles = new ArrayList<>(Arrays.asList(
                PublicTransport.decode("ferry,123,30,3,citycat", expectedRoutes),
                PublicTransport.decode("train,42,60,1,3", expectedRoutes),
                PublicTransport.decode("bus,412,20,2,ABC123", expectedRoutes)));

        assertEquals(expectedStops, network.getStops());
        assertEquals(expectedRoutes, network.getRoutes());

        //vehicles do not have an equals method, thus we compare equality of objects
        //with their string representation
        assertEquals(expectedVehicles.toString(), network.getVehicles().toString());
    }

    @Test
    public void testIntegerSpaces() throws IOException,
            TransportFormatException {
        Network expectedNetwork = new Network("test/networkFiles/NetworkTest1.txt");
        Network actualNetwork = new Network("test/networkFiles/" +
                "testIntegerSpaces.txt");

        assertEquals(expectedNetwork.getVehicles().toString(),
                actualNetwork.getVehicles().toString());
        assertEquals(expectedNetwork.getStops(), actualNetwork.getStops());
        assertEquals(expectedNetwork.getRoutes(), actualNetwork.getRoutes());
    }

    @Test(expected = IOException.class)
    public void testNull() throws IOException, TransportFormatException {
        new Network(null);
    }

    @Test(expected = TransportFormatException.class)
    public void testIncorrectCount() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testIncorrectCount.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testNegativeCount() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testNegativeCount.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testExtraLinesAtBottom() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testExtraLines.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testExtraSpaceAtBottom() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testExtraSpace.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testBlankLines() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/testBlankLines.txt");
    }

    @Test
    public void testEmpty() throws IOException,
            TransportFormatException {
        Network network = new Network("test/networkFiles/" +
                "testEmpty.txt");
        assertEquals(new ArrayList<PublicTransport>(), network.getVehicles());
        assertEquals(new ArrayList<Route>(), network.getRoutes());
        assertEquals(new ArrayList<Stop>(), network.getStops());
    }

    @Test(expected = TransportFormatException.class)
    public void testEmptyNoCount() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testEmptyNoCount.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testStopInvalidExtraDelimiters() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testStopExtraDelimiters.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testStopInvalidTooFewDelimiters() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testStopTooFewDelimiters.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testStopInvalidNoName() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testStopNoName.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testStopInvalidInteger() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testStopInteger.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testStopInvalidNoCoordinates() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testStopNoCoordinates.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testRouteType() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testRouteType.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testRouteNumber() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testRouteNumber.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testRouteInvalidStop() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testRouteInvalidStop.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testRouteEmptyStopName() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testRouteEmptyStopName.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testRouteExtraDelimiters() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testRouteExtraDelimiters.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testRouteTooFewDelimiters() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testRouteTooFewDelimiters.txt");
    }

    @Test
    public void testRouteValidNoStops() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testRouteValidNoStops.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testVehicleType() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testVehicleType.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testVehicleInteger() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testVehicleInteger.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testVehicleMissingRoute() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testVehicleMissingRoute.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testVehicleReferencingWrongRouteType() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testVehicleReferencingWrongRouteType.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testTrainCarriageCountNotInteger() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testTrainCarriageCountNotInteger.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testVehicleNoStopsOnRoute() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testVehicleNoStopsOnRoute.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testVehicleExtraDelimiters() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testVehicleExtraDelimiters.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testVehicleTooFewDelimiters() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testVehicleTooFewDelimiters.txt");
    }

    @Test(expected = TransportFormatException.class)
    public void testVehicleMissingParts() throws IOException,
            TransportFormatException {
        new Network("test/networkFiles/" +
                "testVehicleMissingParts.txt");
    }

    @Test
    public void testFerryType() throws IOException, TransportFormatException {

        // an empty ferrytype should be recorded as CityCat
        Network network = new Network("test/networkFiles/" +
                "testFerryType.txt");
        Ferry ferry = (Ferry) network.getVehicles().get(0);
        assertEquals("CityCat", ferry.getFerryType());
    }

    @Test
    public void testAddRoute() {
        Route route = new BusRoute("route", 1);
        network.addRoute(route);
        assertTrue(network.getRoutes().contains(route));
    }

    @Test
    public void testAddRouteNull() {
        network.addRoute(null);
        assertFalse(network.getRoutes().contains(null));
    }

    @Test
    public void testAddStop() {
        Stop stop = new Stop("stop",10,10);
        network.addStop(stop);
        assertTrue(network.getStops().contains(stop));
    }

    @Test
    public void testAddStopNull() {
        network.addStop(null);
        assertFalse(network.getStops().contains(null));
    }

    @Test
    public void testAddStops() {
        Stop stop = new Stop("stop", 10, 10);
        Stop stop2 = new Stop("stop2", 11, 11);
        network.addStops(new ArrayList<>(Arrays.asList(stop, stop2)));
        assertTrue(network.getStops().contains(stop));
        assertTrue(network.getStops().contains(stop2));
    }

    @Test
    public void testAddStopsNull() {
        Stop stop = new Stop("stop", 10, 10);
        network.addStops(new ArrayList<>(Arrays.asList(stop, null)));
        assertFalse(network.getStops().contains(stop));
        assertFalse(network.getStops().contains(null));
    }

    @Test
    public void testAddVehicle() {
        Route route = new BusRoute("route", 10);
        PublicTransport vehicle = new Bus(1,10, route, "rego");
        network.addVehicle(vehicle);
        assertTrue(network.getVehicles().contains(vehicle));
    }

    @Test
    public void testAddVehicleNull() {
        network.addVehicle(null);
        assertFalse(network.getVehicles().contains(null));
    }

    @Test
    public void testGetRoutes() throws IOException, TransportFormatException {
        Network network = new Network("test/networkFiles/NetworkTest1.txt");

        assertEquals(network.getRoutes(), new ArrayList<Route>(Arrays.asList(
                new TrainRoute("red", 1),
                new BusRoute("blue", 2),
                new FerryRoute("orange", 3))));
    }

    @Test
    public void testGetRoutesModifyReturn() throws IOException,
            TransportFormatException {
        Network network = new Network("test/networkFiles/NetworkTest1.txt");
        Route uq = new BusRoute("UQ", 44);
        network.getRoutes().add(uq);
        assertFalse(network.getRoutes().contains(uq));
    }

    @Test
    public void testGetStops() throws IOException, TransportFormatException {
        Network network = new Network("test/networkFiles/NetworkTest1.txt");

        ArrayList<Stop> existingStops = new ArrayList<Stop>(Arrays.asList(
                new Stop("stop0",0,1),
                new Stop("stop1",-1,0),
                new Stop("stop2",4,2),
                new Stop("stop3",2,-8)));
        Route.decode("train,red,1:stop0|stop2|stop1", existingStops);
        Route.decode("bus,blue,2:stop1|stop3|stop0", existingStops);
        Route.decode("ferry,orange,3:stop1", existingStops);

        assertEquals(network.getStops(), existingStops);
    }

    @Test
    public void testGetStopsModifyReturn() throws IOException,
            TransportFormatException {
        Network network = new Network("test/networkFiles/NetworkTest1.txt");

        ArrayList<Stop> existingStops = new ArrayList<Stop>(Arrays.asList(
                new Stop("stop0",0,1),
                new Stop("stop1",-1,0),
                new Stop("stop2",4,2),
                new Stop("stop3",2,-8)));
        Route.decode("train,red,1:stop0|stop2|stop1", existingStops);
        Route.decode("bus,blue,2:stop1|stop3|stop0", existingStops);
        Route.decode("ferry,orange,3:stop1", existingStops);

        network.getStops().add(new Stop("stop4", 5, 10));
        assertEquals(network.getStops(), existingStops);
    }

    @Test
    public void testGetVehicles() throws IOException,
            TransportFormatException {
        Network network = new Network("test/networkFiles/NetworkTest1.txt");

        ArrayList<Stop> existingStops = new ArrayList<Stop>(Arrays.asList(
                new Stop("stop0",0,1),
                new Stop("stop1",-1,0),
                new Stop("stop2",4,2),
                new Stop("stop3",2,-8)));

        ArrayList<Route> existingRoutes = new ArrayList<Route>(Arrays.asList(
                Route.decode("train,red,1:stop0|stop2|stop1", existingStops),
                Route.decode("bus,blue,2:stop1|stop3|stop0", existingStops),
                Route.decode("ferry,orange,3:stop1", existingStops)));

        ArrayList<PublicTransport> expectedVehicles =
                new ArrayList<PublicTransport>(Arrays.asList(
                        PublicTransport.decode("ferry,123,30,3,citycat",
                                existingRoutes),
                        PublicTransport.decode("train,42,60,1,3",
                                existingRoutes),
                        PublicTransport.decode("bus,412,20,2,3",
                                existingRoutes)));
        assertEquals(network.getVehicles().toString(), expectedVehicles.toString());

        // modifying the return should not change
        network.getVehicles().add(new Bus(1, 1,existingRoutes.get(0),"rego"));
        assertEquals(network.getVehicles().toString(), expectedVehicles.toString());
    }

    @Test
    public void testSave() throws IOException, TransportFormatException {
        Network network = new Network();

        Stop stop = new Stop("stop",1, 2);
        Stop stop2 = new Stop("stop2", 10,11);
        BusRoute busroute = new BusRoute("busroute", 40);
        Bus bus = new Bus(10, 20, busroute, "rego");

        network.addStops(new ArrayList<Stop>(Arrays.asList(stop, stop2)));

        network.addRoute(busroute);
        network.addVehicle(bus);

        busroute.addStop(stop);
        busroute.addStop(stop2);

        network.save("test/networkFiles/testSave.txt");

        assertEquals(network.getRoutes(), new Network("test/networkFiles/testSave.txt").getRoutes());
        assertEquals(network.getVehicles().toString(), new Network("test/networkFiles/testSave.txt").getVehicles().toString());
        assertEquals(network.getStops(), new Network("test/networkFiles/testSave.txt").getStops());
    }

    @Test
    public void testSaveNoStopsNoVehicles() throws Exception {
        Network network = new Network();
        FerryRoute ferryroute = new FerryRoute("ferryroute", 100);
        network.addRoute(ferryroute);
        network.save("test/networkFiles/testSaveNoStopsNoVehicles.txt");
        assertEquals(network.getRoutes(), new Network("test/networkFiles/testSaveNoStopsNoVehicles.txt").getRoutes());
        assertEquals(network.getVehicles().toString(), new Network("test/networkFiles/testSaveNoStopsNoVehicles.txt").getVehicles().toString());
        assertEquals(network.getStops(), new Network("test/networkFiles/testSaveNoStopsNoVehicles.txt").getStops());
    }

    @Test
    public void testSaveNull() throws IOException {
        Network network = new Network();
        network.save(null);
    }

}


