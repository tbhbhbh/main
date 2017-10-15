package seedu.address.logic.commands;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
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

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Phone;
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

    public static final String MESSAGE_SUCCESS = "%1$s contacts imported";
    public static final String MESSAGE_INVALID_COMMAND = "Command entered is invalid.";
    public static final int ADDRESSBOOK_SIZE = 1000;

    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String service;

    /**
     * Creates an ImportCommand to add contacts from the specified service
     */
    public ImportCommand(String service) {
        this.service = service;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {

        ArrayList<String> failedToAdd = new ArrayList<>();
        int count = 0;

        try {
            //setUp();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // authorization
            Credential credential = authorize();
            PeopleService peopleService =
                    new PeopleService.Builder(httpTransport, JSON_FACTORY, credential).build();
            ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                    .setPageSize(ADDRESSBOOK_SIZE)
                    .setPersonFields("names,emailAddresses,phoneNumbers")
                    .execute();
            List<Person> connections = response.getConnections();

            String name;
            String phone;
            String email;
            String address;
            String birthday;

            for (Person person : connections) {
                List<Name> names = person.getNames();
                List<PhoneNumber> numbers = person.getPhoneNumbers();
                List<EmailAddress> emailAddresses = person.getEmailAddresses();
                List<Birthday> birthdays = person.getBirthdays();
                List<Address> addresses = person.getAddresses();

                // get first value for each list
                if (names != null && names.size() > 0) {
                    name = names.get(0).getDisplayName();
                    if (!seedu.address.model.person.Name.isValidName(name)) {
                        failedToAdd.add(name);
                        continue;
                    }
                } else {
                    continue;
                }

                if (numbers != null && numbers.size() > 0) {
                    phone = numbers.get(0).getCanonicalForm().replace("+", "");
                } else {
                    phone = "91234567";
                }
                if (emailAddresses != null && emailAddresses.size() > 0) {
                    email = emailAddresses.get(0).getValue();
                    System.out.println(email);
                } else {
                    email = "test@gmail.com";
                }
                if (birthdays != null && birthdays.size() > 0) {
                    birthday = birthdays.get(0).getText();
                } else {
                    birthday = "08/11/1995";
                }
                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0).getFormattedValue();
                } else {
                    address = "160, Bishan";
                }
                Set<Tag> defaultTags = SampleDataUtil.getTagSet("Google");

                seedu.address.model.person.Name nameAdd = new seedu.address.model.person.Name(name);
                seedu.address.model.person.Person toAdd;
                toAdd = new seedu.address.model.person.Person(nameAdd,
                        new Phone(phone), new Email(email), new seedu.address.model.person.Address(address),
                        new seedu.address.model.person.Birthday(birthday), defaultTags);
                model.addPerson(toAdd);
                count++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String s: failedToAdd) {
            System.out.println(s);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, count));
    }

    /**
     * Creates a credential for the application to interact with the Google People API
     */
    private static Credential authorize() throws Exception {
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


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && service.equals(((ImportCommand) other).service));
    }

}
