package pages;



import helpers.WaitUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SearchPage extends AbstractBasePage {
    private static Logger LOGGER = Logger.getLogger(SearchPage.class.getName());

    @FindBy(xpath = "//input[@name='q']")
    private WebElement searchField;

    @FindBy(xpath = "//input[contains(@value, 'Google') and @type='submit']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[@id='res']")
    private WebElement resultData;

    @FindAll({@FindBy (xpath = "//h3[@class='r']/a")})
    private List<WebElement> resultDataLinks;


    public SearchPage(final boolean waitToLoad) {
        super(waitToLoad);
    }

    @Override
    protected void waitPageLoading() {
            WaitUtils.waitForElement(searchButton, "Search Page is not loaded!");
    }

    public SearchPage() {
        super(true);
    }


    public SearchPage fillForm(final String query) {
        LOGGER.info("Going to search query:" + query);
        searchField.sendKeys(query);
        searchButton.click();
        return this;
    }

    public void verifySearchSuccessful() {
        WaitUtils.waitForElement(resultData, "Search was not successful, next page not loaded!");
        LOGGER.info("Going to Verify res:" + resultDataLinks.size());
        LOGGER.info("Successfully search with text:\n" +
                resultDataLinks.stream()
                        .map(w -> w.getText())
                        .collect(Collectors.joining("\n")));
    }
}
