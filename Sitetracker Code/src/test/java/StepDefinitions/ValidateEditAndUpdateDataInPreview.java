package StepDefinitions;

import Enums.ObjectRepository.Identfier;
import Enums.UrlEnum;
import Selenium.SeleniumUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ValidateEditAndUpdateDataInPreview extends SeleniumUtils {

	@Given("the application URL and browser")
	public void the_application_url_and_browser(DataTable dataTable) {
		String browser = dataTable.asMaps().get(0).get("browser");
		String appUrl = UrlEnum.urls.BASE_URL.getLabel();
		startup(browser, appUrl);
	}

	@Given("Click on Tab \"Component References")
	public void click_on_tab_component_references() {
		click(Identfier.COMPONENT_REFERENCE.getLabel());
	}

	@When("User search {string} in quickfind")
	public void user_search_in_quickfind(String string) {
		selectFromTypeAhead(Identfier.QUICK_FIND.getLabel(), Identfier.DATATABLE.getLabel(), string);
	}

	@When("select {string} from the dropdown")
	public void select_from_the_dropdown(String string) {
		selectFromDropDown(Identfier.EXAMPLE_DROP_ICON.getLabel(), Identfier.DROPDOWNVAL.getLabel(), string);
	}

	@When("Click on the run button")
	public void click_on_the_run_button() {
		click(Identfier.RUNBUTTON.getLabel());
	}

	@When("under the section {string} update the values for all rows in colum {int}")
	public void under_the_section_update_the_values_for_all_rows_in_colum(String string, Integer int1,
			DataTable dataTable) {
		UpdateTable(Identfier.TABLE_DATA.getLabel(), int1, dataTable);
	}

	@Then("Validate the data has been updated for given {int}")
	public void validate_the_data_has_been_updated(DataTable data, Integer int1) {
		validateTableData(Identfier.TABLE_DATA.getLabel(), data, int1);
	}

}
