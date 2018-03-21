package br.edu.ifnmg.dtnchat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
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
import br.edu.ifnmg.dtnchat.entidade.Usuario;

/**
 * Created by andrefellype on 17/01/18.
 */

public class MsgUniqueAdapter extends BaseAdapter {

    private Context context;
    private Usuario usuario;
    private List<Mensagem> mensagens;

    public MsgUniqueAdapter(Context context, Usuario usuario, List<Mensagem> mensagens){
        this.context = context;
        this.usuario = usuario;
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
        View layout = inflater.inflate(R.layout.lv_mensagem_unique, null);

        Mensagem mensagem = this.mensagens.get(i);

        TextView tvTexto = (TextView) layout.findViewById(R.id.tvTexto);
        TextView tvStatus = (TextView) layout.findViewById(R.id.tvStatus);

        tvTexto.setText(mensagem.getTexto());
        tvTexto.setGravity(mensagem.getEmissor() == usuario.getId_servidor() ? Gravity.RIGHT : Gravity.LEFT);
        tvStatus.setGravity(mensagem.getEmissor() == usuario.getId_servidor() ? Gravity.RIGHT : Gravity.LEFT);

        int cor = Color.BLACK;
        String msgStatus = (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(mensagem.getData_envio());

        if(mensagem.getId_servidor() == 0){
            cor = Color.GREEN;
            msgStatus += " Enviando...";
        } else {
            if(!mensagem.isStatus()){
                cor = Color.LTGRAY;
                msgStatus += " Enviado";
            } else {
                if(mensagem.getEmissor() == usuario.getId_servidor()) {
                    cor = Color.DKGRAY;
                    msgStatus += " Entregue";
                } else {
                    cor = Color.BLUE;
                    msgStatus += " Recebido";
                }
            }
        }

        tvTexto.setTextColor(cor);
        tvStatus.setText(msgStatus);

        return layout;
    }


}
