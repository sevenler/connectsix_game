package hong.connectSix.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;


/**
 * ���� �� 
 * @author Hong
 *
 */
public class Board {
	
	public static Board _instance=new Board();
	
	public static Board _instanceForEngine=new Board();
	
	public static Board getInstanceForEngine(){
		return _instanceForEngine;
	}
	
	public static Board getInstance(){
		return _instance;
	}
	public void reset(){
		manual=new Manual();
		chessTypeRecord=new ChessTypeRecord();
		zobrist=new Zobrist();
		makeMove=new MakeMove();
		init();
	}
	
	
	/**
	 * ��ȡ��index��·��index��1��ʼ
	 * @param index
	 * @return
	 */
	public Road getRoads(int index) {
		return roads[index-1];
	}
	/**
	 * ��ȡ�����ڵ����е�·
	 * @param pos
	 * @return
	 */
	public ArrayList<Road> toRoads(Position pos) {
		return toRoads[pos.x-1][pos.y-1];
	}
	/**
	 * ��ȡ·��6�ĵ�
	 * @param road
	 * @return
	 */
	public ArrayList<Position> toPositions(Road road) {
		return toPositions[road.index-1];
	}
	
	@Override
	protected Board clone() throws CloneNotSupportedException {
		Board board=new Board();
		
		for(int i=0;i<19;i++){
			for(int j=0;j<19;j++){
				board.positions[i][j]=this.positions[i][j].clone();
			}
		}
		for(int i=0;i<924;i++){
			board.roads[i]=this.roads[i].clone();
		}
		board.manual=this.manual.clone();
		board.chessTypeRecord=this.chessTypeRecord.clone();
		board.zobrist=this.zobrist.clone();
		return board;
	}
	/**
	 * ע�⣺x��y��1��ʼ
	 * @param x
	 * @param y
	 * @return
	 */
	public Position getPosition(int x,int y) {
		return positions[x-1][y-1];
	}
	
	public Position getPosition(Position pos){
		if(pos==null) return null;
		return positions[pos.x-1][pos.y-1];
	}
	/**
	 * �ҵ������϶�Ӧ���߲�
	 * @param step
	 * @return
	 */
	public Step getStep(Step step){
		return new Step(getPosition(step.one),getPosition(step.another));
	}
	
	public boolean compare(Object o) throws Exception {
		Board board=(Board)o;
		//�ж����еĵ��Ƿ���ͬ   
		for(int i=0;i<19;i++){
			for(int j=0;j<19;j++){
				if(!board.positions[i][j].equals(this.positions[i][j])){
					throw new Exception("not the same (positions):"+board.positions[i][j]+"(compare):"+this.positions[i][j]+"(this)");
				}
			}
		}
		for(int i=0;i<924;i++){
			if(!board.roads[i].equals(this.roads[i])){
				throw new Exception("not the same (roads):"+board.roads[i]+"\n(compare):"+this.roads[i]+"(this)");
			}
		}
		if(!board.zobrist.equals(this.zobrist)){
			throw new Exception("not the same (zobrist):"+board.zobrist+"(compare):"+this.zobrist+"(this)");
		}
		if(!board.manual.equals(this.manual)){
			throw new Exception("not the same (manual):"+board.manual+"(compare):"+this.manual+"(this)");
		}
		if(!board.chessTypeRecord.equals(this.chessTypeRecord)){
			throw new Exception("not the same (chessTypeRecord):"+board.chessTypeRecord+"\n(compare):"+this.chessTypeRecord+"(this)");
		}
		
		return true;
	}
	
	/**
	 * ��ת��·����ע�⣺ʹ��ʱ���±��0��ʼ
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Road>[][] toRoads=new ArrayList [19][19];
	/**
	 * ·ת��������ע�⣺ʹ��ʱ���±��0��ʼ
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Position>[] toPositions=new ArrayList [924];
	
	
	
	/**
	 * 19*19����
	 */
	private Position [][] positions=new Position[19][19];
	
	/**
	 * ��924��·
	 */
	private Road [] roads = new Road[924];
	/**
	 * ���ף�ʹ��ջ�洢����������
	 */
	public Manual manual=new Manual();
	/**
	 * ���μ�¼
	 */
	public ChessTypeRecord chessTypeRecord=new ChessTypeRecord();
	
