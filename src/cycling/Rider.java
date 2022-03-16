package cycling;

public class Rider implements Serializable
{
    // Attributes
    private int id;
    private int teamID;
    private String name;
    private int yearOfBirth;
    private static int numberOfRiders;

    public Rider(int teamID, String name, int yearOfBirth)
    {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.id = ++numberOfRiders;
    }

    public int getId()
    {
        return this.id;
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
