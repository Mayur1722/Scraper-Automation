package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
    Wrappers wrapper;
    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */


    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);
        wrapper = new Wrappers(driver);
        driver.manage().window().maximize();
    }

    @Test
    public void testCase01() throws InterruptedException {

        System.out.println("Start: TestCase01");

        // Navigate to the website using wrapper method
        String url = "https://www.scrapethissite.com/pages/";
        wrapper.navigateToUrl(url);

        // Initialize explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click on "Hockey Teams: Forms, Searching and Pagination"
        List<WebElement> titleOptions = driver.findElements(By.xpath("//div[@class='page']//a"));

        // Iterate over the list of title to select Hockey Teams: Forms, Searching and Pagination
        for (WebElement titleOption : titleOptions) {
            try {
                String title = titleOption.getText();
                if (title.equalsIgnoreCase("Hockey Teams: Forms, Searching and Pagination")) {

                    wait.until(ExpectedConditions.visibilityOf(titleOption));
                    wrapper.clickOnElement(titleOption);
                    break; // Exit loop after clicking
                }
            } catch (StaleElementReferenceException e) {
                // Re-fetch elements
                titleOptions = driver.findElements(By.xpath("//div[@class='page']//a"));
            }
        }

        Thread.sleep(3000);

        // Create a list to store team data
        List<HashMap<String, Object>> teamDataList = new ArrayList<>();

        // Iterate over the pages to collect data
        for (int i = 2; i <= 5; i++) {
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));

            // Iterate over each row to extract data
            for (WebElement row : rows) {
                List<WebElement> columns = row.findElements(By.tagName("td"));

                if (columns.size() >= 3) {

                    // Extract team name from first column
                    String teamName = columns.get(0).getText();
                    System.out.println("team Name : " + teamName);

                    // Extract year from second column
                    String year = columns.get(1).getText();
                    System.out.println("year :" + year);

                    // Extract win percentage and convert it to double
                    double winPercentage = Double.parseDouble(columns.get(5).getText());
                    System.out.println("win Percentage :" + winPercentage);

                    // Store team data only if the win percentage is less than 0.40
                    if (winPercentage < 0.40) {
                        HashMap<String, Object> teamData = new HashMap<>();
                        teamData.put("Epoch Time", Instant.now().getEpochSecond());
                        teamData.put("Team Name", teamName);
                        teamData.put("Year", year);
                        teamData.put("win percentage", winPercentage);
                        teamDataList.add(teamData);
                    }
                }
            }
            try {
                // Locate the pagination button for the next page
                WebElement pageButton = driver.findElement(By.xpath("//ul[@class='pagination']//li/a[@href='/pages/forms/?page_num=" + i + "']"));

                if (pageButton.isDisplayed() && pageButton.isEnabled()) {
                    wrapper.clickOnElement(pageButton);
                    Thread.sleep(2000);

                } else {
                    System.out.println("Page " + i + " is not available. Stopping pagination.");
                    break;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Could not find page " + i + ". Stopping pagination.");
                break;
            }
        }

        // Initialize object mapper to write data to a JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            // Enable pretty-printing of JSON data
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Write team data to a JSON file
            objectMapper.writeValue(new File("hockey-team-data.json"), teamDataList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("End: TestCase01");
    }

    @Test
    public void testCase02() throws InterruptedException {

        System.out.println("Start: TestCase02");
        final String OUTPUT_FOLDER = "./output";

        // Navigate to the website using wrapper method
        String url = "https://www.scrapethissite.com/pages/";
        wrapper.navigateToUrl(url);

        // Initialize WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Iterate over the list of title to select Oscar Winning Films: AJAX and Javascript
        List<WebElement> titleOptions = driver.findElements(By.xpath("//div[@class='page']//a"));
        for (WebElement titleOption : titleOptions) {
            try {
                String title = titleOption.getText();
                if (title.equalsIgnoreCase("Oscar Winning Films: AJAX and Javascript")) {

                    // Wait until the element is visible before clicking
                    wait.until(ExpectedConditions.visibilityOf(titleOption));
                    wrapper.clickOnElement(titleOption);
                    break; // Exit loop after clicking
                }
            } catch (StaleElementReferenceException e) {
                // Handle stale element exception by re-fetching the elements
                titleOptions = driver.findElements(By.xpath("//div[@class='page']//a"));
            }
        }

        ArrayList<HashMap<String, Object>> moviesData = new ArrayList<>();

        // Find all available years for desired movie
        List<WebElement> years = driver.findElements(By.xpath("//div[@class='col-md-12 text-center']/a"));

        for (WebElement year : years) {
            String yearText = year.getText();

            // Click on the year to view movie details
            wrapper.clickOnElement(year);
            Thread.sleep(3000);

            // Find all movies listed for the selected year
            List<WebElement> movies = driver.findElements(By.xpath("//table/tbody/tr"));
            System.out.println("size of movies " + movies.size());

            // Iterate over the first 5 movies
            for (int i = 0; i < Math.min(movies.size(), 5); i++) {

                movies = driver.findElements(By.xpath("//table/tbody/tr"));

                WebElement movie = movies.get(i);

                // Extract movie title
                WebElement movieTitleElement = movie.findElement(By.xpath(".//td[@class='film-title']"));
                String movieText = movieTitleElement.getText();

                // Extract movie nomination details
                WebElement nominationElement = movie.findElement(By.xpath(".//td[@class='film-nominations']"));
                String nominationText = nominationElement.getText();

                // Extract movie award details
                WebElement awardsElement = movie.findElement(By.xpath(".//td[@class='film-awards']"));
                String awardsText = awardsElement.getText();

                // Check if the movie won the "Best Picture" award
                boolean isWinner = nominationText.contains("Best Picture");

                // Store details in a HashMap
                HashMap<String, Object> movieData = new HashMap<>();
                movieData.put("Epoch Time", Instant.now().getEpochSecond());
                movieData.put("year", year);
                movieData.put("title", movieText);
                movieData.put("nomination", nominationText);
                movieData.put("awards", awardsText);
                movieData.put("isWinner", isWinner);

                // Add movie data to the list
                moviesData.add(movieData);
            }
        }

        // Create output folder if it doesn't exist
        File outputFolder = new File(OUTPUT_FOLDER);
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }

        File outputFile = new File(OUTPUT_FOLDER + "/" + "oscar-winner-data.json");
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Enable pretty-print formatting
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Prepare a list for serialization
            List<HashMap<String, Object>> serializeMoviesData = new ArrayList<>();

            //.toString() ensures each value is converted to a String, making it safe for serialization.
            for (HashMap<String, Object> movie : moviesData) {
                HashMap<String, Object> cleanedMovie = new HashMap<>();
                cleanedMovie.put("epochTime", movie.get("Epoch Time").toString());
                cleanedMovie.put("year", movie.get("year").toString());
                cleanedMovie.put("title", movie.get("title").toString());
                cleanedMovie.put("nomination", movie.get("nomination").toString());
                cleanedMovie.put("awards", movie.get("awards").toString());
                cleanedMovie.put("isWinner", movie.get("isWinner"));

                serializeMoviesData.add(cleanedMovie);
            }
            // Write movie data to the JSON file
            mapper.writeValue(outputFile, serializeMoviesData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Assertions to verify JSON file creation and data presence
        Assert.assertTrue(outputFile.exists(), "JSON file should be present");
        Assert.assertTrue(outputFile.length() > 0, "JSON file should not be empty");

        System.out.println("End: TestCase02");
    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();

    }
}