package br.edu.ifnmg.dtnchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifnmg.dtnchat.adapter.MsgUniqueAdapter;
import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.MensagemDAO;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Cliente;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;
import br.edu.ifnmg.dtnchat.entidade.Usuario;

public class MensagemActivity extends AppCompatActivity {

    private Cliente cliente;
    private Usuario usuario;
    private ListView lvMensagem;
    private List<Mensagem> mensagens;
    private MensagemDAO mensagemDAO;
    private boolean stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.cliente = (Cliente) getIntent().getExtras().getSerializable("cliente");
        this.stop = false;

        TextView edtCliente = (TextView) findViewById(R.id.edtCliente);
        edtCliente.setText(this.cliente.getNome());

        try {
            this.mensagens = new ArrayList<>();
            DatabaseHelper dh = new DatabaseHelper(this);
            UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());
            mensagemDAO = new MensagemDAO(dh.getConnectionSource());

            this.usuario = usuarioDAO.queryForAll().get(0);
            lvMensagem = (ListView) findViewById(R.id.lvMsg);

            List<Mensagem> mensagensTemp = mensagemDAO.queryForAll();
            for(Mensagem m1 : mensagensTemp){
                if(m1.getEmissor() == this.cliente.getId_servidor() || m1.getDestinatario() == this.cliente.getId_servidor()){
                    this.mensagens.add(m1);
                }
            }

            lvMensagem.setAdapter(new MsgUniqueAdapter(this, this.usuario, this.mensagens));
            lvMensagem.setSelection(this.mensagens.size() - 1);

            ProccessList proccessList = new ProccessList();
            proccessList.start();

        }catch (Exception e){
            Log.e("FAIL",e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.msg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.stop = true;
            finish();
            return true;
        } else if(id == R.id.action_send){
            this.stop = true;
            Intent intent = new Intent(MensagemActivity.this, SendMensagemActivity.class);
            intent.putExtra("cliente", cliente);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ProccessList extends Thread{

        public boolean ativo = true;

        public ProccessList(){
        }

        public void run(){
            int x = 1;
            while(ativo && !stop){
                try {
                    int total = 0;
                    List<Mensagem> mensagensTemp = mensagemDAO.queryForAll();
                    for(Mensagem m1 : mensagensTemp) {
                        boolean status = true;
                        for (Mensagem m2 : mensagens) {
                            if(m1.getId_servidor() == m2.getId_servidor() && m1.isStatus() == m2.isStatus()){
                                status = false;
                            }
                        }
                        if(status) {
                            if(m1.getEmissor() == cliente.getId_servidor() || m1.getDestinatario() == cliente.getId_servidor()) {
                                mensagens.add(m1);
                                total++;
                            }
                        }
                    }
                    if(total > 0){
                        stop = true;
                        Intent intent = new Intent(MensagemActivity.this, MensagemActivity.class);
                        intent.putExtra("cliente", cliente);
                        startActivity(intent);
                        finish();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
