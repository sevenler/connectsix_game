package hong.connectSix.model;


public class Road {
	
	@Override
	protected Road clone() throws CloneNotSupportedException {
		Road road=new Road();
		road.first=this.first;
		road.last=this.last;
		road.index=this.index;
		road.direction=this.direction;
		road.score=this.score;
		road.isDrop=this.isDrop;
		road.color=this.color;
		road.count=this.count;
		road.countWhenDrop=this.countWhenDrop;
		return road;
	}
	public Position first=null;
	public Position last=null;
	public int index=0;//1<=index<=924
	public int direction=0;
	
	public int score=0;
	public boolean isDrop=false;
	public int color =Position.Color.EMPTY;
	public int count=0;//·��ӵ���ӵ����� 0<=count<=6
	/**
	 * ��·��������ʱ�򣬼�¼����ǰӵ���ӵ�����
	 * ·�������Ժ���1�λ���ȡ��������Ҫ��·��Ч���лָ�
	 * ��count==countWhenDropʱ���ͻָ���Ч
	 */
	public int countWhenDrop=-1;
	
	public void fresh(){
		score=0;
		isDrop=false;
		color=Position.Color.EMPTY;
		count=0;
		countWhenDrop=-1;
	}
	
	/**
	 * ��ȡ·�ϵ�index����
	 * @param index
	 */
	public Position getPositionIndexOf(int index){
		return null;
	}
	
	public Road() {
		super();
	}

	public Road(int index) {
		super();
		this.index = index;
	}
	
	public Road(Position first, Position last, int index, int direction){
		this.first = first;
		this.last = last;
		this.index = index;
		this.direction = direction;
	}

	public Road(Position first, Position last, int index, int direction,
			int score, boolean isDrop, int color, int count, int countWhenDrop) {
		super();
		this.first = first;
		this.last = last;
		this.index = index;
		this.direction = direction;
		this.score = score;
		this.isDrop = isDrop;
		this.color = color;
		this.count = count;
		this.countWhenDrop = countWhenDrop;
	}
	
	@Override
	public boolean equals(Object o) {
		Road r=(Road)o;
		return this.index==r.index
		&& this.first==r.first
		&& this.last==r.last
		&& this.direction==r.direction
		&& this.score==r.score
		&& this.isDrop==r.isDrop
		&& this.color==r.color
		&& this.count==r.count
		&& this.countWhenDrop==r.countWhenDrop;
	}

	@Override
	public int hashCode() {
		return this.index;
	}
	
	@Override
	public String toString() {
		return "Road [first=" + first + ", last=" + last + ", index=" + index
				+ ", direction=" + direction + ", score=" + score + ", isDrop="
				+ isDrop + ", color=" + color + ", count=" + count
				+ ", countWhenDrop=" + countWhenDrop + "]";
	}

	/**
	 * ·�ķ���
	 * @author Hong
	 *
	 */
	public interface Direction{
		/**
		 * ˮƽ����
		 */
		public static final int HORIZONTAL=1;
		/**
		 * ��ֱ����
		 */
		public static final int VERTICAL=2;
		/**
		 * ����������
		 */
		public static final int LEFTUPTORIGHTDOWN=3;
		/**
		 * ����������
		 */
		public static final int RIGHTUPTOLEFTDOWN=4;
	}
	public interface ChessType{
		public static final int ONE=0;
		public static final int TWO=1;
		public static final int THREE=2;
		public static final int FOUR=3;
		public static final int FIVE=4;
		public static final int SIXE=5;
	}
}
