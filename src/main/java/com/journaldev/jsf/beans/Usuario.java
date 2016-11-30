package com.journaldev.jsf.beans;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String senha;
    private String[] perfis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String[] getPerfis() {
        return perfis;
    }

    public void setPerfis(String[] perfis) {
        this.perfis = perfis;
    }

    public boolean contemPerfil(String[] perfis) {
    	if(this.perfis!=null && perfis != null){
    		for (String perfil : this.perfis) {
    			for (String perfil2 : perfis) {
    				if(perfil.equals(perfil2)){
    					return true;
    				}
    			}
    		}
    	}
        return false;
    }

}
