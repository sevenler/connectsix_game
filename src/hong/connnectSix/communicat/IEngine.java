package hong.connnectSix.communicat;

public interface IEngine {
	public String getBest();
	/**
	 * 取消走子 注意：每次取消一个点
	 * @return
	 */
	public int back();
	/**
	 * 重新走子
	 * @return
	 */
	public int reStart();
	/**
	 * 走步
	 * @param step
	 */
	public void move(String step);
}
