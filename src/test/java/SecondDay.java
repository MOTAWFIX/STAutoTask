import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

public class SecondDay {

    WebDriver D1;

    @Test
    public void OpenBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        D1 = new ChromeDriver(options);
        navigateto("https://storefront-consumer-frontend-store-front-dev.skynodes.dev/");
        windowmax();
        
    }


    public void navigateto(String url) {
        D1.navigate().to(url);
    }

    //public void backto() {D1.navigate().back();}
    //public void forwardto() {D1.navigate().forward();}
    //public void refresh() {D1.navigate().refresh();}

    public void windowmax() {
        D1.manage().window().maximize();
    }

    public void close() {
        D1.close();
    }
}
