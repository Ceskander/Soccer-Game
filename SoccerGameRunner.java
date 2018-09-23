//READ ME:
//Farkhondeh, Vista & Crystal Eskander Final Project

//Date:5/26/16

//Did you receive help? Yes, we used the physics example from Mr.Wai's examples. 
//Vista: goalies, score, time, wall bounce, mouse pressed
//Crystal: Collision with Goalie, song, wall bounce, mouse released

//
import processing.core.PApplet; 
import processing.core.PFont;
import processing.core.PImage;

//import java.lang.*;
import java.util.*;
import java.applet.*;
//import java.awt.*;
//import java.io.*;
import java.awt.event.MouseEvent;


import javax.swing.*;

//import java.util.*;

public class SoccerGameRunner extends PApplet
{
	
	@SuppressWarnings("serial")
	PImage soccerBall = loadImage("soccerballRevised.png");
	PImage goalie = loadImage("goalie.png");
	PImage cover = loadImage("WillFerrellCover.jpg");
	PImage gameOverImage = loadImage("gameOverImage.jpg");
	PImage field = loadImage("soccerField.jpg");
	PImage goal = loadImage("soccerGoalRevised.jpg");
	int StageX = 1000;
	int StageY = 700;
	
	PImage enemyPic = loadImage("goalie.png");
	int enemyX = 350;
	int enemyY = 25;
	
	boolean enemyGoLeft = true;
	boolean enemyGoRight = false;
	
	PImage secondEnemyPic = loadImage("goalie.png");
	int secEnemyX = 200;
	int secEnemyY = 25;

	boolean secEnemyGoLeft = true;
	boolean secEnemyGoRight = false;
	
	int mousePressedX = 0;
	int mousePressedY = 0;
	int mouseReleasedX = 0;
	int mouseReleasedY = 0;

	
	int randX = 0;
	int randY = 0;
	int ballX = 0;
	int ballY = 0;
	
	int ballsTotal = 1;
	Ball[] balls = new Ball[ballsTotal];
	int ballsIterator = 0;
	int counter = 60;
	int time = 45;
	
	int left = 0;
	int right = StageX - 50;
	int top = -200000;
	int bottom = StageY - 230;
	
	float spring = .3f;
	float gravity = .0f;
	float bounce = .5f;
	int score = 0;
	int highScore = 0;
	boolean onBall = false;
	boolean scored = false;
	
	boolean hitsGoalie = false;
	
	float drag = .98f;
	boolean gameOver = false;
	boolean gameStart = false;
	boolean needHelp = false;
	PFont myFont = createFont("Arial", 45 );
	Random randomNumberGenerator = new Random();
	
	AudioClip shakira;
	
	//song
	public void setup()
	{
		size(1000, 800);
		background(200);
		smooth();
		frameRate(60);
		shakira = getAudioClip(getCodeBase(),"Shakira -waka waka (1).wav");
		shakira.play();
		shakira.loop();
	}		
		
	//draws pictures on the screen

		public void draw()
		{
			
		
			if(!gameOver)
			{
				if(gameStart)
				{
					background(200);
					image(goalie, 50, 50, 50, 50); 
					int randomX = 300 +randomNumberGenerator.nextInt(400);
					int randomY =  650+ randomNumberGenerator.nextInt(1);
				
					
					if (ballsIterator < ballsTotal)
					{
						ballX = randomX;
						ballY = randomY;
						float width =  40;
						balls[ballsIterator] = new Ball(ballX-20, ballY-20, width, width);
						ballsIterator++;
					}
					stroke(0);
					line(ballX, ballY, mouseX , mouseY);
					stroke(0,0,255);
					noStroke();
					
					
					
					
					//how the balls interact alone
					for (int i = 0; i < ballsIterator ; i++)
					{
						Ball ball = balls[i];
						checkWalls(ball);
						checkgoalie(ball);
						ballReset(ball);
						if(ball.vx != 0 && ball.vy != 0)
						{
							ball.vx *= drag;
							ball.vy += gravity;
						}
						
						ball.draw();
						
					}
					counter--;
					if( counter == 0)
					{
						time--; 
						counter = 60;
					}
					if(time == 0)
					{
						gameOver = true;
					}
					fill(255,192,203);
					textFont(myFont);
					text("Score: " + score, 20, 450, 500, 500);
					text("Time Left: " + time, 680, 450, 500, 500);
					if(time == 0)
					{
						if(score > highScore)
						{
							highScore = score;
							
						}
					}
				}
				else
				{
					image(cover, 0, 20, 1000, 730);
					textFont(myFont);
					fill(0,255,255);
					text("Play", 480, 50, 500, 500);
					text("How to Play", 400, 130, 500, 500);
					fill(255,192,203);
					
					if(needHelp)
					{
						JOptionPane.showMessageDialog(null, "                            HOW TO PLAY\n\n"

						
						+"Click on the soccer ball. Then while holding \n"
						+"down your click, drag the cursor in the opposite \n"
						+"direction of the desired kick, and before the ball \n"+
						"hits the line drag the ball again. You have 45 seconds \n"
						+"to kick the ball in the goal, while avoiding the goalie. \n" +
						"Good Luck!" ); 
						needHelp = false;
					}
					
					
				}
			}
			else
			{
				fill(255, 0, 0);
				background(200);
				image(gameOverImage, 0, 0, 1000, 500);
				textFont(myFont);
				text("Game Over", 400, 50, 500, 500);
				text("Score: " + score, 20, 450, 500, 500);
				text("High Score: " +highScore, 400, 375, 500, 500);
				text("Try Again?", 400, 450, 400, 400);
				
				
			}
		}
		
