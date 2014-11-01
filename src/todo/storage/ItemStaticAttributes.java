package todo.storage;

import todo.model.Item;

/**
 * This class maintains two Item's static attributes, which are itemQty and
 * lastItemId. This class is used to cater for GSON's toJson and fromJson
 * methods.
 * 
 * @author Lui
 *
 */
public class ItemStaticAttributes {
	private int itemQty;
	private int lastItemId;

	public ItemStaticAttributes() {
		itemQty = Item.getItemQty();
		lastItemId = Item.getLastItemId();
	}

	// getters
	public int getItemQty() {
		return this.itemQty;
	}

	public int getLastItemId() {
		return this.lastItemId;
	}

	// setters
	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}

	public void setLastItemId(int lastItemId) {
		this.lastItemId = lastItemId;
	}
}
