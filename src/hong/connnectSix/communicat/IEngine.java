package hong.connnectSix.communicat;

public interface IEngine {
	public String getBest();
	/**
	 * ȡ������ ע�⣺ÿ��ȡ��һ����
	 * @return
	 */
	public int back();
	/**
	 * ��������
	 * @return
	 */
	public int reStart();
	/**
	 * �߲�
	 * @param step
	 */
	public void move(String step);
}
