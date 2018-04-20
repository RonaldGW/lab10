package edu.uab.cs203.playground;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.uab.cs203.Objectmon;
import edu.uab.cs203.Team;
import edu.uab.cs203.network.GymClient;
import edu.uab.cs203.network.GymServer;

public class GClientA implements GymClient, Serializable {

	private static final long serialVersionUID = 1L;
	private String message;
	private Team listA;

	public static void main(String[] args) {
		String host = "localhost";
		int port =10002;
		try {
			Registry registry = LocateRegistry.getRegistry(host,port);
			GymServer stub =(GymServer)registry.lookup("GServer");
			String s =stub.networkToString();
			System.out.println(s);

		} catch (Exception e) {
			System.out.println("EXCEPTION");
			e.printStackTrace();
		}
	}


		@Override
		public Team<Objectmon> getTeam() throws RemoteException {
			return this.listA;
		}

		@Override
		public Objectmon networkApplyDamage(Objectmon from, Objectmon to, int damage) throws RemoteException {

			return null;
		}

		@Override
		public void networkTick() throws RemoteException {
			listA.tick();

		}

		@Override
		public Objectmon nextObjectmon() throws RemoteException {
			for(int i=0; i<listA.size(); i++) {
				if(((Objectmon) listA.get(i)).isFainted()!= true) {
					return (Objectmon) listA.get(i);
				}
			}
	
			return null;
		}

		@Override
		public void printMessage(String message) throws RemoteException {
			this.message = message;
			System.out.println("This will, like, be a message or something..");

		}

		@Override
		public void setTeam(Team teamA) throws RemoteException {
			this.listA = teamA;

		}

	}