	/**
	 * ����zobristʵ��
	 */
	public Zobrist zobrist=new Zobrist();
	
	public  MakeMove makeMove=new MakeMove();
	
	
	private Board(){
		super();
		init();
	}
	
	private void init(){
		//��ʼ��19*19����
		for(int i=0;i<19;i++){
			for(int j=0;j<19;j++){
				positions[i][j]=new Position(i+1,j+1);
			}
		}
		for(int i=0;i<924;i++){
			roads[i]=new Road(i+1);
		}
		//��ʼ����·��ϵ��
		for(int i=0;i<19;i++){
			for(int j=0;j<19;j++){
				toRoads[i][j]=new ArrayList<Road>();
			}
		}
		for(int i=0;i<924;i++){
			toPositions[i]=new ArrayList<Position>();
		}
		//��·��ϵ����ʼ��ֵ
		//��ʼ��ˮƽ����
		for (int i = 1; i <= 14; i++)   //ÿ��·��6����
        {
            for (int j = 1; j <= 19; j++)
            {
                int roadIndex = (i-1) * 19 + j;//1 - 266
                for (int k = 0; k < 6; k++)
                {
                    int X = i + k;
                    int Y = j;
                    toPositions[roadIndex-1].add(positions[X-1][Y-1]);
                    toRoads[X-1][Y-1].add(roads[roadIndex-1]);
                }
                //Ϊ·���׵㡢β��ͷ���
                roads[roadIndex-1].first=positions[i-1][j-1];
                roads[roadIndex-1].last=positions[i+4][j-1];
                roads[roadIndex-1].direction=Road.Direction.HORIZONTAL;
            }
        }
		//��ʼ����ֱ����
		for (int j = 1; j <= 14; j++)
        {
            for (int i = 1; i <= 19; i++)
            {
                int roadIndex = 266 + i + (j - 1) * 19;//267 - 531
                for (int k = 0; k < 6; k++)
                {
                    int X = i;
                    int Y = j + k;
                    toPositions[roadIndex-1].add(positions[X-1][Y-1]);
                    toRoads[X-1][Y-1].add(roads[roadIndex-1]);
                }
              //Ϊ·���׵㡢β��ͷ���
                roads[roadIndex-1].first=positions[i-1][j-1];
                roads[roadIndex-1].last=positions[i-1][j+4];
                roads[roadIndex-1].direction=Road.Direction.VERTICAL;
            }
        }
		//��������б
		for (int i = 1; i <= 14; i++)
        {
            for (int j = 1; j <= 14; j++)
            {
                int roadIndex = 532 + (i - 1) * 14 + j;//532 - 728
                for (int k = 0; k < 6; k++)
                {
                    int X = i + k;
                    int Y = j + k;
                    toPositions[roadIndex-1].add(positions[X-1][Y-1]);
                    toRoads[X-1][Y-1].add(roads[roadIndex-1]);
                }
              //Ϊ·���׵㡢β��ͷ���
                roads[roadIndex-1].first=positions[i-1][j-1];
                roads[roadIndex-1].last=positions[i+4][j+4];
                roads[roadIndex-1].direction=Road.Direction.LEFTUPTORIGHTDOWN;
            }
        }
		//��������б
		for (int i = 1; i <= 14; i++)
        {
            for (int j = 1; j <= 14; j++)
            {
                int roadIndex = 728 + (i - 1) * 14 + j;//729 - 924
                for (int k = 0; k < 6; k++)
                {
                    int X = i - k + 5;
                    int Y = j + k;
                    toPositions[roadIndex-1].add(positions[X-1][Y-1]);
                    toRoads[X-1][Y-1].add(roads[roadIndex-1]);
                }
              //Ϊ·���׵㡢β��ͷ���
                roads[roadIndex-1].first=positions[i+4][j-1];
                roads[roadIndex-1].last=positions[i-1][j+4];
                roads[roadIndex-1].direction=Road.Direction.RIGHTUPTOLEFTDOWN;
            }
        }
	}
	
