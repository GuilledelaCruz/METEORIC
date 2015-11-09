package guilledelacruz.meteoric.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import guilledelacruz.meteoric.GameMain;
import guilledelacruz.meteoric.screens.GameScreen;

public class ContactEntities implements ContactListener {
	
	private GameScreen screen;
	private Sound sound;
	
	public ContactEntities(GameScreen s){
		screen = s;
		sound = Gdx.audio.newSound(Gdx.files.internal("music/explode.ogg"));
	}

	public void beginContact(Contact contact) {
		if(contact.getFixtureA().getBody().getPosition().x * GameMain.PIXELS_TO_METERS < GameMain.WIDTH / 2){
			screen.forceToZero(-1);
		}else{
			screen.forceToZero(1);
		}

		Short a = contact.getFixtureA().getFilterData().categoryBits;
		Short b = contact.getFixtureB().getFilterData().categoryBits;
		
		if (a.equals(GameMain.METEOR_ENTITY) && b.equals(GameMain.OBSTACLE_ENTITY) ||
				b.equals(GameMain.METEOR_ENTITY) && a.equals(GameMain.OBSTACLE_ENTITY)){

			screen.endGame();
			sound.play();
		}
	}

	public void endContact(Contact contact) {
		
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
}
