package ch01;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Sever {

	public static void main(String[] args) {
		// 서버측 소켓통신을 만들기 위해서 준비물
		// 1. SeverSocket (클라이언트 측 소켓과 연결만 시켜 준다.)
		// 2. 클라이언트와 연결 되는 소켓을 들고 있어야 한다.

		// 로컬 컴퓨터에는 정해진, 사용할 수 있는 포트 번호 갯수가 할당 되어 있다.
		// 1 ~ 1024 포트 번호는 잘 알려진 포트 번호 시스템이 선점하고 있는 번호들 이다.

		// try - catch - resource
		try (ServerSocket severSocket = new ServerSocket(5000)) {
			System.out.println("서버 포트 번호 : 5000 으로 생성");

			// 내부 메서드 안에서 while 문을 돌면서 클라이언트 측에
			// 연결을 기다리고 있습니다.
			Socket socket = severSocket.accept();

			// 여기 아래는 클라이언트 측과 양 끝단에 소켓이 서로
			// 연결 되어야 실행 흐름이 내려온다.
			System.out.println("Clinet connected. . . . ");
			// 대상 - 소켓 --> 입력 스트림을 가져 온다. ,
			InputStream input = socket.getInputStream();
			// input 은 바이트 기반으로 들어온다.
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String message = reader.readLine(); // 한 줄 단위로 데이터를 읽어라 (문자 기반)
			System.out.println("Received : " + message);

			// 기본 소켓은 클라이언트가 연결 되어야 생성 된다.
			socket.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
