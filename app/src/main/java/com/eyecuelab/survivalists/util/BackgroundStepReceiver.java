package com.eyecuelab.survivalists.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eyecuelab.survivalists.services.BackgroundStepService;

/**
 * Created by eyecuelab on 5/16/16.
 */
public class BackgroundStepReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, BackgroundStepService.class));

    }
}
