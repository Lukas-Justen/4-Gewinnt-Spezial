package de.thbingen.movs.lukas.a4gewinntspezial.adapters;

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;

public abstract class EndpointDiscoveryAdapter extends EndpointDiscoveryCallback {

    abstract public void onEndpointFound(String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo);

    public void onEndpointLost(String endpointId) {}

}
