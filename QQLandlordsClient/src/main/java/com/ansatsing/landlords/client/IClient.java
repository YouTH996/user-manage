package com.ansatsing.landlords.client;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public interface IClient {
    public void connectServer() throws UnknownHostException, SocketException, IOException;
}
