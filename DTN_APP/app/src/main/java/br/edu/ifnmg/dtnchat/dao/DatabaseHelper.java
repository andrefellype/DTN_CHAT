package br.edu.ifnmg.dtnchat.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.edu.ifnmg.dtnchat.entidade.Cliente;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;
import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 16/01/18.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String databaseName = "dtnchat";
    private static final int databaseVersion = 1;

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sd, ConnectionSource cs) {
        try {
            TableUtils.createTable(cs, Usuario.class);
            TableUtils.createTable(cs, Cliente.class);
            TableUtils.createTable(cs, Mensagem.class);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase sd, ConnectionSource cs, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(cs, Usuario.class, true);
            TableUtils.dropTable(cs, Cliente.class, true);
            TableUtils.dropTable(cs, Mensagem.class, true);
            onCreate(sd, cs);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close(){
        super.close();
    }

}
