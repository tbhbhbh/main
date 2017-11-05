//@@author danielbrzn
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to show the location of a Person.
 */
public class ShowLocationEvent extends BaseEvent {

    private final String googleMapsUrl;

    public ShowLocationEvent(String mapsUrl) {
        this.googleMapsUrl = mapsUrl;
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
