package com.controleEstoque.controller;
import com.controleEstoque.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static com.controleEstoque.model.Historico.createHistorico;

public class CadastrarController {

    //Anotações dos componenets Javafx
    @FXML
    private TextField createNome;
    @FXML
    private TextField createPreco;
    @FXML
    private TextField createQuantidade;
    @FXML
    private Button createCadastrar;
    @FXML
    private Button controleEstoqueButton;
    @FXML
    private Button fornecedoresButton;
    @FXML
    private Button clientesButton;
    @FXML
    private Button historicoButton;
    @FXML
    private Button atualizarEstoqueButton;

    //Esse metodo e chamado pelo javaFX quando o palco e iniciado, estabelece as acoes dos botoes
    public void initialize() {
        controleEstoqueButton.setOnAction(event -> loadScreen("/view/Controle.fxml"));
        fornecedoresButton.setOnAction(event -> loadScreen("/view/Fornecedores.fxml"));
        clientesButton.setOnAction(event -> loadScreen("/view/Clientes.fxml"));
        historicoButton.setOnAction(event -> loadScreen("/view/Historico.fxml"));
        atualizarEstoqueButton.setOnAction(event -> loadScreen("/view/Atualizar.fxml"));
        createCadastrar.setOnAction(event -> acaoCadastrar()); // Move o setOnAction para initialize
    }

    //estabelece o palco atual, ja que Stage nao pode ser nulo
    private void loadScreen(String fxmlFile) {
        Stage currentStage = (Stage) controleEstoqueButton.getScene().getWindow();
        new RouteController().loadScreen(currentStage, fxmlFile);
    }

    @FXML
    public void acaoCadastrar() {
        String nome_create = createNome.getText();
        double preco;
        int quantidade;

        try {
            preco = Double.parseDouble(createPreco.getText());
            quantidade = Integer.parseInt(createQuantidade.getText());
            cadastrar(nome_create, preco, quantidade);
        } catch (NumberFormatException e) {
            // Exibir um alerta em caso de entrada inválida
            Alert alert = new Alert(Alert.AlertType.ERROR, "Entrada inválida: verifique os campos de preço e quantidade.");
            alert.showAndWait();
        }
    }

    public void cadastrar(String nome_create, double preco, int quantidade) {
        createItem(nome_create, preco, quantidade);
    }

    public static void createItem(String nome, double preco, int quantidade) {
        PreparedStatement st = null;
        java.sql.Connection conn = null; // Declare a conexão aqui

        try {
            conn = DB.getConnection(); // Obtém a conexão
            st = conn.prepareStatement(
                    "INSERT INTO produto (Nome, Preco, Quantidade) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, nome);
            st.setDouble(2, preco);
            st.setInt(3, quantidade);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    createHistorico(nome, preco, quantidade, id);
                    System.out.println("Produto cadastrado com sucesso! ID = " + id);
                    // Usar Alert para notificar o usuário
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Produto cadastrado com sucesso! ID = " + id);
                    alert.showAndWait();
                }
            } else {
                System.out.println("Nenhuma linha afetada.");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nenhum produto cadastrado.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar o produto: " + e.getMessage());
            alert.showAndWait();
        } finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }
}
