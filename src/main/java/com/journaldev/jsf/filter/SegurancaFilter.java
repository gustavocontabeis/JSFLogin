package com.journaldev.jsf.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.journaldev.jsf.beans.Usuario;

@WebFilter(filterName = "SegurancaFilter", urlPatterns = {"*.xhtml"})
public class SegurancaFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SegurancaFilter.class.getSimpleName());
	
	public static final String USER = "usuario";
	private Map<String, String[]> paginasPrivadas = new HashMap<>();
	
    private static final String FACES_LOGIN_XHTML = "/faces/login.xhtml";
    private static final String URL_PATTERN = "/faces/";

    public SegurancaFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        paginasPrivadas.put("admin.xhtml", new String[]{"admin"});
        paginasPrivadas.put("client.xhtml", new String[]{"client"});
        paginasPrivadas.put("user.xhtml", new String[]{"user"});
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	LOGGER.debug("---");
        try {

            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            HttpSession session = request.getSession(false);

            String requestURI = request.getRequestURI();
            
            LOGGER.debug("requestURI: {}", requestURI);
            
            if (requestURI.indexOf("/public/") >= 0
                    || requestURI.contains("javax.faces.resource")) {
            	LOGGER.debug("É público ou resource.");
                chain.doFilter(req, res);
            }

            //verifica se é privada e se esta logado e se tem permissao.
            String page = getPage(request);
            
            if (paginasPrivadas.containsKey(page)) {
                LOGGER.debug("É uma página privada.");
                Usuario usuario = (Usuario) getUser(session);
                if (usuario != null) {
                	LOGGER.debug("{} está logado.", usuario.getNome());
                    String[] perfis = paginasPrivadas.get(page);
                    if (usuario.contemPerfil(perfis)) {
                        LOGGER.debug("OK. {} autorizado a acessar {}", usuario.getNome(), requestURI);
                        chain.doFilter(req, res);
                    } else {
                    	LOGGER.debug("Usuário {} não autorizado a acessar {}", usuario.getNome(), requestURI!=null?requestURI:"");
                    	//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário ou senha inválidos."));
                        redirecionarLogin(request, response);
                    }
                } else {
                	LOGGER.debug("Nenhum usuário logado. Você será redirecionado para o login.");
                	redirecionarLogin(request, response);
                }
            }else{
            	LOGGER.debug("É uma página pública.");
            	chain.doFilter(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private String getPage(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return requestURI.replace(request.getContextPath(), "").replace(URL_PATTERN, "").replaceAll(";jsessionid=.*.?", "");
	}
	
    private static Object getUser(HttpSession session) {
        if(session == null){
            return null;
        }
        return session.getAttribute(USER);
    }

    private void redirecionarLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.debug("Redirecionado para login com destino a \"{}\".", request.getRequestURI());
        request.getSession(false).setAttribute("destino", request.getRequestURI());
        response.sendRedirect(request.getContextPath() + FACES_LOGIN_XHTML);
    }

    @Override
    public void destroy() {

    }
}
