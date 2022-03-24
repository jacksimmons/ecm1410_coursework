// Part of the cycling package
package cycling;

// Java packages in use
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.EOFException;
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
 * CyclingPortal is an implementor
 * of the CyclingPortalInterface interface.
 */
public class CyclingPortal implements CyclingPortalInterface {

    // Instance attributes
    protected ArrayList<Race> races;
	protected ArrayList<Stage> stages;
    protected ArrayList<Segment> segments;
	protected ArrayList<Rider> riders;
	protected ArrayList<Team> teams;
    protected String outdir;

    // Static attributes
    private static final HashMap<StageType, int[]> stagePointsMap = new HashMap<StageType, int[]>();
    private static final HashMap<SegmentType, int[]> segmentPointsMap = new HashMap<SegmentType, int[]>();
    static {
        stagePointsMap.put(StageType.FLAT,
        new int[]{50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2});
        stagePointsMap.put(StageType.MEDIUM_MOUNTAIN,
        new int[]{30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2});
        stagePointsMap.put(StageType.HIGH_MOUNTAIN,
        new int[]{20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1});
        stagePointsMap.put(StageType.TT,
        new int[]{20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1});

        segmentPointsMap.put(SegmentType.SPRINT,
        new int[]{20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1});
        segmentPointsMap.put(SegmentType.C4,
        new int[]{1});
        segmentPointsMap.put(SegmentType.C3,
        new int[]{2, 1});
        segmentPointsMap.put(SegmentType.C2,
        new int[]{5, 3, 2, 1});
        segmentPointsMap.put(SegmentType.C1,
        new int[]{10, 8, 6, 4, 2, 1});
        segmentPointsMap.put(SegmentType.HC,
        new int[]{20, 15, 12, 10, 8, 6, 4, 2});
    }

    // Constructor
    public CyclingPortal()
    {
        this.races = new ArrayList<Race>();
        this.stages = new ArrayList<Stage>();
        this.segments = new ArrayList<Segment>();
        this.riders = new ArrayList<Rider>();
        this.teams = new ArrayList<Team>();
        outdir = System.getProperty("user.dir");
        outdir = outdir.substring(0, outdir.length() - 3); // Subtracts "\src\cycling"
        this.outdir = outdir + "\\res";
        System.out.println(this.outdir);
    }

    // These methods iteratively search through the ArrayList attributes for the member
    // with the matching ID.

    /**
	 * Returns the race object corresponding to the provided race ID.
     *
	 * @param raceId The ID of the race being queried.
	 * @return The Race object.
	 */
    public Race getRace(int raceId)
    {
        // Finds the corresponding race and returns it if it exists.
        // If it doesn't exist, null is returned.

        Race race = null;
        for (int i=0; i<this.races.size(); i++)
        {
            if (this.races.get(i).getId() == raceId)
            {
                race = this.races.get(i);
                break;
            }
        }

        return race;
    }

    /**
	 * Returns the stage object corresponding to the provided stage ID.
     *
	 * @param stageId The ID of the stage being queried.
	 * @return The Stage object.
	 */
    public Stage getStage(int stageId)
    {
        // Finds the corresponding stage and returns it if it exists.
        // If it doesn't exist, null is returned.

        Stage stage = null;
        for (int i=0; i<this.stages.size(); i++)
        {
            if (this.stages.get(i).getId() == stageId)
            {
                stage = this.stages.get(i);
                break;
            }
        }

        return stage;
    }

    /**
	 * Returns the segment object corresponding to the provided segment ID.
     *
	 * @param segmentId The ID of the segment being queried.
	 * @return The Segment object.
	 */
    public Segment getSegment(int segmentId)
    {
        // Finds the corresponding segment and returns it if it exists.
        // If it doesn't exist, null is returned.

        Segment segment = null;
        for (int i=0; i<this.segments.size(); i++)
        {
            if (this.segments.get(i).getId() == segmentId)
            {
                segment = this.segments.get(i);
                break;
            }
        }

        return segment;
    }

    /**
	 * Returns the team object corresponding to the provided team ID.
     *
	 * @param teamId The ID of the team being queried.
	 * @return The Team object.
	 */
    public Team getTeam(int teamId)
    {
        // Finds the corresponding team and returns it if it exists.
        // If it doesn't exist, null is returned.

        Team team = null;
        for (int i=0; i<this.teams.size(); i++)
        {
            if (this.teams.get(i).getId() == teamId)
            {
                team = this.teams.get(i);
                break;
            }
        }

        return team;
    }

    /**
	 * Returns the rider object corresponding to the provided rider ID.
     *
	 * @param riderId The ID of the rider being queried.
	 * @return The Rider object.
	 */
    public Rider getRider(int riderId)
    {
        // Finds the corresponding rider and returns it if it exists.
        // If it doesn't exist, null is returned.

        Rider rider = null;
        for (int i=0; i<this.riders.size(); i++)
        {
            if (this.riders.get(i).getId() == riderId)
            {
                rider = this.riders.get(i);
                break;
            }
        }

        return rider;
    }

