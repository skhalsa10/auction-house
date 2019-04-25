package Auction.AuctionHouse;

import java.io.IOException;
import java.nio.file.Files;
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

    public ItemGenerator() {
        aCount = 0;
        nCount = 0;
        adjectives = new ArrayList<>();
        nouns = new ArrayList<>();
        random = new Random();
        initLists();
    }

    /**
     *
     */
    private void initLists() {
        try {
            adjectives = (ArrayList<String>)Files.readAllLines(Paths.get("./resources/adjectives.txt"));
            aCount = adjectives.size();
            nouns = (ArrayList<String>)Files.readAllLines(Paths.get("./resources/nouns.txt"));
            nCount = nouns.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Item getItem(){
        adjective = adjectives.get(random.nextInt(aCount+1));
        noun = nouns.get(random.nextInt(nCount+1));
        //System.out.println(adjective + " " + noun);
        return (new Item(adjective + " " + noun, 1));
    }

    public static void main(String args[]){
        ItemGenerator test = new ItemGenerator();
        System.out.println(test.getItem());
    }


}
