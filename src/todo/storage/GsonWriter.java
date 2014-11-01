package todo.storage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import todo.model.Item;
import todo.model.ItemList;

import com.google.gson.Gson;

public class GsonWriter {
	public GsonWriter() {

	}

	public void write(ItemList il) throws IOException {
		Writer writer = new FileWriter("Output.json");

		Gson gson = new Gson();
		gson.toJson(il, writer);

		ItemStaticAttributes itemStaticAttributes = new ItemStaticAttributes();
		itemStaticAttributes.setItemQty(Item.getItemQty());
		itemStaticAttributes.setLastItemId(Item.getLastItemId());

		gson.toJson(itemStaticAttributes, writer);

		writer.close();
	}
}
