package br.edu.ifnmg.dtnchat.entidade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by andrefellype on 16/01/18.
 */

@DatabaseTable(tableName = "mensagem")
public class Mensagem implements Serializable{

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int id_servidor;
    @DatabaseField
    private int emissor;
    @DatabaseField
    private int destinatario;
    @DatabaseField
    private String texto;
    @DatabaseField
    private boolean status;
    @DatabaseField
    private Date data_envio;

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

    public int getEmissor() {
        return emissor;
    }

    public void setEmissor(int emissor) {
        this.emissor = emissor;
    }

    public int getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(int destinatario) {
        this.destinatario = destinatario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getData_envio() {
        return data_envio;
    }

    public void setData_envio(Date data_envio) {
        this.data_envio = data_envio;
    }
}
