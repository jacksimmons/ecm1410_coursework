package cycling;

public class Stage implements Serializable
{
    // Attributes
    private int raceId;
    private String name;
    private String description;
    private double length;
    private LocalDateTime startTime;

    public Stage(int raceId, String name, String description, double length, LocalDateTime startTime)
    {
        this.raceId = raceId;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
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
}
