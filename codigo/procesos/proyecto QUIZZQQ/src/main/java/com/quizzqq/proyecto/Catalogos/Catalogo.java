package com.quizzqq.proyecto.Catalogos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author DARWIN ONOFRE
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Catalogo {

    private Integer id;
    private String idCatalogo;
    private String codigoCatalogo;
    private String descripcion;
    private String estado;
}
