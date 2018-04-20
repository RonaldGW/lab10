package lab10;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.ObjectmonNameGenerator;
import edu.uab.cs203.Team;
import edu.uab.cs203.lab05.BasicTeam;
import edu.uab.cs203.lab09.Hashmon;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

public class GClientA implements GymClient, Serializable{
private Team<Objectmon> teamA;
	public static void main(String[] args) throws RemoteException {
	GClientA client = new GClientA();
	Team<Objectmon> team = new BasicTeam<>();
	client.setTeam(team);
	String host = "localhost";
	int port =10002;
	
	try {
		Registry registry = LocateRegistry.getRegistry(host,port);
		GymServer stub =(GymServer)registry.lookup("GServer");
		String s =stub.networkToString();
		System.out.println(s);
		
	} catch (Exception e) {
		System.out.println("EXCEPTION");
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	@Override
	public Team<Objectmon> getTeam() throws RemoteException {
		return this.teamA;
	}

	@Override
	public Objectmon networkApplyDamage(Objectmon arg0, Objectmon arg1, int arg2) throws RemoteException {
		arg1.setHp(arg1.getHp()-arg2);
		return arg1;
	}

	@Override
	public void networkTick() throws RemoteException {
		this.teamA.tick();
		
	}

	@Override
	public Objectmon nextObjectmon() throws RemoteException {
		return null;
	}

	@Override
	public void printMessage(String arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTeam(Team arg0) throws RemoteException {
		for(int i=0;i<=6;i++) {
			Hashmon teammon = new Hashmon(ObjectmonNameGenerator.nextName());
			arg0.add(teammon);}

	}}
