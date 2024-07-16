package com.venturessoft.human.services

import com.venturessoft.human.models.response.EmpleadosPayResponse
import com.venturessoft.human.models.Response.DeleteAcountResponse
import com.venturessoft.human.models.Response.DetailsFreeStationResponse
import com.venturessoft.human.models.Response.FreeStationResponse
import com.venturessoft.human.models.Response.ListPhotoAuthResponse
import com.venturessoft.human.models.Response.PhotoAuthResponse
import com.venturessoft.human.models.request.*
import com.venturessoft.human.models.response.*
import retrofit2.Call
import retrofit2.http.*
interface ApiService {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.ALTAUSUARIOADMIN)
    fun getAltaUsuarioAdmin(
        @Body AltaUsuarioAdminRequest: AltaUsuarioAdminRequest,
        @Header("Authorization") token: String
    ): Call<AltaUsuarioAdminResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.ALTAUSUARIOADMIN)
    fun getDeleteUser(
        @Body eliminarUsuario: EliminarUsuario,
        @Header("Authorization") token: String
    ): Call<AltaUsuarioAdminResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.Token.TOKENNOLOGIN)
    fun getConsultaTokenNoLogin(
        @Body ConsultaTokeNoLogin: ConsultaTokeNoLoginRequest,
        @Query("token") token: String?
    ): Call<ConsultaTokeNoLoginResponse>
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST(URL.AdministrarUsuario.ACCESOUSUARIO)
    fun getUserAccess(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserAccesoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.RECUPERAR_PASWORD)
    fun getRecuperarPassword(
        @Body RecuperarPasswordRequest: RecuperarPasswordRequest,
        @Header("Authorization") token: String
    ): Call<RecuperarPasswordResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.ACTUALIZAPASSWORD)
    fun getActualizarPassword(
        @Body ActualizarPasswordRequest: ActualizarPasswordRequest,
        @Header("Authorization") token: String
    ): Call<ActualizarPasswordResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.ACTUALIZAUSUARIO)
    fun getActualizaUsuario(
        @Body ActualizarUsuarioRequest: ActualizarUsuarioRequest,
        @Header("Authorization")token: String
    ): Call<ActualizarUsuarioResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.BUSQUEDAUSUARIO)
    fun getBusquedaUsuario(
        @Body BusquedaUsuarioRequest: BusquedaUsuarioRequest,
        @Header("Authorization") token: String
    ): Call<BusquedaUsuarioResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.CONSULTAUSUARIO)
    fun getConsultaUsuario(
        @Body ConsultaUsuarioRequest: ConsultaUsuarioRequest,
        @Header("Authorization")token: String
    ): Call<ConsultaUsuarioResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.ALTAUSUARIOSUPERVISOR)
    fun getAltaUsuarioSupervisor(
        @Body altaUsuarioSupervisorRequest: AltaUsuarioSupervisorRequest,
        @Header("Authorization")token: String
    ): Call<AltaUsuarioSupervisorResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarUsuario.ELIMINAUSUARIO)
    fun getEliminaUsuarioSupervisor(
        @Body eliminaUsuarioRequest: EliminaUsuarioRequest,
        @Header("Authorization")token: String
    ): Call<EliminaUsuarioResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarReportes.CONSULTAESTADISTICAS)
    fun getStatisticalConsultation(
        @Body StatisticalConsultationRequest: ConsultaEstadisticaRequest,
        @Header("Authorization")token: String
    ): Call<ConsultaEstadisticaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarReportes.REPORTECHECADAS)
    fun getReporteChecadas(
        @Body reporteChecadasRequest: ReporteChecadasRequest,
        @Header("Authorization")token: String
    ): Call<ReporteChecadasResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarReportes.REPORTESOPCION)
    fun getReportesOpcion(
        @Body reporteOpcionRequest: ReporteOpcionRequest,
        @Header("Authorization")token: String
    ): Call<ReporteOpcionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarReportes.REPORTESENROL)
    fun getReportesEnroll(
        @Body reporteEnrollRequest: ReporteEnrollRequest,
        @Header("Authorization") token: String
    ): Call<ReporteEnrollResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministrarReportes.CONSULTAEMPLEADOSEST)
    fun getSaldo(
        @Header("Authorization") token: String,
        @Query("idCompania") idCompania: Long?
    ): Call<EmpleadosPayResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarReportes.REPORTESTATIONFREELITS)
    fun getReportCheckDetails(
        @Body request: ReporteStationFreeRequest,
        @Header("Authorization")token: String
    ): Call<FreeStationResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarReportes.REPORTESTATIONFREE)
    fun getCheckDetails(
        @Body request: DetailsEstacionesLibresRequest,
        @Header("Authorization")token: String
    ): Call<DetailsFreeStationResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarCompañia.ALTACOMPANIA)
    fun getAltaCompania(
        @Body AltaCompaniaRequest: AltaCompaniaRequest,
        @Header("Authorization") token: String
    ): Call<AltaCompaniaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT(URL.AdministrarCompañia.ACTUALIZACOMPANIA)
    fun getActualizaCompañia(
        @Path("idCompania") idCompania: Long ,
        @Body actualizaCompaniaRequest: ActualizaCompaniaRequest,
        @Header("Authorization")token: String
    ): Call<ActualizaCompaniaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministrarCompañia.CONSULTACOMPANIA)
    fun getConsultaCompañia(
        @Header("Authorization") token: String,
        @Query("idCompania") idCompania: Long?,
        @Query("idUsuario") idUsuario: Long?
    ): Call<ConsultaCompaniaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministrarCompañia.CONSULTACOMPANIADATA)
    fun getConsultaCompañiaData(
        @Header("Authorization") token: String,
        @Query("idCompania") idCompania: Long?,
        @Query("idUsuario") idUsuario: Long?
    ): Call<ConsultaCompaniaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarFacturacion.ALTAFACTURACION)
    fun getAltaFacturacion(
        @Body AltaFacturacionRequest: AltaFacturacionRequest,
        @Header("Authorization") token: String
    ): Call<AltaFacturacionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT(URL.AdministrarFacturacion.ACTUALIZAFACTURACION)
    fun getActualizaFacturacion(
        @Path("idFacturacion") idFacturacion: Int ,
        @Body actualizaFacturacionRequest: ActualizaFacturacionRequest,
        @Header("Authorization")token: String
    ): Call<ActualizaFacturacionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministrarFacturacion.CONSULTAFACTURACION)
    fun getConsultaFacturacion(
        @Query("idCia") idCia: Long,
        @Query("idFacturacion") idFacturacion: Int,
        @Header("Authorization") token: String
    ): Call<ConsultaFacturacionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministracionEmpleados.ALTAEMPLEADO)
    fun getAltaEmpleado(
        @Body altaEmpleadoRequest: AltaEmpleadoRequest,
        @Header("Authorization") token: String
    ): Call<AltaEmpleadoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministracionEmpleados.CONSULTAEMPLEADO)
    fun getConsultaEmpleado(
        @Body consultaEmpleadoRequest: ConsultaEmpleadoRequest,
        @Header("Authorization") token: String
    ): Call<ConsultaEmpleadoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("${URL.AdministracionEmpleados.ACTUALIZAEMPLEADO}/{idCia}/{idEmpleado}/{idUsuario}")
    fun putActualizaEmpleado(
        @Body actualizaEmpleadoRequest: ActualizaEmpleadoRequest,
        @Path("idCia") idCia:Long?,
        @Path("idEmpleado") idEmpleado:Long?,
        @Path("idUsuario") idUsuario:Long?,
        @Header("Authorization") token: String
    ): Call<ActualizaEmpleadoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministracionEmpleados.ELIMINAEMPLEADO)
    fun getEliminaEmpleado(
        @Body eliminaEmpleadoRequest: EliminaEmpleadoRequest,
        @Header("Authorization") token: String
    ): Call<EliminaEmpleadoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministracionEmpleados.BUSQUEDAEMPLEADO)
    fun getBusquedaEmpleado(
        @Body busquedaEmpleadoRequest: BusquedaEmpleadoRequest,
        @Header("Authorization") token: String
    ): Call<BusquedaEmpleadoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarZonas.ALTAZONA)
    fun getAltaZonas(
        @Body AltaZonaRequest: AltaZonaRequest,
        @Header("Authorization") token: String
    ): Call<AltaZonasResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarZonas.BUSQUEDAZONA)
    fun getBusquedaZonas(
        @Body busquedaZonaRequest: BusquedaZonaRequest,
        @Header("Authorization") token:String
    ): Call<BusquedaZonaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarZonas.BUSQUEDAZONA)
    fun getBusquedaZonasAdmi(
        @Body busquedaZonaRequest: BusquedaZonaRequest,
        @Header("Authorization") token:String
    ): Call<BusquedaZonaAdmiResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @HTTP(method = "DELETE", path = "AdministraZona/Desvinculado/Elimina",hasBody = true)
    fun getEliminaZonas(
        @Body eliminaZonaRequest: EliminaZonaRequest,
        @Header("Authorization") token: String,
        @Query("idUsuario") idUsuario: Long,
        @Query("idZona") idZona: Int
    ): Call<EliminaZonaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarZonas.CONSULTAZONA)
    fun getConsultaZona(
        @Body consultaZonaRequest: ConsultaZonaRequest,
        @Header("Authorization") token: String
    ): Call<ConsultaZonaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarZonas.ACTUALIZAZONA)
    fun getActualizaZonas(
        @Body actualizaZonaRequest: ActualizaZonaRequest,
        @Header("Authorization")  token: String
    ): Call<ActualizaZonaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarEstaciones.ALTAESTACION)
    fun getAltaEstaciones(
        @Body altaEstacionRequest: AltaEstacionRequest,
        @Header("Authorization") token: String
    ): Call<AltaEstacionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarEstaciones.CONSULTAESTACION)
    fun getConsultaEstacion(
        @Body consultaEstacionRequest: ConsultaEstacionRequest,
        @Header("Authorization")token: String
    ): Call<ConsultaEstacionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarEstaciones.BUSQUEDAESTACION)
    fun getBusquedaEstacion(
        @Body busquedaEstacionRequest: BusquedaEstacionRequest,
        @Header("Authorization")token: String
    ): Call<BusquedaEstacionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarEstaciones.ACTUALIZAESTACION)
    fun getActualizaEstacion(
        @Body actualizaEstacionRequest: ActualizaEstacionRequest,
        @Header("Authorization")token: String
    ): Call<ActualizaEstacionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarEstaciones.ELIMINAESTACION)
    fun getEliminaEstacion(
        @Body eliminaEstacionRequest: EliminaEstacionRequest,
        @Header("Authorization")token: String
    ): Call<EliminaEstacionResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministrarImpuesto.CONSULTAIMPUESTO)
    fun getConsultaImpuesto(
        @Query("clavePais") clavePais: String,
        @Header("Authorization") token: String
    ): Call<ConsultaImpuestoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarCompra.ADMINISTRARCOMPRA)
    fun getAdministrarCompra(
        @Body administrarCompraRequest: AdministrarCompraRequest,
        @Header("Authorization") token: String
    ): Call<AdministrarCompraResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.Vinculado.BUSCAR_EMPLEADO_VINCULADO)
    fun getBuscarEmpVinculado(
        @Query("ciaNom") ciaNom: Int,
        @Query("numEmp") numEmp: Long,
        @Query("fechaFoto") fechaFoto: String,
        @Header("Authorization") token: String
    ): Call<BuscarEmpleadoVinResponse>
    @Headers("Content-Type: application/json;charset=UTF-8", "Authorization: Basic SHVtYW46VCEyZURrVHdYNE1BaHNuWlNCZnBwWCpWelZ1a05T")
    @POST(URL.Vinculado.CONSULTA_IMAGEN)
    fun getConsultarImg(
        @Body consultaImagenVinRequest: ConsultaImagenVinRequest
    ): Call<ConsultaImagenVinResponse>
    @Headers("Content-Type: application/json;charset=UTF-8", "Authorization: Basic SHVtYW46VCEyZURrVHdYNE1BaHNuWlNCZnBwWCpWelZ1a05T")
    @POST(URL.Vinculado.ENROLAR)
    fun getCambiarImagen(
        @Body enrolarRequest: EnrolarRequest
    ): Call<EnrolarResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.Vinculado.CAMBIAR_NIP)
    fun getCambiarNip(
        @Body cambiarNipRequest: CambiarNipRequest,
        @Header("Authorization") token: String
    ): Call<CambiarNipResponse>
    @Headers("Content-Type: application/json;charset=UTF-8", "Authorization: Basic SHVtYW5lVGltZTpQQHNzd29yZDIy")
    @POST(URL.Vinculado.OLVIDE_MI_NIP)
    fun getOlvideNip(
        @Body olvideNipRequest: OlvideNipRequest
    ): Call<OlvideNipResponse>
    @Headers("Content-Type: application/json;charset=UTF-8", "Authorization: Basic SHVtYW5lVGltZTpQQHNzd29yZDIy")
    @POST(URL.AdministrarPrivacidad.ACEPTAAVISO)
    fun getAceptaAviso(
        @Body aceptaAvisoRequest: AceptaAvisoRequest
    ): Call<AceptarAvisoResponse>
    @Headers("Content-Type: application/json;charset=UTF-8", "Authorization: Basic SHVtYW5lVGltZTpQQHNzd29yZDIy")
    @POST(URL.AdministrarPrivacidad.GETAVISO)
    fun getAvisoPrivacidad(
        @Body getAvisoPrivacidadRequest: GetAvisoPrivacidadRequest
    ): Call<GetAvisoPrivacidadResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.HistorialAuditorias.LISTA_AUDITORIA)
    fun getListaAuditoria(
        @Body listaAuditoriaRequest: ListaAuditoriaRequest,
        @Header("Authorization") token: String
    ): Call<ListaAuditoriaResponse>
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.HistorialAuditorias.DETALLES_AUDITORIA)
    fun getDetalleAuditoria(
        @Body detallesRequest: DetallesRequest,
        @Header("Authorization") token: String
    ): Call<DetallesResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministracionEmpleados.DELETEACOUNT)
    fun getDeleteAcount(
        @Body deleteAccountRequest: DeleteAccountRequest,
        @Header("Authorization") token: String
    ): Call<DeleteAcountResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministrarReportes.LISTPHOTOAUTH)
    fun getListPhoto(
        @Query("idCia") idCia: Long,
        @Query("idUsuario") idUsuario: Long,
        @Header("Authorization")token: String
    ): Call<ListPhotoAuthResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministrarReportes.PHOTOAUTH)
    fun authorizationPhoto(
        @Body photoAuthRequest: PhotoAuthRequest,
        @Header("Authorization")token: String
    ): Call<PhotoAuthResponse>
}