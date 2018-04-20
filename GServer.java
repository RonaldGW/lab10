package lab10;

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
	private String message;
	private boolean Bisready;
	private boolean Aisready;
	private GymClient ClientA;
	private GymClient ClientB;
	
	
	public static void main(String[] args) {
		try {
			Runtime.getRuntime().exec("rmiregistry 10002");
			Registry registry = LocateRegistry.createRegistry(10002);
			GServer server =new GServer();
			registry.bind("GServer",server);
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
		GClientA client;
		try {
		client = (GClientA)LocateRegistry.getRegistry(host, port).lookup(registryName);
		this.ClientA =client;
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
			GClientA client;
		try {
		client = (GClientA)LocateRegistry.getRegistry(host, port).lookup(registryName);
		this.ClientB=client;
		this.Bisready = true;
		client.printMessage("You have connected!");
		} 
		catch (NotBoundException e) {
			e.printStackTrace();
		}
		}
		

	@Override
	public void setTeamAReady(boolean ready) throws RemoteException {
		
	}

	@Override
	public void setTeamBReady(boolean ready) throws RemoteException {
		
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
			if(this.getTeamA().canFight()&& this.getTeamB().canFight()) {
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
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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