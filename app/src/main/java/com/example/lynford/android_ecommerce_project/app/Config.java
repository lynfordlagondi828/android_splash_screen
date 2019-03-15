package com.example.lynford.android_ecommerce_project.app;

public class Config {

    public static final String BASE_URL = "http://192.168.254.103/ford_php/rest_api/PayPalServer/v1";

    public static final String GET_ALL_PRODUCTS = BASE_URL + "/get_all_products";
    public static final String GET_PRODUCT_DETAILS = BASE_URL + "/get_product_details/_ID_";
    public static final String ADD_TO_CART = BASE_URL + "/add_to_cart";
    public static final String GET_ALL_CART = BASE_URL + "/get_all_cart";

    /**
     * confirm order
     */
    public static final String CONFIRM_ORDER = BASE_URL + "/confirm_order";

    /**
     * update cart status if order successful
     *
     */
    public static final String UPDATE_CART_STATUS = BASE_URL + "/update_cart_status";

}
