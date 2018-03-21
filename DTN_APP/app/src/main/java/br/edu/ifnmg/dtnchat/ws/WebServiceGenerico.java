package br.edu.ifnmg.dtnchat.ws;

import android.os.AsyncTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 16/01/18.
 */

public abstract class WebServiceGenerico<T, M, N> extends AsyncTask<T, M, N> {

    private String host;
    private String caminho;
    private Map<String, String> parametros;

    public WebServiceGenerico(Usuario usuario) {
        host = usuario.getIp() + ":" + usuario.getPorta();
        this.parametros = new HashMap<>();
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = "/" + caminho;
    }


    public URL getURL() throws MalformedURLException {
        return new URL("http://" + host + "" + this.getCaminho() + "" + this.getParametros());
    }

    public void addParametro(String nome, String valor){
        this.parametros.put(nome, valor);
    }

    public String getParametros(){
        String result = "";
        int contador = 0;
        for(String parametro : parametros.keySet()){
            if(contador == 0)
                result = result + "?";
            else
                result = result + "&";
            result = result + "" + parametro + "=" + this.parametros.get(parametro);
        }
        return result;
    }

}
