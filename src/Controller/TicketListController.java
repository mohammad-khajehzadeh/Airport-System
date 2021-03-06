package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;

public class TicketListController implements Initializable {

    private String ID;
    private boolean click = false;

    PassengerRepository passengerRepository = new PassengerRepository();
    TicketRepository ticketRepository = new TicketRepository();
    FlightRepository flightRepository = new FlightRepository();

    @FXML
    ImageView airplane;
    @FXML
    TableColumn<String, Flight> flightNumberColumn;
    @FXML
    TableColumn<String, Flight> DateColumn;
    @FXML
    TableColumn<String, Flight> depColumn;
    @FXML
    TableColumn<String, Flight> desColumn;
    @FXML
    TableColumn<String, Flight> priceColumn;
    @FXML
    TableColumn<String, Flight> hourColumn;
    @FXML
    TableColumn<String, Flight> numColumn;
    @FXML
    TableView<Flight> tableView;
    @FXML
    TextField flightNumberField;
    @FXML
    TextField depField;
    @FXML
    TextField desField;
    @FXML
    TextField dateField;
    @FXML
    TextField priceField;
    @FXML
    TextField hourField;
    @FXML
    Button buyBTN;
    @FXML
    Label priceLBL;
    @FXML
    Label errorLBL;
    @FXML
    ProgressBar progressBar;
    @FXML
    Label percentLBL;
    @FXML
    Button increaseBTN;
    @FXML
    Button decreaseBTN;
    @FXML
    Label numberLBL;

