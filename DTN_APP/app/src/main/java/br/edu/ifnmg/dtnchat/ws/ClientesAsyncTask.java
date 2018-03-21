package br.edu.ifnmg.dtnchat.ws;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifnmg.dtnchat.dao.ClienteDAO;
import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.entidade.Cliente;
import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 17/01/18.
 */

public class ClientesAsyncTask extends WebServiceGenerico<String, String, String> {

    HttpURLConnection urlConnection;
    Context context;

    public ClientesAsyncTask(Context context, Usuario usuario){
        super(usuario);
        this.context = context;
        this.setCaminho("tcc/DTN_WEB/clientes.php");
    }

    @Override
    protected String doInBackground(String... args) {
        StringBuilder result = new StringBuilder();
        try {
            urlConnection = (HttpURLConnection) this.getURL().openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            Log.e("FAIL","Exception: "+e.getMessage());
        } finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            DatabaseHelper dh = new DatabaseHelper(context);
            ClienteDAO clienteDAO = new ClienteDAO(dh.getConnectionSource());
            JSONArray json = null;
            JSONObject mJsonObject = new JSONObject();
            json = new JSONArray(result);
            for(int i=0; i<json.length(); i++) {
                Cliente cliente = null;
                mJsonObject = (JSONObject) json.get(i);
                Map<String, Object> values = new HashMap<>();
                values.put("id_servidor",mJsonObject.getInt("id"));
                List<Cliente> clienteList = clienteDAO.queryForFieldValues(values);
                if(clienteList.isEmpty()) cliente = new Cliente();
                else cliente = clienteList.get(0);
                cliente.setId_servidor(mJsonObject.getInt("id"));
                cliente.setNome(mJsonObject.getString("nome"));
                cliente.setTelefone(mJsonObject.getString("telefone"));
                if(clienteList.isEmpty()) {
                    clienteDAO.create(cliente);
                }else{
                    clienteDAO.update(cliente);
                }
            }
        }catch (JSONException e) {
            Log.e("FAIL", result);
        }catch (SQLException e){
            Log.e("FAIL","SQLException: " + e.getMessage());
        }
    }

}
