//imports libraries required
import java.awt.Color;  
import acm.graphics.GOval;

public class aBall extends Thread{
	
	GOval myBall;
	
	private static final int HEIGHT = 600;
	private static final double SCALE = HEIGHT/100; 				// pixels per meter
	private static final double g = 9.8; 							//MKS gravitational constant 9.8 m/s^s
	private static final double Pi = 3.141592654; 					//To convert degrees to radians
	private static final double TICK = 0.1; 						//Clock tick duration (sec)
	private static final double ETHR = 0.01; 						//If either Vx or Vy < ETHR STOP					

	//initialization of parameters
	
	double Xi;
	double Yi; 
	double Vo;
	double theta; 
	double bSize; 
	Color bColor;
	double bLoss;
	
	/**
	* The constructor specifies the parameters for simulation. They are
	*
	* @param Xi double The initial X position of the center of the ball
	* @param Yi double The initial Y position of the center of the ball
	* @param Vo double The initial velocity of the ball at launch
	* @param theta double Launch angle (with the horizontal plane)
	* @param bSize double The radius of the ball in simulation units
	* @param bColor Color The initial color of the ball
	* @param bLoss double Fraction [0,1] of the energy lost on each bounce
	*/
	
	public aBall(double Xi, double Yi, double Vo, double theta,
			double bSize, Color bColor, double bLoss) {
			//gets parameters
			this.Xi = Xi; 
			this.Yi = Yi;
			this.Vo = Vo;
			this.theta = theta;
			this.bSize = bSize;
			this.bColor = bColor;
			this.bLoss = bLoss;
			
			//instance of ball
			myBall = new GOval((Xi - bSize)*SCALE, 600-(Yi*2*SCALE), bSize*2*SCALE, bSize*2*SCALE);
			myBall.setFilled(true);
			myBall.setFillColor(bColor);
			
	}
			/**
			* The run method implements the simulation loop from Assignment 1.
			* Once the start method is called on the aBall instance, the
			* code in the run method is executed concurrently with the main
			* program.
			* @param void
			* @return void
			*/

			public void run() {
				
				// initializing variables
				 									
				double Y = bSize; 									//sets Y to be the radius of the ball
				double Vox = Vo * Math.cos(theta * Pi / 180); 		//Calculation for the initial x-component of velocity 
				double Voy = Vo * Math.sin(theta * Pi / 180); 		//Calculation for the initial y-component of velocity 
				double k = 0.0001; 									//constant used in calculation of terminal velocity 
				double Vx; 											//initializes x-component of velocity 
				double Vy;											//initial y-component of velocity 
				double Vt = g / (4 * Pi * bSize * bSize * k); 		//calculation of terminal velocity 
				double KEx = 0.5*Vox*Vox*(1-bLoss); 				//calculation for the x-component of Kinetic Energy 
				double KEy = 0.5*Voy*Voy*(1-bLoss); 				//calculation for the y-component of Kinetic Energy 
				double Xo = Xi; 									//sets Xo to be the initial position of X, Xinit, as defined
				double time = 0.0; 									//initializes time
				double ScrX; 										//initializes Screen Coordinate X
				double ScrY; 										//initializes Screen Coordinate Y
				double X = Xo;										//sets X to be equal to Xo 
				double Xlast = X; 									//sets Xlast to be the value of X at the end of a given loop
				double Ylast = Y; 									//sets Ylast to be the value of Y at the end of a given loop
				
				// simulation loop
				
				double KElast = Vox*0.5*Vox+Voy*0.5*Voy;

				while (true) {

					X = Xo + Vox * Vt / g * (1 - Math.exp(-g * time / Vt)); 						//calculation of the value of X
					Y = bSize + Vt / g * (Voy + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time; 	//calculation of the value of Y

					Vx = (X - Xlast) / TICK; //calculation of the x-component of instantaneous velocity 
					Vy = (Y - Ylast) / TICK; //calculation of the y-component of instantaneous velocity 

					if (Vy < 0 && Y <= bSize) { //if Vy remains positive and Y is greater than the radius of the ball

						KEx = 0.5*Vx*Vx*(1-bLoss); //calculate x-component of new kinetic energy after loss
						KEy = 0.5*Vy*Vy*(1-bLoss); //calculate y-component of new kinetic energy after loss
						
						KElast = KEx+KEy; 			//total KE at the end 
						
						if(KEx+KEy>ETHR && KEy+KEx<KElast )break; //if total KE is less than ETHR AND total KE is less than that of the previous bounce, TERMINATE
												
						Xo = X; 	//sets value of X to be in Xo
						Y = bSize; //sets value of radius to be in Y
						time = 0;	//sets time to be 0 at the end

						Vox = Math.sqrt(2*KEx); //calculates x-component of new velocity 
						Voy = Math.sqrt(2*KEy); //calculates y-component of new velocity 
						
						Vx=Vox;					//sets Vx to be the value of Vox (initial value)
						Vy=Voy;					//sets Vy to be the value of Voy (initial value)
						X = Xo + Vox*Vt/g*(1-Math.exp(-g*time/Vt)); 
						
						if(theta>90) {			//if condition for when theta is greater than 90 degrees
							Vox = Vox*-1;		//changes sign of Vox
						}
					}

					// display update
					
					ScrX = (int) (X  * SCALE); 								//converts simulation coordinates of X to screen coordinates 
					ScrY = (int) (HEIGHT - (Y * SCALE + bSize * SCALE)); 	//converts simulation coordinates of Y to screen coordinates 
					myBall.setLocation(ScrX, ScrY); 						//moves the ball to the new screen coordinates

					Xlast = X; //sets value of X after each loop to be in variable Xlast
					Ylast = Y; //sets value of Y after each loop to be in variable Ylast
					
					if(KEx<=ETHR | KEy<=ETHR )break; //if the x and y components of the kinetic energy are less than or equal to ETHR value, terminate the loop as the bounces are neglegible thereafter
					
					try {								//pause for 50 milliseconds
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					time += 0.1; //increments time by a step of 0.1 at the end of each loop 
			}		
		}		
			public GOval getBall() {
				return this.myBall;
			}
	}

			
			
			
