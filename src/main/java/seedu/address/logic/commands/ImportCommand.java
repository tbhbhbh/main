package seedu.address.logic.commands;

import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.people.v1.model.Person;

import javafx.application.Platform;
import javafx.concurrent.Task;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.CloseProgressEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.ShowProgressEvent;
import seedu.address.commons.util.GoogleUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Imports contacts from the user's specified online service
 */
public class ImportCommand extends UndoableCommand {


    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports contacts from a specified online service. "
            + "Parameters: "
            + "SERVICE_NAME "
            + "Example: " + COMMAND_WORD + " "
            + "google ";

    public static final String MESSAGE_SUCCESS = "%1$s contacts imported. %2$s contacts failed to import.";
    public static final String MESSAGE_IN_PROGRESS = "Importing in progress";
    public static final String MESSAGE_FAILURE = "Failed to import contacts.";
    public static final String MESSAGE_CONNECTION_FAILURE = "Failed to access Google. Check your internet connection or"
            + " try again in a few minutes.";
    public static final int ADDRESSBOOK_SIZE = 1000;
    private static int peopleAdded;
    private static Credential credential;
    private static HttpTransport httpTransport;
    private final String service;

    /**
     * Creates an ImportCommand to add contacts from the specified service
     */
    public ImportCommand(String service) {
        this.service = service.toLowerCase();
        peopleAdded = 0;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        // authorization with Google
        try {
            // Check for connectivity to Google
            if (!GoogleUtil.isReachable()) {
                return new CommandResult(MESSAGE_CONNECTION_FAILURE);
            }
            Thread thread = new Thread(() -> {
                try {
                    credential = GoogleUtil.authorize(httpTransport);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            thread.join(30000);

            // Retrieve a list of Persons
            List<Person> connections = GoogleUtil.retrieveContacts(credential, httpTransport);
            // Import contacts into the application
            importContacts(connections);
        } catch (Exception e) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        return new CommandResult(MESSAGE_IN_PROGRESS);
    }


    /**
     * Imports contacts into the application using the given {@code List<Person>}
     */
    public void importContacts(List<Person> connections) {


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int amountToAdd = connections.size();
                for (Person person : connections) {
                    seedu.address.model.person.Person toAdd = GoogleUtil.convertPerson(person);
                    if (toAdd == null) {
                        continue;
                    }
                    try {
                        model.addPerson(toAdd);
                    } catch (DuplicatePersonException e) {
                        e.printStackTrace();
                    }
                    peopleAdded++;
                    Platform.runLater(() -> {
                        updateProgress(peopleAdded, amountToAdd);

                    });


                }
                return null;
            }
        };

        task.setOnSucceeded(t -> {
            EventsCenter.getInstance().post(new CloseProgressEvent());
            EventsCenter.getInstance().post(new NewResultAvailableEvent(
                    String.format(MESSAGE_SUCCESS, peopleAdded, connections.size() - peopleAdded), false));
        });

        task.setOnFailed(t -> EventsCenter.getInstance().post(
                new NewResultAvailableEvent(String.format(MESSAGE_FAILURE), true)));

        EventsCenter.getInstance().post(new ShowProgressEvent(task.progressProperty()));
        Thread importThread = new Thread(task);
        importThread.start();
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && service.equals(((ImportCommand) other).service));
    }

}
