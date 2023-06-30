package com.coherent.webclient;

import com.coherent.common.models.Catalog;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductsServlet extends HttpServlet {
    private boolean connected;
    private int port;
    private String host;
    private ClientService clientService;

    public void init() {
        clientService = new ClientService();
        ServletConfig servletConfig = getServletConfig();
        connected = Boolean.parseBoolean(servletConfig.getInitParameter("initConnectStatus"));
        port = Integer.parseInt(servletConfig.getInitParameter("port"));
        host = servletConfig.getInitParameter("host");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("connected", connected);
        request.getRequestDispatcher("/WEB-INF/products.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean optionToConnect = Boolean.parseBoolean(request.getParameter("btnConnect"));
        if (optionToConnect) {
            clientService.connect(host, port);
            connected = true;
            request.setAttribute("connected", connected);
            try {
                Catalog catalog = clientService.getCatalog();
                request.setAttribute("catalog", catalog);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (!optionToConnect) {
            connected = false;
            request.setAttribute("connected", connected);
            clientService.closeConnection();
        }

        request.getRequestDispatcher("/WEB-INF/products.jsp").forward(request, response);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Catalog catalog;
        try {
            catalog = clientService.getCatalog();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("catalog", catalog);
    }
}
