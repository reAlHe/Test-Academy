import BasePage from './base.page';

class ProductDetails extends BasePage {

    get price() { return $('#our_price_display') }
    get addToCartBtn() {return $('#add_to_cart')}
    get proceedToCheckoutBtn() {return $('a[title="Proceed to checkout"]')}

    open() {
        super.open('http://www.automationinpractice.com');
    }

    addToCart() {
        this.addToCartBtn.click()
    }

    viewPrice() {
        return this.price.getText()
    }

    proceedToCheckout() {
        browser.waitUntil(() => this.proceedToCheckoutBtn.isClickable())
        this.proceedToCheckoutBtn.moveTo()
        this.proceedToCheckoutBtn.click()
    }
}

export default new ProductDetails();