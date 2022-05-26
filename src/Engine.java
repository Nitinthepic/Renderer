public class Engine implements Runnable {

	private final Thread gameLoopThread;
	private final Window window;
	private final ILogic logic;
	private final Timer timer;

	public Engine(String windowTitle, int width, int height, boolean vSync,
				  ILogic logic) {
		this.gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		this.window = new Window(windowTitle, width, height, vSync);
		this.logic = logic;
		timer = new Timer();
	}

	public void start() {
		gameLoopThread.start();
	}

	@Override
	public void run() {
		try {
			init();
			gameLoop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			logic.cleanup();
		}
	}

	private void init() throws Exception {
		window.init();
		timer.init();
		logic.init(window);
	}

	private void input(){
		logic.input(window);
	}

	private void update(float interval){
		logic.update(interval);
	}

	private void render(){
		logic.render(window);
		window.update();
	}

	private void sync() {
		float loopSlot = 1f / 60;
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

			while(accumulator>=interval){
				update(interval);
				accumulator -= interval;
			}
			render();
			if(!window.isvSyncEnabled())
				sync();
		}
	}
}
