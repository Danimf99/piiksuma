package piiksuma.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InsertionFacadeTest extends FacadeTest {

    @Before
    public void setUp() throws Exception {
        oldUserDao = ApiFacade.getEntrypoint().getUserDao();
        oldPostDao = ApiFacade.getEntrypoint().getPostDao();
        oldInteractionDao = ApiFacade.getEntrypoint().getInteractionDao();
        oldMultimediaDao = ApiFacade.getEntrypoint().getMultimediaDao();
        oldMessagesDao = ApiFacade.getEntrypoint().getMessagesDao();

        ApiFacade.getEntrypoint().setInteractionDao(mockedInteractionDao);
        ApiFacade.getEntrypoint().setUserDao(mockedUserDao);
        ApiFacade.getEntrypoint().setPostDao(mockedPostDao);
        ApiFacade.getEntrypoint().setMultimediaDao(mockedMultimediaDao);
        ApiFacade.getEntrypoint().setMessagesDao(mockedMessagesDao);
    }

    @After
    public void tearDown() throws Exception {
        ApiFacade.getEntrypoint().setInteractionDao(oldInteractionDao);
        ApiFacade.getEntrypoint().setUserDao(oldUserDao);
        ApiFacade.getEntrypoint().setPostDao(oldPostDao);
        ApiFacade.getEntrypoint().setMultimediaDao(oldMultimediaDao);
        ApiFacade.getEntrypoint().setMessagesDao(oldMessagesDao);
    }

    @Test
    public void getConnection() {
    }

    @Test
    public void setConnection() {
    }

    @Test
    public void createUser() {
    }

    @Test
    public void createAchievement() {
    }

    @Test
    public void followUser() {
    }

    @Test
    public void addMultimedia() {
    }

    @Test
    public void createPost() {
    }

    @Test
    public void createHashtag() {
    }

    @Test
    public void archivePost() {
    }

    @Test
    public void newTicket() {
    }

    @Test
    public void replyTicket() {
    }

    @Test
    public void sendMessage() {
    }

    @Test
    public void createNotification() {
    }

    @Test
    public void notifyUser() {
    }

    @Test
    public void createEvent() {
    }
}