    /**
	 * Returns the HashMap relating Riders to their total race time.
     *
	 * @param raceId The ID of the race being queried.
     * @param useAdjusted Whether or not the HashMap values should be adjusted time or not.
	 * @return The HashMap.
	 */
    public HashMap<Integer, LocalTime> getRiderToTotalRaceTimeMap(int raceId, boolean useAdjusted) throws IDNotRecognisedException
    {
        HashMap<Integer, LocalTime> riderToTime = new HashMap<>();

        for (Stage stage : this.stages)
        {
            if (stage.getRaceId() == raceId)
            {
                HashMap<Integer, ArrayList<LocalTime>> riderResultsMap = stage.getRiderResults();
                for (int riderId : riderResultsMap.keySet())
                {
                    Rider rider = this.getRider(riderId);
                    if (rider != null)
                    {
                        LocalTime stageTime;
                        if (useAdjusted)
                        {
                            stageTime = this.getRiderAdjustedElapsedTimeInStage(stage.getId(), riderId);
                        }
                        else
                        {
                            LocalTime[] riderResults = this.getRiderResultsInStage(stage.getId(), riderId);
                            stageTime = riderResults[riderResults.length - 1];
                        }
                        LocalTime prevTime = riderToTime.get(riderId);

                        if (prevTime == null)
                        {
                            riderToTime.put(riderId, stageTime);
                        }
                        else
                        {
                            riderToTime.put(riderId, prevTime.plusHours(stageTime.getHour()).plusMinutes(stageTime.getMinute()).plusSeconds(stageTime.getSecond()).plusNanos(stageTime.getNano()));
                        }
                    }
                }
            }
        }

        return riderToTime;
    }

    /**
	 * Returns an array of riders sorted by their race time.
     *
	 * @param riderToTime A rider to race time HashMap.
	 * @return The array of riders.
	 */
    public int[] sortRidersByTotalRaceTime(HashMap<Integer, LocalTime> riderToTime)
    {
        // Make an ArrayList containing all of the finishing times; sort it
        ArrayList<LocalTime> sortedFinishes = new ArrayList<LocalTime>(riderToTime.values());
        Collections.sort(sortedFinishes);

        // Make an array of the same size as sortedFinishes to match the return type
        // This will contain the Riders IDs sorted by rank.
        // Create a variable to track the position in the array to ensure an index error doesn't occur.
        int[] sortedRiders = new int[sortedFinishes.size()];
        int sortedRidersIndex = 0;

        // Iterate over the sorted finishing times...
        for (LocalTime finish : sortedFinishes)
        {
            // Iterate a second time, over the key set of the list of rider IDs
            for (int key : riderToTime.keySet())
            {
                // Check if the rider has the same time as the sorted time
                // If they do, the rider ID will be matched to the same index
                // belonging to the finishing time (so it will be indexed by
                // its rank).
                if (finish.equals(riderToTime.get(key)) && sortedRidersIndex < sortedRiders.length)
                {
                    // Assign the rider ID (key) to the correct index, increment the index for the
                    // next pass.
                    sortedRiders[sortedRidersIndex] = key;
                    sortedRidersIndex++;
                }
            }
        }

        return sortedRiders;
    }

    /**
	 * Returns an array of scores of riders - each element is located in the index of the rank of the rider it belongs to.
     *
	 * @param raceId The ID of the race being queried.
     * @param type Whether to calculate Standard or Mountain points.
	 * @return The Array of points.
	 */
    public int[] getAnyTypeOfRidersPointsInRace(int raceId, String type) throws IDNotRecognisedException
    {
        HashMap<Integer, LocalTime> riderToTime = this.getRiderToTotalRaceTimeMap(raceId, false);
        int[] sortedRiders = this.sortRidersByTotalRaceTime(riderToTime);
        int[] pointsSortedByRiderRank = new int[sortedRiders.length];
        for (Stage stage : this.stages)
        {
            int[] sortedRidersOfStage = this.getRidersRankInStage(stage.getId());

            if (stage.getRaceId() == raceId)
            {
                HashMap<Integer, ArrayList<LocalTime>> riderResults = stage.getRiderResults();
                for (int key : riderResults.keySet())
                {
                    int keyIndexInSortedRiders = this.intLinearSearch(sortedRiders, key);
                    if (keyIndexInSortedRiders != -1)
                    {
                        int keyIndexInStageRank = this.intLinearSearch(sortedRidersOfStage, key);
                        if (keyIndexInStageRank != -1)
                        {
                            if (type == "Mountain")
                            {
                                pointsSortedByRiderRank[keyIndexInSortedRiders] += this.getRidersMountainPointsInStage(stage.getId())[keyIndexInStageRank];
                            }
                            else if (type == "Standard")
                            {
                                pointsSortedByRiderRank[keyIndexInSortedRiders] += this.getRidersPointsInStage(stage.getId())[keyIndexInStageRank];
                            }
                        }
                    }
                }
            }
        }
        return pointsSortedByRiderRank;
    }

