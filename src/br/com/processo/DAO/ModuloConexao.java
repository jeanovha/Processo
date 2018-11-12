/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.processo.DAO;

import java.sql.*;

/**
 *
 * @author Admin
 */
public class ModuloConexao {

    public static Connection conector() {

        java.sql.Connection conexao = null;

        String driver = "com.mysql.jdbc.Driver";

        String url = "jdbc:mysql://localhost:3306/db_processo";
        String user = "root";
        String senha = "1234";
        
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, senha);
            return conexao;
        } catch (Exception e) {
            System.out.println("Erro na conexão do banco de dados");
            System.out.println(e);
            return null;
        }
    }

}
