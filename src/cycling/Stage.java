package cycling;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Stage implements Serializable
{
    // Attributes
    private int id;
    private int raceId;
    private String name;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private StageState state;
    private ArrayList<Integer> orderedSegments;
    private HashMap<Integer, ArrayList<LocalTime>> riderResults;
    private static int numberOfStages; // Starts 0

    public Stage(int raceId, String name, String description, double length,
    LocalDateTime startTime, StageType type, StageState state)
    {
        this.raceId = raceId;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        this.state = state;
        this.orderedSegments = new ArrayList<Integer>();
        this.riderResults = new HashMap<Integer, ArrayList<LocalTime>>();
        this.id = ++numberOfStages;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getRaceId()
    {
        return this.raceId;
    }

    public void setRaceId(int raceId)
    {
        this.raceId = raceId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getLength()
    {
        return this.length;
    }

    public void setLength(double length)
    {
        this.length = length;
    }

    public LocalDateTime getStartTime()
    {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime)
    {
        this.startTime = startTime;
    }

    public StageType getType()
    {
        return this.type;
    }

    public void setType(StageType type)
    {
        this.type = type;
    }

    public StageState getState()
    {
        return this.state;
    }

    public void setState(StageState state)
    {
        this.state = state;
    }

    public ArrayList<Integer> getSegments()
    {
        return this.orderedSegments;
    }

    public void addSegment(int segmentId)
    {
        this.orderedSegments.add(segmentId);
    }

    public void setSegments(ArrayList<Integer> segmentIds)
    {
        this.orderedSegments = segmentIds;
    }

    public HashMap<Integer, ArrayList<LocalTime>> getRiderResults()
    {
        return this.riderResults;
    }

    public void addRiderResult(int riderId, LocalTime... checkpoints)
    {
        this.riderResults.put(riderId, new ArrayList<LocalTime>(Arrays.asList(checkpoints)));
    }

    public void setRiderResults(HashMap<Integer, ArrayList<LocalTime>> riderResults)
    {
        this.riderResults = riderResults;
    }

    public static int getNumberOfStages()
    {
        return numberOfStages;
    }
}
