package com.journaldev.jsf.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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

import com.journaldev.jsf.beans.Usuario;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter {
	
	private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class.getSimpleName());
	
	public static final String USER = 				"username";
	private static final String REDIRECIONAR_PARA = "redirecionar_para";
	private static final String URL_PATTERN = 		"/faces/";
    
    Map<String, String[]>paginasPrivadas = new HashMap<>();

	public AuthorizationFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
            paginasPrivadas.put("admin.xhtml", new String[]{"admin"});
            paginasPrivadas.put("client.xhtml", new String[]{"client"});
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		try {

			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			HttpSession session = request.getSession(false);

			String reqURI = request.getRequestURI();
                        
			if(reqURI.indexOf("/public/") >= 0
					|| reqURI.contains("javax.faces.resource")){
				chain.doFilter(req, res);
			}
            
            //verifica se é privada e se esta logado e se tem permissao.
            String page = reqURI.replace(request.getContextPath(), "").replace(URL_PATTERN, ""); 
            
            if(paginasPrivadas.containsKey(page)){
            	if((session != null && session.getAttribute(USER) != null)){
            		Usuario usuario = (Usuario) session.getAttribute(USER); 
            		String[] perfis = paginasPrivadas.get(reqURI);
            		if(!usuario.contemPerfil(perfis)){
            			redirecionarLogin(request, response);
            		}else{
            			String redirecionarPara = (String) session.getAttribute(REDIRECIONAR_PARA);
            			response.sendRedirect(request.getContextPath() + URL_PATTERN+redirecionarPara);
            		}
            	}else{
            		redirecionarLogin(request, response);
            	}
            }
            chain.doFilter(req, res);
                        
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void redirecionarLogin(HttpServletRequest reqt, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(reqt.getContextPath() + "/faces/login.xhtml");
	}

	@Override
	public void destroy() {

	}
}