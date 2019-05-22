import java.util.ArrayList;
import java.util.List;

public class Root 
{
	private String city;
	private List<String> root;
	
	public Root()
	{
		root = new ArrayList<String>();
	}
	
	public void setNode(String Node)
	{
		this.city = Node;
	}
	
	public String getNode()
	{
		return this.city;
	}
	
	public Boolean exists(String city)
	{
		if(root.contains(city))
			return true;
		return false;
	}
	
	public void delete(String city)
	{
		root.remove(city);
	}
	
	public void add(String city)
	{
		root.add(city);
	}
}
