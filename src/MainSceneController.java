import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainSceneController {

    @FXML
    private ListView<String> listaDeCompras;
    @FXML
    private TextField campoDeTexto;
    @FXML
    private ComboBox<String> comboBoxListas;

    private List<List<String>> todasAsListas;
    private List<String> listaAtual;

    public void initialize() {
        todasAsListas = new ArrayList<>();
        listaAtual = new ArrayList<>();
    }

    @FXML
    private void adicionarItem(ActionEvent event) {
        String item = campoDeTexto.getText().trim();
        if (!item.isEmpty()) {
            listaAtual.add(item);
            listaDeCompras.getItems().setAll(listaAtual);
            campoDeTexto.clear();
        }
    }

    @FXML
    private void removerItem(ActionEvent event) {
        int index = listaDeCompras.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            listaAtual.remove(index);
            listaDeCompras.getItems().setAll(listaAtual);
        }
    }

    @FXML
    private void criarLista(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nome da Lista");
        dialog.setHeaderText(null);
        dialog.setContentText("Dê um nome para sua lista:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            todasAsListas.add(new ArrayList<>());
            comboBoxListas.getItems().add(name);
        });
    }

    @FXML
    private void excluirLista(ActionEvent event) {
        int index = comboBoxListas.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            todasAsListas.remove(index);
            comboBoxListas.getItems().remove(index);
            listaAtual.clear();
            listaDeCompras.getItems().clear();
        }
    }

    @FXML
    private void exibirListaSelecionada(ActionEvent event) {
        int index = comboBoxListas.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            listaAtual = todasAsListas.get(index);
            listaDeCompras.getItems().setAll(listaAtual);
        }
    }
}
