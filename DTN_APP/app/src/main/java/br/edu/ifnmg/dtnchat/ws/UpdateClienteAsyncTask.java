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

import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 17/01/18.
 */

public class UpdateClienteAsyncTask extends WebServiceGenerico<String, String, String> {

    Context context;
    private Usuario usuario;

    public UpdateClienteAsyncTask(Context context, Usuario usuario){
        super(usuario);
        this.context = context;
        this.usuario = usuario;
        this.setCaminho("tcc/DTN_WEB/updateCliente.php");
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
            JSONArray json = null;
            JSONObject mJsonObject = new JSONObject();
            json = new JSONArray(result);
            mJsonObject = (JSONObject) json.get(0);
            UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());
            usuarioDAO.update(usuario);
            Toast.makeText(context, "Registro alterado com sucesso.", Toast.LENGTH_SHORT).show();
        }catch (JSONException e) {
            Log.e("FAIL", result);
        }catch (SQLException e){
            Log.e("FAIL", e.getMessage());
        }
    }

}
