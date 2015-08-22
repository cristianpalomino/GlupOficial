package pe.com.glup.ws;

/**
 * Created by Glup on 24/06/15.
 */
public class WSGlup {

    /**
     * Tag's Catalogo
     */
    public static final String NUMERO_PAGINA = "#num_pagina";
    public static final String NUMERO_REGISTROS = "#num_registro";
    public static final String BUSCAR = "#buscar";
    public static final String FILTRO_POSICION = "#filtro_posicion";

    public static final String ORQUESTADOR = "http://glup-com-pe-ormvxmez8gb9.runscope.net/api/orquestadorServiciosApp.php";
    public static final String ORQUESTADOR_CATALOGO = "http://glup-com-pe-ormvxmez8gb9.runscope.net/api/orquestadorServiciosApp.php?" +
            "num_pagina=#num_pagina&" +
            "num_registro=#num_registro&" +
            "buscar=#buscar";
    public static final String ORQUESTADOR_PROBADOR = "http://glup-com-pe-ormvxmez8gb9.runscope.net/api/orquestadorServiciosApp.php?" +
            "num_pagina=#num_pagina&" +
            "num_registro=#num_registro&" +
            "filtro_posicion=#filtro_posicion";
}
