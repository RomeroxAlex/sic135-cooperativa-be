/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

/**
 *
 * @author jordi
 */
@ApplicationPath("api/v1")
@OpenAPIDefinition(
    info = @Info(
        title = "API Contabilidad",
        version = "1.0.0",
        description = "API RESTful para el sistema de contabilidad - SIC135 Cooperativa",
        contact = @Contact(
            name = "UES FMO SIC135",
            email = "soporte@ues.edu.sv"
        ),
        license = @License(
            name = "MIT",
            url = "https://opensource.org/licenses/MIT"
        )
    )
)
public class ContabilidadApp extends Application{
    // http://localhost:9080/contabilidad/api/v1/

}
