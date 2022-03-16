package cycling;

public class Rider implements Serializable
{
    // Attributes
    private int id;
    private int teamId;
    private int raceId;
    private String name;
    private int yearOfBirth;
    private static int numberOfRiders;

    public Rider(int teamId, int raceId, String name, int yearOfBirth)
    {
        this.teamId = teamId;
        this.raceId = raceId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.id = ++numberOfRiders;
    }

    public int getId()
    {
        return this.id;
    }

    public int getTeamId()
    {
        return this.teamId;
    }

    public int getRaceId()
    {
        return this.raceId;
    }

    public String getName()
    {
        return this.name;
    }

    public int getYearOfBirth()
    {
        return this.yearOfBirth
    }

    public static int getNumberOfRiders()
    {
        return numberOfRiders;
    }
}
