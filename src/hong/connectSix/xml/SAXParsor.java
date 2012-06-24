package hong.connectSix.xml;

import hong.connectSix.xml.ConfigBean.Item;
import hong.connectSix.xml.ConfigBean.Model;
import hong.connectSix.xml.ConfigBean.Radio;

import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

public class SAXParsor {

	public ConfigBean readXML(int xml,Context mContext) throws Exception{
		SAXParserFactory spf=SAXParserFactory.newInstance();//创建SAX解析器工厂对象
		SAXParser saxParser=spf.newSAXParser();//使用解析器工厂创建解析器实例
		  
		//创建SAX解析器要使用的事件监听器对象
		StudentSAXHandler handler=new StudentSAXHandler(mContext);
		saxParser.parse(mContext.getResources().openRawResource(xml),handler);//开始解析文件
		return handler.getResult();//获取结果
	}
 
	 /**
	  * SAX的事件监听器，当处理特定的XML文件的时候，就需要为
	  * 其创建一个继承DefaultHandler的类来处理特定的事件
	  * 可以说，这实际上就是SAX处理XML文件的核心
	  * @author Hong
	  *
	  */
	 static class StudentSAXHandler extends DefaultHandler{
		 /* //保存已经读到过但还没有关闭的标签
		  Stack tagsStack=new Stack();
		  List studentBeans=new ArrayList();
		  StudentBean bean=null;*/
		 
		  //保存已经读到过但还没有关闭的标签
		  private Stack<String> tagsStack=new Stack<String>();
		  private ConfigBean bean=null;
		  private Model  model=null;
		  private Item item=null;
		  private Radio radio=null;
		  private Context mContext;
		  
		  
		public StudentSAXHandler(Context mContext) {
			super();
			this.mContext = mContext;
		  }
		  
		/**当遇到文档的开头的时候，调用这个方法，可以在其中做一些预处理的工作*/
		  public void startDocument() throws SAXException{
			  System.out.println("--------Parse begin---------");
		  }
		  /**当文档结束的时候，调用这个方法，可以在其中做一些善后的工作*/
		  public void endDocument() throws SAXException{
			  System.out.println("--------Parse end-----------");
		  }
		  
		  /**
		   * 当读到一个开始标签的时候，会触发这个方法
		   * @param namespaceURI 就是名域
		   * @param localName 标签名
		   * @param qName 标签的修饰前缀
		   * @param atts 这个标签所包含的属性列表
		   * @throws SAXException
		   */
		  public void startElement(String namespaceURI,String localName,String qName,Attributes atts)throws SAXException{
			  tagsStack.push(qName);
			  if(bean==null){
				  if(qName.equals("config")){
					  bean=new ConfigBean();
				  }
			  }else{
				  if(qName.equals("model")){
					  model=bean.new Model();
					  bean.getModels().add(model);
					  model.setTitle(atts.getValue("title"));
					  String imageStr=atts.getValue("image");
					  //将配置的img字符串转化为int类型
					  int image = mContext.getResources().getIdentifier(imageStr, "drawable", mContext.getPackageName());
					  model.setImage(image);
					  model.setModel(Integer.parseInt(atts.getValue("value")));
				  }else if(qName.equals("item")){
					  item=bean.new Item();
					  model.getItems().add(item);
					  item.setChecked(Integer.parseInt(atts.getValue("checked")));
					  item.setId(Integer.parseInt(atts.getValue("id")));
					  item.setTitle(atts.getValue("title"));
					  String imageStr=atts.getValue("image");
					  //将配置的img字符串转化为int类型
					  int image = mContext.getResources().getIdentifier(imageStr, "drawable", mContext.getPackageName());
					  item.setImage(image);
				  }else if(qName.equals("redio")){
					  radio=bean.new Radio();
					  item.getReadios().add(radio);
					  radio.setText(atts.getValue("text"));
					  radio.setValue(Integer.parseInt(atts.getValue("value")));
				  }
			  }
		  }
		  
		  /**在遇到结束标签的时候，调用这个方法*/
		  public void endElement(String namespaceURI,String localName,String qName)throws SAXException{
			  String currenttag=(String)tagsStack.pop();//将最近读取的标签弹出
			  if(!currenttag.equals(qName)){
				  throw new SAXException("XML文档格式不正确，标签不匹配");
			  }
			  System.out.println("---------Processing element end--------,qName:"+qName);
		  }
		  
		  /**处理XML文件中读到的字符串*/
		  public void characters(char[] chs,int start,int length)throws SAXException{
		  }
		  
		  public ConfigBean getResult(){
			  return bean;
		  }
	 }
}
