package test.cucumber.selenium.phantomjs;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GEDO_Ingreso_Firma_Simple_PhantomJS {
	private WebDriver driver = null;
	private StringBuffer verificationErrors = new StringBuffer();
	WebDriverWait wait;

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				"C:/phantomjs-2.1.1-windows/bin/phantomjs.exe");
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] { "--web-security=no",
				"--ignore-ssl-errors=yes", "--ignore-ssl-errors=true", "--ssl-protocol=TLSv1" });

		try {
			driver = new PhantomJSDriver(caps);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			System.out.println("Inicialización Driver PhantomJS: SIN ERRORES");
		} catch (Exception e) {
			System.out.println("Inicialización Driver PhantomJS: ERROR");
			throw e;
		}
	}

	@Given("^El usuario cuenta cuanto mínimo una tarea de firma en su bandeja$")
	public void el_usuario_cuenta_cuanto_minimo_una_tarea_de_firma_en_su_bandeja() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new Exception();
	}

	@When("^Ingresa a la URL \"(.*)\"$")
	public void ingresoCAS(String url) throws Exception {
		driver.navigate().to(url);
		wait = new WebDriverWait(driver, 20);
	}

	@When("^El usuario ingresa al sistema con sus credenciales: usuario \"(.*)\" y password \"(.*)\"$")
	public void datosLogin(String usuario, String password) throws Exception {
		driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div/div/div/div/div[2]/div/input")).clear();
		driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div/div/div/div/div[2]/div/input")).sendKeys(usuario);
		driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div/div/div/div/div[3]/div/input")).clear();
		driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div/div/div/div/div[3]/div/input"))
				.sendKeys(password);
		driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div/div/div/div/div[4]/button")).click();
	}

	@When("^El usuario firma una tarea de firma simple con certificado$")
	public void testGEDOIngresoFirmaSimple() throws Exception {
		wait = new WebDriverWait(driver, 30);
		Thread.sleep(3000);
		waitElementIsPresentByXpath("//td/img");
		driver.findElement(By.xpath("//td/img")).click();
		waitElementIsPresentByXpath("//td[5]/div/div/div/img");
		driver.findElement(By.xpath("//td[5]/div/div/div/img")).click();
		/*
		 * Cerrar cartel de firma de documento y volver a la bandeja de inicio
		 */
		waitElementIsPresentByXpath("//td[2]/div/div/div/div/img");
		driver.findElement(By.xpath("//td[2]/div/div/div/div/img")).click();

	}

	/**
	 * @param xpath
	 * @throws Exception
	 *             Espera a que un elemento aparezca Busca el mismo mediante su
	 *             xpath
	 */
	private void waitElementIsPresentByXpath(String xpath) throws Exception {
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if (isElementPresent(By.xpath(xpath)))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(2000);
		}
	}

	@Then("^Se genera un número de documento GDE$")
	public void se_genera_un_numero_de_documento_GDE() throws Exception {
		String mensajeGeneracionDocumento = driver
				.findElement(By
						.xpath("/html/body/div[3]/div[2]/div/div/div/table/tbody/tr/td/table/tbody//tr[1]/td/table/tbody/tr/td/table/tbody/tr/td/span"))
				.getText();
		System.out.println("Mensaje de confirmación: " + mensajeGeneracionDocumento);
		/**
		 * TODO: Generar validación de mensaje de documento generado
		 */
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
