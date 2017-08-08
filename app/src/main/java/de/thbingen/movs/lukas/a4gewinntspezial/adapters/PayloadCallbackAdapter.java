package de.thbingen.movs.lukas.a4gewinntspezial.adapters;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 01.08.2017
 *          <p>
 *          Abstrakte Klasse, die die Methode onPayloadTransferUpdate() leer implementiert um im
 *          späteren Code Platz zu sparen.
 */
public abstract class PayloadCallbackAdapter extends PayloadCallback {

    // Muss implenentiert werden um eine Nachricht des anderen Geräts zu empfangen.
    public abstract void onPayloadReceived(String s, Payload payload);

    // Wird nicht benötigt daher leer implentiert
    public void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate) {}

}
