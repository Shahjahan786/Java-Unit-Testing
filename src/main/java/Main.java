import java.util.Optional;

public class Main {
    public static void main(String args[]) throws Exception{
        VolcanoAnalyzer va = new VolcanoAnalyzer();
        va.loadVolcanoes(Optional.empty());

        va.topAgentsOfDeath();

    }
}
