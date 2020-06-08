import BasePage from './base.page';

class Cart extends BasePage {

    get firstItem() { return $('//*[@id="product_5_19_0_0"]/td[2]/small[1]') }

    open() {
        super.open('http://automationpractice.com/index.php?controller=order');
    }

    viewOrderedItemDetail() {
        return this.firstItem.getText()
    }
}

export default new Cart();