package Auction.AuctionHouse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;


/**
 * this class will generate random items. for now these items just have a String that represents them.
 * It will pick a random adjective and a random noun. and return an item that contains that.
 */
public class ItemGenerator {
    private ArrayList<String> adjectives;
    private ArrayList<String> nouns;
    private String adjective;
    private String noun;
    private int aCount;
    private int nCount;
    private Random random;
    private int counterID;

    public ItemGenerator() {
        aCount = 0;
        nCount = 0;
        counterID = 1;
        adjectives = new ArrayList<>();
        nouns = new ArrayList<>();
        random = new Random();
        initLists();
    }

    /**
     * this will just initialize the arrays that are used to build random items
     */
    private void initLists() {

        ClassLoader cl = this.getClass().getClassLoader();
        BufferedReader aReader = new BufferedReader(new InputStreamReader(cl.getResourceAsStream("adjectives.txt")));
        BufferedReader nReader = new BufferedReader(new InputStreamReader(cl.getResourceAsStream("nouns.txt")));

        adjectives = getListofWords(aReader);
        nouns = getListofWords(nReader);
        //adjectives = (ArrayList<String>)Files.readAllLines(Paths.get("adjectives.txt"));
        aCount = adjectives.size();
        //nouns = (ArrayList<String>)Files.readAllLines(Paths.get(this.getClass().getResource("nouns.txt").toURI()));
        nCount = nouns.size();
        try {
            aReader.close();
            nReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> getListofWords(BufferedReader aReader) {
        ArrayList<String> list = new ArrayList<>();
        String line = null;
        try {
            line = aReader.readLine();

            while(line != null){
                list.add(line);
                line = aReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     *
     * @return this will return a newly generated ITEM for the Auction house's selling pleasure.
     */
    public Item getItem(){
        adjective = adjectives.get(random.nextInt(aCount));
        noun = nouns.get(random.nextInt(nCount));
        //System.out.println(adjective + " " + noun);
        return (new Item(adjective + " " + noun, counterID++));

    }

}
