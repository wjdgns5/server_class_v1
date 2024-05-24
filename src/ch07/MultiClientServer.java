package ch07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MultiClientServer {
	
	private static final int Port = 5000;
	
	private static Vector<PrintWriter> clientWriters = new Vector<>();
	
	
	public static void main(String[] args) {
		System.out.println("Server Started. . . . ");
		
		try(
				ServerSocket serverSocket = new ServerSocket(Port);
				) {
			
			while(true) {
				
				Socket socket = serverSocket.accept();
				
				// 새로운 클라이언트가 연결되면 새로운 스레드가 생성된다.
				new ClientHandler(socket).start(); 
				
			}
			
		} catch (Exception e) {
			
		}

	} // end of main
	
	// 정적 내부 클래스 설계
	private static class ClientHandler extends Thread {
		
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
		} // 생성자
		
		// Thread 스레드 start() 호출시 동작 되는 메서드 - 약속
		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				
				// 코드 추가
				// 클라이언트로부터 이름 받기 (약속되어 있음 ! )
				String nameMessage = in.readLine(); // startsWith 시작하는 문자열을 의미
				if(nameMessage != null  && nameMessage.startsWith("NAME:")) {
					String clientName = nameMessage.substring(5);
					// substring : 문자열을 잘라낸다. N은 0, A 는 1, M 는 2, E 는 3
					broadcastMessage("해당 서버에 : " + nameMessage + " 님 입장");
				} else {
					// 약속과 다르게 접근했다면 종료 처리 
					socket.close();
					return;
				}
				
				// 여기서 중요 !  - 서버가 관리하는 자료구조에 자원 저장 (클라이언트와 연결된 소켓 -> outputStream)
				clientWriters.add(out);
				
				String message;
				while( (message = in.readLine()) != null ) {
					System.out.println("Received : " + message);
					
					// 약속 -> 클라이언트와, 서버와 약속
					// : 기준으로 처리, \기준, <--
					// message:안녕\n
					String[] parts = message.split(":", 2);
					System.out.println("parts 인덱스 갯수 : " + parts.length);
					// 명령 부분을 분리
					String command = parts[0];
					// 데이터 부분을 분리
					String data = parts.length > 1 ? parts[1] : "";
					
					if(command.equals("MSG")){
						System.out.println("연결된 전체 사용자에게 MSG 방송");
						broadcastMessage(message);
					} else if (command.equals("BYE")) {
						System.out.println("Client disconnected  . . . . ");
						break; // while문 구문 종료 ...
					}
					
					broadcastMessage(message);
				} // end of while
				// . . . finally 구문으로 빠진다.
			
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				try {
					socket.close();
					// 도전 과제!!
					// 서버측에서 관리하고 있는 PrintWriter를 제거 해야 한다.
					// 인덱스 번호가 필요하다.
					// clientWriters.add() 할 때 지정된 나의 인덱스 번호가 필요
					// clientWriters.remove();
					System.out.println(". . . . . 클라이언트 연결 해제 . . . . . ");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // end of try - catch - fianlly()
		} // end of run()
	} // end of ClientHandler()
	
	// 모든 클라이언트에게 메세지 보내기 - 브로드캐스트
	private static void broadcastMessage(String message){
		for(PrintWriter writer : clientWriters) {
			writer.print(message);
		}
	}
	
	
	
	
	
	
	
	

} // end of class
