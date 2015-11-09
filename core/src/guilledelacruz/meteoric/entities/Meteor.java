package guilledelacruz.meteoric.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import guilledelacruz.meteoric.GameMain;

public class Meteor {

    private TextureAtlas textureAtlas;
    private Animation animation;
    private TextureAtlas explodeAtlas;
    private Animation explode;
    private Body body;
    private Float width;
    private Float height;
    
    public Meteor(World world){
    	textureAtlas = new TextureAtlas(Gdx.files.internal("images/m.txt"));
    	animation = new Animation(1/12f, textureAtlas.getRegions());
    	explodeAtlas = new TextureAtlas(Gdx.files.internal("images/e.txt"));
    	explode = new Animation(1/1.9f, explodeAtlas.getRegions());
    	
    	width = animation.getKeyFrame(0f).getRegionWidth() * GameMain.scaleWidth;
    	height = animation.getKeyFrame(0f).getRegionHeight() * GameMain.scaleWidth;
    	
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(GameMain.WIDTH / 2 / GameMain.PIXELS_TO_METERS, 
                (GameMain.HEIGHT / 10 + height * 5 / 6) / GameMain.PIXELS_TO_METERS);

        CircleShape shape = new CircleShape();
        shape.setRadius((width / 2) / GameMain.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.5f;
        fixtureDef.filter.categoryBits = GameMain.METEOR_ENTITY;
        fixtureDef.filter.maskBits = GameMain.WORLD_ENTITY | GameMain.OBSTACLE_ENTITY;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();
    }
	
	public Body getMeteorBody(){
		return body;
	}
	
	public Animation getAnimation(){
		return animation;
	}
	
	public Animation getExplotion(){
		return explode;
	}
	
	public Float getWidth(){
		return width;
	}
	
	public Float getHeight(){
		return height;
	}
	
	public void dispose(){
		textureAtlas.dispose();
	}
}
