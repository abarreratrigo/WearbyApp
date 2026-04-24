package com.wearby.wearby_api.dao;

import com.wearby.wearby_api.dto.PrendaFiltroDTO;
import com.wearby.wearby_api.util.ConexionDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de prendas con acceso directo a la BD mediante JDBC.
 * Implementa las consultas complejas de filtrado para la
 * generación de outfits con PreparedStatement parametrizados.
 */
@Repository
@RequiredArgsConstructor
public class PrendaDAO {

    private final ConexionDB conexionDB;

    /**
     * Obtiene prendas de un usuario filtradas por características.
     * Usa PreparedStatement para prevenir inyección SQL.
     */
    public List<PrendaFiltroDTO> buscarPrendasParaOutfit(
            Integer usuarioId,
            Integer categoriaId,
            Integer estiloId,
            Integer temporadaId,
            Integer formalidadId) throws SQLException {

        List<PrendaFiltroDTO> resultado = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT p.id, p.nombre, p.imagen_url, p.favorito,
                   c.nombre AS categoria, e.nombre AS estilo,
                   t.nombre AS temporada, f.nombre AS formalidad,
                   col.nombre AS color
            FROM prenda p
            LEFT JOIN categoria c ON p.id_categoria = c.id
            LEFT JOIN estilo e ON p.id_estilo = e.id
            LEFT JOIN temporada t ON p.id_temporada = t.id
            LEFT JOIN formalidad f ON p.id_formalidad = f.id
            LEFT JOIN color col ON p.id_color = col.id
            WHERE p.id_usuario = ?
            """);

        List<Object> parametros = new ArrayList<>();
        parametros.add(usuarioId);

        if (categoriaId != null) {
            sql.append(" AND p.id_categoria = ?");
            parametros.add(categoriaId);
        }
        if (estiloId != null) {
            sql.append(" AND p.id_estilo = ?");
            parametros.add(estiloId);
        }
        if (temporadaId != null) {
            sql.append(" AND p.id_temporada = ?");
            parametros.add(temporadaId);
        }
        if (formalidadId != null) {
            sql.append(" AND p.id_formalidad = ?");
            parametros.add(formalidadId);
        }

        sql.append(" ORDER BY p.favorito DESC");

        try (Connection con = conexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrendaFiltroDTO dto = new PrendaFiltroDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setNombre(rs.getString("nombre"));
                    dto.setImagenUrl(rs.getString("imagen_url"));
                    dto.setFavorito(rs.getBoolean("favorito"));
                    dto.setCategoria(rs.getString("categoria"));
                    dto.setEstilo(rs.getString("estilo"));
                    dto.setTemporada(rs.getString("temporada"));
                    dto.setFormalidad(rs.getString("formalidad"));
                    dto.setColor(rs.getString("color"));
                    resultado.add(dto);
                }
            }
        }
        return resultado;
    }

    /**
     * Obtiene estadísticas generales para el panel de administrador.
     */
    public int[] obtenerEstadisticas() throws SQLException {
        int[] stats = new int[3];

        try (Connection con = conexionDB.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (Statement st = con.createStatement()) {
                    ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM usuario WHERE rol = 'usuario'");
                    if (rs.next()) stats[0] = rs.getInt(1);

                    rs = st.executeQuery("SELECT COUNT(*) FROM prenda");
                    if (rs.next()) stats[1] = rs.getInt(1);

                    rs = st.executeQuery("SELECT COUNT(*) FROM outfit");
                    if (rs.next()) stats[2] = rs.getInt(1);
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
        return stats;
    }

    /**
     * Llama al procedimiento almacenado para obtener prendas
     * de una categoría concreta que cumplan los filtros comunes.
     * Se llama una vez por cada categoría seleccionada por el usuario.
     */
    public List<PrendaFiltroDTO> llamarProcedimientoOutfit(
            Integer usuarioId,
            Integer categoriaId,
            Integer estiloId,
            Integer temporadaId,
            Integer formalidadId) throws SQLException {

        List<PrendaFiltroDTO> resultado = new ArrayList<>();

        try (Connection con = conexionDB.getConexion();
             CallableStatement cs = con.prepareCall("{CALL generarOutfit(?, ?, ?, ?, ?)}")) {

            cs.setInt(1, usuarioId);

            if (categoriaId != null) cs.setInt(2, categoriaId);
            else cs.setNull(2, Types.INTEGER);

            if (estiloId != null) cs.setInt(3, estiloId);
            else cs.setNull(3, Types.INTEGER);

            if (temporadaId != null) cs.setInt(4, temporadaId);
            else cs.setNull(4, Types.INTEGER);

            if (formalidadId != null) cs.setInt(5, formalidadId);
            else cs.setNull(5, Types.INTEGER);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    PrendaFiltroDTO dto = new PrendaFiltroDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setNombre(rs.getString("nombre"));
                    dto.setImagenUrl(rs.getString("imagen_url"));
                    dto.setFavorito(rs.getBoolean("favorito"));
                    dto.setCategoria(rs.getString("categoria"));
                    dto.setEstilo(rs.getString("estilo"));
                    dto.setTemporada(rs.getString("temporada"));
                    dto.setFormalidad(rs.getString("formalidad"));
                    dto.setColor(rs.getString("color"));
                    resultado.add(dto);
                }
            }
        }
        return resultado;
    }
}