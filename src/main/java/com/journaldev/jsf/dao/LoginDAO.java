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
            ps = con.prepareStatement("Select uid, uname, password, roles from Users where uname = ? and password = ?");
            
            ps.setString(1, user);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("uid"));
                usuario.setNome(rs.getString("uname"));
                usuario.setSenha(rs.getString("password"));
                usuario.setPerfis(rs.getString("roles").split(","));
                return usuario;
            }
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Login error -->" + ex.getMessage());
            return null;
        } finally {
            DataConnect.close(con);
        }
        return null;
    }

	public static void execute(String sql) {
		Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException ex) {
        	ex.printStackTrace();
        } finally {
            DataConnect.close(con);
        }	
	}
}
