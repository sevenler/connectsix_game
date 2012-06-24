package hong.connectSix.model;



public class Position {
	
	public int getScore(int type) {
		return score[type];
	}

	public void setScore(int score,int type) {
		this.score[type] = score;
	}

	@Override
	protected Position clone() throws CloneNotSupportedException {
		Position pos=new Position();
		pos.x=this.x;
		pos.y=this.y;
		pos.color=this.color;
		for(int i=0;i<3;i++){
			pos.score[i]=pos.score[i];
		}
		return pos;
	}

	/**
	 * ���ַ���ת���ɵ�
	 * @param p �ַ���������AA�������ַ�����������
	 * @return
	 */
	public static Position toInt(String p){
		Position pos=null;
		try{
			pos=new Position((p.charAt(0))-'A'+1,(p.charAt(1))-'A'+1);
		}catch(Exception e){
			throw new NumberFormatException("\""+p+"\" cannot fromat to Position");
		}
		return pos;
	}
	
	//��ʾ19*19���㣬1<=x,y<=19
	public int x;
	public int y;
	public int color=Color.EMPTY;//�����ɫ
	//��ķ�ֵ�� ������ֵ(�±꣺0)����ֵ(�±꣺1)�����غ�ֵ(�±꣺2)
	private int[] score = {0,0,0};
	
	
	public void fresh(){
		score[0]=0;
		score[1]=0;
		score[2]=0;
	}
	
	/**
	 * �жϸõ��Ƿ���Ч
	 * @return
	 */
	public boolean isLegal(){
		return (x>=1&&x<=19)&&(y>=1&&y<=19);
	}
	
	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Position(String po) {
		super();
		Position p=Position.toInt(po);
		this.x=p.x;
		this.y=p.y;
	}
	
	public Position() {super();}
	
	@Override
	public boolean equals(Object o) {
		if(o==null)return false;
		Position po=(Position)o;
		if(!((x==po.x)&&(y==po.y))) return false;
		//�����̵�
		//if(this.color!=po.color) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return (x-1)*19+y-1;
	}

	@Override
	public String toString(){
		return " {"+toChars()+" ["+score[0]+","+score[1]+","+score[2]+"] "+
		(color==0?"Empty":color==1?"White":"Black")+"} ";
	}
	
	public String toChars() {
		return (char)('A'+(x-1))+""+(char)('A'+(y-1));
	}
	
	public interface Color{
		public static final int EMPTY=0;
		public static final int WHITE=1;
		public static final int BLACK=2;
	}
	public interface ScoreType{
		static final  int ATTACK=0;
		static final  int DEFENCE=1;
		static final  int SUM=2;
	}
}

