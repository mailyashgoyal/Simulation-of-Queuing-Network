
//Slight modification to the code given by Sir
public class rv {
 double Seed= 1111.0;

// returns a uniform (0,1) random variable 
 double uni_rv()
{
	 double k = 16807.0;
	 double m = 2.147483647e9;
	 double rv;

	 Seed=((k*Seed)%m);	
	 rv=Seed/m;
	 return(rv);
}

// returns an exponential r.v.
 double exp_rv(double lambda)
{
	 double exp;
	 exp = ((-1) / lambda) * Math.log(uni_rv());
	 return(exp);
	}
}

