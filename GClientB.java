package edu.uab.cs203.playground;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import edu.uab.cs203.Objectmon;
import edu.uab.cs203.ObjectmonNameGenerator;
import edu.uab.cs203.Team;
import edu.uab.cs203.lab05.BasicTeam;
import edu.uab.cs203.lab09.Hashmon;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;
import edu.uab.cs203.network.NetworkGym;

public class GClientB extends UnicastRemoteObject implements Serializable, GymClient {


	protected GClientB() throws RemoteException {
		super();
		
	}
	private static Team<Objectmon> team;
	private GymServer client;
	public static void main (String[] arg0) throws RemoteException {

		try {
			int clientPort = 9998;
			int serverPort = 10001;
			String clienta = null;
			String serverHost = "localhost";
			GymClient GClientB = new GClientB();

			Runtime.getRuntime().exec("rmiregistry " + clientPort);
			Registry registry = LocateRegistry.createRegistry(clientPort);
			registry.bind("GClientB", GClientB);
			System.out.println("one");
			Registry serverRegistry = LocateRegistry.getRegistry(serverHost, serverPort);
			GymServer client = (GymServer) serverRegistry.lookup("GServer");
			System.out.println("two");
			team = new BasicTeam<>("Team B", 6);
			Hashmon.loadObjectdex("C:\\Users\\randl\\eclipse-workspace\\Lab10\\objectdex_01.txt");
			for(int i=0;i<=5;i++) {
				Hashmon.getEntry("C:\\Users\\randl\\eclipse-workspace\\Lab10\\objectdex_01.txt");
				team.add(new Hashmon(ObjectmonNameGenerator.nextName()));}
			client.registerClientB("localhost", clientPort, "GClientB");
			System.out.println("three");
			Scanner sk = new Scanner(System.in);
			System.out.println("Are you ready?");
			String begin = sk.nextLine();
			if(begin.equals("yes")) {

				client.setTeamBReady(true);
				client.printMessage(clienta);
			}



		}catch(Exception e) {
			System.out.println("Trace back:");
			e.printStackTrace();
		}
	}
	@Override
	public Team<Objectmon> getTeam() throws RemoteException {
		return this.team;
	}

	@Override
	public Objectmon networkApplyDamage(Objectmon from, Objectmon to, int damage) throws RemoteException {
		this.nextObjectmon().setHp((to.getHp()-damage));
		return to;
	}

	@Override
	public void networkTick() throws RemoteException {
		this.team.tick();

	}

	@Override
	public Objectmon nextObjectmon() throws RemoteException {
		for(int i=0; i<team.size(); i++) {
			if(((Objectmon) team.get(i)).isFainted()!= true) {
				return (Objectmon) team.get(i);
			}
		}
		return null;

	}

	@Override
	public void printMessage(String message) throws RemoteException {
		System.out.print(message);

	}

	@Override
	public void setTeam(Team team) throws RemoteException {
		team = team;
		return;

	}
	public void CreateTeam() throws RemoteException {
		for(int i=0;i<=this.getTeam().getMaxSize();i++) {
			Hashmon bob=new Hashmon(ObjectmonNameGenerator.nextName());
			team.add(bob);
		}}
}