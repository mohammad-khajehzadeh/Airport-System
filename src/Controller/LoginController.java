package Controller;

import Model.PassengerRepository;
import Model.UserRepository;
import Model.employee;
import Model.passenger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class LoginController implements Initializable {


    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button signInBTN;
    @FXML
    ImageView welcomeImage;
    @FXML
    ImageView googleIcon;
    @FXML
    ImageView facebookIcon;
    @FXML
    ImageView ivaoIcon;
    @FXML
    Label passForgotLBL;
    @FXML
    Label signUpLBL;


    public void signInBTN() {

        PassengerRepository passengerRepository = new PassengerRepository();

        List<passenger> passengerList = passengerRepository.passengerList();
        usernameField.setText("khz");
        passwordField.setText("khz");

        for (int i = 0; i < passengerList.size(); i++) {
//خب کجاش مشکل داره؟
//            بزرا الان برات بنویسمش دوباره

            //check the passenger list.....
            if (passengerList.get(i).getUsername().equals(usernameField.getText()) && passengerList.get(i).getPassword().equals(passwordField.getText())) {
//                System.out.println(passengerList.get(i).getID());

                ((Stage) signInBTN.getScene().getWindow()).close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Passenger.fxml"));//
                try {
                    loader.load();
                } catch (IOException e) {
                    System.out.println("theres problem to load paasenger view \n" + e);
                }

                PassengerController passengerController = loader.getController();
                passengerController.setID(passengerList.get(i).getID());
//


                Stage stage = new Stage();
                stage.setScene(new Scene(loader.getRoot()));
                stage.setResizable(false);
                stage.show();
                break;
            }
            //
            else if (i == passengerList.size() - 1) {

                UserRepository userRepository = new UserRepository();
                List<employee> employeeList = userRepository.employer();

                for (int j = 0; j < employeeList.size(); j++) {

                    if (employeeList.get(i).getUsername().equals(usernameField.getText()) && employeeList.get(i).getPassword().equals(passwordField.getText())) {

                        System.out.println(employeeList.get(i).getJob());

                        break;
                    } else if (j == employeeList.size() - 1) {

                        usernameField.setStyle("-fx-border-width: 0 0 0 3 ; -fx-border-color: red ; -fx-background-color:  #c2c2c2 ; -fx-text-inner-color: black");
                        passwordField.setStyle("-fx-border-width: 0 0 0 3; -fx-border-color: red ; -fx-background-color:  #c2c2c2 ; -fx-text-inner-color: black");
                        passForgotLBL.setTextFill(Color.RED);

                    }
                }


            }
        }


    }

    public void enterSignInBTN() {
        signInBTN.setText("");
        signInBTN.setStyle("-fx-background-color: green");
        signInBTN.setGraphic(new ImageView("file:Icons/login btn .png"));
    }

    public void exiteSignInBTN() {
        signInBTN.setStyle("-fx-background-color:  #212121");
        signInBTN.setText("sign in");
        signInBTN.setGraphic(null);
    }

    public void googleIconClicked() {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            //specify the protocol along with the URL
            java.net.URI oURL = new URI(
                    "https://www.google.com");
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException d) {
            // TODO Auto-generated catch block
            d.printStackTrace();
        }
    }

    public void facebookIconClickec() {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            //specify the protocol along with the URL
            java.net.URI oURL = new URI(
                    "https://facebook.com");
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException d) {
            // TODO Auto-generated catch block
            d.printStackTrace();
        }
    }

    public void ivaoIconClickec() {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            //specify the protocol along with the URL
            java.net.URI oURL = new URI(
                    "https://ivao.aero/Login.aspx");
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException d) {
            // TODO Auto-generated catch block
            d.printStackTrace();
        }
    }

    public void signUpLBLClick() {

        ((Stage) signInBTN.getScene().getWindow()).close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SignUp.fxml"));//
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("theres problem to load paasenger view \n" + e);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getRoot()));
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:Icons/signUp.png"));
        stage.setTitle("new passenger...");
        stage.show();

        System.out.println("not yet");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeImage.setImage(new Image("file:Icons/welcome.png"));
        googleIcon.setImage(new Image("file:Icons/google.png"));
        facebookIcon.setImage(new Image("file:Icons/facebook.png"));
        ivaoIcon.setImage(new Image("file:Icons/aaaaa.png"));
    }

}
