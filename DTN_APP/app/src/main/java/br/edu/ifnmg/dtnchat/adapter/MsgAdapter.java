package br.edu.ifnmg.dtnchat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifnmg.dtnchat.R;
import br.edu.ifnmg.dtnchat.dao.ClienteDAO;
import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.entidade.Cliente;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;

/**
 * Created by andrefellype on 17/01/18.
 */

public class MsgAdapter extends BaseAdapter {

    private Context context;
    private List<Mensagem> mensagens;

    public MsgAdapter(Context context, List<Mensagem> mensagens){
        this.context = context;
        this.mensagens = mensagens;
    }

    @Override
    public int getCount() {
        return this.mensagens.size();
    }

    @Override
    public Object getItem(int i) {
        return this.mensagens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.lv_mensagem, null);

        try{
            Mensagem mensagem = this.mensagens.get(i);

            DatabaseHelper dh = new DatabaseHelper(this.context);
            ClienteDAO clienteDAO = new ClienteDAO(dh.getConnectionSource());

            TextView tvUsuario = (TextView) layout.findViewById(R.id.tvUsuario);
            TextView tvData = (TextView) layout.findViewById(R.id.tvData);

            Map<String, Object> values = new HashMap<>();
            values.put("id_servidor", mensagem.getEmissor());
            List<Cliente> clientes = clienteDAO.queryForFieldValues(values);

            if(!clientes.isEmpty()) {
                tvUsuario.setText(clientes.get(0).getNome());
            }

            String data = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(mensagem.getData_envio());
            tvData.setText(data);

        }catch (Exception e){
            Log.e("FAIL", e.getMessage());
        }

        return layout;
    }


}
