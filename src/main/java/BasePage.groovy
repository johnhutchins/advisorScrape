import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

class BasePageScrape {
    static final BASE_URL = 'https://www.virtuoso.com/advisors#all'
    static final travelInfo
    static phoneNumber = '//js-track-call'
    static email = '//js-track-email'
    static card = '//div[@class="catalog-card__details"]'
    static name = '//div[@class="catalog-card__title"]' //class
    static reviewNumber = '//div[@class="reviews__number"]' //class
    static reviewPercent = '//div[@class-"recommended__percent"]' //class
    //static List<WebElement> viewContact //list of contacts

    private WebDriver ff

    @Before
    public void setup(){
        ff = new FirefoxDriver()
    }

    @After
    public void closeFFContext(){
        ff.quit()
    }

    //smoke test
    @Test
    public void getHomepageHeader(){
        VirtuosoPage vPage = new VirtuosoPage(ff)
        assert vPage.getPageHeader() == 'Advisor Catalog'
    }

    @Test
    public void getName(){
        VirtuosoPage vPage = new VirtuosoPage(ff)
        vPage.getContactPeople()
    }

    @Test
    public void gatherAllInfo(){
        VirtuosoPage vPage = new VirtuosoPage(ff)
        vPage.gatherInfo()
    }

}
