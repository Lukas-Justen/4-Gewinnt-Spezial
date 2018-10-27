package de.thbingen.movs.lukas.a4gewinntspezial.adapters;

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 01.08.2017
 *          <p>
 *          Abstrakte Klasse, die die Methode onEndpointLost() leer implementiert um im späteren
 *          Code Platz zu sparen.
 */
public abstract class EndpointDiscoveryAdapter extends EndpointDiscoveryCallback {

    // Muss implenentiert werden um auf das Finden eines neuen Endpunktes zu reagieren.
    abstract public void onEndpointFound(String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo);

    // Wird nicht benötigt daher leer implentiert
    public void onEndpointLost(String endpointId) {
    }

}
