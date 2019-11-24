package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import sample.models.DecodedKeys;
import sample.models.EncodedKeys;

import sample.models.Log;
import sample.models.SustGroup;
import sample.singletons.GroupsProvider;
import sample.singletons.SecureProvider;

import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

import static com.sun.java.accessibility.util.EventID.ITEM;

public class Controller implements Initializable {
    public JFXButton save_to_file;
    public JFXButton key_generate;
    public Label public_key;
    public Label private_key;
    public AnchorPane groups_layout;
    public AnchorPane group_pane_id;
    public JFXListView<Label> items;
    public Label group_public_key;
    public JFXTextField message;
    public JFXButton encrypt;
    public Label encrypted_message;


    private EncodedKeys encodedKeys;
    private DecodedKeys decodedKeys;

    List<SustGroup> groups;

    String  selectedPk;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getGroups();

        items.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + items.getSelectionModel().getSelectedItem());

                String selectedItelText = items.getSelectionModel().getSelectedItem().getText();
                String pkAsString = getGroupByName(selectedItelText).getGroupPublic_key();
                if (pkAsString.length() > 50) {
                    group_public_key.setText(pkAsString.substring(0, 50) + "...");
                    selectedPk=pkAsString;
                } else {
                    group_public_key.setText(pkAsString);
                    selectedPk=pkAsString;
                }

            }
        });


        save_to_file.setOnAction(event -> {
            System.out.println("Save to file");
            try {
                if (encodedKeys != null) {
                    SecureProvider.getInstance().saveMyKeysIntoFile(encodedKeys);

                } else {
                    Log.debug("please generate keys first");
                }

            } catch (Exception e) {
                Log.debug(e.getMessage());

            }

        });

        key_generate.setOnAction(event -> {
            try {
                encodedKeys = SecureProvider.getInstance().keyGeneration();
                public_key.setText(encodedKeys.getPublicKey().substring(0, 50) + "...");
                private_key.setText(encodedKeys.getPrivateKey().substring(0, 50) + "...");

            } catch (Exception e) {
                Log.debug(e.getMessage());
            }
        });


        encrypt.setOnAction(event -> {
            String myMessage = message.getText();


            try {
                byte[] encryptedData =
                        SecureProvider.getInstance().
                                encrypt(myMessage, SecureProvider.getInstance().getPublicKeyFromBase64(selectedPk));
                encrypted_message.setText(Base64.getEncoder().encodeToString(encryptedData).substring(0,55)+"...");
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


    }


    private void setupListView() {
        for (SustGroup sustGroup : groups) {
            items.getItems().add(new Label(sustGroup.getGroupName()));
        }
    }


    private SustGroup getGroupByName(String name) {
        for (SustGroup sustGroup : groups) {

            if (sustGroup.getGroupName().equals(name)) {
                return sustGroup;
            }
        }
        return null;
    }

    private void getGroups() {
        groups = GroupsProvider.getInstance().getGroups();
        setupListView();
        Log.debug("we have ---->  :" + groups.size());
    }
}
