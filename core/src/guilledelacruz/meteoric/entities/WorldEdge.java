package guilledelacruz.meteoric.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import guilledelacruz.meteoric.GameMain;

public class WorldEdge {
	
	public enum WorldEdgeType {
		Left, Right
	}

    private Body body;

    public WorldEdge(World world, WorldEdgeType type){
    	 BodyDef edgeBody = new BodyDef();
         edgeBody.type = BodyDef.BodyType.StaticBody;
         
         if (type.equals(WorldEdgeType.Right)){
             edgeBody.position.set(GameMain.WIDTH / GameMain.PIXELS_TO_METERS, 0);
         }else{
             edgeBody.position.set(0, 0);
         }
         
         FixtureDef edgeDef = new FixtureDef();
         edgeDef.filter.categoryBits = GameMain.WORLD_ENTITY;
         edgeDef.filter.maskBits = GameMain.METEOR_ENTITY;
         
         EdgeShape edge = new EdgeShape();
         edge.set(0, 0, 0, GameMain.HEIGHT);
         edgeDef.shape = edge;
         
         body = world.createBody(edgeBody);
         body.createFixture(edgeDef);
         edge.dispose();
    }
    
    public Body getEdgeBody(){
		return body;
	}
}
