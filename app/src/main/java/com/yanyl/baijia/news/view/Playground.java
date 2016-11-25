package com.yanyl.baijia.news.view;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.yanyl.baijia.news.bean.Dot;
import com.yanyl.baijia.news.common.Const;
import com.yanyl.baijia.news.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Vector;

/**
 * Created by yanyl on 2016/10/4.
 * 自定义 组件
 *
 * Playground 继承 SurfaceView
 *
 */
public class Playground extends SurfaceView implements View.OnTouchListener{


    private static int step;//记录围捕 的总数
    private static final int COL=10;
    private static  final int ROW=10;
    //默认添加路障的数量
    private static  final int BLOCK=15;

    private static   int WIDTH=65;
    private Dot matrix[][];
    private Dot cat;//猫的元素点

    private List<Integer> score;

    private int grid;
    public Playground(Context context ,int grid) {
        super(context);
        this.grid=grid;
        getHolder().addCallback(callback);
        matrix=new Dot[ROW][COL];

        for (int i=0;i<ROW;i++){
            for (int j=0;j<COL;j++){
                matrix[i][j]=new Dot(j,i);
            }
        }
        //设置监听事件
        setOnTouchListener(this);
        //初始化
        initGame();
    }

    private Dot getDot(int x,int y){

        return matrix[y][x];
    }

    /**
     * 判断是否位于边界
     * @return
     */
    private boolean isAtEdage(Dot d){
        if (d.getX()*d.getY()==0||d.getX()+1==COL||d.getY()+1==ROW){
            return true;
        }
        return false;
    }

    /**
     * 获取四周的点
     * @param one
     * @param dir
     * @return
     */
    private Dot getNeighbour(Dot one,int dir){
        switch (dir){
            case 1:
                return getDot(one.getX()-1,one.getY());
            case 2:
                if (one.getY()%2==0){
                    return  getDot(one.getX()-1,one.getY()-1);
                }else {
                    return getDot(one.getX(),one.getY()-1);
                }
            case 3:
                if (one.getY()%2==0){
                    return  getDot(one.getX(),one.getY()-1);
                }else {
                    return   getDot(one.getX()+1,one.getY());
                }

            case 4:
                return  getDot(one.getX()+1,one.getY());

            case 5:
                if (one.getY()%2==0){
                    return  getDot(one.getX(),one.getY()+1);
                }else {
                    return getDot(one.getX()+1,one.getY()+1);
                }

            case 6:
                if (one.getY()%2==0){
                    return  getDot(one.getX()-1,one.getY()+1);
                }else {
                    return  getDot(one.getX(),one.getY()+1);
                }
        }
        return null;
    }

    /**
     * 获取距离障碍的路长
     * @param one
     * @param dir
     * @return
     */
    private int getDistance(Dot one,int dir){
        int distance=0;
        if (isAtEdage(one)){
            return 1;
        }
        Dot ori=one,next;

        while (true) {
            next = getNeighbour(ori, dir);
            //路障
            if (next.getStatus() == Dot.STATUS_ON) {
                return distance * -1;
            }
            if (isAtEdage(next)) {
                distance++;
                return distance;
            }
            distance++;
            ori = next;
        }

    }
    private void moveTo(Dot one){
        one.setStatus(Dot.STATUS_IN);
        getDot(cat.getX(),cat.getY()).setStatus(Dot.STATUS_OFF);
        cat.setXY(one.getX(),one.getY());
    }
    private void move(){
        if (isAtEdage(cat)){
            lose();
            return;
        }
        //可行走的路径
        Vector<Dot> avaliable=new Vector<Dot>();
        //6个方向可以走的集合
        Vector<Dot> positive=new Vector<Dot>();
        //记录方向的集合
        HashMap<Dot,Integer> al=new HashMap<Dot, Integer>();
        //遍历6个方向
        for (int i=1;i<7;i++){
            Dot n=getNeighbour(cat,i);
            if (n.getStatus()==Dot.STATUS_OFF){
                avaliable.add(n);
                al.put(n,i);
                if (getDistance(n,i)>0){
                    positive.add(n);
                }
            }
        }
        if (avaliable.size()==0){
            win();
        }else if (avaliable.size()==1) {
            moveTo(avaliable.get(0));
        }else {
            Dot best=null;//最佳点
            if (positive.size()!=0){//存在可以行走了路径

                int min=999;
                //寻找最短路径
                for (int i=0;i<positive.size();i++){
                    int dis=getDistance(positive.get(i),al.get(positive.get(i)));
                    if (dis<=min){
                        min=dis;
                        best=positive.get(i);
                    }
                }
            }else{// 所有方向存在路障

                int max=0;
                for (int i=0;i<avaliable.size();i++){
                    //遍历距离每个障碍点的距离
                    int k=getDistance(avaliable.get(i),al.get(avaliable.get(i)));//k<0
                    if(k<=max){
                        max=k;
                        best=avaliable.get(i);
                    }
                }
            }
            //移动到最佳路径
            moveTo(best);
        }
    }

