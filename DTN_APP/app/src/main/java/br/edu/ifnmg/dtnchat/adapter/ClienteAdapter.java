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

public class ClienteAdapter extends BaseAdapter {

    private Context context;
    private List<Cliente> clientes;

    public ClienteAdapter(Context context, List<Cliente> clientes){
        this.context = context;
        this.clientes = clientes;
    }

    @Override
    public int getCount() {
        return this.clientes.size();
    }

    @Override
    public Object getItem(int i) {
        return this.clientes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.lv_cliente, null);

        Cliente cliente = this.clientes.get(i);

        TextView tvNome = (TextView) layout.findViewById(R.id.tvNome);
        TextView tvTelefone = (TextView) layout.findViewById(R.id.tvTelefone);

        tvNome.setText(cliente.getNome());
        tvTelefone.setText(cliente.getTelefone());

        return layout;
    }


}
