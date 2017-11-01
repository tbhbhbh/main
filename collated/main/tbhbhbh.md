# tbhbhbh
###### \java\seedu\address\commons\events\ui\SocialRequestEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.UserName;

/**
 * Represents a change in the browser to view the person's social media profile, whether it is Facebook/Instagram.
 */
public class SocialRequestEvent extends BaseEvent {

    private final UserName userName;
    private final String socialMediaLink;

    public SocialRequestEvent(UserName userName, String socialMediaLink) {
        this.userName = userName;
        this.socialMediaLink = socialMediaLink;
    }

    public UserName getUserName() {
        return userName;
    }

    public String getSocialMediaLink() {
        return socialMediaLink;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\logic\commands\SearchCommand.java
``` java
package seedu.address.logic.commands;

import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches for all persons whose information contain "
            + "any of the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice colleagues";

    private final PersonContainsKeywordsPredicate predicate;

    public SearchCommand(PersonContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SearchCommand // instanceof handles nulls
                && this.predicate.equals(((SearchCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\SocialCommand.java
``` java
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.SocialRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Name;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.UserName;

/**
 * Accesses the person's social media profile on the browser.
 */

public class SocialCommand extends Command {
    public static final String COMMAND_WORD = "social";
    public static final String MESSAGE_SOCIAL_EMPTY = " does not have chosen social media: ";
    public static final String MESSAGE_SOCIAL_UNSUPPORTED = "Chosen social media not supported: ";
    public static final String MESSAGE_IF_MISSING = "(Check the person's username fields if his/her page does not "
            + "exist)";
    public static final String MESSAGE_INSTAGRAM = "'s Instagram profile\n";
    public static final String MESSAGE_TWITTER = "'s Twitter profile\n";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Opens up the target index's Twitter or Instagram "
            + "profile.\n"
            + "Parameters: INDEX CHOSEN_SOCIAL_MEDIA\n"
            + "CHOSEN_SOCIAL_MEDIA: ig or tw\n"
            + "Example: " + COMMAND_WORD + " 1 ig";

    public static final String INSTAGRAM_URL_PREFIX = "https://instagram.com/";
    public static final String TWITTER_URL_PREFIX = "https://twitter.com/";

    private final Index index;
    private final String socialMedia;

    private String messageSocialSuccess = "Successfully loaded %1$s";
    private String url;
    private UserName userName;
    private Name realName;

    public SocialCommand(Index index, String socialMedia) {
        this.index = index;
        this.socialMedia = socialMedia;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + ": "
                    + index.getOneBased());
        }

        realName = lastShownList.get(index.getZeroBased()).getName();

        if (this.isInstagramName()) {
            userName = lastShownList.get(index.getZeroBased()).getInstagramName();
            url = INSTAGRAM_URL_PREFIX;
            messageSocialSuccess = messageSocialSuccess + MESSAGE_INSTAGRAM + MESSAGE_IF_MISSING;
        } else if (this.isTwitterName()) {
            userName = lastShownList.get(index.getZeroBased()).getTwitterName();
            url = TWITTER_URL_PREFIX;
            messageSocialSuccess = messageSocialSuccess + MESSAGE_TWITTER + MESSAGE_IF_MISSING;
        } else {
            throw new CommandException(MESSAGE_SOCIAL_UNSUPPORTED
                    + socialMedia);
        }

        if (url.equals(TWITTER_URL_PREFIX + userName) || url.equals(INSTAGRAM_URL_PREFIX + userName)) {
            throw new CommandException(realName + MESSAGE_SOCIAL_EMPTY + convertToActualName(socialMedia));
        }
        EventsCenter.getInstance().post(new SocialRequestEvent(userName, url));
        return new CommandResult(String.format(messageSocialSuccess, realName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SocialCommand // instanceof handles nulls
                && this.socialMedia.equals(((SocialCommand) other).socialMedia)
                && this.index.equals(((SocialCommand) other).index)); // state check
    }

    public String getMessageSocialSuccess() {
        return this.messageSocialSuccess;
    }

    public boolean isInstagramName() {
        return this.socialMedia.equals("ig");
    }

    public boolean isTwitterName() {
        return this.socialMedia.equals("tw");
    }

    public String convertToActualName(String socialMedia) {
        return (socialMedia.equals("tw") ? "Twitter" : "Instagram");
    }

}
```
###### \java\seedu\address\logic\commands\TagDeleteCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Deletes a tag identified using its tag name from the address book.
 */
public class TagDeleteCommand extends UndoableCommand {

    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted tag: %1$s";
    public static final String COMMAND_WORD = "tagdelete";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the tag identified by the tag name in the address book.\n"
            + "Parameters: TAGNAME\n"
            + "Example: " + COMMAND_WORD + " friends";

    private final String tagName;
    private Tag tagToDelete;

    public TagDeleteCommand(String tagName) {
        requireNonNull(tagName);
        this.tagName = tagName.trim();

    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<Tag> tagList = model.getAddressBook().getTagList();

        try {
            tagToDelete = new Tag(tagName);
            if (!tagList.contains(tagToDelete)) {
                throw new CommandException(Messages.MESSAGE_INVALID_TAG_NAME);
            }
            model.deleteTag(tagToDelete);
        } catch (IllegalValueException ive) {
            assert false : "The target tag is invalid";
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, tagName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagDeleteCommand // instanceof handles nulls
                && this.tagName.equals(((TagDeleteCommand) other).tagName)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\TagEditCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.tag.Tag.MESSAGE_TAG_CONSTRAINTS;
import static seedu.address.model.tag.Tag.isValidTagName;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Edits a tag identified using its tag name from the address book and
 * replaces it with a new tag name provided by the user.
 */
public class TagEditCommand extends UndoableCommand {
    public static final String MESSAGE_EDIT_TAG_SUCCESS = "Edited tag: %1$s to %2$s";
    public static final String COMMAND_WORD = "tagedit";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the tag identified by the tag name in the address book to a new tag name provided by the user.\n"
            + "Parameters: TAGNAME NEWTAGNAME\n"
            + "Example: " + COMMAND_WORD + " groupmates " + "friends";

    private final String oldTagName;
    private final String newTagName;
    private Tag tagToEdit;
    private Tag newTag;

    public TagEditCommand(String oldTagName, String newTagName) {
        requireNonNull(oldTagName);
        requireNonNull(newTagName);

        this.oldTagName = oldTagName.trim();
        this.newTagName = newTagName.trim();
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<Tag> tagList = model.getAddressBook().getTagList();

        try {
            newTag = new Tag(newTagName);
            tagToEdit = new Tag(oldTagName);
            if (!isValidTagName(newTag.tagName)) {
                throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
            }
            if (newTag.tagName.isEmpty() || tagToEdit.tagName.isEmpty()) {
                throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
            }
            if (!tagList.contains(tagToEdit)) {
                throw new CommandException(Messages.MESSAGE_UNKNOWN_TAG_NAME);
            }
            model.editTag(tagToEdit, newTag);
        } catch (IllegalValueException ive) {
            assert false : "The provided tag does not exist";
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        }

        return new CommandResult(
                String.format(
                        MESSAGE_EDIT_TAG_SUCCESS,
                        tagToEdit.toString(),
                        newTag.toString()
                )
        );
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagEditCommand // instanceof handles nulls
                && this.oldTagName.equals(((TagEditCommand) other).oldTagName)); // state check
    }
}
```
###### \java\seedu\address\logic\parser\SearchCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new SearchCommand object
 */
public class SearchCommandParser implements Parser<SearchCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SearchCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new SearchCommand(new PersonContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
```
###### \java\seedu\address\logic\parser\SocialCommandParser.java
``` java
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SocialCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SocialCommand object
 */
public class SocialCommandParser implements Parser<SocialCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the SocialCommand
     * and returns an SocialCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SocialCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String[] argsArr = args.trim().split(" ");
        Index index;
        String socialMedia;

        if (argsArr.length == 1 || argsArr.length == 0) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SocialCommand.MESSAGE_USAGE));
        }

        try {
            index = ParserUtil.parseIndex(argsArr[0]);
            socialMedia = argsArr[1];

        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SocialCommand.MESSAGE_USAGE));
        }
        return new SocialCommand(index, socialMedia);
    }
}
```
###### \java\seedu\address\logic\parser\TagDeleteCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.TagDeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new TagDeleteCommand object
 */
public class TagDeleteCommandParser implements Parser<TagDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagDeleteCommand
     * and returns a TagDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagDeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagDeleteCommand.MESSAGE_USAGE));
        }

        return new TagDeleteCommand(trimmedArgs);
    }

}
```
###### \java\seedu\address\logic\parser\TagEditCommandParser.java
``` java
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.tag.Tag.MESSAGE_TAG_CONSTRAINTS;
import static seedu.address.model.tag.Tag.isValidTagName;

import seedu.address.logic.commands.TagEditCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new TagEditCommand object
 */
public class TagEditCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the TagEditCommand
     * and returns a TagEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagEditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String[] stringArr = args.split(" ");

        if (stringArr.length < 3) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagEditCommand.MESSAGE_USAGE));
        }
        if (!isValidTagName(stringArr[1]) || !isValidTagName(stringArr[2])) {
            throw new ParseException(MESSAGE_TAG_CONSTRAINTS);
        }

        String oldTag = stringArr[1].trim();
        String newTag = stringArr[2].trim();

        return new TagEditCommand(oldTag, newTag);
    }
}
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void deleteTag(Tag tag) throws PersonNotFoundException, DuplicatePersonException {
        for (int i = 0; i < addressBook.getPersonList().size(); i++) {
            ReadOnlyPerson oldPerson = addressBook.getPersonList().get(i);

            Person newPerson = new Person(oldPerson);
            Set<Tag> newTags = new HashSet<>(newPerson.getTags());
            newTags.remove(tag);
            newPerson.setTags(newTags);

            addressBook.updatePerson(oldPerson, newPerson);
        }
    }

    @Override
    public void editTag(Tag oldTag, Tag newTag) throws PersonNotFoundException, DuplicatePersonException {
        for (int i = 0; i < addressBook.getPersonList().size(); i++) {
            ReadOnlyPerson oldPerson = addressBook.getPersonList().get(i);
            if (oldPerson.getTags().contains(oldTag)) {
                Person newPerson = new Person(oldPerson);
                Set<Tag> newTags = new HashSet<>(newPerson.getTags());
                newTags.remove(oldTag);
                newTags.add(newTag);
                newPerson.setTags(newTags);

                addressBook.updatePerson(oldPerson, newPerson);
            }
        }
    }
```
###### \java\seedu\address\model\person\PersonContainsKeywordsPredicate.java
``` java
package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code ReadOnlyPerson} matches any or all of the keywords given depending on the number of keywords.
 * Case 1: Given only 1 keyword, {@code ReadOnlyPerson}'s {@code Name} OR {@code Tag} must match the keyword
 * Case 2: Given >1 keywords, {@code ReadOnlyPerson}'s {@code Name} AND {@code Tag} must match the keywords
 * More to be added later including Birthday, Address
 */
public class PersonContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<String> keywords;

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < keywords.size(); i++) {
            try {
                tagList.add(new Tag(keywords.get(i)));
            } catch (IllegalValueException ive) {
                assert false : "The target tag is invalid";
            }
        }

        if (keywords.size() == 1) {
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword))
                    || tagList.stream().anyMatch((tag -> person.getTags().contains(tag)));
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

}
```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * Sorts AddressBook by name.
     */
    public void sortByName() {
        FXCollections.sort(internalList, comparator);
    }
```
