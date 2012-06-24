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
 * ���̿ؼ�
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

	//��������,��Ӧ���촫��ģ��������ڿؼ��Ĺ�����ϵͳ���ݲ����ļ������õģ�
	//������ⲿActivityʹ�ñ��ؼ���ʱ��Ӧ�ø�board��ֵ
	public Board board=null;
	
	
	public void setBoard(Board board) {
		this.board = board;
	}

	//���̹��캯��
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
		                	//�������
		                	if(boardDraw.isNear(last, touchBeginTemp)){
		        				//�ж��Ƿ������ӷ�Χ��
		        				if(boardDraw.isLeagel(last.x, last.y)){
		        					Position pos=boardDraw.toPosition(last.x, last.y);
		        					if(!board.manual.contains(pos)){
		        						//�������߲�������������Ϣ
		        						messageHandler.sendMessage(Messages.GO, pos);
		        					}
		        				}
		                	}else{//�ƶ����̲���
		                		Point temp=new Point((int)event.getX(),(int)event.getY());
			                	System.out.println(" touch moved!");
								int x=temp.x-touchBeginTemp.x;
								int y=temp.y-touchBeginTemp.y;
								boardDraw.startX=boardDraw.startX+x;
								boardDraw.startY=boardDraw.startY+y;
								//��ֹ�ƶ���Χ�������̱߽�
								//����
								if(x < 0 && boardDraw.startX + boardDraw.size * boardDraw.blockLength
										< boardDraw.staticX + boardDraw.size * boardDraw.staticLength){
									boardDraw.startX=boardDraw.startX + (boardDraw.staticX + boardDraw.size * boardDraw.staticLength
									- (boardDraw.startX + boardDraw.size * boardDraw.blockLength));
								}
								//����
								if(x > 0 && boardDraw.startX > boardDraw.staticX){
									boardDraw.startX =  boardDraw.staticX;
								}
								//����
								if(y < 0 && boardDraw.startY + boardDraw.size * boardDraw.blockLength
										< boardDraw.staticY + boardDraw.size * boardDraw.staticLength){
									boardDraw.startY=boardDraw.startY + (boardDraw.staticY + boardDraw.size * boardDraw.staticLength
									- (boardDraw.startY + boardDraw.size * boardDraw.blockLength));
								}
								//����
								if(y > 0 && boardDraw.startY > boardDraw.staticY){
									boardDraw.startY =  boardDraw.staticY;
								}
		                	}
	                	}
	                	isMuiltTouch=false;
	                	BoardView.this.invalidate();
	                    break; 
	                case (MotionEvent.ACTION_MOVE): 
	                	//˫ָ�����Ŵ���С����
	                	if(event.getPointerCount()==2){
	                		Point current=aberration(event);
	                		if(touchMoveTemp != null){
	                			if(boardDraw.isLengthLagel(true) && isInFrequency(touchMoveTemp,current,BoardDraw.FREQUENCY) ){
			                		if(isEnlargement(touchMoveTemp, current)){//�Ŵ�
			                			boardDraw.blockLength=boardDraw.blockLength+boardDraw.staticLength/(BoardDraw.FREQUENCY*5);
			                			boardDraw.isLengthLagel(true);
			                			boardDraw.resetChessImageSize();
			                			BoardView.this.invalidate();
			                			System.out.println("more bigger..");
			                		}
			                		else {//��С
			                			boardDraw.blockLength=boardDraw.blockLength-boardDraw.staticLength/(BoardDraw.FREQUENCY*5);
			                			boardDraw.isLengthLagel(true);
			                			boardDraw.resetChessImageSize();
			                			BoardView.this.invalidate();
			                			System.out.println("more smaller..");
			                		}
			                		//��ȡ��ʼ��������Ϊ�Ŵ����ĵ�
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
								//��ֹ�ƶ���Χ�������̱߽�
								//����
								if(x < 0 && boardDraw.startX + boardDraw.size * boardDraw.blockLength
										< boardDraw.staticX + boardDraw.size * boardDraw.staticLength){
									boardDraw.startX=boardDraw.startX + (boardDraw.staticX + boardDraw.size * boardDraw.staticLength
									- (boardDraw.startX + boardDraw.size * boardDraw.blockLength));
								}
								//����
								if(x > 0 && boardDraw.startX > boardDraw.staticX){
									boardDraw.startX =  boardDraw.staticX;
								}
								//����
								if(y < 0 && boardDraw.startY + boardDraw.size * boardDraw.blockLength
										< boardDraw.staticY + boardDraw.size * boardDraw.staticLength){
									boardDraw.startY=boardDraw.startY + (boardDraw.staticY + boardDraw.size * boardDraw.staticLength
									- (boardDraw.startY + boardDraw.size * boardDraw.blockLength));
								}
								//����
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
	 * ��ȡ2�㴥���Ĳ�
	 * @param event
	 * @return
	 */
	private Point aberration(MotionEvent event){
		float x = (event.getX(0) > event.getX(1))?(event.getX(0) - event.getX(1)):(event.getX(1) - event.getX(0));  
        float y = (event.getY(0) > event.getY(1))?(event.getY(0) - event.getY(1)):(event.getY(1) - event.getY(0));
        return new Point((int)x,(int)y);
	}
	/**
	 * ��ȡ2�㴥�����м��
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
	 * �Ƿ�Ŵ����
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
	 * ����ı仯�����Ƿ�����frequency����
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
		//��������óɺ͸߶�һ��
		super.onMeasure(heightMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//���������Ӧ����boardDraw�ĳ�ʼ��Ӧ����BoardView�Ĺ��캯������
		//����boardDraw�ĳ�ʼ����Ҫ����canvas���������������ʼ������
		if(boardDraw==null){
			int padding=20;//�����������߿�ĳ���
			//ÿ�����ӵĿ��,19*19·�Ļ�������ӵ�������18
			//�ֳ�18+1�������ڣ�����1���Ǳ߽����
			int width=(this.getWidth()-padding*2)/18;
			//Ϊ�����߶��룬��padding��������
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
	 * �ػ���������
	 */
	private void drawChess(){
		Position temp=null;
		int color=0;
		int size=this.board.manual.size();
		int i=1;
		//������������
		for(;i<=size;i++){
			temp=this.board.manual.getPositionOf(i);
			color=this.board.manual.getColorIndexOf(i);
			boardDraw.drawChess(temp,color);
		}
		//�������4�����ӵĺ��
		//��Ҫ��ǵ�����ߵļ��ĵ�����
		int count=(size>=4?4:size)-1;
		for(i=size-count;i<=size;i++){
			temp=this.board.manual.getPositionOf(i);
			color=this.board.manual.getColorIndexOf(i);
			boardDraw.drawPoint(temp, Color.RED);
		}
		//�������2�����ӵ���ʾ��
		count=(size>=2?2:size)-1;
		for(i=size-count;i<=size;i++){
			temp=this.board.manual.getPositionOf(i);
			color=this.board.manual.getColorIndexOf(i);
			boardDraw.drawPoint(temp, Color.RED);
			boardDraw.drawNotice(temp, Color.GREEN);
		}
	}
	
	/**
	 * ���̻�ͼ�࣬���������ӡ������񡢻���ʾ��
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
			
			private int startX;//�������
			private int startY;//�������
			private int blockLength;//���񳤶�
			private int size;//���ĸ�����������Ϊ19*19����size=18
			private Canvas canvas;
			private Paint paint=new Paint();//����
			//������������
			Matrix matrix;
			//����bitmap
			//BitmapFactory��һ�������࣬�����д����ķ����Ӳ�ͬ������Դ������������bitmap����
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
			 * ������
			 */
			public void drawGrid(){
				this.canvas.drawBitmap(this.backgroud_, 1, 1, paint);
				
				paint.setColor(Color.parseColor("#946139"));
				paint.setStrokeWidth(4);
				//���߽���
				canvas.drawLine(startX, startY, startX+size*blockLength, startY, paint);
				canvas.drawLine(startX, startY, startX, startY+size*blockLength, paint);
				canvas.drawLine(startX+size*blockLength, startY, startX+size*blockLength, startY+size*blockLength, paint);
				canvas.drawLine(startX, startY+size*blockLength, startX+size*blockLength, startY+size*blockLength, paint);
				//���ڲ��ָ���
				paint.setStrokeWidth(2);
				for(int i=1;i<=18;i++){
					canvas.drawLine(startX+i*blockLength, startY, startX+i*blockLength, startY+size*blockLength, paint);
				}
				for(int i=1;i<=18;i++){
					canvas.drawLine(startX, startY+i*blockLength, startX+size*blockLength, startY+i*blockLength, paint);
				}
				//��9��Уλ��
				canvas.drawCircle(startX+3*blockLength, startY+3*blockLength, 5, paint);
				canvas.drawCircle(startX+3*blockLength, startY+9*blockLength, 5, paint);
				canvas.drawCircle(startX+3*blockLength, startY+15*blockLength, 5, paint);
				canvas.drawCircle(startX+9*blockLength, startY+3*blockLength, 5, paint);
				canvas.drawCircle(startX+9*blockLength, startY+9*blockLength, 5, paint);
				canvas.drawCircle(startX+9*blockLength, startY+15*blockLength, 5, paint);
				canvas.drawCircle(startX+15*blockLength, startY+3*blockLength, 5, paint);
				canvas.drawCircle(startX+15*blockLength, startY+9*blockLength, 5, paint);
				canvas.drawCircle(startX+15*blockLength, startY+15*blockLength, 5, paint);
				//�������ʾ��ĸ������
				paint.setStrokeWidth(1);
				paint.setColor(Color.BLACK);
				char character;
				for(int i=0;i<19;i++){
					character=(char)('A'+i);
					//��������
					canvas.drawText(i+1+"", startX+i*blockLength, this.startY-5, paint);
					//������ĸ
					canvas.drawText(""+character, startX+i*blockLength, this.startY+size*blockLength+15, paint);
					//��������
					canvas.drawText(i+1+"", startX-15, this.startY+i*blockLength, paint);
					//������ĸ
					canvas.drawText(""+character, startX+size*blockLength+5, this.startY+i*blockLength, paint);
				}
			}
			
			/**
			 * ������
			 * @param pos ��
			 * @param color �����ɫ ����:Position.Color.BLACK
			 */
			public void drawChess(Position pos,int color){
				Bitmap bitmap=(color==Position.Color.BLACK?this.black_:this.white_);
				this.canvas.drawBitmap(bitmap, (pos.x-1)*blockLength+this.startX-blockLength/2, (pos.y-1)*blockLength+this.startY-blockLength/2, paint);
			}
			
			/**
			 * ��colorɫ����ʾ��
			 * @param pos
			 * @param color
			 */
			public void drawPoint(Position pos,int color){
				paint.setColor(color);
				this.canvas.drawCircle((pos.x-1)*blockLength+startX, (pos.y-1)*blockLength+startY, 4, paint);
			}
			
			/**
			 * ����ʾ��
			 * @param pos
			 * @param color ��ʾ�����ɫ ���磺Color.Black
			 */
			public void drawNotice(Position pos,int color){
				paint.setColor(color);
				paint.setStrokeWidth(2);
				int x=(pos.x-1)*blockLength-blockLength/2+this.startX,y=(pos.y-1)*blockLength-blockLength/2+this.startY;
				int len=blockLength/3;
				//����4����
				this.canvas.drawLine(x, y, x+len, y, paint);
				this.canvas.drawLine(x, y+blockLength, x+len, y+blockLength, paint);
				this.canvas.drawLine(x+blockLength-len, y, x+blockLength, y, paint);
				this.canvas.drawLine(x+blockLength-len, y+blockLength, x+blockLength, y+blockLength, paint);
				//����4����
				this.canvas.drawLine(x, y, x, y+len, paint);
				this.canvas.drawLine(x+blockLength, y, x+blockLength, y+len, paint);
				this.canvas.drawLine(x, y+blockLength-len, x, y+blockLength, paint);
				this.canvas.drawLine(x+blockLength, y+blockLength-len, x+blockLength, y+blockLength, paint);
				paint.setColor(color);
			}
			
			/**
			 * ������������ͼƬ�ĸߡ���
			 */
			public void resetChessImageSize(){
				if(matrix==null){
					matrix=new Matrix();
				}
				//�������ӵ����ű���
				matrix.setScale(blockLength/(float)black_cache.getWidth(), blockLength/(float)black_cache.getHeight());
				//��bitmap��������
				black_=Bitmap.createBitmap(black_cache, 0, 0, black_cache.getWidth(), black_cache.getHeight(), matrix, true);
				matrix.setScale(blockLength/(float)white_cache.getWidth(), blockLength/(float)white_cache.getHeight());
				white_=Bitmap.createBitmap(white_cache, 0, 0, white_cache.getWidth(), white_cache.getHeight(), matrix, true);
				matrix.setScale((this.blockLength*this.size+45)/(float)backgroud_cache.getWidth(), (this.blockLength*this.size+45)/(float)backgroud_cache.getHeight());
				backgroud_=Bitmap.createBitmap(backgroud_cache, 0, 0, backgroud_cache.getWidth(), backgroud_cache.getHeight(), matrix, true);
			}
			
			/**
			 * �ж������Ƿ���������
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
			 * �ж��Ƿ�ﵽ������С������Χ
			 * @param recover ����������С��Χ�Ƿ�ָ���С
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
			 * �жϵ�one�Ƿ���another�㸽�� ����������������¼С��length
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
			 * ��������ת�������̵�
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
