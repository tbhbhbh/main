package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.SocialRequestEvent;
import seedu.address.model.person.Name;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.UserName;

/**
 * Accesses the person's social media profile on the browser.
 */

public class SocialCommand extends Command {
    public static final String COMMAND_WORD = "social";
    public static final String MESSAGE_SOCIAL_FAILED = "Chosen social media not supported";
    public static final String MESSAGE_IF_MISSING = "(Check the person's username fields if his/her page does not "
            + "exist)";
    public static final String MESSAGE_INSTAGRAM = "'s Instagram profile\n";
    public static final String MESSAGE_TWITTER = "'s Twitter profile\n";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Opens up the target index's Twitter or Instagram "
            + "profile.\n"
            + "Parameters: INDEX CHOSEN_SOCIAL_MEDIA\n"
            + "CHOSEN_SOCIAL_MEDIA: ig or tw\n"
            + "Example: " + COMMAND_WORD + " 1 ig";

    private static final String INSTAGRAM_URL_PREFIX = "https://instagram.com/";
    private static final String TWITTER_URL_PREFIX = "https://twitter.com/";

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
    public CommandResult execute() {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
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
            return new CommandResult (String.format(MESSAGE_SOCIAL_FAILED, socialMedia));
        }

        EventsCenter.getInstance().post(new SocialRequestEvent(userName, url));
        return new CommandResult(String.format(messageSocialSuccess, realName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SocialCommand // instanceof handles nulls
                && this.index.equals(((SocialCommand) other).index)); // state check
    }

    public boolean isInstagramName() {
        return this.socialMedia.equals("ig");
    }

    public boolean isTwitterName() {
        return this.socialMedia.equals("tw");
    }
}
