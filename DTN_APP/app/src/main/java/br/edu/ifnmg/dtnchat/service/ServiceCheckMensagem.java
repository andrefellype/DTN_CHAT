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
import java.util.List;

import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Usuario;
import br.edu.ifnmg.dtnchat.ws.CheckMensagemAsyncTask;

/**
 * Created by andrefellype on 17/01/18.
 */

public class ServiceCheckMensagem extends Service {

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
                usuario = usuarioDAO.queryForAll().get(0);
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
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    CheckMensagemAsyncTask checkMensagemAsyncTask = new CheckMensagemAsyncTask(context, usuario);
                    checkMensagemAsyncTask.execute(gson.toJson(usuario));
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
