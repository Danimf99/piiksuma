package piiksuma.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class OtherUserProfileController implements Initializable {

    @FXML
    private Text userName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        ContextHandler.getContext().getCurrentUser().setName("OswaldOswin");
       // userName.setText(ContextHandler.getContext().getCurrentUser().getName());
    }
}
