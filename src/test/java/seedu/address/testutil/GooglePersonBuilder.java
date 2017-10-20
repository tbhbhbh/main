package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.PhoneNumber;


/**
 * A utility class to help with building Google Person objects.
 */
public class GooglePersonBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_BIRTHDAY = "03/07/1990";
    public static final String DEFAULT_TAGS = "friends";

    private com.google.api.services.people.v1.model.Person person;


    public GooglePersonBuilder() {

        List names = new ArrayList<>();
        List phone = new ArrayList<>();
        List emails = new ArrayList<>();
        List addresses = new ArrayList<>();
        List birthday = new ArrayList<>();

        names.add(new com.google.api.services.people.v1.model.Name().setDisplayName(DEFAULT_NAME));
        phone.add(new PhoneNumber().setCanonicalForm(DEFAULT_PHONE));
        emails.add(new EmailAddress().setValue(DEFAULT_EMAIL));
        addresses.add(new com.google.api.services.people.v1.model.Address().setFormattedValue(DEFAULT_ADDRESS));
        birthday.add(new com.google.api.services.people.v1.model.Birthday().setText(DEFAULT_BIRTHDAY));

        this.person = new com.google.api.services.people.v1.model.Person().setNames(names).setAddresses(addresses)
                .setEmailAddresses(emails).setPhoneNumbers(phone).setBirthdays(birthday);

    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public GooglePersonBuilder withName(String name) {
        List names = new ArrayList<>();
        names.add(new com.google.api.services.people.v1.model.Name().setDisplayName(name));
        this.person.setNames(names);

        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public GooglePersonBuilder withAddress(String address) {
        List addresses = new ArrayList<>();
        addresses.add(new com.google.api.services.people.v1.model.Address().setFormattedValue(address));
        this.person.setAddresses(addresses);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public GooglePersonBuilder withPhone(String phone) {
        List phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new PhoneNumber().setValue(phone));
        this.person.setPhoneNumbers(phoneNumbers);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public GooglePersonBuilder withEmail(String email) {
        List emails = new ArrayList<>();
        emails.add(new EmailAddress().setValue(email));
        this.person.setEmailAddresses(emails);
        return this;
    }

    /**
     * Sets the {@code Birthday} of the {@code Person} that we are building.
     */
    public GooglePersonBuilder withBirthday(String birthday) {
        List birthdays = new ArrayList<>();
        birthdays.add(new com.google.api.services.people.v1.model.Birthday().setText(birthday));
        this.person.setBirthdays(birthdays);

        return this;
    }

    public com.google.api.services.people.v1.model.Person build() {
        return this.person;
    }

}
