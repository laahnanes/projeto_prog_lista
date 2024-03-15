import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

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
    @FXML
    private Text totalItensText;

    private List<List<Item>> todasAsListas;
    private List<Item> listaAtual;

    public void initialize() {
        todasAsListas = new ArrayList<>();
        listaAtual = new ArrayList<>();
    }

    @FXML
    private void adicionarItem(ActionEvent event) {
        String itemNome = campoDeTexto.getText().trim();
        if (!itemNome.isEmpty()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Quantidade de Itens");
            dialog.setHeaderText(null);
            dialog.setContentText("Quantas unidades?");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(quantidadeStr -> {
                try {
                    int quantidade = Integer.parseInt(quantidadeStr);
                    if (quantidade > 0) {
                        Item item = new Item(itemNome, quantidade);
                        listaAtual.add(item);
                        atualizarListaDeCompras();
                    }
                } catch (NumberFormatException e) {
                    // Lidar com a entrada inválida para a quantidade
                    e.printStackTrace();
                }
            });
            campoDeTexto.clear();
        }
    }

    private void atualizarListaDeCompras() {
        listaDeCompras.getItems().clear();
        for (Item item : listaAtual) {
            listaDeCompras.getItems().add(item.toString());
        }
        atualizarTotalItens();
    }

    private void atualizarTotalItens() {
        int total = 0;
        for (Item item : listaAtual) {
            total += item.getQuantidade();
        }
        totalItensText.setText("Itens totais: " + total);
    }

    @FXML
    private void removerItem(ActionEvent event) {
        int index = listaDeCompras.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            listaAtual.remove(index);
            atualizarListaDeCompras();
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
            atualizarTotalItens();
        }
    }

    @FXML
    private void exibirListaSelecionada(ActionEvent event) {
        int index = comboBoxListas.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            listaAtual = todasAsListas.get(index);
            atualizarListaDeCompras();
        }
    }

    private static class Item {
        private String nome;
        private int quantidade;

        public Item(String nome, int quantidade) {
            this.nome = nome;
            this.quantidade = quantidade;
        }

        public String getNome() {
            return nome;
        }

        public int getQuantidade() {
            return quantidade;
        }

        @Override
        public String toString() {
            return quantidade + " und - " + nome;
        }
    }
}