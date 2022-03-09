package cycling;

public class Rider
{
    // Attributes
    private int id;
    private int teamID;
    private String name;
    private int yearOfBirth;

    public Rider(int id, int teamID, String name, int yearOfBirth)
    {
        this.id = id;
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public int getId()
    {
        return this.id;
    }

}