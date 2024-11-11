package com.controleEstoque.model;
import com.controleEstoque.db.DB;
import javafx.scene.control.Alert;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Historico {
    public static void createHistorico(String nome, double preco, int quantidade, int produto_id) {
        PreparedStatement st = null;
        java.sql.Connection conn = null; // Declare a conexão aqui
        try {
            conn = DB.getConnection(); // Obtém a conexão
            st = conn.prepareStatement(
                    "INSERT INTO historico (Nome, Preco, Quantidade, Produto_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, nome);
            st.setDouble(2, preco);
            st.setInt(3, quantidade);
            st.setInt(4, produto_id);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Alteração registrada ID = " + id);
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
