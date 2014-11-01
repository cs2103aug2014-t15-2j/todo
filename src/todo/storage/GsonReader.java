package todo.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import todo.model.Item;
import todo.model.ItemList;

import com.google.gson.Gson;

public class GsonReader {
	public GsonReader() {

	}

	public ItemList read() throws FileNotFoundException {
		File file = new File("Output.json");
		if (!file.exists()) {
			return new ItemList();
		}

		BufferedReader br = new BufferedReader(new FileReader(file));
		Gson gson = new Gson();
		ItemList il = gson.fromJson(br, ItemList.class);
		ItemStaticAttributes itemStaticAttributes = gson.fromJson(br,
				ItemStaticAttributes.class);
		Item.setItemQty(itemStaticAttributes.getItemQty());
		Item.setLastItemID(itemStaticAttributes.getLastItemId());

		return il;
	}
}
