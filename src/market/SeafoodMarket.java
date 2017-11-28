package market;

import java.util.ArrayList;
import java.util.Date;

import resources.FishSpecies;
import resources.SeaCreature;
import resources.ShellfishSpecies;


/*
 
 -Make prices change at a set interval of time. (Example: Every 10 minutes, make the price for any item go up or down percentage, depending on how stable the market is.)
 
 - Use the Constants class to get the market fluctuation value, and how long before prices expire.
 
 */
public class SeafoodMarket extends Market<SeaCreature, Enum>
{

	public SeafoodMarket(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getCurrentPricePerPound(Enum species)
	{
		if(species instanceof ShellfishSpecies)
		{
			return 2;
		}
		if(species instanceof FishSpecies)
		{
			return 3;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public String getMarketType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double sellItem(SeaCreature items)
	{
		// TODO Auto-generated method stub
		return 2;
	}
	
	public Date getNextPriceChange()
	{
		return new Date();
	}


}
