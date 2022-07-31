import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

public class TwitterAutoBot {

    public static void main(String[] args) throws TwitterException {
        tweetLines();
        reTweet();
        markFav();
    }

    private static void tweetLines() {
        String line;
        try {
            try (
                    InputStream fis = new FileInputStream("C:\\Users\\Sutej\\IdeaProjects\\TwitterAutoBot\\src\\main\\resources\\Tweets.txt");
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("Cp1252"));
                    BufferedReader br = new BufferedReader(isr)
            ) {
                while ((line = br.readLine()) != null) {
                    // Deal with the line
                    sendTweet(line);
                    System.out.println("Tweeting: " + line + "...");

                    try {
                        System.out.println("Sleeping for 30 minutes...");
                        Thread.sleep(1800000); // every 30 minutes
                        // Thread.sleep(10000); // every 10 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendTweet(String line) {
        Twitter twitter = TwitterFactory.getSingleton();
        Status status;
        try {
            status = twitter.updateStatus(line);
            System.out.println(status);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private static void reTweet() throws TwitterException {
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        FileReader reader= null;
        try {
            reader = new FileReader("twitter4j.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties p = new Properties();
        try {
            p.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        twitter.setOAuthConsumer(p.getProperty("oauth.consumerKey"), p.getProperty("oauth.consumerSecret"));
        AccessToken accessToken = new AccessToken(p.getProperty("oauth.accessToken"), p.getProperty("oauth.accessTokenSecret"));
        twitter.setOAuthAccessToken(accessToken);
        twitter.retweetStatus(Long.parseLong(p.getProperty("tweetId")));
    }

    private static void markFav(){

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        FileReader reader= null;
        try {
            reader = new FileReader("twitter4j.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties p = new Properties();
        try {
            p.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        twitter.setOAuthConsumer(p.getProperty("oauth.consumerKey"), p.getProperty("oauth.consumerSecret"));
        AccessToken accessToken = new AccessToken(p.getProperty("oauth.accessToken"), p.getProperty("oauth.accessTokenSecret"));
        twitter.setOAuthAccessToken(accessToken);
        try {
            Status status = twitter.createFavorite(Long.parseLong(p.getProperty("tweetId")));
        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }

}


