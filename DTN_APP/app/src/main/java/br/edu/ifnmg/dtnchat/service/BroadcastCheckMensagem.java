package br.edu.ifnmg.dtnchat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by andrefellype on 16/01/18.
 */

public class BroadcastCheckMensagem extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent("CHECK_MENSAGEM");
        context.startService(intent);
    }

}
