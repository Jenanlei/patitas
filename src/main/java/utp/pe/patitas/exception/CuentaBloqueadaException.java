package utp.pe.patitas.exception;

public class CuentaBloqueadaException extends RuntimeException {
    public CuentaBloqueadaException() {
        super("Cuenta bloqueada");
    }
    
}
