package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
/**
 伺服器端繼承Thread
 * 可以實現簡單的互動  
 * */
public class Client extends Thread {
    private Socket socket; //連結資料用
    private String host = "192.168.1.22";

    public Client(){
        try {
            socket = new Socket(host, 5270);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("OutputStream Error");
        }
        BufferedReader brIn = null;
        try {
            brIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("InputStream Error");
        }
        String line=null;
        try {
            while((line=br.readLine())!=null)
            {
                System.out.println(line);
                if(line.equals("over"))
                    break;
                bw.write(line);
                bw.newLine();
                bw.flush();

                String str=brIn.readLine();
                System.out.println(str);

            }
        } catch (IOException e) {
            System.out.println("ReadLine Error");
        }
        try {
            br.close();
        } catch (IOException e) {
            System.out.println("BufferedReader Close Error");
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Socket Close Error");
        }


    }

}