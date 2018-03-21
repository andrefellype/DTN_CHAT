package br.edu.ifnmg.dtnchat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by andrefellype on 16/01/18.
 */

public class BroadcastPushCliente extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent("PUSH_CLIENTE");
        context.startService(intent);
    }

}
