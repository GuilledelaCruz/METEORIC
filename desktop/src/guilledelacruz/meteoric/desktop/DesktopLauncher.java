package guilledelacruz.meteoric.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import guilledelacruz.meteoric.GameMain;
import guilledelacruz.meteoric.IActivityRequestHandler;

public class DesktopLauncher implements IActivityRequestHandler{

	private static DesktopLauncher application;

	public static void main (String[] argv) {
		if (application == null) {
			application = new DesktopLauncher();
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 320;
		config.height = 480;
		config.addIcon("images/icon.png", Files.FileType.Internal);
		new LwjglApplication(new GameMain(application), config);
	}

	public void showAds(boolean show){

	}
}
