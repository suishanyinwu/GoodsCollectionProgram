package com.it.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona {
    private String personaId;
    private String personaName;
    private String personaImgUrl;
    private String ipId;
}
