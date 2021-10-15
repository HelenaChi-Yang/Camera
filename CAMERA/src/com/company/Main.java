package com.company;

import com.company.gametest9th.utils.GameKernel;

import javax.swing.*;
import java.awt.event.*;

public class Main {


    public static void main(String[] args) {
//        Client client=new Client();
//        client.run();
        JFrame frame = new JFrame();
        frame.setTitle("飛機射擊遊戲");
        frame.setSize(Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        Server server = new Server(6868);
//        server.start();
//        Client client=new Client("192.168.118.179");
//        client.start();
        int[][] commands = {
                {KeyEvent.VK_UP, Global.UP},
                {KeyEvent.VK_DOWN, Global.DOWN},
                {KeyEvent.VK_LEFT, Global.LEFT},
                {KeyEvent.VK_RIGHT, Global.RIGHT},
                {KeyEvent.VK_SPACE, Global.SPACE},
                {KeyEvent.VK_W, KeyEvent.VK_W},
                {KeyEvent.VK_A, KeyEvent.VK_A},
                {KeyEvent.VK_S, KeyEvent.VK_S},
                {KeyEvent.VK_D, KeyEvent.VK_D},
                {KeyEvent.VK_Z, KeyEvent.VK_Z},
                {KeyEvent.VK_X, KeyEvent.VK_X},
                {KeyEvent.VK_TAB, KeyEvent.VK_TAB},


        };

        GameCenter gi = new GameCenter();
        GameKernel kernel = new GameKernel.Builder(gi, Global.LIMIT_DELTA_TIME, Global.NANOSECOUND_PER_UPDATE)
                .initListener(commands)
                .enableMouseTrack(gi)
                .enableKeyboardTrack(gi)
//                .keyTypedMode()
                .keyCleanMode()
                .gen();


        frame.add(kernel);
        frame.setVisible(true);
        kernel.run(Global.IS_DEBUG);
    }
}