    /**
	 * Returns an array of rider IDs sorted by their point classification rank.
     *
	 * @param raceId The ID of the race being queried.
     * @param type Whether they are being classified by Standard points or Mountain points.
	 * @return The array.
	 */
    public int[] getAnyTypeOfRidersPointClassificationRank(int raceId, String type) throws IDNotRecognisedException
    {
        HashMap<Integer, Integer> riderToPoints = new HashMap<>();
        for (Stage stage : this.stages)
        {
            if (stage.getRaceId() == raceId)
            {
                int[] sortedRidersInStage = this.getRidersRankInStage(stage.getId());
                int[] pointsSortedByRiderRankInStage = new int[sortedRidersInStage.length];

                if (type == "Mountain")
                {
                    pointsSortedByRiderRankInStage = this.getRidersMountainPointsInStage(stage.getId());
                }
                else if (type == "Standard")
                {
                    pointsSortedByRiderRankInStage = this.getRidersPointsInStage(stage.getId());
                }

                for (int i=0; i<sortedRidersInStage.length; i++)
                {
                    Integer prevPoints = riderToPoints.get(sortedRidersInStage[i]);
                    if (prevPoints != null)
                    {
                        riderToPoints.put(sortedRidersInStage[i], prevPoints + pointsSortedByRiderRankInStage[i]);
                    }
                    else
                    {
                        riderToPoints.put(sortedRidersInStage[i], (int)pointsSortedByRiderRankInStage[i]);
                    }
                }
            }
        }

        ArrayList<Integer> riders = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>();
        for (int key : riderToPoints.keySet())
        {
            riders.add(key);
        }
        for (int value : riderToPoints.values())
        {
            points.add(value);
        }

        ArrayList<Integer> sortedPoints = points;
        Collections.sort(sortedPoints);
        int[] ridersSortedByPoints = new int[sortedPoints.size()];
        for (int i=0; i<sortedPoints.size(); i++)
        {
            ridersSortedByPoints[i] = riders.get(points.indexOf(sortedPoints.get(i)));
        }
        return ridersSortedByPoints;
    }

