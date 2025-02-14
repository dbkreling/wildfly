/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2021, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.jaxrs.logging;

import static org.jboss.logging.Logger.Level.ERROR;
import static org.jboss.logging.Logger.Level.INFO;
import static org.jboss.logging.Logger.Level.WARN;

import java.util.List;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.jaxrs.deployment.JaxrsSpringProcessor;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.dmr.ModelNode;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@SuppressWarnings("deprecation")
@MessageLogger(projectCode = "WFLYRS", length = 4)
public interface JaxrsLogger extends BasicLogger {

    /**
     * A logger with the category {@code org.jboss.jaxrs}.
     */
    JaxrsLogger JAXRS_LOGGER = Logger.getMessageLogger(JaxrsLogger.class, "org.jboss.as.jaxrs");

    /**
     * Logs a warning message indicating the annotation, represented by the {@code annotation} parameter, not on Class,
     * represented by the {@code target} parameter.
     *
     * @param annotation the missing annotation.
     * @param target     the target.
     */
    @LogMessage(level = WARN)
    @Message(id = 1, value = "%s annotation not on Class: %s")
    void classAnnotationNotFound(String annotation, AnnotationTarget target);

    /**
     * Logs a warning message indicating the annotation, represented by the {@code annotation} parameter, not on Class
     * or Method, represented by the {@code target} parameter.
     *
     * @param annotation the missing annotation.
     * @param target     the target.
     */
    @LogMessage(level = WARN)
    @Message(id = 2, value = "%s annotation not on Class or Method: %s")
    void classOrMethodAnnotationNotFound(String annotation, AnnotationTarget target);

    /**
     * Logs an error message indicating more than one mapping found for Jakarta RESTful Web Services servlet, represented by the
     * {@code servletName} parameter, the second mapping, represented by the {@code pattern} parameter, will not work.
     *
     * @param servletName the name of the servlet.
     * @param pattern     the pattern.
     */
    @LogMessage(level = ERROR)
    @Message(id = 3, value = "More than one mapping found for Jakarta RESTful Web Services servlet: %s the second mapping %s will not work")
    void moreThanOneServletMapping(String servletName, String pattern);

//    /**
//     * Logs a warning message indicating no servlet mappings found for the Jakarta RESTful Web Services application, represented by the
//     * {@code servletName} parameter, either annotate with {@link jakarta.ws.rs.ApplicationPath @ApplicationPath} or add
//     * a {@code servlet-mapping} in the web.xml.
//     *
//     * @param servletName the servlet name.
//     */
//    @LogMessage(level = WARN)
//    @Message(id = 4, value = "No Servlet mappings found for Jakarta RESTful Web Services application: %s either annotate it with @ApplicationPath or add a servlet-mapping in web.xml")
//    void noServletMappingFound(String servletName);
//
//    /**
//     * Logs a warning message indicating that {@code resteasy.scan} was found in the {@code web.xml} and is not
//     * necessary.
//     */
//    @LogMessage(level = WARN)
//    @Message(id = 5, value = "%s found and ignored in web.xml. This is not necessary, as Resteasy will use the container integration in the JAX-RS 1.1 specification in section 2.3.2")
//    void resteasyScanWarning(String param);

    /**
     * Creates an exception indicating the Jakarta RESTful Web Services application class could not be loaded.
     *
     * @param cause the cause of the error.
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 6, value = "Could not load Jakarta RESTful Web Services Application class")
    DeploymentUnitProcessingException cannotLoadApplicationClass(@Cause Throwable cause);

//    /**
//     * Creates an exception indicating more than one application class found in deployment.
//     *
//     * @param app1 the first application.
//     * @param app2 the second application.
//     * @return a {@link DeploymentUnitProcessingException} for the error.
//     */
//    @Message(id = 7, value = "More than one Application class found in deployment %s and %s")
//    DeploymentUnitProcessingException moreThanOneApplicationClassFound(Class<? extends Application> app1, Class<? extends Application> app2);
//
//    /**
//     * A message indicating only one Jakarta RESTful Web Services application class is allowed.
//     *
//     * @param sb a builder with application classes.
//     * @return the message.
//     */
//    @Message(id = 8, value = "Only one Jakarta RESTful Web Services Application Class allowed. %s")
//    String onlyOneApplicationClassAllowed(StringBuilder sb);
//
//    /**
//     * A message indicating the incorrect mapping config.
//     *
//     * @return the message.
//     */
//    @Message(id = 9, value = "Please use either @ApplicationPath or servlet mapping for url path config.")
//    String conflictUrlMapping();

