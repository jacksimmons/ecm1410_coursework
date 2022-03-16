public class Team implements Serializable
{
    private int id;
    private String name;
    private String description;
    private static int numberOfTeams;

    public Team(String name, String description)
    {
        this.name = name;
        this.description = description;
        this.id = ++numberOfTeams;
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

    public static int getNumberOfTeams()
    {
        return numberOfTeams;
    }
}
