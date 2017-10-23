package com.promact.akansh.shoppingappdemo.DeleteProduct;

import com.promact.akansh.shoppingappdemo.Model.Product;

class DeleteProductContract {
    interface deleteView{
        void showMessage(String msg);
    }

    interface deletePresenter {
        void deleteProduct(Product product);
    }
}
