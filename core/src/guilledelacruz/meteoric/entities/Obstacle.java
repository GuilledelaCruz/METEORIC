package guilledelacruz.meteoric.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import guilledelacruz.meteoric.GameMain;

public class Obstacle {

	public enum ObstacleType {
		Asteroid, Planet, Star
	}
	
	private Sprite sprite;
    private Texture img;
    private Body body;
    private ObstacleType type;
    
    public Obstacle(World world, Integer Xposition, Integer Yposition, ObstacleType type){
    	
    	Random r = new Random();
    	
    	if (type.equals(ObstacleType.Planet)){
    		img = new Texture("images/planets/planet"+ (r.nextInt(6) + 1) +".png"); 
    	}else if (type.equals(ObstacleType.Star)){
    		img = new Texture("images/stars/star"+ (r.nextInt(3) + 1) +".png");
    	}else{
    		img = new Texture("images/asteroids/asteroid"+ (r.nextInt(3) + 1) +".png");
    	}
    	this.type = type;
        sprite = new Sprite(img);
        sprite.setSize(sprite.getWidth() * GameMain.scaleWidth, sprite.getHeight() * GameMain.scaleWidth);
        sprite.setPosition(Xposition - sprite.getWidth()/2, Yposition);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) / GameMain.PIXELS_TO_METERS, 
                (sprite.getY() + sprite.getHeight()/2) / GameMain.PIXELS_TO_METERS);

        CircleShape shape = new CircleShape();
        shape.setRadius(sprite.getWidth() / 2 / GameMain.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
		if(type.equals(ObstacleType.Asteroid)){
			fixtureDef.density = 0.1f;
		}else if(type.equals(ObstacleType.Planet)){
			fixtureDef.density = 0.5f;
		}else{
			fixtureDef.density = 1.0f;
		}
        fixtureDef.filter.categoryBits = GameMain.OBSTACLE_ENTITY;
        fixtureDef.filter.maskBits = GameMain.METEOR_ENTITY;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();
        
        body.setLinearVelocity(0f, -3.25f);
    }
    
    public Sprite getAsteroidSprite() {
		return sprite;
	}

	public Texture getAsteroidTexture() {
		return img;
	}
	
	public Body getAsteroidBody(){
		return body;
	}
	
	public ObstacleType getType(){
		return type;
	}
	
	public void dispose(){
		img.dispose();
	}
}
