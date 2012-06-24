package hong.connectSix.wight;

import hong.connectSix.model.Board;
import hong.connectSix.model.Position;
import hong.connectSix.ui.ConnectSixUIActivity.MessageHandler;
import hong.connectSix.ui.ConnectSixUIActivity.Messages;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 棋盘控件
 * @author Hong
 *
 */
public class BoardView extends View{
	private static final String TAG="BoardView";
	private BoardDraw boardDraw=null;
	private MessageHandler messageHandler=null;
	private Point touchBeginTemp=null;
	private Point touchMoveTemp=null;
	private boolean isMuiltTouch=false;
    
	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public BoardDraw getBoardDraw() {
		return boardDraw;
	}

	//数据棋盘,本应构造传入的，但是由于控件的构造是系统根据布局文件来调用的，
	//因此在外部Activity使用本控件的时候，应该给board赋值
	public Board board=null;
	
	
	public void setBoard(Board board) {
		this.board = board;
	}

	//棋盘构造函数
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
                switch (action) { 
	                case (MotionEvent.ACTION_DOWN):
	                	touchBeginTemp=new Point((int)event.getX(),(int)event.getY());
	                    break; 
	                case (MotionEvent.ACTION_UP):
	                	System.out.println("isMuiltTouch:"+isMuiltTouch);
	                	if(!isMuiltTouch){
		                	Point last=new Point((int)event.getX(),(int)event.getY());
		                	//点击走子
		                	if(boardDraw.isNear(last, touchBeginTemp)){
		        				//判断是否在走子范围内
		        				if(boardDraw.isLeagel(last.x, last.y)){
		        					Position pos=boardDraw.toPosition(last.x, last.y);
		        					if(!board.manual.contains(pos)){
		        						//如果点击走步，发送走子消息
		        						messageHandler.sendMessage(Messages.GO, pos);
		        					}
		        				}
		                	}else{//移动棋盘操作
		                		Point temp=new Point((int)event.getX(),(int)event.getY());
			                	System.out.println(" touch moved!");
								int x=temp.x-touchBeginTemp.x;
								int y=temp.y-touchBeginTemp.y;
								boardDraw.startX=boardDraw.startX+x;
								boardDraw.startY=boardDraw.startY+y;
								//禁止移动范围超过棋盘边界
								//左移
								if(x < 0 && boardDraw.startX + boardDraw.size * boardDraw.blockLength
										< boardDraw.staticX + boardDraw.size * boardDraw.staticLength){
									boardDraw.startX=boardDraw.startX + (boardDraw.staticX + boardDraw.size * boardDraw.staticLength
									- (boardDraw.startX + boardDraw.size * boardDraw.blockLength));
								}
								//右移
								if(x > 0 && boardDraw.startX > boardDraw.staticX){
									boardDraw.startX =  boardDraw.staticX;
								}
								//上移
								if(y < 0 && boardDraw.startY + boardDraw.size * boardDraw.blockLength
										< boardDraw.staticY + boardDraw.size * boardDraw.staticLength){
									boardDraw.startY=boardDraw.startY + (boardDraw.staticY + boardDraw.size * boardDraw.staticLength
									- (boardDraw.startY + boardDraw.size * boardDraw.blockLength));
								}
								//下移
								if(y > 0 && boardDraw.startY > boardDraw.staticY){
									boardDraw.startY =  boardDraw.staticY;
								}
		                	}
	                	}
	                	isMuiltTouch=false;
	                	BoardView.this.invalidate();
	                    break; 
	                case (MotionEvent.ACTION_MOVE): 
	                	//双指触摸放大、缩小操作
	                	if(event.getPointerCount()==2){
	                		Point current=aberration(event);
	                		if(touchMoveTemp != null){
	                			if(boardDraw.isLengthLagel(true) && isInFrequency(touchMoveTemp,current,BoardDraw.FREQUENCY) ){
			                		if(isEnlargement(touchMoveTemp, current)){//放大
			                			boardDraw.blockLength=boardDraw.blockLength+boardDraw.staticLength/(BoardDraw.FREQUENCY*5);
			                			boardDraw.isLengthLagel(true);
			                			boardDraw.resetChessImageSize();
			                			BoardView.this.invalidate();
			                			System.out.println("more bigger..");
			                		}
			                		else {//缩小
			                			boardDraw.blockLength=boardDraw.blockLength-boardDraw.staticLength/(BoardDraw.FREQUENCY*5);
			                			boardDraw.isLengthLagel(true);
			                			boardDraw.resetChessImageSize();
			                			BoardView.this.invalidate();
			                			System.out.println("more smaller..");
			                		}
			                		//获取初始触摸点做为放大中心点
			                		if(!isMuiltTouch){
				                		Point center=center(event);
		                				boardDraw.startX=center.x - boardDraw.blockLength * boardDraw.size/2;
		                				boardDraw.startY=center.y - boardDraw.blockLength * boardDraw.size/2;
			                		}
	                			}
	                		}else if(event.getPointerCount()==1){
	                			Point temp=new Point((int)event.getX(),(int)event.getY());
			                	System.out.println(" touch moved!");
								int x=temp.x-touchBeginTemp.x;
								int y=temp.y-touchBeginTemp.y;
								boardDraw.startX=boardDraw.startX+x;
								boardDraw.startY=boardDraw.startY+y;
								//禁止移动范围超过棋盘边界
								//左移
								if(x < 0 && boardDraw.startX + boardDraw.size * boardDraw.blockLength
										< boardDraw.staticX + boardDraw.size * boardDraw.staticLength){
									boardDraw.startX=boardDraw.startX + (boardDraw.staticX + boardDraw.size * boardDraw.staticLength
									- (boardDraw.startX + boardDraw.size * boardDraw.blockLength));
								}
								//右移
								if(x > 0 && boardDraw.startX > boardDraw.staticX){
									boardDraw.startX =  boardDraw.staticX;
								}
								//上移
								if(y < 0 && boardDraw.startY + boardDraw.size * boardDraw.blockLength
										< boardDraw.staticY + boardDraw.size * boardDraw.staticLength){
									boardDraw.startY=boardDraw.startY + (boardDraw.staticY + boardDraw.size * boardDraw.staticLength
									- (boardDraw.startY + boardDraw.size * boardDraw.blockLength));
								}
								//下移
								if(y > 0 && boardDraw.startY > boardDraw.staticY){
									boardDraw.startY =  boardDraw.staticY;
								}
								boardDraw.resetChessImageSize();
								BoardView.this.invalidate();
	                		}
	                		if(!isMuiltTouch) touchMoveTemp=current;
	                		isMuiltTouch=true;
	                    }
                }  
                return true; 
			}
		});
	}
	
	/**
	 * 获取2点触摸的差
	 * @param event
	 * @return
	 */
	private Point aberration(MotionEvent event){
		float x = (event.getX(0) > event.getX(1))?(event.getX(0) - event.getX(1)):(event.getX(1) - event.getX(0));  
        float y = (event.getY(0) > event.getY(1))?(event.getY(0) - event.getY(1)):(event.getY(1) - event.getY(0));
        return new Point((int)x,(int)y);
	}
	/**
	 * 获取2点触摸的中间点
	 * @param event
	 * @return
	 */
	private Point center(MotionEvent event){
		float x = (event.getX(0) > event.getX(1))?event.getX(1)+(event.getX(0) - event.getX(1))/2:
			event.getX(0)+(event.getX(1) - event.getX(0))/2;
        float y = (event.getY(0) > event.getY(1))?event.getY(1)+(event.getY(0) - event.getY(1))/2:
        	event.getY(0)+(event.getY(1) - event.getY(0))/2;
        return new Point((int)x,(int)y);
	}
	/**
	 * 是否放大操作
	 * @param old
	 * @param current
	 * @return
	 */
	private boolean isEnlargement(Point old,Point current){
		if((current.x > old.x) && (current.y > old.y)){
			return true;
		}return false;
	}
	/**
	 * 两点的变化距离是否满足frequency长度
	 * @param old
	 * @param current
	 * @param frequency
	 * @return
	 */
	private boolean isInFrequency(Point old,Point current,int frequency){
		int x=(old.x>current.x)?(old.x-current.x):(current.x-old.x);
		int y=(old.y>current.y)?(old.y-current.y):(current.y-old.y);
		if((x>frequency) && (y> frequency)) return true;
		else return false;
	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//将宽度设置成和高度一样
		super.onMeasure(heightMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//正常的理解应该是boardDraw的初始化应放在BoardView的构造函数里面
		//由于boardDraw的初始化需要传入canvas，因此在这里做初始化工作
		if(boardDraw==null){
			int padding=20;//棋盘网格距离边框的长度
			//每个格子的宽度,19*19路的话，则格子的数量是18
			//分成18+1份是由于，另外1份是边界距离
			int width=(this.getWidth()-padding*2)/18;
			//为了两边对齐，对padding进行修正
			padding=padding+(this.getWidth()-padding*2)%18/2;
			boardDraw=new BoardDraw(padding, padding, width, 18,canvas);
		}
		boardDraw.drawGrid();
		if(board!=null)
			drawChess();
		else{
			Log.e(TAG, "board in BoardView must be initilized,cannot be null.");
		}
	}
	
	/**
	 * 重绘所有棋子
	 */
	private void drawChess(){
		Position temp=null;
		int color=0;
		int size=this.board.manual.size();
		int i=1;
		//绘制所有棋子
		for(;i<=size;i++){
			temp=this.board.manual.getPositionOf(i);
			color=this.board.manual.getColorIndexOf(i);
			boardDraw.drawChess(temp,color);
		}
		//绘制最后4个走子的红点
		//需要标记的最后走的几的点数量
		int count=(size>=4?4:size)-1;
		for(i=size-count;i<=size;i++){
			temp=this.board.manual.getPositionOf(i);
			color=this.board.manual.getColorIndexOf(i);
			boardDraw.drawPoint(temp, Color.RED);
		}
		//绘制最后2个走子的提示框
		count=(size>=2?2:size)-1;
		for(i=size-count;i<=size;i++){
			temp=this.board.manual.getPositionOf(i);
			color=this.board.manual.getColorIndexOf(i);
			boardDraw.drawPoint(temp, Color.RED);
			boardDraw.drawNotice(temp, Color.GREEN);
		}
	}
	
	/**
	 * 棋盘画图类，包括画棋子、画网格、画提示框
	 * @author Hong
	 *
	 */
	public class BoardDraw {
			public static final int FREQUENCY=2;
			public  int lengthMax;
			public  int lengthMin;
			public	int staticX;
			public  int staticY;
			public  int staticLength;
			
			private int startX;//横向起点
			private int startY;//纵向起点
			private int blockLength;//方格长度
			private int size;//表格的个数，六子棋为19*19，则size=18
			private Canvas canvas;
			private Paint paint=new Paint();//画笔
			//用于缩放棋子
			Matrix matrix;
			//棋子bitmap
			//BitmapFactory是一个工具类，里面有大量的方法从不同的数据源来解析、创建bitmap对象
			Bitmap black_cache=BitmapFactory.decodeResource(getResources(),hong.connectSix.R.drawable.black);
			Bitmap black_=null;
			Bitmap white_cache=BitmapFactory.decodeResource(getResources(),hong.connectSix.R.drawable.white);
			Bitmap white_=null;
			Bitmap backgroud_cache=BitmapFactory.decodeResource(getResources(),hong.connectSix.R.drawable.board_pad);
			Bitmap backgroud_=null;
			
			public BoardDraw(int startX, int startY, int blockLength,int size,Canvas canvas) {
				super();
				this.startX = startX;
				this.startY = startY;
				this.blockLength = blockLength;
				this.size = size;
				this.canvas=canvas;
				this.staticX=startX;
				this.staticY=startY;
				this.staticLength = blockLength;
				
				this.lengthMin=blockLength;
				this.lengthMax=blockLength*3;
				
				resetChessImageSize();
			}
			
			/**
			 * 画网格
			 */
			public void drawGrid(){
				this.canvas.drawBitmap(this.backgroud_, 1, 1, paint);
				
				paint.setColor(Color.parseColor("#946139"));
				paint.setStrokeWidth(4);
				//画边界线
				canvas.drawLine(startX, startY, startX+size*blockLength, startY, paint);
				canvas.drawLine(startX, startY, startX, startY+size*blockLength, paint);
				canvas.drawLine(startX+size*blockLength, startY, startX+size*blockLength, startY+size*blockLength, paint);
				canvas.drawLine(startX, startY+size*blockLength, startX+size*blockLength, startY+size*blockLength, paint);
				//画内部分割线
				paint.setStrokeWidth(2);
				for(int i=1;i<=18;i++){
					canvas.drawLine(startX+i*blockLength, startY, startX+i*blockLength, startY+size*blockLength, paint);
				}
				for(int i=1;i<=18;i++){
					canvas.drawLine(startX, startY+i*blockLength, startX+size*blockLength, startY+i*blockLength, paint);
				}
				//画9个校位点
				canvas.drawCircle(startX+3*blockLength, startY+3*blockLength, 5, paint);
				canvas.drawCircle(startX+3*blockLength, startY+9*blockLength, 5, paint);
				canvas.drawCircle(startX+3*blockLength, startY+15*blockLength, 5, paint);
				canvas.drawCircle(startX+9*blockLength, startY+3*blockLength, 5, paint);
				canvas.drawCircle(startX+9*blockLength, startY+9*blockLength, 5, paint);
				canvas.drawCircle(startX+9*blockLength, startY+15*blockLength, 5, paint);
				canvas.drawCircle(startX+15*blockLength, startY+3*blockLength, 5, paint);
				canvas.drawCircle(startX+15*blockLength, startY+9*blockLength, 5, paint);
				canvas.drawCircle(startX+15*blockLength, startY+15*blockLength, 5, paint);
				//画网格表示字母和数字
				paint.setStrokeWidth(1);
				paint.setColor(Color.BLACK);
				char character;
				for(int i=0;i<19;i++){
					character=(char)('A'+i);
					//横向数字
					canvas.drawText(i+1+"", startX+i*blockLength, this.startY-5, paint);
					//横向字母
					canvas.drawText(""+character, startX+i*blockLength, this.startY+size*blockLength+15, paint);
					//纵向数字
					canvas.drawText(i+1+"", startX-15, this.startY+i*blockLength, paint);
					//纵向字母
					canvas.drawText(""+character, startX+size*blockLength+5, this.startY+i*blockLength, paint);
				}
			}
			
			/**
			 * 画棋子
			 * @param pos 点
			 * @param color 点的颜色 例如:Position.Color.BLACK
			 */
			public void drawChess(Position pos,int color){
				Bitmap bitmap=(color==Position.Color.BLACK?this.black_:this.white_);
				this.canvas.drawBitmap(bitmap, (pos.x-1)*blockLength+this.startX-blockLength/2, (pos.y-1)*blockLength+this.startY-blockLength/2, paint);
			}
			
			/**
			 * 画color色的提示点
			 * @param pos
			 * @param color
			 */
			public void drawPoint(Position pos,int color){
				paint.setColor(color);
				this.canvas.drawCircle((pos.x-1)*blockLength+startX, (pos.y-1)*blockLength+startY, 4, paint);
			}
			
			/**
			 * 画提示框
			 * @param pos
			 * @param color 提示框的颜色 例如：Color.Black
			 */
			public void drawNotice(Position pos,int color){
				paint.setColor(color);
				paint.setStrokeWidth(2);
				int x=(pos.x-1)*blockLength-blockLength/2+this.startX,y=(pos.y-1)*blockLength-blockLength/2+this.startY;
				int len=blockLength/3;
				//横向4条线
				this.canvas.drawLine(x, y, x+len, y, paint);
				this.canvas.drawLine(x, y+blockLength, x+len, y+blockLength, paint);
				this.canvas.drawLine(x+blockLength-len, y, x+blockLength, y, paint);
				this.canvas.drawLine(x+blockLength-len, y+blockLength, x+blockLength, y+blockLength, paint);
				//纵向4条线
				this.canvas.drawLine(x, y, x, y+len, paint);
				this.canvas.drawLine(x+blockLength, y, x+blockLength, y+len, paint);
				this.canvas.drawLine(x, y+blockLength-len, x, y+blockLength, paint);
				this.canvas.drawLine(x+blockLength, y+blockLength-len, x+blockLength, y+blockLength, paint);
				paint.setColor(color);
			}
			
			/**
			 * 重新设置棋子图片的高、宽
			 */
			public void resetChessImageSize(){
				if(matrix==null){
					matrix=new Matrix();
				}
				//计算棋子的缩放比例
				matrix.setScale(blockLength/(float)black_cache.getWidth(), blockLength/(float)black_cache.getHeight());
				//对bitmap进行缩放
				black_=Bitmap.createBitmap(black_cache, 0, 0, black_cache.getWidth(), black_cache.getHeight(), matrix, true);
				matrix.setScale(blockLength/(float)white_cache.getWidth(), blockLength/(float)white_cache.getHeight());
				white_=Bitmap.createBitmap(white_cache, 0, 0, white_cache.getWidth(), white_cache.getHeight(), matrix, true);
				matrix.setScale((this.blockLength*this.size+45)/(float)backgroud_cache.getWidth(), (this.blockLength*this.size+45)/(float)backgroud_cache.getHeight());
				backgroud_=Bitmap.createBitmap(backgroud_cache, 0, 0, backgroud_cache.getWidth(), backgroud_cache.getHeight(), matrix, true);
			}
			
			/**
			 * 判断坐标是否在棋盘内
			 * @param x
			 * @param y
			 * @return
			 */
			public boolean isLeagel(int x,int y){
				if(x >= this.startX && y >= this.startY
						&& x <= this.startX+18*blockLength && y <= this.startY+18*blockLength){
					return true;
				}
				return false;
			}
			
			/**
			 * 判断是否达到最大或最小收缩范围
			 * @param recover 超过最大或最小范围是否恢复大小
			 * @return
			 */
			public boolean isLengthLagel(boolean recover){
				if(this.blockLength - this.lengthMin < 0){
					if(recover) this.blockLength=this.lengthMin;
					return false;
				}else if(this.blockLength - this.lengthMax > 0){
					if(recover) this.blockLength=this.lengthMax;
					return false;
				}else return true;
			}
			
			public boolean isMarginLagel(boolean recover){
				
				return true;
			}
			
			/**
			 * 判断点one是否在another点附近 附件的条件是相距记录小于length
			 * @param one
			 * @param another
			 * @return
			 */
			public boolean isNear(Point one,Point another){
				if(one.x<another.x+this.blockLength && one.x>another.x-this.blockLength
						&& one.y<another.y+this.blockLength && one.y>another.y-this.blockLength){
					return true;
				}
				return false;
			}
			
			/**
			 * 像素坐标转换成棋盘点
			 * @param x
			 * @param y
			 * @return
			 */
			public Position toPosition(int x,int y){
				int posX=(x-this.startX+blockLength/2)/blockLength+1;
				int posY=(y-this.startY+blockLength/2)/blockLength+1;
				return new Position(posX,posY);
			}
		}
	
}
