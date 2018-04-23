package lab10;
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
		// TODO Auto-generated constructor stub
	}
private static Team<Objectmon> team;
private GymServer client;
public static void main (String[] arg0) throws RemoteException {
	
	try {
	int clientPort = 9998;
    int serverPort = 10001;
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
    Hashmon.loadObjectdex("C:\\Users\\glenn\\eclipse-workspace\\lab10\\src\\lab10\\objectdex_01.txt");
    for(int i=0;i<=5;i++) {
	team.add(new Hashmon(ObjectmonNameGenerator.nextName()));}
    client.registerClientB("localhost", clientPort, "GClientB");
    System.out.println("three");
    Scanner sk = new Scanner(System.in);
    System.out.println("Are you ready?");
	String begin = sk.nextLine();
	if(begin.equals("yes")) {
		
		client.setTeamAReady(true);
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
public Objectmon networkApplyDamage(Objectmon arg0, Objectmon arg1, int arg2) throws RemoteException {
	this.nextObjectmon().setHp((arg1.getHp()-arg2));
	return arg1;
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
public void printMessage(String arg0) throws RemoteException {
	System.out.print(arg0);
	
}

@Override
public void setTeam(Team arg0) throws RemoteException {
	team = arg0;
	return;

}
public void CreateTeam() throws RemoteException {
    for(int i=0;i<=this.getTeam().getMaxSize();i++) {
        Hashmon bob=new Hashmon(ObjectmonNameGenerator.nextName());
        team.add(bob);
    }}
}