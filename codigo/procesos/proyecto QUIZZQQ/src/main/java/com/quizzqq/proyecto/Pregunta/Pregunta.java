
package com.quizzqq.proyecto.Pregunta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pregunta {

    private Integer id;
    private String codigoTema;
    private String textoPregunta;
    private String estado;
}
