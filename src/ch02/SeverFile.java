package ch02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverFile {

	public static void main(String[] args) {

		// 준비물
		// 1. 서버 소켓이 필요하다.
		// 2. 포트 번호가 필요하다. 개인한테(0번 부터 ~ 65535번 까지 존재한다.)
		// 2. 1 잘 알려진 포트 번호 : 주로 시스템 레벨 0 ~ 1023까지 사용
		// 2. 2 등록 가능하는 포트 : 1024 ~ 49151 까지 등록 가능하다.
		// 2. 3 동적 / 사설 포트번호 - 그 외 임시 사용

		// 지역 변수
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(5001);
			System.out.println("서버를 시작합니다. 포트 번호 : 5001 ");

			Socket socket = serverSocket.accept();
			System.out.println(">>> 클라이언트가 연결 하였습니다. <<<");

			// 데이터를 전달 받기 위해서는 뭐가 필요하다? --> 스트림이 하다.
			InputStream input = socket.getInputStream();

			// 문자기반 스트림
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			// 실제 데이터를 읽는 행위가 필요 하다.
			String message = reader.readLine();
			System.out.println("클리이언트 측 메세지 전달 받음 : " + message);
			socket.close(); // try - catch - resource 안쓰면 직접 자원을 닫아 줘야 한다.

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (serverSocket != null) {
				try {
					serverSocket.close(); // 닫을 때도 예외가 발생 할 가능성이 있기에 try- catch 사용
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	} // end of mainb

} // end of class