	/**
	 * ���μ�¼�ࣺ��¼��ǰ������Ч�㡢��Ч·��˫�����ͣ���ά��������־�����ڳ������ӻָ�����
	 * @author Hong
	 *
	 */
	public class ChessTypeRecord{
		
		@Override
		public boolean equals(Object o) {
			ChessTypeRecord chess=(ChessTypeRecord)o;
			if(this.availablePos.size()!=chess.availablePos.size()){
				return false;
			}
			for(Position pos : this.availablePos){
				if(!chess.availablePos.contains(pos)) return false;
			}
			if(this.availableRoad.size()!=chess.availableRoad.size())return false;
			for(Road road: this.availableRoad){
				if(!chess.availableRoad.contains(road)) return false;
			}
			for(int i=0;i<2;i++){
				for(int j=0;j<6;j++){
					if(chess.chessType[i][j]!=this.chessType[i][j])return false;
				}
			}
			return true;
		}

		@Override
		protected ChessTypeRecord clone() throws CloneNotSupportedException {
			ChessTypeRecord chess=new ChessTypeRecord();
			for(Position pos : this.availablePos){
				chess.availablePos.add(pos);
			}
			for(Road road: this.availableRoad){
				chess.availableRoad.add(road);
			}
			for(int i=0;i<2;i++){
				for(int j=0;j<6;j++){
					chess.chessType[i][j]=this.chessType[i][j];
				}
			}
			for(HashMap<Position,Boolean> log:this.changedPosLog){
				chess.changedPosLog.add(log);
			}
			for(HashMap<Road,Boolean> log:this.changedRoadLog){
				chess.changedRoadLog.add(log);
			}
			for(int[][] hisChess:this.historyChessTypes){
				chess.historyChessTypes.add(hisChess);
			}
			return chess;
		}

		/**
		 * �ȽϺ����������������׳�����ȵ������쳣
		 * ��Ҫ���ڲ������α仯�Ƿ����
		 * @param o
		 * @throws Exception
		 */
		public void compare(Object o) throws Exception {
			ChessTypeRecord ctr=(ChessTypeRecord)o;
			for (Position pos : this.availablePos) {
				if(!ctr.availablePos.contains(pos)){
					throw new Exception("Excetion with: postion "+pos+" in this,but not in compare!");
				}
			}
			for(Road road : this.availableRoad){
				if(!ctr.availableRoad.contains(road)){
					throw new Exception("Excetion with: road "+road+" in this,but not in compare!");
				}
			}
			for (int i=0;i<chessType.length;i++) {
				for(int j=0;j<chessType[i].length;j++){
					if(chessType[i][j]!=ctr.chessType[i][j]){
						throw new Exception("Excetion with:"+"value of chesstype in (i="+i+",j="+j+"),"
								+chessType[i][j]+"(this)!="+ctr.chessType[i][j]+"(compare)");
					}
				}
			}
		}
		
		@Override
		public String toString() {
			int [][] his={{-1,-1,-1,-1,-1,-1},{-1,-1,-1,-1,-1,-1}};
			if(this.historyChessTypes.size()>0)
				his=historyChessTypes.get(historyChessTypes.size()-1);
			String chessTypeStr="";
			for (int i=0;i<his.length;i++) {
				for(int j=0;j<his[i].length;j++){
					chessTypeStr+=his[i][j]+",";
					
				}
				chessTypeStr+=" | ";
			}
			String chess="";
			for (int i=0;i<chessType.length;i++) {
				for(int j=0;j<chessType[i].length;j++){
					chess+=chessType[i][j]+",";
				}
				chess+=" | ";
			}
			return "--ChessTypeRecord [availablePos=" + availablePos
					+ ", availableRoad=" + availableRoad + ", chessType="
					+ chess + "\n changedPosLog="
					+ changedPosLog + ", changedRoadLog=" + changedRoadLog
					+ ", historyChessTypes=" + chessTypeStr + "]";
		}

