package br.edu.ifnmg.dtnchat.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import br.edu.ifnmg.dtnchat.entidade.Mensagem;

/**
 * Created by andrefellype on 16/01/18.
 */

public class MensagemDAO extends BaseDaoImpl<Mensagem, Integer> {

    public MensagemDAO(ConnectionSource cs) throws SQLException {
        super(Mensagem.class);
        setConnectionSource(cs);
        initialize();
    }

}
