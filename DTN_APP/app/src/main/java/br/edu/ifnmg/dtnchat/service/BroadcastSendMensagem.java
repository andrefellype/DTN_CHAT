package br.edu.ifnmg.dtnchat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by andrefellype on 16/01/18.
 */

public class BroadcastSendMensagem extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent("SET_MENSAGEM");
        context.startService(intent);
    }

}
