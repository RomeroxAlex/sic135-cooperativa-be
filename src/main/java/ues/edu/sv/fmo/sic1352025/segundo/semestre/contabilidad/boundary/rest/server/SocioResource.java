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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.SocioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Persona;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Socio;

@Path("socios")
public class SocioResource {

    @Inject
    SocioBean socioBean;

    @Inject
    PersonaBean personaBean;

    /**
     * GET /socios - Obtener todos los socios
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        try {
            List<Socio> listSocios = socioBean.findAll();
            if (listSocios == null) {
                return Response.serverError().build();
            }
            return Response.ok(listSocios).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /socios/{id} - Obtener socio por ID
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") String id) {
        try {
            UUID idSocio = UUID.fromString(id);
            Socio socio = socioBean.findById(idSocio);
            if (socio == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Socio no encontrado")
                        .build();
            }
            return Response.ok(socio).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /socios/buscar/{dui} - Buscar socio por DUI
     */
    @GET
    @Path("buscar/{dui}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByDui(@PathParam("dui") String dui) {
        try {
            List<Socio> socios = socioBean.findAll();
            Socio socioEncontrado = socios.stream()
                    .filter(s -> s.getIdPersona() != null 
                            && dui.equals(s.getIdPersona().getDocumentoIdentidad()))
                    .findFirst()
                    .orElse(null);
            
            if (socioEncontrado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Socio no encontrado con DUI: " + dui)
                        .build();
            }
            return Response.ok(socioEncontrado).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * POST /socios - Crear nuevo socio
     * Espera un objeto con: { documentoFiscal, idPersona: { nombre, documentoIdentidad, fotografia } }
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(Socio socio, @Context UriInfo uriInfo) {
        try {
            // Validar datos
            if (socio == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datos de socio inválidos")
                        .build();
            }

            // Crear o buscar persona
            Persona persona = socio.getIdPersona();
            if (persona != null) {
                // Si viene con ID, buscar la persona existente
                if (persona.getIdPersona() != null) {
                    Persona personaExistente = personaBean.findById(persona.getIdPersona());
                    if (personaExistente != null) {
                        persona = personaExistente;
                    } else {
                        // Si no existe, crear nueva persona
                        persona.setIdPersona(UUID.randomUUID());
                        personaBean.persistEntity(persona);
                    }
                } else {
                    // Crear nueva persona
                    persona.setIdPersona(UUID.randomUUID());
                    personaBean.persistEntity(persona);
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Debe proporcionar información de la persona")
                        .build();
            }

            // Crear socio
            UUID idSocio = UUID.randomUUID();
            socio.setIdSocio(idSocio);
            socio.setIdPersona(persona);
            socioBean.persistEntity(socio);

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idSocio.toString());
            return Response.created(uriBuilder.build()).entity(socio).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al crear socio: " + ex.getMessage()).build();
        }
    }

    /**
     * PUT /socios/{id} - Actualizar socio
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") String id, Socio socio) {
        try {
            UUID idSocio = UUID.fromString(id);
            Socio socioExistente = socioBean.findById(idSocio);
            
            if (socioExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Socio no encontrado")
                        .build();
            }

            // Actualizar campos
            if (socio.getDocumentoFiscal() != null) {
                socioExistente.setDocumentoFiscal(socio.getDocumentoFiscal());
            }

            // Actualizar persona si viene
            if (socio.getIdPersona() != null) {
                Persona personaExistente = socioExistente.getIdPersona();
                Persona personaNueva = socio.getIdPersona();
                
                if (personaExistente != null) {
                    if (personaNueva.getNombre() != null) {
                        personaExistente.setNombre(personaNueva.getNombre());
                    }
                    if (personaNueva.getDocumentoIdentidad() != null) {
                        personaExistente.setDocumentoIdentidad(personaNueva.getDocumentoIdentidad());
                    }
                    if (personaNueva.getFotografía() != null) {
                        personaExistente.setFotografía(personaNueva.getFotografía());
                    }
                    personaBean.updateEntity(personaExistente);
                }
            }

            socioBean.updateEntity(socioExistente);
            return Response.ok(socioExistente).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * DELETE /socios/{id} - Eliminar socio
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") String id) {
        try {
            UUID idSocio = UUID.fromString(id);
            Socio socio = socioBean.findById(idSocio);
            
            if (socio == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Socio no encontrado")
                        .build();
            }

            socioBean.deleteEntity(socio);
            return Response.ok()
                    .entity("Socio eliminado exitosamente")
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
