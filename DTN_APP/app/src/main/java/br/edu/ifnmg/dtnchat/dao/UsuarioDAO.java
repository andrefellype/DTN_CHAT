package br.edu.ifnmg.dtnchat.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 16/01/18.
 */

public class UsuarioDAO extends BaseDaoImpl<Usuario, Integer> {

    public UsuarioDAO(ConnectionSource cs) throws SQLException {
        super(Usuario.class);
        setConnectionSource(cs);
        initialize();
    }

}
