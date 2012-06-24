package hong.connectSix.ui;

import hong.connectSix.R;
import hong.connectSix.control.Role;
import hong.connectSix.control.Role.OnFinishedListener;
import hong.connectSix.control.Role.RoleType;
import hong.connectSix.model.Board;
import hong.connectSix.model.Manual;
import hong.connectSix.model.Position;
import hong.connectSix.model.Step;
import hong.connectSix.wight.BoardView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


public class ConnectSixUIActivity extends Activity {
	private ImageButton backButton=null;
	private ImageButton pauseButton=null;
	private ImageButton restartButton=null;
	private ProgressBar progressBar=null;
	//棋盘控件
	private BoardView boardView=null;
	//黑白角色
	private Role black;
	private Role white;
	//消息处理实例
	private MessageHandler messageHandler=new MessageHandler();
	//数据棋盘
	private Board board=null;
	//某方胜利后记录胜利的走子
	private Position posInWin=null;
	//控制器
	private Controler controler=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//获取配置的对弈角色
        Intent intent=getIntent();
        this.black=(Role)intent.getSerializableExtra("black");
        this.white=(Role)intent.getSerializableExtra("white");
        
        //获取控件
        boardView=(BoardView)findViewById(R.id.board);
        backButton=(ImageButton)findViewById(R.id.button_back);
        pauseButton=(ImageButton)findViewById(R.id.button_pause);
        pauseButton.setImageResource(android.R.drawable.ic_media_play);
        restartButton=(ImageButton)findViewById(R.id.button_restart);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        boardView=(BoardView)findViewById(R.id.board);
        //初始化控制器
        controler=new Controler();
		board=Board.getInstance();
		boardView.setBoard(board);
		boardView.setMessageHandler(messageHandler);
       //回退按钮监听绑定
        backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				messageHandler.sendMessage(Messages.PAUSE, null);
				messageHandler.sendMessage(Messages.BACK, null);
				messageHandler.sendMessage(Messages.INVAILDATE, null);
			}
		});
        //暂停按钮监听绑定
        pauseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(controler.isPause)
					messageHandler.sendMessage(Messages.PLAY, null);
				else messageHandler.sendMessage(Messages.PAUSE, null);
			}
		});
        //重新开始按钮监听绑定
        restartButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
				messageHandler.sendMessage(Messages.RESTART, 0);
				messageHandler.sendMessage(Messages.INVAILDATE, 0);
        	}
        });
        //为电脑角色添加完成搜索处理监听器
        if(black.type==RoleType.MACHINE){
        	black.setOnFinishedListener(new OnFinishedListener() {
				@Override
				public void onResult(String result) {
					Step step=Step.toInt(result);
					if(step.one!=null)messageHandler.sendMessage(Messages.GO,step.one);
					if(step.another!=null)messageHandler.sendMessage(Messages.GO,step.another);
					messageHandler.sendMessage(Messages.FINISHPROGRESS,null);
				}
			});
        }
        if(white.type==RoleType.MACHINE){
        	white.setOnFinishedListener(new OnFinishedListener() {
				@Override
				public void onResult(String result) {
					Step step=Step.toInt(result);
					if(step.one!=null)messageHandler.sendMessage(Messages.GO,step.one);
					if(step.another!=null)messageHandler.sendMessage(Messages.GO,step.another);
					messageHandler.sendMessage(Messages.FINISHPROGRESS,null);
				}
			});
        }
        toPlay(black,false);
	}
	
	/**
	 * 提示信息
	 * @param color
	 */
	public void showText(String text){
		Toast toast=Toast.makeText(this, text, Toast.LENGTH_LONG);
		toast.show();
	}
	
	/**
	 * 获取走棋方角色
	 * @return
	 */
	public Role getShouldPlayRole(){
		int color=this.board.manual.getWhoShouldPlay();
		return color==Position.Color.BLACK?this.black:this.white;
	}
	
	public void toPlay(Role role,boolean moveLastStep){
		System.out.println("to play role:"+role);
		if(role.type==Role.RoleType.MACHINE && controler.isStart && 
				!controler.isPause && !controler.isGameOver){
			System.out.println(role.color+" progress...");
			//发送正在搜索的消息
			messageHandler.sendMessage(Messages.PROGRESING,0);
			Step step=board.manual.peekStep();
			if(step==null) {
				if(moveLastStep)
					role.move(Manual.EMPTYMANUAL);
				role.getBest();
			}
			else{
				if(moveLastStep)
					role.move(step.toChars());
				role.getBest();
			}
		}
	}
	
	public interface Messages{
		public static final int WHAT=0x123;//消息的标志
		
		public static final int DEFAUL=0;//默认的空消息
		
		public static final int INVAILDATE=1;//重绘
		public static final int WIN=2;//某方获胜
		public static final int PAUSE=3;//暂停
		public static final int BACK=4;//撤销走步
		public static final int PLAY=5;//开始
		public static final int PROGRESING=6;//机器搜索中
		public static final int FINISHPROGRESS=7;//机器完成搜索
		public static final int GO=8;//走子消息
		public static final int RESTART=9;//重新开始
	}
	
	public class MessageHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==Messages.WHAT){
				switch(msg.arg1){
					case Messages.INVAILDATE:
						boardView.invalidate();
						break;
					case Messages.PAUSE:
						pauseButton.setImageResource(android.R.drawable.ic_media_pause);
						controler.pause();
						break;
					case Messages.BACK:
						//撤销走子
						Step step=board.manual.peekStep();
						System.out.println("Back step:"+step);
						if(step==null) return;
						Position po=null;
						int back_count=0;
						if(step.one!=null) {
							back_count++;
							po=board.makeMove.unmakeMovePosition();
							//回退子时，退到导致某方获胜的子时，恢复isGameOver状态
							if(po.equals(posInWin))
								controler.isGameOver=false;
						}
						if(step.another!=null) {
							back_count++;
							po=board.makeMove.unmakeMovePosition();
							if(po.equals(posInWin))
								controler.isGameOver=false;
						}
						//如果回退1个走子，这时引擎还没有进行这个点的走子,则是无需进行引擎回退走子的
						if(!(back_count==1 && board.manual.size()!=0)){
							//调用机器角色做退子操作
							if(white.type==RoleType.MACHINE){
								white.back();
							}
							if(black.type==RoleType.MACHINE){
								black.back();
							}
						}
						break;
					case Messages.PLAY:
						pauseButton.setImageResource(android.R.drawable.ic_media_play);
						controler.start();
						toPlay(getShouldPlayRole(),false);
						break;
					case Messages.WIN:
						//输出一方获胜
						int color=(Integer)msg.obj;
						String text=(color==Position.Color.WHITE?
								getResources().getText(hong.connectSix.R.string.white_win).toString():
									getResources().getText(hong.connectSix.R.string.black_win).toString());
						showText(text);
						break;
					case Messages.PROGRESING:
						progressBar.setVisibility(ProgressBar.VISIBLE);
						break;
					case Messages.FINISHPROGRESS:
						progressBar.setVisibility(ProgressBar.GONE);
						break;
					case Messages.GO:
						Position pos=(Position)msg.obj;
						System.out.println("GO Position:"+pos);
						Role play=getShouldPlayRole();
						boolean old=controler.isGameOver;
						try{
							controler.isGameOver=board.makeMove.makeMovePosition(pos, play.color)||controler.isGameOver;
						}catch(IllegalArgumentException e){
							System.out.println("GO Message move postion "+pos+" is wrong:"+e.getMessage());
							throw new IllegalArgumentException(e.getMessage());
						}
						messageHandler.sendMessage(Messages.INVAILDATE, play.color);
						if(controler.isGameOver && !old){
							posInWin=pos;
							//发送某方胜利的消息
							messageHandler.sendMessage(Messages.WIN, play.color);
						}
						Role newPlay=getShouldPlayRole();
						System.out.println("newPlay:"+newPlay+" play:"+play);
						if(!newPlay.equals(play)){
							toPlay(newPlay,true);
						}
						System.out.println("Manual:"+board.manual.toCharsSeparation(','));
						break;
					case Messages.RESTART:
						//重新初始化数据
		        		board.reset();
		        		boardView.setBoard(board);
		        		controler.reStart();
		        		//调用机器角色做退子操作
						if(white.type==RoleType.MACHINE){
							white.reStart();
						}
						if(black.type==RoleType.MACHINE){
							black.reStart();
						}
						toPlay(black,false);
						break;
				}
			}
			super.handleMessage(msg);
		}
		
		/**
		 * 发送消息重绘窗口
		 * @param arg1 
		 * @param obj 
		 */
		public void sendMessage(int arg1,Object obj){
			String text="";
			switch(arg1){
				case Messages.BACK: 
					text="BACK"; break;
				case Messages.FINISHPROGRESS:
					text="FINISHPROGRESS"; break;
				case Messages.GO:
					text="GO"; break;
				case Messages.INVAILDATE:
					text="INVAILDATE";break;
				case Messages.PAUSE:
					text="PAUSE";break;
				case Messages.PLAY:
					text="PLAY";break;
				case Messages.PROGRESING:
					text="PROGRESING";break;
				case Messages.WIN:
					text="WIN";break;
				case Messages.RESTART:
					text="RESTART";break;	
			}
			System.out.println("send message: "+ text);
			Message message=new Message();
			message.what=Messages.WHAT;
			message.arg1=arg1;
			message.obj=obj;
			this.sendMessage(message);
		}
	}
	
	public class Controler {
		public boolean isStart=true;
		public boolean isPause=false;
		public boolean isGameOver=false;
		
		public void start(){
			isStart=true;
			isPause=false;
		}
		
		public void reStart(){
			isStart=true;
			isPause=false;
			isGameOver=false;
		}
		
		public void pause(){
			isPause=true;
		}
		
		public void stop(){
			isPause=true;
			isStart=false;
			isGameOver=true;
		}
	}
	
	
}