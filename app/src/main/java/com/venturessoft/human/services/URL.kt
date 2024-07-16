package com.venturessoft.human.services

class URL {
    companion object {
        var URL_BASE_PAYPAL= "https://eland-ws-02.humaneland.net/ehuman/paypal.html?price="
        var URL_BASE = "https://etd.humaneland.net/soa-infra/resources/HUMANDESVINCULADO/"
        var URL_BASE_UNIFICADO = "https://etd.humaneland.net:9004/soa-infra/resources/HUMANDESVINCULADO/"
        var URL_BASE_REPORTE_VIN =  "https://etd.humaneland.net/soa-infra/resources/HUMANVINCULADO/"
        var URL_BASE_IMAGEN = "https://eland-services.humaneland.net:8443/HumaneTime/Admin/Human/"
        var URL_MICROSERVICIOS = "https://eland-services.humaneland.net:8443/HumaneTime/api/"
    }
    class AdministracionEmpleados {
        companion object {
            const val ALTAEMPLEADO = "AdministracionEmpleados/AltaEmpleado"
            const val CONSULTAEMPLEADO = "AdministracionEmpleados/ConsultaEmpleado"
            const val BUSQUEDAEMPLEADO = "AdministracionEmpleados/BusquedaEmpleado"
            const val ACTUALIZAEMPLEADO = "AdministracionEmpleados/ActualizaEmpleado"
            const val ELIMINAEMPLEADO = "AdministracionEmpleados/EliminaEmpleado"
            const val DELETEACOUNT = "AdministrarUsuarios/ActualizaEstatusUsuario"
        }
    }
    class HistorialAuditorias {
        companion object {
            const val LISTA_AUDITORIA = "AuditoriaEtime/"

            const val DETALLES_AUDITORIA = "AuditoriaEtime/Detalle"
       }
    }
    class AdministrarCompra {
        companion object {
       const val ADMINISTRARCOMPRA = "Desvinculado/ComprarChecada/"
        }
    }
    class AdministrarReportes {
        companion object {
            const val CONSULTAESTADISTICAS = "Desvinculado/ConsultaEstadisticas"
            const val CONSULTAEMPLEADOSEST = "Desvinculado/EstadisticasEmpleadosActivos"
            const val REPORTECHECADAS = "AdministrarReportes/ReporteChecadas"
            const val REPORTESOPCION = "AdministrarReportes/Reporte/ReporteOpcion"
            const val REPORTESENROL = "AdministrarReportes/ReporteEmpleados"
            const val REPORTESTATIONFREE = "AdministrarEstaciones/ReporteChecadas/Detalle"
            const val REPORTESTATIONFREELITS = "AdministrarEstaciones/ReporteChecadas/"
            const val LISTPHOTOAUTH = "AdministraFotoLocal/Autoriza"
            const val PHOTOAUTH = "AdministraFotoLocal/Autoriza"
        }
    }
    class AdministrarPrivacidad {
        companion object {
            const val ACEPTAAVISO = "AdministrarPrivacidad/AdministrarPrivacidad/AceptarAviso"
            const val GETAVISO = "AdministrarPrivacidad/AdministrarPrivacidad/GetAvisoPrivacidad"
        }
    }
    class AdministrarUsuario {
        companion object {
            const val ACCESOUSUARIO = "Authorization/AccesoUsuario"
            const val ALTAUSUARIOADMIN = "AdministrarUsuarios/AltaUsuarioAdmin"
            const val ALTAUSUARIOSUPERVISOR = "AdministrarUsuarios/AltaUsuarioSupervisor"
            const val ACTUALIZAPASSWORD = "AdministrarUsuarios/ActualizaPassword"
            const val ACTUALIZAUSUARIO = "AdministrarUsuarios/ActualizaUsuario"
            const val BUSQUEDAUSUARIO = "AdministrarUsuarios/BusquedaUsuario"
            const val CONSULTAUSUARIO = "AdministrarUsuarios/ConsultaUsuario"
            const val ELIMINAUSUARIO = "AdministrarUsuarios/EliminaUsuario"
            const val RECUPERAR_PASWORD = "AdministrarUsuarios/RecuperaPassword"
        }
    }
    class Token{
        companion object{
            const val TOKENNOLOGIN = "Authorization/AccesoToken"
        }
    }
    class AdministrarCompañia {
        companion object {
            const val ALTACOMPANIA = "Administrar/Compania/"
            const val ACTUALIZACOMPANIA = "Administrar/Compania/{idCompania}"
            const val CONSULTACOMPANIA = "Administrar/Compania/"
            const val CONSULTACOMPANIADATA = "Administrar/Compania/"
        }
    }
    class AdministrarFacturacion {
        companion object {
            const val ALTAFACTURACION = "Administrar/Facturacion/"
            const val ACTUALIZAFACTURACION = "Administrar/Facturacion/{idFacturacion}"
            const val CONSULTAFACTURACION = "Administrar/Facturacion/"
        }
    }
    class AdministrarZonas {
        companion object {
            const val ALTAZONA = "AdministraZona/Desvinculado/Alta"
            const val BUSQUEDAZONA = "AdministraZona/Desvinculado/Busqueda"
            const val ELIMINAZONA = "AdministraZona/Desvinculado/Elimina"
            const val ACTUALIZAZONA = "AdministraZona/Desvinculado/Actualiza"
            const val CONSULTAZONA ="AdministraZona/Desvinculado/Consulta"
        }
    }
    class AdministrarEstaciones {
        companion object {
            const val ALTAESTACION = "AdministrarEstaciones/AltaEstacion"
            const val CONSULTAESTACION = "AdministrarEstaciones/ConsultaEstacion"
            const val BUSQUEDAESTACION = "AdministrarEstaciones/BusquedaEstacion"
            const val ACTUALIZAESTACION = "AdministrarEstaciones/ActualizaEstacion"
            const val ELIMINAESTACION = "AdministrarEstaciones/EliminarEstacion"
        }
    }
    class AdministrarImpuesto {
        companion object {
            const val CONSULTAIMPUESTO = "AdministrarImpuestos"
        }
    }
    class Vinculado {
        companion object {
            const val BUSCAR_EMPLEADO_VINCULADO = "ConsultaEmpleado/Empleado"
            const val CONSULTA_IMAGEN = "AdministrarFotografias/ConsultaImagen"
            const val ENROLAR = "AdministrarFotografias/CargaImagen"
            const val OLVIDE_MI_NIP = "FaceRecognizer/AccesoHuman/OlvideNip"
            const val CAMBIAR_NIP = "workSocial/Actualiza/nip"
        }
    }
}