		/**
		 * ��Ч�㣺������ɾ����Ƶ�����Ҳ����ظ���ʹ��HashSet���д洢�����Ч��
		 * ע�⣺Hashmap�ĳ���һ��Ҫʹ��2��N�η���������ԭ�����������hashmap����
		 */
		public  HashSet<Position> availablePos=new HashSet<Position>(128);
		/**
		 * ��Ч·
		 */
		private HashSet<Road> availableRoad=new HashSet<Road>(512);
		/**
		 * ����˫����������
        * �ֱ�洢˫����1-��6���͵����� 1:�� 0����
        * ����chessType[1,2]Ϊ�ڳ�3����
		 */
		private int [][] chessType={{0,0,0,0,0,0},
									{0,0,0,0,0,0}};
		
		/**
		 * ��ȡcolorɫtype���ε����� ���� 1<=type<=6
		 * @param color
		 * @param type
		 * @return
		 */
		public int getChessType(int color,int type) {
			color=color==Position.Color.BLACK?1:0;
			return chessType[color][type-1];
		}
		public int [][] chessTypeClone(){
			int [][] chessType=new int[2][6];
			for (int i=0;i<chessType.length;i++) {
				for(int j=0;j<chessType[i].length;j++){
					chessType[i][j]=this.chessType[i][j];
				}
			}
			return chessType;
		}
		/**
		 * �ı���Ч��
		 * @param po
		 * @param isAdd
		 */
		public void changeAvailablePos(Position po,boolean isAdd){
			if(isAdd){
				this.availablePos.add(po);
			}else{
				this.availablePos.remove(po);
			}
		}
		/**
		 * �ı���Ч·
		 * @param road
		 * @param isAdd
		 */
		public void changeAvailableRoad(Road road,boolean isAdd){
			if(isAdd){
				this.availableRoad.add(road);
			}else{
				this.availableRoad.remove(road);
			}
		}
		/**
		 * �ı���������
		 * @param color
		 * 
		 * 
		 * @param chessType
		 * @param isAdd
		 */
		public void changeChessType(int color,int chessType,boolean isAdd){
			color=color==Position.Color.BLACK?1:0;
			if(isAdd){
				this.chessType[color][chessType]++;
			}else{
				this.chessType[color][chessType]--;
			}
		}
		
		/**
		 * �ж���Ч���Ƿ����po
		 * @param po
		 * @return
		 */
		public boolean contains(Position po){
			return this.availablePos.contains(po);
		}
		/**
		 * �ж���Ч·�Ƿ����road
		 * @param road
		 * @return
		 */
		public boolean contains(Road road){
			return this.availableRoad.contains(road);
		}
		
		/** 
		 * ������ɾ����Ƶ����Ȼ���ڼ�¼��־��ʱ�򣬻�����ظ��Ե��жϣ����ѡ��ʹ��LinkedList
		 * ��Ч��仯��־  ,��־ <(10,10),false>��ʾɾ����Ч��(10,10)
		 */
		public  List<HashMap<Position,Boolean>> changedPosLog=new LinkedList<HashMap<Position,Boolean>>();
		/**
		 * ��Ч·�仯��־����־ <(215),false>��ʾɾ����Ч·(215)
		 */
		private List<HashMap<Road,Boolean>> changedRoadLog=new LinkedList<HashMap<Road,Boolean>>();
		/**
		 * ��ʷ������־
		 */
		private List<int[][]> historyChessTypes=new LinkedList<int[][]>();
		
		
		public void changePosLog(HashMap<Position,Boolean> posChange,HashMap<Road,Boolean> roadChange,int [][] historyChessType){
			this.changedPosLog.add(posChange);
			this.changedRoadLog.add(roadChange);
			this.historyChessTypes.add(historyChessType);
		}
		
