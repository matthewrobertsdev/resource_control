package graphicclasses;

import catchgame.Constants;
import resources.Fish;


public class FishGraphic extends AbstractSeaCreatureGraphic{
	private Fish fish;
	private FishImageView fishImageView;
	
	public FishGraphic(Fish fish){
		this.fish=fish;
		
		fishImageView=new FishImageView(fish);
		super.seaCreatureImage=AbstractSeaCreatureGraphic.getImage(fish.getSpecies());
		System.out.println("got image");
		fishImageView.setImage(super.seaCreatureImage);
		System.out.println("set image");
		short WEIGHT_GRAPHIC_MULTIPLE=10;
		switch (fish.getSpecies()){
		case COD:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		case SALMON:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		case TUNA:
			WEIGHT_GRAPHIC_MULTIPLE=Constants.SEACREATURE_WEIGHT_GRAPHIC_MULTIPLE;
			break;
		}

		fishImageView.setFitWidth(fish.getWeight()*WEIGHT_GRAPHIC_MULTIPLE);
		fishImageView.setFitHeight(this.seaCreatureImage.getHeight()*fishImageView.getFitWidth()/this.seaCreatureImage.getWidth());
		fishImageView.setSmooth(true);
		fishImageView.setCache(true);
		


	}
	
	public FishImageView getFishImageView(){
		return fishImageView;
	}
	

	/*
	protected ImageView seaCreatureImageView=new ImageView();
	protected Image seaCreatureImage;
	
	public static final Image getImage(final Enum<?> e)
	{
		for(int i = 0; i < Constants.SUPPORTED_SPECIES.length; i++ )
		{
			if(e == Constants.SUPPORTED_SPECIES[i])
			{
				return (new Image("img/" + Constants.SUPPORTED_SPECIES[i].toString().toLowerCase() + ".png"));
			}
		}
		
		return null;	
	}
	
	public ImageView getSeaCreatureImageView(){
		return seaCreatureImageView;
	}
	*/

}
