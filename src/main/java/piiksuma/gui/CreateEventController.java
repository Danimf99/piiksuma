package piiksuma.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import piiksuma.Utilities.PiikLogger;
import piiksuma.Utilities.PiikTextLimiter;
import piiksuma.api.ApiFacade;
import piiksuma.exceptions.PiikException;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class CreateEventController implements Initializable {
    @FXML
    private JFXButton newEvent;
    @FXML
    private JFXTextField eventName;
    @FXML
    private JFXTextField location;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXTextArea description;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Limit text fields
        PiikTextLimiter.addTextLimiter(eventName, 50);
        PiikTextLimiter.addTextLimiter(this.location, 50);
        PiikTextLimiter.addTextLimiter(description, 200);

        newEvent.setOnAction(this::handleNew);
        date.setValidators(new RequiredFieldValidator());
        RequiredFieldValidator eventNameValidator = new RequiredFieldValidator();
        eventNameValidator.setMessage("Field required");
        eventNameValidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .build());
        eventName.setValidators(eventNameValidator);
        eventName.textProperty().addListener((observable, oldValue, newValue) -> {
            newEvent.setDisable(!eventName.validate());
        });
    }

    /**
     * Code to the new event button
     *
     * @param event
     */
    private void handleNew(Event event) {
        piiksuma.Event newEvent = new piiksuma.Event();

        if (!checkFields()) {
            Alert alert = new Alert(ContextHandler.getContext().getStage("createEvent"));
            alert.setHeading("Fields empty!");
            alert.addText("Fields cannot be empty");
            alert.addCloseButton();
            alert.show();
            return;
        }

        newEvent.setCreator(ContextHandler.getContext().getCurrentUser());
        newEvent.setDescription(description.getText());
        newEvent.setLocation(location.getText());
        newEvent.setName(eventName.getText());

        if (date.validate()) {
            newEvent.setDate(Timestamp.valueOf(date.getValue().atStartOfDay()));
        }


        try {
            newEvent = ApiFacade.getEntrypoint().getInsertionFacade().createEvent(newEvent, ContextHandler.getContext().getCurrentUser());
            ContextHandler.getContext().getEventsController().updateEventFeed();
        } catch (PiikException e) {
            PiikLogger.getInstance().log(Level.SEVERE, "CreateEventController -> handleNew", e);
            Alert.newAlert().setHeading("Create event error").addText("Failure creating the new event").addCloseButton().show();
            return;
        }

        ContextHandler.getContext().getStage("createEvent").close();
    }

    /**
     * Function to check if the fields are correctly fill
     *
     * @return TRUE if the fields are correctly fill, FALSE in the other case
     */
    private boolean checkFields() {
        if (eventName.getText().isEmpty() || description.getText().isEmpty() || date.validate() || location.getText().isEmpty()) {
            return false;
        }
        return true;
    }
}
