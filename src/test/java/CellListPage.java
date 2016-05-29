import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Created by Aspenson on 28.02.2016.
 */
public class CellListPage {
    static final int MAX_CONTACTS_NUMBER=10000;
    By firstNameBy=By.xpath("(//input[@class='gwt-TextBox'])[1]");
    By lastNameBy = By.xpath("(//input[@class='gwt-TextBox'])[2]");
    By categoryBy =By.xpath("//select[@class='gwt-ListBox']");
    By birthdateBy = By.xpath("//input[@class='gwt-DateBox']");
    By addressBy = By.xpath("//textarea[@class='gwt-TextArea']");
    By createButtonBy=By.xpath("//button[text() = 'Create Contact']");
    By updateButtonBy=By.xpath("//button[text() = 'Update Contact']");
    By contactListBy=By.xpath("//div[@class='GNHGC04CGB']/div/div");
    By contactNameBy=By.xpath(".//table/tbody/tr/td[2]");
    By contactAddressBy=By.xpath(".//table/tbody/tr[2]/td");
    WebDriver driver;
    WebElement firstName, lastName, category, birthdate, address,contact;
    WebElement createContactButton, updateContactButton;
    List<WebElement> contacts;

    public enum PageButton {CreateContact,UpdateContact}
    public enum FormField{FirstName, LastName, Category,Birthdate,Address}
    public enum ContactItemField{Name,Address}

    static By ContactItemBy(int index){
        return By.xpath("//div[@class='GNHGC04CGB']/div/div["+index+"]");
    }

    public CellListPage(WebDriver driver){
        this.driver=driver;

    }

    private void FindWebElements() {
        firstName = driver.findElement(firstNameBy);
        lastName = driver.findElement(lastNameBy);
        category = driver.findElement(categoryBy);
        birthdate = driver.findElement(birthdateBy);
        address = driver.findElement(addressBy);
    }

    public void WebElementsExist(){
        this.FindWebElements();
        ContactItemBy(1);
        createContactButton=driver.findElement(createButtonBy);
        updateContactButton=driver.findElement(updateButtonBy);

    }


    public void FillContactInfo( String newFirstName,  String newLastName, String newCategory,
                                         String newBirthdate, String newAddress ){
        this.FindWebElements();
        if (firstName.getAttribute("value")!="") firstName.clear();
        firstName.sendKeys(newFirstName);

        if (lastName.getAttribute("value")!="") lastName.clear();
        lastName.sendKeys(newLastName);

        Select categorySelect = new Select(category);
        categorySelect.selectByValue(newCategory);

        if (birthdate.getAttribute("value")!="") birthdate.clear();
        birthdate.sendKeys(newBirthdate);
        birthdate.sendKeys(Keys.ENTER);

        if (address.getAttribute("value")!="") address.clear();
        address.sendKeys(newAddress);
    }
    public void PutButton(PageButton button){
        switch (button){
            case CreateContact:
                createContactButton=driver.findElement(createButtonBy);
                createContactButton.click();
                break;
            case UpdateContact:
                updateContactButton=driver.findElement(updateButtonBy);
                updateContactButton.click();
        }
    }
    public String GetFormText(FormField field){
        String result;
        FindWebElements();
        switch(field){
            case FirstName:
                result=firstName.getAttribute("value");
                break;
            case LastName:
                result=lastName.getAttribute("value");
                break;
            case Category:
                result=category.getAttribute("value");
                break;
            case Birthdate:
                result=birthdate.getAttribute("value");
                break;
            case Address:
                result=address.getAttribute("value");
                break;

            default:
                result="";
        }
        return result;
    }

    private boolean FindContactItem(int index){

        if (index<=0)
            return false;
        contacts = driver.findElements(contactListBy);

        if (contacts.size()>=index){
            contact = driver.findElement(ContactItemBy(index));
            return true;
        }
        else{
            Actions actions  = new Actions(driver);
            int size=0;
            do{
                //contacts = driver.findElements(contactListBy);
                size = contacts.size();
                actions.moveToElement(contacts.get(contacts.size() - 4));
                actions.perform();
                contacts = driver.findElements(contactListBy);
                if (contacts.size()>=index) {
                    contact = driver.findElement(ContactItemBy(index));
                    return true;
                }
                if (contacts.size() == size)
                    break;
            } while(size<MAX_CONTACTS_NUMBER);
            return false;
        }
    }


    public void ContactItemClick(int index){
        if (this.FindContactItem(index))
            contact.click();
    }

    public int GetContactListSize(){
        Actions actions  = new Actions(driver);
        int size=0;
        do{
            contacts = driver.findElements(contactListBy);
            size = contacts.size();
            actions.moveToElement(contacts.get(contacts.size() - 4));
            actions.perform();
            contacts = driver.findElements(contactListBy);
            if (contacts.size()==size) {
                return size-2;
            }
        } while(size<MAX_CONTACTS_NUMBER);
        return MAX_CONTACTS_NUMBER;
    }

    public String GetContactItemText(int index,ContactItemField field){
        String result="";
        if (this.FindContactItem(index)){
            switch (field){
                case Name:
                    result=contact.findElement(contactNameBy).getText();
                    break;
                case Address:
                    result=contact.findElement(contactAddressBy).getText();
                    break;
            }
        }
        return result;
    }
}
