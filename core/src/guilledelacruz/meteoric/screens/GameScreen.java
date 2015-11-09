package guilledelacruz.meteoric.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import guilledelacruz.meteoric.GameMain;
import guilledelacruz.meteoric.entities.Obstacle;
import guilledelacruz.meteoric.entities.Obstacle.ObstacleType;
import guilledelacruz.meteoric.entities.ContactEntities;
import guilledelacruz.meteoric.entities.Meteor;
import guilledelacruz.meteoric.entities.WorldEdge;
import guilledelacruz.meteoric.entities.WorldEdge.WorldEdgeType;

public class GameScreen implements Screen, InputProcessor {
	
	private GameMain game;
	private Boolean end;
	
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private BitmapFont bitmap;
    private GlyphLayout layout;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;
	private Texture textureForceL;
	private Texture textureForceR;
	private Sprite[] spriteForcesLeft;
	private Sprite[] spriteForcesRight;
	private Integer forceTouch;
	private int touchWall;
	private float screenDivision;
    
	private Music song;

    private World world;
    private Meteor meteor;
    private Animation meteorAnimation;
    private Animation meteorExplotion;
    private Body meteorBody;
    
    private List<Obstacle> asteroids;
    private List<Obstacle> asteroidsDestroy;
    private Obstacle asteroid;
	private Float starCounter;
    
    private Float timeCounter;
    private String level;
    
    private Random r;
    private Integer random;
    
    private Float secondsElapsed;
    private Float toEndScreen;
    
    private final float force = 4.75f;
    
    public GameScreen(GameMain g){
    	game = g;
    }

