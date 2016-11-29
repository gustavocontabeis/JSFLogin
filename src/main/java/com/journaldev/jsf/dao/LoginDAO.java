package com.journaldev.jsf.dao;

import com.journaldev.jsf.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.journaldev.jsf.util.DataConnect;

public class LoginDAO {
    
    public static Usuario login(String user, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("Select uname, password, roles from Users where uname = ? and password = ?");
            
            ps.setString(1, user);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong(0));
                usuario.setNome(rs.getNString(1));
                usuario.setSenha(rs.getNString(2));
                usuario.setPerfis(rs.getNString(3).split(","));
                return usuario;
            }
        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
            return null;
        } finally {
            DataConnect.close(con);
        }
        return null;
    }
}
