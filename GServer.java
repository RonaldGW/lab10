package edu.uab.cs203.playground;

import java.awt.List;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;
import edu.uab.cs203.network.NetworkGym;

public class GServer extends UnicastRemoteObject implements GymServer, NetworkGym {
	
	private static final long serialVersionUID = 1L;

	protected GServer() throws RemoteException {
		super();
	}

	private Team listA;
	private Team listB;
	private String message;
	private boolean Bisready;
	private boolean Aisready;
	private GymClient ClientA;
	private GymClient ClientB;
	
	
	public static void main(String[] args) {
		try {
			Runtime.getRuntime().exec("rmiregistry 9001");
			Registry registry = LocateRegistry.createRegistry(9001);
			GServer server =new GServer();
			registry.bind("computeServer",server);
		} catch (Exception  e) {
			System.out.println("System Error:" + e.toString());
			e.printStackTrace();
			
		}
		System.out.print("ready to receive tasks");
		return;
	}

	@Override
	public String networkToString() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printMessage(String message) throws RemoteException {
		this.message = message;
				System.out.println("This will, like, be a message or something..");	
	}

	@Override
	public void registerClientA(String host, int port, String registryName) throws RemoteException {
		System.out.println("Registering client: " + host + ":" + port + ":" + registryName);
		GClient client;
		try {
		client = (GClient)LocateRegistry.getRegistry(host, port).lookup(registryName);
		this.listA.add(client);
		this.Aisready = true;
		client.printMessage("You have connected!");
		} 
		catch (NotBoundException e) {
			e.printStackTrace();
		}
		}
		
	@Override
	public void registerClientB(String host, int port, String registryName) throws RemoteException {
		System.out.println("Registering client: " + host + ":" + port + ":" + registryName);
			GClient client;
		try {
		client = (GClient)LocateRegistry.getRegistry(host, port).lookup(registryName);
		this.listB.add(client);
		this.Bisready = true;
		client.printMessage("You have connected!");
		} 
		catch (NotBoundException e) {
			e.printStackTrace();
		}
		}
		
		
	@Override
	public void setTeamA(Team teamA) throws RemoteException {
		this.listA = teamA;
	}

	@Override
	public void setTeamAReady(boolean ready) throws RemoteException {
		
	}

	@Override
	public void setTeamB(Team teamB) throws RemoteException {
		this.listB = teamB;
		
	}

	@Override
	public void setTeamBReady(boolean ready) throws RemoteException {
		
	}

	@Override
	public void executeTurn() {
		listA.tick();
		listB.tick();
		Objectmon teama = listA.nextObjectmon();
		Objectmon teamb = listB.nextObjectmon();
		if(teama != null) {
			teama.nextAttack();
		}
		if(teamb != null) {
			teamb.nextAttack();
		}
	}

	@Override
	public void fight(int rounds) {
		int count = 0;
		while (rounds != count) {
			if(this.getTeamA().canFight()&& this.getTeamB().canFight()) {
				this.executeTurn();
				count ++;
				if(listA.canFight()&& listB.canFight()) {
					this.broadcastMessage("Team A wins after " + count + " rounds!");
					count = rounds;
				 }else if(listB.canFight() && listA.canFight()){
	                    this.broadcastMessage("Team B wint after "+count+" rounds.");
	                    count = rounds;
	                }
	        }
	            if(this.getTeamA().canFight() && this.getTeamB().canFight()) {
	                this.broadcastMessage("Draw");
				}
			}
		}
		

	@Override
	public GymClient getClientA() {
		return this.ClientA;
	}

	@Override
	public GymClient getClientB() {
		return this.ClientB;
	}

	@Override
	public Team getTeamA() {
		return this.listA;
	}

	@Override
	public Team getTeamB() {
		return this.listB;
	}
}
