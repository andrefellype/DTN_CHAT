package br.edu.ifnmg.dtnchat.ws;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifnmg.dtnchat.MainActivity;
import br.edu.ifnmg.dtnchat.R;
import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.MensagemDAO;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;
import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 17/01/18.
 */

public class MensagensAsyncTask extends WebServiceGenerico<String, String, String> {

        ProgressDialog progessDialo;
        HttpURLConnection urlConnection;
        Context context;

        public MensagensAsyncTask(Context context, Usuario usuario){
            super(usuario);
            this.context = context;
            this.setCaminho("tcc/DTN_WEB/mensagens.php");
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection;
            StringBuilder strBuilder;
            String result = "";
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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DatabaseHelper dh = new DatabaseHelper(context);
                MensagemDAO mensagemDAO = new MensagemDAO(dh.getConnectionSource());
                JSONArray json = null;
                JSONObject mJsonObject = new JSONObject();
                json = new JSONArray(result);
                for(int i=0; i<json.length(); i++) {
                    mJsonObject = (JSONObject) json.get(i);
                    Mensagem mensagem = new Mensagem();
                    mensagem.setId_servidor(mJsonObject.getInt("id"));
                    mensagem.setEmissor(mJsonObject.getInt("emissor"));
                    mensagem.setDestinatario(mJsonObject.getInt("destinatario"));
                    mensagem.setTexto(mJsonObject.getString("texto"));
                    mensagem.setStatus(true);
                    mensagem.setData_envio(format.parse(mJsonObject.getString("data_envio")));
                    mensagemDAO.create(mensagem);
                }
                if(json.length() > 0) {
                    Intent intent = new Intent(context, MainActivity.class);
                    NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                    PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle("NOVA MENSAGEM - DTN CHAT").setContentText("VOCÊ POSSUI " + json.length() + (json.length() > 1 ? "MENSAGEM." : "MENSAGENS."));
                    builder.setContentIntent(p);

                    Notification n = builder.build();
                    n.vibrate = new long[]{150, 300, 150, 600};
                    n.flags = Notification.FLAG_AUTO_CANCEL;

                    nm.notify(R.mipmap.ic_launcher_round, n);

                }
            }catch (JSONException e) {
                Log.e("FAIL", result);
            }catch (SQLException e){
                Log.e("FAIL", "SQLException: " + e.getMessage());
            }catch (ParseException e){
                Log.e("FAIL", "ParseException: " + e.getMessage());
            }
        }
}
