package com.mgn.touchkillz;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

public class AnimatedObject {

    int FrmWidth,FrmHeight;//Tamaño del fotograma en el archivo.
    int TgtWidth,TgtHeight;//Tamaño del fotograma en pantalla.
    int nRow, nCol;//Nº de filas y columnas del sprite.
    int posX,posY;//Posición del objeto en pantalla.
    String fichero;
    Bitmap bmpAnimacion;
    int FrmActual,FrmTotal;
    public AnimatedObject(Context con, int FrmWidth, int FrmHeight, int TgtWidth, int TgtHeight, int nRow, int nCol, int  posX, int posY, String Fichero )
    {
        this.FrmWidth=FrmWidth;
        this.FrmHeight=FrmHeight;
        this.TgtHeight=TgtHeight;
        this.TgtWidth=TgtWidth;
        this.nRow=nRow;
        this.nCol=nCol;
        FrmActual=0;
        this.posX=posX;
        this.posY=posY;
        FrmTotal=nRow*nCol;
        this.fichero=Fichero;
        AssetManager asm=con.getAssets();
        try {
            InputStream is=asm.open(fichero);
            bmpAnimacion= BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void avanzar(int x)
    {
        posX+=x;
    }
    public void sigFrame()
    {
        FrmActual +=1;
        FrmActual%=FrmTotal;
    }
    public void draw(Canvas c)
    {
        Rect src,trg;
        trg=new Rect(posX,posY,posX+TgtWidth,posY+TgtHeight);
        int fil,col;//En qué posición está el frame
        fil=FrmActual/nCol;
        col=FrmActual%nCol;
        src=new Rect(col*FrmWidth,fil*FrmHeight,(col*FrmWidth)+FrmWidth,(fil*FrmHeight)+FrmHeight);
        c.drawBitmap(bmpAnimacion,src,trg,new Paint());
    }
    public boolean finished(){
        return posX>1920;
    }
}
