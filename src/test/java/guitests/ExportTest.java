package guitests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import javafx.application.HostServices;
import seedu.address.TestApp;
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
    public void addressBook_handleExportLocationExists() throws IOException {
        String filePath = getSavedExportLocation();
        File file = new File(filePath);
        // Creates a dummy contacts.vcf file that contains no data if it does not exist
        try {
            FileUtil.createIfMissing(file);
        } catch (IOException e) {
            assert false : "File path is a valid path";
        }
        if (FileUtil.isFileExists(file)) {
            HostServices hostServices = TestApp.getAppHostServices();
            // TODO: assertTrue for showDocument
            //hostServices.showDocument(file.getAbsolutePath());
        }
        // Asserts file exists && correct file path.
        assertTrue(FileUtil.isFileExists(file));
        assertTrue(file.getPath().equals(TestUtil.getFilePathInSandboxFolder("contacts.vcf")));
    }
}
