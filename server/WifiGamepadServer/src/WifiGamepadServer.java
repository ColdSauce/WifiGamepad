import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;


public class WifiGamepadServer {
	
	int desiredPort = 4848;
	ServerSocket serverSocket;
	Socket activeSocket;
	
	DataInputStream socketReader;
	boolean isRunning = false;
	
	Robot robot;
	HashMap<Integer, Integer> keyMap;
	
	public WifiGamepadServer() throws UnknownHostException, IOException, AWTException {
		boolean socketFound = false;
		
		while(!socketFound){
			try {
				serverSocket = new ServerSocket(desiredPort);
				socketFound = true;
			} catch (BindException e){
				desiredPort++;
			}
		}
		
		System.out.println("IPAddress: " + NetworkUtils.getIp());
		System.out.println("Port: " + desiredPort);
				
		activeSocket = serverSocket.accept();
		socketReader = new DataInputStream(activeSocket.getInputStream());
		robot = new Robot();
	}
	
	public void start() throws IOException {
		System.out.println("Client connected!");
		isRunning = true;
		int button;
		boolean isDown;
		

		while(isRunning) {
			button = socketReader.readInt();
			isDown = socketReader.readBoolean();
			if(button == -1){
				isRunning = false;
			} else {
				pressKey(button, isDown);
			}
		}
	}
	
	public void pressKey(int i, boolean b){
		int keyEvent = i;
		System.out.println("Received key: " + i);
		if(b){

			robot.keyPress(keyEvent);
		} else {
			robot.keyRelease(keyEvent);
		}
	}
	
	public static void main(String[] args){
		try {
			new WifiGamepadServer().start();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
