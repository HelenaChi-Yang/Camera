package com.company.gameObj;

import com.company.Global;
import com.company.controllers.ImageResourceController;
import com.company.controllers.SceneController;
import com.company.gameObj.GameObject;
import com.company.gametest9th.utils.CommandSolver;
import com.company.gametest9th.utils.Delay;
import com.company.gametest9th.utils.GameKernel;
import com.company.gametest9th.utils.Path;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aircraft extends GameObject implements GameKernel.GameInterface {

    public Aircraft(int x, int y) {
        super(x, y, 55, 55, x, y, 50, 50);
        dir = Global.Direction.RIGHT;
        img = SceneController.getInstance().imageController().tryGetImage(new Path().img().actors().aircraft());
//        img = SceneController.getInstance().imageController().tryGetImage("/resources/img/actors/airplane1.png");

        hitCount=0;
        aircraftBlood=3;
        planeSpeed=8;
        delay = new Delay(120);
        delay.loop();
d =1;
    }

    private Image img;
    private Global.Direction dir;
    private int hitCount;//目前被敵軍打到次數
    private int aircraftBlood;//飛機血量
    private int planeSpeed;

    public int getPlaneSpeed(){
        return planeSpeed;
    }
    public int getHitCount(){
        return hitCount;
    }
    public boolean isDead(){
        return hitCount>=aircraftBlood;
    }
    public void hit(){
        hitCount++;
    }
    private Delay delay;
    private int d;

public int time=1;
    public int setTime;
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), painter().width()*time, painter().height()*time, null);
    }

    @Override
    public void update() {
        translateX(5*d);

        //撞到牆壁改方向
        if(painter().right() >= 1500) {
            d = -1;
        }
        //撞到牆變改方向
        if(painter().left() <= 0) {
            d = 1;
        }

    }

}
