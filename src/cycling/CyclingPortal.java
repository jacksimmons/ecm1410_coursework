package cycling;

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

/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortalInterface interface.
 *
 * @author Diogo Pacheco
 * @version 1.0
 *
 */
public class CyclingPortal implements CyclingPortalInterface {
    // Attributes
    private ArrayList<Race> races;
	private ArrayList<Stage> stages;
    private ArrayList<Segment> segments;
	private ArrayList<Rider> riders;
	private ArrayList<Team> teams;
    private String outdir;

    // Constructor
    public CyclingPortal()
    {
        this.races = new ArrayList<Race>();
        this.stages = new ArrayList<Stage>();
        this.segments = new ArrayList<Segment>();
        this.riders = new ArrayList<Rider>();
        this.teams = new ArrayList<Team>();
        outdir = System.getProperty("user.dir");
        outdir = outdir.substring(0, outdir.length() - 12); // Subtracts "\src\cycling"
        outdir += "\res";
        this.outdir = outdir;
    }

    // These methods iteratively search through the ArrayList attributes for the member
    // with the matching ID.

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
            }

            // Returns the found details in the form of a formatted string.
            return String.format("Race ID: %i\nName: %s\nDescription: %s\nNumber of Stages: %i\nLength: %d",
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
            int[] raceStages = new int[this.stages.size()]; // Upper bound for the length of the array
            for (int i=0; i<this.stages.size(); i++)
            {
                if (this.stages.get(i).getId() == raceId)
                {
                    raceStages[i] = this.stages.get(i).getId();
                }
            }
            return raceStages;
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
            if (location > stage.getLength())
            {
                throw new InvalidLocationException("Segment location is out of the provided stage's bounds.");
            }

            if (stage.getState() == StageState.WAITING)
            {
                throw new InvalidStageStateException("Stage is waiting for results; no segments can be added to it.");
            }

            if (stage.getType() == StageType.TT)
            {
                throw new InvalidStageTypeException("Time-trial segments can't contain any segments.");
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
        // checks succeed. Returns the id of the segment, or -1 if the checks fail.
        Stage stage = this.getStage(stageId);
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            if (location > stage.getLength())
            {
                throw new InvalidLocationException("Segment location is out of the provided stage's bounds.");
            }

            if (stage.getState() == StageState.WAITING)
            {
                throw new InvalidStageStateException("Stage is waiting for results; no segments can be added to it.");
            }

            if (stage.getType() == StageType.TT)
            {
                throw new InvalidStageTypeException("Time-trial segments can't contain any segments.");
            }

            Segment segment = new Segment(stageId, location, SegmentType.SPRINT, 0.0, 0.0);
            this.segments.add(segment);
            return segment.getId();
        }
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
        // Finds a segment by ID, then removes it from the segments attribute.
        Segment segment = this.getSegment(segmentId);
        if (segment == null)
        {
            throw new IDNotRecognisedException("Segment ID doesn't exist.");
        }
        else
        {
            this.segments.remove(segment); // !
        }
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
        // ! add more prep stuff? and method descriptor
        Stage stage = this.getStage(stageId);
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
        }
        else
        {
            stage.setState(StageState.WAITING);
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
            int[] segments = new int[this.segments.size()]; // Upper bound for number of segments
            for (int i=0; i<this.segments.size(); i++)
            {
                if (this.segments.get(i).getStageId() == stageId)
                {
                    segments[i] = this.segments.get(i).getId();
                }
            }
            return segments;
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
		for (int i=0; i<this.teams.size(); i++)
		{
			if (this.teams.get(i).getId() == teamId)
			{
				this.teams.remove(i);
				return;
			}
			throw new IDNotRecognisedException("No teams have the ID provided.");
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
		return teamIds;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
        // Return the riders of a team, or throws an exception.
		int[] teamRiders = new int[this.riders.size()]; // Upper bound for number of riders
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
                    teamRiders[i] = this.riders.get(i).getId();
                }
            }
            return teamRiders;
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

        //
        if (rider == null)
        {
            throw new IDNotRecognisedException("Rider ID doesn't exist.");
        }
        else
        {
            // !
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
                if (t.isAfter(t_prev))
                {
                    t_prev = t;
                    continue;
                }
                else
                {
                    throw new InvalidCheckpointsException("Invalid result: Checkpoint times are not chronological.");
                }
            }

            if (checkpoints.length != stage.getSegments().size() + 2)
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
		Stage stage = this.getStage(stageId);
        Rider rider = this.getRider(riderId);

        if (stage != null)
        {
            if (rider != null)
            {
                // Allowed cast, as stage.getRiderResults().get(riderId) is of type ArrayList<LocalTime>.
                LocalTime[] checkpoints = (LocalTime[])stage.getRiderResults().get(riderId).toArray();
                return checkpoints;
            }
            throw new IDNotRecognisedException("Rider ID doesn't exist.");
        }
        throw new IDNotRecognisedException("Stage ID doesn't exist.");
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Stage stage = this.getStage(stageId);
        Rider rider = this.getRider(riderId);

        if (stage != null)
        {
            if (rider != null)
            {
                HashMap<Integer, ArrayList<LocalTime>> riderResults = stage.getRiderResults();
                LocalTime[] checkpoints = (LocalTime[])riderResults.get(riderId).toArray();
                LocalTime finishingTime = checkpoints[checkpoints.length - 1];

                for (ArrayList<LocalTime> results : riderResults.values())
                {
                    LocalTime comparedFinishingTime = results.get(results.size()-1);
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
                    if (finish.equals(riderResults.get(key)) && sortedRidersIndex < sortedRiders.length)
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

            return (LocalTime[])sortedAdjustedFinishes.toArray();
        }
        throw new IDNotRecognisedException("Stage ID doesn't exist.");
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eraseCyclingPortal() {
        // ! Objects stored in these lists will be deleted when they are reset by the garbage collector.
		this.races = new ArrayList<Race>();
        this.stages = new ArrayList<Stage>();
        this.segments = new ArrayList<Segment>();
        this.riders = new ArrayList<Rider>();
        this.teams = new ArrayList<Team>();
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {

        for (int i=0; i<this.races.size(); i++)
        {
            FileOutputStream fos = new FileOutputStream(outdir + "race.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.races.get(i));
        }

        for (int i=0; i<this.stages.size(); i++)
        {
            FileOutputStream fos = new FileOutputStream(outdir + "stage.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.stages.get(i));
        }

        for (int i=0; i<this.segments.size(); i++)
        {
            FileOutputStream fos = new FileOutputStream(outdir + "segment.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.segments.get(i));
        }

        for (int i=0; i<this.riders.size(); i++)
        {
            FileOutputStream fos = new FileOutputStream(outdir + "rider.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.riders.get(i));
        }

        for (int i=0; i<this.teams.size(); i++)
        {
            FileOutputStream fos = new FileOutputStream(outdir + "team.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.teams.get(i));
        }
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
        this.eraseCyclingPortal(); // Loading assumes current state is not preserved.

        FileInputStream raceFin = new FileInputStream(outdir + "race.ser");
        ObjectInputStream raceOin = new ObjectInputStream(raceFin);
        while (raceOin.available() != 0)
        {
            // While there are more than 0 bytes to be read from the file...
            Object obj = raceOin.readObject();
            if (obj instanceof Race)
            {
                this.races.add((Race)obj);
            }
        }
        raceOin.close();

        FileInputStream stageFin = new FileInputStream(outdir + "stage.ser");
        ObjectInputStream stageOin = new ObjectInputStream(stageFin);
        while (stageOin.available() != 0)
        {
            // While there are more than 0 bytes to be read from the file...
            Object obj = stageOin.readObject();
            if (obj instanceof Stage)
            {
                this.stages.add((Stage)obj);
            }
        }
        stageOin.close();

        FileInputStream segmentFin = new FileInputStream(outdir + "segment.ser");
        ObjectInputStream segmentOin = new ObjectInputStream(segmentFin);
        while (segmentOin.available() != 0)
        {
            // While there are more than 0 bytes to be read from the file...
            Object obj = segmentOin.readObject();
            if (obj instanceof Segment)
            {
                this.segments.add((Segment)obj);
            }
        }
        segmentOin.close();

        FileInputStream riderFin = new FileInputStream(outdir + "rider.ser");
        ObjectInputStream riderOin = new ObjectInputStream(riderFin);
        while (riderOin.available() != 0)
        {
            // While there are more than 0 bytes to be read from the file...
            Object obj = riderOin.readObject();
            if (obj instanceof Rider)
            {
                this.riders.add((Rider)obj);
            }
        }
        riderOin.close();

        FileInputStream teamFin = new FileInputStream(outdir + "team.ser");
        ObjectInputStream teamOin = new ObjectInputStream(teamFin);
        while (teamOin.available() != 0)
        {
            // While there are more than 0 bytes to be read from the file...
            Object obj = teamOin.readObject();
            if (obj instanceof Team)
            {
                this.teams.add((Team)obj);
            }
        }
        teamOin.close();
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
        // Iteratively searches for the first (and only) Race with the given name, and removes it.
        for (int i=0; i < this.races.size(); i++)
        {
            if (this.races.get(i).getName() == name)
            {
                this.races.remove(i);
            }
        }
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

}
