package com.hhs.xiaomao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class pssPlayer {

	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		
		int Port=25565;
		String IPaddress;
		
		System.out.println("Input Server IP:");
		IPaddress=s.next();
		
		System.out.println("Input Server Port:");
		Port=s.nextInt();
		
		System.out.println("Try to connect server,at:");
		System.out.println("    IP: "+IPaddress);
		System.out.println("    Port: "+Port);
		System.out.println();
		
		String PlayerNo="Player1";
		boolean GetResult=true;
		
		try {
			Socket sc=new Socket(IPaddress,Port);
			DataInputStream din=new DataInputStream(sc.getInputStream());
			DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
			dout.writeUTF("IDK");
			dout.writeUTF("Player_Register");
			System.out.println("Trying to registered player");

			String message=din.readUTF();
			if(message.equals("Registration_Failed")) {
				//Registration Failed
				System.out.println("You are too late!");
				System.out.println("Players are already registered!");
				System.exit(0);
			}
			else if(message.equals("Susseccfully_Registered_As_Player_1")) {
				//Registered as player 1
				System.out.println("You registered as player 1!");
				PlayerNo="Player1";
			}
			else if(message.equals("Susseccfully_Registered_As_Player_2")) {
				//Registered as player 2
				System.out.println("You registered as player 2!");
				PlayerNo="Player2";
			}
			
			din.close();dout.close();
			sc.close();
			
			String attack;
			
			System.out.println("---------------Match Start-------------");
			while(true) {
				System.out.println("Input your attack(Paper/Scissor/Stone):");
				attack=s.next();
				if(attack.equals("Paper")||attack.equals("Scissor")||attack.equals("Stone")) {
					break;
				}
				System.out.println("[WRONG ATTACK]Please input again.");
			}
			
			while(!(message.equals("1")||message.equals("2"))) {
				sc=new Socket(IPaddress,Port);
				din=new DataInputStream(sc.getInputStream());
				dout=new DataOutputStream(sc.getOutputStream());
				
				dout.writeUTF(PlayerNo);
				if(GetResult) {
					dout.writeUTF(attack);
					GetResult=false;
				}
				else {
					dout.writeUTF("Ask");
				}
				
				message=din.readUTF();
				
				if(message.equals("1")||message.equals("2")||message.equals("Draw")) {
					System.out.println("Enemy used "+din.readUTF());
					System.out.println();
					GetResult=true;
					
					if(message.equals("Draw")) {
						while(true) {
							System.out.println("Input your attack(Paper/Scissor/Stone):");
							attack=s.next();
							if(attack.equals("Paper")||attack.equals("Scissor")||attack.equals("Stone")) {
								break;
							}
							System.out.println("[WRONG ATTACK]Please input again.");
						}
					}
					
				}
				if(message.equals("Wait")) {
					Thread.sleep(1000);
				}
			}
			
			sc.close() ;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//Pause
		s.nextLine();
		s.close();
	}

}
