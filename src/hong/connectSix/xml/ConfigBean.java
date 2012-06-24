package hong.connectSix.xml;

import java.util.ArrayList;
import java.util.List;

public class ConfigBean {
	public class Model{
		private int model;
		private String title;
		private int image;
		private List<Item> items=new ArrayList<ConfigBean.Item>();
		public int getModel() {
			return model;
		}
		public void setModel(int model) {
			this.model = model;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getImage() {
			return image;
		}
		public void setImage(int image) {
			this.image = image;
		}
		public List<Item> getItems() {
			return items;
		}
		public void setItems(List<Item> items) {
			this.items = items;
		}
		@Override
		public String toString() {
			return "Model [model=" + model + ", title=" + title + ", image="
					+ image + ", items=" + items + "]";
		}
	}
	public class Item{
		private String title;
		private int image;
		private int id;
		private int checked;
		public int getChecked() {
			return checked;
		}
		public void setChecked(int checked) {
			this.checked = checked;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		private List<Radio> readios=new ArrayList<ConfigBean.Radio>();
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getImage() {
			return image;
		}
		public void setImage(int image) {
			this.image = image;
		}
		public List<Radio> getReadios() {
			return readios;
		}
		public void setReadios(List<Radio> readios) {
			this.readios = readios;
		}
		@Override
		public String toString() {
			return "Item [title=" + title + ", image=" + image + ", readios="
					+ readios + "]";
		}
		@Override
		public Object clone() throws CloneNotSupportedException {
			Item item=new Item();
			item.setChecked(checked);
			item.setId(id);
			item.setImage(image);
			item.setReadios(readios);
			item.setTitle(title);
			return item;
		}
	}
	public class Radio{
		private String text;
		private int value;
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return "Radio [text=" + text + ", value=" + value + "]";
		}
	}
	private List<Model> models=new ArrayList<ConfigBean.Model>();
	public List<Model> getModels() {
		return models;
	}
	public void setModels(List<Model> models) {
		this.models = models;
	}
	@Override
	public String toString() {
		return "ConfigBean [models=" + models + "]";
	}
}
