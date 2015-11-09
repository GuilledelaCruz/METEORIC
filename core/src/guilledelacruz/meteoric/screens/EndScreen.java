package guilledelacruz.meteoric.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import guilledelacruz.meteoric.GameMain;

public class EndScreen implements Screen, InputProcessor{

	private GameMain game;
	
	private SpriteBatch batch;
    private BitmapFont bitmap;
    private GlyphLayout layout;
    
    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    
    private String level;
    private String maxLevel;
    
    private OrthographicCamera camera;
	
	public EndScreen(GameMain g){
		game = g;
	}

	public void show() {
		game.showAds(true);

		batch = new SpriteBatch();
        bitmap = game.getText();
        layout = new GlyphLayout();
        
        backgroundTexture = new Texture(Gdx.files.internal("images/background.png"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(game.WIDTH, game.HEIGHT * 2);
                
        if (game.SCORE > game.MAXSCORE){
        	game.MAXSCORE = game.SCORE;
        
        	FileHandle file = Gdx.files.local(game.pathScore);
        	Integer score = game.MAXSCORE;
        	file.writeString(score.toString(), false);
        }

		if (game.SCORE <= 60){
			level = game.getBundle().get("beginner");
		}else if(game.SCORE <= 120){
			level = game.getBundle().get("advanced");
		}else if(game.SCORE <= 240){
			level = game.getBundle().get("expert");
		}else{
			level = game.getBundle().get("master");
		}

		if (game.MAXSCORE <= 60){
			maxLevel = game.getBundle().get("beginner");
		}else if(game.MAXSCORE <= 120){
			maxLevel = game.getBundle().get("advanced");
		}else if(game.MAXSCORE <= 240){
			maxLevel = game.getBundle().get("expert");
		}else{
			maxLevel = game.getBundle().get("master");
		}
        
        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	}

	public void render(float delta) {
		camera.update();
	      
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();

        backgroundSprite.draw(batch);
        
        layout.setText(bitmap, game.getBundle().get("time") + " " + game.SCORE + " " + level);
        bitmap.draw(batch, layout, game.WIDTH / 2 - layout.width / 2, game.HEIGHT * 2 / 3 - layout.height / 2 + game.WIDTH / 18 + 24);
        layout.setText(bitmap, "Record: " + game.MAXSCORE  + " " + maxLevel);
        bitmap.draw(batch, layout, game.WIDTH / 2 - layout.width / 2, game.HEIGHT * 2 / 3 - layout.height / 2);
        
        layout.setText(bitmap, game.getBundle().get("touch2"));
        bitmap.draw(batch, layout, game.WIDTH / 2 - layout.width / 2, game.HEIGHT / 3 - layout.height / 2);
        
        batch.end();
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

	public void dispose() {
		backgroundTexture.dispose();
		batch.dispose();
	}

	public boolean keyDown(int keycode) {
		return false;
	}

	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.SPACE)
			game.gameScreen();
		return true;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		game.gameScreen();
		return true;
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
}