    /**
	 * A simple linear search algorithm over an array of integers.
     *
	 * @param arr The array (sorted or not).
	 * @param value The value to be searched for.
     * @return The index of the value if found, or -1 if not found.
	 */
    public int intLinearSearch(int[] arr, int value)
    {
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] == value)
            {
                return i;
            }
        }
        return -1;
    }

    // Pre-existing Methods
	@Override
	public int[] getRaceIds() {
        // Iteratively adds the ID of each race in the races list to raceIds, then returns raceIds.
        int[] raceIds = new int[this.races.size()];
		for (int i=0; i < this.races.size(); i++)
        {
            raceIds[i] = this.races.get(i).getId();
        }
        return raceIds;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
        // Creates a Race object, with necessary checks in place. Returns the Race ID or raises an exception.
		if (name.length() <= 30 && name != null && name != "")
		{
            // The name is of correct size, is not null and is not empty.
			for (int i=0; i < name.length(); i++)
			{
				if (Character.isWhitespace(name.charAt(i)))
				{
                    // The name has whitespace; invalid.
					throw new InvalidNameException("No whitespace allowed in race names.");
                }
			}

			for (int i=0; i<this.races.size(); i++)
			{
				if (this.races.get(i).getName() == name)
				{
                    // The name has already been taken.
					throw new IllegalNameException("This race name is already taken.");
                }
			}

            // The name is valid; create the Race.
			Race race = new Race(name, description);
            this.races.add(race);
            return race.getId();
		}
		else
		{
            // The name is not of correct size, or is null or empty.
			throw new InvalidNameException("Race name length is of incorrect length (1 to 30 characters) or is null.");
        }
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
        // Displays all the relevant details of a race, formatted.
        Race race = this.getRace(raceId);

        // Need to deal with the fact that getRace returns null if no match is found.
        if (race == null)
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
        else
        {
            // Calculate the number of stages and total length of the race.
            int numberOfStages = 0;
            double totalLength = 0.0;
            for (int i=0; i<this.stages.size(); i++)
            {
                Stage stage = this.stages.get(i);
                if (stage.getRaceId() == raceId)
                {
                    numberOfStages++;
                    totalLength += stage.getLength();
                }
                assert numberOfStages <= this.stages.size();
            }

            // Returns the found details in the form of a formatted string.
            return String.format("Race ID: %d\nName: %s\nDescription: %s\nNumber of Stages: %d\nLength: %fkm",
            raceId, race.getName(), race.getDescription(), numberOfStages, totalLength);
        }
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
        // Removes a race by ID or raises an exception.

        Race race = this.getRace(raceId);

        // Need to deal with the fact that getRace returns null if no match is found.
        if (race == null)
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
        else
        {
            // Cascade through all stages and segments connected to the race by ID.

            // Find the stages linked to this Race
            ArrayList<Stage> stages = new ArrayList<>(this.stages);
            ArrayList<Segment> segments = new ArrayList<>(this.segments);

            // Find the stages linked to this Race
            for (Stage stage : stages)
            {
                if (stage.getRaceId() == race.getId())
                {
                    // Find the segments linked to these stages
                    for (Segment segment : segments)
                    {
                        if (segment.getStageId() == stage.getId())
                        {
                            // Delete the segments
                            this.segments.remove(segment);
                        }
                    }
                    // Delete the stages
                    this.stages.remove(stage);
                }
            }
            // Delete the Race.
            this.races.remove(race);
        }
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
        // Returns the number of stages in the race provided, or raises an exception.
        Race race = this.getRace(raceId);

        // Need to deal with the fact that getRace returns null if no match is found.
        if (race == null)
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
        else
        {
            // Iteratively search for stages belonging to the race...
            // ... and increment numberOfStages whenever one is found
            int numberOfStages = 0;
            for (int i=0; i<this.stages.size(); i++)
            {
                if (this.stages.get(i).getRaceId() == raceId)
                {
                    numberOfStages++;
                }
            }
            return numberOfStages;
        }
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {

        // Adds a stage to the given race, and either returns the ID of the stage or raises an exception.

        boolean validRaceId = false;
        boolean validName = true;

        // Check to see if the race ID is valid
        for (int i=0; i<this.races.size(); i++)
        {
            if (raceId == this.races.get(i).getId())
            {
                validRaceId = true;
            }
        }

        if (!validRaceId)
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }

        else
        {
            // Check to see if the name isn't valid
            for (int i=0; i<this.stages.size(); i++)
            {
                if (stageName == this.stages.get(i).getName())
                {
                    validName = false;
                }
            }

            if (!validName)
            {
                throw new IllegalNameException("Stage name already exists.");
            }

            else
            {
                if (stageName == null || stageName == "" || stageName.length() > 30)
                {
                    throw new InvalidNameException("Stage name has invalid length (1 to 30 characters) or is null.");
                }

                for (int i=0; i < stageName.length(); i++)
                {
                    if (Character.isWhitespace(stageName.charAt(i)))
                    {
                        // The name has whitespace; invalid.
                        throw new InvalidNameException("No whitespace allowed in stage names.");
                    }
                }

                // All checks passed if reaches here.
                Stage stage = new Stage(raceId, stageName, description, length, startTime, type, StageState.PREPARING);
                this.stages.add(stage);
                return stage.getId();
            }
        }
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
        // Iteratively checks for all stages belonging to a certain race and returns an array containing their IDs,
        // or an empty array if an invalid raceID is provided.
        Race race = this.getRace(raceId);
        if (race == null)
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
        else
        {
            ArrayList<Integer> raceStages = new ArrayList<>();
            for (int i=0; i<this.stages.size(); i++)
            {
                if (this.stages.get(i).getRaceId() == raceId)
                {
                    raceStages.add(this.stages.get(i).getId());
                }
            }
            assert raceStages.size() == this.getNumberOfStages(raceId);
            int[] raceStagesArray = new int[raceStages.size()];
            for (int i=0; i<raceStages.size(); i++)
            {
                raceStagesArray[i] = raceStages.get(i);
            }
            return raceStagesArray;
        }
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
        // Returns the length of a stage, or 0 if the stage ID provided doesn't exist.
        Stage stage = this.getStage(stageId);

        // Need to deal with the fact that stage can be null if it wasn't found in getStage.
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            return stage.getLength();
        }
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		Stage stage = this.getStage(stageId);

        // Need to deal with the fact that stage can be null if it wasn't found in getStage.
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            // Cascade through all segments connected to the stage by ID.

            // Find the segments
            for (Segment segment : this.segments)
            {
                if (segment.getStageId() == stageId)
                {
                    // Remove the segments
                    this.segments.remove(segment);
                }
            }
            // Remove the stage.
            this.stages.remove(stage);
        }
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {

        Stage stage = this.getStage(stageId);

        // Need to deal with the fact that stage can be null if not found in getStage.
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            if (location > stage.getLength() || location < 0)
            {
                throw new InvalidLocationException("Segment location is out of the provided stage's bounds.");
            }

            if (stage.getState() == StageState.WAITING)
            {
                throw new InvalidStageStateException("Stage is waiting for results; no segments can be added to it.");
            }

            if (stage.getType() == StageType.TT)
            {
                throw new InvalidStageTypeException("Time-trial stages can't contain any segments.");
            }

            Segment segment = new Segment(stageId, location, type, averageGradient, length);
            this.segments.add(segment);
            return segment.getId();
        }
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// Adds a segment without length or averageGradient attributes (sets them to 0), if the necessary
        // checks succeed. Returns the id of the segment or throws an error.
        Stage stage = this.getStage(stageId);
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            if (location > stage.getLength() || location < 0)
            {
                throw new InvalidLocationException("Segment location is out of the provided stage's bounds.");
            }

            if (stage.getState() == StageState.WAITING)
            {
                throw new InvalidStageStateException("Stage is waiting for results; no segments can be added to it.");
            }

            if (stage.getType() == StageType.TT)
            {
                throw new InvalidStageTypeException("Time-trial stages can't contain any segments.");
            }

            Segment segment = new Segment(stageId, location, SegmentType.SPRINT, 0.0, 0.0);
            this.segments.add(segment);
            return segment.getId();
        }
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
        // Finds a segment by ID, then removes it from the segments attribute.
        // Note that any valid rider results already in a stage will become invalid upon the removal of a segment.

        Segment segment = this.getSegment(segmentId);
        if (segment == null)
        {
            throw new IDNotRecognisedException("Segment ID doesn't exist.");
        }
        else
        {
            this.segments.remove(segment);
        }
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
        // Concludes the preparation of the given stage
        // ! test whether results can be gotten without this method - shouldnt be possible
        Stage stage = this.getStage(stageId);
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            stage.setState(StageState.WAITING);
            assert(stage.getState() == StageState.WAITING);
        }
	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
        // Returns an array of segment IDs of a stage, or throws an exception.

        Stage stage = this.getStage(stageId);

        // Need to deal with the fact that stage can be null if not found in getStage.
        if (stage == null)
        {
            throw new IDNotRecognisedException("This stage ID doesn't exist.");
        }
        else
        {
            ArrayList<Integer> stageSegments = new ArrayList<>();
            for (int i=0; i<this.segments.size(); i++)
            {
                if (this.segments.get(i).getStageId() == stageId)
                {
                    stageSegments.add(this.segments.get(i).getId());
                }
            }

            int[] stageSegmentsArray = new int[stageSegments.size()];
            for (int i=0; i<stageSegments.size(); i++)
            {
                stageSegmentsArray[i] = stageSegments.get(i);
            }
            return stageSegmentsArray;
        }
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        // Creates a Team with the necessary checks. Either returns the Team ID or raises an exception.
        if (name.length() <= 30 && name != null && name != "")
		{
			for (int i=0; i < name.length(); i++)
			{
				if (Character.isWhitespace(name.charAt(i)))
				{
					throw new InvalidNameException("No whitespace allowed in team names.");
                }
			}

			for (int i=0; i<this.teams.size(); i++)
			{
				if (this.teams.get(i).getName() == name)
				{
					throw new IllegalNameException("This team name is already taken.");
                }
			}

			Team team = new Team(name, description);
            this.teams.add(team);
            return team.getId();
		}
		else
		{
            throw new InvalidNameException("Team name length is of incorrect length (1 to 30 characters) or is null.");
        }
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
        // Finds the team and removes or throws an exception.
        Team team = this.getTeam(teamId);

        if (team == null)
        {
            throw new IDNotRecognisedException("Team ID doesn't exist.");
        }
        else
        {
            // Cascade through all riders connected to the team by ID.

            // Find the riders

            ArrayList<Rider> riders = new ArrayList<>(this.riders);
            for (Rider rider : riders)
            {
                if (rider.getTeamId() == teamId)
                {
                    // Remove them
                    this.removeRider(rider.getId());
                }
            }
            // Remove the team
            this.teams.remove(team);
		}
	}

	@Override
	public int[] getTeams() {
		// Returns an array of all the team IDs.
		int[] teamIds = new int[this.teams.size()]; // Upper bound for number of team IDs.
		for (int i=0; i<this.teams.size(); i++)
		{
			teamIds[i] = this.teams.get(i).getId();
		}
        assert teamIds.length == this.teams.size();
		return teamIds;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
        // Return the riders of a team, or throws an exception.
        ArrayList<Integer> teamRiders = new ArrayList<>();
        Team team = this.getTeam(teamId);

        // Need to deal with the fact that team can be null if not found in getTeam.
        if (team == null)
        {
            throw new IDNotRecognisedException("Team ID doesn't exist.");
        }
        else
        {
            for (int i=0; i<this.riders.size(); i++)
            {
                if (this.riders.get(i).getTeamId() == teamId)
                {
                    teamRiders.add(this.riders.get(i).getId());
                }
            }

            int[] teamRidersArray = new int[teamRiders.size()];
            for (int i=0; i<teamRiders.size(); i++)
            {
                teamRidersArray[i] = teamRiders.get(i);
            }
            return teamRidersArray;
        }
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		// Creates a rider and returns its ID if successful. If not, throws an error and returns -1.
        // The rider will have a raceId attribute value of -1 as it has not yet been added to one.
		if (yearOfBirth >= 1900)
		{
			if (name != null)
			{
                if (this.getTeam(teamID) != null)
                {
                    Rider rider = new Rider(teamID, -1, name, yearOfBirth);
    				this.riders.add(rider);
    				return rider.getId();
                }
                throw new IDNotRecognisedException("Team ID doesn't exist.");
            }
			throw new IllegalArgumentException("Name of the rider cannot be null.");
        }
		throw new IllegalArgumentException("Year of birth must be at least 1900.");
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
        // Iteratively searches for the first (and only) Rider with the given ID, and removes it.

        Rider rider = this.getRider(riderId);

        if (rider == null)
        {
            throw new IDNotRecognisedException("Rider ID doesn't exist.");
        }
        else
        {
            // Remove all rider results from stages
            for (Stage stage : this.stages)
            {
                HashMap<Integer, ArrayList<LocalTime>> riderResults = stage.getRiderResults();
                for (int key : riderResults.keySet())
                {
                    if (key == rider.getId())
                    {
                        riderResults.remove(key);
                        stage.setRiderResults(riderResults);
                    }
                }
            }

            // Remove the rider once this is done
            this.riders.remove(rider);
        }
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {

		Stage stage = this.getStage(stageId);

        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            if (stage.getRiderResults().containsKey(riderId))
            {
                throw new DuplicatedResultException("This stage already has a registered result for this rider.");
            }

            LocalTime t_prev = checkpoints[0];
            for (LocalTime t : checkpoints)
            {
                if (t.isAfter(t_prev) || t.equals(t_prev))
                {
                    // Ensure all the times are consecutive.
                    t_prev = t;
                    continue;
                }
                else
                {
                    throw new InvalidCheckpointsException("Invalid result: Checkpoint times are not chronological.");
                }
            }

            if (checkpoints.length != this.getStageSegments(stageId).length)
            {
                // Checkpoints include the start and end points of the race, so 2 more checkpoints than segments are needed.
                throw new InvalidCheckpointsException("Invalid result: Not all, or too many checkpoints are accounted for.");
            }

            if (stage.getState() == StageState.PREPARING)
            {
                // Stage is not ready for results!
                throw new InvalidStageStateException("This stage is not ready to receive results.");
            }

            stage.addRiderResult(riderId, checkpoints);
        }

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // Returns the rider's results in the given stage, or an empty array if they don't have any.
        Stage stage = this.getStage(stageId);
        Rider rider = this.getRider(riderId);

        if (stage != null)
        {
            if (rider != null)
            {
                // Allowed cast, as stage.getRiderResults().get(riderId) is of type ArrayList<LocalTime>.
                ArrayList<LocalTime> results = stage.getRiderResults().get(riderId);
                if (results != null)
                {
                    LocalTime[] resultsArray = new LocalTime[results.size()];
                    for (int i=0; i<resultsArray.length; i++)
                    {
                        resultsArray[i] = results.get(i);
                    }
                    return resultsArray;
                }
                return new LocalTime[0];
            }
            throw new IDNotRecognisedException("Rider ID doesn't exist.");
        }
        throw new IDNotRecognisedException("Stage ID doesn't exist.");
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // Returns the adjusted elapsed time of a rider (the lowest time of all riders which reached the finish line
        // less than a second before this rider).
        Stage stage = this.getStage(stageId);
        Rider rider = this.getRider(riderId);

        if (stage != null)
        {
            if (rider != null)
            {
                HashMap<Integer, ArrayList<LocalTime>> riderResults = stage.getRiderResults();
                ArrayList<LocalTime> results = riderResults.get(riderId);
                LocalTime[] resultsArray = new LocalTime[results.size()];
                for (int i=0; i<resultsArray.length; i++)
                {
                    resultsArray[i] = results.get(i);
                }

                LocalTime finishingTime = resultsArray[resultsArray.length - 1];

                for (ArrayList<LocalTime> riderResult : riderResults.values())
                {
                    LocalTime comparedFinishingTime = riderResult.get(riderResult.size()-1);
                    if (finishingTime.isAfter(comparedFinishingTime))
                    {
                        finishingTime = comparedFinishingTime;
                    }
                }

                return finishingTime;
            }
            throw new IDNotRecognisedException("Rider ID doesn't exist.");
        }
        throw new IDNotRecognisedException("Stage ID doesn't exist.");
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // Removes a rider's result from the given stage.
        Stage stage = this.getStage(stageId);
        Rider rider = this.getRider(riderId);

        if (stage != null)
        {
            if (rider != null)
            {
                HashMap<Integer, ArrayList<LocalTime>> riderResults = stage.getRiderResults();
                riderResults.remove(riderId);
                stage.setRiderResults(riderResults);
                return;
            }
            throw new IDNotRecognisedException("Rider ID doesn't exist.");
        }
        throw new IDNotRecognisedException("Stage ID doesn't exist.");
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		Stage stage = this.getStage(stageId);

        if (stage != null)
        {
            // Simplify the map to Rider : Finish Time
            HashMap<Integer, ArrayList<LocalTime>> riderResults = stage.getRiderResults();
            if (riderResults.keySet().size() > 0)
            {
                HashMap<Integer, LocalTime> riderFinishes = new HashMap<Integer, LocalTime>();
                // Populate the simplified map with finishing times
                for (int key : riderResults.keySet())
                {
                    ArrayList<LocalTime> riderCheckpoints = riderResults.get(key);
                    riderFinishes.put(key, riderCheckpoints.get(riderCheckpoints.size()-1));
                }

                // Make an ArrayList containing all of the finishing times; sort it
                ArrayList<LocalTime> sortedFinishes = new ArrayList<LocalTime>(riderFinishes.values());
                Collections.sort(sortedFinishes);

                // Make an array of the same size as sortedFinishes to match the return type
                // This will contain the Riders IDs sorted by rank.
                // Create a variable to track the position in the array to ensure an index error doesn't occur.
                int[] sortedRiders = new int[sortedFinishes.size()];
                int sortedRidersIndex = 0;

                // Iterate over the sorted finishing times...
                for (LocalTime finish : sortedFinishes)
                {
                    // Iterate a second time, over the key set of riderResults (i.e. the list of rider IDs)
                    for (int key : riderResults.keySet())
                    {
                        // Check if the rider has the same time as the sorted time
                        // If they do, the rider ID will be matched to the same index
                        // belonging to the finishing time (so it will be indexed by
                        // its rank).
                        if (finish.equals(riderResults.get(key).get(riderResults.size()-1)) && sortedRidersIndex < sortedRiders.length)
                        {
                            // Assign the rider ID (key) to the correct index, increment the index for the
                            // next pass.
                            sortedRiders[sortedRidersIndex] = key;
                            sortedRidersIndex++;
                        }
                    }
                }

                // Return the sorted array of rider IDs.
                return sortedRiders;
            }
            return new int[0];
        }
        // Stage is null; the stage ID is invalid.
        throw new IDNotRecognisedException("Stage ID doesn't exist.");
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {

        Stage stage = this.getStage(stageId);
        if (stage != null)
        {
            // Simplify the map to Rider : Finish Time
            HashMap<Integer, ArrayList<LocalTime>> riderResults = stage.getRiderResults();
            HashMap<Integer, LocalTime> riderFinishes = new HashMap<Integer, LocalTime>();

            // Populate the simplified map with finishing times
            for (int key : riderResults.keySet())
            {
                ArrayList<LocalTime> riderCheckpoints = riderResults.get(key);
                riderFinishes.put(key, riderCheckpoints.get(riderCheckpoints.size()-1));
            }

            // Get adjusted elapsed times in stage
            HashMap<Integer, LocalTime> adjustedRiderFinishes = new HashMap<Integer, LocalTime>();

            for (int riderId : riderFinishes.keySet())
            {
                adjustedRiderFinishes.put(riderId, this.getRiderAdjustedElapsedTimeInStage(stageId, riderId));
            }

            // Make an ArrayList containing all of the finishing times; sort it
            ArrayList<LocalTime> sortedAdjustedFinishes = new ArrayList<LocalTime>(adjustedRiderFinishes.values());
            Collections.sort(sortedAdjustedFinishes);

            LocalTime[] sortedAdjustedFinishesArray = new LocalTime[sortedAdjustedFinishes.size()];
            for (int i=0; i<sortedAdjustedFinishesArray.length; i++)
            {
                sortedAdjustedFinishesArray[i] = sortedAdjustedFinishes.get(i);
            }
            return sortedAdjustedFinishesArray;
        }
        throw new IDNotRecognisedException("Stage ID doesn't exist.");
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
        Stage stage = this.getStage(stageId);

        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            int[] ranking = this.getRidersRankInStage(stageId);
            int[] points = new int[ranking.length];
            for (int i=0; i < ranking.length; i++)
            {
                // Set the points values to the finishing points.
                try {
                    int mapValue = stagePointsMap.get(stage.getType())[i];
                    points[i] = mapValue;
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    // The array found from stagePointsMap.get(stage.getType()) returned null;
                    // this means that the number of points scored is 0.
                    points[i] = 0;
                }
            }
            return points;
        }
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
        // !
        Stage stage = this.getStage(stageId);

        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            int[] ranking = this.getRidersRankInStage(stageId);
            int[] points = new int[ranking.length];

            HashMap<Integer, ArrayList<LocalTime>> riderResults = stage.getRiderResults();
            int[] segments = this.getStageSegments(stageId);
            for (int i=0; i < segments.length; i++)
            {
                Segment segment = this.getSegment(segments[i]);
                if (segment != null)
                {
                    // Simplify the map to Rider : Checkpoint Time
                    HashMap<Integer, LocalTime> riderSegmentFinishes = new HashMap<Integer, LocalTime>();

                    // Populate the simplified map with finishing times
                    for (int key : riderResults.keySet())
                    {
                        ArrayList<LocalTime> riderCheckpoints = riderResults.get(key);
                        riderSegmentFinishes.put(key, riderCheckpoints.get(i));
                    }

                    // Make an ArrayList containing all of the finishing times; sort it
                    ArrayList<LocalTime> sortedSegmentFinishes = new ArrayList<LocalTime>(riderSegmentFinishes.values());
                    Collections.sort(sortedSegmentFinishes);

                    // Create a variable to track the position in the array to ensure an index error doesn't occur.
                    int[] sortedRiders = new int[sortedSegmentFinishes.size()];
                    int sortedRidersIndex = 0;

                    // Iterate over the sorted finishing times...
                    for (LocalTime finish : sortedSegmentFinishes)
                    {
                        // Iterate a second time, over the key set of riderResults (i.e. the list of rider IDs)
                        for (int key : riderResults.keySet())
                        {
                            int index = this.intLinearSearch(ranking, key);
                            // Check if the rider has the same time as the sorted time
                            // If they do, the rider ID will be matched to the same index
                            // belonging to the finishing time (so it will be indexed by
                            // its rank).
                            if (finish.equals(riderResults.get(key).get(i)) && sortedRidersIndex < sortedRiders.length && index != -1)
                            {
                                // Add the correct number of points to the "index"th term of ranking, based on the position...
                                // ... of the rider in the SEGMENT ranking (sortedRiders)
                                try {
                                    points[index] += segmentPointsMap.get(segment.getType())[sortedRidersIndex];
                                    sortedRidersIndex++;
                                }
                                catch (IndexOutOfBoundsException e)
                                {
                                    // Number of points = 0, by default
                                    sortedRidersIndex++;
                                }
                            }
                        }
                    }


                }
            }
            return points;
        }
	}

	@Override
	public void eraseCyclingPortal() {
        // Objects stored in these lists will be deleted when they are reset by the garbage collector.
		this.races = new ArrayList<Race>();
        this.stages = new ArrayList<Stage>();
        this.segments = new ArrayList<Segment>();
        this.riders = new ArrayList<Rider>();
        this.teams = new ArrayList<Team>();
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {

        FileOutputStream f = new FileOutputStream(outdir + "\\" + filename);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(this);
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
        this.eraseCyclingPortal(); // Loading assumes current state is not preserved.

        FileInputStream f = new FileInputStream(outdir + "\\" + filename);
        ObjectInputStream o = new ObjectInputStream(f);
        try
        {
            // While there are more than 0 bytes to be read from the file...
            Object obj = o.readObject();
            if (obj instanceof CyclingPortal)
            {
                CyclingPortal portal = (CyclingPortal)obj;
                for (Race race : portal.races)
                {
                    this.races.add(race);
                }
                for (Stage stage : portal.stages)
                {
                    this.stages.add(stage);
                }
                for (Segment segment : portal.segments)
                {
                    this.segments.add(segment);
                }
                for (Team team : portal.teams)
                {
                    this.teams.add(team);
                }
                for (Rider rider : portal.riders)
                {
                    this.riders.add(rider);
                }
            }

            o.close();
        }
        catch (EOFException e)
        {
            // End of file reached
            o.close();
        }
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
        // Iteratively searches for the first (and only) Race with the given name, and removes it...
        // ... or throws an exception.

        // Cascade through all stages and segments connected to the race by ID.

        boolean raceNameFound = false;

        ArrayList<Race> races = new ArrayList<>(this.races);
        ArrayList<Stage> stages = new ArrayList<>(this.stages);
        ArrayList<Segment> segments = new ArrayList<>(this.segments);

        for (Race race : races)
        {
            if (race.getName().equals(name))
            {
                raceNameFound = true;

                // Find the stages linked to this Race
                for (Stage stage : stages)
                {
                    if (stage.getRaceId() == race.getId())
                    {
                        // Find the segments linked to these stages
                        for (Segment segment : segments)
                        {
                            if (segment.getStageId() == stage.getId())
                            {
                                // Delete the segments
                                this.segments.remove(segment);
                            }
                        }
                        // Delete the stages
                        this.stages.remove(stage);
                    }
                }
                // Delete the Race.
                this.races.remove(race);
            }
        }

        if (!raceNameFound)
        {
            throw new NameNotRecognisedException("No Races were found with this name.");
        }
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		Race race = this.getRace(raceId);

        if (race != null)
        {
            HashMap<Integer, LocalTime> riderToTime = this.getRiderToTotalRaceTimeMap(raceId, true);
            int[] sortedRiders = this.sortRidersByTotalRaceTime(riderToTime);
            LocalTime[] sortedTimes = new LocalTime[sortedRiders.length];

            for (int i=0; i<sortedRiders.length; i++)
            {
                sortedTimes[i] = riderToTime.get(sortedRiders[i]);
            }

            return sortedTimes;
        }
        else
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
        Race race = this.getRace(raceId);

        if (race != null)
        {
            return getAnyTypeOfRidersPointsInRace(raceId, "Standard");
        }
        else
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
        Race race = this.getRace(raceId);

        if (race != null)
        {
            return getAnyTypeOfRidersPointsInRace(raceId, "Mountain");
        }
        else
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
        Race race = this.getRace(raceId);

        if (race != null)
        {
            HashMap<Integer, LocalTime> riderToTime = this.getRiderToTotalRaceTimeMap(raceId, true);
            int[] sortedRiders = this.sortRidersByTotalRaceTime(riderToTime);

            return sortedRiders;
        }
        else
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
        Race race = this.getRace(raceId);

        if (race != null)
        {
            return this.getAnyTypeOfRidersPointClassificationRank(raceId, "Standard");
        }
        else
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
        Race race = this.getRace(raceId);

        if (race != null)
        {
            return this.getAnyTypeOfRidersPointClassificationRank(raceId, "Mountain");
        }
        else
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
	}

}
