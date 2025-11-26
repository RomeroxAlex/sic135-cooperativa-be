package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CabeceraTransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CreditoBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.SocioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.TransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CabeceraTransaccion;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Credito;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaBancaria;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Socio;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Transaccion;

@Path("reportes")
public class ReportesResource {

    @Inject
    TransaccionBean transaccionBean;

    @Inject
    CuentaContableBean cuentaContableBean;

    @Inject
    CabeceraTransaccionBean cabeceraTransaccionBean;

    @Inject
    CuentaBancariaBean cuentaBancariaBean;

    @Inject
    SocioBean socioBean;

    @Inject
    CreditoBean creditoBean;

    /**
     * GET /reportes/balance-general - Balance General
     * Parámetros opcionales: fecha
     */
    @GET
    @Path("balance-general")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBalanceGeneral(@QueryParam("fecha") String fecha) {
        try {
            // Por ahora retornamos las cuentas contables agrupadas
            List<CuentaContable> cuentas = cuentaContableBean.findAll();
            
            Map<String, Object> balanceGeneral = new HashMap<>();
            balanceGeneral.put("fecha", fecha != null ? fecha : "Al día de hoy");
            balanceGeneral.put("cuentas", cuentas);
            balanceGeneral.put("totalCuentas", cuentas.size());

            return Response.ok(balanceGeneral).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al generar balance general: " + ex.getMessage())
                    .build();
        }
    }

    /**
     * GET /reportes/estado-resultados - Estado de Resultados
     * Parámetros: inicio, fin
     */
    @GET
    @Path("estado-resultados")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEstadoResultados(
            @QueryParam("inicio") String fechaInicio,
            @QueryParam("fin") String fechaFin) {
        try {
            List<Transaccion> transacciones = transaccionBean.findAll();
            
            Map<String, Object> estadoResultados = new HashMap<>();
            estadoResultados.put("fechaInicio", fechaInicio);
            estadoResultados.put("fechaFin", fechaFin);
            estadoResultados.put("transacciones", transacciones);
            
            // Calcular totales básicos
            double totalIngresos = 0.0;
            double totalEgresos = 0.0;
            for (Transaccion t : transacciones) {
                if (t.getMonto() != null) {
                    double monto = t.getMonto().doubleValue();
                    if (monto > 0) {
                        totalIngresos += monto;
                    } else {
                        totalEgresos += Math.abs(monto);
                    }
                }
            }
            
            estadoResultados.put("totalIngresos", totalIngresos);
            estadoResultados.put("totalEgresos", totalEgresos);
            estadoResultados.put("utilidadNeta", totalIngresos - totalEgresos);

            return Response.ok(estadoResultados).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al generar estado de resultados: " + ex.getMessage())
                    .build();
        }
    }

    /**
     * GET /reportes/libro-diario - Libro Diario
     * Parámetros: inicio, fin
     */
    @GET
    @Path("libro-diario")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getLibroDiario(
            @QueryParam("inicio") String fechaInicio,
            @QueryParam("fin") String fechaFin) {
        try {
            List<CabeceraTransaccion> cabeceras = cabeceraTransaccionBean.findAll();
            
            Map<String, Object> libroDiario = new HashMap<>();
            libroDiario.put("fechaInicio", fechaInicio);
            libroDiario.put("fechaFin", fechaFin);
            libroDiario.put("asientos", cabeceras);
            libroDiario.put("totalAsientos", cabeceras.size());

            return Response.ok(libroDiario).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al generar libro diario: " + ex.getMessage())
                    .build();
        }
    }

    /**
     * GET /reportes/libro-mayor - Libro Mayor
     * Parámetros: inicio, fin, cuenta (opcional)
     */
    @GET
    @Path("libro-mayor")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getLibroMayor(
            @QueryParam("inicio") String fechaInicio,
            @QueryParam("fin") String fechaFin,
            @QueryParam("cuenta") String idCuenta) {
        try {
            List<CuentaContable> cuentas = cuentaContableBean.findAll();
            
            Map<String, Object> libroMayor = new HashMap<>();
            libroMayor.put("fechaInicio", fechaInicio);
            libroMayor.put("fechaFin", fechaFin);
            libroMayor.put("cuentas", cuentas);

            return Response.ok(libroMayor).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al generar libro mayor: " + ex.getMessage())
                    .build();
        }
    }

    /**
     * GET /reportes/dashboard - Dashboard con métricas generales
     */
    @GET
    @Path("dashboard")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDashboard() {
        try {
            List<Socio> socios = socioBean.findAll();
            List<CuentaBancaria> cuentasBancarias = cuentaBancariaBean.findAll();
            List<Credito> creditos = creditoBean.findAll();
            List<Transaccion> transacciones = transaccionBean.findAll();

            // Calcular totales
            double totalCuentasBancarias = cuentasBancarias.stream()
                    .filter(c -> c.getSaldo() != null)
                    .mapToDouble(c -> c.getSaldo().doubleValue())
                    .sum();

            double totalCreditos = creditos.stream()
                    .filter(c -> c.getMonto() != null)
                    .mapToDouble(c -> c.getMonto().doubleValue())
                    .sum();

            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("totalSocios", socios.size());
            dashboard.put("totalCuentasBancarias", cuentasBancarias.size());
            dashboard.put("saldoTotalCuentasBancarias", totalCuentasBancarias);
            dashboard.put("totalCreditos", creditos.size());
            dashboard.put("montoTotalCreditos", totalCreditos);
            dashboard.put("totalTransacciones", transacciones.size());

            return Response.ok(dashboard).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al generar dashboard: " + ex.getMessage())
                    .build();
        }
    }

    /**
     * GET /reportes/flujo-efectivo - Flujo de efectivo
     */
    @GET
    @Path("flujo-efectivo")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getFlujoEfectivo(
            @QueryParam("inicio") String fechaInicio,
            @QueryParam("fin") String fechaFin) {
        try {
            List<Transaccion> transacciones = transaccionBean.findAll();
            
            Map<String, Object> flujoEfectivo = new HashMap<>();
            flujoEfectivo.put("fechaInicio", fechaInicio);
            flujoEfectivo.put("fechaFin", fechaFin);
            flujoEfectivo.put("movimientos", transacciones);

            return Response.ok(flujoEfectivo).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al generar flujo de efectivo: " + ex.getMessage())
                    .build();
        }
    }

    /**
     * GET /reportes/cartera-creditos - Cartera de créditos
     */
    @GET
    @Path("cartera-creditos")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCarteraCreditos() {
        try {
            List<Credito> creditos = creditoBean.findAll();
            
            long creditosActivos = creditos.stream()
                    .filter(c -> c.getActivo() != null && c.getActivo())
                    .count();

            double montoTotal = creditos.stream()
                    .filter(c -> c.getMonto() != null)
                    .mapToDouble(c -> c.getMonto().doubleValue())
                    .sum();

            Map<String, Object> carteraCreditos = new HashMap<>();
            carteraCreditos.put("totalCreditos", creditos.size());
            carteraCreditos.put("creditosActivos", creditosActivos);
            carteraCreditos.put("montoTotal", montoTotal);
            carteraCreditos.put("creditos", creditos);

            return Response.ok(carteraCreditos).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al generar cartera de créditos: " + ex.getMessage())
                    .build();
        }
    }
}
