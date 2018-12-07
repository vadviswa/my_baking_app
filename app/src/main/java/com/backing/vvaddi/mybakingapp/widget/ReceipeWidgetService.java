package com.backing.vvaddi.mybakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class ReceipeWidgetService extends IntentService {

    public static final String RECEIPE_LIST = "com.backing.vvaddi.mybakingapp.action.reciepe";

    public ReceipeWidgetService() {
        super("ReceipeWidgetService");
    }

    /**
     * Starts this service to perform WaterPlants action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionWaterPlants(Context context) {
        Intent intent = new Intent(context, ReceipeWidgetService.class);
        intent.setAction(RECEIPE_LIST);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (RECEIPE_LIST.equals(action)) {
                handleActionWaterPlants();
            }
        }
    }

    /**
     * Handle action WaterPlant in the provided background thread with the provided
     * parameters.
     */
    private void handleActionWaterPlants() {

    }
}
