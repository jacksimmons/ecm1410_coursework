package cycling;

public class Segment implements Serializable
{
    // Attributes
    private int id;
    private int stageId;
    private double location; // Finishing location
    private SegmentType type;
    private double averageGradient;
    private double length;
    private static int numberOfSegments;

    public Segment(int stageId, double location, SegmentType type,
    double averageGradient, double length)
    {
        this.stageId = stageId;
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
        this.id = ++numberOfSegments;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getLocation()
    {
        return this.location;
    }

    public void setLocation(double location)
    {
        this.location = location;
    }

    public SegmentType getType()
    {
        return this.type;
    }

    public void setType(SegmentType segmentType)
    {
        this.type = segmentType;
    }

    public double getAverageGradient()
    {
        // Note that this method can return null, as addIntermediateSprintToStage doesn't provide an averageGradient.
        return this.averageGradient;
    }

    public void setAverageGradient(double averageGradient)
    {
        this.averageGradient = averageGradient;
    }

    public double getLength()
    {
        // Note that this method can return null, as addIntermediateSprintToStage doesn't provide a length.
        return this.length;
    }

    public void setLength(double length)
    {
        this.length = length;
    }

    public static int getNumberOfSegments()
    {
        return this.numberOfSegments;
    }
}
