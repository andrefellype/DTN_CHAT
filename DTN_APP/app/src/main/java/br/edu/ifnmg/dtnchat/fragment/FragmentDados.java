package br.edu.ifnmg.dtnchat.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifnmg.dtnchat.MensagemActivity;
import br.edu.ifnmg.dtnchat.R;
import br.edu.ifnmg.dtnchat.adapter.MsgAdapter;
import br.edu.ifnmg.dtnchat.dao.ClienteDAO;
import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.MensagemDAO;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Cliente;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;
import br.edu.ifnmg.dtnchat.entidade.Usuario;
import br.edu.ifnmg.dtnchat.ws.UpdateClienteAsyncTask;

/**
 * Created by andrefellype on 17/01/18.
 */

@SuppressLint("ValidFragment")
public class FragmentDados extends Fragment {

    private Usuario usuario;
    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtIp;
    private EditText edtPorta;
    private Button btnSalvar;

    @SuppressLint("ValidFragment")
    public FragmentDados(Usuario usuario){
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dados, container, false);

        this.edtNome = (EditText) view.findViewById(R.id.edtNome);
        this.edtTelefone = (EditText) view.findViewById(R.id.edtTelefone);
        this.edtIp = (EditText) view.findViewById(R.id.edtIp);
        this.edtPorta = (EditText) view.findViewById(R.id.edtPorta);
        this.btnSalvar = (Button) view.findViewById(R.id.btnSalvar);

        try{
            DatabaseHelper dh = new DatabaseHelper(view.getContext());
            UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());

            this.usuario = usuarioDAO.queryForAll().get(0);

            edtNome.setText(usuario.getNome());
            edtTelefone.setText(usuario.getTelefone());
            edtIp.setText(usuario.getIp());
            edtPorta.setText(usuario.getPorta());

            this.btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usuario.setNome(edtNome.getText().toString().trim());
                    usuario.setTelefone(edtTelefone.getText().toString().trim());
                    usuario.setIp(edtIp.getText().toString().trim());
                    usuario.setPorta(edtPorta.getText().toString().trim());

                    if(usuario.getNome().isEmpty() || usuario.getTelefone().isEmpty() || usuario.getIp().isEmpty() || usuario.getPorta().isEmpty()) {
                        Toast.makeText(view.getContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    } else {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        UpdateClienteAsyncTask updateClienteAsyncTask = new UpdateClienteAsyncTask(view.getContext(), usuario);
                        updateClienteAsyncTask.execute(gson.toJson(usuario));
                    }
                }
            });
        }catch (Exception e){
            Log.e("FAIL", e.getMessage());
        }

        return view;
    }

}
