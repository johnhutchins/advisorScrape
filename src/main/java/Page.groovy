import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions

class VirtuosoPage {

    static final BASE_URL = 'https://www.virtuoso.com/advisors#all'
    static final ADVISOR_CATALOG_PAGE_TITLE = 'catalog-page-title'
    static phoneNumber = '//a[@class="js-track-call"]'
    static email = '//a[@class="js-track-email"]'
    static card = '//div[@data-virtuosotest="advisorCompany"]'
    static location = '//span[@data-virtuosotest="advisorLocation"]'
    static cardEMailAndPhone = '//div[@data-virtuosotest="advisorContact"]'
    static name = '//h2[@class="catalog-card__title w-100"]' //class
    static reviewNumber = '//div[@class="reviews__number"]' //class
    static reviewPercent = '//div[@class-"recommended__percent"]' //class
    static CONTACT_INFO_BUTTON = "//button[text()='View Contact Info']"
    static NEXT_PAGE = "//a[@data-action='next']"

    private final WebDriver driver

    public VirtuosoPage(WebDriver driver){
        driver.get(BASE_URL)
        this.driver = driver
    }

    public String getPageHeader(){
        WebDriverWait wait = new WebDriverWait(driver, 5)
        WebElement s =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ADVISOR_CATALOG_PAGE_TITLE)))
        return s.getText()
    }

    public List<Contact> getContactPeople() {
        try {
            Map<Map<String, String>, Map<String, String>> objList = new HashMap<Map<String, String>, Map<String, String>>()
            WebDriverWait wait = new WebDriverWait(driver, 10)

            List<WebElement> names = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(name)))
            List<WebElement> locations = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(location)))

            List<WebElement> cardAndPhoneNumbers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CONTACT_INFO_BUTTON)))
            Map<String, String> contactMap = new HashMap<String, String>()
            Map<String, String> phoneEmail = new HashMap<String, String>()

            List<Contact> contacts = new ArrayList<Contact>()

            for (Integer i = 0; i < names.size(); i++) {
                WebElement clickThing = cardAndPhoneNumbers[i]
                clickThing.click()
                sleep(1000)

                try {
                    List<WebElement> email = driver.findElements(By.xpath(email))
                    List<WebElement> phone = driver.findElements(By.xpath(phoneNumber))

                    phoneEmail.put(email[i].getText(), phone[i].getText())
                    contactMap.put(names[i].getText(), locations[i].getText())

                    Contact c = new Contact(names[i].getText(), locations[i].getText(), email[i].getText(), phone[i].getText())
                    contacts.add(c)

                    objList.put(contactMap, phoneEmail)
                } catch (Exception e) {
                    System.out.println('Error on page: ' + i)
                    System.out.println('error: ' + e)
                }
            }
            return contacts
        } catch(Exception error){
            System.out.println("the error was : " + error)
        }
    }

    public def gatherInfo(){
        List<Contact> allPeople = new ArrayList<Contact>()
        for(Integer k=0;k<54;k++){
            List<Contact> cts = getContactPeople()
            allPeople.add(cts)
            sleep(2000)
            goToNextPage()
            sleep(2000)
        }
        //
        //System.out.println('all People == ' + allPeople)
        Gson gson = new Gson()
        JsonElement element = gson.toJsonTree(allPeople, new TypeToken<List<Contact>>() {}.getType())
        JsonArray jsonArray = element.getAsJsonArray();
        System.out.print('Json: '+jsonArray)

    }


    public void goToNextPage(){
        driver.findElement(By.xpath(NEXT_PAGE)).click()
    }




}