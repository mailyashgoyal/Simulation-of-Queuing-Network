
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class PCN {

	public static void main(String arg[]) throws IOException
	{
		//Event list
		EventList Elist ;                
		rv rr = new rv();
		// Arrival or departure event
		final int ArrL = 0,ArrG = 1,Dep = 2;

		// Number of customers in system/ departures from system
		int N = 0,Ndep = 0;        
		// Clock,E[N] , Max Capacity, Machine Available
		double clock = 0.0, EN = 0.0, K = 0.0 , m = 0.0;

		// Arrival/Service rate
		double lambda = 0.0,gamma = 0.0,mu=4;  

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 
		String sentence = null;

		System.out.println("Enter number of servers 2");
		sentence = null;
		sentence = inFromUser.readLine(); 
		m = Double.parseDouble(sentence);

		System.out.println("Enter k 4");
		sentence = null;
		sentence = inFromUser.readLine(); 
		K = Double.parseDouble(sentence);

		System.out.println("Enter mu 4");
		sentence = null;
		sentence = inFromUser.readLine(); 
		mu = Double.parseDouble(sentence);

		System.out.println("Enter gamma 5");
		sentence = null;
		sentence = inFromUser.readLine(); 
		gamma = Double.parseDouble(sentence);

		System.out.println(" ");
		System.out.println("Theoretical Values");

		double theoLambda = 0.0;     // arrival for machine 2
		double theoGaama = 5;        // arrival for machine 1
		double theoMU= 4;            // service rate for both machines
		double theoM = 2;            // number of machines
		double theoEN = 0.0;         // Expected number
		double theoET = 0.0;         // Expected time spent
		double theoEffLambda = 0.0;  // Effective arrival rate
		double theoPBlock = 0.0;     // probability of blocking
		double theoUtil = 0.0;       // utilization

		//State probability
		double p0 = 0.0;             
		double p1 = 0.0;             
		double p2 = 0.0;             
		double p3 = 0.0;       
		double p4 = 0.0;       

		Event E;

		//loop for rho
		for(double theoRho = 0.1; theoRho <= 1 ; theoRho = theoRho + 0.1)    
		{
			theoLambda = (theoRho * theoM * theoMU);  
			System.out.println("Theortical Rho  Value                   " + theoRho);
			System.out.println("Theortical Lambda  Value                " + theoLambda);

			//Steady state probability
			p1 = (theoLambda + theoGaama)/theoMU;  
			p2 = ((theoLambda + theoGaama) * (theoLambda + theoGaama))/(2* Math.pow(theoMU,2));
			p3 = ((theoLambda + theoGaama) * (theoLambda + theoGaama)* theoLambda)/(4* Math.pow(theoMU,3));
			p4 = ((theoLambda + theoGaama) * (theoLambda + theoGaama)* theoLambda * theoLambda)/(8* Math.pow(theoMU,4));

			//Calculating probablity using therom of total probability
			p0 = 1 / (1+p1+p2+p3+p4);      
			p1 = p1 * p0;
			p2 = p2 * p0;
			p3 = p3 * p0;
			p4 = p4 * p0; 

			//Expected number
			theoEN = (1 * p1) + (2 * p2) + (3 * p3) + (4 * p4); 
			System.out.println("Theortical EnValue                      " + theoEN);

			//Effective lambda
			theoEffLambda = ((theoLambda + theoGaama) * p0) + 
					((theoLambda + theoGaama) * p1) + (theoLambda  * p2) + (theoLambda  * p3);
			//Expected time spent
			theoET =  theoEN / theoEffLambda;
			System.out.println("Theortical AverageTime                  " + theoET);

			//Blocking Probability
			theoPBlock = (theoLambda * p4) /
					(((theoLambda + theoGaama) * p0) + ((theoLambda + theoGaama) * p1) +
							(theoLambda  * p2) + (theoLambda  * p3) +(theoLambda  * p4) );
			System.out.println("Theortical BlockingProbability          " +theoPBlock);

			//Utilization
			theoUtil = (0.5 * p1) + ( p2) + (p3) + (p4); 
			System.out.println("Theortical Utilization                  " + theoUtil);
			System.out.println(" ");
		}  

		//Simulation values
		System.out.println(" ");
		System.out.println("Simulation Values");
		//loop for rho
		for (double rho = 0.1; rho <= 1 ; rho = rho + 0.1) 
		{
			Elist = new EventList();
			lambda = 0.0 ;                  // Machine 2 Arrival rate
			clock = 0.0;                    // System clock
			EN = 0.0;                       // Avg number of customers in systems
			N = 0;                          // Number of customers in system
			Ndep = 0;                       // Number of departures from system
			double NArrived = 0;            // Number of arrivals into the System.
			double NBlocked = 0;            // Number of blocked customers
			double Util = 0.0;              // utilization
			double nowClock = 0.0;

			// calculating the value of lambda
			lambda = (rho * m * mu);        

			System.out.println("Lambda                                  " + lambda);
			System.out.println("Rho                                     " + rho);

			//Event qq = ;
			Elist = new EventList(new Event(rr.exp_rv(gamma),ArrG));// Generate first arrival event for machine 2
			Elist.insert(new Event(rr.exp_rv(lambda),ArrL)); //Generate first arrival event for machine 1

			while (Ndep < 100000)                // loop for 100000 components
			{
				double prev = clock;             // Store old clock value
				//	E = Elist.get();           // Get next Event from list
				E =Elist.getEvent();
				Elist.removeFront();
				clock = E.time;                  // Update system clock 
				nowClock = clock - prev;

				switch (E.type) 
				{
				case ArrL: 
				{
					// If arrival is from machine 2
					EN = EN + CalculateEn(N,nowClock);                      
					Util =  Util +calculateUtil(N,  m,  nowClock);
					// if number of customers in system more than capacity
/*
					NArrived++;   
					if(N >= K)                               
					{
						// The number of customers blocked will be incremented
						NBlocked++;                          
						// next arrival
						Elist.addtolist(new Event(rr.exp_rv(lambda),ArrL));             
						//break;
					}              
					// update number of arrivals
					else
					{
						// next arrival
						Elist.addtolist(new Event(clock+rr.exp_rv(lambda),ArrL));

						// update number of customers
						N++;                                                 
						if(N <= m ) 
						{
							// machine is idle, departure 
							Elist.addtolist(new Event(clock+rr.exp_rv(mu),Dep));    
						}
					}
					break;

					*/NArrived++; 	
						if(N<K)
						{
							N++;	
							Elist.insert(new Event(clock+rr.exp_rv(lambda),ArrL));
							if(N<=m)		
							{
								Elist.insert(new Event(clock+rr.exp_rv(mu),Dep));
							}
						}
						else	
						{
							NBlocked++;
							Elist.insert(new Event(clock+rr.exp_rv(lambda),ArrL));
						}

						break;
					 
				}
				case ArrG:
				{

					EN = EN + CalculateEn(N,nowClock);
					Util =  Util +calculateUtil(N,  m,  nowClock);
					//blocking will not happen here
					if(N >= 2)     
					{
						Elist.insert(new Event(clock+rr.exp_rv(gamma),ArrG));             //  next arrival  
						break;
					}

					N++;       // number of customers in system
					NArrived++;    // number of arrivals in system  
					Elist.insert(new Event(clock+rr.exp_rv(gamma),ArrG));                 // next arrival  
					if(N <= m ) //server idle, generate  departure event.
					{
						Elist.insert(new Event(clock+rr.exp_rv(mu),Dep));                 //  departure    
					}        
					break;
				}
				case Dep:  
				{
					// If departure event occurs
					EN = EN + CalculateEn(N,nowClock);                   
					Util =  Util +calculateUtil(N,  m,  nowClock);
					N--;                  // decrement system size   
					if (N >= m)           // If no of comp is either 2 or 3 or 4 then the rate with be MU
					{
						// next departure
						Elist.insert(new Event(clock+rr.exp_rv(mu),Dep));                
					} 
					Ndep++;               //  increment num of departures
					break;
				}
				}
				//Elist.remove(0);    // delete the event in the list         
			}
			System.out.println("Expected number of customers            " + EN/clock);
			System.out.println("Expected time customers spent in system " + EN/Ndep);
			System.out.println("Blocking Probability                    " + NBlocked/NArrived);
			System.out.println("Utilization                             " + Util/clock);
			System.out.println(" ");
		}
	}

	//Calculate utilization
	private static double calculateUtil(double N, Double m, double nowClock)
	{
		double Util = 0.0;
		if((N > 0 ) && (N < m))
		{Util = ((N/m)*(nowClock));}
		else if(N >= m)
		{Util = (nowClock);}
		return Util;
	}
	private static double CalculateEn(double N, double nowClock)
	{
		double En = 0.0;
		En = N*(nowClock);
		return En;
	}


}
