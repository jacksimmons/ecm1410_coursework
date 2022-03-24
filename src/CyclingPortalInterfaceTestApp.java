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
	IllegalNameException, InvalidNameException, IDNotRecognisedException, InvalidLengthException {
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
		LocalDateTime startingTime = LocalDateTime.now();
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
		int c4ClimbId = portal.addCategorizedClimbToStage(
		flatStageId, 0, SegmentType.C4, 0.05, 1);
		int sprintId = portal.addIntermediateSprintToStage(
		flatStageId, 2);
		Segment c4Climb = portal.getSegment(c4ClimbId);
		assert c4Climb.getId() == c4ClimbId;
		assert c4Climb.getStageId() == flatStageId;
		assert c4Climb.getLocation() == 0;
		assert c4Climb.getType() == SegmentType.C4;
		assert c4Climb.getAverageGradient() == 0.05;
		assert c4Climb.getLength() == 1;

		int[] segmentIds = portal.getStageSegments(flatStageId);

		//assert (portal.getRaceIds().length == 0)
		//		: "Innitial SocialMediaPlatform not empty as required or not returning an empty array.";

	}

}
