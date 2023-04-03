package Java_ChatProgram;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient02 {

    public static void main(String[] args) {
        MultiClient02 multiClient = new MultiClient02();
        multiClient.start();
    }

    public void start() {
        Socket socket = null;
        BufferedReader in = null;
        try {
            socket = new Socket("localhost", 8000);
            System.out.println("[서버와 연결되었습니다]");

            String name = "user" + (int)(Math.random()*10);
            Thread sendThread = new SendThread02(socket, name);
            sendThread.start();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (in != null) {
                String inputMsg = in.readLine();
                if(("[" + name + "]님이 나가셨습니다").equals(inputMsg)) break;
                System.out.println("From:" + inputMsg);
            }
        } catch (IOException e) {
            System.out.println("[서버 접속끊김]");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[서버 연결종료]");
    }
}

class SendThread02 extends Thread {
    Socket socket = null;
    String name;

    Scanner scanner = new Scanner(System.in);

    public SendThread02(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            // 최초1회는 client의 name을 서버에 전송
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(name);
            out.flush();

            while (true) {
                String outputMsg = scanner.nextLine();
                out.println(outputMsg);
                out.flush();
                if("quit".equals(outputMsg)) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}