		public void unchangePosLog(){
			//ͨ����־�ָ���Ч��
			HashMap<Position,Boolean> posChange=this.changedPosLog.get(this.changedPosLog.size()-1);
			for (Entry<Position,Boolean> pos : posChange.entrySet()) {
				if(pos.getValue()){
					//�����true����ɾ������Ч��
					this.availablePos.remove(pos.getKey());
				}else{
					//�����false������Ӹ���Ч��
					this.availablePos.add(pos.getKey());
				}
			}
			//ͨ����־�ָ���Ч·
			HashMap<Road,Boolean> roadChange=this.changedRoadLog.get(this.changedRoadLog.size()-1);
			for (Entry<Road,Boolean> road : roadChange.entrySet()) {
				if(road.getValue()){
					//�����true����ɾ������Ч·
					this.availableRoad.remove(road.getKey());
				}else{
					//�����false������Ӹ���Ч·
					this.availableRoad.add(road.getKey());
				}
			}
			//ͨ����־�ָ�����
			this.chessType=this.historyChessTypes.get(this.historyChessTypes.size()-1);
			//����β��־
			this.changedPosLog.remove(this.changedPosLog.size()-1);
			this.changedRoadLog.remove(this.changedRoadLog.size()-1);
			this.historyChessTypes.remove(this.historyChessTypes.size()-1);
		}
	}
	
	/**
	 * ����zobrist�ࣺ���ڱ�ʾ��ǰ�����ֵ����ͬ�ľ������ڲ�ͬ��zobristֵ
	 * @author Hong
	 *
	 */
	public class Zobrist{
		
		public int getZobristKey() {
			return zobristKey;
		}

		public long getZobristKeyCheck() {
			return zobristKeyCheck;
		}

		@Override
		public String toString() {
			return "Zobrist [zobristKey=" + zobristKey + ", zobristKeyCheck="
					+ zobristKeyCheck + "]";
		}

		@Override
		public boolean equals(Object o) {
			Zobrist zo=(Zobrist)o;
			return (zo.zobristKey==this.zobristKey) && (zo.zobristKeyCheck==this.zobristKeyCheck);
		}

		@Override
		protected Zobrist clone() throws CloneNotSupportedException {
			Zobrist zo=new Zobrist();
			zo.zobristPlayer=this.zobristPlayer;
			zo.zobristPlayerCheck=this.zobristPlayerCheck;
			for(int i=0;i<2;i++){
				for(int j=0;j<19;j++){
					for(int k=0;k<19;k++){
						zo.zobristPlayerTable[i][j][k]=this.zobristPlayerTable[i][j][k];
						zo.zobristPlayerCheckTable[i][j][k]=this.zobristPlayerCheckTable[i][j][k];
					}
				}
			}
			zo.zobristKey=this.zobristKey;
			zo.zobristKeyCheck=this.zobristKeyCheck;
			return zo;
		}
		private int zobristPlayer=0;//32λzobrist���巽λ��
		private long zobristPlayerCheck=0;//64λzobrist���巽λ��У��ֵ
		private int[][][] zobristPlayerTable=new int[2][19][19];//32λ����λ�ã����̹�19*19���㣬˫����19*19*2������zobrist���巽λ��,��ɫ��1 ��ɫ��0
		private long[][][] zobristPlayerCheckTable=new long[2][19][19];//64λ����λ�ã����̹�19*19���㣬˫����19*19*2������zobrist���巽λ��У��ֵ
		
		private int zobristKey=0;//����32λzobrist��ֵ
		private long zobristKeyCheck=0;//����64λzobristУ��ֵ
		
		public Zobrist(){
			//����ʷ���ֵ�洢������ÿ���²��������ʱ���ж����Ƿ����,max����Ϊ2*19*19*8+2*19*19*4=8664
			HashSet<Integer> list32=new HashSet<Integer>(1024);
			HashSet<Long> list64=new HashSet<Long>(1024);
			//ʵ�������������ʵ��
			Random random=new Random(System.currentTimeMillis());
			Integer buf32=random.nextInt();
			Long buf64=random.nextLong();
			//ʹ���������ʼ��zobristPlayer��zobristPlayerCheck
			this.zobristKey=buf32;
			this.zobristKeyCheck=buf64;
			list32.add(buf32);
			list64.add(buf64);
			//ʹ���������ʼ��zobristPlayerTable��zobristPlayerCheckTable
			for(int i=0;i<2;i++){
				for(int j=0;j<19;j++){
					for(int k=0;k<19;k++){
						//�ж�������Ƿ��ظ������ظ�����ӵ�table����ȥ���ظ������²���
						while(true){
							buf32=random.nextInt();
							if(!list32.contains(buf32)){
								this.zobristPlayerTable[i][j][k]=buf32;
								list32.add(buf32);
								break;
							}
						}
						while(true){
							buf64=random.nextLong();
							if(!list64.contains(buf64)){
								this.zobristPlayerCheckTable[i][j][k]=buf64;
								list64.add(buf64);
								break;
							}
						}	
					}
				}
			}
			System.gc();
			//ʹ��0��ʼ��zobristKey��zobristKeyCheck
			zobristKey=0;
			zobristKeyCheck=0;
		}
		
