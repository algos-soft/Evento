#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

#parse("Header.java")

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import it.algos.web.servlet.AlgosServlet;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet 3.0 introduces a @WebServlet annotation which can be used to replace the traditional web.xml.
 * <p>
 * The straightforward approach to create a Vaadin application using servlet 3.0 annotations,
 * is to simply move whatever is in web.xml to a custom servlet class (extends VaadinServlet)
 * and annotate it using @WebServlet and add @WebInitParams as needed.
 * <p><
 * Vaadin 7.1 introduces two features which makes this a lot easier, @VaadinServletConfiguration
 * and automatic UI finding.
 * VaadinServletConfiguration is a type safe, Vaadin version of @WebInitParam
 * which provides you with the option to select UI by referring the UI class
 * directly toggle productionMode using a boolean and more
 */
@WebServlet(value = "/*", asyncSupported = true, displayName = "${NAME}")
@VaadinServletConfiguration(productionMode = false, ui = ${NAME}UI.class)
public class ${NAME}Servlet extends AlgosServlet {

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        super.sessionInit(event);

        // Do session start stuff here

    }// end of method

}// end of class
