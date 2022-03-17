package cycling;

import java.io.Serializable;

public class Rider implements Serializable
{
    // Attributes
    private int id;
    private int teamId;
    private int raceId;
    private String name;
    private int yearOfBirth;
    private static int numberOfRiders; // Starts 0

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

    public void setId(int id)
    {
        this.id = id;
    }

    public int getTeamId()
    {
        return this.teamId;
    }

    public void setTeamId(int teamId)
    {
        this.teamId = teamId;
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

    public int getYearOfBirth()
    {
        return this.yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth)
    {
        this.yearOfBirth = yearOfBirth;
    }

    public static int getNumberOfRiders()
    {
        return numberOfRiders;
    }
}
