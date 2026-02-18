/*
 * Login.jsx
 * Componente React que replica el JSP de login y usa la misma lógica de validación (login.js).
 * Conexión directa al servlet.
 *
 * Autor: ORLANDUVALIE TABARES GUTIERREZ (adaptado)
 * Fecha: 22/10/2025
 */

import React, { useState, useEffect } from "react";
import "../stylescss/login.css";

function Login() {
  // Estados
  const [nombreUsuario, setNombreUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [recordarme, setRecordarme] = useState(false);
  const [mensajeUsuario, setMensajeUsuario] = useState("");
  const [mensajePassword, setMensajePassword] = useState("");
  const [mensajeServidor, setMensajeServidor] = useState("");

  // URL del servlet (igual formato que Clientes.jsx)
  const URL_SERVLET = "http://localhost:8084/Mi_Proyecto_VIFAC-Sys/loginServlet";

  // Al montar, intenta leer la cookie 'rememberMe' (si el servidor la puso) o localStorage
  useEffect(() => {
    // intenta cookie first (si el JSP anterior las usaba)
    const cookies = document.cookie.split(";").map(c => c.trim());
    const rememberCookie = cookies.find(c => c.startsWith("rememberMe="));
    if (rememberCookie) {
      const valor = decodeURIComponent(rememberCookie.split("=")[1] || "");
      if (valor) {
        setNombreUsuario(valor);
        setRecordarme(true);
        return;
      }
    }
    // fallback localStorage (por si quieres manejarlo en frontend)
    const stored = localStorage.getItem("rememberMe");
    if (stored) {
      setNombreUsuario(stored);
      setRecordarme(true);
    }
  }, []);

  // Validación (misma lógica que tu login.js)
  const validar = () => {
    let hayErrores = false;
    let msgUsuario = "";
    let msgPassword = "";

    if (!nombreUsuario.trim()) {
      msgUsuario = "El usuario es obligatorio.";
      hayErrores = true;
    }

    const pwd = contrasena;
    if (pwd.length < 8) {
      msgPassword += "Debe tener al menos 8 caracteres. ";
      hayErrores = true;
    }
    if (!/[A-Z]/.test(pwd)) {
      msgPassword += "Debe contener al menos una mayúscula. ";
      hayErrores = true;
    }
    if (!/[a-z]/.test(pwd)) {
      msgPassword += "Debe contener al menos una minúscula. ";
      hayErrores = true;
    }
    if (!/\d/.test(pwd)) {
      msgPassword += "Debe contener al menos un número. ";
      hayErrores = true;
    }
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(pwd)) {
      msgPassword += "Debe contener al menos un carácter especial. ";
      hayErrores = true;
    }

    setMensajeUsuario(msgUsuario);
    setMensajePassword(msgPassword);

    return !hayErrores;
  };

  // Manejo envío — POST directo al servlet (same pattern que Clientes.jsx)
  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensajeServidor("");
    if (!validar()) return;

    try {
      const params = new URLSearchParams();
      params.append("nombreUsuario", nombreUsuario);
      params.append("contrasena", contrasena);
      if (recordarme) params.append("remember", "on");

      const res = await fetch(URL_SERVLET, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString(),
        credentials: "include" // mantiene cookies de sesión establecidas por el servlet
      });

      // intenta parsear JSON; si el servlet responde con redirect/HTML falla y cae al catch
      const data = await res.json();

      // Esperamos algo como { status: "success" } o { status: "error", message: "..." }
      if (data.status === "success" || data.ok || data.status === "ok") {
        // si quieres guardar remember también en front:
        if (recordarme) localStorage.setItem("rememberMe", nombreUsuario);
        else localStorage.removeItem("rememberMe");

        // redirigir al index (igual que en tu JSP -> indexServlet)
        window.location.href = "/indexServlet";
      } else {
        setMensajeServidor(data.message || data.mensaje || "Credenciales inválidas");
      }
    } catch (err) {
      console.error("login error:", err);
      setMensajeServidor("Error al conectar con el servidor o respuesta inesperada.");
    }
  };

  return (
    <div className="login-page bg-white">
      <div className="header-container">
        <div className="left-text">login</div>
        <div className="center-text">MAXI-LIMPIEZA</div>
        <div className="right-text">Calidad que se siente</div>
      </div>

      <div id="layoutAuthentication">
        <div id="layoutAuthentication_content">
          <main>
            <div className="container">
              <div className="row justify-content-start mt-3">
                <div className="col-sm-10 col-md-7 col-lg-5">
                  <div className="card shadow-lg border-7 rounded-lg mt-2">
                    <div className="card-header">
                      <h3 className="text-center font-weight-secondary my-3">Inicio de Sesión</h3>
                    </div>

                    <div className="card-body mx-2">
                      {mensajeServidor && (
                        <div className="alert alert-danger" role="alert">{mensajeServidor}</div>
                      )}

                      <form id="login-form" onSubmit={handleSubmit}>
                        <div className="form-floating mb-3">
                          <input
                            className="form-control"
                            id="inputusuario"
                            name="nombreUsuario"
                            type="text"
                            placeholder="Usuario Empresarial"
                            value={nombreUsuario}
                            onChange={(e) => setNombreUsuario(e.target.value)}
                            required
                          />
                          <label htmlFor="inputusuario">Usuario Empresarial</label>
                          <p id="usuario-error" className="error-message">{mensajeUsuario}</p>
                        </div>

                        <div className="form-floating mb-3">
                          <input
                            className="form-control"
                            id="inputPassword"
                            name="contrasena"
                            type="password"
                            placeholder="Contraseña"
                            value={contrasena}
                            onChange={(e) => setContrasena(e.target.value)}
                            required
                          />
                          <label htmlFor="inputPassword">Contraseña</label>
                          <p id="password-error" className="error-message">{mensajePassword}</p>
                        </div>

                        <div className="mb-3 d-flex flex-wrap justify-content-between align-items-center">
                          <div className="d-flex align-items-center">
                            <input
                              className="form-check-input me-2"
                              id="inputRememberPassword"
                              type="checkbox"
                              name="remember"
                              checked={recordarme}
                              onChange={(e) => setRecordarme(e.target.checked)}
                            />
                            <label className="form-check-label mb-0" htmlFor="inputRememberPassword">¿Recordarme?</label>
                          </div>
                          <div className="d-flex align-items-center">
                            <a className="small" href="/restablecercontrasenaServlet">¿Olvidó su clave?</a>
                          </div>
                        </div>

                        <div className="d-flex justify-content-center mt-5">
                          <button type="submit" className="btn btn-outline-primary btn-login-width">Inicio de Sesión</button>
                        </div>
                      </form>
                    </div>

                    <div className="card-footer text-auto py-3">
                      <div className="col-sm-12 aviso-legal">
                        <figure className="text-center">
                          <p className="parrafo-justificado">
                            Estás ingresando al sistema <b>VIFAC-Sys</b>. El acceso está restringido solo a usuarios autorizados.
                            Esta plataforma está monitoreada; el uso inadecuado puede generar sanciones. Protege tu información,
                            no compartas credenciales y sigue las normas de seguridad establecidas.
                          </p>
                        </figure>
                      </div>
                    </div>

                  </div>
                </div>
              </div>
            </div>
          </main>
        </div>

        <div id="layoutAuthentication_footer" className="footer-text-color">
          <footer className="py-3 bg-light mb-5">
            <div className="container-fluid px-3">
              <div className="d-flex align-items-center justify-content-between">
                <div className="text-muted">© 2025</div>
                <div>
                  <a href="#" className="link-dark text-muted">Política de privacidad</a> &middot;
                  <a href="#" className="link-dark text-muted">Diseñado por O.T.G “VIFAC-Sys”</a>
                </div>
              </div>
            </div>
          </footer>
        </div>

      </div>
    </div>
  );
}

export default Login;
