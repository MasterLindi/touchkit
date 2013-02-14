package com.vaadin.addon.touchkit.server;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.addon.touchkit.settings.TouchKitSettings;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;

/**
 * This servlet should be used by TouchKit applications. It automatically
 * creates TouchKitSettings bound to {@link VaadinService} in
 * {@link #servletInitialized()} method. It should be overridden to configure
 * defaults suitable for this web application.
 * <p>
 * If TouchKit application cannot use this Servlet as super class, developers
 * should create and bound {@link TouchKitSettings} instance manually.
 */
public class TouchKitServlet extends VaadinServlet {

    private TouchKitSettings touchKitSettings;

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.server.VaadinServlet#servletInitialized()
     */
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        touchKitSettings = new TouchKitSettings(getService());
    }

    /**
     * @return TouchKitSettings bound to VaadinService related to this Servlet
     */
    public TouchKitSettings getTouchKitSettings() {
        return touchKitSettings;
    }

    @Override
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null
                && request.getPathInfo().startsWith(
                        "/VAADIN/themes/touchkit/styles.css")) {
            serveDummyFile(response);
        } else {
            super.service(request, response);
        }
    }

    protected void writeStaticResourceResponse(HttpServletRequest request,
            HttpServletResponse response, URL resourceUrl) throws IOException {
        String file = resourceUrl.getFile();
        if (file.endsWith(".manifest")) {
            response.setContentType("text/cache-manifest");
            response.setHeader("Cache-Control", "max-age=1, must-revalidate");
        }
        super.writeStaticResourceResponse(request, response, resourceUrl);
    }

    private void serveDummyFile(HttpServletResponse response)
            throws IOException {
        response.setContentType("text/css");
        response.setHeader("Cache-Control", "max-age=1000000");
        response.getOutputStream().write("\n".getBytes());
    }

}
