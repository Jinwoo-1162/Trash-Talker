import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Midi {
    public static void main(String[] args) throws Exception
    {
        Gson gson = new Gson();
        File toExport = new File("/Users/jinwoopark/Desktop/Gatech/Hack GT Horizons/src/toExport.txt");
        FileWriter myWriter = new FileWriter("/Users/jinwoopark/Desktop/Gatech/Hack GT Horizons/src/toExport.txt");

        // 1. JSON file to Java object
        Object jstring = gson.fromJson(new FileReader("/Users/jinwoopark/Desktop/Gatech/Hack GT Horizons/src/birthday.js"), Object.class);

        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fileData = new String(Files.readAllBytes(Paths
                .get("/Users/jinwoopark/Desktop/Gatech/Hack GT Horizons/src/birthday.js")));
        //System.out.print(gson.fromJson(fileData, Note.class));

        JsonElement jsonElement = new JsonParser().parse(fileData);

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray tracks = jsonObject.getAsJsonArray("track");

        JsonElement event = tracks.get(1);
        JsonElement setTempo = tracks.get(0);

        //in beats per minute
        int tempo = 120;
        for (JsonElement ele : setTempo.getAsJsonObject().getAsJsonArray("event")) {
           if (ele.getAsJsonObject().get("metaType").getAsInt() == 81) {
                tempo = ele.getAsJsonObject().get("data").getAsInt();
                break;
            }
        }

        int ticksPBeat = jsonObject.get("timeDivision").getAsJsonPrimitive().getAsInt();

        Iterator<JsonElement> eventIt = event.getAsJsonObject().getAsJsonArray("event").iterator();

        int count = 0;
        List changes = new ArrayList<int[]>();
        int min = 1000000000;

        while (eventIt.hasNext() && count != event.getAsJsonObject().getAsJsonArray("event").size() - 1) {
            int channel = event.getAsJsonObject().getAsJsonArray("event").get(count).getAsJsonObject()
                    .get("type").getAsJsonPrimitive().getAsInt();
            int deltaTime = event.getAsJsonObject().getAsJsonArray("event").get(count).getAsJsonObject()
                    .get("deltaTime").getAsJsonPrimitive().getAsInt();
            if (min > deltaTime && deltaTime != 0) {
                min = deltaTime;
            }
            JsonElement data = event.getAsJsonObject().getAsJsonArray("event").get(count);
            String noteS = data.getAsJsonObject().get("data").toString();
            if (channel == 9 || channel == 8) {
                int note = Integer.parseInt(noteS.substring(1, noteS.indexOf(',')));
                //System.out.print(" | " + deltaTime + " ");
                //System.out.print(note % 12 + " | ");
                int[] temp = {deltaTime, channel - 8, note % 12};
                changes.add(temp);
                System.out.print(Arrays.toString(temp));
            }
            //System.out.println(Arrays.toString(base));
            count++;
        }
        System.out.println();

        int size = changes.size();
        int scale = (int) Math.pow((double) min / ticksPBeat, -1);
        int indexCount = 0;
        int beats = 0;

        System.out.println();
        System.out.println("******* The Dot Matrix *******");
        System.out.println();
        System.out.println(" 0  1  2  3  4  5  6  7  8  9  10 11");
        while (size - 1 > indexCount) {
            int[] base = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            while (((int[]) changes.get(indexCount))[0] == 0) {
                base[((int[]) changes.get(indexCount))[2]] = ((int[]) changes.get(indexCount))[1];
                indexCount++;
            }

            int loops = ((int[]) changes.get(indexCount))[0] / min;

            for (int i = 0; i < loops; i++) {
                System.out.println(Arrays.toString(base));
                myWriter.write("1");
                for (int num : base) {
                    myWriter.write("" + num);
                }
                myWriter.write("\n");
            }

            if (indexCount == size - 1) {
                break;
            }

            int[] base2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            while (((int[]) changes.get(indexCount))[0] != 0) {
                base2[((int[]) changes.get(indexCount))[2]] = ((int[]) changes.get(indexCount))[1];
                indexCount++;
            }

            int loops2 = ((int[]) changes.get(indexCount))[0] / min;

            for (int i = 0; i < loops2; i++) {
                System.out.println(Arrays.toString(base2));
                for (int num : base) {
                    myWriter.write("" + 1 + num);
                }
                myWriter.write("\n");
            }

            /**
            while (((int[]) changes.get(indexCount))[0] == 0) {
                base[((int[]) changes.get(indexCount))[2]] = ((int[]) changes.get(indexCount))[1];
                indexCount++;
            }



            for (int i = 0; i < ((int[]) changes.get(indexCount))[0]; i++) {
                System.out.println(Arrays.toString(base));
                indexCount++;
                beats++;
                if (indexCount == size) {
                    break;
                }
            }
             */
        }
        System.out.print(beats + " " + min + " " + scale);
        myWriter.close();
        //JsonElement notes = event.getAsJsonArray("");
    }
} 