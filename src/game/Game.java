package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import gameObjects.Board;
import gameObjects.Piece;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 3119695923651282663L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;

	private Thread thread;
	private boolean running = false;
	public Handler handler;
	public Input input;

	public boolean keys = false;

	public Game() {
		System.out.println("starting");
		new Panel(WIDTH, HEIGHT, "Paint", this);
		input = new Input(this);
		handler = new Handler(input);
		input.setHandler(handler);
		System.out.println("i just made a handler");
		this.addKeyListener(input);
		this.addMouseListener(input);
		Level.startLevel(this, 7, 7, 3);
		start();
	}

	public static void main(String[] args) {
		Game g = new Game();
		// Thread thread = new Thread(new Game());
		// g.start();
	}

	public synchronized void start() {
		System.out.println("we've begun");
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		double numUps = 60.0;
		double ns = 1000000000 / numUps;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				render();
				delta--;
				frames++;
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
	}

	public void update() {
		handler.updateObjects();
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		handler.renderObjects(g);

		g.dispose();
		bs.show();

	}

	public Handler getHandler() {
		return handler;
	}

}
