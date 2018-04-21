package lab10;

import java.awt.List;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
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

public class GServer extends UnicastRemoteObject implements GymServer, NetworkGym,Serializable{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected GServer() throws RemoteException {
		super();
	}
	private String message;
	private boolean Bisready;
	private boolean Aisready;
	private GymClient ClientA;
	private GymClient ClientB;
	
	
	public static void main(String[] args) {
		try {
			Runtime.getRuntime().exec("rmiregistry 10005");
			Registry registry = LocateRegistry.createRegistry(10005);
			GServer server =new GServer();
			registry.bind("GServer",server);
			System.out.print("ready to receive tasks");
			
		} catch (Exception  e) {
			System.out.println("System Error:" + e.toString());
			e.printStackTrace();

		}
		return;
	}

	@Override
	public String networkToString() throws RemoteException {
		// TODO Auto-generated method stub
		return "cool";
	}

	@Override
	public void printMessage(String message) throws RemoteException {
		this.message = message;
				System.out.println("This will, like, be a message or something.."+message);	
	}

	@Override
	public void registerClientA(String host, int port, String registryName) throws RemoteException {
		System.out.println("Registering client: " + host + ":" + port + ":" + registryName);
		
		
		
			
			System.out.print("woot");
				try {
					//GServer server =new GServer();
					//registry.bind("GServer",server);
					Registry registry = LocateRegistry.getRegistry("localhost",port);
					 GClientA client = new GClientA();
					 registry.bind("GClientA", client);
					this.ClientA =client;
					this.Aisready = true;
					System.out.print("ugh");
					client.printMessage("You have connected!");

					
				} catch (AlreadyBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(this.Bisready) {
					ClientA.printMessage("hellor");
					ClientB.printMessage("yo");
					
					System.out.print("hello mother fucker");
					fight(100);}
				

		
		
		}
		
	@Override
	public void registerClientB(String host, int port, String registryName) throws RemoteException {
		System.out.println("Registering client: " + host + ":" + port + ":" + registryName);
		
		
		
		
		System.out.print("woot");
			try {
				//GServer server =new GServer();
				//registry.bind("GServer",server);
				Registry registry = LocateRegistry.getRegistry("localhost",port);
				 GClientB client = new GClientB();
				 registry.lookup("GClientB");
				this.ClientB =client;
				this.Bisready = true;
				System.out.print("ugh");
				client.printMessage("You have connected!");
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

	
	
	}
	

	@Override
	public void setTeamAReady(boolean ready) throws RemoteException {
	if(this.ClientA==null) {
		this.Aisready=false;
	}
	this.Aisready=true;
	}

	@Override
	public void setTeamBReady(boolean ready) throws RemoteException {
		if(this.ClientB==null) {
			this.Bisready=false;
		}
		this.Bisready=true;
		}


	@Override
	public void executeTurn() {
		try {
			this.ClientA.networkTick();
			this.ClientB.networkTick();
			Objectmon teama = ClientA.nextObjectmon();
			Objectmon teamb = ClientB.nextObjectmon();
			
			if(teama == null &&teamb== null) {
				return;
			}
			this.ClientB.networkApplyDamage(teama, teamb,teama.nextAttack().getDamage(teamb));
			if(teamb.isFainted()==true) {
				return;	}
			this.ClientA.networkApplyDamage(teamb, teama, teamb.nextAttack().getDamage(teama));
			return;
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void fight(int rounds) {
		int count = 0;
		while (rounds != count) {
			try {
				if(ClientA.getTeam().canFight()&&ClientB.getTeam().canFight()) {
					this.executeTurn();
					count ++;
					try {
						if(this.ClientA.getTeam().canFight()&& this.ClientB.getTeam().canFight()) {
							this.broadcastMessage("Team A wins after " + count + " rounds!");
							count = rounds;
						 }else if(this.ClientB.getTeam().canFight()&& this.ClientA.getTeam().canFight()){
						        this.broadcastMessage("Team B wint after "+count+" rounds.");
						        count = rounds;
						    }
						  if(this.ClientA.getTeam().canFight() && this.ClientB.getTeam().canFight()) {
				                this.broadcastMessage("Draw");}
					}catch (RemoteException e) {
						System.out.print("fight no go why???");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		try {
			return this.ClientA.getTeam();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return null;
	}

	@Override
	public Team getTeamB() {
		try {
			return this.ClientB.getTeam();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return null;
	}

	@Override
	public void setTeamA(Team arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTeamB(Team arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}