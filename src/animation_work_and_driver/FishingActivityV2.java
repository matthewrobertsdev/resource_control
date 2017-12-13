package animation_work_and_driver;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
/*
Class by Dr. Java and the JavaDocs
Nils Johnson, Caileigh Fitzgerald, Thanh Lam, and Matt Roberts
Date: 11-27-2017
*/
/*
Purpose: to allow the user to catch SeaCreatures by getting
SeaCreaturesfrom the server and adding them to the user's
SimpleFishingPane with onClick event handler's theat extract
a fish when the fish's GUI is clicked on and add it to
the Player's IceChest.  SeaCreatures are currently rendered
with circles colored by species and sized by SeaCreature's
weight.

Modification Info:
These methods were originally found in GameControl.
Colors have been added, and the ability to get all SeaCreatures
(as opposed to just cod) has been added.  The ability to add the
SeaCreature to the player's ice chest is new too.
*/
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import catchgame.Constants;
import catchgame.Ocean;
import catchgame.Packets.ClientSubOceanSeaCreatureStatePacket;
import catchgame.Packets.SeaCreaturesPacket;
import catchgame.Player;
import graphicclasses.FishImageView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import resources.Fish;
import resources.FishSpecies;
import resources.Shellfish;
import resources.ShellfishSpecies;
import userinterface.GamePane;
import userinterface.SimpleFishingPane;
import utilities.ArrayListUtilities;
import utilities.NumberUtilities;

/**
 * This is responsible for sending info to the server about the ClientSubOcean
 * conditions, receiving SeaCreature packets, adding them to the screen, and
 * providing action events for when a seaCreature is caught.
 * 
 * @author Matt Roberts
 *
 */
public class FishingActivityV2 {

	private SimpleFishingPane simpleFishingPane;

	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;

	private Player player;

	private ClientSubOcean clientSubOcean;
	ClientFishingActivityFishManagerV2 clientFishingActivityFishManager;

	// for debugging
	Ocean ocean = new Ocean();

	/**
	 * This constructs a FishingActivity by putting SeaCreatures on the screen.
	 * 
	 * @param gamePane
	 *            the gamepane where the FishingActivity's UI will be held
	 * @param toServer
	 *            an ObjectOutputStream to the server
	 * @param fromServer
	 *            an ObjectInputStream from the server
	 * @param player
	 *            the player who will be affected by the user fishing
	 * @throws IOException
	 */
	public FishingActivityV2(SimpleFishingPane simpleFishingPane, 
			Player player) {
		/*
		ImageView iv=new ImageView();
		Image i=new Image("img/cod.png");
		iv.setImage(i);
		iv.setFitWidth(300);
		*/
		clientSubOcean = new ClientSubOcean();
		clientFishingActivityFishManager = new ClientFishingActivityFishManagerV2(simpleFishingPane);
		this.simpleFishingPane = simpleFishingPane;
		this.player = player;
		this.toServer = toServer;
		this.fromServer = fromServer;
		testAddFishToFishingActivity();
		clientFishingActivityFishManager.doBasicClientSubOceanAnimation();
		//simpleFishingPane.getChildren().add(iv);
	}
	 
