package utp.pe.patitas.service;

import org.springframework.stereotype.Service;

import utp.pe.patitas.exception.CredencialesInvalidasException;
import utp.pe.patitas.exception.CuentaBloqueadaException;
import utp.pe.patitas.model.Usuario;
import utp.pe.patitas.repository.UsuarioRepository;

@Service
public class LoginService {

    private final UsuarioRepository repo;

    public LoginService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public String login(String usuario, String clave) {
        if (usuario == null || usuario.isBlank() || clave == null || clave.isBlank()) // RN1
            throw new IllegalArgumentException("Datos obligatorios");
        Usuario u = repo.findByUsuario(usuario)
                .orElseThrow(CredencialesInvalidasException::new); // RN3
        if (u.isBloqueado())
            throw new CuentaBloqueadaException(); // RN4
        if (!u.getClave().equals(clave)) {
            u.setIntentos(u.getIntentos() + 1);
            if (u.getIntentos() >= 3)
                u.setBloqueado(true); // RN4
            repo.save(u);
            throw new CredencialesInvalidasException(); // RN3
        }
        u.setIntentos(0); // RN5
        repo.save(u);
        return "Bienvenido, " + u.getNombre(); // RN2
    }
}

