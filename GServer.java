package lab10;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
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
			if(teamb.isFainted()==true) {
				return;	}
			this.GClientA.networkApplyDamage(teamb, teama, teamb.nextAttack().getDamage(teama));
			return;
	}catch(Exception e) {
		System.out.println("Error: "+e.toString());
		e.printStackTrace();
	}
	}
	@Override
	public void fight(int arg0) {
		// TODO Auto-generated method stub
		  int count = 0;
		  int end = 50;
	        while(end != count) {
	        	if(this.getTeamA().canFight() && this.getTeamB().canFight()) {
	        	   
	        	       try {
        	    	   System.out.print(this.GClientA.getTeam().toString()+"\n");
						System.out.print(this.GClientB.getTeam().toString()+"\n");
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
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
		// TODO Auto-generated method stub
		return GClientA;
	}

	@Override
	public GymClient getClientB() {
		// TODO Auto-generated method stub
		return GClientB;
	}

	@Override
	public Team getTeamA(){
		// TODO Auto-generated method stub
		Team zar = null;
		try {
			zar = GClientA.getTeam();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zar;
	}

	@Override
	public Team getTeamB(){
		// TODO Auto-generated method stub
		Team zar = null;
		try {
			zar = this.getClientB().getTeam();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zar;
	}

	@Override
	public String networkToString() throws RemoteException {
		// TODO Auto-generated method stub
		return this.toString();
	}

	@Override
	public void printMessage(String arg0) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		
	}

	@Override
	public void registerClientA(String arg0, int arg1, String arg2) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(arg0+arg1+arg2);
		GymClient clientA;
		try {
        this.GClientA  = (GymClient) LocateRegistry.getRegistry(arg0, arg1).lookup(arg2);
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
	public void registerClientB(String arg0, int arg1, String arg2) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(arg0+arg1+arg2);
		GymClient clientB;
		try {
        this.GClientB  = (GymClient) LocateRegistry.getRegistry(arg0, arg1).lookup(arg2);
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
	public void setTeamA(Team arg0) throws RemoteException {
		// TODO Auto-generated method stub
		this.getClientA().setTeam(arg0);
	}

	@Override
	public void setTeamAReady(boolean arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTeamB(Team arg0) throws RemoteException {
		// TODO Auto-generated method stub
		this.getClientB().setTeam(arg0);
		
	}

	@Override
	public void setTeamBReady(boolean arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
