package org.impl;

import org.interfaces.MyCallBack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy
{
        public static void main (String args[])
        {
            /** default port */
            int port = 2000;
            ServerSocket serverProxy = null;
            Socket proxyConnection, serverHttp;

            try
            {
                //org.impl.Proxy server socket
                serverProxy = new ServerSocket(port);
                if (null != serverProxy )
                {
                    try
                    {
                        System.out.println("En attente de connexion sur le port : "+serverProxy.getLocalPort());
                        while (true)
                        {
                            proxyConnection = serverProxy.accept();
                            System.out.println("Nouvelle connexion : "+proxyConnection);

                            //HTTP server socket
                            serverHttp = new Socket("127.0.0.1", 1234);

                            final Socket finalProxyConnection = proxyConnection;
                            MyCallBack call = new MyCallBack() {
                                @Override
                                public void execute()
                                {
                                    try
                                    {
                                        finalProxyConnection.close();
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            StreamRedirecter proxyThread = new StreamRedirecter(serverHttp.getOutputStream(), proxyConnection.getInputStream(), null);
                            StreamRedirecter serverHttpThread = new StreamRedirecter(proxyConnection.getOutputStream(), serverHttp.getInputStream(), call);

                            proxyThread.start();
                            serverHttpThread.start();
                        }
                    }
                    catch (Exception ex)
                    {
                        // Error of connection
                        System.err.println("Une erreur est survenue : "+ex);
                        ex.printStackTrace();
                    }
                }
            }
            catch (IOException ex)
            {
                // For any reason, impossible to create the socket on the required port.
                System.err.println("Impossible de créer un socket serveur sur ce port : "+ex);
                try
                {
                    // trying an anonymous one.
                    serverProxy = new ServerSocket(0);
                }
                catch (IOException ex2)
                {
                    // Impossible to connect!
                    System.err.println("Impossible de créer un socket serveur : "+ex2);
                }
            }
        }
}
