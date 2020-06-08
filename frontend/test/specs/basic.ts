import Home from '../../src/pageobjects/home.page'
import SearchResults from '../../src/pageobjects/searchresults.page'
import ProductDetails from '../../src/pageobjects/productdetails.page'
import Cart from '../../src/pageobjects/cart.page'

describe('ordering a dress', () => {
    it('should work correctly', () => {
        Home.open();
        Home.searchForItem('dress');
        SearchResults.viewDetailsForFirstItem();
        expect(ProductDetails.viewPrice()).toEqual('$28.98')
        ProductDetails.addToCart();
        ProductDetails.proceedToCheckout()
        expect(Cart.viewOrderedItemDetail()).toEqual('SKU : demo_5')
    })
})