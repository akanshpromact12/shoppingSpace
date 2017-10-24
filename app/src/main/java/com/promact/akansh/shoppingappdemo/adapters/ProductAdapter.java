package com.promact.akansh.shoppingappdemo.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.promact.akansh.shoppingappdemo.HomeModule.HomePresenter;
import com.promact.akansh.shoppingappdemo.Model.Product;
import com.promact.akansh.shoppingappdemo.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private ArrayList<Product> products = new ArrayList<>();
    private HomePresenter presenter;

    public ProductAdapter(Context context, ArrayList<Product> products, HomePresenter presenter) {
        this.context = context;
        this.products = products;
        this.presenter = presenter;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_info, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final Product product = products.get(position);

        holder.productImage.setImageResource(R.drawable.ic_bubble_in);
        holder.productName.setText(product.getProductName());
        float d = (float) Double.parseDouble(product.getProductDesc());
        holder.productRating.setRating(d);
        holder.productRating.setIsIndicator(true);
        holder.productRating.setNumStars(5);
        holder.productPrice.setText("" + product.getSellingPrice());
        holder.optionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.optionsView);
                popupMenu.inflate(R.menu.options_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.optionUpdateProd:
                                presenter.updtProducts(context, product);
                                break;
                            case R.id.optionDeleteProd:
                                AlertDialog.Builder alertForDelete = new AlertDialog
                                        .Builder(context);
                                alertForDelete.setTitle("Permanently delete product");
                                alertForDelete.setMessage("Are you sure you want to delete" +
                                        " the product?");
                                alertForDelete.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final ProgressDialog progressDialog = ProgressDialog
                                                    .show(context, "Please wait...",
                                                    "Deletion in progress", true);
                                            progressDialog.setCancelable(true);

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    products.remove(position);
                                                    presenter.delProducts(context, product);

                                                    progressDialog.dismiss();
                                                    //notifyDataSetChanged();
                                                }
                                            }).start();
                                        }
                                    });
                                alertForDelete.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                alertForDelete.show();
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        RatingBar productRating;
        ImageView optionsView;

        ProductViewHolder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            productRating = (RatingBar) itemView.findViewById(R.id.productRating);
            optionsView = (ImageView) itemView.findViewById(R.id.optionsView);
        }
    }
}
