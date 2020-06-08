import BasePage from './base.page';

class SearchResults extends BasePage {

    get firstItem() { return $('//*[@id="center_column"]/ul/li[1]') }

    open() {
        super.open('http://www.automationinpractice.com');
    }

    viewDetailsForFirstItem() {
        this.firstItem.click()
    }
}

export default new SearchResults();