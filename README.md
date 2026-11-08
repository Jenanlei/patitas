# patitas

Laboratorio de pruebas (Semana 13 · Sesión 26)

Pruebas unitarias (JUnit 5 + Mockito) y de sistema (Postman) para el login de la Veterinaria Patitas, hecho con Spring Boot, Maven y VS Code.

Curso: Curso integrador I: sistemas - software
Paquete base: utp.pe.patitas
Endpoint probado: POST http://localhost:8080/api/auth/login
Evidencias
1. Panel Testing con las 9 pruebas en verde

Panel Testing 9/9

2. Pruebas de sistema en Postman

200 OK: ana / Patitas2026 Postman 200

400 Bad Request: campos vacíos Postman 400

401 Unauthorized: credenciales incorrectas Postman 401

423 Locked: 4.º intento de ana / 1234 Postman 423

Pruebas implementadas
Caso	Tipo	Descripción	Resultado esperado
CP-01	Unitaria	Login correcto	"Bienvenido, Ana" e intentos = 0
CP-02	Unitaria	Clave incorrecta	CredencialesInvalidasException, intentos = 1
CP-03	Unitaria	Usuario inexistente	CredencialesInvalidasException
CP-04	Unitaria	Campos vacíos	IllegalArgumentException
CP-05	Unitaria	Segundo fallo	No bloquea la cuenta
CP-06	Unitaria	Tercer fallo	Bloquea la cuenta
CP-07	Unitaria	Correcto tras 2 fallos	intentos = 0
CP-08	Unitaria	Cuenta ya bloqueada + clave correcta	CuentaBloqueadaException
CP-09	Unitaria	Usuario con solo espacios	IllegalArgumentException
CP-10	Sistema (Postman)	4 envíos seguidos de ana / 1234	401, 401, 401 y 423 "Cuenta bloqueada"

Resultado de Postman:

Caso	Body	Respuesta
CP-01	ana / Patitas2026	200 OK
CP-02 / CP-03	ana / 1234 o pepe / Patitas2026	401 Unauthorized
CP-04	"" / ""	400 Bad Request
CP-10	4.º envío de ana / 1234	423 Locked
Defecto encontrado

CP-06 salió en rojo (expected: <true> but was: <false>). La causa estaba en LoginService: la condición if (u.getIntentos() > 3) bloqueaba la cuenta en el 4.º fallo y no en el 3.º. Se corrigió a >= 3, se volvieron a correr las pruebas desde el panel Testing y con .\mvnw test (regresión) y todas pasaron.

CP-06 en rojo

Además, antes de crear el ManejadorErrores, un dato vacío en Postman devolvía 500. Con el manejador, cada excepción se traduce a su código: IllegalArgumentException → 400, CredencialesInvalidasException → 401 y CuentaBloqueadaException → 423.

Cómo ejecutar
.\mvnw test               # pruebas unitarias (9 de LoginServiceTest + contextLoads)
.\mvnw spring-boot:run    # levanta la API en el puerto 8080
Metacognición
¿Qué aprendí? @Mock crea un repositorio falso para no usar la base de datos; @InjectMocks crea el LoginService y le inyecta ese repositorio falso; assertThrows comprueba que el login lance la excepción esperada (por ejemplo, CredencialesInvalidasException con clave incorrecta).
¿Cómo lo aprendí? Me trabé con el nombre del paquete (utp.pe.patitas en lugar de pe.patitas) y con una respuesta de Postman que mostraba 200 pero con el mensaje de error; lo resolví corrigiendo los package e import de cada clase y reiniciando la app para repetir las pruebas desde cero.
¿Para qué me sirve? En mi proyecto probaré primero el método de login con el caso límite de un usuario con solo espacios, que debe lanzar IllegalArgumentException.
