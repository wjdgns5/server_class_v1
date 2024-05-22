package ch04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

	// 메인 함수
	public static void main(String[] args) {

		System.out.println("=== 서버 실행 ===");

		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(5001);
			socket = serverSocket.accept();
			System.out.println("포트 번호 - 5001 할당 완료");

			// 클라이언트 데이터를 받을 입력 스트림 필요
			// 클라이언트에 데이터를 보낼 출력 스트림 필요

			// 서버측에서 키보드 입력을 받기위한 입력 스트림 필요

			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);

			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			// 멀티 쓰레딩 개념에 확장
			// 클라이언트로 부터 데이터를 읽는 쓰레드
			Thread readThread = new Thread(() -> { // Thread를 람다식으로 표현

				try {
					String clientMessage;
					while ((clientMessage = socketReader.readLine()) != null) { // try- catch
						System.out.println("서버측 콘솔 : " + clientMessage);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});

			/// 클라이언트에게 데이터를 보내는 스레드 생성
			Thread writeThread = new Thread(() -> {
				try {
					String serverMessage;
					while ((serverMessage = keyboardReader.readLine()) != null) {
						// 1. 먼저 키보드를 통해서 데이터를 읽고
						// 2. 출력 스트림을 활용해서 데이터를 보내야 한다.
						socketWriter.println(serverMessage);
					}

				} catch (Exception e2) {

				}
			});

			// 스레드 동작 -> start() 호출
			readThread.start();
			writeThread.start();

			// Thread join() 메서드는 하나의 스레드가 종료될때 까지 기다리도록 하는 기능을 제공한다.
			readThread.join();
			writeThread.join();

			System.out.println("--- 서버 프로그램 종료 --- ");

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of main

} // end of class
