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

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthorizationFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class.getSimpleName());

    public static final String USER = "usuario";
    private static final String REDIRECIONAR_PARA = "redirecionar_para";
    private static final String URL_PATTERN = "/faces/";

    Map<String, String[]> paginasPrivadas = new HashMap<>();

    public AuthorizationFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        paginasPrivadas.put("admin.xhtml", new String[]{"admin"});
        paginasPrivadas.put("client.xhtml", new String[]{"client"});
        paginasPrivadas.put("user.xhtml", new String[]{"user"});
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {

            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            HttpSession session = request.getSession(false);

            String requestURI = request.getRequestURI();
            LOGGER.info("requestURI: " + requestURI);
            if (requestURI.indexOf("/public/") >= 0
                    || requestURI.contains("javax.faces.resource")) {
                chain.doFilter(req, res);
            }

            //verifica se é privada e se esta logado e se tem permissao.
            String page = requestURI.replace(request.getContextPath(), "").replace(URL_PATTERN, "");
            LOGGER.info("page: " + page);
            LOGGER.info("user?: " + (getUser(session) != null));
            
            if (paginasPrivadas.containsKey(page)) {
                if (getUser(session) != null) {
                    Usuario usuario = (Usuario) getUser(session);
                    String[] perfis = paginasPrivadas.get(requestURI);
                    if (!usuario.contemPerfil(perfis)) {
                        redirecionarLogin(request, response);
                    } else {
                        String redirecionarPara = (String) session.getAttribute(REDIRECIONAR_PARA);
                        final String name = request.getContextPath() + URL_PATTERN + redirecionarPara;
                        response.sendRedirect(name);
                        LOGGER.info("redirecionado para "+name);
                    }
                } else {
                    redirecionarLogin(request, response);
                }
            }
            chain.doFilter(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private static Object getUser(HttpSession session) {
        if(session == null){
            return null;
        }
        return session.getAttribute(USER);
    }

    private void redirecionarLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("redirecionado para login.");
        response.sendRedirect(request.getContextPath() + "/faces/login.xhtml");
    }

    @Override
    public void destroy() {

    }
}
