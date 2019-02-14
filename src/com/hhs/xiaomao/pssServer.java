package com.hhs.xiaomao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * This is just a testing application!<br>
 * To test my TCP server programming skill...<br>
 * Just for fun!<br>
 * 
 * <h1>NOTE:Run this class for the Paper Scissor Stone server</h1><br>
 * 
 * @author XiaoMao205
 * @version 1.0.0
 */
public class pssServer {

	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		
		System.out.println("Input Server Port:");
		int ServerPort=s.nextInt();
		System.out.println();
		
		int Registered=0,no=0,ResultCnt=0;
		List<String> History1=new ArrayList<String>();
		List<String> History2=new ArrayList<String>();
		String Current1="Scissor",Current2="Scissor",Result="None";
		boolean OK1=false,OK2=false,ResultOK=false;
		
		InetAddress Player1=null,Player2=null;
		
		try {
			ServerSocket server=new ServerSocket(ServerPort);
			
			System.out.println();
			System.out.println("Server IP: "+InetAddress.getLocalHost());
			System.out.println("Server Port: "+server.getLocalPort());
			System.out.println("---------------Server Start!--------------");
			
			while(true) {
				Socket sc=server.accept();
				DataInputStream din=new DataInputStream(sc.getInputStream());
				DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
				
				String CurrentPlayer=din.readUTF();
				String message=din.readUTF();
				
				System.out.println("Connection #"+no);
				System.out.println("IP: "+sc.getInetAddress());
				System.out.println("Port: "+sc.getLocalPort());
				System.out.println("Player: "+CurrentPlayer);
				System.out.println("Message: "+message);
				System.out.println();
				no++;
				
				//Register for players
				if(message.equals("Player_Register")) {	
					if(Registered==0) {
						//First player registered
						
						Player1=sc.getInetAddress();
						System.out.println("Player1 Registered at IP: "+Player1);
						
						dout.writeUTF("Susseccfully_Registered_As_Player_1");
						
						Registered++;
					}
					else if(Registered==1) {
						//Second player registered
						//Game start
						
						Player2=sc.getInetAddress();
						System.out.println("Player2 Registered at IP: "+Player1);
						System.out.println();
						System.out.println("All Player Registered:");
						System.out.println("Player1: "+Player1);
						System.out.println("Player2: "+Player2);
						
						dout.writeUTF("Susseccfully_Registered_As_Player_2");
						
						Registered++;
					}
					else {
						//All Registered
						
						System.out.println("Player All Registered!No more registration!");
						
						dout.writeUTF("Registration_Failed");
					}
				}
				else if(message.equals("Paper")||message.equals("Scissor")||message.equals("Stone")){
					if(CurrentPlayer.equals("Player1")) {
						//Player 1 Attacks
						
						Current1=message;
						History1.add(message);
						OK1=true;
					}
					else {
						//Player 2 Attacks
						
						Current2=message;
						History2.add(message);
						OK2=true;
					}
					if(OK1&&OK2) {
						//Get Result!
						
						Result=judge(Current1,Current2);
						ResultOK=true;
						
						//Tell!
						dout.writeUTF(Result);
						
						if(CurrentPlayer.equals("Player1")) {
							dout.writeUTF(Current2);
						}
						else {
							dout.writeUTF(Current1);
						}
						ResultCnt++;
					}
					else {
						//Tell Player to Wait
						dout.writeUTF("Wait");
					}
				}
				else if(message.equals("Ask")) {
					//Player Asking for Result
					if(ResultOK) {
						//Result Get,Tell
						ResultCnt++;
						
						dout.writeUTF(Result);
						
						if(CurrentPlayer.equals("Player1")) {
							dout.writeUTF(Current2);
						}
						else {
							dout.writeUTF(Current1);
						}
					}
					else {
						//The Other Player didn't attack! Wait!
						dout.writeUTF("Wait");
					}
					if(ResultCnt==2) {
						if(Result.equals("Draw")) {
							//Go To Next Round
							Current1="Scissor";
							Current2="Scissor";
							Result="None";
							ResultCnt=0;
							OK1=false;
							OK2=false;
							ResultOK=false;
						}
						else {
							//Match End
							server.close();
							break;
						}
					}
				}
				din.close();dout.close();
				sc.close();
			
				System.out.println("-------------------------------------------");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("--------------Match End-------------");
		System.out.println("Server Closed");
		System.out.println();
		System.out.println("Match History:");
		for(int i=0;i<History1.size();i++) {
			System.out.println("Round #"+(i+1)+":");
			System.out.println(History1.get(i)+" - "+History2.get(i));
		}
		
		//Pause
		s.nextLine();
		s.close();
	}
	
	//Judge the winner!
	private static String judge(String current1, String current2) {
		if(current1.equals(current2)) {
			return "Draw";
		}
		if(current1.equals("Paper")) {
			if(current2.equals("Scissor")) {
				return "2";
			}
			else if(current2.equals("Stone")){
				return "1";
			}
		}
		else if(current1.equals("Scissor")) {
			if(current2.equals("Stone")) {
				return "2";
			}
			else if(current2.equals("Paper")){
				return "1";
			}
		}
		else if(current1.equals("Stone")) {
			if(current2.equals("Paper")) {
				return "2";
			}
			else if(current2.equals("Scissor")){
				return "1";
			}
		}
		return "Error";
	}

}
