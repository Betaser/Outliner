package base;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import base.input.Input;
import miscDataStructures.Camera;
import state.EditingOutlineState;
import state.State;

public class Game {
	
	private Map<Double, BufferedImage> imageLayers;
	
	private static Game game;

	int targetFPS;
	State state;
	GamePanel gamePanel;
	
	public JFrame window;
	public JLayeredPane panelManager;
	public Input input;
	public Camera camera;
	public final Camera DEF_CAMERA = new Camera(1, 0, 0);
	public int winWidth, winHeight;
			
	public Game(String programName, int width, int height, int targetFPS) {
		this.targetFPS = targetFPS;
		game = this;
		winWidth = width;
		winHeight = height;
		imageLayers = new TreeMap<>(Game::imageLayerCmp);

		window = new JFrame(programName);
		window.setSize(width, height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		gamePanel = new GamePanel();
		panelManager = new JLayeredPane();
		panelManager.setBorder(BorderFactory.createEmptyBorder());
		panelManager.setPreferredSize(new Dimension(winWidth, winHeight));
		window.add(gamePanel);
		window.pack();
		gamePanel.add(panelManager);
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		input = new Input();
		gamePanel.addKeyListener(input);
		gamePanel.addMouseListener(input);
		gamePanel.addMouseWheelListener(input);
		gamePanel.addMouseMotionListener(input);
		gamePanel.setFocusTraversalKeysEnabled(false);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		
		camera = new Camera(1, 0, 0);
		state = new EditingOutlineState();
		
		state.init();
	}
	
	public class GamePanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private Map<Double, BufferedImage> layersToDraw;
		
		boolean startedRunning;
		
		public GamePanel() {
			layersToDraw = new TreeMap<>();
			setPreferredSize(new Dimension(winWidth, winHeight));
			setBackground(Color.WHITE);
			setDoubleBuffered(true);
		}

		//	Wildly asynchronous
		public void repaint() {
			if (!startedRunning)
				return;
			super.repaint();
			synchronized (layersToDraw) {
				layersToDraw.clear();
				layersToDraw.putAll(imageLayers);
				imageLayers.clear();
			}
		}

		//	Wildly asynchronous
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(new Color(255, 254, 250));
			g.fillRect(0, 0, winWidth, winHeight);
			
			synchronized (layersToDraw) {
				for (BufferedImage layer : layersToDraw.values())
					g.drawImage(layer, 0, 0, winWidth, winHeight, null);
			}
			
			g.setColor(Color.BLACK);
			g.drawString("FPS: " + targetFPS, 0, 10);
		}
		
	}
	
	private static int imageLayerCmp(double d1, double d2) {
		if (d1 != d2)
			return d1 < d2 ? 1 : -1;
		return 0;
	}
	
	public BufferedImage createLayer(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
	}
	
	public void draw(BufferedImage image, double x, double y, double width, double height, double depth, Camera cam) {
		Objects.requireNonNull(cam);
		if (imageLayers.containsKey(depth)) {
			Graphics g = imageLayers.get(depth).getGraphics();
			g.drawImage(image, (int) (x - cam.xPos), (int) (winHeight - y - height + cam.yPos), (int) width, (int) height, null);
			return;
		}
		BufferedImage layer = createLayer((int) (winWidth * cam.zoom), (int) (winHeight * cam.zoom));
		Graphics g = layer.getGraphics();
		g.drawImage(image, (int) (x - cam.xPos), (int) (winHeight - y - height + cam.yPos), (int) width, (int) height, null);
		imageLayers.put(depth, layer);
	}
	
	public void run() {
		double drawInterval;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int renderCount = 0;
		gamePanel.startedRunning = true;
		
		while (true) {
			drawInterval = 1e9 / targetFPS;
			now = System.nanoTime();
			delta += (now - lastTime) / drawInterval;
			timer += now - lastTime;
			lastTime = now;
			
			if (delta >= 1) {
				input.tick();
				state.tick();
				state.render();
				gamePanel.repaint();
				renderCount++;
				delta--;
			}
			
			if (timer >= 1e9) {
				targetFPS = renderCount;
				renderCount = 0;
				timer = 0;
			}
		}
	}
	
	public void focusGamePanel() {
		gamePanel.requestFocus();
	}
	
	public int getSceneWidth() {
		return (int) (winWidth * camera.zoom);
	}
	
	public int getSceneHeight() {
		return (int) (winHeight * camera.zoom);
	}
	
	public static Game get() {
		return game;
	}
	
}
