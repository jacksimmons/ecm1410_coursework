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

    public double getLocation()
    {
        return this.location;
    }

    public SegmentType getType()
    {
        return this.type;
    }

    public double getAverageGradient()
    {
        return this.averageGradient;
    }

    public double getLength()
    {
        return this.length;
    }

    public static int getNumberOfSegments()
    {
        return this.numberOfSegments;
    }
}