		/**
		 * ���ӣ����¼������zobristֵ
		 * @param po
		 * @param color
		 */
		public void move(Position po,int color){
			color=color==Position.Color.BLACK?1:0;
			//��ǰ�����zobristֵ���㷽����zobrist��ֵ=zobristԭֵ ^ zobrist��λ��  ^ po��color��ɫ�ķ�λ��ֵ
			zobristKey=zobristKey^this.zobristPlayer^this.zobristPlayerTable[color][po.x-1][po.y-1];
			zobristKeyCheck=zobristKeyCheck^this.zobristPlayerCheck^this.zobristPlayerCheckTable[color][po.x-1][po.y-1];
		}
		/**
		 * ȡ�����ӣ����¼������zobristֵ
		 * @param po
		 * @param color
		 */
		public void unmove(Position po,int color){
			color=color==Position.Color.BLACK?1:0;
			//�ٴν���ͬ������������ɻָ�ԭʼzobristֵ
			zobristKey=zobristKey^this.zobristPlayer^this.zobristPlayerTable[color][po.x-1][po.y-1];
			zobristKeyCheck=zobristKeyCheck^this.zobristPlayerCheck^this.zobristPlayerCheckTable[color][po.x-1][po.y-1];
		}
		
		
	}
	
	/**
	 * �����ࣺ�Ծ���������ӻ�ȡ�����ӣ����ı����·�����״̬������¼�仯��־��
	 * @author Hong
	 *
	 */
	public class MakeMove{
		/**
		 * ����
		 * @param po 
		 * @param color
		 * @return ��ʾ��Ϸ�Ƿ����
		 */
		public boolean makeMovePosition(Position po,int color){
			//��ȡ�����ϵ�po��
			po=Board.this.getPosition(po);
			if(manual.contains(po)){
				//ֱ���׳��߲�������쳣
				//throw new IllegalArgumentException("this postion was been in manual!");
				return false;
			}
			boolean isWin=false;//��¼��Ϸ�Ƿ����
		    int	_color=color==Position.Color.BLACK?1:0;
			//��¼�仯����Ч�����Ч·
			HashMap<Position,Boolean> posChange=new HashMap<Position,Boolean>(50);
			HashMap<Road,Boolean> roadChange=new HashMap<Road,Boolean>(50);
			int[][] historyChessType=chessTypeRecord.chessTypeClone();//��ȡ��ʷ����ֵ
			
			//��������:��������+zobrist����+����õ�����
			manual.push(po);
			zobrist.move(po, color);
			positions[po.x-1][po.y-1].color=color;
			
			//�߳�����Ч��
			//һ��Ҫ�ж��Ƿ�����Ч�㣬���������Ч���򲻼�¼�仯��־
			if(chessTypeRecord.availablePos.contains(po)){
				chessTypeRecord.changeAvailablePos(po, false);
				posChange.put(po, false);
			}
			
			//�ı�õ����ڵ�·��״̬
			List<Road> roads=toRoads[po.x-1][po.y-1];
			for (Road road : roads) {
				int hisCount=road.count++;//����ӵ���ӵ�����������¼��ʷ����
				if(road.isDrop)continue;//��������·������Ϣ����
				
				//��·
				if(road.color==Position.Color.EMPTY){
					//1.���α仯����1·++
					chessTypeRecord.chessType[_color][0]++;
					//2.����·����ɫ
					road.color=color;
					//3.����·������״̬��[����]
					//4.·��ֵ
					road.score=getDynamicValueOfRoad(road, po, true);
					//5.������Ч·
					chessTypeRecord.changeAvailableRoad(road, true);
					//��¼��Ч·��־
					roadChange.put(road, true);
					//6.����µ���Ч��
					List<Position> list=toPositions[road.index-1];
					//ѭ����·�����еĵ�
					for(Position pos:list){
						if(this.isNearBy(pos, po, 2) && pos.color==Position.Color.EMPTY && !chessTypeRecord.contains(pos)){
							//�ҵ��������ӵ�2���������ڵĿյ㣬���Ϊ�µ���Ч��
							chessTypeRecord.changeAvailablePos(pos, true);
							//��¼��Ч��仯��־
							posChange.put(pos, true);
						}
					}
				}
				
				//ͬɫ·
				else if(road.color==color){
					//�ж���Ϸ�Ƿ����
					if(road.count>=6){
						isWin=true;
					}
					//1.���α仯��ԭ��������--������������++
					chessTypeRecord.chessType[_color][hisCount-1]--;
					chessTypeRecord.chessType[_color][hisCount]++;
					//2.����·��ɫ [����]
                    //3.����·������״̬ [����]
					
                    //4.·��ֵ
					road.score=getDynamicValueOfRoad(road, po, true);
					//5.������Ч·[����]
					//6.����µ���Ч��
					List<Position> list=toPositions[road.index-1];
					//ѭ����·�����еĵ�
					for(Position pos:list){
						if(this.isNearBy(pos, po, 2) && pos.color==Position.Color.EMPTY && !chessTypeRecord.contains(pos)){
							//�ҵ��������ӵ�2���������ڵĿյ㣬���Ϊ�µ���Ч��
							chessTypeRecord.changeAvailablePos(pos, true);
							//��¼��Ч��仯��־
							posChange.put(pos, true);
						}
					}
				}
				
				//��ɫ·��������
				else {
					//1.���α仯��ԭ��������--
					chessTypeRecord.chessType[_color][hisCount-1]--;
					//����ʱ��¼����ǰ��ӵ��������
					road.countWhenDrop=hisCount;
					//2.����·��ɫ [�ı���Ҳ�����壬�Ǿ͸ɴ಻Ҫ����]
					//3.����·������״̬ [����]
					road.isDrop=true;
					//4.·��ֵ,
                    //ע�⣺��·���������ֵ��Ȼ��������ǰ�ķ�ֵ��
                    //���������ֵ��������ģ���ʹ�����ֵʱ��Ҫ�������״̬һ��ʹ��
					//Ȼ�������ı�����Ϊ�˻ָ�����״̬������ʹ����ֵ
					//5.��Ч�仯
					chessTypeRecord.changeAvailableRoad(road, false);
					roadChange.put(road, false);
					//6.��Ч��仯[����]
				}
			}
			
			//������Ч�㣬��Ч·�仯��־����ʷ����
			chessTypeRecord.changePosLog(posChange, roadChange, historyChessType);
			
			return isWin;
		}
		/**
		 * ȡ������
		 */
		public Position unmakeMovePosition(){
			//����ȡ������
			Position pos=manual.pull();
			//����ȡ������+zobristȡ������
			zobrist.unmove(pos, pos.color);
			pos.color=Position.Color.EMPTY;
			//ͨ��������־�ָ�����
			chessTypeRecord.unchangePosLog();
			List<Road> list=toRoads[pos.x-1][pos.y-1];
			for(Road road:list){
				road.count--;//�ָ�ȡ�������ڵ�·��ӵ���ӵ�����
				if(road.count<=0){
					//�ָ���·��ɫ�Լ�countWhenDrop
					road.color=Position.Color.EMPTY;
					road.countWhenDrop=-1;
				}
				//�ָ���ֵ
				road.score=getDynamicValueOfRoad(road, pos, false);
				if(road.isDrop && road.count==road.countWhenDrop){
					//�����ȡ�����Ӻ�·��ӵ������������ָ�������ǰ��״̬����ָ�·��Ч
                    //���ʱ���·����Ч״̬ʹ��·�ķ�ֵ���Ϳ��Խ��ж�ȡ�ˡ�
					road.isDrop=false;
					road.countWhenDrop=-1;
				}
			}
			return pos;
		}
		