	public void testAddFishToFishingActivity() {
		try {
			ArrayList<Fish> sampleCod = ocean.extractAndReturnABunchOfFish(FishSpecies.COD,
					(clientSubOcean.currentPopulationCod + 90), clientSubOcean.maxPopulationCod);
			clientFishingActivityFishManager.codPopulation.addAll(sampleCod);
			ArrayList<Fish> sampleSalmon = ocean.extractAndReturnABunchOfFish(FishSpecies.SALMON,
					(clientSubOcean.currentPopulationSalmon + 60), clientSubOcean.maxPopulationSalmon);
			clientFishingActivityFishManager.salmonPopulation.addAll(sampleSalmon);
			ArrayList<Fish> sampleTuna = ocean.extractAndReturnABunchOfFish(FishSpecies.TUNA,
					(clientSubOcean.currentPopulationTuna + 60), clientSubOcean.maxPopulationTuna);
			clientFishingActivityFishManager.tunaPopulation.addAll(sampleTuna);
			ArrayList<Shellfish> sampleLobsters = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.LOBSTER,
					(clientSubOcean.currentPopulationLobster + 20), clientSubOcean.maxPopulationLobster);
			clientFishingActivityFishManager.lobsterPopulation.addAll(sampleLobsters);
			ArrayList<Shellfish> sampleCrab = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.CRAB,
					(clientSubOcean.currentPopulationCrab + 60), clientSubOcean.maxPopulationCrab);
			clientFishingActivityFishManager.crabPopulation.addAll(sampleCrab);
			ArrayList<Shellfish> sampleOysters = ocean.ecxtractAndReturnABunchOfShellfish(ShellfishSpecies.OYSTER,
					(clientSubOcean.currentPopulationOyster + 40), clientSubOcean.maxPopulationOyster);
			clientFishingActivityFishManager.oysterPopuliation.addAll(sampleOysters);
			addFishPacketToScreen(sampleCod, Constants.DISTANCE_FROM_TOP, 
					(int)simpleFishingPane.getHeight()/4+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT);
			addFishPacketToScreen(sampleSalmon, Constants.DISTANCE_FROM_TOP, 
					(int)simpleFishingPane.getHeight()/4+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT);
			addFishPacketToScreen(sampleTuna, Constants.DISTANCE_FROM_TOP, 
					(int)simpleFishingPane.getHeight()/4+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT);
			addShellfishPacketToScreen(sampleLobsters, (int)simpleFishingPane.getHeight()*3/4+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT, Constants.DISTANCE_FROM_BOTTOM);
			addShellfishPacketToScreen(sampleCrab, (int)simpleFishingPane.getHeight()*3/4+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT, Constants.DISTANCE_FROM_BOTTOM);
			addShellfishPacketToScreen(sampleOysters, (int)simpleFishingPane.getHeight()*3/4+Constants.ONE_HALF_FISH_SHELLFISH_SEPERATION_HEIGHT, Constants.DISTANCE_FROM_BOTTOM);
		} catch (Exception e) {

		}
	}

	/**
	 * Adds fish to the screen between the topOffset and bottomOffset and of the
	 * given color
	 * 
	 * @param fishUpdate
	 *            the fish to add to the screen
	 * @param topOffset
	 *            the highest the fish can be on the screen
	 * @param bottomOffset
	 *            the lowest the fish can be on the screen
	 * @param color
	 *            the color the fish should be given
	 */
	public void addFishPacketToScreen(ArrayList<Fish> fishUpdate, int topOffset, int bottomOffset) {
		for (int i = 0; i <= fishUpdate.size() - 1; i++) {
			addFishToScreen(fishUpdate.get(i), topOffset, bottomOffset);
		}
	}

	/**
	 * Adds shellfish to the screen between the topOffset and bottomOffset and
	 * of the given color
	 * 
	 * @param shellfishUpdate
	 *            the shellfish to add to the screen
	 * @param topOffset
	 *            the highest the shellfish can be on the screen
	 * @param bottomOffset
	 *            the lowest the shellfish can be on the screen
	 * @param color
	 *            the color the shellfish should be given
	 */
	public void addShellfishPacketToScreen(ArrayList<Shellfish> shellfishUpdate, int topOffset, int bottomOffset) {
		for (int i = 0; i <= shellfishUpdate.size() - 1; i++) {
			addShellfishToScreen(shellfishUpdate.get(i), topOffset, bottomOffset);
		}
	}

	/**
	 * Adds an individual fish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * 
	 * @param fish
	 *            the fish to add to the screen
	 * @param topOffset
	 *            the highest the fish can be on the screen
	 * @param bottomOffset
	 *            the lowest the fish can be on the screen
	 * @param color
	 *            the color the fish should be given
	 */
	public void addFishToScreen(Fish fish, int topOffset, int bottomOffset) {
		fish.setFishBodyByWeight();
		System.out.println("set FishBody");
		double width = simpleFishingPane.getMinWidth();
		double height = simpleFishingPane.getMinHeight();
		fish.getFishGraphic().getFishImageView().setTranslateX(
				NumberUtilities.getRandomDouble(0, width - fish.getFishGraphic().getFishImageView().getFitWidth()));
		fish.getFishGraphic().getFishImageView().setTranslateY(NumberUtilities.getRandomDouble(topOffset,
				height - bottomOffset - fish.getFishGraphic().getFishImageView().getFitHeight()));
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				simpleFishingPane.getChildren().add(fish.getFishGraphic().getFishImageView());
				fish.getFishGraphic().getFishImageView().setOnMouseClicked(new ExtractFishAction(fish));
			}
		});
	}

	/**
	 * Adds an individual shellfish to the screen between the topOffset and
	 * bottomOffset and of the given color
	 * 
	 * @param shellfish
	 *            the shellfish to add to the screen
	 * @param topOffset
	 *            the highest the shellfish can be on the screen
	 * @param bottomOffset
	 *            the lowest the shellfish can be on the screen
	 * @param color
	 *            the color the shellfish should be given
	 */
	public void addShellfishToScreen(Shellfish shellfish, int topOffset, int bottomOffset) {
		shellfish.setShellfishBodyByWeight();
		double width = simpleFishingPane.getMinWidth();
		double height = simpleFishingPane.getMinHeight();
		shellfish.getShellfishGraphic().getShellfishImageView().setTranslateX(NumberUtilities.getRandomDouble(0,
				width - shellfish.getShellfishGraphic().getShellfishImageView().getFitWidth()));
		shellfish.getShellfishGraphic().getShellfishImageView().setTranslateY(NumberUtilities.getRandomDouble(topOffset,
				height - bottomOffset - shellfish.getShellfishGraphic().getShellfishImageView().getFitHeight()));
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				simpleFishingPane.getChildren().add(shellfish.getShellfishGraphic().getShellfishImageView());
				shellfish.getShellfishGraphic().getShellfishImageView().setOnMouseClicked(new ExtractShellfishAction(shellfish));
			}
		});
	}

	

	/**
	 * Action that occurs when a fish is caught.
	 */
	private class ExtractFishAction implements EventHandler<MouseEvent> {
		Fish fish;

		ExtractFishAction(Fish fish) {
			this.fish = fish;
		}

		@Override
		public void handle(MouseEvent e) {
			try {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						player.addSeaCreatureToIceChest(fish);
						simpleFishingPane.getChildren().remove(fish.getFishGraphic().getFishImageView());
						switch (fish.getSpecies()) {
						case COD:
							ArrayListUtilities.removeObjectFromArrayList(clientFishingActivityFishManager.codPopulation,
									fish);
							System.out.println("Cod: "+clientFishingActivityFishManager.codPopulation.size());
							clientSubOcean.currentPopulationCod--;
							break;
						case SALMON:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.salmonPopulation, fish);
							System.out.println("Salmon: "+clientFishingActivityFishManager.salmonPopulation.size());
							clientSubOcean.currentPopulationSalmon--;
							break;

						case TUNA:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.tunaPopulation, fish);
							System.out.println("Tuna: "+clientFishingActivityFishManager.tunaPopulation.size());
							clientSubOcean.currentPopulationTuna--;
							break;
						}
						
					}
				});
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		};
	}

	/**
	 * Action that occurs when a shellfish is caught.
	 */
	private class ExtractShellfishAction implements EventHandler<MouseEvent> {
		Shellfish shellfish;

		ExtractShellfishAction(Shellfish shellfish) {
			this.shellfish = shellfish;
		}

		@Override
		public void handle(MouseEvent e) {
			try {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						player.addSeaCreatureToIceChest(shellfish);
						simpleFishingPane.getChildren().remove(shellfish.getShellfishGraphic().getShellfishImageView());
						switch (shellfish.getSpecies()) {
						case LOBSTER:
							ArrayListUtilities.removeObjectFromArrayList(clientFishingActivityFishManager.lobsterPopulation,
									shellfish);
							System.out.println("Lobster: "+clientFishingActivityFishManager.lobsterPopulation.size());
							clientSubOcean.currentPopulationLobster--;
							break;
						case CRAB:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.crabPopulation, shellfish);
							System.out.println("Crab: "+clientFishingActivityFishManager.crabPopulation.size());
							clientSubOcean.currentPopulationCrab--;
							break;

						case OYSTER:
							ArrayListUtilities
									.removeObjectFromArrayList(clientFishingActivityFishManager.oysterPopuliation, shellfish);
							System.out.println("Oyster: "+clientFishingActivityFishManager.oysterPopuliation.size());
							
							clientSubOcean.currentPopulationOyster--;
							break;
						}
						
					}
				});
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		};
	}

	/**
	 * For keeping track of all the SeaCreatures on the client's side.
	 */
	class ClientSubOcean {

		int currentPopulationCod = 0;
		int maxPopulationCod = Constants.COD_MAX_POPULATION / 10;
		int currentPopulationSalmon = 0;
		int maxPopulationSalmon = Constants.SALMON_MAX_POPULATION / 10;
		int currentPopulationTuna = 0;
		int maxPopulationTuna = Constants.TUNA_MAX_POPULATION / 10;
		int currentPopulationOyster = 0;
		int maxPopulationOyster = Constants.OYSTER_MAX_POPULATION / 10;
		int currentPopulationLobster = 0;
		int maxPopulationLobster = Constants.LOBSTER_MAX_POPULATION / 10;
		int currentPopulationCrab = 0;
		int maxPopulationCrab = Constants.CRAB_MAX_POPULATION / 10;
	}
}