    public void show() {
		game.showAds(false);

    	end = false;
    	secondsElapsed = 0f;
    	toEndScreen = 0f;
		starCounter = 0f;
    	timeCounter = 0f;
    	level = "";
    	r = new Random();
		asteroids = new ArrayList<Obstacle>();
		asteroidsDestroy = new ArrayList<Obstacle>();
    	
        batch = new SpriteBatch();
        bitmap = game.getText();
        layout = new GlyphLayout();
        backgroundTexture = new Texture(Gdx.files.internal("images/background.png"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(game.WIDTH, game.HEIGHT * 2);
		textureForceL = new Texture(Gdx.files.internal("images/leftarrow.png"));
		textureForceR = new Texture(Gdx.files.internal("images/rightarrow.png"));
		spriteForcesLeft = new Sprite[10];
		spriteForcesRight = new Sprite[10];
		Sprite sprite;
		for(int i=0; i<10; i++){
			sprite = new Sprite(textureForceL);
			sprite.setSize(game.WIDTH / 21, sprite.getHeight() / 2);
			spriteForcesLeft[i] = sprite;
			sprite = new Sprite(textureForceR);
			sprite.setSize(game.WIDTH / 21, sprite.getHeight() / 2);
			spriteForcesRight[i] = sprite;
		}
		forceTouch = 0;
		touchWall = 0;
		screenDivision = game.WIDTH / 21;
        
        song = Gdx.audio.newMusic(Gdx.files.internal("music/song.mp3"));
		song.setLooping(true);
		song.setVolume(0.66f);
        song.play();

        world = new World(new Vector2(0f, 0f),true);
        world.setContactListener(new ContactEntities(this));
        
        meteor = new Meteor(world);
        meteorBody = meteor.getMeteorBody();
        meteorAnimation = meteor.getAnimation();
        meteorExplotion = meteor.getExplotion();
             
        new WorldEdge(world, WorldEdgeType.Left);
        new WorldEdge(world, WorldEdgeType.Right);
       
        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }
    
    public void render(float delta) {
		camera.update();

    	if (end){
    		toEndScreen += delta;
    		
    		if (toEndScreen >= 2f){
    			game.SCORE = secondsElapsed.intValue();
    			game.endScreen();
    			return;
    		}
    	}else{
        	secondsElapsed += delta;	
    	}

		// Step the physics simulation forward at a rate of 60hz
		world.step(1f / 60f, 6, 2);
		generator(delta);

		// clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        
        // draw background
        if (secondsElapsed < backgroundSprite.getHeight() / 2){
        	batch.draw(backgroundSprite, 0, -secondsElapsed.intValue() * 2, backgroundSprite.getWidth(), backgroundSprite.getHeight());
        }else{
        	batch.draw(backgroundSprite, 0, -backgroundSprite.getHeight() / 2);
        }

		// Set and update all obstacles' position like before and destroy every obstacle out of the screen and draw
		asteroidsDestroy.clear();
		for (Obstacle a: asteroids){
			if (a.getAsteroidBody().getPosition().y * game.PIXELS_TO_METERS <= -a.getAsteroidSprite().getHeight() / 2){
				world.destroyBody(a.getAsteroidBody());
				a.dispose();
				asteroidsDestroy.add(a);
			}else{
				a.getAsteroidSprite().setPosition((a.getAsteroidBody().getPosition().x * game.PIXELS_TO_METERS) - a.getAsteroidSprite().getWidth()/2,
						(a.getAsteroidBody().getPosition().y * game.PIXELS_TO_METERS) - a.getAsteroidSprite().getHeight()/2);
				batch.draw(a.getAsteroidSprite(), a.getAsteroidSprite().getX(), a.getAsteroidSprite().getY(), a.getAsteroidSprite().getWidth(), a.getAsteroidSprite().getHeight());
			}
		}
		asteroids.removeAll(asteroidsDestroy);

		// draw meteor
		if (!end){
			batch.draw(meteorAnimation.getKeyFrame(secondsElapsed, true), (meteorBody.getPosition().x * game.PIXELS_TO_METERS) - meteor.getWidth()/2 ,
					(meteorBody.getPosition().y * game.PIXELS_TO_METERS) - meteor.getHeight() * 5 / 6,
					meteor.getWidth(), meteor.getHeight());
		}else{
			batch.draw(meteorExplotion.getKeyFrame(toEndScreen, true), (meteorBody.getPosition().x * game.PIXELS_TO_METERS) - meteor.getWidth() ,
					(meteorBody.getPosition().y * game.PIXELS_TO_METERS) - meteor.getHeight() * 5 / 6,
					meteor.getWidth() * 2, meteor.getHeight());
		}

        if(secondsElapsed.intValue() <= game.MAXSCORE){
        	bitmap.draw(batch, game.getBundle().get("time") + " " + secondsElapsed.intValue(), game.WIDTH / 8, game.HEIGHT);
        }else{
        	bitmap.draw(batch, "Record: " + secondsElapsed.intValue(), game.WIDTH / 8, game.HEIGHT);
        }

        layout.setText(bitmap, game.getBundle().get("level") + " " + game.getBundle().get(level));
        bitmap.draw(batch, layout, game.WIDTH / 2, game.HEIGHT);

		if(forceTouch < 0){
			for(int i = 0; i < -forceTouch; i++)
				batch.draw(spriteForcesLeft[i], screenDivision * (9 - i),spriteForcesLeft[i].getHeight(), spriteForcesLeft[i].getWidth(), spriteForcesLeft[i].getHeight());
		}else{
			for(int i = 0; i < forceTouch; i++)
				batch.draw(spriteForcesRight[i], screenDivision * (11 + i),spriteForcesRight[i].getHeight(), spriteForcesRight[i].getWidth(), spriteForcesRight[i].getHeight());
		}

        batch.end();
    }

    public void dispose() {
    	backgroundTexture.dispose();
		textureForceL.dispose();
		textureForceR.dispose();
    	song.dispose();
        meteor.dispose();
        for (Obstacle a: asteroids){
        	a.dispose();
        }
        world.dispose();
    }

    public void endGame(){
    	end = true;
    }

	public void forceToZero(int wall){
		touchWall = wall;
		forceTouch = 0;
	}
    
    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {     
    	if (!end){
    		if(keycode == Input.Keys.D) 
        		meteorBody.applyForceToCenter(new Vector2(force ,0f), true);
        	if(keycode == Input.Keys.A)
        		meteorBody.applyForceToCenter(new Vector2(-force,0f), true);
    		}
        return !end;
    }

    public boolean keyTyped(char character) {
        return false;
    }
    
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	if (!end) {
			if (screenX >= game.WIDTH / 2) {
				meteorBody.applyForceToCenter(new Vector2(force, 0f), true);

				if(touchWall < 1){
					if(touchWall == -1){
						touchWall = 0;
					}
					if (forceTouch <= 9){
						forceTouch = forceTouch + 1;
					}
				}else{
					touchWall = 1;
					forceTouch = 0;
				}
			} else {
				meteorBody.applyForceToCenter(new Vector2(-force, 0f), true);

				if(touchWall > -1){
					if(touchWall == 1){
						touchWall = 0;
					}
					if (forceTouch >= -9){
						forceTouch = forceTouch - 1;
					}
				}else{
					touchWall = -1;
					forceTouch = 0;
				}
			}
		}
        return !end;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }

	public void resize(int width, int height) {	
		game.WIDTH = width;
		game.HEIGHT = height;
		game.scaleWidth = (float) width / game.SCALE;
		camera.viewportWidth=width;
        camera.viewportHeight=height;
        camera.update();
	}

	public void pause() {
		
	}

	public void resume() {
		
	}

	public void hide() {
		dispose();
	}

    private void generator(Float delta){

		starCounter += delta;
    	timeCounter += delta;

		if(secondsElapsed.intValue() <= 3){
			level = "beginner";
			if (timeCounter >= 0.5f) {
				timeCounter -= 0.5f;
			}
		}else if(secondsElapsed.intValue() <= 30){
    		level = "beginner";
    		if (timeCounter >= 0.5f){
    			timeCounter -= 0.5f;
    			
    			asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
		        asteroids.add(asteroid);
		        if (r.nextInt(2) == 1){
		        	asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
			        asteroids.add(asteroid);
		        }
    		}
    	}else if(secondsElapsed.intValue() <= 60){
    		level = "beginner";
    		if (timeCounter >= 0.43f){
    			timeCounter -= 0.43f;
    			
    			random = r.nextInt(16);
    			
    			if (random <= 2){
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    			}else if(random <= 7){
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    		        asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    			}else if(random <= 11){
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Planet);
    		        asteroids.add(asteroid);
    		        timeCounter -= 0.2f;
    			}else{
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Planet);
    		        asteroids.add(asteroid);
    		        timeCounter -= 0.2f;
    			}
    		}
    	}else if(secondsElapsed.intValue() <= 120){
    		level = "advanced";
    		if (timeCounter >= 0.38f){
    			timeCounter -= 0.38f;
    			
    			random = r.nextInt(15);
    			
    			if (random <= 2){
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    			}else if(random <= 7){
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    		        asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    			}else if(random <= 12){
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Planet);
    		        asteroids.add(asteroid);
    		        timeCounter -= 0.172f;
    			}else{
    				int position = r.nextInt(game.WIDTH);
    				asteroid = new Obstacle(world, position, game.HEIGHT, ObstacleType.Planet);
    		        asteroids.add(asteroid);
    		        if (position < game.WIDTH / 2){
    		        	int auxiliar = position + (128);
        				asteroid = new Obstacle(world, auxiliar + r.nextInt(game.WIDTH - auxiliar), game.HEIGHT, ObstacleType.Planet);
        		        asteroids.add(asteroid);
    		        }else{
        				asteroid = new Obstacle(world, r.nextInt(position) - 128, game.HEIGHT, ObstacleType.Planet);
        		        asteroids.add(asteroid);
    		        }
    		        timeCounter -= 0.172f;
    			}
    		}
    	}else{
			if(secondsElapsed.intValue() <= 240){
				level = "expert";
			}else{
				level = "master";
			}
    		if (timeCounter >= 0.34f){
    			timeCounter -= 0.34f;
    			
    			random = r.nextInt(19);
    			
    			if(random <= 7){
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    		        asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
    			}else if(random <= 13){
    				asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
    		        asteroids.add(asteroid);
					asteroid = new Obstacle(world, r.nextInt(game.WIDTH), game.HEIGHT, ObstacleType.Asteroid);
					asteroids.add(asteroid);
    				asteroid = new Obstacle(world, 64 + r.nextInt(game.WIDTH - 128), game.HEIGHT, ObstacleType.Planet);
    		        asteroids.add(asteroid);
    		        timeCounter -= 0.22f;
    			}else if(random <= 17){
					int position = 64 + r.nextInt(game.WIDTH - 128);
					asteroid = new Obstacle(world, position, game.HEIGHT, ObstacleType.Planet);
					asteroids.add(asteroid);
					if (position < game.WIDTH / 2){
						int auxiliar = position + (128);
						asteroid = new Obstacle(world, auxiliar + r.nextInt(game.WIDTH - auxiliar), game.HEIGHT, ObstacleType.Planet);
						asteroids.add(asteroid);
					}else{
						asteroid = new Obstacle(world, 64 + r.nextInt(position - (128 + 64)), game.HEIGHT, ObstacleType.Planet);
						asteroids.add(asteroid);
					}
    				timeCounter -= 0.22f;
    			}else{
					if(starCounter >= 2f) {
						starCounter = 0f;
						int position = 128 + r.nextInt(game.WIDTH - (128 * 2));
						asteroid = new Obstacle(world, position, game.HEIGHT, ObstacleType.Star);
						asteroids.add(asteroid);
						if (position < game.WIDTH / 2){
							int auxiliar = position + (128 + 64);
							asteroid = new Obstacle(world, auxiliar + r.nextInt(game.WIDTH - auxiliar), game.HEIGHT, ObstacleType.Planet);
							asteroids.add(asteroid);
						}else{
							asteroid = new Obstacle(world, 64 + r.nextInt(position - (128 + 128)), game.HEIGHT, ObstacleType.Planet);
							asteroids.add(asteroid);
						}
						timeCounter -= 0.75f;
					}else{
						int position = 64 + r.nextInt(game.WIDTH - 128);
						asteroid = new Obstacle(world, position, game.HEIGHT, ObstacleType.Planet);
						asteroids.add(asteroid);
						if (position < game.WIDTH / 2){
							int auxiliar = position + (128);
							asteroid = new Obstacle(world, auxiliar + r.nextInt(game.WIDTH - auxiliar - 64), game.HEIGHT, ObstacleType.Planet);
							asteroids.add(asteroid);
						}else{
							asteroid = new Obstacle(world, 64 + r.nextInt(position - (128 + 64)), game.HEIGHT, ObstacleType.Planet);
							asteroids.add(asteroid);
						}
						timeCounter -= 0.22f;
					}
    			}
    		}
    	}
    }
}
