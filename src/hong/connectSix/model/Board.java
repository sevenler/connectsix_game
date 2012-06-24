package hong.connectSix.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;


/**
 * 棋盘 类 
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
	 * 获取第index条路，index从1开始
	 * @param index
	 * @return
	 */
	public Road getRoads(int index) {
		return roads[index-1];
	}
	/**
	 * 获取点所在的所有的路
	 * @param pos
	 * @return
	 */
	public ArrayList<Road> toRoads(Position pos) {
		return toRoads[pos.x-1][pos.y-1];
	}
	/**
	 * 获取路上6的点
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
	 * 注意：x，y从1开始
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
	 * 找到棋盘上对应的走步
	 * @param step
	 * @return
	 */
	public Step getStep(Step step){
		return new Step(getPosition(step.one),getPosition(step.another));
	}
	
	public boolean compare(Object o) throws Exception {
		Board board=(Board)o;
		//判断所有的点是否相同   
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
	 * 点转换路链，注意：使用时，下标从0开始
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Road>[][] toRoads=new ArrayList [19][19];
	/**
	 * 路转换点链，注意：使用时，下标从0开始
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Position>[] toPositions=new ArrayList [924];
	
	
	
	/**
	 * 19*19个点
	 */
	private Position [][] positions=new Position[19][19];
	
	/**
	 * 共924条路
	 */
	private Road [] roads = new Road[924];
	/**
	 * 棋谱：使用栈存储棋盘已落子
	 */
	public Manual manual=new Manual();
	/**
	 * 棋形记录
	 */
	public ChessTypeRecord chessTypeRecord=new ChessTypeRecord();
	
	/**
	 * 局面zobrist实例
	 */
	public Zobrist zobrist=new Zobrist();
	
	public  MakeMove makeMove=new MakeMove();
	
	
	private Board(){
		super();
		init();
	}
	
	private void init(){
		//初始化19*19个点
		for(int i=0;i<19;i++){
			for(int j=0;j<19;j++){
				positions[i][j]=new Position(i+1,j+1);
			}
		}
		for(int i=0;i<924;i++){
			roads[i]=new Road(i+1);
		}
		//初始化点路关系链
		for(int i=0;i<19;i++){
			for(int j=0;j<19;j++){
				toRoads[i][j]=new ArrayList<Road>();
			}
		}
		for(int i=0;i<924;i++){
			toPositions[i]=new ArrayList<Position>();
		}
		//点路关系链初始化值
		//初始化水平方向
		for (int i = 1; i <= 14; i++)   //每条路有6个点
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
                //为路赋首点、尾点和方向
                roads[roadIndex-1].first=positions[i-1][j-1];
                roads[roadIndex-1].last=positions[i+4][j-1];
                roads[roadIndex-1].direction=Road.Direction.HORIZONTAL;
            }
        }
		//初始化竖直方向
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
              //为路赋首点、尾点和方向
                roads[roadIndex-1].first=positions[i-1][j-1];
                roads[roadIndex-1].last=positions[i-1][j+4];
                roads[roadIndex-1].direction=Road.Direction.VERTICAL;
            }
        }
		//左上至右斜
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
              //为路赋首点、尾点和方向
                roads[roadIndex-1].first=positions[i-1][j-1];
                roads[roadIndex-1].last=positions[i+4][j+4];
                roads[roadIndex-1].direction=Road.Direction.LEFTUPTORIGHTDOWN;
            }
        }
		//右上至左斜
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
              //为路赋首点、尾点和方向
                roads[roadIndex-1].first=positions[i+4][j-1];
                roads[roadIndex-1].last=positions[i-1][j+4];
                roads[roadIndex-1].direction=Road.Direction.RIGHTUPTOLEFTDOWN;
            }
        }
	}
	
	/**
	 * 棋形记录类：记录当前局面有效点、有效路、双方棋型，并维护更改日志，用于撤销走子恢复棋形
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
		 * 比较函数，如果不相等则抛出不相等的内容异常
		 * 主要用于测试棋形变化是否相等
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
		 * 有效点：由于增删操作频繁并且不能重复，使用HashSet进行存储会提高效率
		 * 注意：Hashmap的长度一定要使用2的N次方数，具体原因可上网查阅hashmap机制
		 */
		public  HashSet<Position> availablePos=new HashSet<Position>(128);
		/**
		 * 有效路
		 */
		private HashSet<Road> availableRoad=new HashSet<Road>(512);
		/**
		 * 当局双方棋型数组
        * 分别存储双方成1-成6棋型的数量 1:黑 0：白
        * 例：chessType[1,2]为黑成3棋型
		 */
		private int [][] chessType={{0,0,0,0,0,0},
									{0,0,0,0,0,0}};
		
		/**
		 * 获取color色type棋形的数量 其中 1<=type<=6
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
		 * 改变有效点
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
		 * 改变有效路
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
		 * 改变棋形数量
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
		 * 判断有效点是否包括po
		 * @param po
		 * @return
		 */
		public boolean contains(Position po){
			return this.availablePos.contains(po);
		}
		/**
		 * 判断有效路是否包括road
		 * @param road
		 * @return
		 */
		public boolean contains(Road road){
			return this.availableRoad.contains(road);
		}
		
		/** 
		 * 由于增删操作频繁，然而在记录日志的时候，会进行重复性的判断，因此选择使用LinkedList
		 * 有效点变化日志  ,日志 <(10,10),false>表示删除有效点(10,10)
		 */
		public  List<HashMap<Position,Boolean>> changedPosLog=new LinkedList<HashMap<Position,Boolean>>();
		/**
		 * 有效路变化日志，日志 <(215),false>表示删除有效路(215)
		 */
		private List<HashMap<Road,Boolean>> changedRoadLog=new LinkedList<HashMap<Road,Boolean>>();
		/**
		 * 历史棋形日志
		 */
		private List<int[][]> historyChessTypes=new LinkedList<int[][]>();
		
		
		public void changePosLog(HashMap<Position,Boolean> posChange,HashMap<Road,Boolean> roadChange,int [][] historyChessType){
			this.changedPosLog.add(posChange);
			this.changedRoadLog.add(roadChange);
			this.historyChessTypes.add(historyChessType);
		}
		
		public void unchangePosLog(){
			//通过日志恢复有效点
			HashMap<Position,Boolean> posChange=this.changedPosLog.get(this.changedPosLog.size()-1);
			for (Entry<Position,Boolean> pos : posChange.entrySet()) {
				if(pos.getValue()){
					//如果是true，则删除该有效点
					this.availablePos.remove(pos.getKey());
				}else{
					//如果是false，则添加该有效点
					this.availablePos.add(pos.getKey());
				}
			}
			//通过日志恢复有效路
			HashMap<Road,Boolean> roadChange=this.changedRoadLog.get(this.changedRoadLog.size()-1);
			for (Entry<Road,Boolean> road : roadChange.entrySet()) {
				if(road.getValue()){
					//如果是true，则删除该有效路
					this.availableRoad.remove(road.getKey());
				}else{
					//如果是false，则添加该有效路
					this.availableRoad.add(road.getKey());
				}
			}
			//通过日志恢复棋形
			this.chessType=this.historyChessTypes.get(this.historyChessTypes.size()-1);
			//弹出尾日志
			this.changedPosLog.remove(this.changedPosLog.size()-1);
			this.changedRoadLog.remove(this.changedRoadLog.size()-1);
			this.historyChessTypes.remove(this.historyChessTypes.size()-1);
		}
	}
	
	/**
	 * 局面zobrist类：用于表示当前局面的值，不同的局面用于不同的zobrist值
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
		private int zobristPlayer=0;//32位zobrist走棋方位键
		private long zobristPlayerCheck=0;//64位zobrist走棋方位键校验值
		private int[][][] zobristPlayerTable=new int[2][19][19];//32位各个位置（棋盘共19*19个点，双方有19*19*2个）的zobrist走棋方位键,黑色：1 白色：0
		private long[][][] zobristPlayerCheckTable=new long[2][19][19];//64位各个位置（棋盘共19*19个点，双方有19*19*2个）的zobrist走棋方位键校验值
		
		private int zobristKey=0;//棋盘32位zobrist键值
		private long zobristKeyCheck=0;//棋盘64位zobrist校验值
		
		public Zobrist(){
			//将历史随机值存储起来，每次新产生随机数时，判断其是否存在,max长度为2*19*19*8+2*19*19*4=8664
			HashSet<Integer> list32=new HashSet<Integer>(1024);
			HashSet<Long> list64=new HashSet<Long>(1024);
			//实例化产生随机数实例
			Random random=new Random(System.currentTimeMillis());
			Integer buf32=random.nextInt();
			Long buf64=random.nextLong();
			//使用随机数初始化zobristPlayer和zobristPlayerCheck
			this.zobristKey=buf32;
			this.zobristKeyCheck=buf64;
			list32.add(buf32);
			list64.add(buf64);
			//使用随机数初始化zobristPlayerTable和zobristPlayerCheckTable
			for(int i=0;i<2;i++){
				for(int j=0;j<19;j++){
					for(int k=0;k<19;k++){
						//判断随机数是否重复，不重复则添加到table当中去，重复则重新产生
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
			//使用0初始化zobristKey和zobristKeyCheck
			zobristKey=0;
			zobristKeyCheck=0;
		}
		
		/**
		 * 走子：重新计算局面zobrist值
		 * @param po
		 * @param color
		 */
		public void move(Position po,int color){
			color=color==Position.Color.BLACK?1:0;
			//当前局面的zobrist值计算方法：zobrist新值=zobrist原值 ^ zobrist方位键  ^ po点color颜色的方位键值
			zobristKey=zobristKey^this.zobristPlayer^this.zobristPlayerTable[color][po.x-1][po.y-1];
			zobristKeyCheck=zobristKeyCheck^this.zobristPlayerCheck^this.zobristPlayerCheckTable[color][po.x-1][po.y-1];
		}
		/**
		 * 取消走子：重新计算局面zobrist值
		 * @param po
		 * @param color
		 */
		public void unmove(Position po,int color){
			color=color==Position.Color.BLACK?1:0;
			//再次进行同样的与操作即可恢复原始zobrist值
			zobristKey=zobristKey^this.zobristPlayer^this.zobristPlayerTable[color][po.x-1][po.y-1];
			zobristKeyCheck=zobristKeyCheck^this.zobristPlayerCheck^this.zobristPlayerCheckTable[color][po.x-1][po.y-1];
		}
		
		
	}
	
	/**
	 * 走子类：对局面进行走子或取消走子，并改变局面路、点的状态，并记录变化日志。
	 * @author Hong
	 *
	 */
	public class MakeMove{
		/**
		 * 走子
		 * @param po 
		 * @param color
		 * @return 表示游戏是否结束
		 */
		public boolean makeMovePosition(Position po,int color){
			//获取棋盘上的po点
			po=Board.this.getPosition(po);
			if(manual.contains(po)){
				//直接抛出走步错误的异常
				//throw new IllegalArgumentException("this postion was been in manual!");
				return false;
			}
			boolean isWin=false;//记录游戏是否结束
		    int	_color=color==Position.Color.BLACK?1:0;
			//记录变化的有效点和有效路
			HashMap<Position,Boolean> posChange=new HashMap<Position,Boolean>(50);
			HashMap<Road,Boolean> roadChange=new HashMap<Road,Boolean>(50);
			int[][] historyChessType=chessTypeRecord.chessTypeClone();//获取历史棋形值
			
			//棋盘走子:棋谱走子+zobrist走子+局面该点走子
			manual.push(po);
			zobrist.move(po, color);
			positions[po.x-1][po.y-1].color=color;
			
			//踢出该有效点
			//一定要判断是否是有效点，如果不是有效点则不记录变化日志
			if(chessTypeRecord.availablePos.contains(po)){
				chessTypeRecord.changeAvailablePos(po, false);
				posChange.put(po, false);
			}
			
			//改变该点所在的路的状态
			List<Road> roads=toRoads[po.x-1][po.y-1];
			for (Road road : roads) {
				int hisCount=road.count++;//增加拥有子的数量，并记录历史数量
				if(road.isDrop)continue;//对于抛弃路不做信息更改
				
				//空路
				if(road.color==Position.Color.EMPTY){
					//1.棋形变化：成1路++
					chessTypeRecord.chessType[_color][0]++;
					//2.设置路的颜色
					road.color=color;
					//3.设置路的抛弃状态：[不变]
					//4.路赋值
					road.score=getDynamicValueOfRoad(road, po, true);
					//5.更改有效路
					chessTypeRecord.changeAvailableRoad(road, true);
					//记录有效路日志
					roadChange.put(road, true);
					//6.添加新的有效点
					List<Position> list=toPositions[road.index-1];
					//循环该路上所有的点
					for(Position pos:list){
						if(this.isNearBy(pos, po, 2) && pos.color==Position.Color.EMPTY && !chessTypeRecord.contains(pos)){
							//找到距离落子点2个长度以内的空点，添加为新的有效点
							chessTypeRecord.changeAvailablePos(pos, true);
							//记录有效点变化日志
							posChange.put(pos, true);
						}
					}
				}
				
				//同色路
				else if(road.color==color){
					//判断游戏是否结束
					if(road.count>=6){
						isWin=true;
					}
					//1.棋形变化：原棋形数量--，新棋形数量++
					chessTypeRecord.chessType[_color][hisCount-1]--;
					chessTypeRecord.chessType[_color][hisCount]++;
					//2.设置路颜色 [不变]
                    //3.设置路的抛弃状态 [不变]
					
                    //4.路赋值
					road.score=getDynamicValueOfRoad(road, po, true);
					//5.更改有效路[不变]
					//6.添加新的有效点
					List<Position> list=toPositions[road.index-1];
					//循环该路上所有的点
					for(Position pos:list){
						if(this.isNearBy(pos, po, 2) && pos.color==Position.Color.EMPTY && !chessTypeRecord.contains(pos)){
							//找到距离落子点2个长度以内的空点，添加为新的有效点
							chessTypeRecord.changeAvailablePos(pos, true);
							//记录有效点变化日志
							posChange.put(pos, true);
						}
					}
				}
				
				//异色路，则抛弃
				else {
					//1.棋形变化：原棋形数量--
					chessTypeRecord.chessType[_color][hisCount-1]--;
					//抛弃时记录抛弃前的拥有子数量
					road.countWhenDrop=hisCount;
					//2.设置路颜色 [改变了也无意义，那就干脆不要改它]
					//3.设置路的抛弃状态 [不变]
					road.isDrop=true;
					//4.路赋值,
                    //注意：当路抛弃后，其分值依然保留抛弃前的分值，
                    //抛弃后其分值是无意义的，在使用其分值时需要配合抛弃状态一起使用
					//然而这样的保存是为了恢复抛弃状态后，重新使用其值
					//5.有效变化
					chessTypeRecord.changeAvailableRoad(road, false);
					roadChange.put(road, false);
					//6.有效点变化[不变]
				}
			}
			
			//保存有效点，有效路变化日志和历史棋形
			chessTypeRecord.changePosLog(posChange, roadChange, historyChessType);
			
			return isWin;
		}
		/**
		 * 取消走子
		 */
		public Position unmakeMovePosition(){
			//棋谱取消走子
			Position pos=manual.pull();
			//棋盘取消走子+zobrist取消走子
			zobrist.unmove(pos, pos.color);
			pos.color=Position.Color.EMPTY;
			//通过棋形日志恢复棋形
			chessTypeRecord.unchangePosLog();
			List<Road> list=toRoads[pos.x-1][pos.y-1];
			for(Road road:list){
				road.count--;//恢复取消点所在的路的拥有子的数量
				if(road.count<=0){
					//恢复空路颜色以及countWhenDrop
					road.color=Position.Color.EMPTY;
					road.countWhenDrop=-1;
				}
				//恢复分值
				road.score=getDynamicValueOfRoad(road, pos, false);
				if(road.isDrop && road.count==road.countWhenDrop){
					//当多次取消走子后，路的拥有子数量如果恢复到抛弃前的状态，则恢复路有效
                    //则此时配合路的有效状态使用路的分值，就可以进行读取了。
					road.isDrop=false;
					road.countWhenDrop=-1;
				}
			}
			return pos;
		}
		
		/**
		 * 走步
		 * @param step
		 * @param color
		 * @return 表示游戏是否结束
		 */
		public boolean makeMoveStep(Step step,int color){
			return ((step.one==null?false:makeMovePosition(step.one, color))
					|| (step.another==null?false:makeMovePosition(step.another, color)));
		}
		/**
		 * 取消走步
		 */
		public Step unmakeMoveStep(){
			Step step=new Step();
			step.one=unmakeMovePosition();
			step.another=unmakeMovePosition();
			return step;
		}
		
		/**
		 * 获取点pos在road上的动态分值
		 * @param road 注意：road的count值已经改变
		 * @param pos
		 * @param isMakeMove
		 * @return
		 */
		public int getDynamicValueOfRoad(Road road,Position pos,boolean isMakeMove){
			int count=road.count;
			if(isMakeMove){
				//走子，（原路分值/原棋形分值+新增点在路上的分值）*新棋形分值
				return (road.score/ConstValue.EachChess[count-1] + getStaticValueInRoad(road,pos))
					*ConstValue.EachChess[count];
			}else{
				if(!road.isDrop){
					//取消走子前未抛弃，（原路分值/原棋形分值-取消点在路上的分值）*新棋形分值
					return (road.score/ConstValue.EachChess[count+1] - getStaticValueInRoad(road,pos))
					*ConstValue.EachChess[count];
				}else return road.score;//如果是抛弃路，则返回路的原值
			}
		}
		
		/**
		 * 获取点pos在road上的静态分值
		 * @param road
		 * @param pos
		 * @return
		 */
		public int getStaticValueInRoad(Road road,Position pos){
			int indexOfRoad;
			//计算点在该路上的位置
			if(road.index>=0&&road.index<266){//水平
				indexOfRoad=(pos.y+19*(pos.x-1)-road.index)/19;
			}else if(road.index>=266&&road.index<532){//竖直
				indexOfRoad=(266+pos.x+19*(pos.y-1)-road.index)/19;
			}else if(road.index>=532&&road.index<728){//左上至右下斜
				indexOfRoad=(532+pos.y+14*(pos.x-1)-road.index)/15;
			}else {//右上至左下斜
				indexOfRoad=(road.index-728-pos.y-14*(pos.x-1)+70)/13;
			}
			//通过位置去获取分值
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
		 * 判断两个点是否在length距离内
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
		 * 7种基本棋型的值:空路、成1、......、成6
		 */
		public static final int[] EachChess={1, 2, 4, 8, 16, 32, 64};
		/**
		 * 路上6个点对于该路的分值
		 */
		public static final int[] EachPostionInRoad={2, 4, 8, 8, 4, 2};
	}
}

	
