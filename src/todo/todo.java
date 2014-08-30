package todo;

public class todo {
	public static void main(String arg[]){
		int input = Integer.parseInt(arg[0]);
		
		System.out.println(add(input));
	}
	
	public static int add(int x){
		return x + 1;
	}
}
