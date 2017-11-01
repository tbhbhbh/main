# JunQuann
###### /java/seedu/address/commons/util/ImagePathUtil.java
``` java
/**
 * Appends selected image path for inputs
 */
public class ImagePathUtil {

    private static final String ERROR_MESSAGE = "Please make sure that your input includes either dp/Y or dp/N";

    private static final int PREFIX_LENGTH = PREFIX_DP.getPrefix().length();

    public static String setPath(String arguments, CommandBox commandBox) throws ParseException {
        try {
            int prefixIndex = findPrefixPosition(arguments, PREFIX_DP.getPrefix(), 0);
            StringBuilder sb = new StringBuilder(arguments);
            String choice = sb.substring(prefixIndex + PREFIX_LENGTH, prefixIndex + PREFIX_LENGTH + 1);
            if (requireFileChooser(choice)) {
                String selectedPath = commandBox.getDisplayPicPath();
                sb.replace(prefixIndex, prefixIndex + PREFIX_LENGTH + 1, PREFIX_DP.getPrefix() + selectedPath + " ");
                arguments = sb.toString();
            } else {
                sb.replace(prefixIndex, prefixIndex + PREFIX_LENGTH + 1, PREFIX_DP.getPrefix()
                        + DEFAULT_DISPLAY_PIC + " ");
                arguments = sb.toString();
            }
            return arguments;
        } catch (StringIndexOutOfBoundsException sioe) {
            throw new ParseException(ERROR_MESSAGE, sioe);
        }
    }

    /**
     *
     * @param input is the user input in the Command Box
     * @return
     */
    private static boolean requireFileChooser(String input) {
        String trimmedInput = input.trim();
        if (trimmedInput.equalsIgnoreCase("y")) {
            return true;
        }
        return false;
    }

    private static int findPrefixPosition(String argsString, String prefix, int fromIndex) {
        int prefixIndex = argsString.indexOf(" " + prefix, fromIndex);
        return prefixIndex == -1 ? -1
                : prefixIndex + 1; // +1 as offset for whitespace
    }

}
```
###### /java/seedu/address/commons/util/ImagePathUtil.java
``` java

```
###### /java/seedu/address/model/person/DisplayPic.java
``` java
/**
 * Represents a Person's display picture path in a addressbook
 */
public class DisplayPic {

    public final String displayPicPath;

    public DisplayPic(String displayPicPath) throws IllegalValueException {
        requireNonNull(displayPicPath);
        String trimmedDisplayPicPath = displayPicPath.trim();
        this.displayPicPath = trimmedDisplayPicPath;
    }

    @Override
    public String toString() {
        return this.displayPicPath;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DisplayPic // instanceof handles nulls
                && this.displayPicPath.equals(((DisplayPic) other).displayPicPath)); // state check
    }


    @Override
    public int hashCode() {
        return displayPicPath.hashCode();
    }
}
```
###### /java/seedu/address/ui/CommandBox.java
``` java
    public String getDisplayPicPath() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter ("PICTURE files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else {
            return DEFAULT_DISPLAY_PIC;
        }
    }
```
###### /java/seedu/address/ui/PersonCard.java
``` java
    /**
     * Initialise the image in PersonCard display
     * @param person
     */
    private void initImage(ReadOnlyPerson person) {
        Image displayPicture;
        if (person.getDisplayPic().toString().equals(DEFAULT_DISPLAY_PIC)) {
            displayPicture = AppUtil.getImage(DEFAULT_DISPLAY_PIC);
        } else {
            File personImg = new File(person.getDisplayPic().toString());
            String imgUrl = personImg.toURI().toString();
            displayPicture = new Image(imgUrl);
        }
        displayPic.setFill(new ImagePattern(displayPicture));
    }
```
###### /java/seedu/address/ui/PersonCard.java
``` java

    /**
     * Create new labels and bind a colour to it
     * @param person
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            Label newTag = new Label(tag.tagName);
            newTag.setStyle("-fx-background-color: "
                    + getTagColours(tag.tagName));
            tags.getChildren().add(newTag);
        });
    }

    private String getTagColours(String tagName) {
        if (!tagColours.containsKey(tagName)) {
            tagColours.put(tagName, colours[rand.nextInt(colours.length)]);
        }
        return tagColours.get(tagName);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
```
###### /java/seedu/address/ui/PersonDescription.java
``` java
/**
 * The UI component that is responsible for displaying contact's in depth information
 */
public class PersonDescription extends UiPart<StackPane> {

    private static final String FXML = "PersonDescription.fxml";
    private static final String DEFAULT_DP = "/images/defaultperson.png";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Circle displayPic;

    @FXML
    private Label name;

    @FXML
    private Label group;

    @FXML
    private Label mobile;

    @FXML
    private Label birthday;

    @FXML
    private Label email;

    @FXML
    private Label address;

    @FXML
    private Label instagram;

    @FXML
    private Label twitter;


    public PersonDescription() {
        super(FXML);
        registerAsAnEventHandler(this);
    }

    /**
     *
     * @param person
     */
    private void loadPersonDescription(ReadOnlyPerson person) {
        name.setText(person.getName().fullName);
        mobile.setText(person.getPhone().value);
        birthday.setText(person.getBirthday().value);
        email.setText(person.getEmail().value);
        address.setText(person.getAddress().value);
        instagram.setText(person.getInstagramName().value);
        twitter.setText(person.getTwitterName().value);
        initDisplayPic(person);
    }

    /**
     *
     * @param person
     */
    private void initDisplayPic(ReadOnlyPerson person) {
        Image displayPictureImg;
        String profilePic = person.getDisplayPic().toString();
        if (profilePic.equals(DEFAULT_DISPLAY_PIC)) {
            displayPictureImg = AppUtil.getImage(profilePic);
        } else {
            File imgFile = new File(person.getDisplayPic().toString());
            displayPictureImg = new Image(imgFile.toURI().toString());
        }
        displayPic.setFill(new ImagePattern(displayPictureImg));
    }


    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonDescription(event.getNewSelection().person);
    }


}
```
###### /java/seedu/address/ui/PersonDescription.java
``` java

```
###### /resources/view/BrowserPanel.fxml
``` fxml
<StackPane prefHeight="400.0" prefWidth="300.0" style="-fx-background-radius: 5; -fx-background-insets: 0; -fx-background-color: #fff;" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
  <WebView fx:id="browser" minHeight="-Infinity" minWidth="-Infinity" prefHeight="-1.0" prefWidth="-1.0" />
   <padding>
      <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
   </padding>
</StackPane>
```
###### /resources/view/CommandBox.fxml
``` fxml
<StackPane styleClass="anchor-pane" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
   <JFXTextField fx:id="commandTextField" labelFloat="true" onAction="#handleCommandInputChanged" onKeyPressed="#handleKeyPress" promptText="Enter command here..." unFocusColor="BLACK">
      <padding>
         <Insets left="20.0" right="20.0" top="10.0" />
      </padding>
   </JFXTextField>
</StackPane>
```
###### /resources/view/default.css
``` css
.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
    -fx-background-color: #F5F9FA;
}

.list-view {
     -fx-background-insets: 0;
     -fx-padding: 0;
     -fx-background-color: derive(#1d1d1d, 20%);
}

.list-cell:filled:hover{
    -fx-background-color: #424d5f;
}

.list-cell:filled:hover .label{
    -fx-text-fill: #f7f7f7;
}

.list-cell:filled:selected{
    -fx-background-color: #00bfbf;
}

.list-cell:empty{
    -fx-background-color: #F5F9FA;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: white;
    -fx-background-color: #3e7b91;
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}
```
###### /resources/view/default.css
``` css

```
###### /resources/view/GroupList.fxml
``` fxml
<VBox minWidth="150.0" prefHeight="445.0" prefWidth="150.0" style="-fx-background-radius: 10; -fx-background-color: white;" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <Pane minHeight="30.0" minWidth="150.0" VBox.vgrow="NEVER">
         <children>
            <Label alignment="CENTER" layoutX="25.0" layoutY="-3.0" prefHeight="30.0" prefWidth="87.0" text="Groups">
               <font>
                  <Font name="Apple Chancery" size="18.0" />
               </font>
            </Label>
         </children>
      </Pane>
      <JFXListView fx:id="tagListView" style="-fx-background-radius: 10; -fx-background-color: #F5F9FA;" VBox.vgrow="ALWAYS" />
   </children>
   <padding>
      <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
   </padding>
</VBox>
```
###### /resources/view/MainWindow.fxml
``` fxml
<VBox stylesheets="@default.css" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">

  <MenuBar fx:id="menuBar" maxHeight="25.0" prefHeight="22.0" prefWidth="1022.0" VBox.vgrow="NEVER">
    <Menu mnemonicParsing="false" text="File">
      <MenuItem mnemonicParsing="false" onAction="#handleExit" text="Exit" />
    </Menu>
    <Menu mnemonicParsing="false" text="Help">
      <MenuItem fx:id="helpMenuItem" mnemonicParsing="false" onAction="#handleHelp" text="Help" />
    </Menu>
  </MenuBar>
  <StackPane fx:id="commandBoxPlaceholder" prefHeight="30.0" prefWidth="1022.0" style="-fx-background-color: #C1D3DD;" VBox.vgrow="NEVER">
      <padding>
         <Insets bottom="5.0" left="10.0" right="10.0" top="20.0" />
      </padding>
  </StackPane>

  <StackPane fx:id="resultDisplayPlaceholder" maxHeight="100" minHeight="80.0" prefHeight="80.0" style="-fx-background-color: #C1D3DD;" VBox.vgrow="NEVER">
      <padding>
         <Insets left="10.0" right="10.0" />
      </padding>
  </StackPane>
   <HBox prefWidth="1150.0" spacing="10.0" style="-fx-background-color: #F5F9FA;" VBox.vgrow="ALWAYS">
      <children>
         <VBox fx:id="tagList" minWidth="150.0" prefHeight="200.0" prefWidth="150.0" HBox.hgrow="NEVER">
            <children>
               <StackPane fx:id="tagListPanelPlaceholder" VBox.vgrow="ALWAYS" />
            </children>
         </VBox>
       <VBox fx:id="personList" minWidth="270.0" prefWidth="270.0" HBox.hgrow="NEVER">
         <StackPane fx:id="personListPanelPlaceholder" VBox.vgrow="ALWAYS" />
       </VBox>
       <StackPane fx:id="personDescriptionPlaceHolder" minWidth="300.0" prefWidth="100.0" HBox.hgrow="NEVER">
            <HBox.margin>
               <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
            </HBox.margin>
         </StackPane>
       <StackPane fx:id="browserPlaceholder" alignment="TOP_RIGHT" minWidth="400.0" prefWidth="100.0" HBox.hgrow="ALWAYS">
            <HBox.margin>
               <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
            </HBox.margin>
         </StackPane>
      </children>
      <padding>
         <Insets left="10.0" right="10.0" />
      </padding>
   </HBox>

  <StackPane fx:id="statusbarPlaceholder" VBox.vgrow="NEVER" />
</VBox>
```
###### /resources/view/PersonDescription.fxml
``` fxml
<AnchorPane prefHeight="503.0" prefWidth="269.0" style="-fx-background-color: #FDFDFD; -fx-background-radius: 5;" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <ImageView fitHeight="186.0" fitWidth="297.0" pickOnBounds="true" style="-fx-opacity: 0.8;" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="0.0">
         <image>
            <Image url="@../images/Avatar.jpg" />
         </image>
      </ImageView>
      <Circle fx:id="displayPic" fill="WHITE" layoutX="149.0" layoutY="186.0" radius="41.0" stroke="WHITE" strokeType="INSIDE" strokeWidth="2.0" />
      <Label fx:id="name" alignment="CENTER" layoutX="83.0" layoutY="226.0" prefHeight="25.0" prefWidth="132.0" text="Name" textAlignment="CENTER">
         <font>
            <Font name="Helvetica Bold" size="20.0" />
         </font>
      </Label>
      <Label fx:id="group" layoutX="130.0" layoutY="251.0" prefHeight="16.0" prefWidth="38.0" text="Group" textAlignment="CENTER">
         <font>
            <Font name="YuGothic Medium" size="12.0" />
         </font>
      </Label>
      <VBox alignment="CENTER_LEFT" layoutY="275.0" prefHeight="228.0" prefWidth="297.0" spacing="15.0">
         <children>
            <Label fx:id="mobile" graphicTextGap="15.0" prefHeight="25.0" prefWidth="237.0" text="Mobile" textAlignment="CENTER" VBox.vgrow="ALWAYS">
               <graphic>
                  <MaterialDesignIconView glyphName="PHONE" size="20" />
               </graphic>
            </Label>
            <Label fx:id="birthday" graphicTextGap="15.0" prefHeight="25.0" prefWidth="237.0" text="Birthday" textAlignment="CENTER">
               <graphic>
                  <MaterialDesignIconView glyphName="CAKE_VARIANT" size="20" />
               </graphic>
            </Label>
            <Label fx:id="email" graphicTextGap="15.0" prefHeight="25.0" prefWidth="237.0" text="Email" textAlignment="CENTER">
               <graphic>
                  <MaterialDesignIconView glyphName="GMAIL" size="20" />
               </graphic>
            </Label>
            <Label fx:id="address" graphicTextGap="15.0" prefHeight="20.0" prefWidth="265.0" text="Address" textAlignment="CENTER" wrapText="true">
               <graphic>
                  <MaterialDesignIconView glyphName="MAP" size="20" />
               </graphic>
            </Label>
            <Label fx:id="instagram" graphicTextGap="15.0" prefHeight="25.0" prefWidth="237.0" text="Instagram" textAlignment="CENTER">
               <graphic>
                  <MaterialDesignIconView glyphName="INSTAGRAM" size="20" />
               </graphic>
            </Label>
            <Label fx:id="twitter" graphicTextGap="15.0" prefHeight="25.0" prefWidth="237.0" text="Twitter" textAlignment="CENTER">
               <graphic>
                  <MaterialDesignIconView glyphName="TWITTER" size="20" />
               </graphic>
            </Label>
         </children>
         <padding>
            <Insets bottom="10.0" left="20.0" top="10.0" />
         </padding>
      </VBox>
   </children>
   <padding>
      <Insets bottom="5.0" left="5.0" right="5.0" top="5.0" />
   </padding>
   <effect>
      <DropShadow color="#cfe4ff" />
   </effect>
</AnchorPane>
```
###### /resources/view/PersonListCard.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <HBox id="cardPane" fx:id="cardPane" prefHeight="90.0" prefWidth="230.0" style="-fx-background-color: transparent;">
         <children>
            <Pane minHeight="25.0" minWidth="70.0" prefHeight="25.0" prefWidth="30.0" HBox.hgrow="NEVER">
               <children>
                  <Circle fx:id="displayPic" fill="WHITE" layoutX="35.0" layoutY="45.0" radius="25.0" stroke="WHITE" strokeType="INSIDE" />
               </children>
            </Pane>
            <VBox alignment="CENTER_LEFT" minHeight="90.0" prefHeight="90.0" prefWidth="166.0" HBox.hgrow="ALWAYS">
               <children>
                  <HBox alignment="CENTER_LEFT" spacing="5">
                     <children>
                        <Label fx:id="id" styleClass="cell_big_label">
                           <minWidth>
                              <Region fx:constant="USE_PREF_SIZE" />
                           </minWidth>
                        </Label>
                        <Label fx:id="name" styleClass="cell_big_label" text="\$first">
                           <font>
                              <Font name="NanumGothic ExtraBold" size="15.0" />
                           </font>
                        </Label>
                     </children>
                  </HBox>
                  <FlowPane fx:id="tags" />
                  <Label fx:id="phone" styleClass="cell_small_label" text="\$phone" textFill="#797777">
                     <font>
                        <Font name="Material Design Icons" size="13.0" />
                     </font>
                  </Label>
               </children>
               <padding>
                  <Insets left="10.0" />
               </padding>
            </VBox>
         </children>
      </HBox>
      <Separator prefWidth="200.0">
         <VBox.margin>
            <Insets />
         </VBox.margin>
         <padding>
            <Insets left="50.0" />
         </padding>
      </Separator>
   </children>
</VBox>
```
###### /resources/view/PersonListPanel.fxml
``` fxml
<JFXListView fx:id="personListView" depth="10" maxHeight="1.7976931348623157E308" maxWidth="1.7976931348623157E308" prefHeight="400.0" prefWidth="250.0" style="-fx-background-color: #F5F9FA;" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1" />
```
###### /resources/view/ResultDisplay.fxml
``` fxml
<StackPane fx:id="placeHolder" style="-fx-background-color: TRANSPARENT;" styleClass="pane-with-border" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
  <TextArea fx:id="resultDisplay" editable="false" opacity="0.55" style="-fx-background-color: transparent;">
      <padding>
         <Insets bottom="10.0" left="20.0" right="20.0" top="5.0" />
      </padding>
      <StackPane.margin>
         <Insets />
      </StackPane.margin>
      <font>
         <Font name="Helvetica" size="15.0" />
      </font></TextArea>
</StackPane>
```
