package com.controleEstoque.controller;
import com.controleEstoque.db.DB;
import com.controleEstoque.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;

public class ControleController {

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
    private TextField nomeProdutoField;
    @FXML
    private TableView<Produto> tabelaProdutos;
    @FXML
    private TableColumn<Produto, Integer> colunaId;
    @FXML
    private TableColumn<Produto, String> colunaNome;
    @FXML
    private TableColumn<Produto, Double> colunaPreco;
    @FXML
    private TableColumn<Produto, Integer> colunaQuantidade;
    @FXML
    private Button pesquisarButton;

    //Esse metodo e chamado pelo javaFX quando o palco e iniciado, estabelece as acoes dos botoes
    public void initialize() {
        atualizarEstoqueButton.setOnAction(event -> loadScreen("/view/Atualizar.fxml"));
        fornecedoresButton.setOnAction(event -> loadScreen("/view/Fornecedores.fxml"));
        clientesButton.setOnAction(event -> loadScreen("/view/Clientes.fxml"));
        historicoButton.setOnAction(event -> loadScreen("/view/Historico.fxml"));
        cadastrarProdutoButton.setOnAction(event -> loadScreen("/view/Cadastrar.fxml"));
        if (tabelaProdutos != null) {
            colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
            colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        }
        pesquisarButton.setOnAction(event -> acaoPesquisaNome());
    }

    //estabelece o palco atual, ja que Stage nao pode ser nulo
    private void loadScreen(String fxmlFile) {
        Stage currentStage = (Stage) controleEstoqueButton.getScene().getWindow();
        new RouteController().loadScreen(currentStage, fxmlFile);
    }

    //chama a funcao pesquisarNome passando o texto da interface como parametro
    public void acaoPesquisaNome() {
        String nomeProduto = nomeProdutoField.getText();
        pesquisarNome(nomeProduto);
    }


    private void pesquisarNome(String nome) {
        ObservableList<Produto> produtos = FXCollections.observableArrayList();
        PreparedStatement st = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();
            String sql = null;
            if(nome==""||nome==null){
                sql = STR."SELECT * FROM produto LIMIT 50";
            }else {
                sql = STR."SELECT * FROM produto WHERE Nome LIKE '%\{nome}%'";
            }
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nomeProduto = rs.getString("Nome");
                double preco = rs.getDouble("Preco");
                int quantidade = rs.getInt("Quantidade");

                produtos.add(new Produto(id, nomeProduto, preco, quantidade));
            }
            if (tabelaProdutos != null) {
                tabelaProdutos.setItems(produtos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao buscar produtos: " + e.getMessage());
            alert.showAndWait();
        } finally {
            // Fechar recursos manualmente
            DB.closeResultSet(rs);
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }

}

