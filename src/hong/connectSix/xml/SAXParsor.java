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
		SAXParserFactory spf=SAXParserFactory.newInstance();//����SAX��������������
		SAXParser saxParser=spf.newSAXParser();//ʹ�ý�������������������ʵ��
		  
		//����SAX������Ҫʹ�õ��¼�����������
		StudentSAXHandler handler=new StudentSAXHandler(mContext);
		saxParser.parse(mContext.getResources().openRawResource(xml),handler);//��ʼ�����ļ�
		return handler.getResult();//��ȡ���
	}
 
	 /**
	  * SAX���¼����������������ض���XML�ļ���ʱ�򣬾���ҪΪ
	  * �䴴��һ���̳�DefaultHandler�����������ض����¼�
	  * ����˵����ʵ���Ͼ���SAX����XML�ļ��ĺ���
	  * @author Hong
	  *
	  */
	 static class StudentSAXHandler extends DefaultHandler{
		 /* //�����Ѿ�����������û�йرյı�ǩ
		  Stack tagsStack=new Stack();
		  List studentBeans=new ArrayList();
		  StudentBean bean=null;*/
		 
		  //�����Ѿ�����������û�йرյı�ǩ
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
		  
		/**�������ĵ��Ŀ�ͷ��ʱ�򣬵������������������������һЩԤ����Ĺ���*/
		  public void startDocument() throws SAXException{
			  System.out.println("--------Parse begin---------");
		  }
		  /**���ĵ�������ʱ�򣬵������������������������һЩ�ƺ�Ĺ���*/
		  public void endDocument() throws SAXException{
			  System.out.println("--------Parse end-----------");
		  }
		  
		  /**
		   * ������һ����ʼ��ǩ��ʱ�򣬻ᴥ���������
		   * @param namespaceURI ��������
		   * @param localName ��ǩ��
		   * @param qName ��ǩ������ǰ׺
		   * @param atts �����ǩ�������������б�
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
					  //�����õ�img�ַ���ת��Ϊint����
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
					  //�����õ�img�ַ���ת��Ϊint����
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
		  
		  /**������������ǩ��ʱ�򣬵����������*/
		  public void endElement(String namespaceURI,String localName,String qName)throws SAXException{
			  String currenttag=(String)tagsStack.pop();//�������ȡ�ı�ǩ����
			  if(!currenttag.equals(qName)){
				  throw new SAXException("XML�ĵ���ʽ����ȷ����ǩ��ƥ��");
			  }
			  System.out.println("---------Processing element end--------,qName:"+qName);
		  }
		  
		  /**����XML�ļ��ж������ַ���*/
		  public void characters(char[] chs,int start,int length)throws SAXException{
		  }
		  
		  public ConfigBean getResult(){
			  return bean;
		  }
	 }
}
