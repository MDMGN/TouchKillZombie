package com.mgn.touchkillz;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class DrawView extends SurfaceView implements Runnable{

    Typeface tpf;
    Thread hilo=null;
    AnimatedObject obj;
    SurfaceHolder holder;

    volatile boolean running = false;
    public DrawView(Context context) {

        super(context);
        holder=getHolder();
        AssetManager asm=context.getAssets();
        String path="fonts/edosz.ttf";
        tpf=Typeface.createFromAsset(asm,path);
        obj = new AnimatedObject(context, 430, 519, 430, 519, 1, 6, 200, 500 + 200, "zombie_load.png");
    }



    public void resume()
    {
        hilo=new Thread(this);
        running=true;
        hilo.start();
    }
    public void pause()
    {
        running=false;

        while(true)
        {
            try {
                hilo.join();
                break;
            }
            catch (InterruptedException ex)
            {
                continue;
            }
        }
        hilo=null;
    }
    @Override
    public void run() {
        Canvas c;
        long ahora=System.currentTimeMillis();
        Random r=new Random(System.currentTimeMillis());

        while (running){

            if(System.currentTimeMillis() - ahora >83){
                obj.sigFrame();
                int n=r.nextInt(25);
                obj.avanzar(n);
                ahora=System.currentTimeMillis();
            }


            if(!holder.getSurface().isValid()){
                continue;
            }

            c=holder.lockCanvas();
            this.draw(c);
            holder.unlockCanvasAndPost(c);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint p = new Paint();
        p.setTypeface(tpf);
        p.setColor(Color.RED);
        p.setTextSize(100);
        canvas.drawText("Cargando... ", 220, 1350, p);
        obj.draw(canvas);
    }
}
