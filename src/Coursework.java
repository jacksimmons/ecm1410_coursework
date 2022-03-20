import cycling.*;

public class Coursework
{
    public static void main(String[] args)
    {
        CyclingPortal cyclingPortal = new CyclingPortal();
        try {
            cyclingPortal.createTeam("Cyclists", "The Best Cyclists");
        } catch (IllegalNameException e) {
            e.printStackTrace();
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }
}
