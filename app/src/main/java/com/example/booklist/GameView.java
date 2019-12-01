package com.example.booklist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread;//定义一个绘制界面的线程对象
    private ArrayList<Sprite>sprites=new ArrayList<>();
    public GameView(Context context) {
        super(context);
        surfaceHolder=this.getHolder();
        surfaceHolder.addCallback(this);
        sprites.add(new Sprite(R.drawable.boo_icon));

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(null==drawThread){//创建线程
            drawThread=new DrawThread();
            drawThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(null!=drawThread){//终止线程
            drawThread.stopThread();
            drawThread=null;
        }

    }

    private class DrawThread extends Thread{
        private boolean beAlive=false;//用于控制线程结束
        public void run(){
            beAlive=true;
            while(beAlive){
                Canvas canvas=null;
                try{
                    synchronized (surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas();//同步锁定
                        canvas.drawColor(Color.WHITE);//白色填充
                        for (int i = 0; i < sprites.size(); i++) sprites.get(i).move();//让所有精灵移动
                        for (int i = 0; i < sprites.size(); i++) sprites.get(i).draw(canvas);//让所有精灵画图
                }
            }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (null != canvas) {//通知界面更新
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }try {
                    Thread.sleep(10);//休眠十毫秒后继续下一轮刷新
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                }
        }
        public void stopThread(){
            beAlive=false;
            while (true){
               try{
                   this.join();//保证run方法执行完毕
                   break;
               }catch (InterruptedException e){
                   e.printStackTrace();
               }
            }
        }
    }

    private class Sprite {
        private int resourceId;
        private int x,y;
        private double direction;
        public Sprite(int resourceId) {
            this.resourceId=resourceId;
            x= (int) (Math.random()*getWidth());
            y= (int) (Math.random()*getHeight());
        }
        public void move(){
            x += 15 * Math.cos(direction);
            if (x < 0) x = getWidth();
            else if (x > getWidth()) x = 0;
            y += 15 * Math.sin(direction);
            if (y < 0) y = getHeight();
            else if (y > getHeight()) y = 0;
            if (Math.random() < 0.05) direction = Math.random() * 2 * Math.PI;
        }
        public void draw(Canvas canvas){//在画布指定位置上画出图标
            Drawable drawable=getContext().getResources().getDrawable(R.drawable.boo_icon);
            Rect drawableRect=new Rect(x,y,x+drawable.getIntrinsicWidth(),y+drawable.getIntrinsicHeight());
            drawable.setBounds(drawableRect);
            drawable.draw(canvas);
        }
    }
}
