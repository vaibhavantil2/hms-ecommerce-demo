/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.huawei.industrydemo.shopping.viewadapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.industrydemo.shopping.R;
import com.huawei.industrydemo.shopping.constants.KeyConstants;
import com.huawei.industrydemo.shopping.entity.Product;
import com.huawei.industrydemo.shopping.entity.ShoppingCart;
import com.huawei.industrydemo.shopping.inteface.OnItemModifyListener;
import com.huawei.industrydemo.shopping.page.ProductActivity;

import java.util.List;

/**
 * ShoppingCart Adapter
 * 
 * @version [Ecommerce-Demo 1.0.0.300, 2020/9/22]
 * @see com.huawei.industrydemo.shopping.fragment.ShopCarFragment
 * @since [Ecommerce-Demo 1.0.0.300]
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private static final String TAG = ShoppingCartAdapter.class.getSimpleName();
    private List<ShoppingCart> shoppingCartList;
    private Context context;
    private OnItemModifyListener onItemModifyListener;
    
    public ShoppingCartAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemModifyListener(OnItemModifyListener onItemModifyListener) {
        this.onItemModifyListener = onItemModifyListener;
    }

    public void setShoppingCartList(List<ShoppingCart> shoppingCartList) {
        this.shoppingCartList = shoppingCartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoppingcart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCart shoppingCart = shoppingCartList.get(position);
        Product product = shoppingCart.getProduct();
        holder.imageThumbnail.setImageResource(
            context.getResources().getIdentifier(product.getImages()[0], "mipmap", context.getPackageName()));
        holder.textProductName.setText(product.getBasicInfo().getShortName());
        holder.textConfiguration.setText(product.getBasicInfo().getConfiguration().toString());
        holder.textPrice.setText(context.getString(R.string.shopping_cart_price, product.getBasicInfo().getPrice()));
        holder.textQuantity.setText(String.valueOf(shoppingCart.getQuantity()));
        holder.checkItemChoose.setChecked(shoppingCart.isChoosed());

        holder.textAdd.setOnClickListener(v -> {
            onItemModifyListener.onItemQuantityAdd(position, holder.textQuantity);
        });

        holder.textReduce.setOnClickListener(v -> {
            onItemModifyListener.onItemQuantityReduce(position, holder.textQuantity);
        });

        holder.checkItemChoose.setOnClickListener(view -> {
            shoppingCart.setChoosed(((CheckBox) view).isChecked());
            onItemModifyListener.onItemChoose(position, ((CheckBox) view).isChecked());
        });
        holder.itemView.setOnClickListener(view -> {
            shoppingCart.setChoosed(!((CheckBox) view.findViewById(R.id.ck_chose)).isChecked());
            onItemModifyListener.onItemChoose(position, !((CheckBox) view.findViewById(R.id.ck_chose)).isChecked());
        });

        View.OnClickListener redirectToProductDetail = view -> {
            Log.d(TAG, "redirectToProductDetail");
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra(KeyConstants.PRODUCT_KEY, product.getNumber());
            context.startActivity(intent);
        };
        holder.textProductName.setOnClickListener(redirectToProductDetail);
        holder.textConfiguration.setOnClickListener(redirectToProductDetail);
        holder.imageThumbnail.setOnClickListener(redirectToProductDetail);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return shoppingCartList == null ? 0 : shoppingCartList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumbnail;
        TextView textProductName;
        TextView textConfiguration;
        TextView textPrice;
        TextView textQuantity;
        TextView textReduce;
        TextView textAdd;
        CheckBox checkItemChoose;

        ViewHolder(View itemView) {
            super(itemView);
            checkItemChoose = itemView.findViewById(R.id.ck_chose);
            imageThumbnail = itemView.findViewById(R.id.iv_show_pic);
            textReduce = itemView.findViewById(R.id.iv_sub);
            textAdd = itemView.findViewById(R.id.iv_add);

            textProductName = itemView.findViewById(R.id.tv_product_name);
            textConfiguration = itemView.findViewById(R.id.tv_configuration);
            textPrice = itemView.findViewById(R.id.tv_price);
            textQuantity = itemView.findViewById(R.id.tv_show_quantity);
        }

    }
}
