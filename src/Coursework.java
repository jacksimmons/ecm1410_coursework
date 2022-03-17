import cycling.*;

public class Coursework
{
    public static void main(String[] args)
    {
        CyclingPortal cyclingPortal = new CyclingPortal();
        cyclingPortal.createTeam();
        System.out.println(cyclingPortal.getTeams().type);
    }
}
