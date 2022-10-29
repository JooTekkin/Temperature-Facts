import edu.duke.DirectoryResource;
import edu.duke.FileResource;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;

public class InfoFacts {
    double coldestTemb = 0.0;
    public CSVRecord coldestHourInFile(CSVParser parser){
        CSVRecord coldestTemperature = null;
        double minTemp = 100 ;
        for(CSVRecord record : parser){
            double temp = Double.parseDouble(record.get(1));
            if( temp < minTemp){
                minTemp = temp;
                coldestTemperature = record;
            }
        }
        return coldestTemperature;
    }


    public String fileWithColdestTemperature(){
        String name = null;
        double minTemp = 100.0;
        DirectoryResource dr = new DirectoryResource();
        for(File file : dr.selectedFiles()){
            FileResource fr = new FileResource(file);
            CSVParser parser = fr.getCSVParser();
            CSVRecord record = coldestHourInFile(parser);
            double currTemp = Double.parseDouble(record.get("TemperatureF"));
            if(currTemp < minTemp){
                minTemp = currTemp;
                name = file.getName();
                coldestTemb = currTemp;
            }
        }

        return name;
    }



    public CSVRecord lowestHumidityInFile(CSVParser parser){
        int minHumidity = 100;
        CSVRecord lowestHumidity = null;
        for(CSVRecord record : parser){
            if(!Double.isNaN(Double.parseDouble(record.get("Humidity")))){
                int currHumidity = Integer.parseInt(record.get("Humidity"));
                if(currHumidity < minHumidity){
                    minHumidity = currHumidity;
                    lowestHumidity = record;
                }
            }
        }
        return lowestHumidity;
    }



    public CSVRecord lowestHumidityInManyFiles(){
        int minHumidity = 100;
        CSVRecord lowestHumidity = null;
        DirectoryResource dr = new DirectoryResource();
        for(File file : dr.selectedFiles()){
            FileResource fileResource = new FileResource(file);
            CSVParser parser = fileResource.getCSVParser();
            CSVRecord record = lowestHumidityInFile(parser);
            int currHumidity = Integer.parseInt(record.get("Humidity"));
            if(currHumidity < minHumidity){
                minHumidity = currHumidity;
                lowestHumidity = record;
            }
        }
        return lowestHumidity;
    }


    public double averageTemperatureInFile(CSVParser parser){
        double avgTemp = 0.0;
        double numsTemp = 0.0;
        for(CSVRecord record : parser){
            numsTemp++;
            avgTemp += Double.parseDouble(record.get("TemperatureF"));
        }
        return avgTemp/numsTemp;
    }


    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value){
        double avgTemp = 0.0;
        double numsTemp = 0.0;
        for(CSVRecord record : parser){
         double currHumi = Double.parseDouble(record.get("Humidity"));
         if(currHumi >= value){
             numsTemp++;
             avgTemp += Double.parseDouble(record.get("TemperatureF"));
         }
        }
        return avgTemp/numsTemp;
    }


    private static void getAllTemperature(String name) {
        File filePath = new File("C:\\Users\\yusuf\\IdeaProjects\\Temperature Facts\\nc_weather\\2014\\" + name);
        FileResource fileResource = new FileResource(filePath);
        CSVParser parser = fileResource.getCSVParser();
        for(CSVRecord record : parser){
            System.out.println(record.get("DateUTC") +" "+ record.get("TemperatureF"));
        }
    }





    public void testFileWithColdestTemperature(){
        String name = fileWithColdestTemperature();
        System.out.println("Coldest day was in file " + name);
        System.out.println("Coldest temperature on that day was " + coldestTemb);
        System.out.println("All Temperature on the coldest day were: ");
        getAllTemperature(name);
    }


    public void testLowestHumidityInFile(){
        FileResource fileResource = new FileResource();
        CSVParser parser = fileResource.getCSVParser();
        CSVRecord record = lowestHumidityInFile(parser);
        System.out.println("Lowest Humidity was " + record.get("Humidity") + " at " + record.get("DateUTC"));
    }


    public void testLowestHumidityInManyFiles(){
        CSVRecord record = lowestHumidityInManyFiles();
        System.out.println("Lowest Humidity was " + record.get("Humidity") + " at " + record.get("DateUTC"));
    }


    public void testAverageTemperatureInFile(){
        FileResource fileResource = new FileResource();
        CSVParser parser = fileResource.getCSVParser();
        double avgTemp = averageTemperatureInFile(parser);
        System.out.println("Average temperature in file is " + avgTemp);
    }



    public void testAverageTemperatureWithHighHumidityInFile(){
        FileResource fileResource = new FileResource();
        CSVParser parser = fileResource.getCSVParser();
        double avgTemp = averageTemperatureWithHighHumidityInFile(parser,80);
        if(Double.isNaN(avgTemp)){
            System.out.println("No temperatures with that humidity");
        } else {
            System.out.println("Average Temp when high Humidity is " + avgTemp);
        }
    }
}
