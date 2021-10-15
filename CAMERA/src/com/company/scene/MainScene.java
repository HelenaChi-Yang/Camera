package com.company.scene;

import com.company.scene_process.Camera;
import com.company.scene_process.SmallMap;
import com.company.Global;
import com.company.controllers.SceneController;
import com.company.gameObj.Aircraft;
import com.company.gameObj.Rect;
import com.company.gametest9th.utils.CommandSolver;
import com.company.gametest9th.utils.Path;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainScene extends Scene implements CommandSolver.KeyListener , CommandSolver.MouseCommandListener {
    private Aircraft plane1;
    private int planeSpeed;//飛機速度
    private Image img;
    private Camera camera;
    private Aircraft plane2;
    private Aircraft plane3;
    private int imageWidth;
    private int imageHeight;
    private SmallMap smallMap;

    @Override
    public void sceneBegin() {
        plane1 = new Aircraft(Global.SCREEN_X/2, Global.SCREEN_Y/2);
        planeSpeed=8;//飛機速度
        img= SceneController.getInstance().imageController().tryGetImage(new Path().img().background().Green());
        imageHeight=1000;
        imageWidth=1600;
        camera =new Camera(imageWidth,imageHeight);
        camera.setScreenMovingRange(100,10);
        plane2=new Aircraft(600, 590);
        plane3=new Aircraft(300, 350);
//        camera.setTarget(plane1);
        camera.setChaseX(10);
        camera.setChaseY(10);
        smallMap = new SmallMap(0.2,0.2);

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        camera.startCamera(g);
        g.drawImage(img,0,0,imageWidth,imageHeight,null);
        plane1.paint(g);
        plane2.paint(g);
        plane3.paint(g);
        camera.paint(g);
        camera.endCamera(g);

        smallMap.start(g);
        //設定小地圖位置
        g.drawImage(img,100,200,imageWidth,imageHeight,null);
        //畫布移動(根據小地圖位置)
        g.translate(100,200);
        Image planeImg = SceneController.getInstance().imageController().tryGetImage(new Path().img().actors().aircraft());
        smallMap.paint(g, plane1,planeImg, plane1.painter().width(), plane1.painter().height());
        Image batmanImg = SceneController.getInstance().imageController().tryGetImage(new Path().img().actors().batman());
        smallMap.paint(g,plane2,batmanImg, plane1.painter().width(), plane1.painter().height());
        Rect plane3Painter = plane3.painter();
        smallMap.paint(g,plane3,Color.red,plane3Painter.width(),plane3Painter.height());
    }


    @Override
    public void update() {
        camera.update();
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return this;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
            return this;
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        if(commandCode==Global.LEFT && plane1.collider().left()>0){
            plane1.translateX(-planeSpeed);
        }
        if (commandCode==Global.RIGHT && plane1.collider().right()<imageWidth){
            plane1.translateX(planeSpeed);
        }
        if(commandCode==Global.DOWN && plane1.collider().bottom()<imageHeight){
            plane1.translateY(planeSpeed);
        }
        if(commandCode==Global.UP && plane1.collider().top()>0){
            plane1.translateY(planeSpeed*-1);
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        camera.keyReleased(commandCode,trigTime);
    }

    @Override
    public void keyTyped(char c, long trigTime) {
    }

    @Override
    public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        camera.mouseTrig(e, state, trigTime);
    }
}
