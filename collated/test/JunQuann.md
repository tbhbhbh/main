# JunQuann
###### /java/guitests/guihandles/PersonCardHandle.java
``` java
/**
 * Provides a handle to a person card in the person list panel.
 */
public class PersonCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String PHONE_FIELD_ID = "#phone";
    private static final String TAGS_FIELD_ID = "#tags";
    private static final String DP_FIELD_ID = "#displayPic";

    private final Label idLabel;
    private final Label nameLabel;
    private final Label phoneLabel;
    private final List<Label> tagLabels;
    private final Circle displayPicPanel;

    public PersonCardHandle(Node cardNode) {
        super(cardNode);

        this.idLabel = getChildNode(ID_FIELD_ID);
        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.phoneLabel = getChildNode(PHONE_FIELD_ID);
        this.displayPicPanel = getChildNode(DP_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        this.tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getPhone() {
        return phoneLabel.getText();
    }

    public Image getDisplayPic() {
        ImagePattern displayPicImage = (ImagePattern) displayPicPanel.getFill();
        return displayPicImage.getImage();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }
}
```
###### /java/guitests/guihandles/PersonDescriptionHandle.java
``` java
/**
 * Provides a handle to the person description panel.
 */
public class PersonDescriptionHandle extends NodeHandle<Node> {
    private static final String DP_FIELD_ID = "#displayPic";
    private static final String NAME_FIELD_ID = "#name";
    private static final String MOBILE_FIELD_ID = "#mobile";
    private static final String BIRTHDAY_FIELD_ID = "#birthday";
    private static final String EMAIL_FIELD_ID = "#email";
    private static final String ADDRESS_FIELD_ID = "#address";
    private static final String INSTAGRAM_FIELD_ID = "#instagram";
    private static final String TWITTER_FIELD_ID = "#twitter";

    private final Circle displayPicPanel;
    private final Label nameLabel;
    private final Label mobileLabel;
    private final Label birthdayLabel;
    private final Label emailLabel;
    private final Label addressLabel;
    private final Label instagramLabel;
    private final Label twitterLabel;

    public PersonDescriptionHandle(Node personDescriptionPanelNode) {
        super(personDescriptionPanelNode);

        this.displayPicPanel = getChildNode(DP_FIELD_ID);
        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.mobileLabel = getChildNode(MOBILE_FIELD_ID);
        this.birthdayLabel = getChildNode(BIRTHDAY_FIELD_ID);
        this.emailLabel = getChildNode(EMAIL_FIELD_ID);
        this.addressLabel = getChildNode(ADDRESS_FIELD_ID);
        this.instagramLabel = getChildNode(INSTAGRAM_FIELD_ID);
        this.twitterLabel = getChildNode(TWITTER_FIELD_ID);
    }

    public Image getDisplayPic() {
        ImagePattern displayPicImage = (ImagePattern) displayPicPanel.getFill();
        return displayPicImage.getImage();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getMobile() {
        return mobileLabel.getText();
    }

    public String getBirthday() {
        return birthdayLabel.getText();
    }

    public String getEmail() {
        return emailLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    public String getInstagram() {
        return instagramLabel.getText();
    }

    public String getTwitter() {
        return twitterLabel.getText();
    }


}
```
###### /java/seedu/address/model/person/DisplayPicTest.java
``` java
public class DisplayPicTest {

    @Test
    public void isValidDisplayPic() throws IllegalValueException {
        //invalid file path
        assertFalse(DisplayPic.isValidPicPath(" "));
        assertFalse(DisplayPic.isValidPicPath("invalid file path"));
        assertFalse(DisplayPic.isValidPicPath("/filePath/does/not/exists"));

        //valid file path but not picture file
        assertFalse(DisplayPic.isValidPicPath("./src/test/"));
        assertFalse(DisplayPic.isValidPicPath("./src/test/data/"));
        assertFalse(DisplayPic.isValidPicPath("./src/test/data/ImageFileStorageTest"));

        //valid picture extension but invalid file path
        assertFalse(DisplayPic.isValidPicPath("./invalid.png"));
        assertFalse(DisplayPic.isValidPicPath("./invalid.jpeg"));

        //valid file and is picture
        assertTrue(DisplayPic.isValidPicPath("./src/test/data/ImageFileStorageTest/testImage.png"));
    }
}
```
###### /java/seedu/address/storage/ImageFileStorageTest.java
``` java
public class ImageFileStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/ImageFileStorageTest/");
    private static final String TEST_IMAGE = FileUtil.getPath("./src/test/data/ImageFileStorageTest/testImage.png");
    private static final String TEST_FILENAME = "test.png";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void createImageFolder_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        createImageDir(null);
    }

    public void createImageDir(String filePath) throws IOException {
        new ImageFileStorage(filePath).createImageDir();
    }

    @Test
    public void assertImageFile() {
        String expectedFile = FileUtil.getPath(TEST_DATA_FOLDER + TEST_FILENAME);
        String actualFile = new ImageFileStorage(TEST_DATA_FOLDER).getImageFilePath(TEST_FILENAME);
        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void copiedImage_success() throws IOException {
        String filePath = testFolder.getRoot().getPath();
        ImageFileStorage imageFileStorage = new ImageFileStorage(filePath);
        imageFileStorage.copyImage(TEST_IMAGE, TEST_FILENAME);
        File expectedFile = new File(TEST_IMAGE);
        File actualFile = new File(imageFileStorage.getImageFilePath(TEST_FILENAME));
        assertFileContentEqual(expectedFile, actualFile);
    }

    /**
     * Asserts that the actual file {@code actualFile} has the same binary data as {@code expectedFile}.
     */
    public void assertFileContentEqual(File expectedFile, File actualFile) throws IOException {
        byte[] expectedContent = Files.readAllBytes(expectedFile.toPath());
        byte[] actualContent = Files.readAllBytes(actualFile.toPath());
        boolean equal = Arrays.equals(expectedContent, actualContent);
        assertTrue(equal);
    }
}
```
###### /java/seedu/address/testutil/EditPersonDescriptorBuilder.java
``` java
    /**
     * Sets the {@code DisplayPic} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withDisplayPic(String displayPic) {
        try {
            ParserUtil.parseDisplayPic(Optional.of(displayPic)).ifPresent(descriptor::setDisplayPic);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("address is expected to be unique.");
        }
        return this;
    }
```
###### /java/seedu/address/testutil/PersonBuilder.java
``` java
    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withDisplayPic(String displayPic) {
        try {
            this.person.setDisplayPic(new DisplayPic(displayPic, true));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("address is expected to be unique.");
        }
        return this;
    }
```
###### /java/seedu/address/ui/testutil/GuiTestAssert.java
``` java
    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(PersonCardHandle expectedCard, PersonCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getPhone(), actualCard.getPhone());
        assertImageEquals(expectedCard.getDisplayPic(), actualCard.getDisplayPic());
        assertEquals(expectedCard.getTags(), actualCard.getTags());
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedPerson}.
     */
    public static void assertCardDisplaysPerson(ReadOnlyPerson expectedPerson, PersonCardHandle actualCard) {
        assertEquals(expectedPerson.getName().fullName, actualCard.getName());
        assertEquals(expectedPerson.getPhone().value, actualCard.getPhone());
        Image expectedDisplayPic = AppUtil.getImage(expectedPerson.getDisplayPic().getNewDisplayPicPath());
        Image actualDisplayPic = actualCard.getDisplayPic();
        assertImageEquals(expectedDisplayPic, actualDisplayPic);
        assertEquals(expectedPerson.getTags().stream().map(tag -> tag.tagName).collect(Collectors.toList()),
                actualCard.getTags());
    }

    /**
     * Asserts that {@code personDescription} displays the details of {@code expectedPerson}.
     */
    public static void assertDescriptionDisplaysPerson(ReadOnlyPerson expectedPerson,
                                                       PersonDescriptionHandle actualDescription) {
        Image expectedDisplayPic = AppUtil.getImage(expectedPerson.getDisplayPic().getNewDisplayPicPath());
        Image actualDisplayPic = actualDescription.getDisplayPic();
        assertImageEquals(expectedDisplayPic, actualDisplayPic);
        assertEquals(expectedPerson.getName().fullName, actualDescription.getName());
        assertEquals(expectedPerson.getPhone().value, actualDescription.getMobile());
        assertEquals(expectedPerson.getBirthday().value, actualDescription.getBirthday());
        assertEquals(expectedPerson.getEmail().value, actualDescription.getEmail());
        assertEquals(expectedPerson.getAddress().value, actualDescription.getAddress());
        assertEquals(expectedPerson.getInstagramName().value, actualDescription.getInstagram());
        assertEquals(expectedPerson.getTwitterName().value, actualDescription.getTwitter());
    }
```
###### /java/seedu/address/ui/testutil/GuiTestAssert.java
``` java
    /**
     * Asserts that the image {@code expected} equals to {@code actual}
     */
    public static void assertImageEquals(Image expected, Image displayed) {
        boolean equal = true;
        for (int i = 0; i < expected.getWidth(); i++) {
            for (int j = 0; j < expected.getHeight(); j++) {
                if (expected.getPixelReader().getArgb(i, j) != displayed.getPixelReader().getArgb(i, j)) {
                    equal = false;
                }
            }
        }
        assertTrue(equal);
    }
}
```
###### /java/systemtests/AddressBookSystemTest.java
``` java
    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field",
            "jfx-text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", "jfx-text-field", CommandBox.ERROR_STYLE_CLASS);
```
