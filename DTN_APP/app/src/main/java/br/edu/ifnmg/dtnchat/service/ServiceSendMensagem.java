package br.edu.ifnmg.dtnchat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.MensagemDAO;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Mensagem;
import br.edu.ifnmg.dtnchat.entidade.Usuario;
import br.edu.ifnmg.dtnchat.ws.SendMensagemAsyncTask;

/**
 * Created by andrefellype on 17/01/18.
 */

public class ServiceSendMensagem extends Service{

        private Usuario usuario;
        private Context context;
        public List<Worker> threads = new ArrayList<Worker>();

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate(){
            super.onCreate();
            this.context = this;
            try{
                DatabaseHelper dh = new DatabaseHelper(this);
                UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());
                this.usuario = usuarioDAO.queryForAll().get(0);
            }catch (SQLException e){}
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId){
            Worker w = new Worker(startId);
            w.start();
            threads.add(w);
            return(super.onStartCommand(intent, flags, startId));
        }


        class Worker extends Thread{
            public int startId;
            public boolean ativo = true;
            public Worker(int startId){
                this.startId = startId;
            }
            public void run(){
                while(ativo){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Log.e("FAIL","InterruptedException: "+e.getMessage());
                    }
                    try{
                        DatabaseHelper dh = new DatabaseHelper(context);
                        MensagemDAO mensagemDAO = new MensagemDAO(dh.getConnectionSource());
                        Map<String, Object> values = new HashMap<>();

                        values.put("id_servidor", 0);
                        List<Mensagem> mensagemList = mensagemDAO.queryForFieldValuesArgs(values);
                        for(Mensagem mensagem : mensagemList) {
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            SendMensagemAsyncTask sendMensagemAsyncTask = new SendMensagemAsyncTask(context, usuario, mensagem);
                            sendMensagemAsyncTask.execute(gson.toJson(mensagem));
                        }
                    }catch (SQLException e){}
                }
                stopSelf(startId);
            }
        }


        @Override
        public void onDestroy(){
            super.onDestroy();
            for(int i = 0, tam = threads.size(); i < tam; i++){
                threads.get(i).ativo = false;
            }
        }
}
