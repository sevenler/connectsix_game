package hong.connectSix.ui;

import hong.connectSix.R;
import hong.connectSix.control.Role;
import hong.connectSix.control.Role.RoleType;
import hong.connectSix.model.Position;
import hong.connectSix.xml.ConfigBean;
import hong.connectSix.xml.SAXParsor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class ConfigModelActivity extends Activity {
	//初始化两个角色
	private Role black=new Role("black", RoleType.HUMAN, Position.Color.BLACK);
	private Role white=new Role("white", RoleType.MACHINE, Position.Color.WHITE);
	//游戏难度
	private int hard;
	
	public int getHard() {
		return hard;
	}

	public void setHard(int hard) {
		this.hard = hard;
	}

	public Role getBlack() {
		return black;
	}

	public void setBlack(Role black) {
		this.black = black;
	}

	public Role getWhite() {
		return white;
	}

	public void setWhite(Role white) {
		this.white = white;
	}

	MuiltGradeListAdapter adapter;
	ExpandableListView exList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SAXParsor parsor=new SAXParsor();
		ConfigBean bean=null;
		try {
			bean=parsor.readXML(R.raw.config_activity_content, ConfigModelActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setContentView(R.layout.config_model_activity1);
		ExpandableListView expandableListView=(ExpandableListView)findViewById(R.id.expandableListView1);
        adapter = new MuiltGradeListAdapter(ConfigModelActivity.this, bean);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);
        expandableListView.setDivider(null);
	}
	
	public void initConnSixUI(){
		//用于启动游戏界面
		Intent intent=new Intent(this,ConnectSixUIActivity.class);
		intent.putExtra("black", black);
		intent.putExtra("white", white);
		startActivity(intent);
	}
	
	//变换颜色
	public void changeColor(int color){
		switch(color){
			case Position.Color.WHITE:
				this.white.type=RoleType.HUMAN;
				this.black.type=RoleType.MACHINE;
				break;
			case Position.Color.BLACK:
				this.black.type=RoleType.HUMAN;
				this.white.type=RoleType.MACHINE;
				break;
		}
	}
}
