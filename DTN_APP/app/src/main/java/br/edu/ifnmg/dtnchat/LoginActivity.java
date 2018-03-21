package br.edu.ifnmg.dtnchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Usuario;
import br.edu.ifnmg.dtnchat.ws.LoginAsyncTask;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
            DatabaseHelper dh = new DatabaseHelper(this);
            UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());

            if(!usuarioDAO.queryForAll().isEmpty()){
                Intent intent = new Intent(this, BeginActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            Log.e("FAIL", e.getMessage());
        }
    }

    public void conectar(View view){
        EditText edtNome = (EditText) findViewById(R.id.edtNome);
        EditText edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        EditText edtIp = (EditText) findViewById(R.id.edtIp);
        EditText edtPorta = (EditText) findViewById(R.id.edtPorta);

        Usuario usuario = new Usuario();
        usuario.setNome(edtNome.getText().toString().trim());
        usuario.setTelefone(edtTelefone.getText().toString().trim());
        usuario.setIp(edtIp.getText().toString().trim());
        usuario.setPorta(edtPorta.getText().toString().trim());

        try{
            if(usuario.getNome().isEmpty() || usuario.getTelefone().isEmpty() || usuario.getIp().isEmpty() || usuario.getPorta().isEmpty()){
                throw new Exception("Preencha todos os campos.");
            }

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            LoginAsyncTask loginAsyncTask = new LoginAsyncTask(view.getContext(), usuario);
            loginAsyncTask.execute(gson.toJson(usuario));
        }catch (Exception e){
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