		/**
		 * �߲�
		 * @param step
		 * @param color
		 * @return ��ʾ��Ϸ�Ƿ����
		 */
		public boolean makeMoveStep(Step step,int color){
			return ((step.one==null?false:makeMovePosition(step.one, color))
					|| (step.another==null?false:makeMovePosition(step.another, color)));
		}
		/**
		 * ȡ���߲�
		 */
		public Step unmakeMoveStep(){
			Step step=new Step();
			step.one=unmakeMovePosition();
			step.another=unmakeMovePosition();
			return step;
		}
		
		/**
		 * ��ȡ��pos��road�ϵĶ�̬��ֵ
		 * @param road ע�⣺road��countֵ�Ѿ��ı�
		 * @param pos
		 * @param isMakeMove
		 * @return
		 */
		public int getDynamicValueOfRoad(Road road,Position pos,boolean isMakeMove){
			int count=road.count;
			if(isMakeMove){
				//���ӣ���ԭ·��ֵ/ԭ���η�ֵ+��������·�ϵķ�ֵ��*�����η�ֵ
				return (road.score/ConstValue.EachChess[count-1] + getStaticValueInRoad(road,pos))
					*ConstValue.EachChess[count];
			}else{
				if(!road.isDrop){
					//ȡ������ǰδ��������ԭ·��ֵ/ԭ���η�ֵ-ȡ������·�ϵķ�ֵ��*�����η�ֵ
					return (road.score/ConstValue.EachChess[count+1] - getStaticValueInRoad(road,pos))
					*ConstValue.EachChess[count];
				}else return road.score;//���������·���򷵻�·��ԭֵ
			}
		}
		
