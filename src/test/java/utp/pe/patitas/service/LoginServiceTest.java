package utp.pe.patitas.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import utp.pe.patitas.exception.CredencialesInvalidasException;
import utp.pe.patitas.exception.CuentaBloqueadaException;
import utp.pe.patitas.model.Usuario;
import utp.pe.patitas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    UsuarioRepository repo;

    @InjectMocks
    LoginService service;

    Usuario ana;

    @BeforeEach
    void preparar() {
        ana = new Usuario("ana", "Patitas2026", "Ana");
    }

    @Test
    @DisplayName("CP-01 Login correcto devuelve bienvenida")
    void loginCorrecto_devuelveBienvenida() {
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        String resultado = service.login("ana", "Patitas2026");
        assertEquals("Bienvenido, Ana", resultado);
        assertEquals(0, ana.getIntentos());
    }

    @Test
    @DisplayName("CP-02 Clave incorrecta")
    void claveIncorrecta_lanzaExcepcion() {
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        assertThrows(CredencialesInvalidasException.class,
                () -> service.login("ana", "1234"));
        assertEquals(1, ana.getIntentos());
    }

    @Test
    @DisplayName("CP-03 Usuario inexistente")
    void usuarioInexistente_lanzaExcepcion() {
        when(repo.findByUsuario("pepe")).thenReturn(Optional.empty());
        assertThrows(CredencialesInvalidasException.class,
                () -> service.login("pepe", "Patitas2026"));
    }

    @Test
    @DisplayName("CP-04 Campos vacíos")
    void camposVacios_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> service.login("", ""));
    }

    @Test
    @DisplayName("CP-05 Segundo fallo no bloquea")
    void segundoFallo_noBloquea() {
        ana.setIntentos(1);
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        assertThrows(CredencialesInvalidasException.class,
                () -> service.login("ana", "1234"));
        assertFalse(ana.isBloqueado());
    }

    @Test
    @DisplayName("CP-06 Tercer fallo bloquea la cuenta")
    void tercerFallo_bloqueaCuenta() {
        ana.setIntentos(2);
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        assertThrows(CredencialesInvalidasException.class,
                () -> service.login("ana", "1234"));
        assertTrue(ana.isBloqueado());
    }

    @Test
    @DisplayName("CP-07 Correcto tras fallos reinicia intentos")
    void correctoTrasFallos_reiniciaIntentos() {
        ana.setIntentos(2);
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        String resultado = service.login("ana", "Patitas2026");
        assertEquals("Bienvenido, Ana", resultado);
        assertEquals(0, ana.getIntentos());
    }

    @Test
    @DisplayName("CP-08 Cuenta ya bloqueada + clave correcta lanza CuentaBloqueadaException")
    void cuentaBloqueada_claveCorrecta_lanzaExcepcion() {
        // Arrange
        ana.setBloqueado(true);
        when(repo.findByUsuario("ana")).thenReturn(Optional.of(ana));
        // Act + Assert
        assertThrows(CuentaBloqueadaException.class,
                () -> service.login("ana", "Patitas2026"));
        assertTrue(ana.isBloqueado());
    }

    @Test
    @DisplayName("CP-09 Usuario con solo espacios lanza IllegalArgumentException")
    void usuarioSoloEspacios_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> service.login("   ", "Patitas2026"));
    }
}