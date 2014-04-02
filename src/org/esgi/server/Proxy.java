package org.esgi.server;

import org.utils.RoundRobiner;
import org.utils.StreamRedirecter;
import org.interfaces.MyCallBack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy {

    ServerSocket serverProxy = null;
    RoundRobiner roundRobiner;

    public static void main(String args[]) {
        new Proxy(2000, new RoundRobiner.HttpServerAddress("127.0.0.1", 1234) ).run();
    }

    public Proxy(int port, RoundRobiner.HttpServerAddress... addresses) {

        roundRobiner = new RoundRobiner(addresses);

        try {
            serverProxy = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(){
        if (null != serverProxy )
        {
            try
            {
                while (true)
                {
                    final Socket proxyConnection = serverProxy.accept();
                    System.out.println("Nouvelle connexion : "+proxyConnection);

                    //HTTP server socket
                    final Socket serverHttp = roundRobiner.getConnection();//new Socket("127.0.0.1", 1234);

                    MyCallBack call = new MyCallBack() {
                        @Override
                        public boolean execute()
                        {
                            try
                            {
                                if(serverHttp.isClosed()){
                                    proxyConnection.close();
                                    return false;
                                }
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                                return false;
                            }

                            return true;
                        }
                    };

                    StreamRedirecter proxyThread = new StreamRedirecter(serverHttp.getOutputStream(), proxyConnection.getInputStream(), call);
                    StreamRedirecter serverHttpThread = new StreamRedirecter(proxyConnection.getOutputStream(), serverHttp.getInputStream(), call);

                    proxyThread.start();
                    serverHttpThread.start();
                }
            }
            catch (Exception ex)
            {
                System.err.println("Une erreur est survenue : " + ex);
            }
        }

    }
}