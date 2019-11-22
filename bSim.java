
import java.awt.Color;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class bSim extends GraphicsProgram{

	private static final int WIDTH = 1200; 				// n.b. screen coordinates
	private static final int HEIGHT = 600; 				//height of the screen
	private static final int OFFSET = 200;
	private static final double SCALE = HEIGHT/100; 	// pixels per meter
	private static final int NUMBALLS = 100; 			// # balls to simulate
	private static final double MINSIZE = 1.0; 			// Minumum ball radius (meters)
	private static final double MAXSIZE = 10.0; 		// Maximum ball radius (meters)
	private static final double EMIN = 0.1; 			// Minimum loss coefficient
	private static final double EMAX = 0.6; 			// Maximum loss coefficient
	private static final double VoMIN = 40.0; 			// Minimum velocity (meters/sec)
	private static final double VoMAX = 50.0; 			// Maximum velocity (meters/sec)
	private static final double ThetaMIN = 80.0; 		// Minimum launch angle (degrees)
	private static final double ThetaMAX = 100.0; 		// Maximum launch angle (degrees)
	
	RandomGenerator rgen = new RandomGenerator();		//random number generator
	
	public void run() {
		this.resize(WIDTH, HEIGHT+OFFSET); //creating screen size according to dimensions set
		
		rgen.setSeed((long)0.12345);

		GRect rect = new GRect(0, HEIGHT, WIDTH, 3); //creating ground plane
		rect.setFilled(true);
		add(rect); 									 //adding ground plane to the screen
		
		for(int i = 0; i < NUMBALLS; i++) {
			double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX); //generates random value of theta
			double iSize = rgen.nextDouble(MINSIZE,MAXSIZE);	//generates random value of theta
			Color iColor = rgen.nextColor();					//generates random value of theta
			double iLoss = rgen.nextDouble(EMIN,EMAX);			//generates random value of theta
			double iVel = rgen.nextDouble(VoMIN,VoMAX);			//generates random value of theta
			
			aBall iball = new aBall((WIDTH/2)/SCALE, iSize, iVel, iTheta ,iSize ,iColor ,iLoss);
			add(iball.myBall);
			iball.start();
			
//			Question 2
//			aBall iball = new aBall(5.0, 1.0, 40.0, 85.0,1.0 ,Color.RED ,0.4);
//			add(iball.myBall);
//			iball.start();
			
//			Question 3
//			aBall iball = new aBall(95.0, 1.0, 40.0, 95.0 ,1.0 ,Color.RED ,0.4);
//			add(iball.myBall);
//			iball.start();
		}
	}
}
