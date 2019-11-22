package bounce;

import java.awt.Color; //imports libraries required 

import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

@SuppressWarnings("serial")
public class Bounce extends GraphicsProgram {	//the call Bounce extends the Graphics Program class from the acm library

	private static final int WIDTH = 600; 							//defines the width of the screen in pixels
	private static final int HEIGHT = 600; 							//distance from top of the screen to the ground plane
	private static final int OFFSET = 200; 							//distance from the ground plane to the bottom of the screen
	private static final double g = 9.8; 							//MKS gravitational constant 9.8 m/s^s
	private static final double Pi = 3.141592654; 					//To convert degrees to radians
	private static final double Xinit = 5.0; 						//Initial ball location
	private static final double TICK = 0.1; 						//Clock tick duration (sec)
	private static final double ETHR = 0.01; 						//If either Vx or Vy < ETHR STOP
	private static final double XMAX = 100.0; 						//Maximum value of X 
	private static final double YMAX = 100.0; 						//Maximum value of Y
	private static final double PD = 1; 							//Trace point diameter
	private static final double SCALE = (HEIGHT - OFFSET) / XMAX; 	//Pixels/meter
	private static final boolean TEST = true; 						//print if test true

	public void run() {
		this.resize(WIDTH, HEIGHT+OFFSET); //creating screen size according to dimensions set

		GRect rect = new GRect(0, HEIGHT, WIDTH, 3); //creating ground plane
		rect.setFilled(true);
		add(rect); 									 //adding ground plane to the screen

		// input from user

		double Vo = readDouble("Enter initial velocity of the ball in meters/second [0,100] = "); //prompts user for initial velocity 
		double theta = readDouble("Enter the launch angle in degrees [0,90] = "); 				  //prompts user for launch angle
		double loss = readDouble("Enter energy loss parameter [0,1] = "); 						  //prompts user for energy loss parameter expressed out of 1
		double bSize = readDouble("Enter the radius of the ball in meters [0.1,5.0] = "); 		  //prompts user for radius of the ball 

		GOval myBall = new GOval((Xinit - bSize) * SCALE, HEIGHT - (bSize * SCALE), bSize * 2 * SCALE,bSize * 2 * SCALE); //creates the ball 
		myBall.setFilled(true);
		myBall.setColor(Color.RED);
		add(myBall); //adds the ball to the screen 

		// initializing variables

		double X = Xinit; 									//sets X to hold value of Xinit (the inital x position of the ball)
		double Y = bSize; 									//sets Y to be the radius of the ball
		double Vox = Vo * Math.cos(theta * Pi / 180); 		//Calculation for the initial x-component of velocity 
		double Voy = Vo * Math.sin(theta * Pi / 180); 		//Calculation for the initial y-component of velocity 
		double k = 0.0016; 									//constant used in calculation of terminal velocity 
		double Vx; 											//initializes x-component of velocity 
		double Vy;											//initial y-component of velocity 
		double Xlast = X; 									//sets Xlast to be the value of X at the end of a given loop
		double Ylast = Y; 									//sets Ylast to be the value of Y at the end of a given loop
		double Vt = g / (4 * Pi * bSize * bSize * k); 		//calculation of terminal velocity 
		double KEx = 0.5*Vox*Vox*(1-loss); 					//calculation for the x-component of Kinetic Energy 
		double KEy = 0.5*Voy*Voy*(1-loss); 					//calculation for the y-component of Kinetic Energy 
		double Xo = Xinit; 									//sets Xo to be the initial position of X, Xinit, as defined
		double time = 0.0; 									//initializes time
		double ScrX; 										//initializes Screen Coordinate X
		double ScrY; 										//initializes Screen Coordinate Y

		// simulation loop

		while (true) {

			X = Xo + Vox * Vt / g * (1 - Math.exp(-g * time / Vt)); 						//calculation of the value of X
			Y = bSize + Vt / g * (Voy + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time; 	//calculation of the value of Y

			Vx = (X - Xlast) / TICK; //calculation of the x-component of instantaneous velocity 
			Vy = (Y - Ylast) / TICK; //calculation of the y-component of instantaneous velocity 

			if (Vy < 0 && Y <= bSize) { //if Vy remains positive and Y is greater than the radius of the ball

				KEx = 0.5*Vx*Vx*(1-loss); //calculate x-component of new kinetic energy after loss
				KEy = 0.5*Vy*Vy*(1-loss); //calculate y-component of new kinetic energy after loss
				
				Xo = X; 	//sets value of X to be in Xo
				Y = bSize; //sets value of radius to be in Y

				Vox = Math.sqrt(2*KEx); //calculates x-component of new velocity 
				Voy = Math.sqrt(2*KEy); //calculates y-component of new velocity 
				
				time=0; 				//sets time to be 0 
			
			}

			// display update
			ScrX = (int) ((X - bSize) * SCALE); 					//converts simulation coordinates of X to screen coordinates 
			ScrY = (int) (HEIGHT - (Y * SCALE + bSize * SCALE)); 	//converts simulation coordinates of Y to screen coordinates 
			myBall.setLocation(ScrX, ScrY); 						//moves the ball to the new screen coordinates
			
			GOval trackingball = new GOval(ScrX+bSize*SCALE, ScrY+bSize*SCALE, PD, PD); //creates tracerpoint that follows the movement of the ball
			trackingball.setFilled(true);
			trackingball.setColor(Color.BLACK);
			add(trackingball); //adds the tracerpoint to the screen

			time += 0.1; //increments time by a step of 0.1 at the end of each loop 

			Xlast = X; //sets value of X after each loop to be in variable Xlast
			Ylast = Y; //sets value of Y after each loop to be in variable Ylast
			
			//if(X>20 && Y==bSize)break; //this is to stop the ball at one bounce, required for part 1 of the assignment 
			
			if(KEx<=ETHR || KEy<=ETHR )break; //if the x and y components of the kinetic energy are less than or equal to ETHR value, terminate the loop as the bounces are neglegible thereafter
			
			pause(100); //pause so the movement of the ball can be seen
			
			if(TEST) {
				System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy: %.2f\n",time,Xo+X,Y,Vx,Vy); //prints the values of the program 
			}
		}
	}
}
