public class Engine implements Runnable {

	public static final int TARGET_FPS = 75;

	public static final int TARGET_UPS = 30;
	private final Window window;
	private final ILogic logic;
	private final Timer timer;
	private final MouseInput mouseInput;

	public Engine(String windowTitle, int width, int height, boolean vSync,
				  ILogic logic) {
		this.window = new Window(windowTitle, width, height, vSync);
		this.logic = logic;
		this.mouseInput = new MouseInput();
		timer = new Timer();
	}

	@Override
	public void run() {
		try {
			init();

			gameLoop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	private void init() throws Exception {
		window.init();
		timer.init();
		mouseInput.init(window);
		logic.init(window);
	}

	private void input() {
		mouseInput.input(window);
		logic.input(window,mouseInput);
	}

	private void update(float interval) {
		logic.update(interval,mouseInput);
	}

	private void render() {
		logic.render(window);
		window.update();
	}

	private void sync() {
		float loopSlot = 1f / TARGET_FPS;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
			}
		}
	}

	private void gameLoop() {
		float loopStartTime;
		float accumulator = 0f;
		float interval = 1f / 30;
		boolean running = true;

		while (running && !window.shouldClose()) {
			loopStartTime = timer.getElapsedTime();
			accumulator += loopStartTime;

			input();

			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}
			render();
			if (!window.isvSyncEnabled())
				sync();
		}
	}

	protected void cleanup() {
		logic.cleanup();
	}
}
