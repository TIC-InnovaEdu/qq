/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quizzqq.proyecto.Pregunta;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author USER
 */
@Data
@AllArgsConstructor
public class Respuesta {
    private Integer id;
    private Integer idPregunta;
    private String textoRespuesta;
    private String tipoRespuesta;
}