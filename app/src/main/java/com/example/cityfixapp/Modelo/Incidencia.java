package com.example.cityfixapp.Modelo;

public class Incidencia {
    public int id;
    public String titulo;
    public String descripcion;
    public String ubicacion;
    public String estado;
    public String fechaHora;
    public int idTecnico; // <- NUEVO: id del técnico asignado
    public byte[] foto; // <- NUEVO: foto como array de bytes

    public Incidencia(int id, String titulo, String descripcion, String ubicacion, String estado, String fechaHora, byte[] foto) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.fechaHora = fechaHora;
        this.foto = foto;
    }
}
