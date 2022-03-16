package cycling;

public class Stage implements Serializable
{
    // Attributes
    private int id;
    private int raceId;
    private String name;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private static int numberOfStages; // Starts 0

    public Stage(int raceId, String name, String description, double length,
    LocalDateTime startTime, StageType stageType, List)
    {
        this.raceId = raceId;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.stageType = stageType;
        this.id = ++numberOfStages;
    }

    public int getId()
    {
        return this.id;
    }

    public int getRaceId()
    {
        return this.raceId;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public double getLength()
    {
        return this.length;
    }

    public LocalDateTime getStartTime()
    {
        return this.startTime;
    }

    public StageType getStageType()
    {
        return this.stageType;
    }

    public static int getNumberOfStages()
    {
        return numberOfStages;
    }
}
