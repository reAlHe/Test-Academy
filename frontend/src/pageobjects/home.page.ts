import BasePage from './base.page';

class Home extends BasePage {

    get search() { return $('#search_query_top') }
    get submitBtn() { return $('button[type="submit"]') }

    open() {
        super.open('http://automationpractice.com');
    }

    searchForItem(text: string) {
        this.search.setValue(text);
        this.submitBtn.click();
    }
}

export default new Home();