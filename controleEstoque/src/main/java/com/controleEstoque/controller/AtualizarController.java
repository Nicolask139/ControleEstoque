package com.controleEstoque.controller;
import com.controleEstoque.db.DB;
import com.controleEstoque.model.Historico;
import com.controleEstoque.model.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AtualizarController {

    //Anotações dos componenets Javafx
    @FXML
    private Button controleEstoqueButton;
    @FXML
    private Button fornecedoresButton;
    @FXML
    private Button clientesButton;
    @FXML
    private Button historicoButton;
    @FXML
    private Button cadastrarProdutoButton;
    @FXML
    private Button atualizarEstoqueButton;
    @FXML
    private Button entradaButton;
    @FXML
    private Button saidaButton;
    @FXML
    private TextField quantidade;
    @FXML
    private TextField nome;

    //Esse metodo e chamado pelo javaFX quando o palco e iniciado, estabelece as acoes dos botoes
    public void initialize() {
        controleEstoqueButton.setOnAction(event -> loadScreen("/view/Controle.fxml"));
        fornecedoresButton.setOnAction(event -> loadScreen("/view/Fornecedores.fxml"));
        clientesButton.setOnAction(event -> loadScreen("/view/Clientes.fxml"));
        historicoButton.setOnAction(event -> loadScreen("/view/Historico.fxml"));
        cadastrarProdutoButton.setOnAction(event -> loadScreen("/view/Cadastrar.fxml"));
        entradaButton.setOnAction(event -> entradaProduto());
        saidaButton.setOnAction(event -> saidaProduto());
    }

    //estabelece o palco atual, ja que Stage nao pode ser nulo
    private void loadScreen(String fxmlFile) {
        Stage currentStage = (Stage) atualizarEstoqueButton.getScene().getWindow();
        new RouteController().loadScreen(currentStage, fxmlFile);
    }

    private void entradaProduto() {
        String input = nome.getText();
        int quantidadeEntradaProduto = parseInt(quantidade.getText());
        if (input.isEmpty() || quantidadeEntradaProduto <= 0) {
            showError("Nome ou quantidade inválidos.");
            return;
        }
        Produto produto = getProdutoByNomeOuId(input);
        if (produto != null) {
            int novaQuantidade = produto.getQuantidade() + quantidadeEntradaProduto;
            atualizarEstoque(produto.getId(), novaQuantidade);
            Historico.createHistorico(produto.getNome(), produto.getPreco(), quantidadeEntradaProduto, produto.getId());
            showSuccess("Entrada de produto realizada com sucesso!");
        } else {
            showError("Produto não encontrado.");
        }
    }

    private void saidaProduto() {
        String input = nome.getText();
        int quantidadeSaidaProduto = parseInt(quantidade.getText());
        if (input.isEmpty() || quantidadeSaidaProduto <= 0) {
            showError("Nome ou quantidade inválidos.");
            return;
        }
        Produto produto = getProdutoByNomeOuId(input);
        if (produto != null) {
            if (produto.getQuantidade() >= quantidadeSaidaProduto) {
                int novaQuantidade = produto.getQuantidade() - quantidadeSaidaProduto;
                atualizarEstoque(produto.getId(), novaQuantidade);
                // Usa o nome do produto extraído do objeto `Produto`
                Historico.createHistorico(produto.getNome(), produto.getPreco(), -quantidadeSaidaProduto, produto.getId());
                showSuccess("Saída de produto realizada com sucesso!");
            } else {
                showError("Estoque insuficiente.");
            }
        } else {
            showError("Produto não encontrado.");
        }
    }

    private Produto getProdutoByNomeOuId(String input) {
        Produto produto = null;
        String sql;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();
            if (isNumeric(input)) {
                sql = "SELECT * FROM produto WHERE Id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(input));
            } else {
                sql = "SELECT * FROM produto WHERE Nome = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, input);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("Id");
                String nomeProduto = rs.getString("Nome");
                double preco = rs.getDouble("Preco");
                int quantidade = rs.getInt("Quantidade");
                produto = new Produto(id, nomeProduto, preco, quantidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erro ao buscar produto: " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection();
        }
        return produto;
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void atualizarEstoque(int produtoId, int novaQuantidade) {
        String sql = "UPDATE produto SET Quantidade = ? WHERE Id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DB.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, produtoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erro ao atualizar estoque: " + e.getMessage());
        } finally {
            DB.closeStatement(stmt);
            DB.closeConnection();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            showError("Quantidade inválida.");
            return -1; // Valor inválido
        }
    }
}
