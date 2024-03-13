import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MainSceneController {

    @FXML
    private TextField inputnameitem;

    @FXML
    void handleAddItem(ActionEvent event) {
        String itemName = inputnameitem.getText();
        System.out.println(itemName);
    }

}
