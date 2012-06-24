package hong.connectSix.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 棋谱：使用栈存储已落子
 * @author Hong
 *
 */
public class Manual {
	
	/**
	 * 空棋盘串
	 */
	public static final String EMPTYMANUAL="NULL";
	
	public static final String RESEARCH="research";
	
	/**
	 * Manual.EMPTYMANUAL棋盘串来构造时，返回null
	 * @param manual
	 * @return
	 */
	public static List<Position> toPositions(String manual) throws Exception{
		if(manual.length()%2!=0) throw new IllegalArgumentException(" input manual is wrong ");
		List<Position> list=new LinkedList<Position>();
		if(manual.equals(Manual.EMPTYMANUAL)) return list;
		int size=manual.length();
		Position temp=null;
		for(int i=0;i<size;i=i+2){
			temp=Position.toInt(manual.substring(i, i+2));
			if(temp.isLegal())
				list.add(temp);
			//else throw new IllegalArgumentException(" input manual is wrong ");
		}
		return list;
	}
	
	@Override
	protected Manual clone() throws CloneNotSupportedException {
		Manual manual=new Manual();
		for(Position pos : this.movedPositions){
			manual.movedPositions.add(pos);
		}
		return manual;
	}
	List<Position> movedPositions=new LinkedList<Position>();
	
	/**
	 * 获得退回走步的数量
	 * @return
	 */
	public int getBackNumber(){
		int count=this.movedPositions.size();
		if(count<=1) return count;
		else{
			return count%2+1;
		}
	}
	
	//是否包括点pos
	public boolean contains(Position pos){
		return this.movedPositions.contains(pos);
	}
	/**
	 * 添加点到最后
	 * @param po
	 */
	public void push(Position po){
		this.movedPositions.add(po);
	}
	/**
	 * 删除尾子，并将其返回
	 * @return
	 */
	public Position pull(){
		int index=this.movedPositions.size()-1;
		Position pos=this.movedPositions.get(index);
		this.movedPositions.remove(index);
		return pos;
	}
	/**
	 * 获取尾子，不删除
	 * @return
	 */
	public Position peek(){
		int index=this.movedPositions.size()-1;
		Position pos=this.movedPositions.get(index);
		return pos;
	}
	/**
	 * 返回棋盘的最后走步
	 * @return
	 */
	public Step peekStep(){
		int count=this.movedPositions.size();
		if(count==0) return null;
		else if(count==1) return new Step(movedPositions.get(0),null);
		else {
			if(count%2==0){
				return new Step(movedPositions.get(count-1),null);
			}else {
				return new Step(movedPositions.get(count-1),movedPositions.get(count-2));
			}
		}
	}
	public int size(){
		return this.movedPositions.size(); 
	}
	/**
	 * 获取第index个点，index从1开始
	 * @param index
	 * @return
	 */
	public Position getPositionOf(int index){
		return this.movedPositions.get(index-1);
	}
	/**
	 * 获取当前棋谱下，该那方走棋
	 * @return
	 */
	public int getWhoShouldPlay(){
		int size=this.movedPositions.size();
		if(((size % 4) == 1) || ((size % 4) == 2)) return Position.Color.WHITE;
		else return Position.Color.BLACK;
	}
	/**
	 * 获取第index个点的颜色
	 * @param index index从1开始
	 * @return
	 */
	public int getColorIndexOf(int index){
		if(((index % 4) == 2) || ((index % 4) == 3)) return Position.Color.WHITE;
		return Position.Color.BLACK;
	}
	/**
	 * 获取点的位置，位置表示从1开始
	 * 不存在返回0
	 * @param p
	 * @return
	 */
	public int getIndexOf(Position p){
		return this.movedPositions.indexOf(p)+1;
	}
	
	
	public Manual() {
		super();
	}
	
	public Manual(Manual m) {
		super();
		this.movedPositions=m.movedPositions;
	}
	
	@Override
	public String toString() {
		return "Manual [movedPositions=" + movedPositions + "]";
	}
	/**
	 * 将棋盘转换成字符串
	 * @return
	 */
	public String toChars(){
		StringBuilder sb=new StringBuilder();
		for(Position pos:this.movedPositions){
			sb.append(pos.toChars());
		}
		String str=sb.toString();
		if(str.equals("")){
			return EMPTYMANUAL;
		}
		return sb.toString();
	}
	/**
	 * 将棋谱转换为separator分割开来的字符串
	 * @param separator
	 * @return
	 */
	public String toCharsSeparation(char separator){
		if(this.movedPositions.size()==0){
			return EMPTYMANUAL;
		}
		if(this.movedPositions.size()==1){
			return this.toChars();
		}
		else{
			StringBuilder sb=new StringBuilder();
			sb.append(movedPositions.get(0).toChars()+separator);
			for(int i=2;i<this.movedPositions.size();i=i+2){
				sb.append(new Step(movedPositions.get(i-1),movedPositions.get(i)).toChars()+separator);
			}
			String value=sb.toString();
			return value.substring(0, value.length()-1);
		}
	}
	@Override
	public boolean equals(Object o) {
		Manual m=(Manual)o;
		if(this.movedPositions.size()!=m.movedPositions.size())return false;
		
		if(m.movedPositions.size()>0){
			//比较第1个子
			if(!(m.movedPositions.get(0).equals(this.movedPositions.get(0))))
				return false;
			//如果最后1步只下一子时，直接比较最后一个子
			if(m.movedPositions.size()%2==0 )
			{
				if(!m.movedPositions.get(m.movedPositions.size()-1).equals(
						this.movedPositions.get(this.movedPositions.size()-1)))
				{
					return false;
				}
			}
		}
		Step step1,step2;
		for(int i=1;i+1<m.movedPositions.size();i=i+2){
			step1=new Step(this.movedPositions.get(i),this.movedPositions.get(i+1));
			step2=new Step(m.movedPositions.get(i),m.movedPositions.get(i+1));
			if(!step1.equals(step2)){
				return false;
			}
		}
		
		return true;
	}
	@Override
	public int hashCode() {
		return 1;
	}
}
