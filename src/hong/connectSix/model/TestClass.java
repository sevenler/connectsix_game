package hong.connectSix.model;




import java.util.List;

import junit.framework.TestCase;

public class TestClass extends TestCase {
	
	public void test(int i){
		/*Manual m=new Manual();
		Manual m1=new Manual();
		Position p=new Position(1,1);
		Position p1=null;
		m.add(p);
		m1.add(p);
		p=new Position(2,2);
		m.add(p);
		p1=new Position(3,3);
		m.add(p1);
		
		m1.add(p1);
		m1.add(new Position(2,2));
	
		System.out.println("manual:"+(m.getColorIndexOf(6)==Position.Color.BLACK?"Black":"white"));
		System.out.println("manual:"+m.equals(m1));
		
		Board board=Board.getInstance();
		int[][] historyChessType=board.chessTypeRecord.getChessType();
		board.chessTypeRecord.changeAvailablePos(new Position("AA"), true);
		board.chessTypeRecord.changeAvailableRoad(new Road(215), true);
		board.chessTypeRecord.changeChessType(Position.Color.WHITE, Road.ChessType.ONE, true);
		
		HashMap<Position,Boolean> posChange=new HashMap<Position,Boolean>();
		posChange.put(new Position("AA"), true);
		HashMap<Road,Boolean> roadChange=new HashMap<Road,Boolean>();
		roadChange.put(new Road(215),true);
		board.chessTypeRecord.changePosLog(posChange, roadChange, historyChessType);
		System.out.println("chessType:"+board.chessTypeRecord.toString());
		board.chessTypeRecord.unchangePosLog();
		System.out.println("chessType:"+board.chessTypeRecord.toString());
		
		
		//≤‚ ‘zobrist
		Board.Zobrist zobrist=board.zobrist.newZobrist(board.zobrist);
		board.zobrist.makeMoveHash(new Position(10,10), Position.Color.BLACK);
		board.zobrist.makeMoveHash(new Position(11,11), Position.Color.BLACK);
		board.zobrist.unmakeMoveHash(new Position(10,10), Position.Color.BLACK);
		board.zobrist.unmakeMoveHash(new Position(11,11), Position.Color.BLACK);
		System.out.println("before:"+zobrist);
		System.out.println("after:"+board.zobrist);*/
		/*
		Board board=Board.getInstance();
		Position po=board.toPositions[0].get(0);
		Road road = board.toRoad[0][0].get(0);
		road.count++;
		System.out.println(" position:"+po);
		System.out.println(" road:"+road);
		System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		road.score=board.makeMove.getDynamicValueOfRoad(road, po, true);
		System.out.println(" dynamic value:"+road.score);
		
		 po=board.toPositions[0].get(1);
		 road = board.toRoad[0][0].get(0);
		road.count++;
		System.out.println(" position:"+po);
		System.out.println(" road:"+road);
		System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		road.score=board.makeMove.getDynamicValueOfRoad(road, po, true);
		System.out.println(" dynamic value:"+road.score);
		
		 po=board.toPositions[0].get(1);
		 road = board.toRoad[0][0].get(0);
		road.count--;
		System.out.println(" position:"+po);
		System.out.println(" road:"+road);
		System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		road.score=board.makeMove.getDynamicValueOfRoad(road, po, false);
		System.out.println(" dynamic value:"+road.score);
		
		po=board.toPositions[0].get(0);
		road = board.toRoad[0][0].get(0);
		road.count--;
		System.out.println(" position:"+po);
		System.out.println(" road:"+road);
		System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		road.score=board.makeMove.getDynamicValueOfRoad(road, po, false);
		System.out.println(" dynamic value:"+road.score);*/
		
		/*po=board.toPositions[0].get(2);
		road = board.toRoad[0][0].get(0);
		road.count++;
		System.out.println(" position:"+po);
		System.out.println(" road:"+road);
		System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		road.score=board.makeMove.getDynamicValueOfRoad(road, po, true);
		System.out.println(" dynamic value:"+road.score);
		
		po=board.toPositions[0].get(3);
		road = board.toRoad[0][0].get(0);
		road.count++;
		System.out.println(" position:"+po);
		System.out.println(" road:"+road);
		System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		road.score=board.makeMove.getDynamicValueOfRoad(road, po, true);
		System.out.println(" dynamic value:"+road.score);
		
		po=board.toPositions[0].get(4);
		road = board.toRoad[0][0].get(0);
		road.count++;
		System.out.println(" position:"+po);
		System.out.println(" road:"+road);
		System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		road.score=board.makeMove.getDynamicValueOfRoad(road, po, true);
		System.out.println(" dynamic value:"+road.score);
		
		po=board.toPositions[0].get(5);
		road = board.toRoad[0][0].get(0);
		road.count++;
		System.out.println(" position:"+po);
		System.out.println(" road:"+road);
		System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		road.score=board.makeMove.getDynamicValueOfRoad(road, po, true);
		System.out.println(" dynamic value:"+road.score);*/
		
		/*Position po=null;
		Road road=null;
		for(int i=0;i<924;i++){
			 po=board.toPositions[i].get(0);
			 road=board.toRoad[po.x-1][po.y-1].get(2);
			 System.out.println(" position:"+po);
				System.out.println(" road:"+road);
				System.out.println(" static value:"+board.makeMove.getStaticValueInRoad(road, po));
		}*/
		
		
		/*String str="";
		for(Road road :board.toRoad[9][9]){
			str+=road.toString()+"\n";
		}
		System.out.println("=="+str);*/
		
		/*Board board=Board.getInstance();
		Board board1=null;
		try{
			board1=board.clone();
		}catch(Exception e){
			e.printStackTrace();
		}
		board.makeMove.makeMovePosition(board.getPosition(9, 9), Position.Color.WHITE);
		board.makeMove.makeMovePosition(board.getPosition(1, 1), Position.Color.WHITE);
		board.makeMove.makeMovePosition(board.getPosition(19, 19), Position.Color.WHITE);
		board.makeMove.unmakeMovePosition();
		board.makeMove.unmakeMovePosition();
		board.makeMove.unmakeMovePosition();
		try{
			System.out.println(" compare result:"+board.compare(board1));
		}catch(Exception e){
			e.printStackTrace();
		}*/
		try{
			Manual.toPositions("JJSKJJSS");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testOfManual(){
		/*Position pos=new Position(10,10);
		Manual manual=new Manual();
		manual.push(new Position(1,5));
		manual.push(new Position(10,5));
		manual.push(new Position(10,10));
		System.out.println(manual.contains(pos));*/
		Board board=Board.getInstance();
		Position pos=Position.toInt("HQ");
		List<Road> list=board.toRoads(pos);
		for(Road road:list){
			System.out.println("--"+road);
		}
		System.out.println("---------6666666666-----");
		for(int i=0;i<19;i++){
			for(int j=0;j<19;j++){
				pos=new Position(i+1,j+1);
				list=board.toRoads(pos);
				for(Road road:list){
					List<Position> li=board.toPositions(road);
					if(!li.contains(pos))
						System.out.println("------");
					
				//	System.out.println(road);
				}
				/*System.out.println("--"+pos);
				System.out.println("---------------");*/
			}
		}
		
	}
	
	
}
