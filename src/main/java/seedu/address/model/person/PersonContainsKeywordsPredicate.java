//@@author tbhbhbh
package seedu.address.model.person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code ReadOnlyPerson} matches any or all of the keywords given depending on the number of keywords.
 * Case 1: Given only 1 keyword, {@code ReadOnlyPerson}'s {@code Name} OR {@code Tag} must match the keyword
 * Case 2: Given >1 keywords, {@code ReadOnlyPerson}'s {@code Name} AND {@code Tag} must match the keywords
 * More to be added later including Birthday
 */
public class PersonContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<String> keywords;
    private List<String> personMasterList;

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        List<Tag> tagList = new ArrayList<>();

        this.personMasterList = createListFromPerson(person);


        for (int i = 0; i < keywords.size(); i++) {
            try {
                System.out.println("i: " + i + " kw: " + keywords.get(i));
                tagList.add(new Tag(keywords.get(i)));
            } catch (IllegalValueException ive) {
                assert false : "The target tag is invalid";
            }
        }

        if (keywords.size() == 1) {
            //if (keywords.get(0).)
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword))
                    || person.getTags().contains(tagList.get(0));
            // tagList.stream().anyMatch((tag -> person.getTags().contains(tag)));
        }

        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword))
                && tagList.stream().anyMatch((tag -> person.getTags().contains(tag)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((PersonContainsKeywordsPredicate) other).keywords)); // state check
    }

    public List<String> createListFromPerson(ReadOnlyPerson person) {
        List<String> masterList = person.getTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.toList());

        

    }

}
