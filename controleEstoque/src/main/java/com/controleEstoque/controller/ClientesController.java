package com.controleEstoque.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ClientesController {

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

    //Esse metodo e chamado pelo javaFX quando o palco e iniciado, estabelece as acoes dos botoes
    public void initialize() {
        controleEstoqueButton.setOnAction(event -> loadScreen("/view/Controle.fxml"));
        fornecedoresButton.setOnAction(event -> loadScreen("/view/Fornecedores.fxml"));
        historicoButton.setOnAction(event -> loadScreen("/view/Historico.fxml"));
        atualizarEstoqueButton.setOnAction(event -> loadScreen("/view/Atualizar.fxml"));
        cadastrarProdutoButton.setOnAction(event -> loadScreen("/view/Cadastrar.fxml"));
    }

    //estabelece o palco atual, ja que Stage nao pode ser nulo
    private void loadScreen(String fxmlFile) {
        Stage currentStage = (Stage) clientesButton.getScene().getWindow();
        new RouteController().loadScreen(currentStage, fxmlFile);
    }
}