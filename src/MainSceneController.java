import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainSceneController {

    @FXML
    private ListView<HBox> listaDeCompras;
    @FXML
    private TextField campoDeTexto;
    @FXML
    private ComboBox<String> comboBoxListas;
    @FXML
    private Text totalItensText;
    @FXML
    private Text totalPriceText;

    private List<List<Item>> todasAsListas;
    private List<Item> listaAtual;

    public void initialize() {
        todasAsListas = new ArrayList<>();
        listaAtual = new ArrayList<>();
        listaDeCompras.setCellFactory(lv -> new ListCell<HBox>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CheckBox checkBox = (CheckBox) item.getChildren().get(0);
                    Item listItem = listaAtual.get(getIndex());
                    checkBox.setSelected(listItem.isMarcado());
                    checkBox.setOnAction(e -> marcarItem(checkBox, listItem));
                    setGraphic(item);
                }
            }
        });
    }

    @FXML
    private void adicionarItem(ActionEvent event) {
        String itemNome = campoDeTexto.getText().trim();
        if (!itemNome.isEmpty() && comboBoxListas.getSelectionModel().getSelectedItem() != null) {
            TextInputDialog quantidadeDialog = new TextInputDialog();
            quantidadeDialog.setTitle("Quantidade de Itens");
            quantidadeDialog.setHeaderText(null);
            quantidadeDialog.setContentText("Quantas unidades?");

            Optional<String> resultQuantidade = quantidadeDialog.showAndWait();
            resultQuantidade.ifPresent(quantidadeStr -> {
                try {
                    int quantidade = Integer.parseInt(quantidadeStr);
                    if (quantidade > 0) {
                        TextInputDialog precoDialog = new TextInputDialog();
                        precoDialog.setTitle("Preço do Item");
                        precoDialog.setHeaderText(null);
                        precoDialog.setContentText("Qual o preço unitário? (R$)");

                        Optional<String> resultPreco = precoDialog.showAndWait();
                        resultPreco.ifPresent(precoStr -> {
                            try {
                                double preco = Double.parseDouble(precoStr);
                                if (preco > 0) {
                                    Item item = new Item(itemNome, quantidade, preco);
                                    listaAtual.add(item);
                                    atualizarListaDeCompras();
                                } else {
                                    exibirAlerta("Erro", "Preço Inválido", "O preço deve ser maior que zero.");
                                }
                            } catch (NumberFormatException e) {
                                exibirAlerta("Erro", "Preço Inválido", "O preço deve ser um número válido.");
                            }
                        });
                    } else {
                        exibirAlerta("Erro", "Quantidade Inválida", "A quantidade deve ser maior que zero.");
                    }
                } catch (NumberFormatException e) {
                    exibirAlerta("Erro", "Quantidade Inválida", "A quantidade deve ser um número inteiro válido.");
                }
            });
            campoDeTexto.clear();
        } else {
            exibirAlerta("Nenhuma lista encontrada", "Por favor, selecione uma lista.",
                    "Selecione uma lista antes de adicionar um item. Caso não exista lista, crie uma nova.");
        }
    }

    private void exibirAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void atualizarListaDeCompras() {
        listaDeCompras.getItems().clear();
        for (Item item : listaAtual) {
            HBox hbox = new HBox();
            CheckBox checkBox = new CheckBox();
            Label label = new Label(item.toString());
            Button incrementButton = new Button("+");
            Button decrementButton = new Button("-");
            incrementButton.setPrefWidth(30);
            decrementButton.setPrefWidth(30);
            incrementButton.setStyle("-fx-background-color: #F4A460; -fx-text-fill: white;");
            decrementButton.setStyle("-fx-background-color: #F4A460; -fx-text-fill: white;");
            incrementButton.setOnAction(e -> incrementarQuantidade(item));
            decrementButton.setOnAction(e -> decrementarQuantidade(item));
            hbox.setSpacing(10);
            hbox.getChildren().addAll(checkBox, label, incrementButton, decrementButton);
            listaDeCompras.getItems().add(hbox);
        }
        atualizarTotalItens();
        atualizarPrecoTotal();
    }

    private void incrementarQuantidade(Item item) {
        item.incrementarQuantidade();
        atualizarListaDeCompras();
    }

    private void decrementarQuantidade(Item item) {
        item.decrementarQuantidade();
        if (item.getQuantidade() <= 0) {
            listaAtual.remove(item);
        }
        atualizarListaDeCompras();
    }

    @FXML
    private void marcarItem(CheckBox checkBox, Item item) {
    item.setMarcado(checkBox.isSelected());
}

    private void atualizarTotalItens() {
        int total = 0;
        for (Item item : listaAtual) {
            total += item.getQuantidade();
        }
        totalItensText.setText("Itens totais: " + total);
    }

    private void atualizarPrecoTotal() {
        double precoTotal = 0;
        for (Item item : listaAtual) {
            precoTotal += item.getPrecoTotal();
        }
        totalPriceText.setText("Preço total: R$" + String.format("%.2f", precoTotal));

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
            atualizarPrecoTotal();
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
        private double precoUnitario;
        private boolean marcado;

        public Item(String nome, int quantidade, double precoUnitario) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
        }

        public boolean isMarcado() {
            return marcado;
        }

        public void setMarcado(boolean marcado) {
            this.marcado = marcado;
        }

        public String getNome() {
            return nome;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public double getPrecoUnitario() {
            return precoUnitario;
        }

        public double getPrecoTotal() {
            return quantidade * precoUnitario;
        }

        public void incrementarQuantidade() {
            quantidade++;
        }

        public void decrementarQuantidade() {
            quantidade--;
        }

        @Override
        public String toString() {
            return "   " + quantidade + " und - " + nome + "   (R$" + String.format("%.2f", precoUnitario) + ")";
        }
    }
}
