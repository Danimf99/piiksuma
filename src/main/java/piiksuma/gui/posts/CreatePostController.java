package piiksuma.gui.posts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import piiksuma.*;
import piiksuma.Utilities.PiikLogger;
import piiksuma.Utilities.PiikTextLimiter;
import piiksuma.api.ApiFacade;
import piiksuma.api.MultimediaType;
import piiksuma.exceptions.PiikDatabaseException;
import piiksuma.exceptions.PiikInvalidParameters;
import piiksuma.gui.ContextHandler;

import javax.activation.MimeTypeParseException;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatePostController implements Initializable {

    public JFXChipView<String> hashtagEditor;
    @FXML
    private JFXButton postButton;

    @FXML
    private JFXTextArea postText;

    @FXML
    private JFXButton multimediaButton;

    @FXML
    private ImageView boxImage;

    private URI imageURI;

    private Post post;

    private Post postFather;

    public CreatePostController(Post postFather) {
        this.postFather = postFather;
    }

    /**
     * Inits the window components
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postButton.setOnAction(this::publishPost);
        multimediaButton.setOnAction(this::handleMultimediaButton);
        post = new Post();
        post.setAuthor(ContextHandler.getContext().getCurrentUser());
        postText.textProperty().addListener((observable, oldValue, newValue) -> {
            post.setText(newValue);
            postButton.setDisable(!postText.validate());
        });
        PiikTextLimiter.addTextLimiter(postText, 256);
        // Checks if the input is empty
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Field required");
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .build());
        postText.getValidators().add(validator);
        hashtagEditor.requestFocus();
    }

    //GETTERS AND SETTERS
    public Post getPostFather() {
        return postFather;
    }

    public void setPostFather(Post postFather) {
        this.postFather = postFather;
    }

    /**
     * Function to be executed with a click on the multimedia button
     * @param event Event on the window
     */
    private void handleMultimediaButton(Event event) {
        // Creating window to choose image/video
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add multimedia");
        File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            return;
        }
        boxImage.setVisible(true);
        multimediaButton.setVisible(false);

        try {
            RandomAccessFile rndFile = new RandomAccessFile(file, "r");
            byte[] imgBytes = new byte[Math.toIntExact(rndFile.length())];
            rndFile.readFully(imgBytes);
            post.setMultimedia(new Multimedia());
            post.getMultimedia().setHash(
                    Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-512").digest(imgBytes))
            );

            post.getMultimedia().setUri(file.toURI().toString());
            post.getMultimedia().setType(MultimediaType.image);
            boxImage.setImage(new Image(post.getMultimedia().getUri()));
            post.getMultimedia().setResolution(String.valueOf((int) boxImage.getImage().getWidth()) + 'x'
                    + (int) boxImage.getImage().getHeight());
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to publish a post
     * @param event Event on the window
     */
    private void publishPost(Event event) {

        post.getHashtags().clear();
        hashtagEditor.getChips().stream().distinct().map(Hashtag::new).forEach(post::addHashtag);


        if (postFather != null) {
            post.setFatherPost(postFather);
        }
        // Post gets inserted in the database
        try {
            ApiFacade.getEntrypoint().getInsertionFacade().createPost(post, ContextHandler.getContext().getCurrentUser());
            ContextHandler.getContext().getFeedController().updateFeed();
            ContextHandler.getContext().getStage("Create Post").close();
        } catch (PiikDatabaseException | PiikInvalidParameters e) {
            e.showAlert(e, "Failure publishing the post");
        }

    }
}
