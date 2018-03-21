package br.edu.ifnmg.dtnchat.entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by andrefellype on 16/01/18.
 */

@DatabaseTable(tableName = "usuario")
public class Usuario implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int id_servidor;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String telefone;
    @DatabaseField
    private String ip;
    @DatabaseField
    private String porta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_servidor() {
        return id_servidor;
    }

    public void setId_servidor(int id_servidor) {
        this.id_servidor = id_servidor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }
}
