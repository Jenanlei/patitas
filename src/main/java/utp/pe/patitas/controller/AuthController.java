package utp.pe.patitas.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utp.pe.patitas.service.LoginService;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final LoginService service;

	public AuthController(LoginService service) {
		this.service = service;
	}

	@PostMapping("/login")
	public Map<String, String> login(@RequestBody LoginRequest req) {
		return Map.of("mensaje", service.login(req.usuario(), req.clave()));
	}
}

