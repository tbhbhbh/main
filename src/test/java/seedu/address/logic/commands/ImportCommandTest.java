package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ImportCommandTest {
    @Test
    public void equals() {
        ImportCommand importGoogleCommand = new ImportCommand("Google");
        ImportCommand importAppleCommand = new ImportCommand("Apple");

        // same object -> returns true
        assertTrue(importGoogleCommand.equals(importGoogleCommand));

        // same values -> returns true;
        ImportCommand importGoogleCommandCopy = new ImportCommand("Google");
        assertTrue(importGoogleCommand.equals(importGoogleCommandCopy));

        // same values but different case -> returns true;
        ImportCommand importGoogleCommandCase = new ImportCommand("GoOgLe");
        assertTrue(importGoogleCommand.equals(importGoogleCommandCase));

        // different types -> returns false
        assertFalse(importGoogleCommand.equals(1));

        // null -> returns false
        assertFalse(importGoogleCommand.equals(null));

        // different commands -> returns false
        assertFalse(importGoogleCommand.equals(importAppleCommand));
    }
}
