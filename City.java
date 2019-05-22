import java.util.Comparator;
public class City implements Comparator<City>
{
	public int cost;
	public String city;
	
	public City()
	{ }
	
	public City(String city, int cost)
	{
		this.city = city;
		this.cost = cost;
	}
	
	public int compare(City n1, City n2)
	{
		if(n1.cost < n2.cost)
		{
			return -1;
		}
		if(n1.cost > n2.cost)
		{
			return 1;
		}
		return 0;
	}
	

}
