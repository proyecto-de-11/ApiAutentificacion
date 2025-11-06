package org.esfe.controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String holaMundo() {
        return "¡Hola estamos en linea desde la api de autentificación y usuarios";
    }
}