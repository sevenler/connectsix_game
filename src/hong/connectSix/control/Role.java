package hong.connectSix.control;

import hong.connectSix.engine.EngineFactory;
import hong.connnectSix.communicat.IEngine;
import java.io.Serializable;

public class Role implements Serializable,IEngine{
	private static final long serialVersionUID = 1L;
	
	//private static final String TAG="Role";
	
	public String name;//����
	public int type;//��ɫ���ͣ� ��Ӧֵ ΪRoleType
	public int color;//��ɫ
	
	public Role(String name, int type, int color) {
		super();
		this.name = name;
		this.type = type;
		this.color = color;
	}

	@Override
	public String toString() {
		return "Role [name=" + name + ", type=" + type + ", color=" + color
				+ ", onFinishedListener=" + onFinishedListener + "]";
	}

	public interface RoleType{
		//����
		public static final int MACHINE=0;
		//��
		public static final int HUMAN=1;
	}
	
	
	
	//�������֮����ü����¼����õĻ���������
	public OnFinishedListener onFinishedListener;
	
	public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
		this.onFinishedListener = onFinishedListener;
	}

	public interface OnFinishedListener{
		public void onResult(String result);
	}

	@Override
	public String getBest(){
		try {
			if(onFinishedListener==null){
				throw new NullPointerException("onFinishedListener is null,you must to initilized an OnFinishedListener instance to process the result!");
			}
			else {
				new Thread(){
					@Override
					public void run() {
						super.run();
						System.out.println("Role "+color+" searching ...");
						String result=EngineFactory.getInstance().getBest();
						onFinishedListener.onResult(result);
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int back() {
		EngineFactory.getInstance().back();
		return 0;
	}

	@Override
	public int reStart() {
		EngineFactory.getInstance().reStart();
		return 0;
	}

	@Override
	public void move(String step) {
		EngineFactory.getInstance().move(step);
	}
}


