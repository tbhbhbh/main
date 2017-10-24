package guitests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import javafx.application.HostServices;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.TestUtil;

public class ExportTest extends AddressBookGuiTest {
    @Override
    protected AddressBook getInitialData() {
        // return null to force test app to load data from file only
        return null;
    }

    @Override
    protected String getSavedExportLocation() {
        String filePath = TestUtil.getFilePathInSandboxFolder("contacts.vcf");
        deleteFileIfExists(filePath);
        return filePath;
    }

    /**
     * Deletes the file at {@code filePath} if it exists.
     */
    private void deleteFileIfExists(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void mainWindow_handleExportLocationExists_hostServicesExists() {
        File file = mainWindowHandle.handleExport(getSavedExportLocation());
        HostServices hostServices = mainWindowHandle.handleServices();
        String expectedFilePath = TestUtil.getFilePathInSandboxFolder("contacts.vcf");
        // TODO: assertTrue for showDocument
        //hostServices.showDocument(file.getAbsolutePath());

        assertTrue(hostServices != null);   // HostServices is active/present.
        assertTrue(FileUtil.isFileExists(file));    // contacts.vcf file exists.
        assertTrue(file.getPath().equals(expectedFilePath));    // file is stored in correct folder for testing.
    }
}
