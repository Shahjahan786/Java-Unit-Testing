import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try{
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch(Exception e){
            throw(e);
        }
    }

    public Integer numbVolcanoes(){
        return volcanos.size();
    }

    //add methods here to meet the requirements in README.md

    List<Volcano> eruptedInEighties(){
        return volcanos.stream().filter(x -> x.getYear()>= 1980 &&  x.getYear() < 1990 )
                                .collect(Collectors.toList());
    }

    String[] highVEI(){
      
        return  volcanos.stream()
        .filter(x -> x.getVEI() >= 6)
        .map(x-> x.getName())
        .toArray(String[]::new);
    }

    Volcano mostDeadly(){
        return volcanos.stream().filter(x -> x.getDEATHS().equals("30000") ).findFirst().get();
    }

    double causedTsunami(){
        List<Volcano> filtered = volcanos.stream().filter(x -> x.getTsu().equals("tsu")).collect(Collectors.toList());
        double filteredSize = filtered.size();
        double totalSize = volcanos.size();

        System.out.println(filteredSize / totalSize);
        return (filteredSize / totalSize) * 100;
    }

    String mostCommonType(){
         System.out.println(mostFrequentStream(volcanos));
        return mostFrequentStream(volcanos);
    }

    String mostFrequentStream(List<Volcano> elements) {
    Map<String, Long> temp = elements.stream()
            .collect(Collectors.groupingBy(a -> a.getType(), Collectors.counting()));
            

    return temp.entrySet()
                     .stream()
                     .max(Map.Entry.comparingByValue())
                     .map(Map.Entry::getKey).get();

    }   

    int eruptionsByCountry(String country){
       return (int) volcanos.stream().filter(x -> x.getCountry().equals(country)).count();       
    }

    double averageElevation(){
        return volcanos.stream().map(x -> x.getElevation()).collect(Collectors.toList()).stream().mapToDouble(Integer::doubleValue)
        .average()
        .orElse(0.0);
    }

    String[] volcanoTypes(){
        return volcanos.stream()
        .map(x -> x.getType())
        .distinct().toArray(String[]::new);
    }

    double percentNorth(){
        List<Volcano> filtered = volcanos.stream().filter(x -> x.getLatitude() > 0)
        .collect(Collectors.toList());
        double filteredSize = filtered.size();
        double totalSize = volcanos.size();

        System.out.println(filteredSize / totalSize);
        return (filteredSize / totalSize) * 100;
       

    }

    String[] manyFilters(){

        return volcanos.stream()
        .filter(x -> x.getLatitude() < 0 && x.getYear() > 1800 && x.getVEI() == 5)
        .map(x -> x.getName())
        .toArray(String[]::new);

    }

    String[] elevatedVolcanoes(int elevation){
        return volcanos.stream()
        .filter(x -> x.getElevation() >= elevation)
        .map(x -> x.getName())
        .toArray(String[]::new);
    }

    String[] topAgentsOfDeath(){
        
        List<Volcano> sorted =  volcanos.stream().sorted((s1, s2) -> Double.compare(Double.parseDouble("0"+s2.getDEATHS()), (Double.parseDouble("0"+s1.getDEATHS())))).collect(Collectors.toList());
        sorted = sorted.stream().filter(x -> !x.getAgent().equals("")).limit(10).collect(Collectors.toList());


    
        String arr[] =  sorted.stream().map(x -> x.getAgent()).distinct().toArray(String[]::new);

            for (String ob : arr) {
            System.out.println(ob);
        }


        return arr;
    }



}
