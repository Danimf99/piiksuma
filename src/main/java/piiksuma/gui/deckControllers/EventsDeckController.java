package piiksuma.gui.deckControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXHamburger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.event.Event;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import piiksuma.Utilities.PiikLogger;
import piiksuma.exceptions.PiikInvalidParameters;
import piiksuma.gui.ContextHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class EventsDeckController extends AbstractDeckController implements Initializable {

    @FXML
    private JFXButton mainButton;

    @FXML
    private JFXHamburger hamburguerButton;

    @FXML
    private JFXButton userButton;
    @FXML
    private JFXButton userDataButton;

    @FXML
    private JFXButton viewNotificationsButton;

    /**
     * Inits the window components
     * @param location
     * @param resources
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Common deck implementation
        super.setHamburguerButton(hamburguerButton);
        super.setUserProfileButton(userButton);
        super.setUserDataButton(userDataButton);
        super.setViewNotificationsButton(viewNotificationsButton);

        FontAwesomeIconView buttonIcon = new FontAwesomeIconView(FontAwesomeIcon.BOOKMARK);
        buttonIcon.getStyleClass().add("deck-button-graphic");
        mainButton.setGraphic(buttonIcon);

        mainButton.setOnAction(this::handleNewEvent);
    }

    /**
     * Function to open a window to create a new event
     *
     * @param event generated by the button
     */
    private void handleNewEvent(Event event) {
        // TODO: Create a new Post. Which should be another method that might be better placed in another class.

        // Requests the feed controller to update the feed for the new Post to show up
        Stage eventStage = new Stage();

        try {
            ContextHandler.getContext().register("createEvent", eventStage);
        } catch (PiikInvalidParameters e) {

            e.showAlert(e, "Failure updating the feed for the new post");
            return;
        }
        // Stage configuration

        eventStage.setTitle("Create Event");
        eventStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/createEvent.fxml"));
        JFXDecorator decorator;

        try {
            decorator = new JFXDecorator(eventStage, loader.load(), false, false, true);
        } catch (IOException e) {
            PiikLogger.getInstance().log(Level.SEVERE, "EventsDeckController -> handleNewEvent", e);
            return;
        }


        Scene scene = new Scene(decorator, 550, 450);


        scene.getStylesheets().add(
                getClass().getResource("/gui/css/global.css").toExternalForm()
        );
        eventStage.initModality(Modality.WINDOW_MODAL);
        eventStage.initOwner(ContextHandler.getContext().getStage("primary"));
        eventStage.setScene(scene);
        // Show and wait till it closes
        eventStage.showAndWait();
    }

}

