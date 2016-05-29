import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CellListTest {
    static final int NUMBER_OF_CHECKS=10;
    static WebDriver driver;
    static CellListPage page;
    final static String url = "http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList";


    @BeforeClass
    public static void openBrowser() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);
        page=new CellListPage(driver);
    }

    @Test
    public void WebElementsExistTest()    {
        try {
            page.WebElementsExist();
        } catch (NoSuchElementException e){
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void AddContactTest(){
        String
                newContactFirstName="Ivan",
                newContactLastName="Ivanov",
                newContactCategory="Coworkers",
                newContactBirthdate="July 20, 1993",
                newContactAddress="18 Lenin str.";

        //filling the form and put "create contact"
        page.FillContactInfo(newContactFirstName,newContactLastName, newContactCategory,newContactBirthdate,newContactAddress);
        page.PutButton(CellListPage.PageButton.CreateContact);
        int listSize=page.GetContactListSize();


        //check if name in list equals to newContactFirstName+" "+newContactLastName
        Assert.assertEquals(newContactFirstName+" "+newContactLastName,
                page.GetContactItemText(listSize,CellListPage.ContactItemField.Name));

        //check if address in list equal to newContactAddress
        Assert.assertEquals(newContactAddress,
                page.GetContactItemText(listSize,CellListPage.ContactItemField.Address));

        //check if all data of last contact info is correct
        page.ContactItemClick(listSize);
        Assert.assertEquals(newContactFirstName, page.GetFormText(CellListPage.FormField.FirstName));
        Assert.assertEquals(newContactLastName, page.GetFormText(CellListPage.FormField.LastName));
        Assert.assertEquals(newContactCategory, page.GetFormText(CellListPage.FormField.Category));
        Assert.assertEquals(newContactBirthdate, page.GetFormText(CellListPage.FormField.Birthdate));
        Assert.assertEquals(newContactAddress, page.GetFormText(CellListPage.FormField.Address));
    }
    @Test
    public void ContactInfoDisplayTest(){
        int checksNumber=(page.GetContactListSize()>NUMBER_OF_CHECKS)? NUMBER_OF_CHECKS:page.GetContactListSize();
        String formName, formAddress;
        for (int i=0; i<checksNumber;i++){

            page.ContactItemClick(i+1);
            formName=page.GetFormText(CellListPage.FormField.FirstName)+" "+page.GetFormText(CellListPage.FormField.LastName);
            formAddress=page.GetFormText(CellListPage.FormField.Address);
            Assert.assertEquals(formName,page.GetContactItemText(i+1, CellListPage.ContactItemField.Name));
            Assert.assertEquals(formAddress,page.GetContactItemText(i+1, CellListPage.ContactItemField.Address));

        }
    }

    @Test
    public void UpdateContactTest(){
        String newContactLastName[]={"Ivanov","Petrov","Sidorov","Vasin","Vasina"};
        String newContactFirstName []={"Ivan","Pyotr","Sidor","Vasily","Vasilisa"};
        String newContactCategory []={"Family","Friends","Coworkers","Contacts","Businesses"};
        String newContactBirthdate[]={"June 21, 1950", "May 29, 1890","January 31, 1990","April 1, 1990","July 8, 1990"};
        String newContactAddress[]={"19 Lenin str.","20 Lenin str.","19 Lenin av.","33 Malyshev str.","4a Lenin str."};

        int updatesNumber=(page.GetContactListSize()>5)? 5:page.GetContactListSize();


        //update contacts
        for (int i=0;i<updatesNumber;i++) {

            page.ContactItemClick(i+1);
            page.FillContactInfo(newContactFirstName[i], newContactLastName[i], newContactCategory[i],
                    newContactBirthdate[i], newContactAddress[i]);
            page.PutButton(CellListPage.PageButton.UpdateContact);
        }

        //check this contacts
        for (int i=0;i<updatesNumber;i++){
            page.ContactItemClick(i+1);
            Assert.assertEquals(newContactFirstName[i], page.GetFormText(CellListPage.FormField.FirstName));
            Assert.assertEquals(newContactLastName[i], page.GetFormText(CellListPage.FormField.LastName));
            Assert.assertEquals(newContactCategory[i], page.GetFormText(CellListPage.FormField.Category));
            Assert.assertEquals(newContactBirthdate[i], page.GetFormText(CellListPage.FormField.Birthdate));
            Assert.assertEquals(newContactAddress[i], page.GetFormText(CellListPage.FormField.Address));
        }
    }
    @AfterClass
    public static void CloseBrowser(){
        driver.close();
    }
}
