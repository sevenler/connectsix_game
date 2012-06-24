package hong.connectSix.model;

public class Step {
	public Position one=null;
	public Position another=null;
	
	/**
	 * 将字符串装换成Step
	 * @param s字符串必须是AABB这样的字符，而不能是1122
	 * @return
	 */
	public static Step toInt(String s){
		Step step=null;
		try{
			if(s.length()==2){
				step=new Step(Position.toInt(s.substring(0, 2)),null);
			}else{
				step=new Step(Position.toInt(s.substring(0, 2)),Position.toInt(s.substring(2, 4)));
			}
		}catch(Exception e){
			throw new NumberFormatException("\""+s+"\" cannot fromat to Step: "+e.getMessage());
		}
		return step;
	}
	public String toChars(){
		return one==null?"":one.toChars() +
				(another==null?"":another.toChars());
	}
	public void set(Position one, Position another) {
		this.one = one;
		this.another = another;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null)return false;
		Step step=(Step)o;
		return (this.one==null?(step.one==null?true:false):this.one.equals(step.one) && 
				(this.another==null?(step.another==null?true:false):this.another.equals(step.another)))
				||(this.one==null?(step.another==null?true:false):this.one.equals(step.another) &&
						(this.another==null?(step.one==null?true:false):this.another.equals(step.one)));
	}

	@Override
	public int hashCode() {
		return  one.hashCode()*another.hashCode();
	}
	
	@Override
	public String toString() {
		return "Step [one=" + one + ", another=" + another + "]";
	}
	
	public Step(Position one, Position another) {
		super();
		this.one = one;
		this.another = another;
	}
	public Step() {
		super();
	}
	
	
	
	
}
