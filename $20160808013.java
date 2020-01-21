import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


//PinarKok20160808013


public class $20160808013 {
    public static void main(String[] args) {
        String fileName = args[0];
        try (FileReader reader = new FileReader(fileName);
             BufferedReader processes = new BufferedReader(reader)) {

            List<HashMap<String, ArrayList<int[]>>> processArray = new ArrayList<>();

            List<String> text = processes.lines().collect(Collectors.toList());

            for(int i=0;i<text.size();i++){

                String[] parts = text.get(i).split(":");
                String PID = parts[0];
                String burstTimes = parts[1];

                HashMap<String, ArrayList<int[]>> processList = new HashMap<String, ArrayList<int[]> >();
                ArrayList<int[]> arr = new ArrayList<int[]>();
                String[] b = burstTimes.split(";");
                for (int j=0;j<b.length;j++){
                    String[] aa = (b[j].split("[\\(\\)]")[1]).split(",");
                    int[] bb = new int[2];
                    for(int k=0;k<2;k++){
                        bb[k] = Integer.parseInt(aa[k]);
                        if(bb[k]<0) bb[k]=0;
                    }
                    arr.add(bb);
                }

                processList.put(PID, arr);
                processArray.add(processList);

            }
            int[] returnTimes = new int[processArray.size()];
            Arrays.fill(returnTimes,0);

            int time = 0;
            int index = 0;
            int[] bb = null;
            int[] cpuTimes = new int[processArray.size()];
            System.out.println("Time\tPID\tBurst\tReturn time");
            while(firstNotNull(processArray)!=null){

                if(returnTimes[index]<=time && firstNotNull(processArray)!=null){

                    int i = getProcIndex(processArray,index);

                    int[] procceses = processArray.get(index).get("P" + (index + 1)).get(i);
                    processArray.get((index)).get("P" + (index + 1)).set(i, null);

                    cpuTimes[index] += procceses[0];
                    int burst;
                    if(procceses[0] < 0) burst =0;
                    else burst = procceses[0];

                    returnTimes[index] = time + burst + procceses[1];
                    System.out.print(time+"\t"+"P"+(index+1)+"\t"+"("+procceses[0]+","+(procceses[1]==0 ? -1:procceses[1])+")"+"\t");
                    if(firstNotNull(processArray)!=null) {
                        time += burst ;

                    }

                    System.out.println(returnTimes[index]);
                    try{
                        bb = firstNotNull(processArray);
                        index = bb[0];
                    }
                    catch (Exception e){
                        break;
                    }

                }

                else {

                    if(index!=processArray.size()-1){

                        index++;
                    }
                    else {
                        time++;
                        index = 0;
                    }
                }

            }
            System.out.println();
            System.out.println("Total time :\t " + time);
            System.out.println();
            double awg = 0;
            double att = 0;
            for(int i=0; i<processArray.size(); i++){
                System.out.println("Waiting time (P"+(i+1)+ ") :\t "+ (returnTimes[i] - cpuTimes[i]));
                System.out.println("Turnaround time (P"+(i+1)+ ") :\t " + (returnTimes[i]));
                System.out.println();
                awg += (returnTimes[i] - cpuTimes[i]);
                att += (returnTimes[i]);
            }
            System.out.println("AVERAGE WAITING TIME :\t" +(awg/processArray.size()));
            System.out.println("AVERAGE TURNAROUND TIME :\t" +(att/processArray.size()));

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }



    public static int[] firstNotNull(List<HashMap<String, ArrayList<int[]>>> processArray){
        for(int index =0;index<processArray.size();index++){
            for(int i=0;i<processArray.get(index).get("P"+(index+1)).size();i++){
                if(processArray.get((index)).get("P" + (index + 1)).get(i)!=null){
                    int[] arr = new int[2];
                    arr[0] = index;
                    arr[1] = i;
                    return arr;
                }
            }
        }
        return null;
    }
    public static int getProcIndex(List<HashMap<String, ArrayList<int[]>>> processArray,int index){
        for(int i=0;i<processArray.get(index).get("P"+(index+1)).size();i++){
            if(processArray.get((index)).get("P" + (index + 1)).get(i)!=null){
                return i;
            }
        }

        return 0;
    }

}