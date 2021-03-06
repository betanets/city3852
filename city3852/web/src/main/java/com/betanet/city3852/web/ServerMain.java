/*
 * The MIT License
 *
 * Copyright 2017 Alexander Shkirkov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.betanet.city3852.web;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Server runner.
 * 
 * @author shkirkov.au
 */
@Slf4j
public class ServerMain {
    private static final String XML_CONFIG_FILES_LOCATION = "classpath:/spring/servlet-context.xml";
    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    private static final int DEFAULT_SERVER_PORT = 12112;
    private static final int SESSION_MAX_INACTIVE_INTERVAL = 3600;

    private int serverPort = DEFAULT_SERVER_PORT;

    public static void main(String[] args) throws Exception {
        new ServerMain().startServer();
    }

    private void startServer() throws Exception {
        initConfiguration();
        System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        Server server = new Server(serverPort);
        log.info("City3852 server started at port " + serverPort);
        server.setHandler(getServletContextHandler(getContext()));
        server.start();
        server.join();
    }

    private void initConfiguration() {
        //TODO: server port configuration
    }

    private Handler getServletContextHandler(WebApplicationContext applicationContext) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath(CONTEXT_PATH);
        //addSecurityFilter(contextHandler);
        DispatcherServlet servlet = new DispatcherServlet(applicationContext);
        servlet.setThrowExceptionIfNoHandlerFound(true);
        contextHandler.addServlet(new ServletHolder(servlet), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(applicationContext));
        contextHandler.getSessionHandler().setMaxInactiveInterval(SESSION_MAX_INACTIVE_INTERVAL);
        return contextHandler;
    }

    private WebApplicationContext getContext() {
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocation(XML_CONFIG_FILES_LOCATION);
        return context;
    }
    
    //TODO: security filters (if needed)
}
