package de.thbingen.movs.lukas.a4gewinntspezial.adapters;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

public abstract class PayloadCallbackAdapter extends PayloadCallback {

    public abstract void onPayloadReceived(String s, Payload payload);

    public void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate) {}

}