		////////////////////////////////////////////////////////////////////////
		// FUNCTIONS
		
		//kicking the ball my pressing the mouse

		public void mousePressed(MouseEvent e)
		{
			try
			{
				
					
				if(gameStart)	
				{
					if(abs( mouseX - balls[ballsIterator - 1].getX() )<100)
					{
						mousePressedX = mouseX;
						mousePressedY = mouseY;
						onBall = true;
					}
				}
				else
				{
					if(abs(mouseX - 520) <= 50 && abs(mouseY - 70) <= 30 )
					{					
						gameStart = true;
					}
					else if(abs(mouseX - 500)<= 130 && abs(mouseY - 160) <= 30)
					{
						needHelp = true;
					}
				}
				if(gameOver)
				{
					if(abs(mouseX - 490) <= 110 && abs(mouseY - 480) <= 40 )
					{
						gameOver = false;
						score = 0; 
						time = 45;
					}
					
				}
					//score++;
					
				
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
		
	//aiming the ball by releasing the mouse
		public void mouseReleased(MouseEvent m)
		{
			try
			{
				
					if(onBall)
					{
						mouseReleasedX = mouseX;
						mouseReleasedY = mouseY;
						balls[ballsIterator-1].vx = (mousePressedX - mouseReleasedX)*.14f;
						balls[ballsIterator-1].vy = (mousePressedY- mouseReleasedY)*.10f;
						onBall = false;
					}
					
					
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
		
		//collisons for the soccerball
		public void checkCollision(Ball ballA, Ball ballB)
		{
			float desX = ballA._x - ballB._x;
			float desY = ballA._y - ballB._y;
			float dis = sqrt(desX * desX + desY * desY);
			float len = ballA._width + ballB._width;
			if (dis < len)
			{
				float angle = atan2(desY, desX);
				float sine = sin(angle);
				float cosine = cos(angle);
				
				Point2D pos0 = new Point2D(0, 0);
				Point2D pos1 = rotate(desX, desY, sine, cosine, true); // rotate ball1's position
				Point2D vel0 = rotate(ballA.vx, ballA.vy, sine, cosine, true); // rotate ball0's velocity
				Point2D vel1 = rotate(ballB.vx, ballB.vy, sine, cosine, true); // rotate ball1's velocity
				
				float vxTotal = vel0.x - vel1.x;
				vel0.x = ((ballA.mass - ballB.mass) * vel0.x + 2 * ballB.mass * vel1.x) / (ballA.mass + ballB.mass);
				vel1.x = vxTotal + vel0.x;
				
				float absV = Math.abs(vel0.x) + Math.abs(vel1.x);
				float overlap = (ballA._width / 2 + ballB._width / 2) - Math.abs(pos0.x - pos1.x);
				pos0.x += vel0.x / absV * overlap;
				pos1.x += vel1.x / absV * overlap;
				
				Point2D vel0F = rotate(vel0.x, vel0.y, sine, cosine, false);
				Point2D vel1F = rotate(vel1.x, vel1.y, sine, cosine, false);
				ballA.vx = vel0F.x;
				ballA.vy = vel0F.y;
				ballB.vx = vel1F.x;
				ballB.vy = vel1F.y;
			}
		}

		//soccer ball bounces off the walls
		public void checkWalls(Ball ball)
		{
			ball._x += ball.vx;
			if (ball._x < left + ball._width / 2)
			{
				ball._x = left + ball._width / 2;
				ball.vx *= 0 * bounce;
			}
			else if (ball._x > right - ball._width / 2)
			{
				ball._x = right - ball._width / 2;
				ball.vx *= 0 * bounce;
			}
			ball._y += ball.vy;
			if (ball._y < top + ball._height / 2)
			{
				ball._y = top + ball._height / 2;
				ball.vy *= 0 * bounce;
			}
			else if (ball._y > bottom - ball._height / 2)
			{
				ball._y = bottom - ball._height / 2;
				ball.vy *= 0 * bounce;
			}
			
			
		}
		
	//soccer ball bounces off of goalie and doesn’t score
		public void checkgoalie(Ball ball)
		{
			
			
			/*if(abs(ball._x - enemyX) <=25 && abs(ball._y - enemyY) <= 60 )
			{
				ball.vx *= -1.18 * bounce;
				ball.vy *= 1* bounce;
			}

			else if(abs(ball._x -enemyX) <= 20 && abs(ball._y - enemyY) <= 20 )
			{
				ball.vx *= -.7 * bounce;
				ball.vy *= -1 * bounce;
			}
			else if(abs(ball._x - enemyX) <= 10 && abs(ball._y - enemyY) <= 20 )
			{
				ball.vx *= -1 * bounce;
				ball.vy *= -1 * bounce;
			}*/
			
			/*if( Math.abs( ball._x -enemyX)<50 && Math.abs(ball._x-enemyY)< 50 )
			{
				ball.vx *= -1 * bounce;
				ball.vy *= -1 * bounce;
				score+=0;
			}
			*/
			
			
			if(ball._y<=(enemyY + 200) &&ball._y >=(enemyY-200) && ball._x <= (enemyX+100) && ball._x>(enemyX-100 ))
			{
	            
	            score+=0;
	            ball.vy *= -2 * bounce;
	            ball.vx *= -2 * bounce;
	            scored=false;
	            
	        }
			else if(ball._y<=(secEnemyY + 200) &&ball._y >=(secEnemyY-200) && ball._x <= (secEnemyX+100) && ball._x>(secEnemyX-100 ))
			{
	            
	            score+=0;
	            ball.vy *= -2 * bounce;
	            ball.vx *= -2 * bounce;
	            scored=false;
	            
	        }
