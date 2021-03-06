package com.company.scene_process;

import com.company.Global;
import com.company.gameObj.GameObject;
import com.company.gametest9th.utils.CommandSolver;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class Camera extends GameObject implements CommandSolver.MouseCommandListener, CommandSolver.KeyCommandListener {

    private enum State{
        TARGET_MODE, //鏡頭追焦物件
        MOUSE_MODE //滑鼠控制鏡頭縮放
    }

    /** 主要屬性 */

    private final int imgWidth;//背景圖寬度(最大的那張底圖)
    private final int imgHeight;//背景圖高度(最大的那張底圖)

    private double chaseX;//追蹤除數(x軸)
    private double chaseY;//追蹤除數(y軸)

    private State cameraState;//鏡頭控制狀態

    private GameObject target;//主要追蹤對象

    /** 次要屬性 */
    private double multiple;//放大倍率

    /** 設定螢幕移動距離 與判定範圍 */
    private int screenRange;
    private int screenStep;

    private int mouseX;
    private int mouseY;

    /** 預設鏡頭為全景 */
    public Camera(int inputImageWidth, int inputImageHeight){
        super(0,0,Global.SCREEN_X,Global.SCREEN_Y);
        this.chaseX = 1; //追焦初始值
        this.chaseY = 1; //追焦初始值
        this.imgWidth = inputImageWidth;
        this.imgHeight = inputImageHeight;
        this.multiple = 1d;//預設初始倍率
        this.cameraState = State.MOUSE_MODE;//預設初始狀態為滑鼠控制
        this.mouseX = 0;
        this.mouseY = 0;
    }

    /** 可設定鏡頭大小 */
    public Camera(int inputImageWidth, int inputImageHeight,int x, int y, int cameraWidth, int cameraHeight){
        //需要兩個以上的鏡頭，可以設定該鏡頭的座標與寬高
        super(x,y,cameraWidth,cameraHeight);
        this.chaseX = 1; //追焦初始值
        this.chaseY = 1; //追焦初始值
        this.imgWidth = inputImageWidth;
        this.imgHeight = inputImageHeight;
        this.cameraState = State.MOUSE_MODE;//預設初始狀態為滑鼠控制
        //放大功能
        this.multiple = 1d;//預設初始倍率
        //捲軸功能
        this.screenRange = 100;
        this.screenStep = 10;
        this.mouseX = 0;
        this.mouseY = 0;
    }

    /** 主要追蹤角色 */
    public void setTarget(GameObject inputTrackTarget){
        this.target = inputTrackTarget;
        this.cameraState = State.TARGET_MODE;
    }

    /** 追蹤X&Y軸的速度除數 */ //數字越大移動越慢
    public void setChase(int inputChaseX, int inputChaseY){
        this.chaseX = inputChaseX;
        this.chaseY = inputChaseY;
    }

    /** 追蹤X軸的速度除數 */ //數字越大移動越慢
    public void setChaseX(int chaseX){
        this.chaseX = chaseX;
    }

    /** 追蹤Y軸的速度除數 */ //數字越大移動越慢
    public void setChaseY(int chaseY){
        this.chaseY = chaseY;
    }

    /** 開始追焦目標物件 */
    public void trackTarget() {

        int dX = (int)((target.collider().centerX() - collider().centerX()) / chaseX);//鏡頭X軸移動距離
        int dY = (int)((target.collider().centerY() - collider().centerY()) / chaseY);//鏡頭Ｙ軸移動距離

        //超出修正
        if(collider().left() < 0){
            translateX(-collider().left());
        }
        if(collider().right() > imgWidth){
            translateX(imgWidth-collider().right());
        }
        if(collider().top() < 0){
            translateY(-collider().top());
        }
        if(collider().bottom() > imgHeight){
            translateY(imgHeight-collider().bottom());
        }

        //鏡頭還沒碰到最左邊＆最右邊
        if(collider().left() + dX >= 0 && collider().right() + dX <= imgWidth) {
            translateX(dX);
        }

        //鏡頭還沒碰到最上面＆最下面
        if(collider().top() + dY >= 0 && collider().bottom() +dY <= imgHeight) {
            translateY(dY);
        }

    }

    /** 開始使用鏡頭 */
    public void startCamera(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.scale(multiple,multiple);
        g2d.translate(-painter().left() ,-painter().top());
    }

    /** 結束鏡頭 */ //畫布回歸原始位置
    public void endCamera(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(painter().left() ,painter().top());
    }

    /** 每次移動鏡頭邏輯更新 */
    @Override
    public void update() {
        if (cameraState == State.TARGET_MODE) {
            trackTarget(); // 追焦功能
        }else {
            moveScreen(mouseX, mouseY);
        }
    }

    @Override
    public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if(this.cameraState == State.MOUSE_MODE){
            if (state == CommandSolver.MouseState.MOVED) {
                // 在MOUSE_MODE中，預設滑鼠在地圖四角移動，鏡頭朝該方向移動 -> 設定偵測範圍&移動距離
//                moveScreen(e);
                mouseX = e.getX();
                mouseY = e.getY();
            }
        }
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        if(commandCode == KeyEvent.VK_Z) {
            //按下Z放大畫面
            multiple += 0.1;
        }
        if(commandCode == KeyEvent.VK_X) {
            //按下X縮小畫面
            multiple -= 0.1;
        }
        if(commandCode == KeyEvent.VK_A) {
            //按下A切換鏡頭追焦畫面
            if(cameraState == State.MOUSE_MODE) {
                this.cameraState = State.TARGET_MODE;
            } else {
                this.cameraState = State.MOUSE_MODE;
            }
        }
    }

    /** 在Debug模式時，畫出鏡頭外框 */
    @Override
    public void paintComponent(Graphics g) {
        if(Global.IS_DEBUG){
            g.setColor(Color.RED);
            g.drawRect(painter().left(), painter().top(), painter().width(), painter().height());
        }
    }

    /** 設定螢幕移動距離 與判定範圍 */
    public void setScreenMovingRange(int screenRange, int screenStep){
        this.screenRange = screenRange;
        this.screenStep = screenStep;
    }

    /** 移動螢幕 */
    private void moveScreen(int inputX, int inputY) {
        int dX = 0;
        int dY = 0;
        if (inputX > Global.SCREEN_X - screenRange) {
            dX = screenStep;
        }
        if (inputX < screenRange) {
            dX = -screenStep;
        }
        if (inputY > Global.SCREEN_Y - screenRange) {
            dY = screenStep;
        }
        if (inputY < screenRange) {
            dY = -screenStep;
        }
        translate(dX, dY);

        if (collider().left() <= 0) {
            translateX(-dX);
        }
        if (collider().right() >= imgWidth) {
            translateX(-dX);
        }
        if (collider().top() <= 0) {
            translateY(-dY);
        }
        if (collider().bottom() >= imgHeight) {
            translateY(-dY);
        }
    }
}