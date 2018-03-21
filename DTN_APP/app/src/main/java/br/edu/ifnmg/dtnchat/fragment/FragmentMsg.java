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
import android.widget.ListView;

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
import br.edu.ifnmg.dtnchat.entidade.Cliente;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;
import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 17/01/18.
 */

@SuppressLint("ValidFragment")
public class FragmentMsg extends Fragment {

    private Usuario usuario;
    private List<Mensagem> mensagens;

    @SuppressLint("ValidFragment")
    public FragmentMsg(Usuario usuario){
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        try{
            this.mensagens = new ArrayList<>();

            DatabaseHelper dh = new DatabaseHelper(view.getContext());
            MensagemDAO mensagemDAO = new MensagemDAO(dh.getConnectionSource());

            QueryBuilder<Mensagem, ?> qb = mensagemDAO.queryBuilder();
            qb.orderBy("data_envio", false);
            List<Mensagem> mensagensTemp = qb.query();

            for(Mensagem m1 : mensagensTemp){
                boolean status = true;
                for(Mensagem m2 : this.mensagens){
                    if(m1.getEmissor() == m2.getEmissor()){
                        status = false;
                    }
                }
                if(status && m1.getDestinatario() == this.usuario.getId_servidor()){
                    this.mensagens.add(m1);
                }
            }

            ListView lvUltimaMsg = (ListView) view.findViewById(R.id.lvUltimaMsg);
            lvUltimaMsg.setAdapter(new MsgAdapter(view.getContext(), this.mensagens));
            lvUltimaMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try{
                        Mensagem mensagem = mensagens.get(i);
                        DatabaseHelper dh = new DatabaseHelper(view.getContext());
                        ClienteDAO clienteDAO = new ClienteDAO(dh.getConnectionSource());

                        Map<String, Object> values = new HashMap<>();
                        values.put("id_servidor", mensagem.getEmissor());
                        List<Cliente> clientes = clienteDAO.queryForFieldValues(values);
                        if(!clientes.isEmpty()){
                            Intent intent = new Intent(view.getContext(), MensagemActivity.class);
                            intent.putExtra("cliente", clientes.get(0));
                            startActivity(intent);
                        }
                    }catch (Exception e){
                        Log.e("FAIL", e.getMessage());
                    }
                }
            });
        }catch (Exception e){
            Log.e("FAIL", e.getMessage());
        }

        return view;
    }

}
