package org.impl;

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
    private MyCallBack call;

    public StreamRedirecter(OutputStream out, InputStream in, MyCallBack call) {
        this.out = out;
        this.in = in;
        this.call = call;
    }

    @Override
    public void run()
    {
        while (run.get())
        {
            try
            {

                byte[] buffer = new byte[1024];
                int len;

                //Write InputStream into OutputStream
                try
                {
                    while ((len = in.read(buffer)) != -1)
                    {
                        sleep(15);
                        out.write(buffer, 0, len);

                        if(len <= 0)
                            if(call != null)
                                call.execute();

                        System.out.println(String.format("%s : %s", Thread.currentThread().getName(), new String(buffer)));
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
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
