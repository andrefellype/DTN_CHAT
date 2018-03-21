package br.edu.ifnmg.dtnchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Usuario;
import br.edu.ifnmg.dtnchat.service.ServiceCheckMensagem;
import br.edu.ifnmg.dtnchat.service.ServicePushCliente;
import br.edu.ifnmg.dtnchat.service.ServicePushMensagem;
import br.edu.ifnmg.dtnchat.service.ServiceSendMensagem;

public class BeginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        try{
            DatabaseHelper dh = new DatabaseHelper(this);
            UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());

            List<Usuario> usuarios = usuarioDAO.queryForAll();
            if(!usuarios.isEmpty()){

                Intent it = new Intent(this, ServicePushCliente.class);
                startService(it);

                it = new Intent(this, ServiceSendMensagem.class);
                startService(it);

                it = new Intent(this, ServicePushMensagem.class);
                startService(it);

                it = new Intent(this, ServiceCheckMensagem.class);
                startService(it);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            Log.e("FAIL", e.getMessage());
        }
    }
}
