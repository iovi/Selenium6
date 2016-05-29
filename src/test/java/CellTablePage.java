import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Created by operator on 01.03.2016.
 */
public class CellTablePage {

    By nextPageButtonBy=By.xpath("//img[@aria-label='Next page']");
    By previousPageButtonBy=By.xpath("//img[@aria-label='Previous page']");
    By firstPageButtonBy=By.xpath("//img[@aria-label='First page']");
    By lastPageButtonBy=By.xpath("//img[@aria-label='Last page']");
    By tableBy=By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/tbody[not(contains(@style,'display: none'))]/tr");
    By subscriptionBy = By.xpath("//table/tbody/tr/td/div[@class='gwt-HTML']");
    WebDriver driver;
    WebElement firstName, lastName, category, address;

    public enum RowField{FirstName, LastName, Category, Address}
    public enum PageButton{NextPage,PreviousPage,FirstPage, LastPage}

    public CellTablePage(WebDriver driver){
        this.driver=driver;
    }

    private By RowFieldBy(int rownum, RowField field){
        By by=null;
        switch (field){
            case FirstName:
                by=By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/tbody/tr["+rownum+"]/td[2]");
                break;
            case LastName:
                by=By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/tbody/tr["+rownum+"]/td[3]");
                break;
            case Category:
                by=By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/tbody/tr["+rownum+"]/td[4]/div/select");
                break;
            case Address:
                by=By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/tbody/tr["+rownum+"]/td[5]");
                break;
        }
        return by;
    }
    private void FindRowElements(int rownum) {
        firstName = driver.findElement(RowFieldBy(rownum,RowField.FirstName));
        lastName = driver.findElement(RowFieldBy(rownum,RowField.LastName));
        category = driver.findElement(RowFieldBy(rownum,RowField.Category));
        address = driver.findElement(RowFieldBy(rownum,RowField.Address));
    }
    public String GetRowFieldText(int rownum, RowField field){
        String result="";
        FindRowElements(rownum);
        switch (field){
            case FirstName:
                result=firstName.getText();
                break;
            case LastName:
                result=lastName.getText();
                break;
            case Category:
                result=category.getAttribute("value");
                break;
            case Address:
                result=address.getText();
                break;
        }
        return result;
    }

    public void SetRowCategory(int rownum, String categoryName){
        FindRowElements(rownum);
        Select categorySelect = new Select(category);
        categorySelect.selectByValue(categoryName);
    }
    public void PutButton(PageButton buttonType){
        WebElement button=null;
        switch(buttonType){
            case FirstPage:
                button=driver.findElement(firstPageButtonBy);
                break;
            case LastPage:
                button=driver.findElement(lastPageButtonBy);
                break;
            case NextPage:
                button=driver.findElement(nextPageButtonBy);
                break;
            case PreviousPage:
                button=driver.findElement(previousPageButtonBy);
                break;
        }
        button.click();
    }
    public boolean IsButtonEnabled(PageButton buttonType){
        WebElement button=null;
        switch(buttonType){
            case FirstPage:
                button=driver.findElement(firstPageButtonBy);
                break;
            case LastPage:
                button=driver.findElement(lastPageButtonBy);
                break;
            case NextPage:
                button=driver.findElement(nextPageButtonBy);
                break;
            case PreviousPage:
                button=driver.findElement(previousPageButtonBy);
                break;
        }
        if (button.getAttribute("aria-disabled").equals("false"))
            return true;
        else
            return false;
    }

    public int GetPageTableSize(){
        List<WebElement> table=driver.findElements(tableBy);
        return table.size();
    }

    public String GetSubscription(){
        WebElement subscription =driver.findElement(subscriptionBy);
        return subscription.getText();
    }
    public void WebElementsExist(){

        //head of the table
        driver.findElement(By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/thead/tr/th[text() = 'First Name']"));
        driver.findElement(By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/thead/tr/th[text() = 'Last Name']"));
        driver.findElement(By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/thead/tr/th[text() = 'Category']"));
        driver.findElement(By.xpath("//table[@class='GNHGC04CIE GNHGC04CKJ']/thead/tr/th[text() = 'Address']"));

        //first row
        this.FindRowElements(1);

        //change page buttons
        driver.findElement(nextPageButtonBy);
        driver.findElement(lastPageButtonBy);
        driver.findElement(previousPageButtonBy);
        driver.findElement(firstPageButtonBy);

        //subscription
        driver.findElement(subscriptionBy);
    }
}
