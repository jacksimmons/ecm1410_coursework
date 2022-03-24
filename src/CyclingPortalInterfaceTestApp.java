import cycling.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.lang.IndexOutOfBoundsException;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortalInterface interface -- note you
 * will want to increase these checks, and run it on your CyclingPortal class
 * (not the BadCyclingPortal class).
 *
 *
 * @author Diogo Pacheco
 * @version 1.0
 */
public class CyclingPortalInterfaceTestApp {

	/**
	 * Test method.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) throws
	IllegalNameException, InvalidNameException, IDNotRecognisedException, InvalidLengthException, InvalidLocationException, DuplicatedResultException, InvalidStageStateException, InvalidStageTypeException, InvalidCheckpointsException, IOException, ClassNotFoundException, NameNotRecognisedException {
		System.out.println("The system compiled and started the execution...");

		CyclingPortal portal = new CyclingPortal();

		// Erroneous/Boundary testing

		// ---Success testing---
		// Races
		int raceId = portal.createRace("Race1", "A test race.");
		Race race = portal.getRace(raceId);
		assert race.getId() == raceId;
		assert race.getName() == "Race1";
		assert race.getDescription() == "A test race.";

		// Stages
		LocalDateTime startingTime = LocalDateTime.of(2022, 3, 3, 12, 00);
		int flatStageId = portal.addStageToRace(
		raceId, "FlatStage", "A test stage.", 2, startingTime, StageType.FLAT);
		int mediumStageId = portal.addStageToRace(
		raceId, "MedStage", "A test stage on an incline.", 1.5, startingTime, StageType.MEDIUM_MOUNTAIN);
		int highStageId = portal.addStageToRace(
		raceId, "HighStage", "A test stage on a steep incline.", 1, startingTime, StageType.HIGH_MOUNTAIN);
		int ttStageId = portal.addStageToRace(
		raceId, "TimeTrial", "A time trial stage.", 5, startingTime, StageType.TT);
		Stage flatStage = portal.getStage(flatStageId);
		assert flatStage.getId() == flatStageId;
		assert flatStage.getRaceId() == raceId;
		assert flatStage.getName() == "FlatStage";
		assert flatStage.getDescription() == "A test stage.";
		assert flatStage.getLength() == 2;
		assert flatStage.getStartTime() == startingTime;
		assert flatStage.getType() == StageType.FLAT;

		System.out.println(portal.viewRaceDetails(raceId));

		int[] stageIds = portal.getRaceStages(raceId);

		// Segments
		// Sprint from 1km to the end
		int c4ClimbId = portal.addCategorizedClimbToStage(flatStageId, 0.0, SegmentType.C4, 0.05, 1.0);
		int sprintId = portal.addIntermediateSprintToStage(flatStageId, 2.0);
		Segment c4Climb = portal.getSegment(c4ClimbId);
		assert c4Climb.getId() == c4ClimbId;
		assert c4Climb.getStageId() == flatStageId;
		assert c4Climb.getLocation() == 0.0;
		assert c4Climb.getType() == SegmentType.C4;
		assert c4Climb.getAverageGradient() == 0.05;
		assert c4Climb.getLength() == 1.0;

		int[] segmentIds = portal.getStageSegments(flatStageId);

		// Teams
		int teamId = portal.createTeam("Team1", "A standard team.");
		Team team = portal.getTeam(teamId);
		assert team.getId() == teamId;
		assert team.getName() == "Team1";
		assert team.getDescription() == "A standard team.";
		assert portal.getTeams()[0] == teamId && portal.getTeams().length == 1;

		// Riders
		int riderId = portal.createRider(teamId, "Rider1", 2003);
		Rider rider = portal.getRider(riderId);
		assert rider.getId() == riderId;
		assert rider.getName() == "Rider1";
		assert rider.getYearOfBirth() == 2003;
		LocalTime timeOfc4 = LocalTime.of(12, 50, 00, 00001);
		LocalTime timeOfSprint = LocalTime.of(13, 10, 00, 00001);

		portal.concludeStagePreparation(flatStageId);
		portal.registerRiderResultsInStage(flatStageId, riderId, timeOfc4, timeOfSprint);
		LocalTime[] riderResults = portal.getRiderResultsInStage(flatStageId, riderId);
		assert riderResults[0].equals(timeOfc4) && riderResults[1].equals(timeOfSprint);

		int secondRiderId = portal.createRider(teamId, "Rider2", 2002);
		Rider secondRider = portal.getRider(secondRiderId);
		LocalTime secondTimeOfc4 = LocalTime.of(12, 50, 00, 00000);
		LocalTime secondTimeOfSprint = LocalTime.of(13, 10, 00, 00000);
		portal.registerRiderResultsInStage(flatStageId, secondRiderId, secondTimeOfc4, secondTimeOfSprint);

		LocalTime adjustedTime = portal.getRiderAdjustedElapsedTimeInStage(flatStageId, riderId);
		System.out.println(adjustedTime.getNano());
		assert adjustedTime.equals(secondTimeOfSprint);

		int[] rankedRiderIds = portal.getRidersRankInStage(flatStageId);
		assert rankedRiderIds[0] == secondRiderId;

		LocalTime[] rankedAdjustedElapsedTimes = portal.getRankedAdjustedElapsedTimesInStage(flatStageId);
		assert rankedAdjustedElapsedTimes[0].equals(rankedAdjustedElapsedTimes[1]);

		int[] ridersPoints = portal.getRidersPointsInStage(flatStageId);
		for (int i=0; i < ridersPoints.length; i++)
		{
			System.out.println("Rider" + rankedRiderIds[i] + ": " + ridersPoints[i]);
		}

		int[] ridersMtnPoints = portal.getRidersMountainPointsInStage(flatStageId);
		for (int i=0; i < ridersMtnPoints.length; i++)
		{
			System.out.println("MtnRider" + rankedRiderIds[i] + ": " + ridersMtnPoints[i]);
		}

		int[] rankedRaceRiderIds = portal.getRidersGeneralClassificationRank(raceId);
		int[] ridersRacePoints = portal.getRidersPointsInRace(raceId);
		for (int i=0; i < ridersRacePoints.length; i++)
		{
			System.out.println("RaceRider" + rankedRaceRiderIds[i] + ": " + ridersRacePoints[i]);
		}

		int[] ridersRaceMtnPoints = portal.getRidersMountainPointsInRace(raceId);
		for (int i=0; i < ridersRaceMtnPoints.length; i++)
		{
			System.out.println("RaceMtnRider" + rankedRaceRiderIds[i] + ": " + ridersRaceMtnPoints[i]);
		}

		int[] ridersPointClass = portal.getRidersPointClassificationRank(raceId);
		for (int rId : ridersPointClass)
		{
			System.out.println("PointClass" + rId);
		}

		int[] ridersMtnPointClass = portal.getRidersMountainPointClassificationRank(raceId);
		for (int rId : ridersMtnPointClass)
		{
			System.out.println("MtnPointClass" + rId);
		}

		portal.deleteRiderResultsInStage(flatStageId, secondRiderId);
		assert portal.getRiderResultsInStage(flatStageId, secondRiderId).length == 0;

		//assert (portal.getRaceIds().length == 0)
		//		: "Innitial SocialMediaPlatform not empty as required or not returning an empty array.";

		// Removal
		portal.removeTeam(teamId);
		try {
			portal.getRiderResultsInStage(flatStageId, riderId);
		}
		catch (IDNotRecognisedException e)
		{
			System.out.println("Cascading of rider successful.");
		}

		assert portal.getTeams().length == 0;
		assert portal.getRidersRankInStage(flatStageId).length == 0;

		// Saving and Loading
		portal.saveCyclingPortal("portal.ser");
		portal.loadCyclingPortal("portal.ser");

		assert (portal.getRace(raceId) instanceof Race);
		portal.removeRaceByName("Race1");
	}
}
