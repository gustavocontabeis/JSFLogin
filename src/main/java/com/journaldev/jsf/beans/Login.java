package com.journaldev.jsf.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.journaldev.jsf.dao.LoginDAO;
import com.journaldev.jsf.filter.SegurancaFilter;
import com.journaldev.jsf.util.SessionUtils;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(Login.class.getSimpleName());

	private String pwd;
	private String msg;
	private String user;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	// validate login
	public void validateUsernamePassword(ActionEvent evt) {
		try {
			Usuario usuario = LoginDAO.login(user, pwd);
			LOGGER.debug("Usuário logado? {}", usuario != null ? "Sim" : "Não");
			if (usuario != null) {
				HttpSession session = SessionUtils.getSession();
				session.setAttribute("usuario", usuario);
				LOGGER.debug("Usuário {} adicionado a sessão", usuario.getNome());
				Object destinoObj = session.getAttribute("destino");
				session.removeAttribute("destino");
				LOGGER.debug("Destino? {}", destinoObj);
				if (destinoObj == null) {
					destinoObj = "/JSFLogin/";
					LOGGER.debug("Você será encaminhado para a home");
				}
				LOGGER.debug("Voce foi logado com sucesso e será encaminhado ao seu destino {}", destinoObj);
				FacesContext.getCurrentInstance().getExternalContext().redirect(destinoObj.toString());
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Usuário ou senha incorretos", "Por favor coloque seu usuário de senha correto."));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// logout event, invalidate session
	public String logout() {
		LOGGER.debug("Fim da sessão.");
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "index";
	}
}