    private void win() {
        score=new ArrayList<Integer>();
        score.add(step);
        int min=0;
        for (int i=0;i<score.size();i++){
            if (min<=score.get(i)){
                min=score.get(i);
            }
        }

       Util.showToast(getContext(),
                "you win with "+step+"  步"+"时间:"+Util.getTime()+"\n 最好成绩:"+min+"步");

    }

    private void lose() {
        Util.showToast(getContext(),
                "you fail with "+step+"  步 \n时间:"+Util.getTime());

    }
    /**
     * 绘制 图案
     */
    private void redraw(){
        //
        Canvas c= getHolder().lockCanvas();
        c.drawColor(Color.LTGRAY);
       //初始化Paint 对象
        Paint paint=new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        for (int i=0;i<ROW;i++){
            //偏移量
            int offset=0;
            if (i%2!=0){
                offset=WIDTH/2;
            }
            for (int j=0;j<COL;j++){
               Dot one=getDot(j,i);
                //不同状态下的颜色点颜色设置
                switch (one.getStatus()){
                    case Dot.STATUS_IN:
                        paint.setColor(0xFFFF0000);
                        break;

                    case Dot.STATUS_ON:
                        paint.setColor(0xFFFFAA00);
                        break;
                    case Dot.STATUS_OFF:
                        paint.setColor(0xFFEEEEEE);
                        break;
                    default:
                        break;
                }
                //画界面
                /**
                 * RectF (float left, 左方
                 float top,  上方
                 float right, 右方
                 float bottom  下方)
                 */
                c.drawOval(new RectF
                                (
                                        one.getX()*WIDTH+offset,
                                        one.getY()*WIDTH,
                                        (one.getX()+1)*WIDTH+offset,
                                        (one.getY()+1)*WIDTH
                                ),
                        paint);
            }

        }
        ////取消锁定
        getHolder().unlockCanvasAndPost(c);
    }
    SurfaceHolder.Callback callback=new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            redraw();
        }

        @Override
        /**
         * 改变Surface 时调用
         */
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            WIDTH=width/(COL+1);
            redraw();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    /***
     * 初始化游戏
     */

    private void initGame(){
        step=0;
        for (int i=0;i<ROW;i++){
            for (int j=0;j<COL;j++){
                matrix[i][j].setStatus(Dot.STATUS_OFF);
            }
        }

        if (grid== Const.DIFFERICAT){
            //随机设置猫的初始位置
            int x0= (int) ((Math.random()*1000)%COL);
            int y0= (int) ((Math.random()*1000)%ROW);
            cat=new Dot(x0,y0);
            getDot(x0,y0).setStatus(Dot.STATUS_IN);
        }else if (grid==Const.EASY){
            // 将点（4,5）设置为猫的初始化位置
            cat=new Dot(4,5);
            getDot(4,5).setStatus(Dot.STATUS_IN);
        }







        //随机设置路障
        for (int i=0;i<BLOCK;){
            int x= (int) ((Math.random()*1000)%COL);
            int y= (int) ((Math.random()*1000)%ROW);
            if (getDot(x,y).getStatus()==Dot.STATUS_OFF){
                getDot(x,y).setStatus(Dot.STATUS_ON);
                i++;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_UP){
            step++;
            int x,y;
            //y坐标
            y= (int) (event.getY()/WIDTH);
            //偶数行
            if (y%2==0){
                x= (int) (event.getX()/WIDTH);
            }else{
                //奇数行
                x= (int) ((event.getX()-WIDTH/2)/WIDTH);
            }
            //超出边界初始化游戏
            if (x+1>COL||y+1>ROW){
                initGame();
            }else if (getDot(x,y).getStatus()==Dot.STATUS_OFF){
                getDot(x,y).setStatus(Dot.STATUS_ON);
                move();
            }
            redraw();
        }
        return true;
    }
}