    /**
     * Jakarta RESTful Web Services resource @Path annotation is on a class or interface that is not a view
     *
     *
     * @param type    The class with the annotation
     * @param ejbName The ejb
     * @return  the exception
     */
    @Message(id = 10, value = "Jakarta RESTful Web Services resource %s does not correspond to a view on the Jakarta Enterprise Beans %s. @Path annotations can only be placed on classes or interfaces that represent a local, remote or no-interface view of an Jakarta Enterprise Beans.")
    DeploymentUnitProcessingException typeNameNotAnEjbView(List<Class<?>> type, String ejbName);

    @Message(id = 11, value = "Invalid value for parameter %s: %s")
    DeploymentUnitProcessingException invalidParamValue(String param, String value);

    @Message(id = 12, value = "No spring integration jar found")
    DeploymentUnitProcessingException noSpringIntegrationJar();

    @LogMessage(level = WARN)
    @Message(id = 13, value = "The context param " + JaxrsSpringProcessor.DISABLE_PROPERTY + " is deprecated, and will be removed in a future release. Please use " + JaxrsSpringProcessor.ENABLE_PROPERTY + " instead")
    void disablePropertyDeprecated();

    @LogMessage(level = ERROR)
    @Message(id = 14, value = "Failed to register management view for REST resource class: %s")
    void failedToRegisterManagementViewForRESTResources(String resClass, @Cause Exception e);

    @LogMessage(level = WARN)
    @Message(id = 15, value = "No Servlet declaration found for Jakarta RESTful Web Services application.  In %s either provide a class that extends jakarta.ws.rs.core.Application or declare a servlet class in web.xml.")
    void noServletDeclaration(String archiveName);

    @LogMessage(level = INFO)
    @Message(id = 16, value = "RESTEasy version %s")
    void resteasyVersion(String version);

    @LogMessage(level = WARN)
    @Message(id = 17, value = "Failed to read attribute from Jakarta RESTful Web Services deployment at %s with name %s")
    void failedToReadAttribute(@Cause Exception ex, PathAddress address, ModelNode modelNode);

    @LogMessage(level = WARN)
    @Message(id = 18, value = "Explicit usage of Jackson annotation in a Jakarta RESTful Web Services deployment; the system will disable Jakarta JSON Binding processing for the current deployment. Consider setting the '%s' property to 'false' to restore Jakarta JSON Binding.")
    void jacksonAnnotationDetected(String property);

    @LogMessage(level = WARN)
    @Message(id = 19, value = "Error converting default value %s for parameter %s in method %s using param converter %s. Exception: %s : %s")
    void paramConverterFailed(String defaultValue, String param, String method,
                                String paramConverter, String exceptionClass,
                                String exceptionMsg);

    @LogMessage(level = WARN)
    @Message(id = 20, value = "\"Error converting default value %s for parameter %s in method %s using method %s. Exception: %s : %s\"")
    void baseTypeMethodFailed(String defaultValue, String param, String method,
                              String converterMethod, String exceptionClass,
                              String exceptionMsg);

    @LogMessage(level = ERROR)
    @Message(id = 21, value = "%s %s")
    void classIntrospectionFailure(String clazz, String msg);

    @Message(id = 22, value = "\"Parameter %s is not a list\"")
    OperationFailedException parameterNotList(String param);

    @Message(id = 23, value = "Illegal value for parameter %s: %s")
    String illegalArgument(String name, String value);

    @Message(id = 30, value = "Invalid ConfigurationFactory found %s")
    IllegalStateException invalidConfigurationFactory(Class<?> factory);

    @LogMessage(level = WARN)
    @Message(id = 31, value = "Failed to load RESTEasy MicroProfile Configuration: %s")
    void failedToLoadConfigurationFactory(String msg);

}
