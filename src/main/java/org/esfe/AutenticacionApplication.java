package org.esfe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Aplicación principal de Autenticación
 * @ EnableScheduling: Habilita la ejecución de tareas programadas
 * (necesario para la limpieza automática de tokens en blacklist)
 */
@SpringBootApplication
@EnableScheduling // Habilita @Scheduled en TokenBlacklistService
public class AutenticacionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutenticacionApplication.class, args);
        System.out.println("🔐 API de Autenticación iniciada correctamente");
        System.out.println("📝 Swagger UI disponible en: http://localhost:8080/swagger-ui.html");
        System.out.println("🔒 Seguridad JWT habilitada");
    }

}