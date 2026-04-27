package com.wearby.sesion;

/**
 * Almacena los datos del usuario autenticado durante la sesión.
 * Patrón Singleton: Una única estancia accesible globalmente
 */
public class SesionUsuario {

    private static SesionUsuario instancia;

    private Integer id;
    private String nombre;
    private String email;
    private String rol;

    private SesionUsuario(){}

    public static SesionUsuario getInstancia() {
        if (instancia == null){
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public void iniciar(Integer id, String nombre, String email, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public void cerrar() {
        this.id = null;
        this.nombre = null;
        this.email = null;
        this.rol = rol;
    }

    public boolean isAdmin(){
        return "admin".equals(rol);
    }

    public Integer getId() {return id;}
    public String getNombre() {return nombre;}
    public String getEmail() {return email;}
    public String getRol() {return rol;}
}
