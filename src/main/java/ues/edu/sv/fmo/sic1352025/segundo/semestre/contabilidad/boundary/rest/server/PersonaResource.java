package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PersonaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Persona;

@Path("personas")
public class PersonaResource {

    @Inject
    PersonaBean personaBean;

    /**
     * GET /personas - Obtener todas las personas
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        try {
            List<Persona> listPersonas = personaBean.findAll();
            if (listPersonas == null) {
                return Response.serverError().build();
            }
            return Response.ok(listPersonas).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /personas/{id} - Obtener persona por ID
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") String id) {
        try {
            UUID idPersona = UUID.fromString(id);
            Persona persona = personaBean.findById(idPersona);
            if (persona == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Persona no encontrada")
                        .build();
            }
            return Response.ok(persona).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * POST /personas - Crear nueva persona
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(Persona persona, @Context UriInfo uriInfo) {
        try {
            if (persona == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datos de persona inválidos")
                        .build();
            }

            UUID idPersona = UUID.randomUUID();
            persona.setIdPersona(idPersona);
            personaBean.persistEntity(persona);

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idPersona.toString());
            return Response.created(uriBuilder.build()).entity(persona).build();
        } catch (Exception ex) {
            return Response.serverError().entity("Error al crear persona: " + ex.getMessage()).build();
        }
    }

    /**
     * PUT /personas/{id} - Actualizar persona
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") String id, Persona persona) {
        try {
            UUID idPersona = UUID.fromString(id);
            Persona personaExistente = personaBean.findById(idPersona);
            
            if (personaExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Persona no encontrada")
                        .build();
            }

            if (persona.getNombre() != null) {
                personaExistente.setNombre(persona.getNombre());
            }
            if (persona.getDocumentoIdentidad() != null) {
                personaExistente.setDocumentoIdentidad(persona.getDocumentoIdentidad());
            }
            if (persona.getFotografía() != null) {
                personaExistente.setFotografía(persona.getFotografía());
            }

            personaBean.updateEntity(personaExistente);
            return Response.ok(personaExistente).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * DELETE /personas/{id} - Eliminar persona
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") String id) {
        try {
            UUID idPersona = UUID.fromString(id);
            Persona persona = personaBean.findById(idPersona);
            
            if (persona == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Persona no encontrada")
                        .build();
            }

            personaBean.deleteEntity(persona);
            return Response.ok()
                    .entity("Persona eliminada exitosamente")
                    .build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }
}
