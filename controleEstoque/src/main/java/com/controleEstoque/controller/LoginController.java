package com.controleEstoque.controller;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.controleEstoque.db.DB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    //Anotações dos componenets Javafx
    @FXML
    private TextField nomeField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private Button loginButton;
    @FXML

    //Esse metodo e chamado pelo javaFX quando o palco e iniciado, estabelece as acoes dos botoes
    public void initialize() {
        loginButton.setOnAction(event -> {
            String nome = nomeField.getText();
            String senha = senhaField.getText();

            loginTrue(event, nome, senha);
        });
    }

    //chama o metodo autenticacao, e se ele for True ele chama o palco do sistema
    private void loginTrue(ActionEvent event, String nome, String senha) {
        boolean autenticado = autenticacao(nome, senha);
        if (autenticado) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Controle.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Usuario ou senha incorretas.");
            alert.showAndWait();
        }
    }

    //esse metodo se certifica se o login e senha digitados batem com o banco de dados
    public static boolean autenticacao(String nome, String senha) {
        PreparedStatement st = null;
        java.sql.Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "SELECT * FROM usuario WHERE nome = ? AND senha = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, nome);
            st.setString(2, senha);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao autenticar o usuário: " + e.getMessage());
            alert.showAndWait();
            return false;
        } finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }
}
