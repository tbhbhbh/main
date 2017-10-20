package seedu.address.logic.commands;

import static seedu.address.ui.CommandBox.DEFAULT_DISPLAY_PIC;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Address;
import com.google.api.services.people.v1.model.Birthday;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;

import javafx.application.Platform;
import javafx.concurrent.Task;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.CloseProgressEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.ShowProgressEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.DisplayPic;
import seedu.address.model.person.Email;
import seedu.address.model.person.Phone;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

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
    public static final String MESSAGE_INVALID_COMMAND = "Command entered is invalid.";
    public static final int ADDRESSBOOK_SIZE = 1000;
    private static int peopleAdded;
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
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
        Credential credential = null;
        try {
            credential = authorize();
            // Retrieve a list of Persons
            List<Person> connections = retrieveContacts(credential);
            // Import contacts into the application
            importContacts(connections);
        } catch (Exception e) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        return new CommandResult(MESSAGE_IN_PROGRESS);
    }


    /**
     * Creates an authorized {@code Credential} for the application to interact with
     * the Google People API
     */
    private static Credential authorize() throws IOException {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(ImportCommand.class.getResourceAsStream("/client_secrets.json")));

        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton("https://www.googleapis.com/auth/contacts.readonly")).build();

        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Retrieves a {@code List<Person>} of contacts from Google using the provided {@code Credential}
     */
    private static List<Person> retrieveContacts(Credential credential) {
        PeopleService peopleService =
                new PeopleService.Builder(httpTransport, JSON_FACTORY, credential).build();

        ListConnectionsResponse response = null;
        try {
            response = peopleService.people().connections().list("people/me")
                    .setPageSize(ADDRESSBOOK_SIZE)
                    .setPersonFields("names,emailAddresses,phoneNumbers")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert response != null;
        return response.getConnections();
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
                    seedu.address.model.person.Person toAdd = convertPerson(person);
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

    /**
     * Converts a {@code Person} from Google to a {@code seedu.address.model.person.Person}
     * Returns null if there is no name in the {@code Person}
     * Returns {@code seedu.address.model.person.Person} if there is at least a name
     */
    public seedu.address.model.person.Person convertPerson(Person person) throws IllegalValueException {
        String name;
        String phone;
        String email;
        String birthday;
        String address;

        List<Name> names = person.getNames();
        List<PhoneNumber> numbers = person.getPhoneNumbers();
        List<EmailAddress> emailAddresses = person.getEmailAddresses();
        List<Birthday> birthdays = person.getBirthdays();
        List<Address> addresses = person.getAddresses();

        // get first value for each list
        if (names != null && names.size() > 0) {
            name = names.get(0).getDisplayName();
            if (!seedu.address.model.person.Name.isValidName(name)) {
                return null;
            }
        } else {
            return null;
        }

        if (numbers != null && numbers.size() > 0) {
            phone = numbers.get(0).getCanonicalForm().replace("+", "");
        } else {
            phone = "";
        }
        if (emailAddresses != null && emailAddresses.size() > 0) {
            email = emailAddresses.get(0).getValue();
        } else {
            email = "";
        }
        if (birthdays != null && birthdays.size() > 0) {
            birthday = birthdays.get(0).getText();
        } else {
            birthday = "";
        }
        if (addresses != null && addresses.size() > 0) {
            address = addresses.get(0).getFormattedValue();
        } else {
            address = "";
        }
        Set<Tag> defaultTags = SampleDataUtil.getTagSet("Google");

        seedu.address.model.person.Name nameAdd = new seedu.address.model.person.Name(name);
        seedu.address.model.person.Person toAdd;
        toAdd = new seedu.address.model.person.Person(nameAdd,
                new Phone(phone), new Email(email), new seedu.address.model.person.Address(address),
                new seedu.address.model.person.Birthday(birthday), new DisplayPic(DEFAULT_DISPLAY_PIC), defaultTags);
        return toAdd;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && service.equals(((ImportCommand) other).service));
    }

}
