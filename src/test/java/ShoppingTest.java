import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class ShoppingTest {

    WebDriver driver;
    WebDriverWait wait;

    String url = "https://storefront-consumer-frontend-store-front-dev.skynodes.dev/";
    String email = "mohamed.tawfik@paysky.io";
    String password = "password";
    String product = "SATIN FLOW BEADED DRESS";

    @BeforeTest
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.navigate().to(url);
    }

    @Test(priority = 1)
    public void login() {
        driver.findElement(By.xpath("//button[.//span[text()='Sign-in']]")).click();

        // انتظر الفورم يظهر
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-email")));

        // أدخل الإيميل
        driver.findElement(By.id("login-email")).sendKeys(email);

        // أدخل الباسورد
        driver.findElement(By.id("login-password")).sendKeys(password);

        // اضغط Login
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // انتظر الـ home page
        wait.until(ExpectedConditions.urlToBe(url));
        System.out.println("✅ Login successful");
    }

    @Test(priority = 2, dependsOnMethods = "login")
    public void searchProduct() {
        // اضغط على الـ search bar واكتب اسم المنتج
        WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='What are you looking for?']")));
        searchBar.click();
        searchBar.sendKeys(product);
        searchBar.sendKeys(Keys.ENTER);

        // انتظر النتايج تظهر
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Pleated')]")));
        System.out.println("✅ Search done");
    }

    @Test(priority = 3, dependsOnMethods = "searchProduct")
    public void chooseProduct() {
        WebElement productCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(text(),'SATIN FLOW BEADED DRESS')]")));
        productCard.click();
        System.out.println("✅ Product page opened");
    }

    @Test(priority = 4, dependsOnMethods = "chooseProduct")
    public void addToCart() {
        // انتظر الصفحة تستقر الأول
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
        }

        // لاقي الزرار وضغط عليه
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'ADD TO CART')]"))).click();

        System.out.println("✅ Added to cart");
    }

    @Test(priority = 5, dependsOnMethods = "addToCart")
    public void openCartAndVerify() {
        // افتح الكارت
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@aria-label,'Cart')]"))).click();

        // تحقق إن المنتج موجود في الكارت
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(),'SATIN FLOW BEADED DRESS')]")));
        System.out.println("✅ Item verified in cart");
    }

    @Test(priority = 6, dependsOnMethods = "openCartAndVerify")
    public void moveToWishlist() {
        // اضغط MOVE TO WISHLIST في الكارت
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Move to Wishlist') or contains(.,'MOVE TO WISHLIST')]"))).click();
        System.out.println("✅ Moved to wishlist");
    }

    @Test(priority = 7, dependsOnMethods = "moveToWishlist")
    public void verifyWishlist() {
        // اضغط Wishlist في الـ navbar
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Wishlist']]"))).click();

        // تحقق إن المنتج موجود
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(),'SATIN FLOW BEADED DRESS')]")));
        System.out.println("✅ Item verified in wishlist");
    }

    @Test(priority = 8, dependsOnMethods = "verifyWishlist")
    public void proceedToCheckout() {
        // اضغط Cart في الـ navbar
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@aria-label,'Cart')]"))).click();

        // اضغط Proceed to Checkout
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@aria-label,'Proceed to checkout')]"))).click();

        System.out.println("✅ Proceeded to checkout");
    }

    @Test(priority = 9, dependsOnMethods = "proceedToCheckout")
    public void removeFromWishlistViaHeart() throws InterruptedException {
        // روح للـ home
        driver.navigate().to(url);

        // سيرش على المنتج
        WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='What are you looking for?']")));
        searchBar.click();
        searchBar.sendKeys(product);
        searchBar.sendKeys(Keys.ENTER);

        // اضغط على المنتج في النتايج
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[contains(text(),'SATIN FLOW BEADED DRESS')]"))).click();

        // انتظر الصفحة تستقر
        Thread.sleep(1500);

        // اضغط بـ JavaScript عشان الزرار hidden في desktop
        WebElement heartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[@aria-label='Remove from wishlist']")));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", heartBtn);

        System.out.println("✅ Removed from wishlist");
    }

    @Test(priority = 10, dependsOnMethods = "removeFromWishlistViaHeart")
    public void logout() {
        // اضغط My Account
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='My Account']]"))).click();

        // اضغط Logout
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='menuitem'][.//span[text()='Logout']]"))).click();

        System.out.println("✅ Logged out");
    }

}