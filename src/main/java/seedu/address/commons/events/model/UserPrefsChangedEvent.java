package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.UserPrefs;

public class UserPrefsChangedEvent extends BaseEvent {

    public final UserPrefs data;

    public UserPrefsChangedEvent(UserPrefs data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of aliases " + data.getAliasMap().size();
    }
}
