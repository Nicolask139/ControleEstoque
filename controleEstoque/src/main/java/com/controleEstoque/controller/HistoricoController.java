package com.controleEstoque.controller;
import com.controleEstoque.db.DB;
import com.controleEstoque.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoricoController {

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
    private TableColumn<Produto, Integer> colunaData;
    @FXML
    private Button pesquisarDia;
    @FXML
    private Button pesquisarSemana;
    @FXML
    private Button pesquisarMes;
    @FXML
    private Button pesquisarTodos;

    //Esse metodo e chamado pelo javaFX quando o palco e iniciado, estabelece as acoes dos botoes
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data_criacao"));
        controleEstoqueButton.setOnAction(event -> loadScreen("/view/Controle.fxml"));
        fornecedoresButton.setOnAction(event -> loadScreen("/view/Fornecedores.fxml"));
        clientesButton.setOnAction(event -> loadScreen("/view/Clientes.fxml"));
        atualizarEstoqueButton.setOnAction(event -> loadScreen("/view/Atualizar.fxml"));
        cadastrarProdutoButton.setOnAction(event -> loadScreen("/view/Cadastrar.fxml"));
        pesquisarDia.setOnAction(event -> popularTableView(1));
        pesquisarSemana.setOnAction(event -> popularTableView(2));
        pesquisarMes.setOnAction(event -> popularTableView(3));
        pesquisarTodos.setOnAction(event -> popularTableView(4));
    }

    //estabelece o palco atual, ja que Stage nao pode ser nulo
    private void loadScreen(String fxmlFile) {
        Stage currentStage = (Stage) historicoButton.getScene().getWindow();
        new RouteController().loadScreen(currentStage, fxmlFile);
    }

    private static List<Produto> executarConsulta(int tipo) {
        List<Produto> produtos = new ArrayList<>();
        PreparedStatement st = null;
        Connection conn;
        ResultSet rs = null;
        String query = null;
        switch (tipo) {
            case 1:
                query = "SELECT id, Nome, preco, Quantidade, data_criacao FROM historico WHERE data_criacao BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) - INTERVAL 1 SECOND";
                break;
            case 2:
                query = "SELECT id, Nome, preco, Quantidade, data_criacao FROM historico WHERE data_criacao >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY Produto_id";
                break;
            case 3:
                query = "SELECT id, Nome, preco, Quantidade, data_criacao FROM historico WHERE MONTH(data_criacao) = MONTH(CURDATE()) AND YEAR(data_criacao) = YEAR(CURDATE()) GROUP BY Produto_id";
                break;
            case 4:
                query = "SELECT id, Nome, preco, Quantidade, data_criacao FROM historico LIMIT 1000";
                break;
            default:
                return produtos;
        }
        try {
            conn = DB.getConnection();
            st = conn.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("Nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("Quantidade");
                Timestamp data_criacao = rs.getTimestamp("data_criacao");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dataFormatada = sdf.format(data_criacao);
                produtos.add(new Produto(id, nome, preco, quantidade, dataFormatada));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao buscar produtos: " + e.getMessage());
            alert.showAndWait();
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
            DB.closeConnection();
        }
        return produtos;
    }

    public void popularTableView(int tipo) {
        List<Produto> produtos = executarConsulta(tipo);
        ObservableList<Produto> observableProdutos = FXCollections.observableArrayList(produtos);
        tabelaProdutos.setItems(observableProdutos);
    }
}
