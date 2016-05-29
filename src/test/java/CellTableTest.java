import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.concurrent.TimeUnit;


public class CellTableTest {
    static final int MAX_PAGES_NUMBER=10000;
    static WebDriver driver;
    final static String url = "http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable";
    static CellTablePage page;




    @BeforeClass
    public static void openBrowser() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);
        page=new CellTablePage(driver);
    }

    @Test
    public void WebElementsExist(){
        try{
            page.WebElementsExist();
        }catch(NoSuchElementException e){
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void ChangeCategoryTest(){
        String firstNames[]=new String[5];
        String lastNames[]=new String[5];
        String addresses[]=new String[5];
        String newContactCategory []={"Family","Friends","Coworkers","Contacts","Businesses"};

        //changing first 5 elements data
        for (int i=0;i<5;i++) {
            firstNames[i]=page.GetRowFieldText(i+1, CellTablePage.RowField.FirstName);
            lastNames[i]=page.GetRowFieldText(i+1, CellTablePage.RowField.LastName);
            addresses[i]=page.GetRowFieldText(i+1, CellTablePage.RowField.Address);
            page.SetRowCategory(i+1,newContactCategory[i]);
        }

        //checking if categories are new and other fields haven't been changed
        for (int i=0;i<5;i++) {
            Assert.assertEquals(page.GetRowFieldText(i+1,CellTablePage.RowField.FirstName),firstNames[i]);
            Assert.assertEquals(page.GetRowFieldText(i+1, CellTablePage.RowField.LastName),lastNames[i]);
            Assert.assertEquals(page.GetRowFieldText(i+1, CellTablePage.RowField.Category),newContactCategory[i]);
            Assert.assertEquals(page.GetRowFieldText(i+1, CellTablePage.RowField.Address),addresses[i]);
        }
    }
    private String [][] GetPageContacts(){
        int size=page.GetPageTableSize();
        String pageData[][]=new String[4][size];
        for (int i=0;i<size;i++){
            pageData[0][i]=page.GetRowFieldText(i+1, CellTablePage.RowField.FirstName);
            pageData[1][i]=page.GetRowFieldText(i+1, CellTablePage.RowField.LastName);
            pageData[2][i]=page.GetRowFieldText(i+1, CellTablePage.RowField.Category);
            pageData[3][i]=page.GetRowFieldText(i+1, CellTablePage.RowField.Address);
        }
        return pageData;
    }

    @Test
    public void PageButtonsTest(){

        if (page.IsButtonEnabled(CellTablePage.PageButton.FirstPage))
            page.PutButton(CellTablePage.PageButton.FirstPage);

        String firstPageData[][]=GetPageContacts();

        //go to last page using "next page"
        for (int i=0; ;i++){
            if (page.IsButtonEnabled(CellTablePage.PageButton.NextPage))
                page.PutButton(CellTablePage.PageButton.NextPage);
            else break;
            if (i>=MAX_PAGES_NUMBER){
                Assert.fail("Table can not contain more then "+MAX_PAGES_NUMBER+" pages");
                break;
            }
        }
        // remember last page contacts
        String lastPageData[][]=GetPageContacts();


        //go to first page using "previous page"
        for (int i=0; ;i++){
            if (page.IsButtonEnabled(CellTablePage.PageButton.PreviousPage))
                page.PutButton(CellTablePage.PageButton.PreviousPage);
            else break;
            if (i>=MAX_PAGES_NUMBER){
                Assert.fail("Table can not contain more then "+MAX_PAGES_NUMBER+" pages");
                break;
            }
        }

        //check if contacts in the first page haven't been changed
        String firstPageData2[][]=GetPageContacts();
        Assert.assertArrayEquals(firstPageData,firstPageData2);


        //go to last page using "last page"
        page.PutButton(CellTablePage.PageButton.LastPage);
        //check if contacts in the last page haven't been changed
        String lastPageData2[][]=GetPageContacts();
        Assert.assertArrayEquals(lastPageData,lastPageData2);

        //go to first page using "first page"
        page.PutButton(CellTablePage.PageButton.FirstPage);
        String lastPageData3[][]=GetPageContacts();
        Assert.assertArrayEquals(lastPageData,lastPageData3);
    }

    public int[] GetIntFromSubscription(String subscription){
        int outInts[]=new int[3];
        subscription=subscription.replaceAll("[^0-9]+"," ");
        String outStrings[]=(subscription.split(" ",3));
        outInts[0]=Integer.parseInt(outStrings[0]);
        outInts[1]=Integer.parseInt(outStrings[1]);
        outInts[2]=Integer.parseInt(outStrings[2]);
        return outInts;
    }
    @Test
    public void TableSubscriptionTest(){
        if (page.IsButtonEnabled(CellTablePage.PageButton.FirstPage))
            page.PutButton(CellTablePage.PageButton.FirstPage);


        int firstContactNumber=1, subscriptionData[], size=0;
        String subscription;
        for (int i=0; ;i++) {
            subscription=page.GetSubscription();
            subscriptionData=GetIntFromSubscription(subscription);

            //check if first subscription int is correct
            Assert.assertEquals(firstContactNumber,subscriptionData[0]);

            //check if last subscription int is correct
            size=page.GetPageTableSize();
            Assert.assertEquals(firstContactNumber+size-1,subscriptionData[1]);

            //click next page button if it is enabled
            if (page.IsButtonEnabled(CellTablePage.PageButton.NextPage))
                page.PutButton(CellTablePage.PageButton.NextPage);
            //else check if total number of contacts is correct
            else {
                Assert.assertEquals(firstContactNumber+size-1,subscriptionData[2]);
                break;
            }

            if (i>=MAX_PAGES_NUMBER){
                Assert.fail("Table can not contain more then "+MAX_PAGES_NUMBER+" pages");
                break;
            }

            firstContactNumber+=size;
        }
    }

    @AfterClass
    public static void CloseBrowser(){
        driver.close();
    }

}
