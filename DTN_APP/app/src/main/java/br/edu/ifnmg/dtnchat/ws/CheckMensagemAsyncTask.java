package br.edu.ifnmg.dtnchat.ws;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.MensagemDAO;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;
import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 17/01/18.
 */

public class CheckMensagemAsyncTask extends WebServiceGenerico<String, String, String> {

    Context context;

    public CheckMensagemAsyncTask(Context context, Usuario usuario){
        super(usuario);
        this.context = context;
        this.setCaminho("tcc/DTN_WEB/verificarMensagem.php");
    }

    @Override
    protected String doInBackground(String... params) {

        String dados = params[0];
        try {
            HttpURLConnection connection = (HttpURLConnection)this.getURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream ());
            wr.write(dados);
            wr.flush();
            wr.close();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            return "O SERVIDOR NÃO SE ENCONTRA DISPONÍVEL!";
        }

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            DatabaseHelper dh = new DatabaseHelper(context);
            MensagemDAO mensagemDAO = new MensagemDAO(dh.getConnectionSource());
            JSONArray json = null;
            JSONObject mJsonObject = new JSONObject();
            json = new JSONArray(result);
            for(int i=0; i<json.length(); i++) {
                mJsonObject = (JSONObject) json.get(i);
                Map<String, Object> values = new HashMap<>();
                values.put("id_servidor",mJsonObject.getInt("id"));
                List<Mensagem> mensagemList = mensagemDAO.queryForFieldValues(values);
                if(!mensagemList.isEmpty()) {
                    Mensagem mensagem = mensagemList.get(0);
                    if(!mensagem.isStatus()) {
                        mensagem.setStatus(true);
                        mensagemDAO.update(mensagem);
                    }
                }
            }
        }catch (JSONException e) {
            Log.e("FAIL", result);
        }catch (SQLException e){
            Log.e("FAIL", "SQLException: " + e.getMessage());
        }
    }

}