		/**
		 * ��ȡ��pos��road�ϵľ�̬��ֵ
		 * @param road
		 * @param pos
		 * @return
		 */
		public int getStaticValueInRoad(Road road,Position pos){
			int indexOfRoad;
			//������ڸ�·�ϵ�λ��
			if(road.index>=0&&road.index<266){//ˮƽ
				indexOfRoad=(pos.y+19*(pos.x-1)-road.index)/19;
			}else if(road.index>=266&&road.index<532){//��ֱ
				indexOfRoad=(266+pos.x+19*(pos.y-1)-road.index)/19;
			}else if(road.index>=532&&road.index<728){//����������б
				indexOfRoad=(532+pos.y+14*(pos.x-1)-road.index)/15;
			}else {//����������б
				indexOfRoad=(road.index-728-pos.y-14*(pos.x-1)+70)/13;
			}
			//ͨ��λ��ȥ��ȡ��ֵ
			switch(indexOfRoad){
				case 0:return ConstValue.EachPostionInRoad[0];
				case 1:return ConstValue.EachPostionInRoad[1];
				case 2:return ConstValue.EachPostionInRoad[2];
				case 3:return ConstValue.EachPostionInRoad[3];
				case 4:return ConstValue.EachPostionInRoad[4];
				case 5:return ConstValue.EachPostionInRoad[5];
			}
			return 0;
		}
		
		/**
		 * �ж��������Ƿ���length������
		 * @param one
		 * @param another
		 * @param length
		 * @return
		 */
		public boolean isNearBy(Position one,Position another,int length){
			if(one.x>=another.x && one.y>=another.y){
				if((one.x-another.x)<=length && (one.y-another.y)<=length){
					return true;
				}
			}else if(one.x<=another.x && one.y>=another.y){
				if((another.x-one.x)<=length && (one.y-another.y)<=length){
					return true;
				}
			}else if(one.x>=another.x && one.y<=another.y){
				if((one.x-another.x)<=length && (another.y-one.y)<=length){
					return true;
				}
			}else{
				if((another.x-one.x)<=length && (another.y-one.y)<=length){
					return true;
				}
			}
			return false;
		}
	}
	
	private interface ConstValue{
		/**
		 * 7�ֻ������͵�ֵ:��·����1��......����6
		 */
		public static final int[] EachChess={1, 2, 4, 8, 16, 32, 64};
		/**
		 * ·��6������ڸ�·�ķ�ֵ
		 */
		public static final int[] EachPostionInRoad={2, 4, 8, 8, 4, 2};
	}
}

	
