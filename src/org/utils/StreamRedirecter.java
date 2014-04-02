package org.utils;

import org.interfaces.MyCallBack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class StreamRedirecter extends Thread
{
    private OutputStream out;
    private InputStream in;
    private AtomicBoolean run = new AtomicBoolean(true);
    private MyCallBack callback;

    public StreamRedirecter(OutputStream out, InputStream in, MyCallBack callback) {
        this.out = out;
        this.in = in;
        this.callback = callback;
    }

    @Override
    public void run()
    {
        while (run.get())
        {

                byte[] buffer = new byte[1024];
                int len;

                //Write InputStream into OutputStream
                try
                {
                    while ((len = in.read(buffer)) != -1)
                    {

                        if(len <= 0 && null != callback) {
                            if(!callback.execute()) {
                                break;
                            }

                        }

                        out.write(buffer, 0, len);
                        System.out.println(String.format("%s : %s", Thread.currentThread().getName(), new String(buffer)));

                        sleep(len > 0 ? 0 : 15);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    break;
                }

        }
    }

    public void setRun(AtomicBoolean run) {
        this.run = run;
    }

    public void thisIsTheEndMyFriend(){
        run.lazySet(false);
    }
}
