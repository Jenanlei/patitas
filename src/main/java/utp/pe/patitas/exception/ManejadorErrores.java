package utp.pe.patitas.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorErrores {
 @ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
	public Map<String, String> datos(Exception e) {
		return Map.of("mensaje", e.getMessage());
	}

	@ExceptionHandler(CredencialesInvalidasException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
	public Map<String, String> credenciales() {
		return Map.of("mensaje", "Usuario o clave incorrectos");
	}

	@ExceptionHandler(CuentaBloqueadaException.class)
	@ResponseStatus(HttpStatus.LOCKED) // 423
	public Map<String, String> bloqueada() {
		return Map.of("mensaje", "Cuenta bloqueada");
	}
}

