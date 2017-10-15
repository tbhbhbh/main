package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to view the authentication page.
 */
public class ShowOAuthPageEvent extends BaseEvent {

    public final String authorizationUrl;

    public ShowOAuthPageEvent(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
