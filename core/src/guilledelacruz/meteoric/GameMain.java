package guilledelacruz.meteoric;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.I18NBundle;
import guilledelacruz.meteoric.screens.*;

import java.util.Locale;

public class GameMain extends Game {
	
	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

	private I18NBundle bundle;
	private BitmapFont text;
	private BitmapFont title;

	private StartScreen start;
	private GameScreen game;
	private EndScreen end;
	
	public Integer SCORE;
	public Integer MAXSCORE;
	public String pathScore;
	
	public static Integer WIDTH;
    public static Integer HEIGHT;
    public static Float scaleWidth;
	public static Float PIXELS_TO_METERS;
	
    public final static short METEOR_ENTITY = 0x1;
    public final static short WORLD_ENTITY = 0x2;
    public final static short OBSTACLE_ENTITY = 0x4;
    public final Float SCALE = 580f;
    
    private IActivityRequestHandler myRequestHandler;

    public GameMain(IActivityRequestHandler handler) {
        myRequestHandler = handler;
    }

	public void create () {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		scaleWidth = (float) WIDTH / SCALE;
		PIXELS_TO_METERS = 100f * scaleWidth;
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/editundo.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = WIDTH / 18;
		text = generator.generateFont(parameter);
		parameter.size = WIDTH / 6;
		title = generator.generateFont(parameter);
		generator.dispose();

		FileHandle fileHandle = Gdx.files.internal("translations/TranslationBundle");
		bundle = I18NBundle.createBundle(fileHandle, Locale.getDefault());
		
		start = new StartScreen(this);
		game = new GameScreen(this);
		end = new EndScreen(this);
		
		SCORE = 0;
		MAXSCORE = 0;		
		pathScore = "Meteoric/score.txt";
		
		FileHandle file = Gdx.files.local(pathScore);
		
		if (!file.exists()){
			file = Gdx.files.local(pathScore);
			file.writeString("0", false);
		}else{
			try{
				MAXSCORE = Integer.valueOf(file.readString());
			}catch (Exception e){
				MAXSCORE = 0;
			}
		}
		
		startScreen();
	}
	
	public void startScreen(){
		setScreen(start);
	}
	
	public void gameScreen(){
		setScreen(game);
	}
	
	public void endScreen(){
		setScreen(end);
	}

	public I18NBundle getBundle(){
		return bundle;
	}
	
	public BitmapFont getText(){
		return text;
	}
	
	public BitmapFont getTitle(){
		return title;
	}

	public void showAds(boolean show){
		myRequestHandler.showAds(show);
	}

}