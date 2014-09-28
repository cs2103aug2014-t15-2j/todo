package todo.model;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
public class ItemTest {
	String item1 = "item-1";
	String item2 = "item-2";
	String item3 = "item-3";
	Date currentDate1 = new Date();
	DateTime testDateTime1 = new DateTime(currentDate1);
	Item abc = new Item (item1,testDateTime1);
	
	Date currentDate2 = new Date();
	DateTime testDateTime2 = new DateTime(currentDate2);
	Date currentDate3 = new Date();
	DateTime testDateTime3 = new DateTime(currentDate3);
	
	@Test
	public void testItemConstructor() {
		Item def = new Item (item1,testDateTime1);
		System.out.println(Item.getItemQty());
		assertEquals(abc.getDescription(),def.getDescription());
		assertEquals(abc.getStartDateTime(),def.getStartDateTime());
		assertEquals(Item.getItemQty(),2);
		
	}
	
	@Test
	public void testStaticAttributes() {
		System.out.println(Item.getItemQty());
		Item ghi = new Item(item3,testDateTime3);
		System.out.println(Item.getItemQty());
		assertEquals(Item.getItemQty(),3);
		assertEquals(Item.getLastItemId(),3);
	}
	
	
	@Test
	public void testTagsInput() {
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		abc.setTags(tagList);
		assertEquals(tagList,abc.getTags());
		abc.displayTagList();
	}
	
}
