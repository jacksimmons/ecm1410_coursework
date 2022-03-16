package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private List<Race> races;
	private List<Stage> stages;
    private List<Segment> segments;
	private List<Rider> riders;
	private List<Team> teams;

    // Constructor
    public CyclingPortal()
    {
        this.races = new List<Race>();
        this.stages = new List<Stage>();
        this.segments = new List<Segment>();
        this.riders = new List<Rider>();
        this.teams = new List<Team>();
    }

    // Added Methods
    public Race getRace(int raceId)
    {
        // Finds the corresponding race and returns it if it exists.
        // If it doesn't exist, null is returned.

        boolean raceIdFound = false;
        Race race = null;
        for (int i=0; i<this.races.size(); i++)
        {
            if (this.races.get(i).getId() == raceId)
            {
                raceIdFound = true;
                race = this.races.get(i);
            }
        }

        return race;
    }

    public Stage getStage(int stageId)
    {
        // Finds the corresponding stage and returns it if it exists.
        // If it doesn't exist, null is returned.

        boolean stageIdFound = false;
        Stage stage = null;
        for (int i=0; i<this.stages.size(); i++)
        {
            if (this.stages.get(i).getId() == stageId)
            {
                stageIdFound = true;
                stage = this.stages.get(i);
            }
        }

        return stage;
    }

    public Segment getSegment(int segmentId)
    {
        // Finds the corresponding segment and returns it if it exists.
        // If it doesn't exist, null is returned.

        boolean segmentIdFound = false;
        Segment segment = null;
        for (int i=0; i<this.segments.size(); i++)
        {
            if (this.segments.get(i).getId() == segmentId)
            {
                segmentIdFound = true;
                segment = this.segments.get(i);
            }
        }

        return segment;
    }

    // Methods
	@Override
	public int[] getRaceIds() {
        // Iteratively adds the ID of each race in the races list to raceIds, then returns raceIds.
        int[] raceIds = new int[races.size()];
		for (i=0; i < this.races.size(); i++)
        {
            raceIds.get(i) = this.races.get(i).getId();
        }
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
        // Creates a Race object, with necessary checks in place. Returns the Race ID or raises an exception.
		if (name.size() <= 30 && name != null && name != "")
		{
            // The name is of correct size, is not null and is not empty.
			for (i=0; i < name.size(); i++)
			{
				if (isWhitespace(name.charAt(i)))
				{
                    // The name has whitespace; invalid.
					throw new InvalidNameException("No whitespace allowed in race names.");
                    return -1;
                }
			}

			for (i=0; i<this.races.size(); i++)
			{
				if (this.races.get(i).getName() == name)
				{
                    // The name has already been taken.
					throw new IllegalNameException("This race name is already taken.");
                    return -1;
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
            return -1;
        }
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		Race race = this.getRace(raceId);
        if (race == null)
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
            return null;
        }
        else
        {
            return String.format("Race ID: %i\nName: %s\nDescription: %s\nNumber of Stages: %i\nLength: %d",
            raceId, race.getName(), race.getDescription(), race.getNumberOfStages(), race.getLength());
        }
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
        // Removes a race by ID or raises an exception.
        Race race = this.getRace(raceId);
        if (race == null)
        {
            throw new IDNotRecognisedException("Race ID doesn't exist.");
        }
        else
        {
            this.race.remove(race);
        }
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		return this.stages.size();
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
            throw new IDNotRecognisedException("ID does not match any race in the system.");
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
                if (stageName == null || stageName == "" || stageName.getLength() > 30)
                {
                    throw new InvalidNameException("Stage name has invalid length (1 to 30 characters) or is null.");
                }

                for (i=0; i < stageName.size(); i++)
                {
                    if (isWhitespace(stageName.charAt(i)))
                    {
                        // The name has whitespace; invalid.
                        throw new InvalidNameException("No whitespace allowed in stage names.");
                    }
                }

                // All checks passed if reaches here.
                Stage stage = new Stage(raceId, stageName, description, length, startTime, stageType);
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
                if (this.stages.get(i).raceId == raceId)
                {
                    raceStages[i] = this.stages.get(i).getId();
                }
            }
            return raceStages;
        }
        return new int[0];
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
        // Returns the length of a stage, or 0 if the stage ID provided doesn't exist.
        Stage stage = this.getStage(stageId);
        if (stage == null)
        {
            throw new IDNotRecognisedException("This stage ID doesn't exist.");
        }
        else
        {
            return stage.getLength();
        }
        return 0.0;
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		Stage stage = this.getStage(stageId);
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
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
            return -1;
        }
        else
        {
            if (location > stage.getLength())
            {
                throw new InvalidLocationException("Segment location is out of the provided stage's bounds.");
                return -1;
            }

            if (stage.getStageState() == StageState.WAITING)
            {
                throw new InvalidStageStateException("Stage is waiting for results; no segments can be added to it.");
                return -1;
            }

            if (stage.getStageType() == StageType.TT)
            {
                throw new InvalidStageTypeException("Time-trial segments can't contain any segments.");
                return -1;
            }

            Segment segment = new Segment(stageId, location, type, averageGradient, length);
            this.segments.add(segment);
            return segment.getId();
        }
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// TODO Auto-generated method stub
        Stage stage = this.getStage(stageId);
        if (stage == null)
        {
            throw new IDNotRecognisedException("Stage ID doesn't exist.");
            return -1;
        }
        else
        {
            if (location > stage.getLength())
            {
                throw new InvalidLocationException("Segment location is out of the provided stage's bounds.");
                return -1;
            }

            if (stage.getStageState() == StageState.WAITING)
            {
                throw new InvalidStageStateException("Stage is waiting for results; no segments can be added to it.");
                return -1;
            }

            if (stage.getStageType() == StageType.TT)
            {
                throw new InvalidStageTypeException("Time-trial segments can't contain any segments.");
                return -1;
            }

            Segment segment = new Segment(stageId, location, SegmentType.SPRINT, null, null);
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
        // Returns a list of segments ID of a stage, or an empty array.
        Stage stage = this.getStage(stageId);
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
                    segments.get(i) = this.segments.get(i).getId();
                }
            }
            return segments;
        }
        return new int[0];
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        // Creates a Team with the necessary checks. Either returns the Team ID or raises an exception.
        if (name.size() <= 30 && name != null && name != "")
		{
			for (i=0; i < name.size(); i++)
			{
				if (isWhitespace(name.charAt(i)))
				{
					throw new InvalidNameException("No whitespace allowed in team names.");
                    return -1;
                }
			}

			for (i=0; i<this.teams.size(); i++)
			{
				if (this.teams.get(i).getName() == name)
				{
					throw new IllegalNameException("This team name is already taken.");
                    return -1;
                }
			}

			Team team = new Team(name, description);
            this.teams.add(team);
            return team.getId();
		}
		else
		{
            throw new InvalidNameException("Team name length is of incorrect length (1 to 30 characters) or is null.");
            return -1;
        }
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		for (i=0; i<this.teams.size(); i++)
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
		// ! annoying return type
		int[] teamIds = new int[this.teams.size()];
		for (i=0; i<this.teams.size(); i++)
		{
			teamIds.add(this.teams.get(i));
		}
		return teamIds;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
        // Return the riders of a team, or an empty array if the ID is invalid.
		int[] teamRiders = new int[this.riders.size()]; // Upper bound for number of riders
        Team team = this.getTeam(teamId);
        if (team == null)
        {
            throw new IDNotRecognisedException("Team ID doesn't exist.");
        }
        else
        {
            for (i=0; i<this.riders.size(); i++)
            {
                if (this.riders.get(i).getTeamId() == teamId)
                {
                    teamRiders[i] = this.riders.get(i);
                }
            }
            return teamRiders;
        }
        return new int[0];
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		// Creates a rider and returns its ID if successful. If not, throws an error and returns -1.
		if (yearOfBirth >= 1900)
		{
			if (name != null)
			{
				for (i=0; i<this.teams.size(); i++)
				{
					if (this.teams.get(i).getName() == name)
					{
						throw new IllegalNameException("This name is already taken.");
						// code end
					}
				}

				Rider rider = new Rider(teamID, name, yearOfBirth);
				this.riders.add(rider);
				return rider.getId();
			}
			else
			{
				throw new IllegalArgumentException("Name of the rider cannot be null.");
			}
		}
		else
		{
			throw new IllegalArgumentException("Year of birth must be at least 1900.");
		}
        return -1;
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
        // Iteratively searches for the first (and only) Rider with the given ID, and removes it.
        for (int i=0; i < this.riders.size(); i++)
        {
            if (this.riders.get(i).getId() == riderId)
            {
                this.riders.remove(i);
            }
        }
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
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
		races = new List<Race>();
        stages = new List<Stage>();
        segments = new List<Segment>();
        riders = new List<Rider>();
        teams = new List<Team>();
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {

        String outdir = System.getProperty("user.dir");
        outdir = outdir.substring(0, outdir.length() - 12); // Subtracts "\src\cycling"
        outdir += "\res";

        for (int i=0; i<this.races.size(); i++)
        {
            FileOutputStream fos = new FileOutputStream(cwd + "race.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.races.get(i));
        }
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
        // Iteratively searches for the first (and only) Race with the given name, and removes it.
        for (int i=0; i < races.size(); i++)
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
