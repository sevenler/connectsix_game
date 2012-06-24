package hong.connectSix.ui;


import hong.connectSix.R;
import hong.connectSix.xml.ConfigBean;
import hong.connectSix.xml.ConfigBean.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MuiltGradeListAdapter extends BaseExpandableListAdapter {
      private LayoutInflater layoutInflater;
      private ConfigModelActivity mContext;
      private ConfigBean configBean; 
      
      
	public MuiltGradeListAdapter(Context mContext,ConfigBean configBean){
		this.mContext=(ConfigModelActivity)mContext;
		this.configBean=configBean;
		layoutInflater = LayoutInflater.from(mContext);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return configBean.getModels().get(groupPosition).getItems().get(childPosition).getTitle();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView ==null){
			convertView = layoutInflater.inflate(R.layout.expend_child_list, null);
		}
		final ImageView head=(ImageView)convertView.findViewById(R.id.child_image);
		   head.setImageResource(configBean.getModels().get(groupPosition).getItems().get(childPosition).getImage());
		final TextView title=(TextView)convertView.findViewById(R.id.child_title);
		   title.setText(configBean.getModels().get(groupPosition).getItems().get(childPosition).getTitle());
	    final RadioButton radioButton1 =(RadioButton)convertView.findViewById(R.id.radio_button1);
	       radioButton1.setText(configBean.getModels().get(groupPosition).getItems().get(childPosition).getReadios().get(0).getText());
	       configBean.getModels().get(groupPosition).getItems().get(childPosition).setChecked(
	    		   configBean.getModels().get(groupPosition).getItems().get(childPosition).getReadios().get(0).getValue());
	       
	       try {
				radioButton1.setTag(configBean.getModels().get(groupPosition).getItems().get(childPosition).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
	       
	       radioButton1.setOnClickListener(new OnRadioClickListener());
	    final RadioButton radioButton2 =(RadioButton)convertView.findViewById(R.id.radio_button2);
	       radioButton2.setText(configBean.getModels().get(groupPosition).getItems().get(childPosition).getReadios().get(1).getText());
	       configBean.getModels().get(groupPosition).getItems().get(childPosition).setChecked(
	    		   configBean.getModels().get(groupPosition).getItems().get(childPosition).getReadios().get(1).getValue());
	       try {
				radioButton2.setTag(configBean.getModels().get(groupPosition).getItems().get(childPosition).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
	       radioButton2.setOnClickListener(new OnRadioClickListener());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return configBean.getModels().get(groupPosition).getItems().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return configBean.getModels().get(groupPosition).getItems();
	}

	@Override
	public int getGroupCount() {
		return configBean.getModels().size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=layoutInflater.inflate(R.layout.expend_parent_list, null);
		}
		final TextView parentTitle = (TextView) convertView.findViewById(R.id.parent_title);
			parentTitle.setText(configBean.getModels().get(groupPosition).getTitle());
		final ImageView parentImage = (ImageView) convertView.findViewById(R.id.parent_image_title);
			parentImage.setImageResource(configBean.getModels().get(groupPosition).getImage());
		final Button button=(Button)convertView.findViewById(R.id.start);
			button.setOnClickListener(new OnStartClickListener());
		return convertView;
	}
	
	/**
	 * 按钮监听函数
	 * @author Hong
	 *
	 */
	private class OnStartClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			System.out.println("getWhite:"+mContext.getWhite());
			System.out.println("getBlack:"+mContext.getBlack());
			mContext.initConnSixUI();
		}
	}
	
	private class OnRadioClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Item value=(Item)v.getTag();
			switch(value.getId()){
				//设置难度
				case 1:
					mContext.setHard(value.getChecked());
					break;
				//设置颜色
				case 2:
					System.out.println("getChecked:"+value.getChecked());
					mContext.changeColor(value.getChecked());
					break;
			}
		}
		
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
}
	
