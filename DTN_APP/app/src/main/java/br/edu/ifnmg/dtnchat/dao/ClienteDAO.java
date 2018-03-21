package br.edu.ifnmg.dtnchat.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import br.edu.ifnmg.dtnchat.entidade.Cliente;

/**
 * Created by andrefellype on 16/01/18.
 */

public class ClienteDAO extends BaseDaoImpl<Cliente, Integer> {

    public ClienteDAO(ConnectionSource cs) throws SQLException {
        super(Cliente.class);
        setConnectionSource(cs);
        initialize();
    }

}
