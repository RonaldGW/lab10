package edu.uab.cs203.playground;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;
import edu.uab.cs203.network.NetworkGym;

public class GServer extends UnicastRemoteObject implements NetworkGym, GymServer {


	private GymClient GClientA;
	private GymClient GClientB;
	private ArrayList<GymClient> clients;
	private Team teama;
	private Team teamb;
	private boolean teamaisready;
	private boolean teambisready;


	protected GServer() throws RemoteException {
		this.clients = new ArrayList<>();
	}


	public static void main(String[] arg0) throws RemoteException {
		try {
			GServer server = new GServer();
			Runtime.getRuntime().exec("rmiregistry 10001");
			Registry registry = LocateRegistry.createRegistry(10001);
			registry.bind("GServer", server);
		}catch(Exception e)
		{System.out.println("Error: "+e.toString());
		e.printStackTrace();
		System.exit(0);

		}
		System.out.println("Ready to Begin");
		return;
	}

	@Override
	public void executeTurn() {
		try {
			this.GClientA.networkTick();
			this.GClientB.networkTick();
			System.out.println(GClientA.nextObjectmon());
			System.out.println(GClientB.nextObjectmon());
			Objectmon teama = GClientA.nextObjectmon();
			Objectmon teamb = GClientB.nextObjectmon();

			if(teama == null &&teamb== null) {
				return;
			}
			this.GClientB.networkApplyDamage(teama, teamb,teama.nextAttack().getDamage(teamb));
			GClientB.addStatusEffectFromAttack(teama.nextAttack(), teamb);
			if(teamb.isFainted()==true) {
				return;	}
			GClientB.addStatusEffectFromAttack(teamb.nextAttack(), teama);
			this.GClientA.networkApplyDamage(teamb, teama, teamb.nextAttack().getDamage(teama));
			return;
		}catch(Exception e) {
			System.out.println("Error: "+e.toString());
			e.printStackTrace();
		}
	}
	@Override
	public void fight(int arg0) {
		int count = 0;
		int end = 50;
		while(end != count) {
			if(this.getTeamA().canFight() && this.getTeamB().canFight()) {

				try {
					System.out.print(this.GClientA.getTeam().toString()+"\n");
					System.out.print(this.GClientB.getTeam().toString()+"\n");
				} catch (RemoteException e) {
		
					e.printStackTrace();
				}
				this.executeTurn();
				count ++;
				if(this.getTeamA().canFight() && this.getTeamB().canFight()) {
					this.broadcastMessage("Team A wint after "+count+" rounds.");
					count = end;
				}else if(this.getTeamB().canFight() && this.getTeamA().canFight()){
					this.broadcastMessage("Team B wint after "+count+" rounds.");
					count = end;
				}

			}

			if(this.getTeamA().canFight() && this.getTeamB().canFight()) {
				this.broadcastMessage("Draw");
			}
			return;


		}
	}

	@Override
	public GymClient getClientA() {
	
		return GClientA;
	}

	@Override
	public GymClient getClientB() {
		
		return GClientB;
	}

	@Override
	public Team getTeamA(){
		Team zar = null;
		try {
			zar = GClientA.getTeam();
		} catch (RemoteException e) {

			e.printStackTrace();
		}
		return zar;
	}

	@Override
	public Team getTeamB(){
		Team zar = null;
		try {
			zar = this.getClientB().getTeam();
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		return zar;
	}

	@Override
	public String networkToString() throws RemoteException {
		return this.toString();
	}

	@Override
	public void printMessage(String message) throws RemoteException {
		System.out.println(message);
		System.out.println(this.teamaisready + "A");
		System.out.println(this.teambisready + "B");
		if(teamaisready == true && teambisready != true) {
			GClientA.printMessage("\nPlease wait as Team B connects." + "\n" + "The fight will begin shortly");
		}else if(teamaisready != true && teambisready == true) {
			GClientB.printMessage("\nPlease wait as Team B connects." + "\n" + "The fight will begin shortly");
		}else if(teamaisready == true && teambisready == true) {
			GClientA.printMessage("\nPlease look at the server for commands");
			GClientB.printMessage("\nPlease look at the server for commands");
			Scanner f = new Scanner(System.in);
			System.out.println("\n\nAre you ready to begin.");
			String go = f.nextLine();
			if(go.equals("yes")) {
				GClientA.setTeam(teama);
				System.out.println(this.getTeamA().toString());
				System.out.println(this.getTeamB().toString());
			}else {
				System.exit(0);
			}
			System.out.print("/nHow many rounds would you like to go?");
			int rounds = f.nextInt();
			this.fight(rounds);
		}

	}

	@Override
	public void registerClientA(String host, int port, String registryname) throws RemoteException {
		
		System.out.println(host+port+registryname);
		GymClient clientA;
		try {
			this.GClientA  = (GymClient) LocateRegistry.getRegistry(host, port).lookup(registryname);
			this.clients.add(this.GClientA);
			this.GClientA.printMessage("Client A registered.");
			System.out.print(this.GClientA.getTeam().toString()+"\n");
			this.teamaisready=true;

		}catch(Exception e) {
			System.out.println("Error: "+e.toString());
			e.printStackTrace();
		}
		if(this.teambisready){
			fight(50);}
	}

	@Override
	public void registerClientB(String host, int port, String registryname) throws RemoteException {
		
		System.out.println(host+port+registryname);
		GymClient clientB;
		try {
			this.GClientB  = (GymClient) LocateRegistry.getRegistry(host, port).lookup(registryname);
			this.clients.add(this.GClientB);
			this.GClientB.printMessage("Client A registered.");
			System.out.print(this.GClientB.getTeam().toString()+"\n");
			this.teambisready=true;
		}catch(Exception e) {
			System.out.println("Error: "+e.toString());
			e.printStackTrace();
		}
		if(this.teambisready) {
			fight(50);
		}
	}

	@Override
	public void setTeamA(Team teama) throws RemoteException {
		
		this.getClientA().setTeam(teamb);
	}

	@Override
	public void setTeamAReady(boolean ready) throws RemoteException {

	}

	@Override
	public void setTeamB(Team teamb) throws RemoteException {
	
		this.getClientB().setTeam(teamb);

	}

	@Override
	public void setTeamBReady(boolean ready) throws RemoteException {

	}

}