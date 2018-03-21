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
import br.edu.ifnmg.dtnchat.adapter.ClienteAdapter;
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
public class FragmentCliente extends Fragment {

    private Usuario usuario;
    private List<Cliente> clientes;

    @SuppressLint("ValidFragment")
    public FragmentCliente(Usuario usuario){
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        try{
            this.clientes = new ArrayList<>();

            DatabaseHelper dh = new DatabaseHelper(view.getContext());
            ClienteDAO clienteDAO = new ClienteDAO(dh.getConnectionSource());

            List<Cliente> clientesTemp = clienteDAO.queryForAll();

            for(Cliente c1 : clientesTemp){
                if(c1.getId_servidor() != this.usuario.getId_servidor()){
                    this.clientes.add(c1);
                }
            }

            ListView lvClientes = (ListView) view.findViewById(R.id.lvClientes);
            lvClientes.setAdapter(new ClienteAdapter(view.getContext(), this.clientes));
            lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cliente cliente = clientes.get(i);

                    Intent intent = new Intent(view.getContext(), MensagemActivity.class);
                    intent.putExtra("cliente", cliente);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            Log.e("FAIL", e.getMessage());
        }

        return view;
    }

}