    // action for the buy btn
    public void buyBTN() {
            /*
            at first check the money of the passenger
            at second check the capacity
             */
        if (moneyCheck() && capacityCheck() && click) {
            // make the new ticket
            addTicket();
            // decrease the flight capacity
            decreaseCapacity();
            // decrease the money of the flight
            decreaseMoney();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "ticket added.", ButtonType.CLOSE);
            alert.showAndWait();
            refresh();
        } else {
            // if dont have the enough money
            if (!moneyCheck()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "you dont have enough money", ButtonType.CLOSE);
                alert.showAndWait();
            }
            // there isn't enough capacity
            if (!capacityCheck()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "there isn't capacity", ButtonType.CLOSE);
                alert.showAndWait();
            }
            if (!click) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "choose the ticket", ButtonType.OK);
                alert.showAndWait();
            }
        }

    }

    // enter the btn
    public void buyBTNEnter() {
        buyBTN.setGraphic(new ImageView("file:Icons/buy.png"));
        buyBTN.setText(null);
    }

    // exit the btn
    public void buyBTNExit() {
        buyBTN.setStyle("-fx-background-color:  #03e203");
        buyBTN.setGraphic(null);
        buyBTN.setText("buy");
    }

    // when click ==> the number of the ticket will be increase (max 10)
    public void increaseBTN() {
        if (Integer.parseInt(numberLBL.getText()) < 10 && click) {
            numberLBL.setText(Integer.toString(Integer.parseInt(numberLBL.getText()) + 1));
            setPriceLBL();
        } else if (!click) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "choose one of them", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    // when click ==> the number of the ticket will be decrease (min 0)
    public void decreaseBTN() {
        if (Integer.parseInt(numberLBL.getText()) > 0 && click) {
            numberLBL.setText(Integer.toString(Integer.parseInt(numberLBL.getText()) - 1));
            setPriceLBL();
        } else if (!click) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "choose one of them", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        airplane.setImage(new Image("file:Icons/airplane.png"));
        tableList();

        Flight flight = new Flight();
        {
            tableView.setRowFactory(tv -> {
                TableRow<Flight> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                            && event.getClickCount() == 1) {

                        Flight clickedRow = row.getItem();
                        show(clickedRow.getFlightNumber());
                        click = true;
                    }
                });
                return row;
            });
        }
    }

    // set the price lbl
    private void setPriceLBL() {
        priceLBL.setText(Integer.toString(Integer.parseInt(numberLBL.getText()) * Integer.parseInt(priceField.getText())));
    }

    // set the value for the table //  show the open flight
    private void tableList() {

        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        depColumn.setCellValueFactory(new PropertyValueFactory<>("dep"));
        desColumn.setCellValueFactory(new PropertyValueFactory<>("des"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        hourColumn.setCellValueFactory(new PropertyValueFactory<>("hours"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        FlightRepository flightRepository = new FlightRepository();
        List<Flight> flights = flightRepository.flightList();

        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getStatus() == Flight.status.open) {
                tableView.getItems().add(flights.get(i));
            }
        }

    }

    // after choose the ticket ==> show the flight info to the fields
    private void show(String flightNumber) {
        FlightRepository flightRepository = new FlightRepository();

        List<Flight> flights = flightRepository.flightList();
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlightNumber().equals(flightNumber)) {
                progressBarShow(flights.get(i).getCapacity(), flights.get(i).getTotalCapacity());
                flightNumberField.setText(flights.get(i).getFlightNumber());
                depField.setText(flights.get(i).getDep());
                desField.setText(flights.get(i).getDes());
                dateField.setText(flights.get(i).getDate());
                priceField.setText(Integer.toString(flights.get(i).getPrice()));
                hourField.setText(flights.get(i).getHours());
            }
        }


    }

    // decrease the money of the passenger
    private void decreaseMoney() {

        List<passenger> passengerList = passengerRepository.passengerList();

        for (int i = 0; i < passengerList.size(); i++) {

            if (passengerList.get(i).getID().equals(getID())) {
                passengerRepository.increaseMoney(Integer.toString(Integer.parseInt(passengerList.get(i).getMoney()) - Integer.parseInt(priceLBL.getText())), getID());
            }

        }
    }

    // decrease capacity
    private void decreaseCapacity() {
        List<Flight> flights = flightRepository.flightList();

        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlightNumber().equals(flightNumberField.getText())) {
                String lineWithoutSpaces = flightNumberField.getText();
                flightRepository.capacityCorrection(flights.get(i).getCapacity() - Integer.parseInt(numberLBL.getText()), lineWithoutSpaces);
            }
        }
    }

    // check the capacity
    private boolean capacityCheck() {
        List<Flight> flights = flightRepository.flightList();
        Boolean check = false;

        for (int i = 0; i < flights.size(); i++) {

            if (flights.get(i).getFlightNumber().equals(flightNumberField.getText()) &&
                    flights.get(i).getCapacity() >= Integer.parseInt(numberLBL.getText())) {

                check = true;
                break;
            } else if (i == (flights.size() - 1)) {
                check = false;
            }
        }
        return check;
    }

    // check the passenger money
    private boolean moneyCheck() {
        List<passenger> passengerList = passengerRepository.passengerList();
        boolean check = false;
        for (int i = 0; i < passengerList.size(); i++) {
            if (passengerList.get(i).getID().equals(getID()) && Integer.parseInt(passengerList.get(i).getMoney())
                    >= Integer.parseInt(priceLBL.getText())) {
                check = true;
                break;
            } else if (i == (passengerList.size() - 1))
                check = false;

        }
        return check;
    }

    // at the end make the new ticket
    private void addTicket() {

        Random rnd = new Random();

        for (int j = 0; j < Integer.parseInt(numberLBL.getText()); j++) {
            ticketRepository.TicketAdder(getID(), flightNumberField.getText(), Integer.parseInt(priceField.getText()), Integer.toString(rnd.nextInt(999999)));
        }
    }

    // show the capacity in the progress bar
    private void progressBarShow(int capacity, int totalCapacity) {
        double a = (capacity * 100 / totalCapacity);

        progressBar.setProgress(a / 100);


        percentLBL.setText(a + "%");
    }

    // refresh the table
    private void refresh() {
        tableView.getItems().clear();

        tableList();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
