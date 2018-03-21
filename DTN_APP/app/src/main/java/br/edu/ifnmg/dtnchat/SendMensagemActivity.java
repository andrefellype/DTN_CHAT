package br.edu.ifnmg.dtnchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.MensagemDAO;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Cliente;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;
import br.edu.ifnmg.dtnchat.entidade.Usuario;

public class SendMensagemActivity extends AppCompatActivity {

    private Cliente cliente;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mensagem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.cliente = (Cliente) getIntent().getExtras().getSerializable("cliente");

        try {
            DatabaseHelper dh = new DatabaseHelper(SendMensagemActivity.this);
            UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());

            this.usuario = usuarioDAO.queryForAll().get(0);

            EditText edtDestinatario = (EditText) findViewById(R.id.edtDestinatario);
            edtDestinatario.setText(this.cliente.getNome());
        } catch (Exception e) {
            Log.e("FAIL", "SQLException: " + e.getMessage());
        }
    }

    public void cancelar(View view){
        Intent intent = new Intent(view.getContext(), MensagemActivity.class);
        intent.putExtra("cliente", this.cliente);
        startActivity(intent);
        finish();
    }

    public void enviar(View view){
        try{
            DatabaseHelper dh = new DatabaseHelper(view.getContext());
            MensagemDAO mensagemDAO = new MensagemDAO(dh.getConnectionSource());

            EditText edtMensagem = (EditText) findViewById(R.id.edtMensagem);

            Mensagem mensagem = new Mensagem();
            mensagem.setId_servidor(0);
            mensagem.setEmissor(this.usuario.getId_servidor());
            mensagem.setDestinatario(this.cliente.getId_servidor());
            mensagem.setData_envio(new Date());
            mensagem.setTexto(edtMensagem.getText().toString().trim());
            mensagem.setStatus(false);
            if(mensagem.getTexto().isEmpty()) {
                throw new Exception("Preencha todos os campos.");
            }
            mensagemDAO.create(mensagem);
            Toast.makeText(view.getContext(), "Mensagem enviada com sucesso.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(view.getContext(), MensagemActivity.class);
            intent.putExtra("cliente", cliente);
            startActivity(intent);
            finish();
        }catch (Exception e){
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
