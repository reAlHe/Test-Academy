export default class BasePage {

    /**
     * Opens page
     */
    open(path: string) {
        browser.url(path);
    }
}