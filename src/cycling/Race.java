package cycling;

public class Race implements Serializable
{
    // Attributes
    private int id;
    private String name;
    private String description;
    private static int numberOfRaces;

    public Race(String name, String description)
    {
        this.name = name;
        this.description = description;
        this.id = ++numberOfRaces;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public static int getNumberOfRaces()
    {
        return numberOfRaces;
    }
}
