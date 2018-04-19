package edu.uab.cs203.playground;

import java.awt.List;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
	private Team ClientA;
	private Team ClientB;
	
	
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
		// TODO Auto-generated method stub
	}

	@Override
	public void setTeamB(Team teamB) throws RemoteException {
		this.listB = teamB;
		
	}

	@Override
	public void setTeamBReady(boolean ready) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fight(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GymClient getClientA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GymClient getClientB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Team getTeamA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Team getTeamB() {
		// TODO Auto-generated method stub
		return null;
	}
}
