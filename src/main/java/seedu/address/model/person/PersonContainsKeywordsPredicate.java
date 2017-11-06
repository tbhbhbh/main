//@@author tbhbhbh
package seedu.address.model.person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.parser.Parser;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;
import seedu.address.storage.AddressBookStorage;

/**
 * Tests that a {@code ReadOnlyPerson} matches any or all of the keywords given depending on the number of keywords.
 * Case 1: Given only 1 keyword, {@code ReadOnlyPerson}'s {@code Name} OR {@code Tag} must match the keyword
 * Case 2: Given >1 keywords, {@code ReadOnlyPerson}'s {@code Name} AND {@code Tag} must match the keywords
 * More to be added later including Birthday
 */
public class PersonContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<String> keywords;
    private List<String> keywordsLower;
    private String[] nameArr;
    private List<Tag> tagList = new ArrayList<>();
    private List<String> masterList = new ArrayList<>();

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
        keywordsLower = new ArrayList<>();
        setUpCaseInsensitiveKeywords();
    }

    @Override
    public boolean test(ReadOnlyPerson person) {

        clearMasterAndTagLists();
        setUpMasterList(person);

        if (keywords.size() == 1) {

            setUpTagList(); // prepares the keyword in tag form to compare with persons

            /* Case 1: keyword is a character (aka initial)
             * searches for people with names (Both first and last names) that start with the character
             */
            if (keywordIsSingleCharacter()) {
                return findNamesWithFirstChar(keywordsLower.toString().charAt(1));
            }

            /* Case 2: keyword can either be a name or a tag
             * searches for the keyword in the person's name or tags
             */
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword))
                    || tagList.stream().anyMatch(tag -> person.getTags().contains(tag));

        }

        /* Case 3: more than 1 keyword
         * Only supports 1 name, but multiple tags
         * a) name + tag
         * b) tag + tag + tag...
         */
        return masterList.containsAll(keywordsLower);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((PersonContainsKeywordsPredicate) other).keywords)); // state check
    }

    public boolean findNamesWithFirstChar(char firstChar) {
        return (nameStartsWith(firstChar));

    }

    public boolean keywordIsSingleCharacter() {
        if (keywords.toString().length() == 3) {
            return true;
        }
        return false;
    }

    public boolean nameStartsWith(char character) {
        for (String name : nameArr) {
            if (name.toLowerCase().charAt(0) == character) return true;
        }
        return false;
    }

    public void clearMasterAndTagLists() {
        masterList.clear();
        tagList.clear();
    }

    public void setUpCaseInsensitiveKeywords() {
        for (int i = 0; i < keywords.size(); i++) {
            keywordsLower.add(keywords.get(i).toLowerCase());
        }
    }

    void setUpTagList() {
        tagList.clear();
        try {
            tagList.add(new Tag(keywords.get(0)));
        } catch (IllegalValueException ive) {
            assert false : "target tag cannot exist";
        }
    }

    public void setUpMasterList(ReadOnlyPerson person) {

        tagList.addAll(person.getTags());
        for (int i = 0; i < tagList.size(); i++) {
            String tagNameToAdd = tagList.get(i).toString().substring(1,tagList.get(i).toString().length()-1);
            masterList.add(tagNameToAdd.toLowerCase());
        }

        nameArr = person.getName().toString().split("\\s+");
        for (int i = 0; i < nameArr.length; i++) {
            masterList.add(nameArr[i].toLowerCase());
        }
    }

}
