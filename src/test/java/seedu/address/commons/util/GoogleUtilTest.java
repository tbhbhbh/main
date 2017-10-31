//@@author danielbrzn
package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.api.services.people.v1.model.Person;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.testutil.GooglePersonBuilder;
import seedu.address.testutil.PersonBuilder;

public class GoogleUtilTest {

    @Test
    public void convertFromGooglePerson_success() throws IllegalValueException {
        Person typicalPerson = new GooglePersonBuilder().build();
        seedu.address.model.person.Person typicalAddressBookPerson = new PersonBuilder().withTags("Google")
                .withTwitter("").withInstagram("").build();
        assertEquals(typicalAddressBookPerson, GoogleUtil.convertPerson(typicalPerson));

    }
}
